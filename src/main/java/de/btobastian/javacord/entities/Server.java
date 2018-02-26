package de.btobastian.javacord.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.auditlog.AuditLog;
import de.btobastian.javacord.entities.auditlog.impl.ImplAuditLog;
import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.ChannelCategoryBuilder;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannelBuilder;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannelBuilder;
import de.btobastian.javacord.entities.impl.ImplBan;
import de.btobastian.javacord.entities.impl.ImplInvite;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplWebhook;
import de.btobastian.javacord.entities.message.emoji.CustomEmojiBuilder;
import de.btobastian.javacord.entities.message.emoji.KnownCustomEmoji;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.PermissionsBuilder;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.RoleBuilder;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.message.CachedMessagePinListener;
import de.btobastian.javacord.listeners.message.CachedMessageUnpinListener;
import de.btobastian.javacord.listeners.message.ChannelPinsUpdateListener;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveAllListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerChangeAfkChannelListener;
import de.btobastian.javacord.listeners.server.ServerChangeAfkTimeoutListener;
import de.btobastian.javacord.listeners.server.ServerChangeDefaultMessageNotificationLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeExplicitContentFilterLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeIconListener;
import de.btobastian.javacord.listeners.server.ServerChangeMultiFactorAuthenticationLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeNameListener;
import de.btobastian.javacord.listeners.server.ServerChangeOwnerListener;
import de.btobastian.javacord.listeners.server.ServerChangeRegionListener;
import de.btobastian.javacord.listeners.server.ServerChangeSplashListener;
import de.btobastian.javacord.listeners.server.ServerChangeVerificationLevelListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNsfwFlagListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangePositionListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelCreateListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.server.channel.ServerTextChannelChangeTopicListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelChangeBitrateListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelChangeUserLimitListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelMemberJoinListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelMemberLeaveListener;
import de.btobastian.javacord.listeners.server.channel.WebhooksUpdateListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiChangeNameListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiChangeWhitelistedRolesListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiDeleteListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberBanListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberJoinListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberLeaveListener;
import de.btobastian.javacord.listeners.server.member.ServerMemberUnbanListener;
import de.btobastian.javacord.listeners.server.role.RoleChangeColorListener;
import de.btobastian.javacord.listeners.server.role.RoleChangeHoistListener;
import de.btobastian.javacord.listeners.server.role.RoleChangeMentionableListener;
import de.btobastian.javacord.listeners.server.role.RoleChangeNameListener;
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
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
     * Gets the display name of the user on this server.
     * If the user has a nickname, it will return the nickname, otherwise it will return the "normal" name.
     *
     * @param user The user.
     * @return The display name of the user on this server.
     */
    default String getDisplayName(User user) {
        return user.getDisplayName(this);
    }

    /**
     * Gets the timestamp of when a user joined the server.
     *
     * @param user The user to check.
     * @return The timestamp of when the user joined the server.
     */
    Optional<Instant> getJoinedAtTimestamp(User user);

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
     * Gets the multi factor authentication level of the server.
     *
     * @return The multi factor authentication level of the server.
     */
    MultiFactorAuthenticationLevel getMultiFactorAuthenticationLevel();

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
     * Gets the afk channel of the server.
     *
     * @return The afk channel of the server.
     */
    Optional<ServerVoiceChannel> getAfkChannel();

    /**
     * Gets the afk timeout in seconds of the server.
     *
     * @return The afk timeout in seconds of the server.
     */
    int getAfkTimeoutInSeconds();

    /**
     * Gets the amount of members without a role which were inactive at least the given amount of days.
     *
     * @param days The amount of days the member has to be inactive.
     * @return The amount of member who would get kicked.
     */
    default CompletableFuture<Integer> getPruneCount(int days) {
        return new RestRequest<Integer>(getApi(), RestMethod.GET, RestEndpoint.SERVER_PRUNE)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("days", String.valueOf(days))
                .execute(result -> result.getJsonBody().get("pruned").asInt());
    }

    /**
     * Kicks all members without a role which were inactive at least the given amount of days.
     *
     * @param days The amount of days the member has to be inactive.
     * @return The amount of member who got kicked.
     */
    default CompletableFuture<Integer> pruneMembers(int days) {
        return pruneMembers(days, null);
    }

    /**
     * Kicks all members without a role which were inactive at least the given amount of days.
     *
     * @param days The amount of days the member has to be inactive.
     * @param reason The audit log reason for the prune.
     * @return The amount of member who got kicked.
     */
    default CompletableFuture<Integer> pruneMembers(int days, String reason) {
        return new RestRequest<Integer>(getApi(), RestMethod.POST, RestEndpoint.SERVER_PRUNE)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("days", String.valueOf(days))
                .setAuditLogReason(reason)
                .execute(result -> result.getJsonBody().get("pruned").asInt());
    }

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
                    return Collections.unmodifiableCollection(invites);
                });
    }

    /**
     * Gets a collection with all members of the server.
     *
     * @return A collection with all members of the server.
     */
    Collection<User> getMembers();

    /**
     * Gets a member by its id.
     *
     * @param id The id of the member.
     * @return The member with the given id.
     */
    Optional<User> getMemberById(long id);

    /**
     * Gets a member by its id.
     *
     * @param id The id of the member.
     * @return The member with the given id.
     */
    default Optional<User> getMemberById(String id) {
        try {
            return getMemberById(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a member by its discriminated name like e. g. {@code Bastian#8222}.
     * This method is case sensitive!
     *
     * @param discriminatedName The discriminated name of the member.
     * @return The member with the given discriminated name.
     */
    default Optional<User> getMemberByDiscriminatedName(String discriminatedName) {
        String[] nameAndDiscriminator = discriminatedName.split("#", 2);
        return getMemberByNameAndDiscriminator(nameAndDiscriminator[0], nameAndDiscriminator[1]);
    }

    /**
     * Gets a member by its discriminated name like e. g. {@code Bastian#8222}.
     * This method is case insensitive!
     *
     * @param discriminatedName The discriminated name of the member.
     * @return The member with the given discriminated name.
     */
    default Optional<User> getMemberByDiscriminatedNameIgnoreCase(String discriminatedName) {
        String[] nameAndDiscriminator = discriminatedName.split("#", 2);
        return getMemberByNameAndDiscriminatorIgnoreCase(nameAndDiscriminator[0], nameAndDiscriminator[1]);
    }

    /**
     * Gets a member by its name and discriminator.
     * This method is case sensitive!
     *
     * @param name The name of the member.
     * @param discriminator The discriminator of the member.
     * @return The member with the given name and discriminator.
     */
    default Optional<User> getMemberByNameAndDiscriminator(String name, String discriminator) {
        return getMembersByName(name).stream()
                .filter(user -> user.getDiscriminator().equals(discriminator))
                .findAny();
    }

    /**
     * Gets a member by its name and discriminator.
     * This method is case insensitive!
     *
     * @param name The name of the member.
     * @param discriminator The discriminator of the member.
     * @return The member with the given name and discriminator.
     */
    default Optional<User> getMemberByNameAndDiscriminatorIgnoreCase(String name, String discriminator) {
        return getMembersByNameIgnoreCase(name).stream()
                .filter(user -> user.getDiscriminator().equalsIgnoreCase(discriminator))
                .findAny();
    }

    /**
     * Gets a collection with all members with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the members.
     * @return A collection with all members with the given name.
     */
    default Collection<User> getMembersByName(String name) {
        return Collections.unmodifiableList(
                getMembers().stream()
                        .filter(user -> user.getName().equals(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a collection with all members with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the members.
     * @return A collection with all members with the given name.
     */
    default Collection<User> getMembersByNameIgnoreCase(String name) {
        return Collections.unmodifiableList(
                getMembers().stream()
                        .filter(user -> user.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a collection with all members with the given nickname on this server.
     * This method is case sensitive!
     *
     * @param nickname The nickname of the members.
     * @return A collection with all members with the given nickname on this server.
     */
    default Collection<User> getMembersByNickname(String nickname) {
        return Collections.unmodifiableList(
                getMembers().stream()
                        .filter(user -> user.getNickname(this).map(nickname::equals).orElse(false))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a collection with all members with the given nickname on this server.
     * This method is case insensitive!
     *
     * @param nickname The nickname of the members.
     * @return A collection with all members with the given nickname on this server.
     */
    default Collection<User> getMembersByNicknameIgnoreCase(String nickname) {
        return Collections.unmodifiableList(
                getMembers().stream()
                        .filter(user -> user.getNickname(this).map(nickname::equalsIgnoreCase).orElse(false))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all roles of the server.
     *
     * @return A sorted list (by position) with all roles of the server.
     */
    List<Role> getRoles();

    /**
     * Gets a role by its id.
     *
     * @param id The id of the role.
     * @return The role with the given id.
     */
    Optional<Role> getRoleById(long id);

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
     * Gets the @everyone role.
     *
     * @return The @everyone role.
     */
    default Role getEveryoneRole() {
        return getRoleById(getId()).orElseThrow(AssertionError::new);
    }

    /**
     * Gets a sorted list (by position) with all roles with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the roles.
     * @return A sorted list (by position) with all roles with the given name.
     */
    default List<Role> getRolesByName(String name) {
        return Collections.unmodifiableList(
                getRoles().stream()
                        .filter(role -> role.getName().equals(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all roles with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the roles.
     * @return A sorted list (by position) with all roles with the given name.
     */
    default List<Role> getRolesByNameIgnoreCase(String name) {
        return Collections.unmodifiableList(
                getRoles().stream()
                        .filter(role -> role.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all roles of the user in the server.
     *
     * @param user The user.
     * @return A sorted list (by position) with all roles of the user in the server.
     */
    default List<Role> getRolesOf(User user) {
        return Collections.unmodifiableList(
                getRoles().stream()
                        .filter(role -> role.getUsers().contains(user))
                        .collect(Collectors.toList()));
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
        return Collections.unmodifiableCollection(allowed);
    }

    /**
     * Get the unset permissions of a given user.
     *
     * @param user The user.
     * @return The unset permissions of the given user.
     */
    default Collection<PermissionType> getUnsetPermissionsOf(User user) {
        if (getOwner() == user) {
            return Collections.emptySet();
        }
        Collection<PermissionType> unset = new HashSet<>();
        getRolesOf(user).forEach(role -> unset.addAll(role.getUnsetPermissions()));
        return Collections.unmodifiableCollection(unset);
    }

    /**
     * Checks if the user has a given set of permissions.
     *
     * @param user The user to check.
     * @param type The permission type(s) to check.
     * @return Whether the user has all given permissions or not.
     * @see #getAllowedPermissionsOf(User)
     */
    default boolean hasPermissions(User user, PermissionType... type) {
        return getAllowedPermissionsOf(user).containsAll(Arrays.asList(type));
    }

    /**
     * Checks if the user has any of a given set of permissions.
     *
     * @param user The user to check.
     * @param type The permission type(s) to check.
     * @return Whether the user has any of the given permissions or not.
     * @see #getAllowedPermissionsOf(User)
     */
    default boolean hasAnyPermission(User user, PermissionType... type) {
        return getAllowedPermissionsOf(user).stream().anyMatch(allowedPermissionType -> Arrays.stream(type).anyMatch(allowedPermissionType::equals));
    }

    /**
     * Gets a custom emoji builder to create custom emojis.
     *
     * @return A custom emoji builder to create custom emojis.
     */
    default CustomEmojiBuilder getCustomEmojiBuilder() {
        return new CustomEmojiBuilder(this);
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
     * This method assumes the file type is "png"!
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
     * Updates the icon of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(BufferedImage icon, String fileType) {
        return getUpdater().setIcon(icon, fileType).update();
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
    default CompletableFuture<Void> updateIcon(File icon) {
        return getUpdater().setIcon(icon).update();
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
    default CompletableFuture<Void> updateIcon(Icon icon) {
        return getUpdater().setIcon(icon).update();
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
    default CompletableFuture<Void> updateIcon(URL icon) {
        return getUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     * This method assumes the file type is "png"!
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(byte[] icon) {
        return getUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(byte[] icon, String fileType) {
        return getUpdater().setIcon(icon, fileType).update();
    }

    /**
     * Updates the icon of the server.
     * This method assumes the file type is "png"!
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(InputStream icon) {
        return getUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(InputStream icon, String fileType) {
        return getUpdater().setIcon(icon, fileType).update();
    }

    /**
     * Removes the icon of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the removal was successful.
     */
    default CompletableFuture<Void> removeIcon() {
        return getUpdater().removeIcon().update();
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
     * This method assumes the file type is "png"!
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
     * Updates the splash of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(BufferedImage splash, String fileType) {
        return getUpdater().setSplash(splash, fileType).update();
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
    default CompletableFuture<Void> updateSplash(File splash) {
        return getUpdater().setSplash(splash).update();
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
    default CompletableFuture<Void> updateSplash(Icon splash) {
        return getUpdater().setSplash(splash).update();
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
    default CompletableFuture<Void> updateSplash(URL splash) {
        return getUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     * This method assumes the file type is "png"!
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(byte[] splash) {
        return getUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(byte[] splash, String fileType) {
        return getUpdater().setSplash(splash, fileType).update();
    }

    /**
     * Updates the splash of the server.
     * This method assumes the file type is "png"!
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(InputStream splash) {
        return getUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(InputStream splash, String fileType) {
        return getUpdater().setSplash(splash, fileType).update();
    }

    /**
     * Removes the splash of the server.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the removal was successful.
     */
    default CompletableFuture<Void> removeSplash() {
        return getUpdater().removeSplash().update();
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
        return updateNickname(user, nickname, null);
    }

    /**
     * Changes the nickname of the given user.
     *
     * @param user The user.
     * @param nickname The new nickname of the user.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(User user, String nickname, String reason) {
        if (user.isYourself()) {
            return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.OWN_NICKNAME)
                    .setUrlParameters(String.valueOf(getId()))
                    .setBody(JsonNodeFactory.instance.objectNode().put("nick", nickname))
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.SERVER_MEMBER)
                    .setUrlParameters(String.valueOf(getId()), String.valueOf(user.getId()))
                    .setBody(JsonNodeFactory.instance.objectNode().put("nick", nickname))
                    .setAuditLogReason(reason)
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
     * Removes the nickname of the given user.
     *
     * @param user The user.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname(User user, String reason) {
        return updateNickname(user, null, reason);
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
        return updateRoles(user, roles, null);
    }

    /**
     * Updates the roles of a server member.
     * This will replace the roles of the server member with a provided collection.
     *
     * @param user The user to update the roles of.
     * @param roles The collection of roles to replace the user's roles.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRoles(User user, Collection<Role> roles, String reason) {
        ObjectNode updateNode = JsonNodeFactory.instance.objectNode();
        ArrayNode rolesJson = updateNode.putArray("roles");
        for (Role role : roles) {
            rolesJson.add(role.getIdAsString());
        }
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.SERVER_MEMBER)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .setBody(updateNode)
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    /**
     * Reorders the roles of the server.
     *
     * @param roles An ordered list with the new role positions.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> reorderRoles(List<Role> roles) {
        return reorderRoles(roles, null);
    }

    /**
     * Reorders the roles of the server.
     *
     * @param roles An ordered list with the new role positions.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> reorderRoles(List<Role> roles, String reason) {
        roles = new ArrayList<>(roles); // Copy the list to safely modify it
        ArrayNode body = JsonNodeFactory.instance.arrayNode();
        roles.removeIf(Role::isEveryoneRole);
        for (int i = 0; i < roles.size(); i++) {
            body.addObject()
                    .put("id", roles.get(i).getIdAsString())
                    .put("position", i + 1);
        }
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.ROLE)
                .setUrlParameters(getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    /**
     * Kicks the given user from the server.
     *
     * @param user The user to kick.
     * @return A future to check if the kick was successful.
     */
    default CompletableFuture<Void> kickUser(User user) {
        return kickUser(user, null);
    }

    /**
     * Kicks the given user from the server.
     *
     * @param user The user to kick.
     * @param reason The audit log reason for this action.
     * @return A future to check if the kick was successful.
     */
    default CompletableFuture<Void> kickUser(User user, String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_MEMBER)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    /**
     * Bans the given user from the server.
     *
     * @param user The user to ban.
     * @return A future to check if the ban was successful.
     */
    default CompletableFuture<Void> banUser(User user) {
        return banUser(user, 0, null);
    }

    /**
     * Bans the given user from the server.
     *
     * @param user The user to ban.
     * @param deleteMessageDays The number of days to delete messages for (0-7).
     * @return A future to check if the ban was successful.
     */
    default CompletableFuture<Void> banUser(User user, int deleteMessageDays) {
        return banUser(user, deleteMessageDays, null);
    }

    /**
     * Bans the given user from the server.
     *
     * @param user The user to ban.
     * @param deleteMessageDays The number of days to delete messages for (0-7).
     * @param reason The reason for the ban.
     * @return A future to check if the ban was successful.
     */
    default CompletableFuture<Void> banUser(User user, int deleteMessageDays, String reason) {
        RestRequest<Void> request = new RestRequest<Void>(getApi(), RestMethod.PUT, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .addQueryParameter("delete-message-days", String.valueOf(deleteMessageDays));
        if (reason != null) {
            request.addHeader("reason", reason);
        }
        return request.execute(result -> null);
    }

    /**
     * Unbans the given user from the server.
     *
     * @param user The user to ban.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(User user) {
        return unbanUser(user.getId(), null);
    }

    /**
     * Unbans the given user from the server.
     *
     * @param userId The id of the user to unban.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(long userId) {
        return unbanUser(userId, null);
    }

    /**
     * Unbans the given user from the server.
     *
     * @param userId The id of the user to unban.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(String userId) {
        return unbanUser(userId, null);
    }

    /**
     * Unbans the given user from the server.
     *
     * @param user The user to ban.
     * @param reason The audit log reason for this action.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(User user, String reason) {
        return unbanUser(user.getId(), reason);
    }

    /**
     * Unbans the given user from the server.
     *
     * @param userId The id of the user to unban.
     * @param reason The audit log reason for this action.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(long userId, String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), String.valueOf(userId))
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    /**
     * Unbans the given user from the server.
     *
     * @param userId The id of the user to unban.
     * @param reason The audit log reason for this action.
     * @return A future to check if the unban was successful.
     */
    default CompletableFuture<Void> unbanUser(String userId, String reason) {
        try {
            return unbanUser(Long.parseLong(userId), reason);
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
                    return Collections.unmodifiableCollection(bans);
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
                    return Collections.unmodifiableList(webhooks);
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
    Collection<KnownCustomEmoji> getCustomEmojis();

    /**
     * Gets a custom emoji in this server by its id.
     *
     * @param id The id of the emoji.
     * @return The emoji with the given id.
     */
    default Optional<KnownCustomEmoji> getCustomEmojiById(long id) {
        return getCustomEmojis().stream().filter(emoji -> emoji.getId() == id).findAny();
    }

    /**
     * Gets a custom emoji in this server by its id.
     *
     * @param id The id of the emoji.
     * @return The emoji with the given id.
     */
    default Optional<KnownCustomEmoji> getCustomEmojiById(String id) {
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
    default Collection<KnownCustomEmoji> getCustomEmojisByName(String name) {
        return Collections.unmodifiableList(
                getCustomEmojis().stream()
                        .filter(emoji -> emoji.getName().equals(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a collection of all custom emojis with the given name in the server.
     * This method is case insensitive!
     *
     * @param name The name of the custom emojis.
     * @return A collection of all custom emojis with the given name in this server.
     */
    default Collection<KnownCustomEmoji> getCustomEmojisByNameIgnoreCase(String name) {
        return Collections.unmodifiableCollection(
                getCustomEmojis().stream()
                        .filter(emoji -> emoji.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList()));
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
        return Collections.unmodifiableList(
                ((ImplServer) this).getUnorderedChannels().stream()
                        .filter(channel -> channel instanceof ChannelCategory)
                        .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                        .map(channel -> (ChannelCategory) channel)
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all text channels of the server.
     *
     * @return A sorted list (by position) with all text channels of the server.
     */
    default List<ServerTextChannel> getTextChannels() {
        return Collections.unmodifiableList(
                ((ImplServer) this).getUnorderedChannels().stream()
                        .filter(channel -> channel instanceof ServerTextChannel)
                        .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                        .map(channel -> (ServerTextChannel) channel)
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all voice channels of the server.
     *
     * @return A sorted list (by position) with all voice channels of the server.
     */
    default List<ServerVoiceChannel> getVoiceChannels() {
        return Collections.unmodifiableList(
                ((ImplServer) this).getUnorderedChannels().stream()
                        .filter(channel -> channel instanceof ServerVoiceChannel)
                        .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                        .map(channel -> (ServerVoiceChannel) channel)
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a channel by its id.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    Optional<ServerChannel> getChannelById(long id);

    /**
     * Gets a channel by its id.
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
        return Collections.unmodifiableList(
                getChannels().stream()
                        .filter(channel -> channel.getName().equals(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the channels.
     * @return A sorted list (by position) with all channels with the given name.
     */
    default List<ServerChannel> getChannelsByNameIgnoreCase(String name) {
        return Collections.unmodifiableList(
                getChannels().stream()
                        .filter(channel -> channel.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a channel category by its id.
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
     * Gets a sorted list (by position) with all channel categories with the given name.
     * This method is case sensitive!
     *
     * @param name The name of the channel categories.
     * @return A sorted list (by position) with all channel categories with the given name.
     */
    default List<ChannelCategory> getChannelCategoriesByName(String name) {
        return Collections.unmodifiableList(
                getChannelCategories().stream()
                        .filter(channel -> channel.getName().equals(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all channel categories with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the channel categories.
     * @return A sorted list (by position) with all channel categories with the given name.
     */
    default List<ChannelCategory> getChannelCategoriesByNameIgnoreCase(String name) {
        return Collections.unmodifiableList(
                getChannelCategories().stream()
                        .filter(channel -> channel.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a text channel by its id.
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
     * Gets a text channel by its id.
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
        return Collections.unmodifiableList(
                getTextChannels().stream()
                        .filter(channel -> channel.getName().equals(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all text channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the text channels.
     * @return A sorted list (by position) with all text channels with the given name.
     */
    default List<ServerTextChannel> getTextChannelsByNameIgnoreCase(String name) {
        return Collections.unmodifiableList(
                getTextChannels().stream()
                        .filter(channel -> channel.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a voice channel by its id.
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
     * Gets a voice channel by its id.
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
        return Collections.unmodifiableList(
                getVoiceChannels().stream()
                        .filter(channel -> channel.getName().equals(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a sorted list (by position) with all voice channels with the given name.
     * This method is case insensitive!
     *
     * @param name The name of the voice channels.
     * @return A sorted list (by position) with all voice channels with the given name.
     */
    default List<ServerVoiceChannel> getVoiceChannelsByNameIgnoreCase(String name) {
        return Collections.unmodifiableList(
                getVoiceChannels().stream()
                        .filter(channel -> channel.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets the voice channel the given user is connected to on this server if any.
     *
     * @param user The user to check.
     * @return The voice channel the user is connected to.
     */
    default Optional<ServerVoiceChannel> getConnectedVoiceChannel(User user) {
        return getVoiceChannels().stream()
                .filter(serverVoiceChannel -> serverVoiceChannel.getConnectedUsers().contains(user))
                .findAny();
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
        return Collections.unmodifiableList(channels);
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
        return hasPermission(user, PermissionType.ADMINISTRATOR);
    }

    /**
     * Checks if the given user can create new channels.
     *
     * @param user The user to check.
     * @return Whether the given user can create channels or not.
     */
    default boolean canCreateChannels(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.MANAGE_CHANNELS);
    }

    /**
     * Checks if the user of the connected account can create new channels.
     *
     * @return Whether the user of the connected account can create channels or not.
     */
    default boolean canYouCreateChannels() {
        return canCreateChannels(getApi().getYourself());
    }

    /**
     * Checks if the given user can view the audit log of the server.
     *
     * @param user The user to check.
     * @return Whether the given user can view the audit log or not.
     */
    default boolean canViewAuditLog(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.VIEW_AUDIT_LOG);
    }

    /**
     * Checks if the user of the connected account can view the audit log of the server.
     *
     * @return Whether the user of the connected account can view the audit log or not.
     */
    default boolean canYouViewAuditLog() {
        return canViewAuditLog(getApi().getYourself());
    }

    /**
     * Checks if the given user can change its own nickname in the server.
     *
     * @param user The user to check.
     * @return Whether the given user can change its own nickname or not.
     */
    default boolean canChangeOwnNickname(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.CHANGE_NICKNAME,
                                PermissionType.MANAGE_NICKNAMES);
    }

    /**
     * Checks if the user of the connected account can change its own nickname in the server.
     *
     * @return Whether the user of the connected account can change its own nickname or not.
     */
    default boolean canYouChangeOwnNickname() {
        return canChangeOwnNickname(getApi().getYourself());
    }

    /**
     * Checks if the given user can manage nicknames on the server.
     *
     * @param user The user to check.
     * @return Whether the given user can manage nicknames or not.
     */
    default boolean canManageNicknames(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.MANAGE_NICKNAMES);
    }

    /**
     * Checks if the user of the connected account can manage nicknames on the server.
     *
     * @return Whether the user of the connected account can manage nicknames or not.
     */
    default boolean canYouManageNicknames() {
        return canManageNicknames(getApi().getYourself());
    }

    /**
     * Checks if the given user can manage emojis on the server.
     *
     * @param user The user to check.
     * @return Whether the given user can manage emojis or not.
     */
    default boolean canManageEmojis(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.MANAGE_EMOJIS);
    }

    /**
     * Checks if the user of the connected account can manage emojis on the server.
     *
     * @return Whether the user of the connected account can manage emojis or not.
     */
    default boolean canYouManageEmojis() {
        return canManageEmojis(getApi().getYourself());
    }

    /**
     * Checks if the given user can manage the server.
     *
     * @param user The user to check.
     * @return Whether the given user can manage the server or not.
     */
    default boolean canManage(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.MANAGE_SERVER);
    }

    /**
     * Checks if the user of the connected account can manage the server.
     *
     * @return Whether the user of the connected account can manage the server or not.
     */
    default boolean canYouManage() {
        return canManage(getApi().getYourself());
    }

    /**
     * Checks if the given user can kick users from the server.
     *
     * @param user The user to check.
     * @return Whether the given user can kick users or not.
     */
    default boolean canKickUsers(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.KICK_MEMBERS);
    }

    /**
     * Checks if the user of the connected account can kick users from the server.
     *
     * @return Whether the user of the connected account can kick users or not.
     */
    default boolean canYouKickUsers() {
        return canKickUsers(getApi().getYourself());
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
     * Checks if the user of the connected account can kick the user.
     * This methods also considers the position of the user roles.
     *
     * @param userToKick The user which should be kicked.
     * @return Whether the user of the connected account can kick the user or not.
     */
    default boolean canYouKickUser(User userToKick) {
        return canKickUser(getApi().getYourself(), userToKick);
    }

    /**
     * Checks if the given user can ban users from the server.
     *
     * @param user The user to check.
     * @return Whether the given user can ban users or not.
     */
    default boolean canBanUsers(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.BAN_MEMBERS);
    }

    /**
     * Checks if the user of the connected account can ban users from the server.
     *
     * @return Whether the user of the connected account can ban users or not.
     */
    default boolean canYouBanUsers() {
        return canBanUsers(getApi().getYourself());
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
     * Checks if the user of the connected account can ban the user.
     * This methods also considers the position of the user roles.
     *
     * @param userToBan The user which should be banned.
     * @return Whether the user of the connected account can ban the user or not.
     */
    default boolean canYouBanUser(User userToBan) {
        return canBanUser(getApi().getYourself(), userToBan);
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
     * Adds a listener, which listens to users getting banned from this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerMemberBanListener.class, listener);
    }

    /**
     * Gets a list with all registered server member ban listeners.
     *
     * @return A list with all registered server member ban listeners.
     */
    default List<ServerMemberBanListener> getServerMemberBanListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerMemberBanListener.class);
    }

    /**
     * Adds a listener, which listens to users getting unbanned from this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerMemberUnbanListener.class, listener);
    }

    /**
     * Gets a list with all registered server member unban listeners.
     *
     * @return A list with all registered server member unban listeners.
     */
    default List<ServerMemberUnbanListener> getServerMemberUnbanListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerMemberUnbanListener.class);
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
     * Adds a listener, which listens to server icon changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeIconListener> addServerChangeIconListener(ServerChangeIconListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeIconListener.class, listener);
    }

    /**
     * Gets a list with all registered server change icon listeners.
     *
     * @return A list with all registered server change icon listeners.
     */
    default List<ServerChangeIconListener> getServerChangeIconListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeIconListener.class);
    }

    /**
     * Adds a listener, which listens to server splash changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeSplashListener> addServerChangeSplashListener(ServerChangeSplashListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeSplashListener.class, listener);
    }

    /**
     * Gets a list with all registered server change splash listeners.
     *
     * @return A list with all registered server change splash listeners.
     */
    default List<ServerChangeSplashListener> getServerChangeSplashListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeSplashListener.class);
    }

    /**
     * Adds a listener, which listens to server verification level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeVerificationLevelListener> addServerChangeVerificationLevelListener(
            ServerChangeVerificationLevelListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeVerificationLevelListener.class, listener);
    }

    /**
     * Gets a list with all registered server change verification level listeners.
     *
     * @return A list with all registered server change verification level listeners.
     */
    default List<ServerChangeVerificationLevelListener> getServerChangeVerificationLevelListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerChangeVerificationLevelListener.class);
    }

    /**
     * Adds a listener, which listens to server region changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeRegionListener> addServerChangeRegionListener(
            ServerChangeRegionListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeRegionListener.class, listener);
    }

    /**
     * Gets a list with all registered server change region listeners.
     *
     * @return A list with all registered server change region listeners.
     */
    default List<ServerChangeRegionListener> getServerChangeRegionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeRegionListener.class);
    }

    /**
     * Adds a listener, which listens to server default message notification level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeDefaultMessageNotificationLevelListener>
    addServerChangeDefaultMessageNotificationLevelListener(
            ServerChangeDefaultMessageNotificationLevelListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChangeDefaultMessageNotificationLevelListener.class, listener);
    }

    /**
     * Gets a list with all registered server change default message notification level listeners.
     *
     * @return A list with all registered server change default message notification level listeners.
     */
    default List<ServerChangeDefaultMessageNotificationLevelListener>
    getServerChangeDefaultMessageNotificationLevelListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerChangeDefaultMessageNotificationLevelListener.class);
    }

    /**
     * Adds a listener, which listens to server owner changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeOwnerListener> addServerChangeOwnerListener(
            ServerChangeOwnerListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeOwnerListener.class, listener);
    }

    /**
     * Gets a list with all registered server change owner listeners.
     *
     * @return A list with all registered server change owner listeners.
     */
    default List<ServerChangeOwnerListener> getServerChangeOwnerListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeOwnerListener.class);
    }

    /**
     * Adds a listener, which listens to server explicit content filter level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeExplicitContentFilterLevelListener>
    addServerChangeExplicitContentFilterLevelListener(ServerChangeExplicitContentFilterLevelListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChangeExplicitContentFilterLevelListener.class, listener);
    }

    /**
     * Gets a list with all registered server change explicit content filter level listeners.
     *
     * @return A list with all registered server change explicit content filter level listeners.
     */
    default List<ServerChangeExplicitContentFilterLevelListener> getServerChangeExplicitContentFilterLevelListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerChangeExplicitContentFilterLevelListener.class);
    }

    /**
     * Adds a listener, which listens to server multi factor authentication level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeMultiFactorAuthenticationLevelListener>
    addServerChangeMultiFactorAuthenticationLevelListener(ServerChangeMultiFactorAuthenticationLevelListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChangeMultiFactorAuthenticationLevelListener.class, listener);
    }

    /**
     * Gets a list with all registered server change multi factor authentication level listeners.
     *
     * @return A list with all registered server change multi factor authentication level listeners.
     */
    default List<ServerChangeMultiFactorAuthenticationLevelListener>
    getServerChangeMultiFactorAuthenticationLevelListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerChangeMultiFactorAuthenticationLevelListener.class);
    }

    /**
     * Adds a listener, which listens to afk channel changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeAfkChannelListener> addServerChangeAfkChannelListener(
            ServerChangeAfkChannelListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeAfkChannelListener.class, listener);
    }

    /**
     * Gets a list with all registered server change afk channel listeners.
     *
     * @return A list with all registered server change afk channel listeners.
     */
    default List<ServerChangeAfkChannelListener> getServerChangeAfkChannelListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChangeAfkChannelListener.class);
    }

    /**
     * Adds a listener, which listens to afk timeout changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChangeAfkTimeoutListener> addServerChangeAfkTimeoutListener(
            ServerChangeAfkTimeoutListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeAfkTimeoutListener.class, listener);
    }

    /**
     * Gets a list with all registered server change afk timeout listeners.
     *
     * @return A list with all registered server change afk timeout listeners.
     */
    default List<ServerChangeAfkTimeoutListener> getServerChangeAfkTimeoutListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChangeAfkTimeoutListener.class);
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
     * Adds a listener, which listens to server channel nsfw flag changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelChangeNsfwFlagListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change nsfw flag listeners.
     *
     * @return A list with all registered server channel change nsfw flag listeners.
     */
    default List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChannelChangeNsfwFlagListener.class);
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
     * Adds a listener, which listens to custom emoji name changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CustomEmojiChangeNameListener> addCustomEmojiChangeNameListener(
            CustomEmojiChangeNameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CustomEmojiChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji change name listeners.
     *
     * @return A list with all registered custom emoji change name listeners.
     */
    default List<CustomEmojiChangeNameListener> getCustomEmojiChangeNameListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), CustomEmojiChangeNameListener.class);
    }

    /**
     * Adds a listener, which listens to custom emoji whitelisted roles changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CustomEmojiChangeWhitelistedRolesListener> addCustomEmojiChangeWhitelistedRolesListener(
            CustomEmojiChangeWhitelistedRolesListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CustomEmojiChangeWhitelistedRolesListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji change whitelisted roles listeners.
     *
     * @return A list with all registered custom emoji change whitelisted roles listeners.
     */
    default List<CustomEmojiChangeWhitelistedRolesListener> getCustomEmojiChangeWhitelistedRolesListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), CustomEmojiChangeWhitelistedRolesListener.class);
    }

    /**
     * Adds a listener, which listens to custom emoji deletions in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CustomEmojiDeleteListener> addCustomEmojiDeleteListener(
            CustomEmojiDeleteListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CustomEmojiDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered custom emoji delete listeners.
     *
     * @return A list with all registered custom emoji delete listeners.
     */
    default List<CustomEmojiDeleteListener> getCustomEmojiDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), CustomEmojiDeleteListener.class);
    }

    /**
     * Adds a listener, which listens to activity changes of users in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(UserChangeActivityListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeActivityListener.class, listener);
    }

    /**
     * Gets a list with all registered user change activity listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    default List<UserChangeActivityListener> getUserChangeActivityListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeActivityListener.class);
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
     * Adds a listener, which listens to role color changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangeColorListener> addRoleChangeColorListener(RoleChangeColorListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangeColorListener.class, listener);
    }

    /**
     * Gets a list with all registered role change color listeners.
     *
     * @return A list with all registered role change color listeners.
     */
    default List<RoleChangeColorListener> getRoleChangeColorListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleChangeColorListener.class);
    }

    /**
     * Adds a listener, which listens to role hoist changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangeHoistListener> addRoleChangeHoistListener(RoleChangeHoistListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangeHoistListener.class, listener);
    }

    /**
     * Gets a list with all registered role change hoist listeners.
     *
     * @return A list with all registered role change hoist listeners.
     */
    default List<RoleChangeHoistListener> getRoleChangeHoistListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleChangeHoistListener.class);
    }

    /**
     * Adds a listener, which listens to role mentionable changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangeMentionableListener> addRoleChangeMentionableListener(
            RoleChangeMentionableListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangeMentionableListener.class, listener);
    }

    /**
     * Gets a list with all registered role change mentionable listeners.
     *
     * @return A list with all registered role change mentionable listeners.
     */
    default List<RoleChangeMentionableListener> getRoleChangeMentionableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), RoleChangeMentionableListener.class);
    }

    /**
     * Adds a listener, which listens to role name changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<RoleChangeNameListener> addRoleChangeNameListener(RoleChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered role change name listeners.
     *
     * @return A list with all registered role change name listeners.
     */
    default List<RoleChangeNameListener> getRoleChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleChangeNameListener.class);
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

    /**
     * Adds a listener, which listens to members of this server changing their avatar.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeAvatarListener.class, listener);
    }

    /**
     * Gets a list with all registered user change avatar listeners.
     *
     * @return A list with all registered user change avatar listeners.
     */
    default List<UserChangeAvatarListener> getUserChangeAvatarListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeAvatarListener.class);
    }

    /**
     * Adds a listener, which listens to users joining a server voice channel on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerVoiceChannelMemberJoinListener.class, listener);
    }

    /**
     * Gets a list with all registered server voice channel member join listeners.
     *
     * @return A list with all registered server voice channel member join listeners.
     */
    default List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerVoiceChannelMemberJoinListener.class);
    }

    /**
     * Adds a listener, which listens to users leaving a server voice channel on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerVoiceChannelMemberLeaveListener.class, listener);
    }

    /**
     * Gets a list with all registered server voice channel member leave listeners.
     *
     * @return A list with all registered server voice channel member leave listeners.
     */
    default List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerVoiceChannelMemberLeaveListener.class);
    }

    /**
     * Adds a listener, which listens to server voice channel bitrate changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerVoiceChannelChangeBitrateListener.class, listener);
    }

    /**
     * Gets a list with all registered server voice channel change bitrate listeners.
     *
     * @return A list with all registered server voice channel change bitrate listeners.
     */
    default List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerVoiceChannelChangeBitrateListener.class);
    }

    /**
     * Adds a listener, which listens to server voice channel user limit changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerVoiceChannelChangeUserLimitListener> addServerVoiceChannelChangeUserLimitListener(
            ServerVoiceChannelChangeUserLimitListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerVoiceChannelChangeUserLimitListener.class, listener);
    }

    /**
     * Gets a list with all registered server voice channel change user limit listeners.
     *
     * @return A list with all registered server voice channel change user limit listeners.
     */
    default List<ServerVoiceChannelChangeUserLimitListener> getServerVoiceChannelChangeUserLimitListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerVoiceChannelChangeUserLimitListener.class);
    }

    /**
     * Adds a listener, which listens to webhook updates on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<WebhooksUpdateListener> addWebhooksUpdateListener(WebhooksUpdateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), WebhooksUpdateListener.class, listener);
    }

    /**
     * Gets a list with all registered webhooks update listeners.
     *
     * @return A list with all registered webhooks update listeners.
     */
    default List<WebhooksUpdateListener> getWebhooksUpdateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), WebhooksUpdateListener.class);
    }

    /**
     * Adds a listener, which listens to all pin updates in channels of this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ChannelPinsUpdateListener> addChannelPinsUpdateListener(
            ChannelPinsUpdateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ChannelPinsUpdateListener.class, listener);
    }

    /**
     * Gets a list with all registered channel pins update listeners.
     *
     * @return A list with all registered channel pins update listeners.
     */
    default List<ChannelPinsUpdateListener> getChannelPinsUpdateListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ChannelPinsUpdateListener.class);
    }

    /**
     * Adds a listener, which listens to all cached message pins in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CachedMessagePinListener> addCachedMessagePinListener(CachedMessagePinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CachedMessagePinListener.class, listener);
    }

    /**
     * Gets a list with all registered cached message pin listeners.
     *
     * @return A list with all registered cached message pin listeners.
     */
    default List<CachedMessagePinListener> getCachedMessagePinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), CachedMessagePinListener.class);
    }

    /**
     * Adds a listener, which listens to all cached message unpins in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<CachedMessageUnpinListener> addCachedMessageUnpinListener(CachedMessageUnpinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CachedMessageUnpinListener.class, listener);
    }

    /**
     * Gets a list with all registered cached message unpin listeners.
     *
     * @return A list with all registered cached message unpin listeners.
     */
    default List<CachedMessageUnpinListener> getCachedMessageUnpinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), CachedMessageUnpinListener.class);
    }

    /**
     * Adds a listener that implements one or more {@code ServerAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends ServerAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addServerAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(Server.class, getId(),
                                                                                    listenerClass, listener))
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code ServerAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends ServerAttachableListener & ObjectAttachableListener> void removeServerAttachableListener(
            T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(Server.class, getId(),
                                                                                           listenerClass, listener));
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code ServerAttachableListener}s and their
     * assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code ServerAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    default <T extends ServerAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerAttachableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId());
    }

    /**
     * Removes a listener from this server.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends ServerAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(Server.class, getId(), listenerClass, listener);
    }

}
