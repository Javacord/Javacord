package org.javacord.core.event.channel.thread;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.channel.thread.ThreadCreateEvent;

public class ThreadCreateEventImpl implements ThreadCreateEvent {

    /**
     * The channel of the event.
     */
    private final TextChannel channel;

    /**
     * Creates a new thread create event.
     *
     * @param channel The channel of the event.
     */
    public ThreadCreateEventImpl(TextChannel channel) {
        this.channel = channel;
    }

    @Override
    public TextChannel getChannel() {return channel;}

    @Override
    public DiscordApi getApi() {
        return getChannel().getApi();
    }
}
