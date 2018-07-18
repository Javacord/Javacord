package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannelBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerVoiceChannelBuilder} to create server voice channels.
 * You usually don't want to interact with this object.
 */
public interface ServerVoiceChannelBuilderDelegate extends ServerChannelBuilderDelegate {

    /**
     * Sets the bitrate of the channel.
     *
     * @param bitrate The bitrate of the channel.
     */
    void setBitrate(int bitrate);

    /**
     * Sets the user limit of the channel.
     *
     * @param userlimit The user limit of the channel.
     */
    void setUserlimit(int userlimit);

    /**
     * Creates the server voice channel.
     *
     * @return The created voice channel.
     */
    CompletableFuture<ServerVoiceChannel> create();

}
