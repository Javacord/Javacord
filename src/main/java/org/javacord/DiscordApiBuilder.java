package org.javacord;

import org.javacord.event.server.ServerBecomesAvailableEvent;
import org.javacord.util.FactoryBuilder;
import org.javacord.util.logging.LoggerUtil;
import org.slf4j.Logger;

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
     * The delegate used to create a {@link DiscordApi} instance.
     */
    private DiscordApiBuilderDelegate delegate = FactoryBuilder.createDiscordApiBuilderDelegate();

    /**
     * Login to the account with the given token.
     *
     * @return A {@link CompletableFuture} which contains the DiscordApi.
     */
    public CompletableFuture<DiscordApi> login() {
        return delegate.login();
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
        return loginShards(IntStream.range(0, delegate.getTotalShards()).filter(shardsCondition).toArray());
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
        if (Arrays.stream(shards).max().orElseThrow(AssertionError::new) >= delegate.getTotalShards()) {
            throw new IllegalArgumentException("shard cannot be greater or equal than totalShards!");
        }
        if (Arrays.stream(shards).min().orElseThrow(AssertionError::new) < 0) {
            throw new IllegalArgumentException("shard cannot be less than 0!");
        }

        if (shards.length == delegate.getTotalShards()) {
            logger.info(
                    "Creating {} {}", delegate.getTotalShards(), (delegate.getTotalShards() == 1) ? "shard" : "shards");
        } else {
            logger.info("Creating {} out of {} shards ({})", shards.length, delegate.getTotalShards(), shards);
        }

        Collection<CompletableFuture<DiscordApi>> result = new ArrayList<>(shards.length);
        int currentShard = delegate.getCurrentShard();
        for (int shard : shards) {
            if (currentShard != 0) {
                CompletableFuture<DiscordApi> future = new CompletableFuture<>();
                future.completeExceptionally(new IllegalArgumentException(
                        "You cannot use loginShards or loginAllShards after setting the current shard!"));
                result.add(future);
                continue;
            }
            result.add(setCurrentShard(shard).login());
        }
        delegate.setCurrentShard(currentShard);
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
        delegate.setToken(token);
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
        delegate.setAccountType(type);
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
        delegate.setTotalShards(totalShards);
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
        delegate.setCurrentShard(currentShard);
        return this;
    }

    /**
     * Sets if Javacord should wait for all servers to become available on startup.
     * If this is disabled the {@link DiscordApi#getServers()} method will return an empty collection directly after
     * logging in and fire {@link ServerBecomesAvailableEvent} events once they
     * become available. You can check the ids of unavailable servers using the
     * {@link DiscordApi#getUnavailableServers()} method.
     *
     * @param waitForServersOnStartup Whether Javacord should wait for all servers
     *                                to become available on startup or not.
     * @return The current instance in order to chain call methods.
     */
    public DiscordApiBuilder setWaitForServersOnStartup(boolean waitForServersOnStartup) {
        delegate.setWaitForServersOnStartup(waitForServersOnStartup);
        return this;
    }

    /**
     * Retrieves the recommended shards count from the Discord API and sets it in this builder.
     * Sharding allows you to split your bot into several independent instances.
     * A shard only handles a subset of a bot's servers.
     *
     * @return A future with the current api builder.
     * @see <a href="https://discordapp.com/developers/docs/topics/gateway#sharding">API docs</a>
     */
    public CompletableFuture<DiscordApiBuilder> setRecommendedTotalShards() {
        return delegate.setRecommendedTotalShards().thenCompose(nothing -> CompletableFuture.completedFuture(this));
    }
}
