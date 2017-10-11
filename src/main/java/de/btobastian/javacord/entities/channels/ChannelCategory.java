package de.btobastian.javacord.entities.channels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;

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
        ((ImplServer) getServer()).getUnorderedChannels().stream()
                .filter(channel -> channel.asServerTextChannel().isPresent())
                .map(channel -> channel.asServerTextChannel().get())
                .filter(channel -> channel.getCategory().orElse(null) == this)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .forEach(channels::add);
        ((ImplServer) getServer()).getUnorderedChannels().stream()
                .filter(channel -> channel.asServerVoiceChannel().isPresent())
                .map(channel -> channel.asServerVoiceChannel().get())
                .filter(channel -> channel.getCategory().orElse(null) == this)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .forEach(channels::add);
        return channels;
    }

    /**
     * Checks is the category is "not safe for work".
     *
     * @return Whether the category is "not safe for work" or not.
     */
    boolean isNsfw();
	/**
	 * Checks if user can see ALL channels
	 * 
	 * @param user
	 *            - The users
	 * @return If they can or not
	 */
	default boolean canSeeAll(User user) {
		return getChannels().stream().allMatch(channel -> {
			return channel.canSee(user);
		});
	}

	/**
	 * Returns a list of channels the user may see
	 * 
	 * @param user
	 *            - The user object
	 * @return The list of channels
	 */
	default List<ServerChannel> getSeenChannels(User user) {
		return getChannels().stream().filter(channel -> {
			return channel.canSee(user);
		}).collect(Collectors.toList());
	}

}

