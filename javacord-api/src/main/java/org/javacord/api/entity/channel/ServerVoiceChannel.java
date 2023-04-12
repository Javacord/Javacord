package org.javacord.api.entity.channel;

import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelAttachableListenerManager;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server voice channel.
 */
public interface ServerVoiceChannel extends RegularServerChannel, VoiceChannel, TextableRegularServerChannel,
                                            ServerVoiceChannelAttachableListenerManager {

    /**
     * Connects to the voice channel self-deafened and disconnects any existing connections in the server.
     *
     * @return The audio connection.
     */
    default CompletableFuture<AudioConnection> connect() {
        return connect(false, true);
    }

    /**
     * Connects to the voice channel and disconnects any existing connections in the server.
     *
     * @param muted Whether to connect self-muted.
     * @param deafened Whether to connect self-deafened.
     * @return The audio connection.
     */
    CompletableFuture<AudioConnection> connect(boolean muted, boolean deafened);

    /**
     * Disconnects from the voice channel if connected.
     *
     * @return A CompletableFuture which completes when the connection has been disconnected.
     */
    default CompletableFuture<Void> disconnect() {
        return getServer()
                .getAudioConnection()
                .filter(audioConnection -> equals(audioConnection.getChannel()))
                .map(AudioConnection::close)
                .orElseGet(() -> CompletableFuture.completedFuture(null));
    }

    /**
     * Checks if the channel is "not safe for work".
     *
     * @return Whether the channel is "not safe for work" or not.
     */
    boolean isNsfw();

    /**
     * Gets the bitrate (int bits) of the channel.
     *
     * @return The bitrate of the channel.
     */
    int getBitrate();

    /**
     * Gets the user limit of the channel.
     *
     * @return The user limit.
     */
    Optional<Integer> getUserLimit();

    /**
     * Gets the ids of the users that are connected to this server voice channel.
     *
     * @return The ids of the users that are connected to this server voice channel.
     */
    Set<Long> getConnectedUserIds();

    /**
     * Gets the users that are connected to this server voice channel.
     *
     * @return The users that are connected to this server voice channel.
     */
    Set<User> getConnectedUsers();

    /**
     * Checks whether the user with the given id is connected to this channel.
     *
     * @param userId The id of the user to check.
     * @return Whether the user with the given id is connected to this channel or not.
     */
    boolean isConnected(long userId);

    /**
     * Checks whether the given user is connected to this channel.
     *
     * @param user The user to check.
     * @return Whether the given user is connected to this channel or not.
     */
    default boolean isConnected(User user) {
        return isConnected(user.getId());
    }

    /**
     * Checks if the given user is a priority speaker in this voice channel.
     *
     * @param user The user to check.
     * @return Whether the given user is a priority speaker or not.
     */
    default boolean isPrioritySpeaker(User user) {
        return hasAnyPermission(user, PermissionType.ADMINISTRATOR, PermissionType.PRIORITY_SPEAKER)
                || hasPermissions(user, PermissionType.PRIORITY_SPEAKER, PermissionType.CONNECT);
    }

    /**
     * Checks if the given user can connect to the voice channel.
     *
     * @param user The user to check.
     * @return Whether the given user can connect or not.
     */
    default boolean canConnect(User user) {
        return hasAnyPermission(user, PermissionType.ADMINISTRATOR, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can connect to the voice channel.
     *
     * @return Whether the user of the connected account can connect or not.
     */
    default boolean canYouConnect() {
        return canConnect(getApi().getYourself());
    }

    /**
     * Checks if the given user can mute users in this voice channel.
     *
     * @param user The user to check.
     * @return Whether the given user can mute users or not.
     */
    default boolean canMuteUsers(User user) {
        return hasPermission(user, PermissionType.ADMINISTRATOR)
                || hasPermissions(user, PermissionType.MUTE_MEMBERS, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can mute users in this voice channel.
     *
     * @return Whether the user of the connected account can mute users or not.
     */
    default boolean canYouMuteUsers() {
        return canMuteUsers(getApi().getYourself());
    }

    /**
     * Checks if the given user can speak in this voice channel.
     *
     * @param user The user to check.
     * @return Whether the given user can speak or not.
     */
    default boolean canSpeak(User user) {
        return hasPermission(user, PermissionType.ADMINISTRATOR)
                || hasPermissions(user, PermissionType.SPEAK, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can speak in this voice channel.
     *
     * @return Whether the user of the connected account can speak or not.
     */
    default boolean canYouSpeak() {
        return canSpeak(getApi().getYourself());
    }

    /**
     * Checks if the given user can use video in this voice channel.
     *
     * @param user The user to check.
     * @return Whether the given user can use video or not.
     */
    default boolean canUseVideo(User user) {
        return hasPermission(user, PermissionType.ADMINISTRATOR)
                || hasPermissions(user, PermissionType.STREAM, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can use video in this voice channel.
     *
     * @return Whether the user of the connected account can use video or not.
     */
    default boolean canYouUseVideo() {
        return canUseVideo(getApi().getYourself());
    }

    /**
     * Checks if the given user can move users in this voice channel.
     *
     * @param user The user to check.
     * @return Whether the given user can move users or not.
     */
    default boolean canMoveUsers(User user) {
        return hasPermission(user, PermissionType.ADMINISTRATOR)
                || hasPermissions(user, PermissionType.MOVE_MEMBERS, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can move users in this voice channel.
     *
     * @return Whether the user of the connected account can move users or not.
     */
    default boolean canYouMoveUsers() {
        return canMoveUsers(getApi().getYourself());
    }

    /**
     * Checks if the given user can use voice activation in this voice channel.
     *
     * @param user The user to check.
     * @return Whether the given user can use voice activation or not.
     */
    default boolean canUseVoiceActivation(User user) {
        return hasAnyPermission(user, PermissionType.ADMINISTRATOR)
                || hasPermissions(user, PermissionType.USE_VAD, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can use voice activation in this voice channel.
     *
     * @return Whether the user of the connected account can use voice activation or not.
     */
    default boolean canYouUseVoiceActivation() {
        return canUseVoiceActivation(getApi().getYourself());
    }

    /**
     * Checks if the given user can deafen users in this voice channel.
     *
     * @param user The user to check.
     * @return Whether the given user can deafen users or not.
     */
    default boolean canDeafenUsers(User user) {
        return hasPermission(user, PermissionType.ADMINISTRATOR)
                || hasPermissions(user, PermissionType.DEAFEN_MEMBERS, PermissionType.CONNECT);
    }

    /**
     * Checks if the user of the connected account can deafen users in this voice channel.
     *
     * @return Whether the user of the connected account can deafen users or not.
     */
    default boolean canYouDeafenUsers() {
        return canDeafenUsers(getApi().getYourself());
    }

    /**
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default ServerVoiceChannelUpdater createUpdater() {
        return new ServerVoiceChannelUpdater(this);
    }

    /**
     * Updates the bitrate of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param bitrate The new bitrate of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateBitrate(int bitrate) {
        return createUpdater().setBitrate(bitrate).update();
    }

    /**
     * Updates the user limit of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param userLimit The new user limit of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateUserLimit(int userLimit) {
        return createUpdater().setUserLimit(userLimit).update();
    }

    /**
     * Removes the user limit of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeUserLimit() {
        return createUpdater().removeUserLimit().update();
    }

    /**
     * {@inheritDoc}
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
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
     * {@link ServerVoiceChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return createUpdater().removeCategory().update();
    }

    /**
     * Updates the nsfw flag of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNsfwFlag(boolean nsfw) {
        return createUpdater().setNsfw(nsfw).update();
    }

    @Override
    default Optional<? extends ServerVoiceChannel> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getVoiceChannelById(getId()));
    }

    @Override
    default CompletableFuture<? extends ServerVoiceChannel> getLatestInstance() {
        Optional<? extends ServerVoiceChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<ServerVoiceChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}
