package org.javacord.core;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.Logger;
import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.internal.DiscordApiBuilderDelegate;
import org.javacord.core.util.gateway.DiscordWebSocketAdapter;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.logging.PrivacyProtectionLogger;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import org.javacord.core.util.rest.RestRequestResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The implementation of {@link DiscordApiBuilderDelegate}.
 */
public class DiscordApiBuilderDelegateImpl implements DiscordApiBuilderDelegate {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordApiBuilderDelegateImpl.class);

    /**
     * The token which is used to login. Must be present in order to login!
     */
    private volatile String token = null;

    /**
     * The account type of the account with the given token.
     */
    private volatile AccountType accountType = AccountType.BOT;

    /**
     * The current shard starting with <code>0</code>.
     */
    private final AtomicInteger currentShard = new AtomicInteger();

    /**
     * The total amount of shards.
     * If the total amount is <code>1</code>, sharding will be disabled.
     */
    private final AtomicInteger totalShards = new AtomicInteger(1);

    /**
     * A retry attempt counter.
     */
    private final AtomicInteger retryAttempt = new AtomicInteger();

    /**
     * Whether Javacord should wait for all servers to become available on startup or not.
     */
    private volatile boolean waitForServersOnStartup = true;

    @Override
    public CompletableFuture<DiscordApi> login() {
        logger.debug("Creating shard {} of {}", currentShard.get() + 1, totalShards.get());
        CompletableFuture<DiscordApi> future = new CompletableFuture<>();
        if (token == null) {
            future.completeExceptionally(new IllegalArgumentException("You cannot login without a token!"));
            return future;
        }
        try (CloseableThreadContext.Instance closeableThreadContextInstance =
                     CloseableThreadContext.put("shard", Integer.toString(currentShard.get()))) {
            new DiscordApiImpl(
                    accountType, token, currentShard.get(), totalShards.get(), waitForServersOnStartup, future);
        }
        return future;
    }

    @Override
    public Collection<CompletableFuture<DiscordApi>> loginShards(int... shards) {
        Objects.requireNonNull(shards);
        if (shards.length == 0) {
            return Collections.emptyList();
        }
        if (Arrays.stream(shards).distinct().count() != shards.length) {
            throw new IllegalArgumentException("shards cannot be started multiple times!");
        }
        if (Arrays.stream(shards).max().orElseThrow(AssertionError::new) >= getTotalShards()) {
            throw new IllegalArgumentException("shard cannot be greater or equal than totalShards!");
        }
        if (Arrays.stream(shards).min().orElseThrow(AssertionError::new) < 0) {
            throw new IllegalArgumentException("shard cannot be less than 0!");
        }

        if (shards.length == getTotalShards()) {
            logger.info("Creating {} {}", getTotalShards(), (getTotalShards() == 1) ? "shard" : "shards");
        } else {
            logger.info("Creating {} out of {} shards ({})", shards.length, getTotalShards(), shards);
        }

        Collection<CompletableFuture<DiscordApi>> result = new ArrayList<>(shards.length);
        int currentShard = getCurrentShard();
        for (int shard : shards) {
            if (currentShard != 0) {
                CompletableFuture<DiscordApi> future = new CompletableFuture<>();
                future.completeExceptionally(new IllegalArgumentException(
                        "You cannot use loginShards or loginAllShards after setting the current shard!"));
                result.add(future);
                continue;
            }
            setCurrentShard(shard);
            result.add(login());
        }
        setCurrentShard(currentShard);
        return result;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
        PrivacyProtectionLogger.addPrivateData(token);
    }

    @Override
    public void setAccountType(AccountType type) {
        this.accountType = type;
    }

    @Override
    public void setTotalShards(int totalShards) {
        if (currentShard.get() >= totalShards) {
            throw new IllegalArgumentException("currentShard cannot be greater or equal than totalShards!");
        }
        if (totalShards < 1) {
            throw new IllegalArgumentException("totalShards cannot be less than 1!");
        }
        this.totalShards.set(totalShards);
    }

    @Override
    public void setCurrentShard(int currentShard) {
        if (currentShard >= totalShards.get()) {
            throw new IllegalArgumentException("currentShard cannot be greater or equal than totalShards!");
        }
        if (currentShard < 0) {
            throw new IllegalArgumentException("currentShard cannot be less than 0!");
        }
        this.currentShard.set(currentShard);
    }

    @Override
    public void setWaitForServersOnStartup(boolean waitForServersOnStartup) {
        this.waitForServersOnStartup = waitForServersOnStartup;
    }

    @Override
    public CompletableFuture<Void> setRecommendedTotalShards() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (token == null) {
            future.completeExceptionally(
                    new IllegalArgumentException("You cannot request the recommended total shards without a token!"));
        } else {
            retryAttempt.set(0);
            setRecommendedTotalShards(future);
        }
        return future;
    }

    private void setRecommendedTotalShards(CompletableFuture<Void> future) {
        DiscordApiImpl api = new DiscordApiImpl(token);
        RestRequest<JsonNode> botGatewayRequest = new RestRequest<>(api, RestMethod.GET, RestEndpoint.GATEWAY_BOT);
        botGatewayRequest
                .execute(RestRequestResult::getJsonBody)
                .thenAccept(resultJson -> {
                    DiscordWebSocketAdapter.setGateway(resultJson.get("url").asText());
                    setTotalShards(resultJson.get("shards").asInt());
                    retryAttempt.set(0);
                    future.complete(null);
                })
                .exceptionally(t -> {
                    int retryDelay = api.getReconnectDelay(retryAttempt.incrementAndGet());
                    logger.info("Retrying to get recommended total shards in {} seconds!", retryDelay);
                    api.getThreadPool().getScheduler().schedule(
                            () -> setRecommendedTotalShards(future), retryDelay, TimeUnit.SECONDS);
                    return null;
                })
                .whenComplete((nothing, throwable) -> api.disconnect());
    }

    @Override
    public int getTotalShards() {
        return totalShards.get();
    }

    @Override
    public int getCurrentShard() {
        return currentShard.get();
    }
}
