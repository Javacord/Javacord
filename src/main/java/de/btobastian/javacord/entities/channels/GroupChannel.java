package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.User;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a group channel. Group channels are not supported by bot accounts!
 */
public interface GroupChannel extends TextChannel, VoiceChannel {

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
     * Gets the updater for this channel.
     *
     * @return The updater for this channel.
     */
    default GroupChannelUpdater getUpdater() {
        return new GroupChannelUpdater(this);
    }

    /**
     * Updates the name of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link GroupChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param name The new name of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return getUpdater().setName(name).update();
    }

}
