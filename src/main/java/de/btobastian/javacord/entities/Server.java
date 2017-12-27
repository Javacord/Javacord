package de.btobastian.javacord.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.ExplicitContentFilterLevel;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.auditlog.AuditLog;
import de.btobastian.javacord.entities.auditlog.impl.ImplAuditLog;
import de.btobastian.javacord.entities.channels.*;
import de.btobastian.javacord.entities.impl.ImplBan;
import de.btobastian.javacord.entities.impl.ImplInvite;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.permissions.*;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveAllListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerChangeNameListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.server.channel.*;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberJoinListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberLeaveListener;
import de.btobastian.javacord.listeners.server.role.*;
import de.btobastian.javacord.listeners.user.*;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The class represents a Discord server, sometimes also called guild.
 */
public interface Server extends DiscordEntity {

    /**
     * Gets the name of the server.
     *
     * @return The name of the server.
     */
    String getName();

    /**
     * Gets the region of the server.
     *
     * @return The region of the server.
     */
    Region getRegion();

    /**
     * Gets the nickname of a user.
     *
     * @param user The user to check.
     * @return The nickname of the user.
     */
    Optional<String> getNickname(User user);

    /**
     * Gets a collection with all members of the server.
     *
     * @return A collection with all members of the server.
     */
    Collection<User> getMembers();

    /**
     * Checks if the server if considered large.
     *
     * @return Whether the server is large or not.
     */
    boolean isLarge();

    /**
     * Gets the amount of members in this server.
     *
     * @return The amount of members in this server.
     */
    int getMemberCount();

    /**
     * Gets the owner of the server.
     *
     * @return The owner of the server.
     */
    User getOwner();

    /**
     * Gets the verification level of the server.
     *
     * @return The verification level of the server.
     */
    VerificationLevel getVerificationLevel();

    /**
     * Gets the explicit content filter level of the server.
     *
     * @return The explicit content filter level of the server.
     */
    ExplicitContentFilterLevel getExplicitContentFilterLevel();

    /**
     * Gets the default message notification level of the server.
     *
     * @return The default message notification level of the server.
     */
    DefaultMessageNotificationLevel getDefaultMessageNotificationLevel();

    /**
     * Gets the icon of the server.
     *
     * @return The icon of the server.
     */
    Optional<Icon> getIcon();

    /**
     * Gets the splash of the server.
     *
     * @return The splash of the server.
     */
    Optional<Icon> getSplash();

