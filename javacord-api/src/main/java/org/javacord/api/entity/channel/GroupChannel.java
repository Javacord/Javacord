package org.javacord.api.entity.channel;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.channel.group.GroupChannelAttachableListenerManager;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a group channel. Group channels are not supported by bot accounts!
 */
public interface GroupChannel extends TextChannel, VoiceChannel, GroupChannelAttachableListenerManager {

    @Override
    default ChannelType getType() {
        return ChannelType.GROUP_CHANNEL;
    }

    /**
     * Gets the members of the group channel.
     *
     * @return The members of the group channel.
     */
    Collection<User> getMembers();

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    Optional<String> getName();

    /**
     * Gets the icon of the group channel.
     *
     * @return The icon of the group channel.
     */
    Optional<Icon> getIcon();

    /**
     * Checks if the user is a member of this group channel.
     *
     * @param user The user to check.
     * @return Whether the user is a member of this group channel or not.
     */
    default boolean isMember(User user) {
        return user.isYourself() || getMembers().contains(user);
    }

    /**
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default GroupChannelUpdater createUpdater() {
        return new GroupChannelUpdater(this);
    }

    /**
     * Updates the name of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link GroupChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param name The new name of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return createUpdater().setName(name).update();
    }

    @Override
    default Optional<GroupChannel> getCurrentCachedInstance() {
        return getApi().getGroupChannelById(getId());
    }

    @Override
    default CompletableFuture<GroupChannel> getLatestInstance() {
        Optional<GroupChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<GroupChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}
