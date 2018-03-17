package org.javacord.api.entity.channel;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerVoiceChannelBuilder} to create server voice channels.
 * You usually don't want to interact with this object.
 */
public interface ServerVoiceChannelBuilderDelegate {

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     */
    void setName(String name);

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     */
    void setCategory(ChannelCategory category);

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
