package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.WebhookBuilder;

public interface TextableRegularServerChannel extends TextChannel, Mentionable, Categorizable {

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
                        || (regularServerChannel.hasPermission(user, PermissionType.ATTACH_FILE)
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
    default String getMentionTag() {
        return "<#" + getIdAsString() + ">";
    }

    /**
     * Checks is the channel is "not safe for work".
     *
     * @return Whether the channel is "not safe for work" or not.
     */
    boolean isNsfw();

    /**
     * Creates a webhook builder for this channel.
     *
     * @return A webhook builder.
     */
    default WebhookBuilder createWebhookBuilder() {
        return new WebhookBuilder(this);
    }

}
