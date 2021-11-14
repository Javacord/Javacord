package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ServerThreadChannelBuilder;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerThreadChannelBuilder} to create server thread channels.
 * You usually don't want to interact with this object.
 */
public interface ServerThreadChannelBuilderDelegate extends ServerChannelBuilderDelegate {

    /**
     * Sets the invitable flag of the thread channel.
     *
     * @param inviteable Whether non-moderators can add other non-moderators to a thread;
     *                   only available when creating a private thread.
     */
    void setInvitableFlag(Boolean inviteable);

    /**
     * Sets the thread channel type of the thread channel.
     *
     * @param channelType The thread chanel type of the
     */
    void setChannelType(ChannelType channelType);

    /**
     * Sets the auto archive duration of the thread channel.
     *
     * @param autoArchiveDuration The auto archive duration in seconds.
     */
    void setAutoArchiveDuration(Integer autoArchiveDuration);

    /**
     * Sets the slowmode delay of the thread channel.
     *
     * @param delay The delay in seconds.
     */
    void setSlowmodeDelayInSeconds(int delay);

    /**
     * Creates the server thread channel.
     *
     * @return The created thread channel.
     */
    CompletableFuture<ServerThreadChannel> create();
}
