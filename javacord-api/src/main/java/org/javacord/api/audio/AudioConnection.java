package org.javacord.api.audio;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.listener.audio.AudioConnectionAttachableListenerManager;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface AudioConnection extends AudioConnectionAttachableListenerManager {

    /**
     * Moves the connection to a different channel.
     * The channel must be in the same server as the current connection.
     *
     * @param destChannel The channel to move to.
     * @return A CompletableFuture indicating whether or not the move was successful.
     */
    CompletableFuture<Void> moveTo(ServerVoiceChannel destChannel);

    /**
     * Moves the connection to a different channel.
     * The channel must be in the same server as the current connection.
     *
     * @param destChannel The channel to move to.
     * @param selfMute    Whether or not to be self-muted on join.
     * @param selfDeafen  Whether or not to be self-deafened on join.
     * @return A CompletableFuture indicating whether or not the move was successful.
     */
    CompletableFuture<Void> moveTo(ServerVoiceChannel destChannel, boolean selfMute, boolean selfDeafen);

    /**
     * Disconnects from the voice channel.
     *
     * <p><b>A disconnected audio connection cannot be reused!</b>
     *
     * @return A CompletableFuture which completes when the connection has been disconnected.
     */
    CompletableFuture<Void> close();

    /**
     * Gets the audio source.
     *
     * @return The audio source.
     */
    Optional<AudioSource> getAudioSource();

    /**
     * Sets the audio source, replacing any currently playing source.
     *
     * @param source The audio source which will become the source.
     */
    void setAudioSource(AudioSource source);

    /**
     * Removes the audio source.
     */
    void removeAudioSource();

    /**
     * Gets the voice channel of the audio connection.
     *
     * @return The voice channel of the audio connection.
     */
    ServerVoiceChannel getChannel();

    /**
     * Gets the self-muted status of this connection.
     *
     * <p>This is solely visual. A muted bot can still play audio.
     *
     * @return Whether or not the connection is self-muted.
     */
    boolean isSelfMuted();

    /**
     * Sets the self-muted status of this connection.
     *
     * <p>This is solely visual. A muted bot can still play audio.
     *
     * @param muted Whether or not to self-mute this connection.
     */
    void setSelfMuted(boolean muted);

    /**
     * Gets the self-deafened status of this connection.
     *
     * <p>This is solely visual. A deafened bot can still receive audio.
     *
     * @return Whether or not the connection is self-deafened.
     */
    boolean isSelfDeafened();

    /**
     * Sets the self-deafened status of this connection.
     *
     * <p>This is solely visual. A deafened bot can still receive audio.
     *
     * @param deafened Whether or not to self-deafen this connection.
     */
    void setSelfDeafened(boolean deafened);

    /**
     * Gets the priority speaker status of this connection.
     *
     * @return Whether or not the connection is priority speaking.
     */
    boolean isPrioritySpeaking();

    /**
     * Sets the priority speaker status of this connection.
     *
     * @param prioritySpeaking Whether or not to become a priority speaker.
     */
    void setPrioritySpeaking(boolean prioritySpeaking);

    /**
     * Gets the speaking flags of this connection.
     *
     * @return The current speaking flags of this connection.
     */
    Set<SpeakingFlag> getSpeakingFlags();

    /**
     * Gets the server of the audio connection.
     *
     * @return The server of the audio connection.
     */
    default Server getServer() {
        return getChannel().getServer();
    }

}
