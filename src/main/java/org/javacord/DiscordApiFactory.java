package org.javacord;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used by the {@link DiscordApiBuilder} internally.
 * You probably won't need it ever.
 */
public interface DiscordApiFactory {

    /**
     * Sets the token.
     *
     * @param token The token to set.
     */
    void setToken(String token);

    /**
     * Sets the account type.
     *
     * @param accountType The account type to set.
     */
    void setAccountType(AccountType accountType);

    /**
     * Sets the total shards.
     *
     * @param totalShards The total shards to set.
     * @see DiscordApiBuilder#setTotalShards(int)
     */
    void setTotalShards(int totalShards);

    /**
     * Sets the current shards.
     *
     * @param currentShard The current shards to set.
     * @see DiscordApiBuilder#setCurrentShard(int)
     */
    void setCurrentShard(int currentShard);

    /**
     * Sets the wait for servers on startup flag.
     *
     * @param waitForServersOnStartup The wait for servers on startup flag to set.
     */
    void setWaitForServersOnStartup(boolean waitForServersOnStartup);

    /**
     * Logs the bot in.
     *
     * @return The discord api instance.
     */
    CompletableFuture<DiscordApi> login();

    /**
     * Sets the recommended total shards.
     *
     * @return A future to check if the action was successful.
     */
    CompletableFuture<ImplDiscordApiFactory> setRecommendedTotalShards();

    /**
     * Gets the total shards.
     *
     * @return The total shards.
     */
    int getTotalShards();

    /**
     * Sets the current shard.
     *
     * @return The current shard.
     */
    int getCurrentShard();

}