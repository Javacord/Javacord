package org.javacord.entity.channel;

import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.entity.user.User;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server text channels.
 */
public interface ServerTextChannelUpdater extends ServerChannelUpdater {

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerTextChannelUpdater setTopic(String topic);

    /**
     * Queues the nsfw to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerTextChannelUpdater setNsfwFlag(boolean nsfw);

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    ServerTextChannelUpdater setCategory(ChannelCategory category);

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    ServerTextChannelUpdater removeCategory();

    @Override
    ServerTextChannelUpdater setAuditLogReason(String reason);

    @Override
    ServerTextChannelUpdater setName(String name);

    @Override
    ServerTextChannelUpdater setRawPosition(int rawPosition);

    @Override
    ServerTextChannelUpdater addPermissionOverwrite(User user, Permissions permissions);

    @Override
    ServerTextChannelUpdater addPermissionOverwrite(Role role, Permissions permissions);

    @Override
    ServerTextChannelUpdater removePermissionOverwrite(User user);

    @Override
    ServerTextChannelUpdater removePermissionOverwrite(Role role);

    @Override
    CompletableFuture<Void> update();

}
