package org.javacord.api.entity.server;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.auditlog.AuditLog;
import org.javacord.api.entity.auditlog.AuditLogActionType;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelCategoryBuilder;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannelBuilder;
import org.javacord.api.entity.emoji.CustomEmojiBuilder;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.permission.RoleBuilder;
import org.javacord.api.entity.server.invite.RichInvite;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNameListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.api.listener.channel.server.ServerChannelChangePositionListener;
import org.javacord.api.listener.channel.server.ServerChannelCreateListener;
import org.javacord.api.listener.channel.server.ServerChannelDeleteListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.api.listener.channel.server.text.WebhooksUpdateListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.api.listener.message.CachedMessagePinListener;
import org.javacord.api.listener.message.CachedMessageUnpinListener;
import org.javacord.api.listener.message.ChannelPinsUpdateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.api.listener.message.MessageEditListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveAllListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.ServerBecomesUnavailableListener;
import org.javacord.api.listener.server.ServerChangeAfkChannelListener;
import org.javacord.api.listener.server.ServerChangeAfkTimeoutListener;
import org.javacord.api.listener.server.ServerChangeDefaultMessageNotificationLevelListener;
import org.javacord.api.listener.server.ServerChangeExplicitContentFilterLevelListener;
import org.javacord.api.listener.server.ServerChangeIconListener;
import org.javacord.api.listener.server.ServerChangeMultiFactorAuthenticationLevelListener;
import org.javacord.api.listener.server.ServerChangeNameListener;
import org.javacord.api.listener.server.ServerChangeOwnerListener;
import org.javacord.api.listener.server.ServerChangeRegionListener;
import org.javacord.api.listener.server.ServerChangeSplashListener;
import org.javacord.api.listener.server.ServerChangeSystemChannelListener;
import org.javacord.api.listener.server.ServerChangeVerificationLevelListener;
import org.javacord.api.listener.server.ServerLeaveListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeNameListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeWhitelistedRolesListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiCreateListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiDeleteListener;
import org.javacord.api.listener.server.member.ServerMemberBanListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
import org.javacord.api.listener.server.member.ServerMemberUnbanListener;
import org.javacord.api.listener.server.role.RoleChangeColorListener;
import org.javacord.api.listener.server.role.RoleChangeHoistListener;
import org.javacord.api.listener.server.role.RoleChangeMentionableListener;
import org.javacord.api.listener.server.role.RoleChangeNameListener;
import org.javacord.api.listener.server.role.RoleChangePermissionsListener;
import org.javacord.api.listener.server.role.RoleChangePositionListener;
import org.javacord.api.listener.server.role.RoleCreateListener;
import org.javacord.api.listener.server.role.RoleDeleteListener;
import org.javacord.api.listener.server.role.UserRoleAddListener;
import org.javacord.api.listener.server.role.UserRoleRemoveListener;
import org.javacord.api.listener.user.UserChangeActivityListener;
import org.javacord.api.listener.user.UserChangeAvatarListener;
import org.javacord.api.listener.user.UserChangeDeafenedListener;
import org.javacord.api.listener.user.UserChangeDiscriminatorListener;
import org.javacord.api.listener.user.UserChangeMutedListener;
import org.javacord.api.listener.user.UserChangeNameListener;
import org.javacord.api.listener.user.UserChangeNicknameListener;
import org.javacord.api.listener.user.UserChangeSelfDeafenedListener;
import org.javacord.api.listener.user.UserChangeSelfMutedListener;
import org.javacord.api.listener.user.UserChangeStatusListener;
import org.javacord.api.listener.user.UserStartTypingListener;
import org.javacord.api.util.event.ListenerManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The class represents a Discord server, sometimes also called guild.
 */
public interface Server extends DiscordEntity, UpdatableFromCache<Server> {

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
     * Gets your self-muted state.
     *
     * @return Whether you are self-muted.
     */
    default boolean areYouSelfMuted() {
        return isSelfMuted(getApi().getYourself());
    }

    /**
     * Gets the self-muted state of the user with the given id.
     *
     * @param userId The id of the user to check.
     * @return Whether the user with the given id is self-muted.
     */
    boolean isSelfMuted(long userId);

    /**
     * Gets the self-muted state of the given user.
     *
     * @param user The user to check.
     * @return Whether the given user is self-muted.
     */
    default boolean isSelfMuted(User user) {
        return isSelfMuted(user.getId());
    }

    /**
     * Gets your self-deafened state.
     *
     * @return Whether you are self-deafened.
     */
    default boolean areYouSelfDeafened() {
        return isSelfDeafened(getApi().getYourself());
    }

    /**
     * Gets the self-deafened state of the user with the given id.
     *
     * @param userId The id of the user to check.
     * @return Whether the user with the given id is self-deafened.
     */
    boolean isSelfDeafened(long userId);

    /**
     * Gets the self-deafened state of the given user.
     *
     * @param user The user to check.
     * @return Whether the given user is self-deafened.
     */
    default boolean isSelfDeafened(User user) {
        return isSelfDeafened(user.getId());
    }

