package org.javacord.api.event.channel.server.voice;

public interface ServerVoiceChannelChangeNsfwEvent extends ServerVoiceChannelEvent {
    /**
     * Gets the new nsfw flag of the channel.
     *
     * @return The new nsfw flag of the channel.
     */
    boolean isNsfw();

    /**
     * Gets the old nsfw flag of the channel.
     *
     * @return The old nsfw flag of the channel.
     */
    boolean wasNsfw();
}
