package de.btobastian.javacord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import de.btobastian.javacord.entities.Activity;
import de.btobastian.javacord.entities.ActivityType;
import de.btobastian.javacord.entities.ApplicationInfo;
import de.btobastian.javacord.entities.Invite;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.Webhook;
import de.btobastian.javacord.entities.channels.Channel;
import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.channels.VoiceChannel;
import de.btobastian.javacord.entities.impl.ImplApplicationInfo;
import de.btobastian.javacord.entities.impl.ImplInvite;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageSet;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.connection.LostConnectionListener;
import de.btobastian.javacord.listeners.connection.ReconnectListener;
import de.btobastian.javacord.listeners.connection.ResumeListener;
import de.btobastian.javacord.listeners.group.channel.GroupChannelChangeNameListener;
import de.btobastian.javacord.listeners.group.channel.GroupChannelCreateListener;
import de.btobastian.javacord.listeners.group.channel.GroupChannelDeleteListener;
import de.btobastian.javacord.listeners.message.MessageAttachableListener;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveAllListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.ServerBecomesAvailableListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerChangeDefaultMessageNotificationLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeExplicitContentFilterLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeIconListener;
import de.btobastian.javacord.listeners.server.ServerChangeMultiFactorAuthenticationLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeNameListener;
import de.btobastian.javacord.listeners.server.ServerChangeOwnerListener;
import de.btobastian.javacord.listeners.server.ServerChangeRegionListener;
import de.btobastian.javacord.listeners.server.ServerChangeVerificationLevelListener;
import de.btobastian.javacord.listeners.server.ServerJoinListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangePositionListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelCreateListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.server.channel.ServerTextChannelChangeTopicListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelMemberJoinListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelMemberLeaveListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiChangeNameListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiDeleteListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberBanListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberJoinListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberLeaveListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberUnbanListener;
import de.btobastian.javacord.listeners.server.role.RoleChangePermissionsListener;
import de.btobastian.javacord.listeners.server.role.RoleChangePositionListener;
import de.btobastian.javacord.listeners.server.role.RoleCreateListener;
import de.btobastian.javacord.listeners.server.role.RoleDeleteListener;
import de.btobastian.javacord.listeners.server.role.UserRoleAddListener;
import de.btobastian.javacord.listeners.server.role.UserRoleRemoveListener;
import de.btobastian.javacord.listeners.user.UserChangeActivityListener;
import de.btobastian.javacord.listeners.user.UserChangeAvatarListener;
import de.btobastian.javacord.listeners.user.UserChangeNameListener;
import de.btobastian.javacord.listeners.user.UserChangeNicknameListener;
import de.btobastian.javacord.listeners.user.UserChangeStatusListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelCreateListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelDeleteListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.DiscordWebSocketAdapter;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.ratelimits.RatelimitManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import okhttp3.OkHttpClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is the most important class for your bot, containing all important methods, like registering listener.
 */
public interface DiscordApi {

    /**
     * Gets the used token.
     * The returned token already includes the {@link AccountType#getTokenPrefix()}, so you can use it directly in the
     * authentication header for custom REST calls.
     *
     * @return The used token.
     */
    String getToken();

    /**
     * Gets the thread pool which is internally used.
     *
     * @return The internally used thread pool.
     */
    ThreadPool getThreadPool();

    /**
     * Gets the used {@link OkHttpClient http client} for this api instance.
     *
     * @return The used http client.
     */
    OkHttpClient getHttpClient();

    /**
     * Gets the object mapper used by this api instance.
     *
     * @return The object mapper used by this api instance.
     */
    ObjectMapper getObjectMapper();

    /**
     * Gets the ratelimit manager for this bot.
     *
     * @return The ratelimit manager for this bot.
     */
    RatelimitManager getRatelimitManager();

    /**
     * Gets the websocket adapter which is used to connect to Discord.
     *
     * @return The websocket adapter.
     */
    DiscordWebSocketAdapter getWebSocketAdapter();

    /**
     * Gets the type of the current account.
     *
     * @return The type of the current account.
     */
    AccountType getAccountType();

    /**
     * Creates an invite link for the this bot.
     * The method only works for bot accounts!
     *
     * @return An invite link for this bot.
     * @throws IllegalStateException If the current account is not {@link AccountType#BOT}.
     */
    default String createBotInvite() {
        return new BotInviteBuilder(getClientId()).build();
    }

    /**
     * Creates an invite link for the this bot.
     * The method only works for bot accounts!
     *
     * @param permissions The permissions which should be granted to the bot.
     * @return An invite link for this bot.
     * @throws IllegalStateException If the current account is not {@link AccountType#BOT}.
     */
    default String createBotInvite(Permissions permissions) {
        return new BotInviteBuilder(getClientId()).setPermissions(permissions).build();
    }

    /**
     * Sets the cache size of all caches.
     * This settings are applied on a per-channel basis.
     * It overrides all previous settings, so it's recommended to directly set it after logging in, if you want to
     * change some channel specific cache settings, too.
     * Please notice that the cache is cleared only once every minute!
     *
     * @param capacity The capacity of the message cache.
     * @param storageTimeInSeconds The maximum age of cached messages.
     */
    void setMessageCacheSize(int capacity, int storageTimeInSeconds);

    /**
     * Gets the default message cache capacity which is applied for every newly created channel.
     *
     * @return The default message cache capacity which is applied for every newly created channel.
     */
    int getDefaultMessageCacheCapacity();

    /**
     * Gets the default maximum age of cached messages.
     *
     * @return The default maximum age of cached messages.
     */
    int getDefaultMessageCacheStorageTimeInSeconds();

    /**
     * Gets the current shard of the bot, starting with <code>0</code>.
     *
     * @return The current shard of the bot.
     */
    int getCurrentShard();

    /**
     * Gets the total amount of shards. If the total amount is <code>0</code> sharding is disabled.
     *
     * @return The total amount of shards.
     */
    int getTotalShards();

    /**
     * Checks if Javacord is waiting for all servers to become available on startup.
     *
     * @return Whether Javacord is waiting for all servers to become available on startup or not.
     */
    boolean isWaitingForServersOnStartup();

    /**
     * Updates the status of this bot.
     * The update might not be visible immediately as it's through the websocket and only a limited amount of
     * status changes is allowed per minute.
     *
     * @param status The status of this bot.
     */
    void updateStatus(UserStatus status);

    /**
     * Gets the status which should be displayed for this bot.
     * This might not be the status which is really displayed in the client, but it's the status which Javacord
     * is trying to set for your bot, so it might change in the client a few seconds afterwards.
     *
     * @return The status which should be displayed for this bot.
     */
    UserStatus getStatus();

    /**
     * Updates the activity of this bot, represented as "Playing Half-Life 3" for example.
     *
     * @param name The name of the activity.
     */
    void updateActivity(String name);

    /**
     * Updates the activity of this bot with any type.
     *
     * @param name The name of the activity.
     * @param type The type of the activity.
     */
    void updateActivity(String name, ActivityType type);