    /**
     * Gets the invites of the server.
     *
     * @return The invites of the server.
     */
    default CompletableFuture<Collection<RichInvite>> getInvites() {
        return new RestRequest<Collection<RichInvite>>(getApi(), RestMethod.GET, RestEndpoint.SERVER_INVITE)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Collection<RichInvite> invites = new HashSet<>();
                    for (JsonNode inviteJson : result.getJsonBody()) {
                        invites.add(new ImplInvite(getApi(), inviteJson));
                    }
                    return invites;
                });
    }

    /**
     * Gets a sorted list (by position) with all roles of the server.
     *
     * @return A sorted list (by position) with all roles of the server.
     */
    List<Role> getRoles();

    /**
     * Gets a role by it's id.
     *
     * @param id The id of the role.
     * @return The role with the given id.
     */
    Optional<Role> getRoleById(long id);

    /**
     * Gets a role by it's id.
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
     * Gets a sorted list (by position) with all roles with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the roles.
     * @return A sorted list (by position) with all roles with the given name.
     */
    default List<Role> getRolesByName(String name) {
        return getRoles().stream()
                .filter(role -> role.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a sorted list (by position) with all roles with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the roles.
     * @return A sorted list (by position) with all roles with the given name.
     */
    default List<Role> getRolesByNameIgnoreCase(String name) {
        return getRoles().stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a sorted list (by position) with all roles of the user in the server.
     *
     * @param user The user.
     * @return A sorted list (by position) with all roles of the user in the server.
     */
    default List<Role> getRolesOf(User user) {
        return getRoles().stream()
                .filter(role -> role.getUsers().contains(user))
                .collect(Collectors.toList());
    }

    /**
     * Gets the permissions of a user.
     *
     * @param user The user.
     * @return The permissions of the user.
     */
    default Permissions getPermissionsOf(User user) {
        PermissionsBuilder builder = new PermissionsBuilder();
        getAllowedPermissionsOf(user).forEach(type -> builder.setState(type, PermissionState.ALLOWED));
        return builder.build();
    }

    /**
     * Get the allowed permissions of a given user.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @param user The user.
     * @return The allowed permissions of the given user.
     */
    default Collection<PermissionType> getAllowedPermissionsOf(User user) {
        Collection<PermissionType> allowed = new HashSet<>();
        if (getOwner() == user) {
            allowed.addAll(Arrays.asList(PermissionType.values()));
        } else {
            getRolesOf(user).forEach(role -> allowed.addAll(role.getAllowedPermissions()));
        }
        return allowed;
    }

    /**
     * Get the unset permissions of a given user.
     *
     * @param user The user.
     * @return The unset permissions of the given user.
     */
    default Collection<PermissionType> getUnsetPermissionsOf(User user) {
        Collection<PermissionType> unset = new HashSet<>();
        if (getOwner() == user) {
            return unset;
        }
        getRolesOf(user).forEach(role -> unset.addAll(role.getUnsetPermissions()));
        return unset;
    }

    /**
     * Checks if the user has a given set of permissions.
     *
     * @param user The user to check.
     * @param type The permission type(s) to check.
     * @return Whether the user has all given permissions of not.
     * @see #getAllowedPermissionsOf(User)
     */
    default boolean hasPermissions(User user, PermissionType... type) {
        return getAllowedPermissionsOf(user).containsAll(Arrays.asList(type));
    }

    /**
     * Gets the updater for this server.
     *
     * @return The updater for this server.
     */
    default ServerUpdater getUpdater() {
        return new ServerUpdater(this);
    }

    /**
     * Updates the name of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param name The new name of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return getUpdater().setName(name).update();
    }

    /**
     * Updates the region of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param region The new region of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRegion(Region region) {
        return getUpdater().setRegion(region).update();
    }

    /**
     * Updates the verification level of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param verificationLevel The new verification level of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateVerificationLevel(VerificationLevel verificationLevel) {
        return getUpdater().setVerificationLevel(verificationLevel).update();
    }

    /**
     * Updates the default message notification level of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param defaultMessageNotificationLevel The new default message notification level of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        return getUpdater().setDefaultMessageNotificationLevel(defaultMessageNotificationLevel).update();
    }

    /**
     * Updates the afk channel of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param afkChannel The new afk channel of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateAfkChannel(ServerVoiceChannel afkChannel) {
        return getUpdater().setAfkChannel(afkChannel).update();
    }

    /**
     * Updates the afk timeout of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param afkTimeout The new afk timeout of the server in seconds.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateAfkTimeoutInSeconds(int afkTimeout) {
        return getUpdater().setAfkTimeoutInSeconds(afkTimeout).update();
    }

    /**
     * Updates the icon of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(BufferedImage icon) {
        return getUpdater().setIcon(icon).update();
    }

    /**
     * Updates the owner of the server.
     * You must be the owner of this server in order to transfer it!
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param owner The new owner of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateOwner(User owner) {
        return getUpdater().setOwner(owner).update();
    }

    /**
     * Updates the splash of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(BufferedImage splash) {
        return getUpdater().setSplash(splash).update();
    }

    /**
     * Updates the system channel of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param systemChannel The new system channel of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> setSystemChannel(ServerTextChannel systemChannel) {
        return getUpdater().setSystemChannel(systemChannel).update();
    }

    /**
     * Changes the nickname of the given user.
     *
     * @param user The user.
     * @param nickname The new nickname of the user.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(User user, String nickname) {
        if (user.isYourself()) {
            return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.OWN_NICKNAME)
                    .setUrlParameters(String.valueOf(getId()))
                    .setBody(JsonNodeFactory.instance.objectNode().put("nick", nickname))
                    .execute(result -> null);
        } else {
            return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.SERVER_MEMBER)
                    .setUrlParameters(String.valueOf(getId()), String.valueOf(user.getId()))
                    .setBody(JsonNodeFactory.instance.objectNode().put("nick", nickname))
                    .execute(result -> null);
        }
    }

    /**
     * Removes the nickname of the given user.
     *
     * @param user The user.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname(User user) {
        return updateNickname(user, null);
    }

    /**
     * Deletes the server.
     *
     * @return A future to check if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER)
                .setUrlParameters(getIdAsString())
                .execute(result -> null);
    }

    /**
     * Leaves the server.
     *
     * @return A future to check if the bot successfully left the server.
     */
    default CompletableFuture<Void> leave() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_SELF)
                .setUrlParameters(getIdAsString())
                .execute(result -> null);
    }

    /**
     * Updates the roles of a server member.
     * This will replace the roles of the server member with a provided collection.
     *
     * @param user The user to update the roles of.
     * @param roles The collection of roles to replace the user's roles.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRoles(User user, Collection<Role> roles) {
        ObjectNode updateNode = JsonNodeFactory.instance.objectNode();
        ArrayNode rolesJson = updateNode.putArray("roles");
        for (Role role : roles) {
            rolesJson.add(role.getIdAsString());
        }
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.SERVER_MEMBER)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .setBody(updateNode)
                .execute(result -> null);
    }

    /**
     * Kicks the given user from the server.
     *
     * @param user The user to kick.
     * @return A future to check if the kick was successful.
     */
    default CompletableFuture<Void> kickUser(User user) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_MEMBER)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .execute(result -> null);
    }

    /**
     * Bans the given user from the server.
     *
     * @param user The user to ban.
     * @return A future to check if the ban was successful.
     */
    default CompletableFuture<Void> banUser(User user) {
        return new RestRequest<Void>(getApi(), RestMethod.PUT, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .setBody(JsonNodeFactory.instance.objectNode().put("delete-message-days", 0))
                .execute(result -> null);
    }

    /**
     * Bans the given user from the server.
     *
     * @param user The user to ban.
     * @param deleteMessageDays The number of days to delete messages for (0-7).
     * @return A future to check if the ban was successful.
     */
    default CompletableFuture<Void> banUser(User user, int deleteMessageDays) {
        return new RestRequest<Void>(getApi(), RestMethod.PUT, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .setBody(JsonNodeFactory.instance.objectNode().put("delete-message-days", deleteMessageDays))
                .execute(result -> null);
    }

    /**
     * Unbans the given user from the server.
     *
     * @param user The user to ban.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(User user) {
        return unbanUser(user.getId());
    }

    /**
     * Unbans the given user from the server.
     *
     * @param userId The id of the user to unban.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(long userId) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), String.valueOf(userId))
                .execute(result -> null);
    }

    /**
     * Unbans the given user from the server.
     *
     * @param userId The id of the user to unban.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(String userId) {
        try {
            return unbanUser(Long.parseLong(userId));
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Gets a collection with all server bans.
     *
     * @return A collection with all server bans.
     */
    default CompletableFuture<Collection<Ban>> getBans() {
        return new RestRequest<Collection<Ban>>(getApi(), RestMethod.GET, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Collection<Ban> bans = new ArrayList<>();
                    for (JsonNode ban : result.getJsonBody()) {
                        bans.add(new ImplBan(this, ban));
                    }
                    return bans;
                });
    }

    /**
     * Gets a list of all webhooks in this server.
     *
     * @return A list of all webhooks in this server.
     */
    default CompletableFuture<List<Webhook>> getWebhooks() {
        return new RestRequest<List<Webhook>>(getApi(), RestMethod.GET, RestEndpoint.SERVER_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    List<Webhook> webhooks = new ArrayList<>();
                    for (JsonNode webhookJson : result.getJsonBody()) {
                        webhooks.add(new ImplWebhook(getApi(), webhookJson));
                    }
                    return webhooks;
                });
    }

    /**
     * Gets the audit log of this server.
     *
     * @param limit The maximum amount of audit log entries.
     * @return The audit log.
     */
    default CompletableFuture<AuditLog> getAuditLog(int limit) {
        return new RestRequest<AuditLog>(getApi(), RestMethod.GET, RestEndpoint.AUDIT_LOG)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("limit", String.valueOf(limit))
                .execute(result -> new ImplAuditLog(getApi(), result.getJsonBody()));
    }

    /**
     * Checks if a user has a given permission.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     * This method also do not take into account overwritten permissions in some channels!
     *
     * @param user The user.
     * @param permission The permission to check.
     * @return Whether the user has the permission or not.
     */
    default boolean hasPermission(User user, PermissionType permission) {
        return getAllowedPermissionsOf(user).contains(permission);
    }

    /**
     * Gets a collection with all custom emojis of this server.
     *
     * @return A collection with all custom emojis of this server.
     */
    Collection<CustomEmoji> getCustomEmojis();

    /**
     * Gets a custom emoji in this server by it's id.
     *
     * @param id The id of the emoji.
     * @return The emoji with the given id.
     */
    default Optional<CustomEmoji> getCustomEmojiById(long id) {
        return getCustomEmojis().stream().filter(emoji -> emoji.getId() == id).findAny();
    }

    /**
     * Gets a custom emoji in this server by it's id.
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
     * Gets a collection of all custom emojis with the given name in the server.
     * This method is case sensitive!
     *
     * @param name The name of the custom emojis.
     * @return A collection of all custom emojis with the given name in this server.
     */
    default Collection<CustomEmoji> getCustomEmojisByName(String name) {
        return getCustomEmojis().stream()
                .filter(emoji -> emoji.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection of all custom emojis with the given name in the server.
     * This method is case insensitive!
     *
     * @param name The name of the custom emojis.
     * @return A collection of all custom emojis with the given name in this server.
     */
    default Collection<CustomEmoji> getCustomEmojisByNameIgnoreCase(String name) {
        return getCustomEmojis().stream()
                .filter(emoji -> emoji.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a new channel category builder.
     *
     * @return The builder to create a new channel category.
     */
    default ChannelCategoryBuilder getChannelCategoryBuilder() {
        return new ChannelCategoryBuilder(this);
    }

    /**
     * Gets a new server text channel builder.
     *
     * @return The builder to create a new server text channel.
     */
    default ServerTextChannelBuilder getTextChannelBuilder() {
        return new ServerTextChannelBuilder(this);
    }

    /**
     * Gets a new server voice channel builder.
     *
     * @return The builder to create a new server voice channel.
     */
    default ServerVoiceChannelBuilder getVoiceChannelBuilder() {
        return new ServerVoiceChannelBuilder(this);
    }

    /**
     * Gets a new role builder.
     *
     * @return The builder to create a new role.
     */
    default RoleBuilder getRoleBuilder() {
        return new RoleBuilder(this);
    }

    /**
     * Gets a sorted list (by position) with all channels of the server.
     *
     * @return A sorted list (by position) with all channels of the server.
     */
    List<ServerChannel> getChannels();

    /**
     * Gets a sorted list (by position) with all channel categories of the server.
     *
     * @return A sorted list (by position) with all channel categories of the server.
     */
    default List<ChannelCategory> getChannelCategories() {
        return ((ImplServer) this).getUnorderedChannels().stream()
                .filter(channel -> channel instanceof ChannelCategory)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .map(channel -> (ChannelCategory) channel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets a sorted list (by position) with all text channels of the server.
     *
     * @return A sorted list (by position) with all text channels of the server.
     */
    default List<ServerTextChannel> getTextChannels() {
        return ((ImplServer) this).getUnorderedChannels().stream()
                .filter(channel -> channel instanceof ServerTextChannel)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .map(channel -> (ServerTextChannel) channel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets a sorted list (by position) with all voice channels of the server.
     *
     * @return A sorted list (by position) with all voice channels of the server.
     */
    default List<ServerVoiceChannel> getVoiceChannels() {
        return ((ImplServer) this).getUnorderedChannels().stream()
                .filter(channel -> channel instanceof ServerVoiceChannel)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .map(channel -> (ServerVoiceChannel) channel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets a channel by it's id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    Optional<ServerChannel> getChannelById(long id);

    /**
     * Gets a channel by it's id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    default Optional<ServerChannel> getChannelById(String id) {
        try {
            return getChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a sorted list (by position) with all channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the channels.
     * @return A sorted list (by position) with all channels with the given name.
     */
    default List<ServerChannel> getChannelsByName(String name) {
        return getChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a sorted list (by position) with all channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the channels.
     * @return A sorted list (by position) with all channels with the given name.
     */
    default List<ServerChannel> getChannelsByNameIgnoreCase(String name) {
        return getChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a channel category by it's id.
     *
     * @param id The id of the channel category.
     * @return The channel category with the given id.
     */
    default Optional<ChannelCategory> getChannelCategoryById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof ChannelCategory)
                .map(channel -> (ChannelCategory) channel);
    }

    /**
     * Gets a channel category by it's id.
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
     * Gets a sorted list (by position) with all channel categories with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the channel categories.
     * @return A sorted list (by position) with all channel categories with the given name.
     */
    default List<ChannelCategory> getChannelCategoriesByName(String name) {
        return getChannelCategories().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a sorted list (by position) with all channel categories with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the channel categories.
     * @return A sorted list (by position) with all channel categories with the given name.
     */
    default List<ChannelCategory> getChannelCategoriesByNameIgnoreCase(String name) {
        return getChannelCategories().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a text channel by it's id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<ServerTextChannel> getTextChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof ServerTextChannel)
                .map(channel -> (ServerTextChannel) channel);
    }

    /**
     * Gets a text channel by it's id.
     *
     * @param id The id of the text channel.
     * @return The text channel with the given id.
     */
    default Optional<ServerTextChannel> getTextChannelById(String id) {
        try {
            return getTextChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a sorted list (by position) with all text channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the text channels.
     * @return A sorted list (by position) with all text channels with the given name.
     */
    default List<ServerTextChannel> getTextChannelsByName(String name) {
        return getTextChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a sorted list (by position) with all text channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the text channels.
     * @return A sorted list (by position) with all text channels with the given name.
     */
    default List<ServerTextChannel> getTextChannelsByNameIgnoreCase(String name) {
        return getTextChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a voice channel by it's id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getVoiceChannelById(long id) {
        return getChannelById(id)
                .filter(channel -> channel instanceof ServerVoiceChannel)
                .map(channel -> (ServerVoiceChannel) channel);
    }

    /**
     * Gets a voice channel by it's id.
     *
     * @param id The id of the voice channel.
     * @return The voice channel with the given id.
     */
    default Optional<ServerVoiceChannel> getVoiceChannelById(String id) {
        try {
            return getVoiceChannelById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a sorted list (by position) with all voice channels with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the voice channels.
     * @return A sorted list (by position) with all voice channels with the given name.
     */
    default List<ServerVoiceChannel> getVoiceChannelsByName(String name) {
        return getVoiceChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a sorted list (by position) with all voice channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the voice channels.
     * @return A sorted list (by position) with all voice channels with the given name.
     */
    default List<ServerVoiceChannel> getVoiceChannelsByNameIgnoreCase(String name) {
        return getVoiceChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Gets a sorted (by position) list with all channels of this server the given user can see.
     * Returns an empty list, if the user is not a member of this server.
     *
     * @param user The user to check.
     * @return The visible channels of this server.
     */
    default List<ServerChannel> getVisibleChannels(User user) {
        List<ServerChannel> channels = getChannels();
        channels.removeIf(channel -> !channel.canSee(user));
        return channels;
    }

    /**
     * Gets the highest role of the given user in this server.
     * The optional is empty, if the user is not a member of this server.
     *
     * @param user The user.
     * @return The highest role of the given user.
     */
    default Optional<Role> getHighestRoleOf(User user) {
        List<Role> roles = getRolesOf(user);
        if (roles.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(roles.get(roles.size() - 1));
    }

    /**
     * Checks if the given user is the owner of the server.
     *
     * @param user The user to check.
     * @return Whether the given user is the owner of the server or not.
     */
    default boolean isOwner(User user) {
        return getOwner() == user;
    }

    /**
     * Checks if the given user is an administrator of the server.
     *
     * @param user The user to check.
     * @return Whether the given user is an administrator of the server or not.
     */
    default boolean isAdmin(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR);
    }

    /**
     * Checks if the given user can create new channels.
     *
     * @param user The user to check.
     * @return Whether the given user can create channels or not.
     */
    default boolean canCreateChannels(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) ||
                hasPermissions(user, PermissionType.MANAGE_CHANNELS);
    }

    /**
     * Checks if the given user can view the audit log of the server.
     *
     * @param user The user to check.
     * @return Whether the given user view the audit log or not.
     */
    default boolean canViewAuditLog(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) ||
                hasPermissions(user, PermissionType.VIEW_AUDIT_LOG);
    }

    /**
     * Checks if the given user can change its own nickname in the server.
     *
     * @param user The user to check.
     * @return Whether the given user can change its own nickname or not.
     */
    default boolean canChangeOwnNickname(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) ||
                hasPermissions(user, PermissionType.CHANGE_NICKNAME) ||
                hasPermission(user, PermissionType.MANAGE_NICKNAMES);
    }

    /**
     * Checks if the given user can manage nicknames on the server.
     *
     * @param user The user to check.
     * @return Whether the given user can manage nicknames or not.
     */
    default boolean canManageNicknames(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) ||
                hasPermission(user, PermissionType.MANAGE_NICKNAMES);
    }

    /**
     * Checks if the given user can manage emojis on the server.
     *
     * @param user The user to check.
     * @return Whether the given user can manage emojis or not.
     */
    default boolean canManageEmojis(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) ||
                hasPermission(user, PermissionType.MANAGE_EMOJIS);
    }

    /**
     * Checks if the given user can kick users from the server.
     *
     * @param user The user to check.
     * @return Whether the given user can kick users or not.
     */
    default boolean canKickUsers(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) ||
                hasPermissions(user, PermissionType.KICK_MEMBERS);
    }

    /**
     * Checks if the given user can kick the other user.
     * This methods also considers the position of the user roles.
     *
     * @param user The user which "want" to kick the other user.
     * @param userToKick The user which should be kicked.
     * @return Whether the given user can kick the other user or not.
     */
    default boolean canKickUser(User user, User userToKick) {
        if (canKickUsers(user)) {
            return false;
        }
        Optional<Role> ownRole = getHighestRoleOf(user);
        Optional<Role> otherRole = getHighestRoleOf(userToKick);
        return ownRole.isPresent() && (!otherRole.isPresent() || ownRole.get().isHigherThan(otherRole.get()));
    }

    /**
     * Checks if the given user can ban users from the server.
     *
     * @param user The user to check.
     * @return Whether the given user can ban users or not.
     */
    default boolean canBanUsers(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) ||
                hasPermissions(user, PermissionType.BAN_MEMBERS);
    }

    /**
     * Checks if the given user can ban the other user.
     * This methods also considers the position of the user roles.
     *
     * @param user The user which "want" to ban the other user.
     * @param userToBan The user which should be banned.
     * @return Whether the given user can ban the other user or not.
     */
    default boolean canBanUser(User user, User userToBan) {
        if (canBanUsers(user)) {
            return false;
        }
        Optional<Role> ownRole = getHighestRoleOf(user);
        Optional<Role> otherRole = getHighestRoleOf(userToBan);
        return ownRole.isPresent() && (!otherRole.isPresent() || ownRole.get().isHigherThan(otherRole.get()));
    }

    /**
     * Adds a listener, which listens to message creates in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), MessageCreateListener.class, listener);
    }

    /**
     * Gets a list with all registered message create listeners.
     *
     * @return A list with all registered message create listeners.
     */
    default List<MessageCreateListener> getMessageCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), MessageCreateListener.class);
    }

    /**
     * Adds a listener, which listens to you leaving this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerLeaveListener> addServerLeaveListener(ServerLeaveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerLeaveListener.class, listener);
    }

    /**
     * Gets a list with all registered server leaves listeners.
     *
     * @return A list with all registered server leaves listeners.
     */
    default List<ServerLeaveListener> getServerLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerLeaveListener.class);
    }

    /**
     * Adds a listener, which listens to this server becoming unavailable.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerBecomesUnavailableListener> addServerBecomesUnavailableListener(
            ServerBecomesUnavailableListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerBecomesUnavailableListener.class, listener);
    }

    /**
     * Gets a list with all registered server becomes unavailable listeners.
     *
     * @return A list with all registered server becomes unavailable listeners.
     */
    default List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerBecomesUnavailableListener.class);
    }

    /**
     * Adds a listener, which listens to users starting to type in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserStartTypingListener.class, listener);
    }

    /**
     * Gets a list with all registered user starts typing listeners.
     *
     * @return A list with all registered user starts typing listeners.
     */
    default List<UserStartTypingListener> getUserStartTypingListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserStartTypingListener.class);
    }

    /**
     * Adds a listener, which listens to server channel creations in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelCreateListener> addServerChannelCreateListener(
            ServerChannelCreateListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelCreateListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel create listeners.
     *
     * @return A list with all registered server channel create listeners.
     */
    default List<ServerChannelCreateListener> getServerChannelCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChannelCreateListener.class);
    }

    /**
     * Adds a listener, which listens to server channel deletions in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelDeleteListener> addServerChannelDeleteListener(
            ServerChannelDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel delete listeners.
     *
     * @return A list with all registered server channel delete listeners.
     */
    default List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChannelDeleteListener.class);
    }

    /**
     * Adds a listener, which listens to message deletions in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), MessageDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered message delete listeners.
     *
     * @return A list with all registered message delete listeners.
     */
    default List<MessageDeleteListener> getMessageDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), MessageDeleteListener.class);
    }

    /**
     * Adds a listener, which listens to message edits in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), MessageEditListener.class, listener);
    }

    /**
     * Gets a list with all registered message edit listeners.
     *
     * @return A list with all registered message edit listeners.
     */
    default List<MessageEditListener> getMessageEditListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), MessageEditListener.class);
    }

    /**
     * Adds a listener, which listens to reactions being added on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ReactionAddListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction add listeners.
     *
     * @return A list with all registered reaction add listeners.
     */
    default List<ReactionAddListener> getReactionAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ReactionAddListener.class);
    }

    /**
     * Adds a listener, which listens to reactions being removed on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ReactionRemoveListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    default List<ReactionRemoveListener> getReactionRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ReactionRemoveListener.class);
    }

    /**
     * Adds a listener, which listens to all reactions being removed at once from a message on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(
            ReactionRemoveAllListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ReactionRemoveAllListener.class, listener);
    }

    /**
     * Gets a list with all registered reaction remove all listeners.
     *
     * @return A list with all registered reaction remove all listeners.
     */
    default List<ReactionRemoveAllListener> getReactionRemoveAllListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ReactionRemoveAllListener.class);
    }

    /**
     * Adds a listener, which listens to users joining this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerMemberJoinListener.class, listener);
    }

    /**
     * Gets a list with all registered server member join listeners.
     *
     * @return A list with all registered server member join listeners.
     */
    default List<ServerMemberJoinListener> getServerMemberJoinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerMemberJoinListener.class);
    }

    /**
     * Adds a listener, which listens to users leaving this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(
            ServerMemberLeaveListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerMemberLeaveListener.class, listener);
    }

    /**
     * Gets a list with all registered server member leave listeners.
     *
     * @return A list with all registered server member leave listeners.
     */
    default List<ServerMemberLeaveListener> getServerMemberLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerMemberLeaveListener.class);
    }

    /**
     * Adds a listener, which listens to server name changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeNameListener> addServerChangeNameListener(ServerChangeNameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered server change name listeners.
     *
     * @return A list with all registered server change name listeners.
     */
    default List<ServerChangeNameListener> getServerChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeNameListener.class);
    }

    /**
     * Adds a listener, which listens to server channel name changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelChangeNameListener> addServerChannelChangeNameListener(
            ServerChannelChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change name listeners.
     *
     * @return A list with all registered server channel change name listeners.
     */
    default List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChannelChangeNameListener.class);
    }

    /**
     * Adds a listener, which listens to server channel position changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelChangePositionListener> addServerChannelChangePositionListener(
            ServerChannelChangePositionListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelChangePositionListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change position listeners.
     *
     * @return A list with all registered server channel change position listeners.
     */
    default List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChannelChangePositionListener.class);
    }

    /**
     * Adds a listener, which listens to custom emoji creations in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CustomEmojiCreateListener> addCustomEmojiCreateListener(
            CustomEmojiCreateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CustomEmojiCreateListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji create listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    default List<CustomEmojiCreateListener> getCustomEmojiCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), CustomEmojiCreateListener.class);
    }

    /**
     * Adds a listener, which listens to game changes of users in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserChangeGameListener> addUserChangeGameListener(UserChangeGameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeGameListener.class, listener);
    }

    /**
     * Gets a list with all registered user change game listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    default List<UserChangeGameListener> getUserChangeGameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeGameListener.class);
    }

    /**
     * Adds a listener, which listens to status changes of users in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeStatusListener.class, listener);
    }

    /**
     * Gets a list with all registered user change status listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    default List<UserChangeStatusListener> getUserChangeStatusListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeStatusListener.class);
    }

    /**
     * Adds a listener, which listens to role permission changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangePermissionsListener.class, listener);
    }

    /**
     * Gets a list with all registered role change permissions listeners.
     *
     * @return A list with all registered role change permissions listeners.
     */
    default List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), RoleChangePermissionsListener.class);
    }

    /**
     * Adds a listener, which listens to role position changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(
            RoleChangePositionListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangePositionListener.class, listener);
    }

    /**
     * Gets a list with all registered role change position listeners.
     *
     * @return A list with all registered role change position listeners.
     */
    default List<RoleChangePositionListener> getRoleChangePositionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleChangePositionListener.class);
    }

    /**
     * Adds a listener, which listens to overwritten permission changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
    addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change overwritten permissions listeners.
     *
     * @return A list with all registered server channel change overwritten permissions listeners.
     */
    default List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    /**
     * Adds a listener, which listens to role creations in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleCreateListener> addRoleCreateListener(RoleCreateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), RoleCreateListener.class, listener);
    }

    /**
     * Gets a list with all registered role create listeners.
     *
     * @return A list with all registered role create listeners.
     */
    default List<RoleCreateListener> getRoleCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleCreateListener.class);
    }

    /**
     * Adds a listener, which listens to role deletions in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(Server.class, getId(), RoleDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered role delete listeners.
     *
     * @return A list with all registered role delete listeners.
     */
    default List<RoleDeleteListener> getRoleDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleDeleteListener.class);
    }

    /**
     * Adds a listener, which listens to user nickname changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(
            UserChangeNicknameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), UserChangeNicknameListener.class, listener);
    }

    /**
     * Gets a list with all registered user change nickname listeners.
     *
     * @return A list with all registered user change nickname listeners.
     */
    default List<UserChangeNicknameListener> getUserChangeNicknameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeNicknameListener.class);
    }

    /**
     * Adds a listener, which listens to server text channel topic changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerTextChannelChangeTopicListener.class, listener);
    }

    /**
     * Gets a list with all registered server text channel change topic listeners.
     *
     * @return A list with all registered server text channel change topic listeners.
     */
    default List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerTextChannelChangeTopicListener.class);
    }

    /**
     * Adds a listener, which listens to users being added to roles in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserRoleAddListener.class, listener);
    }

    /**
     * Gets a list with all registered user role add listeners.
     *
     * @return A list with all registered user role add listeners.
     */
    default List<UserRoleAddListener> getUserRoleAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserRoleAddListener.class);
    }

    /**
     * Adds a listener, which listens to users being removed from roles in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserRoleRemoveListener.class, listener);
    }

    /**
     * Gets a list with all registered user role remove listeners.
     *
     * @return A list with all registered user role remove listeners.
     */
    default List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserRoleRemoveListener.class);
    }

    /**
     * Adds a listener, which listens to members of this server changing their name.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered user change name listeners.
     *
     * @return A list with all registered user change name listeners.
     */
    default List<UserChangeNameListener> getUserChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeNameListener.class);
    }

}
