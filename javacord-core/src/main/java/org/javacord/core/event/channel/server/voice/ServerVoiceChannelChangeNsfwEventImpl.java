package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeNsfwEvent;

/**
 * The implementation of {@link ServerVoiceChannelChangeNsfwEvent}.
 */
public class ServerVoiceChannelChangeNsfwEventImpl extends ServerVoiceChannelEventImpl
        implements ServerVoiceChannelChangeNsfwEvent {

    /**
     * The new nsfw flag of the channel.
     */
    private final boolean newNsfw;

    /**
     * The old nsfw flag of the channel.
     */
    private final boolean oldNsfw;

    /**
     * Creates a new server voice channel change nsfw event.
     *
     * @param channel The server voice channel of the event.
     * @param newNsfw The new nsfw flag of the channel.
     * @param oldNsfw The old nsfw flag of the channel.
     */
    public ServerVoiceChannelChangeNsfwEventImpl(ServerVoiceChannel channel, boolean newNsfw,
                                                 boolean oldNsfw) {
        super(channel);
        this.newNsfw = newNsfw;
        this.oldNsfw = oldNsfw;
    }

    @Override
    public boolean isNsfw() {
        return newNsfw;
    }

    @Override
    public boolean wasNsfw() {
        return oldNsfw;
    }
}
