package org.javacord;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.util.gateway.DiscordWebSocketAdapter;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;
import org.javacord.util.gateway.DiscordWebSocketAdapter;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;
import org.javacord.util.rest.RestRequestResult;
import org.slf4j.Logger;
import org.slf4j.MDC.MDCCloseable;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link DiscordApiFactory}.
 */
public class ImplDiscordApiFactory implements DiscordApiFactory {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplDiscordApiFactory.class);

    /**
     * The token which is used to login. Must be present in order to login!
     */
    private String token = null;

    /**
     * The account type of the account with the given token.
     */
    private AccountType accountType = AccountType.BOT;

    /**
     * The current shard starting with <code>0</code>.
     */
    private int currentShard = 0;

    /**
     * The total amount of shards.
     * If the total amount is <code>1</code>, sharding will be disabled.
     */
    private int totalShards = 1;

    /**
     * Whether Javacord should wait for all servers to become available on startup or not.
     */
    private boolean waitForServersOnStartup = true;

    @Override
    public CompletableFuture<DiscordApi> login() {
        logger.debug("Creating shard {} of {}", currentShard, totalShards);
        CompletableFuture<DiscordApi> future = new CompletableFuture<>();
        if (token == null) {
            future.completeExceptionally(new IllegalArgumentException("You cannot login without a token!"));
            return future;
        }
        try (MDCCloseable mdcCloseable = LoggerUtil.putCloseableToMdc("shard", Integer.toString(currentShard))){
            new ImplDiscordApi(accountType, token, currentShard, totalShards, waitForServersOnStartup, future);
        }
        return future;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void setAccountType(AccountType type) {
        this.accountType = type;
    }

    @Override
    public void setTotalShards(int totalShards) {
        if (currentShard >= totalShards) {
            throw new IllegalArgumentException("currentShard cannot be greater or equal than totalShards!");
        }
        if (totalShards < 1) {
            throw new IllegalArgumentException("totalShards cannot be less than 1!");
        }
        this.totalShards = totalShards;
    }

    @Override
    public void setCurrentShard(int currentShard) {
        if (currentShard >= totalShards) {
            throw new IllegalArgumentException("currentShard cannot be greater or equal than totalShards!");
        }
        if (currentShard < 0) {
            throw new IllegalArgumentException("currentShard cannot be less than 0!");
        }
        this.currentShard = currentShard;
    }

    @Override
    public void setWaitForServersOnStartup(boolean waitForServersOnStartup) {
        this.waitForServersOnStartup = waitForServersOnStartup;
    }

    @Override
    public CompletableFuture<ImplDiscordApiFactory> setRecommendedTotalShards() {
        CompletableFuture<ImplDiscordApiFactory> future = new CompletableFuture<>();
        if (token == null) {
            future.completeExceptionally(new IllegalArgumentException("You cannot request the recommended total shards without a token!"));
            return future;
        }

        RestRequest<JsonNode> botGatewayRequest = new RestRequest<>(new ImplDiscordApi(token), RestMethod.GET, RestEndpoint.GATEWAY_BOT);
        botGatewayRequest
                .execute(RestRequestResult::getJsonBody)
                .thenAccept(resultJson -> {
                    DiscordWebSocketAdapter.setGateway(resultJson.get("url").asText());
                    setTotalShards(resultJson.get("shards").asInt());
                    future.complete(ImplDiscordApiFactory.this);
                })
                .exceptionally(t -> {
                    future.completeExceptionally(t);
                    return null;
                })
                .whenComplete((nothing, throwable) -> botGatewayRequest.getApi().disconnect());

        return future;
    }

    @Override
    public int getTotalShards() {
        return totalShards;
    }

    @Override
    public int getCurrentShard() {
        return currentShard;
    }
}
