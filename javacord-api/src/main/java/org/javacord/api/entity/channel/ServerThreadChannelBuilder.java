package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerThreadChannelBuilderDelegate;
import org.javacord.api.entity.message.Message;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new server thread channels.
 */
public class ServerThreadChannelBuilder extends ServerChannelBuilder<ServerThreadChannelBuilder> {

    /**
     * The server thread channel delegate used by this instance.
     */
    private final ServerThreadChannelBuilderDelegate delegate;

    /**
     * Creates a new server thread channel builder for a ServerTextChannel.
     *
     * @param serverTextChannel The server text channel where the thread will be created in.
     * @param threadType        The ChannelType of the thread.
     * @param name              The name of the thread.
     */
    public ServerThreadChannelBuilder(ServerTextChannel serverTextChannel, ChannelType threadType, String name) {
        super(ServerThreadChannelBuilder.class,
                DelegateFactory.createServerThreadChannelBuilderDelegate(serverTextChannel));
        delegate = (ServerThreadChannelBuilderDelegate) super.delegate;
        delegate.setName(name);
        delegate.setChannelType(threadType);
    }

    /**
     * Creates a new server thread channel builder for a message.
     *
     * @param message The message where this thread should be created for.
     * @param name    The name of the thread.
     */
    public ServerThreadChannelBuilder(Message message, String name) {
        super(ServerThreadChannelBuilder.class, DelegateFactory.createServerThreadChannelBuilderDelegate(message));
        delegate = (ServerThreadChannelBuilderDelegate) super.delegate;
        delegate.setName(name);
    }

    /**
     * Sets the invitable flag of the thread channel. Only available for private threads.
     *
     * @param inviteable Whether non-moderators can add other non-moderators to a thread;
     *                   only available when creating a private thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelBuilder setInvitableFlag(boolean inviteable) {
        delegate.setInvitableFlag(inviteable);
        return this;
    }

    /**
     * Sets the slowmode of the channel.
     *
     * @param delay The delay in seconds.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelBuilder setSlowmodeDelayInSeconds(int delay) {
        delegate.setSlowmodeDelayInSeconds(delay);
        return this;
    }

    /**
     * Sets the auto archive duration of the channel.
     *
     * @param autoArchiveDuration The auto archive duration in seconds.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelBuilder setAutoArchiveDuration(Integer autoArchiveDuration) {
        delegate.setAutoArchiveDuration(autoArchiveDuration);
        return this;
    }

    /**
     * Sets the auto archive duration of the channel.
     *
     * @param autoArchiveDuration The auto archive duration type.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelBuilder setAutoArchiveDuration(AutoArchiveDuration autoArchiveDuration) {
        delegate.setAutoArchiveDuration(autoArchiveDuration.asInt());
        return this;
    }

    /**
     * Creates the server thread channel.
     *
     * @return The created thread channel.
     */
    public CompletableFuture<ServerThreadChannel> create() {
        return delegate.create();
    }

}
