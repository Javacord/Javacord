package org.javacord.api.entity.channel.internal.permissions;

import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.permission.PermissionType;

public interface ServerChannelVoicePermissions extends ServerChannelPermissions {

    /**
     * Checks if the given member is a priority speaker in this voice channel.
     *
     * @param member The member to check.
     * @return Whether the given member is a priority speaker or not.
     */
    default boolean isPrioritySpeaker(Member member) {
        return getPermissionableChannel().hasAnyPermission(member, PermissionType.ADMINISTRATOR,
                PermissionType.PRIORITY_SPEAKER)
                || getPermissionableChannel().hasPermissions(member, PermissionType.PRIORITY_SPEAKER,
                PermissionType.CONNECT);
    }

    /**
     * Checks if the given member can connect to the voice channel.
     *
     * @param member The member to check.
     * @return Whether the given member can connect or not.
     */
    default boolean canConnect(Member member) {
        return getPermissionableChannel().hasAnyPermission(member, PermissionType.ADMINISTRATOR,
                PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can connect to the voice channel.
     *
     * @return Whether the user of the connected account can connect or not.
     */
    default boolean canYouConnect() {
        return getPermissionableChannel().getServer().getMemberById(getPermissionableChannel().getApi().getClientId())
                .map(this::canConnect).orElse(false);
    }

    /**
     * Checks if the given member can mute users in this voice channel.
     *
     * @param member The member to check.
     * @return Whether the given member can mute users or not.
     */
    default boolean canMuteUsers(Member member) {
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || getPermissionableChannel().hasPermissions(member, PermissionType.MUTE_MEMBERS,
                PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can mute users in this voice channel.
     *
     * @return Whether the user of the connected account can mute users or not.
     */
    default boolean canYouMuteUsers() {
        return getPermissionableChannel().getServer().getMemberById(getPermissionableChannel().getApi().getClientId())
                .map(this::canMuteUsers).orElse(false);
    }

    /**
     * Checks if the given member can speak in this voice channel.
     *
     * @param member The member to check.
     * @return Whether the given member can speak or not.
     */
    default boolean canSpeak(Member member) {
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || getPermissionableChannel().hasPermissions(member, PermissionType.SPEAK, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can speak in this voice channel.
     *
     * @return Whether the user of the connected account can speak or not.
     */
    default boolean canYouSpeak() {
        return getPermissionableChannel().getServer().getMemberById(getPermissionableChannel().getApi().getClientId())
                .map(this::canSpeak).orElse(false);
    }

    /**
     * Checks if the given member can use video in this voice channel.
     *
     * @param member The member to check.
     * @return Whether the given member can use video or not.
     */
    default boolean canUseVideo(Member member) {
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || getPermissionableChannel().hasPermissions(member, PermissionType.STREAM, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can use video in this voice channel.
     *
     * @return Whether the user of the connected account can use video or not.
     */
    default boolean canYouUseVideo() {
        return getPermissionableChannel().getServer().getMemberById(getPermissionableChannel().getApi().getClientId())
                .map(this::canUseVideo).orElse(false);
    }

    /**
     * Checks if the given member can move users in this voice channel.
     *
     * @param member The member to check.
     * @return Whether the given member can move users or not.
     */
    default boolean canMoveUsers(Member member) {
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || getPermissionableChannel().hasPermissions(member, PermissionType.MOVE_MEMBERS,
                PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can move users in this voice channel.
     *
     * @return Whether the user of the connected account can move users or not.
     */
    default boolean canYouMoveUsers() {
        return getPermissionableChannel().getServer().getMemberById(getPermissionableChannel().getApi().getClientId())
                .map(this::canMoveUsers).orElse(false);
    }

    /**
     * Checks if the given member can use voice activation in this voice channel.
     *
     * @param member The member to check.
     * @return Whether the given member can use voice activation or not.
     */
    default boolean canUseVoiceActivation(Member member) {
        return getPermissionableChannel().hasAnyPermission(member, PermissionType.ADMINISTRATOR)
                || getPermissionableChannel().hasPermissions(member, PermissionType.USE_VAD,
                PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can use voice activation in this voice channel.
     *
     * @return Whether the user of the connected account can use voice activation or not.
     */
    default boolean canYouUseVoiceActivation() {
        return getPermissionableChannel().getServer().getMemberById(getPermissionableChannel().getApi().getClientId())
                .map(this::canUseVoiceActivation).orElse(false);
    }

    /**
     * Checks if the given member can deafen users in this voice channel.
     *
     * @param member The member to check.
     * @return Whether the given member can deafen users or not.
     */
    default boolean canDeafenUsers(Member member) {
        return getPermissionableChannel().hasPermission(member, PermissionType.ADMINISTRATOR)
                || getPermissionableChannel().hasPermissions(member, PermissionType.DEAFEN_MEMBERS,
                PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can deafen users in this voice channel.
     *
     * @return Whether the user of the connected account can deafen users or not.
     */
    default boolean canYouDeafenUsers() {
        return getPermissionableChannel().getServer().getMemberById(getPermissionableChannel().getApi().getClientId())
                .map(this::canDeafenUsers).orElse(false);
    }

}