    /**
     * Updates the activity of this bot with a streaming url, represented as "Streaming Half-Life 3" for example.
     * The update might not be visible immediately as it's through the websocket and only a limited amount of
     * activity status changes is allowed per minute.
     *
     * @param name The name of the activity.
     * @param streamingUrl The streaming url of the activity.
     */
    void updateActivity(String name, String streamingUrl);

    /**
     * Gets the activity which should be displayed.
     * This might not be the activity which is really displayed in the client, but it's the activity which Javacord
     * is trying to set for your bot, so it might change in the client a few seconds afterwards.
     *
     * @return The activity which should be displayed.
     */
    Optional<Activity> getActivity();

    /**
     * Gets a user of the connected account.
     * This may be a bot user (for normal bots), or a regular user (for client-bots).
     *
     * @return The user of the connected account.
     */
    User getYourself();

    /**
     * Gets the id of the application's owner.
     *
     * @return The id of the application's owner.
     * @throws IllegalStateException If the current account is not {@link AccountType#BOT}.
     */
    long getOwnerId();

    /**
     * Gets the owner of the application.
     *
     * @return The owner of the application.
     * @throws IllegalStateException If the current account is not {@link AccountType#BOT}.
     */
    default CompletableFuture<User> getOwner() {
        return getUserById(getOwnerId());
    }

    /**
     * Gets the client id of the application.
     *
     * @return The client id of the application.
     * @throws IllegalStateException If the current account is not {@link AccountType#BOT}.
     */
    long getClientId();

    /**
     * Disconnects the bot.
     * After disconnecting you should NOT use this instance again.
     */
    void disconnect();

    /**
     * Sets a function which is used to get the delay between reconnects.
     *
     * @param reconnectDelayProvider A function which get's the amount of reconnects (starting with <code>1</code>) as
     *                               the parameter and should return the delay in seconds to wait for the next reconnect
     *                               attempt. By default the function reconnect delay is calculated using the following
     *                               equation: <code>f(x): (x^1.5-(1/(1/(0.1*x)+1))*x^1.5)+(currentShard*6)</code>.
     *                               This would result in a delay which looks like this for a bot with 1 shard:
     *                               <table summary="">
     *                                  <tr>
     *                                      <th>Attempt</th>
     *                                      <th>Delay</th>
     *                                  </tr>
     *                                  <tr><td>1</td><td>1</td></tr>
     *                                  <tr><td>2</td><td>2</td></tr>
     *                                  <tr><td>3</td><td>4</td></tr>
     *                                  <tr><td>4</td><td>6</td></tr>
     *                                  <tr><td>5</td><td>7</td></tr>
     *                                  <tr><td>...</td><td>...</td></tr>
     *                                  <tr><td>10</td><td>16</td></tr>
     *                                  <tr><td>15</td><td>23</td></tr>
     *                                  <tr><td>20</td><td>30</td></tr>
     *                                  <tr><td>...</td><td>...</td></tr>
     *                                  <tr><td>50</td><td>59</td></tr>
     *                                  <tr><td>100</td><td>91</td></tr>
     *                                  <tr><td>150</td><td>115</td></tr>
     *                                  <tr><td>...</td><td>...</td></tr>
     *                               </table>
     *                               Too many reconnect attempts may cause a token reset (usually 1000 per day), so you
     *                               should always make sure to not provide a function which might exceed this limit.
     *                               You should also make sure to take into account the amount of shards!
     */
    void setReconnectDelay(Function<Integer, Integer> reconnectDelayProvider);

    /**
     * Gets the reconnect delay for a given amount of attempts.
     *
     * @param attempt The amount of attempts (starting with <code>1</code>)
     * @return The reconnect delay in seconds.
     */
    int getReconnectDelay(int attempt);

    /**
     * Gets the application info of the bot.
     * The method only works for bot accounts.
     *
     * @return The application info of the bot.
     */
    default CompletableFuture<ApplicationInfo> getApplicationInfo() {
        return new RestRequest<ApplicationInfo>(this, RestMethod.GET, RestEndpoint.SELF_INFO)
                .execute(result -> new ImplApplicationInfo(this, result.getJsonBody()));
    }

    /**
     * Gets a webhook by its id.
     *
     * @param id The id of the webhook.
     * @return The webhook with the given id.
     */
    default CompletableFuture<Webhook> getWebhookById(long id) {
        return new RestRequest<Webhook>(this, RestMethod.GET, RestEndpoint.WEBHOOK)
                .setUrlParameters(String.valueOf(id))
                .execute(result -> new ImplWebhook(this, result.getJsonBody()));
    }

    /**
     * Gets a collection with the ids of all unavailable servers.
     *
     * @return A collection with the ids of all unavailable servers.
     */
    Collection<Long> getUnavailableServers();

    /**
     * Gets an invite by its code.
     *
     * @param code The code of the invite.
     * @return The invite with the given code.
     */
    default CompletableFuture<Invite> getInviteByCode(String code) {
        return new RestRequest<Invite>(this, RestMethod.GET, RestEndpoint.INVITE)
                .setUrlParameters(code)
                .execute(result -> new ImplInvite(this, result.getJsonBody()));
    }

    /**
     * Updates the name of the current account.
     *
     * @param username The new username.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateUsername(String username) {
        return new RestRequest<Void>(this, RestMethod.PATCH, RestEndpoint.CURRENT_USER)
                .setRatelimitRetries(0)
                .setBody(JsonNodeFactory.instance.objectNode().put("username", username))
                .execute(result -> null);
    }

    /**
     * Updates the avatar of the current account.
     * This assumes the provided image is of type <code>"png"</code>.
     *
     * @param avatar The new avatar.
     * @return A future to check if the update was successful.
     * @see #updateAvatar(BufferedImage, String)
     */
    default CompletableFuture<Void> updateAvatar(BufferedImage avatar) {
        return updateAvatar(avatar, "png");
    }

    /**
     * Updates the avatar of the current account.
     *
     * @param avatar The new avatar.
     * @param type The type of the image. Supports <code>"png"</code>, <code>"jpg"</code> and <code>"gif"</code>.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateAvatar(BufferedImage avatar, String type) {
        String base64Avatar;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(avatar, type, os);
            base64Avatar = "data:image/" + type + ";base64," + Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
        return new RestRequest<Void>(this, RestMethod.PATCH, RestEndpoint.CURRENT_USER)
                .setRatelimitRetries(0)
                .setBody(JsonNodeFactory.instance.objectNode().put("avatar", base64Avatar))
                .execute(result -> null);
    }

    /**
     * Gets a collection with all currently cached users.
     *
     * @return A collection with all currently cached users.
     */
    Collection<User> getCachedUsers();