    /**
     * Gets your muted state.
     *
     * @return Whether you are muted.
     */
    default boolean areYouMuted() {
        return isMuted(getApi().getYourself());
    }

    /**
     * Gets the muted state of the user with the given id.
     *
     * @param userId The id of the user to check.
     * @return Whether the user with the given id is muted.
     */
    boolean isMuted(long userId);

    /**
     * Gets the muted state of the given user.
     *
     * @param user The user to check.
     * @return Whether the given user is muted.
     */
    default boolean isMuted(User user) {
        return isMuted(user.getId());
    }

    /**
     * Gets your deafened state.
     *
     * @return Whether you are deafened.
     */
    default boolean areYouDeafened() {
        return isDeafened(getApi().getYourself());
    }

    /**
     * Gets the deafened state of the user with the given id.
     *
     * @param userId The id of the user to check.
     * @return Whether the user with the given id is deafened.
     */
    boolean isDeafened(long userId);

    /**
     * Gets the deafened state of the given user.
     *
     * @param user The user to check.
     * @return Whether the given user is deafened.
     */
    default boolean isDeafened(User user) {
        return isDeafened(user.getId());
    }

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
     * Checks if the server is considered large.
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
     * Gets the application id of the server's owner.
     * The application id is only present for bot-created servers.
     *
     * @return The application id of the server's owner.
     */
    Optional<Long> getApplicationId();

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
     * Gets the system channel of the server.
     *
     * @return The system channel of the server.
     */
    Optional<ServerTextChannel> getSystemChannel();

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
    CompletableFuture<Integer> getPruneCount(int days);

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
    CompletableFuture<Integer> pruneMembers(int days, String reason);

    /**
     * Gets the invites of the server.
     *
     * @return The invites of the server.
     */
    CompletableFuture<Collection<RichInvite>> getInvites();

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
     * Gets a collection with all members with the given display name on this server.
     * This method is case sensitive!
     *
     * @param displayName The display name of the members.
     * @return A collection with all members with the given display name on this server.
     */
    default Collection<User> getMembersByDisplayName(String displayName) {
        return Collections.unmodifiableList(
                getMembers().stream()
                        .filter(user -> user.getDisplayName(this).equals(displayName))
                        .collect(Collectors.toList()));
    }

