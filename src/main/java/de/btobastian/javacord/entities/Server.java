package de.btobastian.javacord.entities;

import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.ChannelCategoryBuilder;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannelBuilder;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannelBuilder;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.PermissionsBuilder;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerChangeNameListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.server.ServerMemberAddListener;
import de.btobastian.javacord.listeners.server.ServerMemberRemoveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangePositionListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelCreateListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.listeners.server.role.RoleChangePermissionsListener;
import de.btobastian.javacord.listeners.server.role.RoleChangePositionListener;
import de.btobastian.javacord.listeners.server.role.RoleCreateListener;
import de.btobastian.javacord.listeners.user.UserChangeGameListener;
import de.btobastian.javacord.listeners.user.UserChangeStatusListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class represents a Discord server, sometimes also called guild.
 */
public interface Server extends DiscordEntity, IconHolder {

	/**
	 * Gets the name of the server.
	 *
	 * @return The name of the server.
	 */
	String getName();

	/**
	 * Gets the region of the server.
	 *
	 * @return The region of the server.
	 */
	Region getRegion();

	/**
	 * Gets the nickname of a user.
	 *
	 * @param user
	 *            The user to check.
	 * @return The nickname of the user.
	 */
	Optional<String> getNickname(User user);

	/**
	 * Gets a collection with all members of the server.
	 *
	 * @return A collection with all members of the server.
	 */
	Collection<User> getMembers();

	/**
	 * Checks if the server if considered large.
	 *
	 * @return Whether the server is large or not.
	 */
	boolean isLarge();

	/**
	 * Gets the amount of members in this server.
	 *
	 * @return The amount of members in this server.
	 */
	int getMemberCount();

	/**
	 * Gets the owner of the server.
	 *
	 * @return The owner of the server.
	 */
	User getOwner();

	/**
	 * Gets a sorted list (by position) with all roles of the server.
	 *
	 * @return A sorted list (by position) with all roles of the server.
	 */
	List<Role> getRoles();

	/**
	 * Gets a role by it's id.
	 *
	 * @param id
	 *            The id of the role.
	 * @return The role with the given id.
	 */
	Optional<Role> getRoleById(long id);

