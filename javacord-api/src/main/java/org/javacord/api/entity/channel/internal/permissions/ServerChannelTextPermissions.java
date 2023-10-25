package org.javacord.api.entity.channel.internal.permissions;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.permission.PermissionType;

public interface ServerChannelTextPermissions extends ServerChannelPermissions, Channel {

    /**
     * Checks if the given member can send messages in this channel.
     *
     * @param member The member to check.
     * @return Whether the given member can write messages or not.
     */
    default boolean canWrite(Member member) {
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || getPermissionableChannel().hasPermissions(member, PermissionType.VIEW_CHANNEL,
                PermissionType.SEND_MESSAGES);
    }

    /**
     * Checks if the member of the connected account can send messages in this channel.
     *
     * @return Whether the member of the connected account can write messages or not.
     */
    default boolean canYouWrite() {
        return canWrite(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member can use external emojis in this channel.
     * It also doesn't check if the member is even able to send any external emojis (twitch subscription or nitro).
     *
     * @param member The member to check.
     * @return Whether the given member can use external emojis or not.
     */
    default boolean canUseExternalEmojis(Member member) {
        if (!canWrite(member)) {
            return false;
        }
        return getPermissionableChannel().hasAnyPermission(member,
                PermissionType.ADMINISTRATOR,
                PermissionType.USE_EXTERNAL_EMOJIS);
    }

    /**
     * Checks if the member of the connected account can use external emojis in this channel.
     * It also doesn't check if the member is even able to send any external emojis (twitch subscription or nitro).
     *
     * @return Whether the member of the connected account can use external emojis or not.
     */
    default boolean canYouUseExternalEmojis() {
        return canUseExternalEmojis(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member can use embed links in this channel.
     *
     * @param member The member to check.
     * @return Whether the given member can embed links or not.
     */
    default boolean canEmbedLinks(Member member) {
        if (!canWrite(member)) {
            return false;
        }
        return getPermissionableChannel().hasAnyPermission(member,
                PermissionType.ADMINISTRATOR,
                PermissionType.EMBED_LINKS);
    }

    /**
     * Checks if the member of the connected account can use embed links in this channel.
     *
     * @return Whether the member of the connected account can embed links or not.
     */
    default boolean canYouEmbedLinks() {
        return canEmbedLinks(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member can read the message history of this channel.
     *
     * @param member The member to check.
     * @return Whether the given member can read the message history or not.
     */
    default boolean canReadMessageHistory(Member member) {
        if (!canSee(member.getUser())) {
            return false;
        }
        return getPermissionableChannel().hasAnyPermission(member,
                PermissionType.ADMINISTRATOR,
                PermissionType.READ_MESSAGE_HISTORY);
    }

    /**
     * Checks if the member of the connected account can read the message history of this channel.
     *
     * @return Whether the member of the connected account can read the message history or not.
     */
    default boolean canYouReadMessageHistory() {
        return canReadMessageHistory(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member can use tts (text to speech) in this channel.
     *
     * @param member The member to check.
     * @return Whether the given member can use tts or not.
     */
    default boolean canUseTts(Member member) {
        if (!canWrite(member)) {
            return false;
        }
        return getPermissionableChannel().hasAnyPermission(member,
                PermissionType.ADMINISTRATOR,
                PermissionType.READ_MESSAGE_HISTORY);
    }

    /**
     * Checks if the member of the connected account can use tts (text to speech) in this channel.
     *
     * @return Whether the member of the connected account can use tts or not.
     */
    default boolean canYouUseTts() {
        return canUseTts(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member can attach files in this channel.
     *
     * @param member The member to check.
     * @return Whether the given member can attach files or not.
     */
    default boolean canAttachFiles(Member member) {
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || (getPermissionableChannel().hasPermission(member, PermissionType.ATTACH_FILES)
                && canWrite(member));
    }

    /**
     * Checks if the member of the connected account can attach files in this channel.
     *
     * @return Whether the member of the connected account can attach files or not.
     */
    default boolean canYouAttachFiles() {
        return canAttachFiles(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member is allowed to add <b>new</b> reactions to messages in this channel.
     *
     * @param member The member to check.
     * @return Whether the given member is allowed to add <b>new</b> reactions to messages in this channel or not.
     */
    default boolean canAddNewReactions(Member member) {
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || getPermissionableChannel().hasPermissions(member,
                PermissionType.VIEW_CHANNEL,
                PermissionType.READ_MESSAGE_HISTORY,
                PermissionType.ADD_REACTIONS);
    }

    /**
     * Checks if the member of the connected account is allowed to add <b>new</b> reactions to messages in this channel.
     *
     * @return Whether the member of the connected account is allowed to add <b>new</b> reactions to messages in this
     *         channel or not.
     */
    default boolean canYouAddNewReactions() {
        return canAddNewReactions(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member can manage messages (delete or pin them or remove reactions of others)
     * in this channel.
     *
     * @param member The member to check.
     * @return Whether the given member can manage messages or not.
     */
    default boolean canManageMessages(Member member) {
        if (!canSee(member.getUser())) {
            return false;
        }
        return getPermissionableChannel().hasAnyPermission(member,
                PermissionType.ADMINISTRATOR,
                PermissionType.MANAGE_MESSAGES);
    }

    /**
     * Checks if the member of the connected account can manage messages (delete or pin them or remove reactions of
     * others) in this channel.
     *
     * @return Whether the member of the connected account can manage messages or not.
     */
    default boolean canYouManageMessages() {
        return canManageMessages(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member can remove reactions of other members in this channel.
     * This method just forwards to {@link #canManageMessages(Member)} as it is the same permission that is needed.
     *
     * @param member The member to check.
     * @return Whether the given member can remove reactions of others or not.
     */
    default boolean canRemoveReactionsOfOthers(Member member) {
        return canManageMessages(member);
    }

    /**
     * Checks if the member of the connected account can remove reactions of other members in this channel.
     *
     * @return Whether the member of the connected account can remove reactions of others or not.
     */
    default boolean canYouRemoveReactionsOfOthers() {
        return canRemoveReactionsOfOthers(getPermissionableChannel().getServer().getYourself());
    }

    /**
     * Checks if the given member can mention everyone (@everyone) in this channel.
     *
     * @param member The member to check.
     * @return Whether the given member can mention everyone (@everyone) or not.
     */
    default boolean canMentionEveryone(Member member) {
        if (!canSee(member.getUser())) {
            return false;
        }
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || (getPermissionableChannel().hasPermission(member, PermissionType.MENTION_EVERYONE)
                && canWrite(member));
    }

    /**
     * Checks if the member of the connected account can mention everyone (@everyone) in this channel.
     *
     * @return Whether the member of the connected account can mention everyone (@everyone) or not.
     */
    default boolean canYouMentionEveryone() {
        return canMentionEveryone(getPermissionableChannel().getServer().getYourself());
    }

}
