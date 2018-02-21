package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.events.server.ServerEvent;

/**
 * A server voice channel event.
 */
public abstract class ServerVoiceChannelEvent extends ServerEvent {

    /**
     * The channel of the event.
     */
    private final ServerVoiceChannel channel;

    /**
     * Creates a new voice channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerVoiceChannelEvent(ServerVoiceChannel channel) {
        super(channel.getApi(), channel.getServer());
        this.channel = channel;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public ServerVoiceChannel getChannel() {
        return channel;
    }

}