	/**
	 * Gets a role by it's id.
	 *
	 * @param id
	 *            The id of the role.
	 * @return The role with the given id.
	 */
	default Optional<Role> getRoleById(String id) {
		try {
			return getRoleById(Long.parseLong(id));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Gets a sorted list (by position) with all roles with the given name. This
	 * method is case sensitive!
	 *
	 * @param name
	 *            The name of the roles.
	 * @return A sorted list (by position) with all roles with the given name.
	 */
	default List<Role> getRolesByName(String name) {
		return getRoles().stream().filter(role -> role.getName().equals(name)).collect(Collectors.toList());
	}

	/**
	 * Gets a sorted list (by position) with all roles with the given name. This
	 * method is case insensitive!
	 *
	 * @param name
	 *            The name of the roles.
	 * @return A sorted list (by position) with all roles with the given name.
	 */
	default List<Role> getRolesByNameIgnoreCase(String name) {
		return getRoles().stream().filter(role -> role.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
	}

	/**
	 * Gets a sorted list (by position) with all roles of the user in the server.
	 *
	 * @param user
	 *            The user.
	 * @return A sorted list (by position) with all roles of the user in the server.
	 */
	default List<Role> getRolesOf(User user) {
		return getRoles().stream().filter(role -> role.getUsers().contains(user)).collect(Collectors.toList());
	}

	/**
	 * Gets the permissions of a user.
	 *
	 * @param user
	 *            The user.
	 * @return The permissions of the user.
	 */
	default Permissions getPermissionsOf(User user) {
		PermissionsBuilder builder = new PermissionsBuilder();
		getAllowedPermissionsOf(user).forEach(type -> builder.setState(type, PermissionState.ALLOWED));
		return builder.build();
	}

	/**
	 * Get the allowed permissions of a given user. Remember, that some permissions
	 * affect others! E.g. a user who has {@link PermissionType#SEND_MESSAGES} but
	 * not {@link PermissionType#READ_MESSAGES} cannot send messages, even though he
	 * has the {@link PermissionType#SEND_MESSAGES} permission.
	 *
	 * @param user
	 *            The user.
	 * @return The allowed permissions of the given user.
	 */
	default Collection<PermissionType> getAllowedPermissionsOf(User user) {
		Collection<PermissionType> allowed = new HashSet<>();
		if (getOwner() == user) {
			allowed.addAll(Arrays.asList(PermissionType.values()));
		} else {
			getRolesOf(user).forEach(role -> allowed.addAll(role.getAllowedPermissions()));
		}
		return allowed;
	}

	/**
	 * Get the unset permissions of a given user.
	 *
	 * @param user
	 *            The user.
	 * @return The unset permissions of the given user.
	 */
	default Collection<PermissionType> getUnsetPermissionsOf(User user) {
		Collection<PermissionType> unset = new HashSet<>();
		if (getOwner() == user) {
			return unset;
		}
		getRolesOf(user).forEach(role -> unset.addAll(role.getUnsetPermissions()));
		return unset;
	}

	/**
	 * Checks if a user has a given permission. Remember, that some permissions
	 * affect others! E.g. a user who has {@link PermissionType#SEND_MESSAGES} but
	 * not {@link PermissionType#READ_MESSAGES} cannot send messages, even though he
	 * has the {@link PermissionType#SEND_MESSAGES} permission. This method also do
	 * not take into account overwritten permissions in some channels!
	 *
	 * @param user
	 *            The user.
	 * @param permission
	 *            The permission to check.
	 * @return Whether the user has the permission or not.
	 */
	default boolean hasPermission(User user, PermissionType permission) {
		return getAllowedPermissionsOf(user).contains(permission);
	}

	/**
	 * Gets a collection with all custom emojis of this server.
	 *
	 * @return A collection with all custom emojis of this server.
	 */
	Collection<CustomEmoji> getCustomEmojis();

	/**
	 * Gets a custom emoji in this server by it's id.
	 *
	 * @param id
	 *            The id of the emoji.
	 * @return The emoji with the given id.
	 */
	default Optional<CustomEmoji> getCustomEmojiById(long id) {
		return getCustomEmojis().stream().filter(emoji -> emoji.getId() == id).findAny();
	}

	/**
	 * Gets a custom emoji in this server by it's id.
	 *
	 * @param id
	 *            The id of the emoji.
	 * @return The emoji with the given id.
	 */
	default Optional<CustomEmoji> getCustomEmojiById(String id) {
		try {
			return getCustomEmojiById(Long.parseLong(id));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Gets a collection of all custom emojis with the given name in the server.
	 * This method is case sensitive!
	 *
	 * @param name
	 *            The name of the custom emojis.
	 * @return A collection of all custom emojis with the given name in this server.
	 */
	default Collection<CustomEmoji> getCustomEmojisByName(String name) {
		return getCustomEmojis().stream().filter(emoji -> emoji.getName().equals(name)).collect(Collectors.toList());
	}

	/**
	 * Gets a collection of all custom emojis with the given name in the server.
	 * This method is case insensitive!
	 *
	 * @param name
	 *            The name of the custom emojis.
	 * @return A collection of all custom emojis with the given name in this server.
	 */
	default Collection<CustomEmoji> getCustomEmojisByNameIgnoreCase(String name) {
		return getCustomEmojis().stream().filter(emoji -> emoji.getName().equalsIgnoreCase(name))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a new channel category builder.
	 *
	 * @return The builder to create a new channel category.
	 */
	default ChannelCategoryBuilder getChannelCategoryBuilder() {
		return new ChannelCategoryBuilder(this);
	}

	/**
	 * Gets a new server text channel builder.
	 *
	 * @return The builder to create a new server text channel.
	 */
	default ServerTextChannelBuilder getTextChannelBuilder() {
		return new ServerTextChannelBuilder(this);
	}

	/**
	 * Gets a new server voice channel builder.
	 *
	 * @return The builder to create a new server voice channel.
	 */
	default ServerVoiceChannelBuilder getVoiceChannelBuilder() {
		return new ServerVoiceChannelBuilder(this);
	}

	/**
	 * Gets a collection with all channels of the server.
	 *
	 * @return A collection with all channels of the server.
	 */
	Collection<ServerChannel> getChannels();

	/**
	 * Gets a sorted list (by position) with all channel categories of the server.
	 *
	 * @return A sorted list (by position) with all channel categories of the
	 *         server.
	 */
	default List<ChannelCategory> getChannelCategories() {
		return getChannels().stream().filter(channel -> channel instanceof ChannelCategory)
				.sorted(Comparator.comparingInt(ServerChannel::getPosition)).map(channel -> (ChannelCategory) channel)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Gets a sorted list (by position) with all text channels of the server.
	 *
	 * @return A sorted list (by position) with all text channels of the server.
	 */
	default List<ServerTextChannel> getTextChannels() {
		return getChannels().stream().filter(channel -> channel instanceof ServerTextChannel)
				.sorted(Comparator.comparingInt(ServerChannel::getPosition)).map(channel -> (ServerTextChannel) channel)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Gets a sorted list (by position) with all voice channels of the server.
	 *
	 * @return A sorted list (by position) with all voice channels of the server.
	 */
	default List<ServerVoiceChannel> getVoiceChannels() {
		return getChannels().stream().filter(channel -> channel instanceof ServerVoiceChannel)
				.sorted(Comparator.comparingInt(ServerChannel::getPosition))
				.map(channel -> (ServerVoiceChannel) channel).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Gets a channel by it's id.
	 *
	 * @param id
	 *            The id of the channel.
	 * @return The channel with the given id.
	 */
	Optional<ServerChannel> getChannelById(long id);

	/**
	 * Gets a channel by it's id.
	 *
	 * @param id
	 *            The id of the channel.
	 * @return The channel with the given id.
	 */
	default Optional<ServerChannel> getChannelById(String id) {
		try {
			return getChannelById(Long.valueOf(id));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Gets a collection with all channels with the given name. This method is case
	 * sensitive!
	 *
	 * @param name
	 *            The name of the channels.
	 * @return A collection with all channels with the given name.
	 */
	default Collection<ServerChannel> getChannelsByName(String name) {
		return getChannels().stream().filter(channel -> channel.getName().equals(name)).collect(Collectors.toList());
	}

	/**
	 * Gets a collection with all channels with the given name. This method is case
	 * insensitive!
	 *
	 * @param name
	 *            The name of the channels.
	 * @return A collection with all channels with the given name.
	 */
	default Collection<ServerChannel> getChannelsByNameIgnoreCase(String name) {
		return getChannels().stream().filter(channel -> channel.getName().equalsIgnoreCase(name))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a channel category by it's id.
	 *
	 * @param id
	 *            The id of the channel category.
	 * @return The channel category with the given id.
	 */
	default Optional<ChannelCategory> getChannelCategoryById(long id) {
		return getChannelById(id).filter(channel -> channel instanceof ChannelCategory)
				.map(channel -> (ChannelCategory) channel);
	}

	/**
	 * Gets a channel category by it's id.
	 *
	 * @param id
	 *            The id of the channel category.
	 * @return The channel category with the given id.
	 */
	default Optional<ChannelCategory> getChannelCategoryById(String id) {
		try {
			return getChannelCategoryById(Long.valueOf(id));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Gets a sorted list (by position) with all channel categories with the given
	 * name. This method is case sensitive!
	 *
	 * @param name
	 *            The name of the channel categories.
	 * @return A sorted list (by position) with all channel categories with the
	 *         given name.
	 */
	default List<ChannelCategory> getChannelCategoriesByName(String name) {
		return getChannelCategories().stream().filter(channel -> channel.getName().equals(name))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a sorted list (by position) with all channel categories with the given
	 * name. This method is case insensitive!
	 *
	 * @param name
	 *            The name of the channel categories.
	 * @return A sorted list (by position) with all channel categories with the
	 *         given name.
	 */
	default List<ChannelCategory> getChannelCategoriesByNameIgnoreCase(String name) {
		return getChannelCategories().stream().filter(channel -> channel.getName().equalsIgnoreCase(name))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a text channel by it's id.
	 *
	 * @param id
	 *            The id of the text channel.
	 * @return The text channel with the given id.
	 */
	default Optional<ServerTextChannel> getTextChannelById(long id) {
		return getChannelById(id).filter(channel -> channel instanceof ServerTextChannel)
				.map(channel -> (ServerTextChannel) channel);
	}

	/**
	 * Gets a text channel by it's id.
	 *
	 * @param id
	 *            The id of the text channel.
	 * @return The text channel with the given id.
	 */
	default Optional<ServerTextChannel> getTextChannelById(String id) {
		try {
			return getTextChannelById(Long.valueOf(id));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Gets a sorted list (by position) with all text channels with the given name.
	 * This method is case sensitive!
	 *
	 * @param name
	 *            The name of the text channels.
	 * @return A sorted list (by position) with all text channels with the given
	 *         name.
	 */
	default List<ServerTextChannel> getTextChannelsByName(String name) {
		return getTextChannels().stream().filter(channel -> channel.getName().equals(name))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a sorted list (by position) with all text channels with the given name.
	 * This method is case insensitive!
	 *
	 * @param name
	 *            The name of the text channels.
	 * @return A sorted list (by position) with all text channels with the given
	 *         name.
	 */
	default List<ServerTextChannel> getTextChannelsByNameIgnoreCase(String name) {
		return getTextChannels().stream().filter(channel -> channel.getName().equalsIgnoreCase(name))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a voice channel by it's id.
	 *
	 * @param id
	 *            The id of the voice channel.
	 * @return The voice channel with the given id.
	 */
	default Optional<ServerVoiceChannel> getVoiceChannelById(long id) {
		return getChannelById(id).filter(channel -> channel instanceof ServerVoiceChannel)
				.map(channel -> (ServerVoiceChannel) channel);
	}

	/**
	 * Gets a voice channel by it's id.
	 *
	 * @param id
	 *            The id of the voice channel.
	 * @return The voice channel with the given id.
	 */
	default Optional<ServerVoiceChannel> getVoiceChannelById(String id) {
		try {
			return getVoiceChannelById(Long.valueOf(id));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Gets a sorted list (by position) with all voice channels with the given name.
	 * This method is case sensitive!
	 *
	 * @param name
	 *            The name of the voice channels.
	 * @return A sorted list (by position) with all voice channels with the given
	 *         name.
	 */
	default List<ServerVoiceChannel> getVoiceChannelsByName(String name) {
		return getVoiceChannels().stream().filter(channel -> channel.getName().equals(name))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a sorted list (by position) with all voice channels with the given name.
	 * This method is case insensitive!
	 *
	 * @param name
	 *            The name of the voice channels.
	 * @return A sorted list (by position) with all voice channels with the given
	 *         name.
	 */
	default List<ServerVoiceChannel> getVoiceChannelsByNameIgnoreCase(String name) {
		return getVoiceChannels().stream().filter(channel -> channel.getName().equalsIgnoreCase(name))
				.collect(Collectors.toList());
	}

	/**
	 * Adds a listener, which listens to message creates in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addMessageCreateListener(MessageCreateListener listener);

	/**
	 * Gets a list with all registered message create listeners.
	 *
	 * @return A list with all registered message create listeners.
	 */
	List<MessageCreateListener> getMessageCreateListeners();

	/**
	 * Adds a listener, which listens to you leaving this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerLeaveListener(ServerLeaveListener listener);

	/**
	 * Gets a list with all registered server leaves listeners.
	 *
	 * @return A list with all registered server leaves listeners.
	 */
	List<ServerLeaveListener> getServerLeaveListeners();

	/**
	 * Adds a listener, which listens to this server becoming unavailable.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerBecomesUnavailableListener(ServerBecomesUnavailableListener listener);

	/**
	 * Gets a list with all registered server becomes unavailable listeners.
	 *
	 * @return A list with all registered server becomes unavailable listeners.
	 */
	List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners();

	/**
	 * Adds a listener, which listens to users starting to type in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addUserStartTypingListener(UserStartTypingListener listener);

	/**
	 * Gets a list with all registered user starts typing listeners.
	 *
	 * @return A list with all registered user starts typing listeners.
	 */
	List<UserStartTypingListener> getUserStartTypingListeners();

	/**
	 * Adds a listener, which listens to server channel creations in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerChannelCreateListener(ServerChannelCreateListener listener);

	/**
	 * Gets a list with all registered server channel create listeners.
	 *
	 * @return A list with all registered server channel create listeners.
	 */
	List<ServerChannelCreateListener> getServerChannelCreateListeners();

	/**
	 * Adds a listener, which listens to server channel deletions in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerChannelDeleteListener(ServerChannelDeleteListener listener);

	/**
	 * Gets a list with all registered server channel delete listeners.
	 *
	 * @return A list with all registered server channel delete listeners.
	 */
	List<ServerChannelDeleteListener> getServerChannelDeleteListeners();

	/**
	 * Adds a listener, which listens to message deletions in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addMessageDeleteListener(MessageDeleteListener listener);

	/**
	 * Gets a list with all registered message delete listeners.
	 *
	 * @return A list with all registered message delete listeners.
	 */
	List<MessageDeleteListener> getMessageDeleteListeners();

	/**
	 * Adds a listener, which listens to message edits in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addMessageEditListener(MessageEditListener listener);

	/**
	 * Gets a list with all registered message edit listeners.
	 *
	 * @return A list with all registered message edit listeners.
	 */
	List<MessageEditListener> getMessageEditListeners();

	/**
	 * Adds a listener, which listens to reactions being added on this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addReactionAddListener(ReactionAddListener listener);

	/**
	 * Gets a list with all registered reaction add listeners.
	 *
	 * @return A list with all registered reaction add listeners.
	 */
	List<ReactionAddListener> getReactionAddListeners();

	/**
	 * Adds a listener, which listens to reactions being removed on this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addReactionRemoveListener(ReactionRemoveListener listener);

	/**
	 * Gets a list with all registered reaction remove listeners.
	 *
	 * @return A list with all registered reaction remove listeners.
	 */
	List<ReactionRemoveListener> getReactionRemoveListeners();

	/**
	 * Adds a listener, which listens to users joining this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerMemberAddListener(ServerMemberAddListener listener);

	/**
	 * Gets a list with all registered server member add listeners.
	 *
	 * @return A list with all registered server member add listeners.
	 */
	List<ServerMemberAddListener> getServerMemberAddListeners();

	/**
	 * Adds a listener, which listens to users leaving this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerMemberRemoveListener(ServerMemberRemoveListener listener);

	/**
	 * Gets a list with all registered server member remove listeners.
	 *
	 * @return A list with all registered server member remove listeners.
	 */
	List<ServerMemberRemoveListener> getServerMemberRemoveListeners();

	/**
	 * Adds a listener, which listens to server name changes.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerChangeNameListener(ServerChangeNameListener listener);

	/**
	 * Gets a list with all registered server change name listeners.
	 *
	 * @return A list with all registered server change name listeners.
	 */
	List<ServerChangeNameListener> getServerChangeNameListeners();

	/**
	 * Adds a listener, which listens to server channel name changes in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerChannelChangeNameListener(ServerChannelChangeNameListener listener);

	/**
	 * Gets a list with all registered server channel change name listeners.
	 *
	 * @return A list with all registered server channel change name listeners.
	 */
	List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners();

	/**
	 * Adds a listener, which listens to server channel position changes in this
	 * server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerChannelChangePositionListener(ServerChannelChangePositionListener listener);

	/**
	 * Gets a list with all registered server channel change position listeners.
	 *
	 * @return A list with all registered server channel change position listeners.
	 */
	List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners();

	/**
	 * Adds a listener, which listens to custom emoji creations in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addCustomEmojiCreateListener(CustomEmojiCreateListener listener);

	/**
	 * Gets a list with all registered custom emoji create listeners.
	 *
	 * @return A list with all registered custom emoji create listeners.
	 */
	List<CustomEmojiCreateListener> getCustomEmojiCreateListeners();

	/**
	 * Adds a listener, which listens to game changes of users in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addUserChangeGameListener(UserChangeGameListener listener);

	/**
	 * Gets a list with all registered user change game listeners.
	 *
	 * @return A list with all registered custom emoji create listeners.
	 */
	List<UserChangeGameListener> getUserChangeGameListeners();

	/**
	 * Adds a listener, which listens to status changes of users in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addUserChangeStatusListener(UserChangeStatusListener listener);

	/**
	 * Gets a list with all registered user change status listeners.
	 *
	 * @return A list with all registered custom emoji create listeners.
	 */
	List<UserChangeStatusListener> getUserChangeStatusListeners();

	/**
	 * Adds a listener, which listens to role permission changes in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addRoleChangePermissionsListener(RoleChangePermissionsListener listener);

	/**
	 * Gets a list with all registered role change permissions listeners.
	 *
	 * @return A list with all registered role change permissions listeners.
	 */
	List<RoleChangePermissionsListener> getRoleChangePermissionsListeners();

	/**
	 * Adds a listener, which listens to role position changes in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addRoleChangePositionListener(RoleChangePositionListener listener);

	/**
	 * Gets a list with all registered role change position listeners.
	 *
	 * @return A list with all registered role change position listeners.
	 */
	List<RoleChangePositionListener> getRoleChangePositionListeners();

	/**
	 * Adds a listener, which listens to overwritten permission changes in this
	 * server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addServerChannelChangeOverwrittenPermissionsListener(
			ServerChannelChangeOverwrittenPermissionsListener listener);

	/**
	 * Gets a list with all registered server channel change overwritten permissions
	 * listeners.
	 *
	 * @return A list with all registered server channel change overwritten
	 *         permissions listeners.
	 */
	List<ServerChannelChangeOverwrittenPermissionsListener> getServerChannelChangeOverwrittenPermissionsListeners();

	/**
	 * Adds a listener, which listens to role creations in this server.
	 *
	 * @param listener
	 *            The listener to add.
	 */
	void addRoleCreateListener(RoleCreateListener listener);

	/**
	 * Gets a list with all registered role create listeners.
	 *
	 * @return A list with all registered role create listeners.
	 */
	List<RoleCreateListener> getRoleCreateListeners();

	/**
	 * Returns whether a user can create a channel
	 * 
	 * @param user
	 *            - The user to test
	 * @return True if they can, false otherwise
	 */
	default boolean canCreateChannels(User user) {
		return user.hasPermission(PermissionType.ADMINISTRATOR, this)
				|| user.hasPermission(PermissionType.MANAGE_CHANNELS, this);
	}

	/**
	 * Determines if a user can view the audit log
	 * 
	 * @param user
	 *            - The user object
	 * @return True if the user can, False otherwise
	 */
	default boolean canViewAuditLogs(User user) {
		return user.hasPermission(PermissionType.ADMINISTRATOR, this)
				|| user.hasPermission(PermissionType.VIEW_AUDIT_LOG, this);
	}

	/**
	 * Determines if a user can ban members in a general context
	 * 
	 * @param admin
	 *            - The administrator
	 * @return True if the user can, False otherwise
	 * 
	 * @see {@link User#isHigherThan(Server, User)}
	 */
	default boolean canBanUsers(User admin) {
		return admin.hasPermission(PermissionType.ADMINISTRATOR, this)
				|| admin.hasPermission(PermissionType.BAN_MEMBERS, this);
	}

	/**
	 * Returns if a user may ban specific user
	 * 
	 * @param admin
	 *            - The Administrator User
	 * @param person
	 *            - The person to test upon
	 * @return True if they can, False otherwise
	 */
	default boolean canBanUser(User admin, User person) {
		return canBanUsers(admin) && admin.isHigherThan(this, person);
	}

	/**
	 * Determines if a user can kick users in a general context.
	 * 
	 * @param admin
	 *            - The administrator user
	 * @return True if user can, False otherwise
	 */
	default boolean canKickUsers(User admin) {
		return admin.hasPermission(PermissionType.ADMINISTRATOR, this)
				|| admin.hasPermission(PermissionType.KICK_MEMBERS, this);
	}

	/**
	 * Determines if certain user can ban another
	 * 
	 * @param admin
	 *            - The administrator
	 * @param subject
	 *            - The subject
	 * @return True if admin can, False otherwise
	 */
	default boolean canKickUser(User admin, User subject) {
		return canBanUsers(admin) && admin.isHigherThan(this, subject);
	}
	
}
