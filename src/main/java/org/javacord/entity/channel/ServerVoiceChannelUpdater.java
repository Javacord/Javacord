package org.javacord.entity.channel;

import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.entity.user.User;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server voice channels.
 */
public interface ServerVoiceChannelUpdater extends ServerChannelUpdater {

    /**
     * Queues the bitrate to be updated.
     *
     * @param bitrate The new bitrate of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelUpdater setBitrate(int bitrate);

    /**
     * Queues the user limit to be updated.
     *
     * @param userLimit The new user limit of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelUpdater setUserLimit(int userLimit);

    /**
     * Queues the user limit to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelUpdater removeUserLimit();

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelUpdater setCategory(ChannelCategory category);

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    ServerVoiceChannelUpdater removeCategory();

    @Override
    ServerVoiceChannelUpdater setAuditLogReason(String reason);

    @Override
    ServerVoiceChannelUpdater setName(String name);

    @Override
    ServerVoiceChannelUpdater setRawPosition(int rawPosition);

    @Override
    ServerVoiceChannelUpdater addPermissionOverwrite(User user, Permissions permissions);

    @Override
    ServerVoiceChannelUpdater addPermissionOverwrite(Role role, Permissions permissions);

    @Override
    ServerVoiceChannelUpdater removePermissionOverwrite(User user);

    @Override
    ServerVoiceChannelUpdater removePermissionOverwrite(Role role);

    @Override
    CompletableFuture<Void> update();

}