    /**
     * Gets a collection with all members with the given display name on this server.
     * This method is case insensitive!
     *
     * @param displayName The display name of the members.
     * @return A collection with all members with the given display name on this server.
     */
    default Collection<User> getMembersByDisplayNameIgnoreCase(String displayName) {
        return Collections.unmodifiableList(
                getMembers().stream()
                        .filter(user -> user.getDisplayName(this).equalsIgnoreCase(displayName))
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
        return getAllowedPermissionsOf(user).stream()
                .anyMatch(allowedPermissionType -> Arrays.stream(type).anyMatch(allowedPermissionType::equals));
    }

    /**
     * Creates a custom emoji builder to create custom emojis.
     *
     * @return A custom emoji builder to create custom emojis.
     */
    default CustomEmojiBuilder createCustomEmojiBuilder() {
        return new CustomEmojiBuilder(this);
    }

    /**
     * Creates an updater for this server.
     *
     * @return An updater for this server.
     */
    default ServerUpdater createUpdater() {
        return new ServerUpdater(this);
    }

    /**
     * Updates the name of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param name The new name of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return createUpdater().setName(name).update();
    }

    /**
     * Updates the region of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param region The new region of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRegion(Region region) {
        return createUpdater().setRegion(region).update();
    }

    /**
     * Updates the explicit content filter level of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param explicitContentFilterLevel The new explicit content filter level of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateExplicitContentFilterLevel(
            ExplicitContentFilterLevel explicitContentFilterLevel) {
        return createUpdater().setExplicitContentFilterLevel(explicitContentFilterLevel).update();
    }

    /**
     * Updates the verification level of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param verificationLevel The new verification level of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateVerificationLevel(VerificationLevel verificationLevel) {
        return createUpdater().setVerificationLevel(verificationLevel).update();
    }

    /**
     * Updates the default message notification level of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param defaultMessageNotificationLevel The new default message notification level of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateDefaultMessageNotificationLevel(
            DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        return createUpdater().setDefaultMessageNotificationLevel(defaultMessageNotificationLevel).update();
    }

    /**
     * Updates the afk channel of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param afkChannel The new afk channel of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateAfkChannel(ServerVoiceChannel afkChannel) {
        return createUpdater().setAfkChannel(afkChannel).update();
    }

    /**
     * Removes the afk channel of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeAfkChannel() {
        return createUpdater().removeAfkChannel().update();
    }

    /**
     * Updates the afk timeout of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param afkTimeout The new afk timeout of the server in seconds.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateAfkTimeoutInSeconds(int afkTimeout) {
        return createUpdater().setAfkTimeoutInSeconds(afkTimeout).update();
    }

    /**
     * Updates the icon of the server.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(BufferedImage icon) {
        return createUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(BufferedImage icon, String fileType) {
        return createUpdater().setIcon(icon, fileType).update();
    }

    /**
     * Updates the icon of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(File icon) {
        return createUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(Icon icon) {
        return createUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(URL icon) {
        return createUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(byte[] icon) {
        return createUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(byte[] icon, String fileType) {
        return createUpdater().setIcon(icon, fileType).update();
    }

    /**
     * Updates the icon of the server.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(InputStream icon) {
        return createUpdater().setIcon(icon).update();
    }

    /**
     * Updates the icon of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param icon The new icon of the server.
     * @param fileType The type of the icon, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateIcon(InputStream icon, String fileType) {
        return createUpdater().setIcon(icon, fileType).update();
    }

    /**
     * Removes the icon of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the removal was successful.
     */
    default CompletableFuture<Void> removeIcon() {
        return createUpdater().removeIcon().update();
    }

    /**
     * Updates the owner of the server.
     * You must be the owner of this server in order to transfer it!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param owner The new owner of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateOwner(User owner) {
        return createUpdater().setOwner(owner).update();
    }

    /**
     * Updates the splash of the server.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(BufferedImage splash) {
        return createUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(BufferedImage splash, String fileType) {
        return createUpdater().setSplash(splash, fileType).update();
    }

    /**
     * Updates the splash of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(File splash) {
        return createUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(Icon splash) {
        return createUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(URL splash) {
        return createUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(byte[] splash) {
        return createUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(byte[] splash, String fileType) {
        return createUpdater().setSplash(splash, fileType).update();
    }

    /**
     * Updates the splash of the server.
     * This method assumes the file type is "png"!
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(InputStream splash) {
        return createUpdater().setSplash(splash).update();
    }

    /**
     * Updates the splash of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param splash The new splash of the server.
     * @param fileType The type of the splash, e.g. "png" or "jpg".
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSplash(InputStream splash, String fileType) {
        return createUpdater().setSplash(splash, fileType).update();
    }

    /**
     * Removes the splash of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the removal was successful.
     */
    default CompletableFuture<Void> removeSplash() {
        return createUpdater().removeSplash().update();
    }

    /**
     * Updates the system channel of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param systemChannel The new system channel of the server.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> setSystemChannel(ServerTextChannel systemChannel) {
        return createUpdater().setSystemChannel(systemChannel).update();
    }

    /**
     * Removes the system channel of the server.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeSystemChannel() {
        return createUpdater().removeSystemChannel().update();
    }

    /**
     * Changes the nickname of the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The user.
     * @param nickname The new nickname of the user.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(User user, String nickname) {
        return createUpdater().setNickname(user, nickname).update();
    }

    /**
     * Changes the nickname of the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The user.
     * @param nickname The new nickname of the user.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNickname(User user, String nickname, String reason) {
        return createUpdater().setNickname(user, nickname).setAuditLogReason(reason).update();
    }

    /**
     * Removes the nickname of the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The user.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname(User user) {
        return createUpdater().setNickname(user, null).update();
    }

    /**
     * Removes the nickname of the given user.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The user.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> resetNickname(User user, String reason) {
        return createUpdater().setNickname(user, null).setAuditLogReason(reason).update();
    }

    /**
     * Deletes the server.
     *
     * @return A future to check if the deletion was successful.
     */
    CompletableFuture<Void> delete();

    /**
     * Leaves the server.
     *
     * @return A future to check if the bot successfully left the server.
     */
    CompletableFuture<Void> leave();

    /**
     * Adds the given role to the given server member.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The server member the role should be added to.
     * @param role The role which should be added to the server member.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> addRoleToUser(User user, Role role) {
        return addRoleToUser(user, role, null);
    }

    /**
     * Adds the given role to the given server member.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The server member the role should be added to.
     * @param role The role which should be added to the server member.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> addRoleToUser(User user, Role role, String reason);

    /**
     * Removes the given role from the given server member.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The server member the role should be removed from.
     * @param role The role which should be removed from the server member.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeRoleFromUser(User user, Role role) {
        return removeRoleFromUser(user, role, null);
    }

    /**
     * Removes the given role from the given server member.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The server member the role should be removed from.
     * @param role The role which should be removed from the server member.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> removeRoleFromUser(User user, Role role, String reason);

    /**
     * Updates the roles of a server member.
     * This will replace the roles of the server member with a provided collection.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The user to update the roles of.
     * @param roles The collection of roles to replace the user's roles.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRoles(User user, Collection<Role> roles) {
        return createUpdater()
                .removeAllRolesFromUser(user)
                .addRolesToUser(user, roles)
                .update();
    }

    /**
     * Updates the roles of a server member.
     * This will replace the roles of the server member with a provided collection.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param user The user to update the roles of.
     * @param roles The collection of roles to replace the user's roles.
     * @param reason The audit log reason for this update.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateRoles(User user, Collection<Role> roles, String reason) {
        return createUpdater()
                .removeAllRolesFromUser(user)
                .addRolesToUser(user, roles)
                .setAuditLogReason(reason)
                .update();
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
    CompletableFuture<Void> reorderRoles(List<Role> roles, String reason);

    /**
     * Moves yourself to the given channel on the server.
     *
     * @param channel The channel to move the user to.
     * @return A future to check if the move was successful.
     */
    default CompletableFuture<Void> moveYourself(ServerVoiceChannel channel) {
        return moveUser(getApi().getYourself(), channel);
    }

    /**
     * Moves the given user to the given channel on the server.
     *
     * @param user The user to move.
     * @param channel The channel to move the user to.
     * @return A future to check if the move was successful.
     */
    default CompletableFuture<Void> moveUser(User user, ServerVoiceChannel channel) {
        return createUpdater().setVoiceChannel(user, channel).update();
    }

    /**
     * Mutes yourself locally for the server.
     *
     * <p>This cannot be undone by other users. If you want to mute yourself server-sidely, so that others can unmute
     * you, use {@link #muteYourself()}, {@link #muteUser(User)} or {@link #muteUser(User, String)}.
     *
     * @see #muteYourself()
     * @see #muteUser(User)
     * @see #muteUser(User, String)
     */
    void selfMute();

    /**
     * Unmutes yourself locally for the server.
     *
     * <p>This cannot be undone by other users. If you want to unmute yourself server-sidely, so that others can
     * mute you, use {@link #unmuteYourself()}, {@link #unmuteUser(User)} or {@link #unmuteUser(User, String)}.
     *
     * @see #unmuteYourself()
     * @see #unmuteUser(User)
     * @see #unmuteUser(User, String)
     */
    void selfUnmute();

    /**
     * Mutes yourself on the server.
     *
     * @return A future to check if the mute was successful.
     */
    default CompletableFuture<Void> muteYourself() {
        return muteUser(getApi().getYourself());
    }

    /**
     * Unmutes yourself on the server.
     *
     * @return A future to check if the unmute was successful.
     */
    default CompletableFuture<Void> unmuteYourself() {
        return unmuteUser(getApi().getYourself());
    }

    /**
     * Mutes the given user on the server.
     *
     * @param user The user to mute.
     * @return A future to check if the mute was successful.
     */
    default CompletableFuture<Void> muteUser(User user) {
        return createUpdater().setMuted(user, true).update();
    }

    /**
     * Mutes the given user on the server.
     *
     * @param user The user to mute.
     * @param reason The audit log reason for this action.
     * @return A future to check if the mute was successful.
     */
    default CompletableFuture<Void> muteUser(User user, String reason) {
        return createUpdater().setMuted(user, true).setAuditLogReason(reason).update();
    }

    /**
     * Unmutes the given user on the server.
     *
     * @param user The user to unmute.
     * @return A future to check if the unmute was successful.
     */
    default CompletableFuture<Void> unmuteUser(User user) {
        return createUpdater().setMuted(user, false).update();
    }

    /**
     * Unmutes the given user on the server.
     *
     * @param user The user to unmute.
     * @param reason The audit log reason for this action.
     * @return A future to check if the unmute was successful.
     */
    default CompletableFuture<Void> unmuteUser(User user, String reason) {
        return createUpdater().setMuted(user, false).setAuditLogReason(reason).update();
    }

    /**
     * Deafens yourself locally for the server.
     *
     * <p>This cannot be undone by other users. If you want to deafen yourself server-sidely, so that others can
     * undeafen you, use {@link #deafenYourself()}, {@link #deafenUser(User)} or {@link #deafenUser(User, String)}.
     *
     * @see #deafenYourself()
     * @see #deafenUser(User)
     * @see #deafenUser(User, String)
     */
    void selfDeafen();

    /**
     * Undeafens yourself locally for the server.
     *
     * <p>This cannot be undone by other users. If you want to undeafen yourself server-sidely, so that others can
     * deafen you, use {@link #undeafenYourself()}, {@link #undeafenUser(User)} or {@link #undeafenUser(User, String)}.
     *
     * @see #undeafenYourself()
     * @see #undeafenUser(User)
     * @see #undeafenUser(User, String)
     */
    void selfUndeafen();

    /**
     * Deafens yourself on the server.
     *
     * @return A future to check if the deafen was successful.
     */
    default CompletableFuture<Void> deafenYourself() {
        return deafenUser(getApi().getYourself());
    }

    /**
     * Undeafens yourself on the server.
     *
     * @return A future to check if the undeafen was successful.
     */
    default CompletableFuture<Void> undeafenYourself() {
        return undeafenUser(getApi().getYourself());
    }

    /**
     * Deafens the given user on the server.
     *
     * @param user The user to deafen.
     * @return A future to check if the deafen was successful.
     */
    default CompletableFuture<Void> deafenUser(User user) {
        return createUpdater().setDeafened(user, true).update();
    }

    /**
     * Deafens the given user on the server.
     *
     * @param user The user to deafen.
     * @param reason The audit log reason for this action.
     * @return A future to check if the deafen was successful.
     */
    default CompletableFuture<Void> deafenUser(User user, String reason) {
        return createUpdater().setDeafened(user, true).setAuditLogReason(reason).update();
    }

    /**
     * Undeafens the given user on the server.
     *
     * @param user The user to undeafen.
     * @return A future to check if the undeafen was successful.
     */
    default CompletableFuture<Void> undeafenUser(User user) {
        return createUpdater().setDeafened(user, false).update();
    }

    /**
     * Undeafens the given user on the server.
     *
     * @param user The user to undeafen.
     * @param reason The audit log reason for this action.
     * @return A future to check if the undeafen was successful.
     */
    default CompletableFuture<Void> undeafenUser(User user, String reason) {
        return createUpdater().setDeafened(user, false).setAuditLogReason(reason).update();
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
    CompletableFuture<Void> kickUser(User user, String reason);

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
    CompletableFuture<Void> banUser(User user, int deleteMessageDays, String reason);

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
    CompletableFuture<Void> unbanUser(long userId, String reason);

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
    CompletableFuture<Collection<Ban>> getBans();

    /**
     * Gets a list of all webhooks in this server.
     *
     * @return A list of all webhooks in this server.
     */
    CompletableFuture<List<Webhook>> getWebhooks();

    /**
     * Gets the audit log of this server.
     *
     * @param limit The maximum amount of audit log entries.
     * @return The audit log.
     */
    CompletableFuture<AuditLog> getAuditLog(int limit);

    /**
     * Gets the audit log of this server.
     *
     * @param limit The maximum amount of audit log entries.
     * @param type The action type of the audit log.
     * @return The audit log.
     */
    CompletableFuture<AuditLog> getAuditLog(int limit, AuditLogActionType type);

    /**
     * Gets the audit log of this server.
     *
     * @param limit The maximum amount of audit log entries.
     * @param before Filter the log before this entry.
     * @return The audit log.
     */
    CompletableFuture<AuditLog> getAuditLogBefore(int limit, AuditLogEntry before);

    /**
     * Gets the audit log of this server.
     *
     * @param limit The maximum amount of audit log entries.
     * @param before Filter the log before this entry.
     * @param type The action type of the audit log.
     * @return The audit log.
     */
    CompletableFuture<AuditLog> getAuditLogBefore(int limit, AuditLogEntry before, AuditLogActionType type);

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
     * Creates a new channel category builder.
     *
     * @return A builder to create a new channel category.
     */
    default ChannelCategoryBuilder createChannelCategoryBuilder() {
        return new ChannelCategoryBuilder(this);
    }

    /**
     * Creates a new server text channel builder.
     *
     * @return A builder to create a new server text channel.
     */
    default ServerTextChannelBuilder createTextChannelBuilder() {
        return new ServerTextChannelBuilder(this);
    }

    /**
     * Creates a new server voice channel builder.
     *
     * @return A builder to create a new server voice channel.
     */
    default ServerVoiceChannelBuilder createVoiceChannelBuilder() {
        return new ServerVoiceChannelBuilder(this);
    }

    /**
     * Creates a new role builder.
     *
     * @return A builder to create a new role.
     */
    default RoleBuilder createRoleBuilder() {
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
    List<ChannelCategory> getChannelCategories();

    /**
     * Gets a sorted list (by position) with all text channels of the server.
     *
     * @return A sorted list (by position) with all text channels of the server.
     */
    List<ServerTextChannel> getTextChannels();

    /**
     * Gets a sorted list (by position) with all voice channels of the server.
     *
     * @return A sorted list (by position) with all voice channels of the server.
     */
    List<ServerVoiceChannel> getVoiceChannels();

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
     * Gets the voice channel the user with the given id is connected to on this server if any.
     *
     * @param userId The id of the user to check.
     * @return The voice channel the user is connected to.
     */
    default Optional<ServerVoiceChannel> getConnectedVoiceChannel(long userId) {
        return getVoiceChannels().stream()
                .filter(serverVoiceChannel -> serverVoiceChannel.isConnected(userId))
                .findAny();
    }

    /**
     * Gets the voice channel the given user is connected to on this server if any.
     *
     * @param user The user to check.
     * @return The voice channel the user is connected to.
     */
    default Optional<ServerVoiceChannel> getConnectedVoiceChannel(User user) {
        return getConnectedVoiceChannel(user.getId());
    }

    /**
     * Gets a sorted (by position) list with all channels of this server the given user can see.
     * Returns an empty list, if the user is not a member of this server.
     *
     * @param user The user to check.
     * @return The visible channels of this server.
     */
    default List<ServerChannel> getVisibleChannels(User user) {
        return Collections.unmodifiableList(getChannels().stream()
                .filter(channel -> channel.canSee(user))
                .collect(Collectors.toList()));
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
     * Checks if the given user can mute members on the server.
     *
     * @param user The user to check.
     * @return Whether the given user can mute members or not.
     */
    default boolean canMuteMembers(User user) {
        return hasAnyPermission(user,
                                PermissionType.ADMINISTRATOR,
                                PermissionType.VOICE_MUTE_MEMBERS);
    }

    /**
     * Checks if the user of the connected account can mute members on the server.
     *
     * @return Whether the user of the connected account can mute members or not.
     */
    default boolean canYouMuteMembers() {
        return canMuteMembers(getApi().getYourself());
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
     * Checks if the given user can manage roles on the server.
     *
     * @param user The user to check.
     * @return Whether the given user can manage roles or not.
     */
    default boolean canManageRoles(User user) {
        return hasAnyPermission(user,
                PermissionType.ADMINISTRATOR,
                PermissionType.MANAGE_ROLES);
    }

    /**
     * Checks if the user of the connected account can manage roles on the server.
     *
     * @return Whether the user of the connected account can manage roles or not.
     */
    default boolean canYouManageRoles() {
        return canManageRoles(getApi().getYourself());
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
    ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener);

    /**
     * Gets a list with all registered message create listeners.
     *
     * @return A list with all registered message create listeners.
     */
    List<MessageCreateListener> getMessageCreateListeners();

    /**
     * Adds a listener, which listens to you leaving this server.
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
     * Adds a listener, which listens to this server becoming unavailable.
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
     * Adds a listener, which listens to users starting to type in this server.
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
     * Adds a listener, which listens to server channel creations in this server.
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
     * Adds a listener, which listens to server channel deletions in this server.
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
     * Adds a listener, which listens to message deletions in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener);

    /**
     * Gets a list with all registered message delete listeners.
     *
     * @return A list with all registered message delete listeners.
     */
    List<MessageDeleteListener> getMessageDeleteListeners();

    /**
     * Adds a listener, which listens to message edits in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener);

    /**
     * Gets a list with all registered message edit listeners.
     *
     * @return A list with all registered message edit listeners.
     */
    List<MessageEditListener> getMessageEditListeners();

    /**
     * Adds a listener, which listens to reactions being added on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener);

    /**
     * Gets a list with all registered reaction add listeners.
     *
     * @return A list with all registered reaction add listeners.
     */
    List<ReactionAddListener> getReactionAddListeners();

    /**
     * Adds a listener, which listens to reactions being removed on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener);

    /**
     * Gets a list with all registered reaction remove listeners.
     *
     * @return A list with all registered reaction remove listeners.
     */
    List<ReactionRemoveListener> getReactionRemoveListeners();

    /**
     * Adds a listener, which listens to all reactions being removed at once from a message on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(ReactionRemoveAllListener listener);

    /**
     * Gets a list with all registered reaction remove all listeners.
     *
     * @return A list with all registered reaction remove all listeners.
     */
    List<ReactionRemoveAllListener> getReactionRemoveAllListeners();

    /**
     * Adds a listener, which listens to users joining this server.
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
     * Adds a listener, which listens to users leaving this server.
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
     * Adds a listener, which listens to users getting banned from this server.
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
     * Adds a listener, which listens to users getting unbanned from this server.
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
     * Adds a listener, which listens to server splash changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeSplashListener> addServerChangeSplashListener(ServerChangeSplashListener listener);

    /**
     * Gets a list with all registered server change splash listeners.
     *
     * @return A list with all registered server change splash listeners.
     */
    List<ServerChangeSplashListener> getServerChangeSplashListeners();

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
     * Adds a listener, which listens to server multi factor authentication level changes.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeMultiFactorAuthenticationLevelListener>
            addServerChangeMultiFactorAuthenticationLevelListener(
                    ServerChangeMultiFactorAuthenticationLevelListener listener);

    /**
     * Gets a list with all registered server change multi factor authentication level listeners.
     *
     * @return A list with all registered server change multi factor authentication level listeners.
     */
    List<ServerChangeMultiFactorAuthenticationLevelListener> getServerChangeMultiFactorAuthenticationLevelListeners();

    /**
     * Adds a listener, which listens to system channel changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeSystemChannelListener> addServerChangeSystemChannelListener(
            ServerChangeSystemChannelListener listener);

    /**
     * Gets a list with all registered server change system channel listeners.
     *
     * @return A list with all registered server change system channel listeners.
     */
    List<ServerChangeSystemChannelListener> getServerChangeSystemChannelListeners();

    /**
     * Adds a listener, which listens to afk channel changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeAfkChannelListener> addServerChangeAfkChannelListener(
            ServerChangeAfkChannelListener listener);

    /**
     * Gets a list with all registered server change afk channel listeners.
     *
     * @return A list with all registered server change afk channel listeners.
     */
    List<ServerChangeAfkChannelListener> getServerChangeAfkChannelListeners();

    /**
     * Adds a listener, which listens to afk timeout changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChangeAfkTimeoutListener> addServerChangeAfkTimeoutListener(
            ServerChangeAfkTimeoutListener listener);

    /**
     * Gets a list with all registered server change afk timeout listeners.
     *
     * @return A list with all registered server change afk timeout listeners.
     */
    List<ServerChangeAfkTimeoutListener> getServerChangeAfkTimeoutListeners();

    /**
     * Adds a listener, which listens to server channel name changes in this server.
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
     * Adds a listener, which listens to server channel nsfw flag changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener);

    /**
     * Gets a list with all registered server channel change nsfw flag listeners.
     *
     * @return A list with all registered server channel change nsfw flag listeners.
     */
    List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners();

    /**
     * Adds a listener, which listens to server channel position changes in this server.
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
     * Adds a listener, which listens to custom emoji creations in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<KnownCustomEmojiCreateListener> addKnownCustomEmojiCreateListener(
            KnownCustomEmojiCreateListener listener);

    /**
     * Gets a list with all registered custom emoji create listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    List<KnownCustomEmojiCreateListener> getKnownCustomEmojiCreateListeners();

    /**
     * Adds a listener, which listens to custom emoji name changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<KnownCustomEmojiChangeNameListener> addKnownCustomEmojiChangeNameListener(
            KnownCustomEmojiChangeNameListener listener);

    /**
     * Gets a list with all registered custom emoji change name listeners.
     *
     * @return A list with all registered custom emoji change name listeners.
     */
    List<KnownCustomEmojiChangeNameListener> getKnownCustomEmojiChangeNameListeners();

    /**
     * Adds a listener, which listens to custom emoji whitelisted roles changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<KnownCustomEmojiChangeWhitelistedRolesListener> addKnownCustomEmojiChangeWhitelistedRolesListener(
            KnownCustomEmojiChangeWhitelistedRolesListener listener);

    /**
     * Gets a list with all registered custom emoji change whitelisted roles listeners.
     *
     * @return A list with all registered custom emoji change whitelisted roles listeners.
     */
    List<KnownCustomEmojiChangeWhitelistedRolesListener> getKnownCustomEmojiChangeWhitelistedRolesListeners();

    /**
     * Adds a listener, which listens to custom emoji deletions in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<KnownCustomEmojiDeleteListener> addKnownCustomEmojiDeleteListener(
            KnownCustomEmojiDeleteListener listener);

    /**
     * Gets a list with all registered custom emoji delete listeners.
     *
     * @return A list with all registered custom emoji delete listeners.
     */
    List<KnownCustomEmojiDeleteListener> getKnownCustomEmojiDeleteListeners();

    /**
     * Adds a listener, which listens to activity changes of users in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(UserChangeActivityListener listener);

    /**
     * Gets a list with all registered user change activity listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    List<UserChangeActivityListener> getUserChangeActivityListeners();

    /**
     * Adds a listener, which listens to status changes of users in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener);

    /**
     * Gets a list with all registered user change status listeners.
     *
     * @return A list with all registered custom emoji create listeners.
     */
    List<UserChangeStatusListener> getUserChangeStatusListeners();

    /**
     * Adds a listener, which listens to role color changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangeColorListener> addRoleChangeColorListener(RoleChangeColorListener listener);

    /**
     * Gets a list with all registered role change color listeners.
     *
     * @return A list with all registered role change color listeners.
     */
    List<RoleChangeColorListener> getRoleChangeColorListeners();

    /**
     * Adds a listener, which listens to role hoist changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangeHoistListener> addRoleChangeHoistListener(RoleChangeHoistListener listener);

    /**
     * Gets a list with all registered role change hoist listeners.
     *
     * @return A list with all registered role change hoist listeners.
     */
    List<RoleChangeHoistListener> getRoleChangeHoistListeners();

    /**
     * Adds a listener, which listens to role mentionable changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangeMentionableListener> addRoleChangeMentionableListener(
            RoleChangeMentionableListener listener);

    /**
     * Gets a list with all registered role change mentionable listeners.
     *
     * @return A list with all registered role change mentionable listeners.
     */
    List<RoleChangeMentionableListener> getRoleChangeMentionableListeners();

    /**
     * Adds a listener, which listens to role name changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<RoleChangeNameListener> addRoleChangeNameListener(RoleChangeNameListener listener);

    /**
     * Gets a list with all registered role change name listeners.
     *
     * @return A list with all registered role change name listeners.
     */
    List<RoleChangeNameListener> getRoleChangeNameListeners();

    /**
     * Adds a listener, which listens to role permission changes in this server.
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
     * Adds a listener, which listens to role position changes in this server.
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
     * Adds a listener, which listens to overwritten permission changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
            addServerChannelChangeOverwrittenPermissionsListener(
                    ServerChannelChangeOverwrittenPermissionsListener listener);

    /**
     * Gets a list with all registered server channel change overwritten permissions listeners.
     *
     * @return A list with all registered server channel change overwritten permissions listeners.
     */
    List<ServerChannelChangeOverwrittenPermissionsListener> getServerChannelChangeOverwrittenPermissionsListeners();

    /**
     * Adds a listener, which listens to role creations in this server.
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
     * Adds a listener, which listens to role deletions in this server.
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
     * Adds a listener, which listens to user nickname changes in this server.
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
     * Adds a listener, which listens to user self-muted changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeSelfMutedListener> addUserChangeSelfMutedListener(UserChangeSelfMutedListener listener);

    /**
     * Gets a list with all registered user change self-muted listeners.
     *
     * @return A list with all registered user change self-muted listeners.
     */
    List<UserChangeSelfMutedListener> getUserChangeSelfMutedListeners();

    /**
     * Adds a listener, which listens to user self-deafened changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeSelfDeafenedListener> addUserChangeSelfDeafenedListener(
            UserChangeSelfDeafenedListener listener);

    /**
     * Gets a list with all registered user change self-deafened listeners.
     *
     * @return A list with all registered user change self-deafened listeners.
     */
    List<UserChangeSelfDeafenedListener> getUserChangeSelfDeafenedListeners();

    /**
     * Adds a listener, which listens to user muted changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeMutedListener> addUserChangeMutedListener(UserChangeMutedListener listener);

    /**
     * Gets a list with all registered user change muted listeners.
     *
     * @return A list with all registered user change muted listeners.
     */
    List<UserChangeMutedListener> getUserChangeMutedListeners();

    /**
     * Adds a listener, which listens to user deafened changes in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeDeafenedListener> addUserChangeDeafenedListener(UserChangeDeafenedListener listener);

    /**
     * Gets a list with all registered user change deafened listeners.
     *
     * @return A list with all registered user change deafened listeners.
     */
    List<UserChangeDeafenedListener> getUserChangeDeafenedListeners();

    /**
     * Adds a listener, which listens to server text channel topic changes in this server.
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
     * Adds a listener, which listens to users being added to roles in this server.
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
     * Adds a listener, which listens to users being removed from roles in this server.
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
     * Adds a listener, which listens to members of this server changing their name.
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
     * Adds a listener, which listens to members of this server changing their discriminator.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<UserChangeDiscriminatorListener> addUserChangeDiscriminatorListener(
            UserChangeDiscriminatorListener listener);

    /**
     * Gets a list with all registered user change discriminator listeners.
     *
     * @return A list with all registered user change discriminator listeners.
     */
    List<UserChangeDiscriminatorListener> getUserChangeDiscriminatorListeners();

    /**
     * Adds a listener, which listens to members of this server changing their avatar.
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
     * Adds a listener, which listens to users joining a server voice channel on this server.
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
     * Adds a listener, which listens to server voice channel bitrate changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener);

    /**
     * Gets a list with all registered server voice channel change bitrate listeners.
     *
     * @return A list with all registered server voice channel change bitrate listeners.
     */
    List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners();

    /**
     * Adds a listener, which listens to server voice channel user limit changes on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerVoiceChannelChangeUserLimitListener> addServerVoiceChannelChangeUserLimitListener(
            ServerVoiceChannelChangeUserLimitListener listener);

    /**
     * Gets a list with all registered server voice channel change user limit listeners.
     *
     * @return A list with all registered server voice channel change user limit listeners.
     */
    List<ServerVoiceChannelChangeUserLimitListener> getServerVoiceChannelChangeUserLimitListeners();

    /**
     * Adds a listener, which listens to webhook updates on this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<WebhooksUpdateListener> addWebhooksUpdateListener(WebhooksUpdateListener listener);

    /**
     * Gets a list with all registered webhooks update listeners.
     *
     * @return A list with all registered webhooks update listeners.
     */
    List<WebhooksUpdateListener> getWebhooksUpdateListeners();

    /**
     * Adds a listener, which listens to all pin updates in channels of this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ChannelPinsUpdateListener> addChannelPinsUpdateListener(ChannelPinsUpdateListener listener);

    /**
     * Gets a list with all registered channel pins update listeners.
     *
     * @return A list with all registered channel pins update listeners.
     */
    List<ChannelPinsUpdateListener> getChannelPinsUpdateListeners();

    /**
     * Adds a listener, which listens to all cached message pins in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<CachedMessagePinListener> addCachedMessagePinListener(CachedMessagePinListener listener);

    /**
     * Gets a list with all registered cached message pin listeners.
     *
     * @return A list with all registered cached message pin listeners.
     */
    List<CachedMessagePinListener> getCachedMessagePinListeners();

    /**
     * Adds a listener, which listens to all cached message unpins in this server.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<CachedMessageUnpinListener> addCachedMessageUnpinListener(CachedMessageUnpinListener listener);

    /**
     * Gets a list with all registered cached message unpin listeners.
     *
     * @return A list with all registered cached message unpin listeners.
     */
    List<CachedMessageUnpinListener> getCachedMessageUnpinListeners();

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
    <T extends ServerAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addServerAttachableListener(T listener);

    /**
     * Removes a listener that implements one or more {@code ServerAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ServerAttachableListener & ObjectAttachableListener> void removeServerAttachableListener(T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code ServerAttachableListener}s and their
     * assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code ServerAttachableListener}s and
     * their assigned listener classes they listen to.
     */
    <T extends ServerAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getServerAttachableListeners();

    /**
     * Removes a listener from this server.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ServerAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

    @Override
    default Optional<Server> getCurrentCachedInstance() {
        return getApi().getServerById(getId());
    }

}
