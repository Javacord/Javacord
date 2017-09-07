package de.btobastian.javacord;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to login to a Discord account.
 */
public class DiscordApiBuilder {

    /**
     * The token which is used to login. Must be present in order to login!
     */
    private String token = null;

    /**
     * The account type of the account with the given token.
     */
    private AccountType accountType = AccountType.BOT;

    /**
     * Whether lazy loading should be enabled or not.
     */
    private boolean lazyLoading = false;

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
        CompletableFuture<DiscordApi> future = new CompletableFuture<>();
        if (token == null) {
            future.completeExceptionally(new IllegalArgumentException("You cannot login without a token!"));
            return future;
        }
        // TODO login process
        future.complete(null);
        return future;
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
     * Sets whether the bot should use lazy loading or not.
     * Lazy loading means it doesn't load offline users on startup for large servers to improve performance.
     * A server is considered as large, if it has more than 250 members.
     * Lazy loading is disabled by default.
     *
     * @param enabled Whether lazy loading should be used or not.
     * @return The current instance in order to chain call methods.
     */
    public DiscordApiBuilder setLazyLoading(boolean enabled) {
        this.lazyLoading = enabled;
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
     * Sets server sharding.
     * Sharding allows you to split your bot into several independent instances.
     * A shard only contains a subset of a bot's server.
     *
     * @param currentShard The shard of this connection starting with <code>0</code>!
     * @param totalShards The total amount of shards. Sharding will be disabled if set to <code>1</code>.
     * @return The current instance in order to chain call methods.
     * @see <a href="https://discordapp.com/developers/docs/topics/gateway#sharding">API docs</a>
     */
    public DiscordApiBuilder setShard(int currentShard, int totalShards) {
        if (currentShard >= totalShards) {
            throw new IllegalArgumentException("currentShards cannot be greater or equal than totalShards!");
        }
        if (totalShards < 1) {
            throw new IllegalArgumentException("totalShards cannot be less than 1!");
        }
        this.currentShard = currentShard;
        this.totalShards = totalShards;
        return this;
    }

}
