package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.internal.permissions.ServerChannelTextPermissions;
import org.javacord.api.entity.webhook.WebhookBuilder;
import org.javacord.api.listener.channel.server.textable.TextableRegularServerChannelAttachableListenerManager;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TextableRegularServerChannel extends TextChannel, RegularServerChannel,
        ServerChannelTextPermissions, TextableRegularServerChannelAttachableListenerManager, Mentionable, Categorizable {

    @Override
    default boolean canWrite(User user) {
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasPermission(user, PermissionType.ADMINISTRATOR)
                        || regularServerChannel.hasPermissions(user, PermissionType.VIEW_CHANNEL,
                        PermissionType.SEND_MESSAGES)).orElse(false);
    }

    @Override
    default boolean canUseExternalEmojis(User user) {
        if (!canWrite(user)) {
            return false;
        }
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasAnyPermission(user,
                        PermissionType.ADMINISTRATOR,
                        PermissionType.USE_EXTERNAL_EMOJIS)).orElse(false);
    }

    @Override
    default boolean canEmbedLinks(User user) {
        if (!canWrite(user)) {
            return false;
        }
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasAnyPermission(user,
                        PermissionType.ADMINISTRATOR,
                        PermissionType.EMBED_LINKS)).orElse(false);
    }

    @Override
    default boolean canReadMessageHistory(User user) {
        if (!canSee(user)) {
            return false;
        }
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasAnyPermission(user,
                        PermissionType.ADMINISTRATOR,
                        PermissionType.READ_MESSAGE_HISTORY)).orElse(false);
    }

    @Override
    default boolean canUseTts(User user) {
        if (!canWrite(user)) {
            return false;
        }
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasAnyPermission(user,
                        PermissionType.ADMINISTRATOR,
                        PermissionType.READ_MESSAGE_HISTORY)).orElse(false);
    }

    @Override
    default boolean canAttachFiles(User user) {
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasPermission(user, PermissionType.ADMINISTRATOR)
                        || (regularServerChannel.hasPermission(user, PermissionType.ATTACH_FILES)
                        && canWrite(user))).orElse(false);
    }

    @Override
    default boolean canAddNewReactions(User user) {
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasPermission(user, PermissionType.ADMINISTRATOR)
                        || regularServerChannel.hasPermissions(user,
                        PermissionType.VIEW_CHANNEL,
                        PermissionType.READ_MESSAGE_HISTORY,
                        PermissionType.ADD_REACTIONS)).orElse(false);
    }

    @Override
    default boolean canManageMessages(User user) {
        if (!canSee(user)) {
            return false;
        }
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasAnyPermission(user,
                        PermissionType.ADMINISTRATOR,
                        PermissionType.MANAGE_MESSAGES)).orElse(false);
    }

    @Override
    default boolean canMentionEveryone(User user) {
        if (!canSee(user)) {
            return false;
        }
        return asRegularServerChannel().map(regularServerChannel ->
                regularServerChannel.hasPermission(user, PermissionType.ADMINISTRATOR)
                        || (regularServerChannel.hasPermission(user, PermissionType.MENTION_EVERYONE)
                        && canWrite(user))).orElse(false);
    }

    @Override
    default TextableRegularServerChannelUpdater createUpdater() {
        return new TextableRegularServerChannelUpdater(this);
    }

    /**
     * Checks if the channel is "not safe for work".
     *
     * @return Whether the channel is "not safe for work" or not.
     */
    boolean isNsfw();

    /**
     * Updates the nsfw flag of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNsfw(boolean nsfw) {
        return createUpdater().setNsfw(nsfw).update();
    }

    /**
     * Gets the delay for slowmode.
     *
     * @return The delay in seconds.
     */
    int getSlowmodeDelayInSeconds();

    /**
     * Check whether slowmode is activated for this channel.
     *
     * @return Whether this channel enforces a slowmode.
     */
    default boolean hasSlowmode() {
        return getSlowmodeDelayInSeconds() != 0;
    }

    /**
     * Set a slowmode for this channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link TextableRegularServerChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param delay The slowmode delay in seconds.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSlowmodeDelayInSeconds(int delay) {
        return createUpdater().setSlowmodeDelayInSeconds(delay).update();
    }

    /**
     * Deactivate slowmode for this channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link TextableRegularServerChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> unsetSlowmode() {
        return createUpdater().unsetSlowmode().update();
    }

    /**
     * {@inheritDoc}
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link TextableRegularServerChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return createUpdater().setCategory(category).update();
    }

    /**
     * {@inheritDoc}
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link TextableRegularServerChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return createUpdater().removeCategory().update();
    }

    @Override
    default Optional<? extends TextableRegularServerChannel> getCurrentCachedInstance() {
        return getApi().getTextableRegularServerChannelById(getId());
    }

    @Override
    default CompletableFuture<? extends TextableRegularServerChannel> getLatestInstance() {
        Optional<? extends TextableRegularServerChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<? extends TextableRegularServerChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

    @Override
    default String getMentionTag() {
        return "<#" + getIdAsString() + ">";
    }

    /**
     * Creates a webhook builder for this channel.
     *
     * @return A webhook builder.
     */
    default WebhookBuilder createWebhookBuilder() {
        return new WebhookBuilder(this);
    }

}
