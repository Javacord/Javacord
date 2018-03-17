package org.javacord.api.event.channel.group;

/**
 * A group channel change name event.
 */
public interface GroupChannelChangeNameEvent extends GroupChannelEvent {

    /**
     * Gets the new name of the channel.
     *
     * @return The new name of the channel.
     */
    String getNewName();

    /**
     * Gets the old name of the channel.
     *
     * @return The old name of the channel.
     */
    String getOldName();

}