    /**
     * Gets a cached user by its id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    Optional<User> getCachedUserById(long id);

    /**
     * Gets a cached user by its id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    default Optional<User> getCachedUserById(String id) {
        try {
            return getCachedUserById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a user by its id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    CompletableFuture<User> getUserById(long id);

    /**
     * Gets a user by its id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    default CompletableFuture<User> getUserById(String id) {
        try {
            return getUserById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return getUserById(-1);
        }
    }

    /**
     * Gets a user by its discriminated name like e. g. {@code Bastian#8222}.
     *
     * @param discriminatedName The discriminated name of the user.
     * @return The user with the given discriminated name.
     */
    default Optional<User> getCachedUserByDiscriminatedName(String discriminatedName) {
        String[] nameAndDiscriminator = discriminatedName.split("#", 2);
        return getCachedUserByNameAndDiscriminator(nameAndDiscriminator[0], nameAndDiscriminator[1]);
    }

    /**
     * Gets a user by its name and discriminator.
     *
     * @param name The name of the user.
     * @param discriminator The discriminator of the user.
     * @return The user with the given name and discriminator.
     */
    default Optional<User> getCachedUserByNameAndDiscriminator(String name, String discriminator) {
        return getCachedUsersByName(name).stream()
                .filter(user -> user.getDiscriminator().equals(discriminator))
                .findAny();
    }

