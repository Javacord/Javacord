package de.btobastian.javacord;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.utils.DiscordWebSocketAdapter;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import de.btobastian.javacord.utils.rest.RestRequestResult;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * This class is used to login to a Discord account.
 */
public class DiscordApiBuilder {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordApiBuilder.class);

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
     * Login to the account with the given token.
     *
     * @return A {@link CompletableFuture} which contains the DiscordApi.
     */
    public CompletableFuture<DiscordApi> login() {
        logger.debug("Creating shard {} of {}", currentShard, totalShards);
        CompletableFuture<DiscordApi> future = new CompletableFuture<>();
        if (token == null) {
            future.completeExceptionally(new IllegalArgumentException("You cannot login without a token!"));
            return future;
        }
        MDC.put("shard", Integer.toString(currentShard));
        new ImplDiscordApi(accountType, token, currentShard, totalShards, future);
        MDC.remove("shard");
        return future;
    }

    /**
     * Login all shards to the account with the given token.
     * It is invalid to call {@link #setCurrentShard(int)} with
     * anything but {@code 0} before calling this method.
     *
     * @return A collection of {@link CompletableFuture}s which contain the {@code DiscordApi}s for the shards.
     */
    public Collection<CompletableFuture<DiscordApi>> loginAllShards() {
        return loginShards(shard -> true);
    }

    /**
     * Login shards adhering to the given predicate to the account with the given token.
     * It is invalid to call {@link #setCurrentShard(int)} with
     * anything but {@code 0} before calling this method.
     *
     * @param shardsCondition The predicate for identifying shards to connect, starting with {@code 0}!
     * @return A collection of {@link CompletableFuture}s which contain the {@code DiscordApi}s for the shards.
     */
    public Collection<CompletableFuture<DiscordApi>> loginShards(IntPredicate shardsCondition) {
        return loginShards(IntStream.range(0, totalShards).filter(shardsCondition).toArray());
    }

    /**
     * Login given shards to the account with the given token.
     * It is invalid to call {@link #setCurrentShard(int)} with
     * anything but {@code 0} before calling this method.
     *
     * @param shards The shards to connect, starting with {@code 0}!
     * @return A collection of {@link CompletableFuture}s which contain the {@code DiscordApi}s for the shards.
     */
    public Collection<CompletableFuture<DiscordApi>> loginShards(int... shards) {
        Objects.requireNonNull(shards);
        if (shards.length == 0) {
            return Collections.emptyList();
        }
        if (Arrays.stream(shards).distinct().count() != shards.length) {
            throw new IllegalArgumentException("shards cannot be started multiple times!");
        }
        if (Arrays.stream(shards).max().orElseThrow(AssertionError::new) >= totalShards) {
            throw new IllegalArgumentException("shard cannot be greater or equal than totalShards!");
        }
        if (Arrays.stream(shards).min().orElseThrow(AssertionError::new) < 0) {
            throw new IllegalArgumentException("shard cannot be less than 0!");
        }

        if (shards.length == totalShards) {
            logger.info("Creating {} {}", totalShards, (totalShards == 1) ? "shard" : "shards");
        } else {
            logger.info("Creating {} out of {} shards ({})", shards.length, totalShards, shards);
        }

        Collection<CompletableFuture<DiscordApi>> result = new ArrayList<>(shards.length);
        int currentShard = this.currentShard;
        for (int shard : shards) {
            if (currentShard != 0) {
                CompletableFuture<DiscordApi> future = new CompletableFuture<>();
                future.completeExceptionally(new IllegalArgumentException("You cannot use loginShards or loginAllShards after setting the current shard!"));
                result.add(future);
                continue;
            }
            result.add(setCurrentShard(shard).login());
        }
        this.currentShard = currentShard;
        return result;
    }

    /**
     * Sets the token which is required for the login process.
     * A tutorial on how to get the token can be found in the
     * <a href="https://github.com/BtoBastian/Javacord/wiki">Javacord wiki</a>.
     *
     * @param token The token to set.
     * @return The current instance in order to chain call methods.
     */
    public DiscordApiBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    /**
     * Sets the account type.
     * By default the builder assumes that you want to login to a bot account.
     * Please notice, that public client bots are not allowed by Discord!
     *
     * @param type The account type.
     * @return The current instance in order to chain call methods.
     */
    public DiscordApiBuilder setAccountType(AccountType type) {
        this.accountType = type;
        return this;
    }

    /**
     * Sets total shards for server sharding.
     * Sharding allows you to split your bot into several independent instances.
     * A shard only handles a subset of a bot's servers.
     *
     * @param totalShards The total amount of shards. Sharding will be disabled if set to <code>1</code>.
     * @return The current instance in order to chain call methods.
     * @see <a href="https://discordapp.com/developers/docs/topics/gateway#sharding">API docs</a>
     */
    public DiscordApiBuilder setTotalShards(int totalShards) {
        if (currentShard >= totalShards) {
            throw new IllegalArgumentException("currentShard cannot be greater or equal than totalShards!");
        }
        if (totalShards < 1) {
            throw new IllegalArgumentException("totalShards cannot be less than 1!");
        }
        this.totalShards = totalShards;
        return this;
    }

    /**
     * Sets shard for server sharding.
     * Sharding allows you to split your bot into several independent instances.
     * A shard only handles a subset of a bot's servers.
     *
     * @param currentShard The shard of this connection starting with <code>0</code>!
     * @return The current instance in order to chain call methods.
     * @see <a href="https://discordapp.com/developers/docs/topics/gateway#sharding">API docs</a>
     */
    public DiscordApiBuilder setCurrentShard(int currentShard) {
        if (currentShard >= totalShards) {
            throw new IllegalArgumentException("currentShard cannot be greater or equal than totalShards!");
        }
        if (currentShard < 0) {
            throw new IllegalArgumentException("currentShard cannot be less than 0!");
        }
        this.currentShard = currentShard;
        return this;
    }

    /**
     * Retrieves the recommended shards count from the Discord API and sets it in this builder.
     * Sharding allows you to split your bot into several independent instances.
     * A shard only handles a subset of a bot's servers.
     *
     * @return The current instance in order to chain call methods.
     * @see <a href="https://discordapp.com/developers/docs/topics/gateway#sharding">API docs</a>
     */
    public CompletableFuture<DiscordApiBuilder> setRecommendedTotalShards() {
        CompletableFuture<DiscordApiBuilder> future = new CompletableFuture<>();
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
                    future.complete(DiscordApiBuilder.this);
                })
                .exceptionally(t -> {
                    future.completeExceptionally(t);
                    return null;
                })
                .whenComplete((nothing, throwable) -> botGatewayRequest.getApi().disconnect());

        return future;
    }
}
