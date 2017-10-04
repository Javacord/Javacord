package de.btobastian.javacord.entities.channels;

import java.util.ArrayList;
import java.util.List;

import de.btobastian.javacord.entities.User;

/**
 * This class represents a channel category.
 */
public interface ChannelCategory extends ServerChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.CHANNEL_CATEGORY;
    }

    /**
     * Gets a sorted (by position) list of all channels in the category.
     *
     * @return The channels in the category.
     */
    default List<ServerChannel> getChannels() {
        List<ServerChannel> channels = new ArrayList<>();
        getServer().getTextChannels().stream()
                .filter(channel -> channel.getCategory().orElse(null) == this)
                .forEach(channels::add);
        getServer().getVoiceChannels().stream()
                .filter(channel -> channel.getCategory().orElse(null) == this)
                .forEach(channels::add);
        return channels;
    }
	/**
	 * Checks if user can access ALL child channles,rather than just one.
	 * @param user - The user object
	 * @return True if user can access all child channles, False otherwise.
	 * @see {@link ChannelCategory#canBeAccessedBy(User)} if you want to just check for visible status.
	 */
	public boolean canBeFullyAccessedBy(User user);
    /**
     * Checks is the category is "not safe for work".
     *
     * @return Whether the category is "not safe for work" or not.
     */
    boolean isNsfw();

}
