package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.Mentionable;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionType;

import java.util.Collection;
import java.util.Optional;

/**
 * This class represents a server text channel.
 */
public interface ServerTextChannel extends ServerChannel, TextChannel, Mentionable {

	@Override
	default ChannelType getType() {
		return ChannelType.SERVER_TEXT_CHANNEL;
	}

	/**
	 * Checks is the channel is "not safe for work".
	 *
	 * @return Whether the channel is "not safe for work" or not.
	 */
	boolean isNsfw();

	/**
	 * Gets the category of the channel.
	 *
	 * @return The category of the channel.
	 */
	Optional<ChannelCategory> getCategory();

	/**
	 * Checks if the given user can send messages in this channel.
	 *
	 * @param user
	 *            The user to check.
	 * @return Whether the given user can write messages or not.
	 */
	default boolean canWrite(User user) {
		Collection<PermissionType> allowed = getEffectiveAllowedPermissions(user);
		return allowed.contains(PermissionType.ADMINISTRATOR)
				|| allowed.contains(PermissionType.READ_MESSAGES) && allowed.contains(PermissionType.SEND_MESSAGES);
	}

	/**
	 * Checks if user can read the message history.
	 * 
	 * @param user
	 *            - The user object
	 * @return True if user can, False otherwise
	 */
	default boolean canReadHistory(User user) {
		return user.hasPermission(PermissionType.ADMINISTRATOR, this)
				|| (user.hasPermission(PermissionType.READ_MESSAGES, this)
						&& user.hasPermission(PermissionType.READ_MESSAGE_HISTORY, this));
	}
}
