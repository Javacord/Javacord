package org.javacord.entity.channel;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new server voice channels.
 */
public interface ServerVoiceChannelBuilder {

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelBuilder setAuditLogReason(String reason);

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelBuilder setName(String name);

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelBuilder setCategory(ChannelCategory category);

    /**
     * Sets the bitrate of the channel.
     *
     * @param bitrate The bitrate of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelBuilder setBitrate(int bitrate);

    /**
     * Sets the user limit of the channel.
     *
     * @param userlimit The user limit of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelBuilder setUserlimit(int userlimit);

    /**
     * Creates the server voice channel.
     *
     * @return The created voice channel.
     */
    CompletableFuture<ServerVoiceChannel> create();

}