    /**
     * Gets a collection with all users with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the users.
     * @return A collection with all users with the given name.
     */
    default Collection<User> getCachedUsersByName(String name) {
        return getCachedUsers().stream()
                .filter(user -> user.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all users with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the users.
     * @return A collection with all users with the given name.
     */
    default Collection<User> getCachedUsersByNameIgnoreCase(String name) {
        return getCachedUsers().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a message set with all currently cached messages.
     *
     * @return A message set with all currently cached messages.
     */
    MessageSet getCachedMessages();

    /**
     * Gets a cached message by its id.
     *
     * @param id The id of the message.
     * @return The cached message.
     */
    Optional<Message> getCachedMessageById(long id);

    /**
     * Gets a cached message by its id.
     *
     * @param id The id of the message.
     * @return The cached message.
     */
    default Optional<Message> getCachedMessageById(String id) {
        try {
            return getCachedMessageById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a message by its id.
     *
     * @param id The id of the message.
     * @param channel The channel of the message.
     * @return The message with the given id.
     * @see TextChannel#getMessageById(long)
     */
    default CompletableFuture<Message> getMessageById(long id, TextChannel channel) {
        return channel.getMessageById(id);
    }

    /**
     * Gets a message by its id.
     *
     * @param id The id of the message.
     * @param channel The channel of the message.
     * @return The message with the given id.
     * @see TextChannel#getMessageById(String)
     */
    default CompletableFuture<Message> getMessageById(String id, TextChannel channel) {
        return channel.getMessageById(id);
    }

    /**
     * Gets a collection with all servers the bot is in.
     *
     * @return A collection with all servers the bot is in.
     */
    Collection<Server> getServers();

    /**
     * Gets a server by its id.
     *
     * @param id The id of the server.
     * @return The server with the given id.
     */
    default Optional<Server> getServerById(long id) {
        return getServers().stream()
                .filter(server -> server.getId() == id)
                .findAny();
    }

    /**
     * Gets a server by its id.
     *
     * @param id The id of the server.
     * @return The server with the given id.
     */
    default Optional<Server> getServerById(String id) {
        try {
            return getServerById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all servers with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the servers.
     * @return A collection with all servers with the given name.
     */
    default Collection<Server> getServersByName(String name) {
        return getServers().stream()
                .filter(server -> server.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all servers with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the servers.
     * @return A collection with all servers with the given name.
     */
    default Collection<Server> getServersByNameIgnoreCase(String name) {
        return getServers().stream()
                .filter(server -> server.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all known custom emojis.
     *
     * @return A collection with all known custom emojis.
     */
    Collection<CustomEmoji> getCustomEmojis();

    /**
     * Gets a custom emoji in this server by its id.
     *
     * @param id The id of the emoji.
     * @return The emoji with the given id.
     */
    default Optional<CustomEmoji> getCustomEmojiById(long id) {
        return getCustomEmojis().stream().filter(emoji -> emoji.getId() == id).findAny();
    }

    /**
     * Gets a custom emoji in this server by its id.
     *
     * @param id The id of the emoji.
     * @return The emoji with the given id.
     */
    default Optional<CustomEmoji> getCustomEmojiById(String id) {
        try {
            return getCustomEmojiById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all custom emojis with the given name in the server.
     * This method is case sensitive!
     *
     * @param name The name of the custom emojis.
     * @return A collection with all custom emojis with the given name in this server.
     */
    default Collection<CustomEmoji> getCustomEmojisByName(String name) {
        return getCustomEmojis().stream()
                .filter(emoji -> emoji.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all custom emojis with the given name in the server.
     * This method is case insensitive!
     *
     * @param name The name of the custom emojis.
     * @return A collection with all custom emojis with the given name in this server.
     */
    default Collection<CustomEmoji> getCustomEmojisByNameIgnoreCase(String name) {
        return getCustomEmojis().stream()
                .filter(emoji -> emoji.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all roles the bot knows.
     *
     * @return A collection with all roles the bot knows.
     */
    default Collection<Role> getRoles() {
        Collection<Role> roles = new HashSet<>();
        getServers().stream().map(Server::getRoles).forEach(roles::addAll);
        return roles;
    }

    /**
     * Gets a role by its id.
     *
     * @param id The id of the role.
     * @return The role with the given id.
     */
    default Optional<Role> getRoleById(long id) {
        return getServers().stream()
                .map(server -> server.getRoleById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    /**
     * Gets a role by its id.
     *
     * @param id The id of the role.
     * @return The role with the given id.
     */
    default Optional<Role> getRoleById(String id) {
        try {
            return getRoleById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all roles with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the roles.
     * @return A collection with all roles with the given name.
     */
    default Collection<Role> getRolesByName(String name) {
        return getRoles().stream()
                .filter(role -> role.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all roles with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the roles.
     * @return A collection with all roles with the given name.
     */
    default Collection<Role> getRolesByNameIgnoreCase(String name) {
        return getRoles().stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all channels of the bot.
     *
     * @return A collection with all channels of the bot.
     */
    default Collection<Channel> getChannels() {
        Collection<Channel> channels = new ArrayList<>();
        channels.addAll(getPrivateChannels());
        channels.addAll(getServerChannels());
        channels.addAll(getGroupChannels());
        return channels;
    }

    /**
     * Gets a collection with all group channels of the bot.
     *
     * @return A collection with all group channels of the bot.
     */
    Collection<GroupChannel> getGroupChannels();

    /**
     * Gets a collection with all private channels of the bot.
     *
     * @return A collection with all private channels of the bot.
     */
    default Collection<PrivateChannel> getPrivateChannels() {
        return getCachedUsers().stream()
                .map(User::getPrivateChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all server channels of the bot.
     *
     * @return A collection with all server channels of the bot.
     */
    default Collection<ServerChannel> getServerChannels() {
        Collection<ServerChannel> channels = new ArrayList<>();
        getServers().forEach(server -> channels.addAll(server.getChannels()));
        return channels;
    }

    /**
     * Gets a collection with all channel categories of the bot.
     *
     * @return A collection with all channel categories of the bot.
     */
    default Collection<ChannelCategory> getChannelCategories() {
        Collection<ChannelCategory> channels = new ArrayList<>();
        getServers().forEach(server -> channels.addAll(server.getChannelCategories()));
        return channels;
    }

    /**
     * Gets a collection with all server text channels of the bot.
     *
     * @return A collection with all server text channels of the bot.
     */
    default Collection<ServerTextChannel> getServerTextChannels() {
        Collection<ServerTextChannel> channels = new ArrayList<>();
        getServers().forEach(server -> channels.addAll(server.getTextChannels()));
        return channels;
    }

    /**
     * Gets a collection with all server voice channels of the bot.
     *
     * @return A collection with all server voice channels of the bot.
     */
    default Collection<ServerVoiceChannel> getServerVoiceChannels() {
        Collection<ServerVoiceChannel> channels = new ArrayList<>();
        getServers().forEach(server -> channels.addAll(server.getVoiceChannels()));
        return channels;
    }

    /**
     * Gets a collection with all text channels of the bot.
     *
     * @return A collection with all text channels of the bot.
     */
    default Collection<TextChannel> getTextChannels() {
        Collection<TextChannel> channels = new ArrayList<>();
        channels.addAll(getPrivateChannels());
        channels.addAll(getServerTextChannels());
        channels.addAll(getGroupChannels());
        return channels;
    }

    /**
     * Gets a collection with all voice channels of the bot.
     *
     * @return A collection with all voice channels of the bot.
     */
    default Collection<VoiceChannel> getVoiceChannels() {
        Collection<VoiceChannel> channels = new ArrayList<>();
        channels.addAll(getPrivateChannels());
        channels.addAll(getServerVoiceChannels());
        channels.addAll(getGroupChannels());
        return channels;
    }

    /**
     * Gets a channel by its id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    default Optional<Channel> getChannelById(long id) {
        return getChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a channel by its id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    default Optional<Channel> getChannelById(String id) {
        try {
            return getChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the channels. Can be <code>null</code> to search for group channels without name.
     * @return A collection with all channels with the given name.
     */
    default Collection<Channel> getChannelsByName(String name) {
        Collection<Channel> channels = new HashSet<>();
        channels.addAll(getServerChannelsByName(name));
        channels.addAll(getGroupChannelsByName(name));
        return channels;
    }

    /**
     * Gets a collection with all channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the channels. Can be <code>null</code> to search for group channels without name.
     * @return A collection with all channels with the given name.
     */
    default Collection<Channel> getChannelsByNameIgnoreCase(String name) {
        Collection<Channel> channels = new HashSet<>();
        channels.addAll(getServerChannelsByNameIgnoreCase(name));
        channels.addAll(getGroupChannelsByNameIgnoreCase(name));
        return channels;
    }

    /**
     * Gets a text channel by its id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<TextChannel> getTextChannelById(long id) {
        return getTextChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a text channel by its id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<TextChannel> getTextChannelById(String id) {
        try {
            return getTextChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all text channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the text channels. Can be <code>null</code> to search for group channels without name.
     * @return A collection with all text channels with the given name.
     */
    default Collection<TextChannel> getTextChannelsByName(String name) {
        Collection<TextChannel> channels = new HashSet<>();
        channels.addAll(getServerTextChannelsByName(name));
        channels.addAll(getGroupChannelsByName(name));
        return channels;
    }

    /**
     * Gets a collection with all text channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the text channels. Can be <code>null</code> to search for group channels without name.
     * @return A collection with all text channels with the given name.
     */
    default Collection<TextChannel> getTextChannelsByNameIgnoreCase(String name) {
        Collection<TextChannel> channels = new HashSet<>();
        channels.addAll(getServerTextChannelsByNameIgnoreCase(name));
        channels.addAll(getGroupChannelsByNameIgnoreCase(name));
        return channels;
    }

    /**
     * Gets a voice channel by its id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<VoiceChannel> getVoiceChannelById(long id) {
        return getVoiceChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a voice channel by its id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<VoiceChannel> getVoiceChannelById(String id) {
        try {
            return getVoiceChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all voice channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the voice channels. Can be <code>null</code> to search for group channels without name.
     * @return A collection with all voice channels with the given name.
     */
    default Collection<VoiceChannel> getVoiceChannelsByName(String name) {
        Collection<VoiceChannel> channels = new HashSet<>();
        channels.addAll(getServerVoiceChannelsByName(name));
        channels.addAll(getGroupChannelsByName(name));
        return channels;
    }

    /**
     * Gets a collection with all voice channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the voice channels. Can be <code>null</code> to search for group channels without name.
     * @return A collection with all voice channels with the given name.
     */
    default Collection<VoiceChannel> getVoiceChannelsByNameIgnoreCase(String name) {
        Collection<VoiceChannel> channels = new HashSet<>();
        channels.addAll(getServerVoiceChannelsByNameIgnoreCase(name));
        channels.addAll(getGroupChannelsByNameIgnoreCase(name));
        return channels;
    }

    /**
     * Gets a server channel by its id.
     *
     * @param id The id of the server channel.
     * @return The server channel with the given id.
     */
    default Optional<ServerChannel> getServerChannelById(long id) {
        return getServerChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a server channel by its id.
     *
     * @param id The id of the server channel.
     * @return The server channel with the given id.
     */
    default Optional<ServerChannel> getServerChannelById(String id) {
        try {
            return getServerChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all server channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the server channels.
     * @return A collection with all server channels with the given name.
     */
    default Collection<ServerChannel> getServerChannelsByName(String name) {
        return getServerChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all server channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the server channels.
     * @return A collection with all server channels with the given name.
     */
    default Collection<ServerChannel> getServerChannelsByNameIgnoreCase(String name) {
        return getServerChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a channel category by its id.
     *
     * @param id The id of the channel category.
     * @return The channel category with the given id.
     */
    default Optional<ChannelCategory> getChannelCategoryById(long id) {
        return getChannelCategories().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a channel category by its id.
     *
     * @param id The id of the channel category.
     * @return The channel category with the given id.
     */
    default Optional<ChannelCategory> getChannelCategoryById(String id) {
        try {
            return getChannelCategoryById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all channel categories with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the channel categories.
     * @return A collection with all channel categories with the given name.
     */
    default Collection<ChannelCategory> getChannelCategoriesByName(String name) {
        return getChannelCategories().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all channel categories with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the channel categories.
     * @return A collection with all channel categories with the given name.
     */
    default Collection<ChannelCategory> getChannelCategoriesByNameIgnoreCase(String name) {
        return getChannelCategories().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a server text channel by its id.
     *
     * @param id The id of the server text channel.
     * @return The server text channel with the given id.
     */
    default Optional<ServerTextChannel> getServerTextChannelById(long id) {
        return getServerTextChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a server text channel by its id.
     *
     * @param id The id of the server text channel.
     * @return The server text channel with the given id.
     */
    default Optional<ServerTextChannel> getServerTextChannelById(String id) {
        try {
            return getServerTextChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all server text channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the server text channels.
     * @return A collection with all server text channels with the given name.
     */
    default Collection<ServerTextChannel> getServerTextChannelsByName(String name) {
        return getServerTextChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all server text channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the server text channels.
     * @return A collection with all server text channels with the given name.
     */
    default Collection<ServerTextChannel> getServerTextChannelsByNameIgnoreCase(String name) {
        return getServerTextChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a server voice channel by its id.
     *
     * @param id The id of the server voice channel.
     * @return The server voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getServerVoiceChannelById(long id) {
        return getServerVoiceChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a server voice channel by its id.
     *
     * @param id The id of the server voice channel.
     * @return The server voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getServerVoiceChannelById(String id) {
        try {
            return getServerVoiceChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all server voice channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the server voice channels.
     * @return A collection with all server voice channels with the given name.
     */
    default Collection<ServerVoiceChannel> getServerVoiceChannelsByName(String name) {
        return getServerVoiceChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all server voice channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the server voice channels.
     * @return A collection with all server voice channels with the given name.
     */
    default Collection<ServerVoiceChannel> getServerVoiceChannelsByNameIgnoreCase(String name) {
        return getServerVoiceChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a private channel by its id.
     *
     * @param id The id of the private channel.
     * @return The private channel with the given id.
     */
    default Optional<PrivateChannel> getPrivateChannelById(long id) {
        return getPrivateChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a private channel by its id.
     *
     * @param id The id of the private channel.
     * @return The private channel with the given id.
     */
    default Optional<PrivateChannel> getPrivateChannelById(String id) {
        try {
            return getPrivateChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a group channel by its id.
     *
     * @param id The id of the group channel.
     * @return The group channel with the given id.
     */
    default Optional<GroupChannel> getGroupChannelById(long id) {
        return getGroupChannels().stream()
                .filter(channel -> channel.getId() == id)
                .findAny();
    }

    /**
     * Gets a group channel by its id.
     *
     * @param id The id of the group channel.
     * @return The group channel with the given id.
     */
    default Optional<GroupChannel> getGroupChannelById(String id) {
        try {
            return getGroupChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a collection with all group channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the group channels. Can be <code>null</code> to search for group channels without name.
     * @return A collection with all group channels with the given name.
     */
    default Collection<GroupChannel> getGroupChannelsByName(String name) {
        return getGroupChannels().stream()
                .filter(channel -> Objects.deepEquals(channel.getName().orElse(null), name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection with all server channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the group channels. Can be <code>null</code> to search for group channels without name.
     * @return A collection with all group channels with the given name.
     */
    default Collection<GroupChannel> getGroupChannelsByNameIgnoreCase(String name) {
        return getGroupChannels().stream()
                .filter(channel -> {
                    String channelName = channel.getName().orElse(null);
                    if (name == null || channelName == null) {
                        return Objects.deepEquals(channelName, name);
                    }
                    return name.equalsIgnoreCase(channelName);
                })
                .collect(Collectors.toList());
    }

    /**
     * Adds a listener, which listens to global message creates.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener);

    /**
     * Gets a list with all registered message create listeners.
     *
     * @return A list with all registered message create listeners.
     */
    List<MessageCreateListener> getMessageCreateListeners();

    /**
     * Adds a listener, which listens to server joins.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerJoinListener> addServerJoinListener(ServerJoinListener listener);

    /**
     * Gets a list with all registered server join listeners.
     *
     * @return A list with all registered server join listeners.
     */
    List<ServerJoinListener> getServerJoinListeners();

    /**
     * Adds a listener, which listens to server leaves.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerLeaveListener> addServerLeaveListener(ServerLeaveListener listener);

    /**
     * Gets a list with all registered server leaves listeners.
     *
     * @return A list with all registered server leaves listeners.
     */
    List<ServerLeaveListener> getServerLeaveListeners();

    /**
     * Adds a listener, which listens to servers becoming available.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerBecomesAvailableListener> addServerBecomesAvailableListener(
            ServerBecomesAvailableListener listener);

    /**
     * Gets a list with all registered server becomes available listeners.
     *
     * @return A list with all registered server becomes available listeners.
     */
    List<ServerBecomesAvailableListener> getServerBecomesAvailableListeners();

    /**
     * Adds a listener, which listens to servers becoming unavailable.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerBecomesUnavailableListener> addServerBecomesUnavailableListener(
            ServerBecomesUnavailableListener listener);

    /**
     * Gets a list with all registered server becomes unavailable listeners.
     *
     * @return A list with all registered server becomes unavailable listeners.
     */
    List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners();

    /**
     * Adds a listener, which listens to users starting to type.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener);

    /**
     * Gets a list with all registered user starts typing listeners.
     *
     * @return A list with all registered user starts typing listeners.
     */
    List<UserStartTypingListener> getUserStartTypingListeners();

    /**
     * Adds a listener, which listens to server channel creations.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelCreateListener> addServerChannelCreateListener(ServerChannelCreateListener listener);

    /**
     * Gets a list with all registered server channel create listeners.
     *
     * @return A list with all registered server channel create listeners.
     */
    List<ServerChannelCreateListener> getServerChannelCreateListeners();

    /**
     * Adds a listener, which listens to server channel deletions.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelDeleteListener> addServerChannelDeleteListener(ServerChannelDeleteListener listener);

    /**
     * Gets a list with all registered server channel delete listeners.
     *
     * @return A list with all registered server channel delete listeners.
     */
    List<ServerChannelDeleteListener> getServerChannelDeleteListeners();

    /**
     * Adds a listener, which listens to private channel creations.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<PrivateChannelCreateListener> addPrivateChannelCreateListener(PrivateChannelCreateListener listener);

    /**
     * Gets a list with all registered private channel create listeners.
     *
     * @return A list with all registered private channel create listeners.
     */
    List<PrivateChannelCreateListener> getPrivateChannelCreateListeners();

    /**
     * Adds a listener, which listens to private channel deletions.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(PrivateChannelDeleteListener listener);

    /**
     * Gets a list with all registered private channel delete listeners.
     *
     * @return A list with all registered private channel delete listeners.
     */
    List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners();

    /**
     * Adds a listener, which listens to group channel creations.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<GroupChannelCreateListener> addGroupChannelCreateListener(GroupChannelCreateListener listener);

    /**
     * Gets a list with all registered group channel create listeners.
     *
     * @return A list with all registered group channel create listeners.
     */
    List<GroupChannelCreateListener> getGroupChannelCreateListeners();

    /**
     * Adds a listener, which listens to group channel name changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener);

    /**
     * Gets a list with all registered group channel change name listeners.
     *
     * @return A list with all registered group channel change name listeners.
     */
    List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners();

    /**
     * Adds a listener, which listens to group channel deletions.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(GroupChannelDeleteListener listener);

    /**
     * Gets a list with all registered group channel delete listeners.
     *
     * @return A list with all registered group channel delete listeners.
     */
    List<GroupChannelDeleteListener> getGroupChannelDeleteListeners();

    /**
     * Adds a listener, which listens to message deletions.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener);

    /**
     * Adds a listener, which listens to message deletions of a specific message.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageDeleteListener> addMessageDeleteListener(
            Message message, MessageDeleteListener listener) {
        return addMessageDeleteListener(message.getId(), listener);
    }

    /**
     * Adds a listener, which listens to message deletions of a specific message.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageDeleteListener> addMessageDeleteListener(long messageId, MessageDeleteListener listener);

    /**
     * Gets a list with all registered message delete listeners.
     *
     * @return A list with all registered message delete listeners.
     */
    List<MessageDeleteListener> getMessageDeleteListeners();

    /**
     * Gets a list with all registered message delete listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered message delete listeners.
     */
    default List<MessageDeleteListener> getMessageDeleteListeners(Message message) {
        return getMessageDeleteListeners(message.getId());
    }

    /**
     * Gets a list with all registered message delete listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered message delete listeners.
     */
    List<MessageDeleteListener> getMessageDeleteListeners(long messageId);

    /**
     * Gets a list with all registered message delete listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered message delete listeners.
     */
    default List<MessageDeleteListener> getMessageDeleteListeners(String messageId) {
        try {
            return getMessageDeleteListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) { }
        return Collections.emptyList();
    }

    /**
     * Adds a listener, which listens to message edits.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener);

    /**
     * Adds a listener, which listens to message edits of a specific message.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageEditListener> addMessageEditListener(
            Message message, MessageEditListener listener) {
        return addMessageEditListener(message.getId(), listener);
    }

    /**
     * Adds a listener, which listens to message edits of a specific message.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageEditListener> addMessageEditListener(long messageId, MessageEditListener listener);

    /**
     * Gets a list with all registered message edit listeners.
     *
     * @return A list with all registered message edit listeners.
     */
    List<MessageEditListener> getMessageEditListeners();

    /**
     * Gets a list with all registered message edit listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered message edit listeners.
     */
    default List<MessageEditListener> getMessageEditListeners(Message message) {
        return getMessageEditListeners(message.getId());
    }

    /**
     * Gets a list with all registered message edit listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered message edit listeners.
     */
    List<MessageEditListener> getMessageEditListeners(long messageId);

    /**
     * Adds a listener, which listens to reactions being added.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener);

    /**
     * Adds a listener, which listens to reactions being added to a specific message.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionAddListener> addReactionAddListener(Message message, ReactionAddListener listener) {
        return addReactionAddListener(message.getId(), listener);
    }

    /**
     * Adds a listener, which listens to reactions being added to a specific message.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionAddListener> addReactionAddListener(long messageId, ReactionAddListener listener);

    /**
     * Gets a list with all registered reaction add listeners.
     *
     * @return A list with all registered reaction add listeners.
     */
    List<ReactionAddListener> getReactionAddListeners();

    /**
     * Gets a list with all registered reaction add listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered reaction add listeners.
     */
    default List<ReactionAddListener> getReactionAddListeners(Message message) {
        return getReactionAddListeners(message.getId());
    }

    /**
     * Gets a list with all registered reaction add listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction add listeners.
     */
    List<ReactionAddListener> getReactionAddListeners(long messageId);

    /**
     * Gets a list with all registered reaction add listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction add listeners.
     */
    default List<ReactionAddListener> getReactionAddListeners(String messageId) {
        try {
            return getReactionAddListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) { }
        return Collections.emptyList();
    }

    /**
     * Adds a listener, which listens to reactions being removed.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener);

    /**
     * Adds a listener, which listens to reactions being removed from a specific message.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionRemoveListener> addReactionRemoveListener(
            Message message, ReactionRemoveListener listener) {
        return addReactionRemoveListener(message.getId(), listener);
    }

    /**
     * Adds a listener, which listens to reactions being removed from a specific message.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveListener> addReactionRemoveListener(long messageId, ReactionRemoveListener listener);

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    List<ReactionRemoveListener> getReactionRemoveListeners();

    /**
     * Gets a list with all registered reaction remove listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered reaction remove listeners.
     */
    default List<ReactionRemoveListener> getReactionRemoveListeners(Message message) {
        return getReactionRemoveListeners(message.getId());
    }

    /**
     * Gets a list with all registered reaction remove listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction remove listeners.
     */
    List<ReactionRemoveListener> getReactionRemoveListeners(long messageId);

    /**
     * Gets a list with all registered reaction remove listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction remove listeners.
     */
    default List<ReactionRemoveListener> getReactionRemoveListeners(String messageId) {
        try {
            return getReactionRemoveListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) { }
        return Collections.emptyList();
    }

    /**
     * Adds a listener, which listens to all reactions being removed at once.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(ReactionRemoveAllListener listener);

    /**
     * Adds a listener, which listens to all reactions being removed from a specific message at once.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(
            Message message, ReactionRemoveAllListener listener) {
        return addReactionRemoveAllListener(message.getId(), listener);
    }

    /**
     * Adds a listener, which listens to all reactions being removed from a specific message at once.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(long messageId, ReactionRemoveAllListener listener);

    /**
     * Gets a list with all registered reaction remove all listeners.
     *
     * @return A list with all registered reaction remove all listeners.
     */
    List<ReactionRemoveAllListener> getReactionRemoveAllListeners();

    /**
     * Gets a list with all registered reaction remove all listeners of a specific message.
     *
     * @param message The message.
     * @return A list with all registered reaction remove all listeners.
     */
    default List<ReactionRemoveAllListener> getReactionRemoveAllListeners(Message message) {
        return getReactionRemoveAllListeners(message.getId());
    }

    /**
     * Gets a list with all registered reaction remove all listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction remove all listeners.
     */
    List<ReactionRemoveAllListener> getReactionRemoveAllListeners(long messageId);

    /**
     * Gets a list with all registered reaction remove all listeners of a specific message.
     *
     * @param messageId The id of the message.
     * @return A list with all registered reaction remove all listeners.
     */
    default List<ReactionRemoveAllListener> getReactionRemoveAllListeners(String messageId) {
        try {
            return getReactionRemoveAllListeners(Long.valueOf(messageId));
        } catch (NumberFormatException ignored) { }
        return Collections.emptyList();
    }

    /**
     * Adds a listener, which listens to users joining servers.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener);

    /**
     * Gets a list with all registered server member join listeners.
     *
     * @return A list with all registered server member join listeners.
     */
    List<ServerMemberJoinListener> getServerMemberJoinListeners();

    /**
     * Adds a listener, which listens to users leaving servers.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(ServerMemberLeaveListener listener);

    /**
     * Gets a list with all registered server member leave listeners.
     *
     * @return A list with all registered server member leave listeners.
     */
    List<ServerMemberLeaveListener> getServerMemberLeaveListeners();

    /**
     * Adds a listener, which listens to users getting banned from servers.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener);

    /**
     * Gets a list with all registered server member ban listeners.
     *
     * @return A list with all registered server member ban listeners.
     */
    List<ServerMemberBanListener> getServerMemberBanListeners();

    /**
     * Adds a listener, which listens to users getting unbanned from servers.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener);

    /**
     * Gets a list with all registered server member unban listeners.
     *
     * @return A list with all registered server member unban listeners.
     */
    List<ServerMemberUnbanListener> getServerMemberUnbanListeners();

    /**
     * Adds a listener, which listens to server name changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeNameListener> addServerChangeNameListener(ServerChangeNameListener listener);

    /**
     * Gets a list with all registered server change name listeners.
     *
     * @return A list with all registered server change name listeners.
     */
    List<ServerChangeNameListener> getServerChangeNameListeners();

    /**
     * Adds a listener, which listens to server icon changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeIconListener> addServerChangeIconListener(ServerChangeIconListener listener);

    /**
     * Gets a list with all registered server change icon listeners.
     *
     * @return A list with all registered server change icon listeners.
     */
    List<ServerChangeIconListener> getServerChangeIconListeners();

    /**
     * Adds a listener, which listens to server verification level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeVerificationLevelListener> addServerChangeVerificationLevelListener(
            ServerChangeVerificationLevelListener listener);

    /**
     * Gets a list with all registered server change verification level listeners.
     *
     * @return A list with all registered server change verification level listeners.
     */
    List<ServerChangeVerificationLevelListener> getServerChangeVerificationLevelListeners();

    /**
     * Adds a listener, which listens to server region changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeRegionListener> addServerChangeRegionListener(ServerChangeRegionListener listener);

    /**
     * Gets a list with all registered server change region listeners.
     *
     * @return A list with all registered server change region listeners.
     */
    List<ServerChangeRegionListener> getServerChangeRegionListeners();

    /**
     * Adds a listener, which listens to server default message notification level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeDefaultMessageNotificationLevelListener>
    addServerChangeDefaultMessageNotificationLevelListener(
            ServerChangeDefaultMessageNotificationLevelListener listener);

    /**
     * Gets a list with all registered server change default message notification level listeners.
     *
     * @return A list with all registered server change default message notification level listeners.
     */
    List<ServerChangeDefaultMessageNotificationLevelListener> getServerChangeDefaultMessageNotificationLevelListeners();

    /**
     * Adds a listener, which listens to server multi factor authentication level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeMultiFactorAuthenticationLevelListener>
    addServerChangeMultiFactorAuthenticationLevelListener(ServerChangeMultiFactorAuthenticationLevelListener listener);

    /**
     * Gets a list with all registered server change multi factor authentication level listeners.
     *
     * @return A list with all registered server change multi factor authentication level listeners.
     */
    List<ServerChangeMultiFactorAuthenticationLevelListener> getServerChangeMultiFactorAuthenticationLevelListeners();

    /**
     * Adds a listener, which listens to server owner changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeOwnerListener> addServerChangeOwnerListener(ServerChangeOwnerListener listener);

    /**
     * Gets a list with all registered server change owner listeners.
     *
     * @return A list with all registered server change owner listeners.
     */
    List<ServerChangeOwnerListener> getServerChangeOwnerListeners();

    /**
     * Adds a listener, which listens to server explicit content filter level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeExplicitContentFilterLevelListener> addServerChangeExplicitContentFilterLevelListener(
            ServerChangeExplicitContentFilterLevelListener listener);

    /**
     * Gets a list with all registered server change explicit content filter level listeners.
     *
     * @return A list with all registered server change explicit content filter level listeners.
     */
    List<ServerChangeExplicitContentFilterLevelListener> getServerChangeExplicitContentFilterLevelListeners();

    /**
     * Adds a listener, which listens to server channel name changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelChangeNameListener> addServerChannelChangeNameListener(
            ServerChannelChangeNameListener listener);

    /**
     * Gets a list with all registered server channel change name listeners.
     *
     * @return A list with all registered server channel change name listeners.
     */
    List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners();

    /**
     * Adds a listener, which listens to server channel position changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelChangePositionListener> addServerChannelChangePositionListener(
            ServerChannelChangePositionListener listener);

    /**
     * Gets a list with all registered server channel change position listeners.
     *
     * @return A list with all registered server channel change position listeners.
     */
    List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners();

    /**
     * Adds a listener, which listens to custom emoji creations.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<CustomEmojiCreateListener> addCustomEmojiCreateListener(CustomEmojiCreateListener listener);

    /**
     * Gets a list with all registered custom emoji create listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    List<CustomEmojiCreateListener> getCustomEmojiCreateListeners();

    /**
     * Adds a listener, which listens to custom emoji name changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<CustomEmojiChangeNameListener> addCustomEmojiChangeNameListener(
            CustomEmojiChangeNameListener listener);

    /**
     * Gets a list with all registered custom emoji change name listeners.
     *
     * @return A list with all registered custom emoji change name listeners.
     */
    List<CustomEmojiChangeNameListener> getCustomEmojiChangeNameListeners();

    /**
     * Adds a listener, which listens to custom emoji deletions.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<CustomEmojiDeleteListener> addCustomEmojiDeleteListener(CustomEmojiDeleteListener listener);

    /**
     * Gets a list with all registered custom emoji delete listeners.
     *
     * @return A list with all registered custom emoji delete listeners.
     */
    List<CustomEmojiDeleteListener> getCustomEmojiDeleteListeners();

    /**
     * Adds a listener, which listens to user activity changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(UserChangeActivityListener listener);

    /**
     * Gets a list with all registered user change activity listeners.
     *
     * @return A list with all registered user change activity listeners.
     */
    List<UserChangeActivityListener> getUserChangeActivityListeners();

    /**
     * Adds a listener, which listens to users joining a server voice channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener);

    /**
     * Gets a list with all registered server voice channel member join listeners.
     *
     * @return A list with all registered server voice channel member join listeners.
     */
    List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners();

    /**
     * Adds a listener, which listens to users leaving a server voice channel on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener);

    /**
     * Gets a list with all registered server voice channel member leave listeners.
     *
     * @return A list with all registered server voice channel member leave listeners.
     */
    List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners();

    /**
     * Adds a listener, which listens to user status changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener);

    /**
     * Gets a list with all registered user change status listeners.
     *
     * @return A list with all registered user change status listeners.
     */
    List<UserChangeStatusListener> getUserChangeStatusListeners();

    /**
     * Adds a listener, which listens to role permission changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener);

    /**
     * Gets a list with all registered role change permissions listeners.
     *
     * @return A list with all registered role change permissions listeners.
     */
    List<RoleChangePermissionsListener> getRoleChangePermissionsListeners();

    /**
     * Adds a listener, which listens to role position changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(RoleChangePositionListener listener);

    /**
     * Gets a list with all registered role change position listeners.
     *
     * @return A list with all registered role change position listeners.
     */
    List<RoleChangePositionListener> getRoleChangePositionListeners();

    /**
     * Adds a listener, which listens to overwritten permission changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
    addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener);

    /**
     * Gets a list with all registered server channel change overwritten permissions listeners.
     *
     * @return A list with all registered server channel change overwritten permissions listeners.
     */
    List<ServerChannelChangeOverwrittenPermissionsListener> getServerChannelChangeOverwrittenPermissionsListeners();

    /**
     * Adds a listener, which listens to role creations.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleCreateListener> addRoleCreateListener(RoleCreateListener listener);

    /**
     * Gets a list with all registered role create listeners.
     *
     * @return A list with all registered role create listeners.
     */
    List<RoleCreateListener> getRoleCreateListeners();

    /**
     * Adds a listener, which listens to role deletions.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener);

    /**
     * Gets a list with all registered role delete listeners.
     *
     * @return A list with all registered role delete listeners.
     */
    List<RoleDeleteListener> getRoleDeleteListeners();

    /**
     * Adds a listener, which listens to user nickname changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(UserChangeNicknameListener listener);

    /**
     * Gets a list with all registered user change nickname listeners.
     *
     * @return A list with all registered user change nickname listeners.
     */
    List<UserChangeNicknameListener> getUserChangeNicknameListeners();

    /**
     * Adds a listener, which listens to connection losses.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<LostConnectionListener> addLostConnectionListener(LostConnectionListener listener);

    /**
     * Gets a list with all registered lost connection listeners.
     *
     * @return A list with all registered lost connection listeners.
     */
    List<LostConnectionListener> getLostConnectionListeners();

    /**
     * Adds a listener, which listens to reconnects.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReconnectListener> addReconnectListener(ReconnectListener listener);

    /**
     * Gets a list with all registered reconnect listeners.
     *
     * @return A list with all registered reconnect listeners.
     */
    List<ReconnectListener> getReconnectListeners();

    /**
     * Adds a listener, which listens to resumes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ResumeListener> addResumeListener(ResumeListener listener);

    /**
     * Gets a list with all registered resume listeners.
     *
     * @return A list with all registered resume listeners.
     */
    List<ResumeListener> getResumeListeners();

    /**
     * Adds a listener, which listens to server text channel topic changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener);

    /**
     * Gets a list with all registered server text channel change topic listeners.
     *
     * @return A list with all registered server text channel change topic listeners.
     */
    List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners();

    /**
     * Adds a listener, which listens to users being added to roles.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener);

    /**
     * Gets a list with all registered user role add listeners.
     *
     * @return A list with all registered user role add listeners.
     */
    List<UserRoleAddListener> getUserRoleAddListeners();

    /**
     * Adds a listener, which listens to users being removed from roles.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener);

    /**
     * Gets a list with all registered user role remove listeners.
     *
     * @return A list with all registered user role remove listeners.
     */
    List<UserRoleRemoveListener> getUserRoleRemoveListeners();

    /**
     * Adds a listener, which listens to user name changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener);

    /**
     * Gets a list with all registered user change name listeners.
     *
     * @return A list with all registered user change name listeners.
     */
    List<UserChangeNameListener> getUserChangeNameListeners();

    /**
     * Adds a listener, which listens to user avatar changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener);

    /**
     * Gets a list with all registered user change avatar listeners.
     *
     * @return A list with all registered user change avatar listeners.
     */
    List<UserChangeAvatarListener> getUserChangeAvatarListeners();

    /**
     * Adds a {@code GloballyAttachableListener}.
     * Adding a listener multiple times will only add it once
     * and return the same listener manager on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The manager for the added listener.
     */
    <T extends GloballyAttachableListener> ListenerManager<T> addListener(Class<T> listenerClass, T listener);

    /**
     * Adds a listener that implements one or more {@code GloballyAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same set of listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @return The managers for the added listener.
     */
    @SuppressWarnings("unchecked")
    default Collection<ListenerManager<? extends GloballyAttachableListener>> addListener(
            GloballyAttachableListener listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GloballyAttachableListener.class::isAssignableFrom)
                .filter(listenerClass -> listenerClass != GloballyAttachableListener.class)
                .map(listenerClass -> (Class<GloballyAttachableListener>) listenerClass)
                .map(listenerClass -> addListener(listenerClass, listener))
                .collect(Collectors.toList());
    }

    /**
     * Removes a {@code GloballyAttachableListener}.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends GloballyAttachableListener> void removeListener(Class<T> listenerClass, T listener);

    /**
     * Removes a listener that implements one or more {@code GloballyAttachableListener}s.
     *
     * @param listener The listener to remove.
     */
    @SuppressWarnings("unchecked")
    default void removeListener(GloballyAttachableListener listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GloballyAttachableListener.class::isAssignableFrom)
                .filter(listenerClass -> listenerClass != GloballyAttachableListener.class)
                .map(listenerClass -> (Class<GloballyAttachableListener>) listenerClass)
                .forEach(listenerClass -> removeListener(listenerClass, listener));
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code GloballyAttachableListener}s and their
     * assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code GloballyAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    <T extends GloballyAttachableListener> Map<T, List<Class<T>>> getListeners();

    /**
     * Adds a listener that implements one or more {@code MessageAttachableListener}s to the message with the given id.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param messageId The id of the message which should be listened to.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addMessageAttachableListener(long messageId, T listener);

    /**
     * Adds a listener that implements one or more {@code MessageAttachableListener}s to the given message.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param message The message which should be listened to.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    default <T extends MessageAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addMessageAttachableListener(Message message, T listener) {
        return addMessageAttachableListener(message.getId(), listener);
    }

    /**
     * Removes a {@code MessageAttachableListener} from the message with the given id.
     *
     * @param messageId The id of the message.
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            long messageId, Class<T> listenerClass, T listener);

    /**
     * Removes a listener that implements one or more {@code MessageAttachableListener}s from the given message.
     *
     * @param message The message.
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            Message message, Class<T> listenerClass, T listener) {
        removeMessageAttachableListener(message.getId(), listenerClass, listener);
    }

    /**
     * Removes a listener that implements one or more {@code MessageAttachableListener}s from the message with the given id.
     *
     * @param messageId The id of the message.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            long messageId, T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(MessageAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> removeMessageAttachableListener(messageId, listenerClass, listener));
    }

    /**
     * Removes a listener that implements one or more {@code MessageAttachableListener}s from the given message.
     *
     * @param message The message.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends MessageAttachableListener & ObjectAttachableListener> void removeMessageAttachableListener(
            Message message, T listener) {
        removeMessageAttachableListener(message.getId(), listener);
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code MessageAttachableListener}s and their
     * assigned listener classes they listen to for the message with the given id.
     *
     * @param messageId The id of the message.
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code MessageAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    <T extends MessageAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getMessageAttachableListeners(long messageId);

    /**
     * Gets a map with all registered listeners that implement one or more {@code MessageAttachableListener}s and their
     * assigned listener classes they listen to for the given message.
     *
     * @param message The message.
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code MessageAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    default <T extends MessageAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getMessageAttachableListeners(Message message) {
        return getMessageAttachableListeners(message.getId());
    }

}
