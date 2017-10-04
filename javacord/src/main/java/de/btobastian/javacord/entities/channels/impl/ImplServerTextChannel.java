package de.btobastian.javacord.entities.channels.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangePositionListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import de.btobastian.javacord.utils.cache.MessageCache;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link ServerTextChannel}.
 */
public class ImplServerTextChannel implements ServerTextChannel {

	/**
	 * The discord api instance.
	 */
	private final ImplDiscordApi api;

	/**
	 * The id of the channel.
	 */
	private final long id;

	/**
	 * The name of the channel.
	 */
	private String name;

	/**
	 * The server of the channel.
	 */
	private final ImplServer server;

	/**
	 * The position of the channel.
	 */
	private int position;

	/**
	 * The message cache of the server text channel.
	 */
	private final ImplMessageCache messageCache;

	/**
	 * Whether the channel is "not safe for work" or not.
	 */
	private boolean nsfw = false;

	/**
	 * The parent id of the channel.
	 */
	private long parentId;

	/**
	 * A map with all overwritten user permissions.
	 */
	private final ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions = new ConcurrentHashMap<>();

	/**
	 * A map with all overwritten role permissions.
	 */
	private final ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions = new ConcurrentHashMap<>();

	/**
	 * A map which contains all listeners. The key is the class of the listener.
	 */
	private final ConcurrentHashMap<Class<?>, List<Object>> listeners = new ConcurrentHashMap<>();

	/**
	 * Creates a new server text channel object.
	 *
	 * @param api
	 *            The discord api instance.
	 * @param server
	 *            The server of the channel.
	 * @param data
	 *            The json data of the channel.
	 */
	public ImplServerTextChannel(ImplDiscordApi api, ImplServer server, JSONObject data) {
		this.api = api;
		this.server = server;
		this.messageCache = new ImplMessageCache(api, api.getDefaultMessageCacheCapacity(),
				api.getDefaultMessageCacheStorageTimeInSeconds());

		id = Long.parseLong(data.getString("id"));
		name = data.getString("name");
		position = data.getInt("position");
		nsfw = data.has("nsfw") && data.getBoolean("nsfw");
		parentId = Long.valueOf(data.optString("parent_id", "-1"));

		JSONArray permissionOverwritesJson = data.optJSONArray("permission_overwrites");
		permissionOverwritesJson = permissionOverwritesJson == null ? new JSONArray() : permissionOverwritesJson;
		for (int i = 0; i < permissionOverwritesJson.length(); i++) {
			JSONObject permissionOverwrite = permissionOverwritesJson.getJSONObject(i);
			long id = Long.parseLong(permissionOverwrite.optString("id", "-1"));
			int allow = permissionOverwrite.optInt("allow", 0);
			int deny = permissionOverwrite.optInt("deny", 0);
			Permissions permissions = new ImplPermissions(allow, deny);
			switch (permissionOverwrite.getString("type")) {
			case "role":
				overwrittenRolePermissions.put(id, permissions);
				break;
			case "member":
				overwrittenUserPermissions.put(id, permissions);
				break;
			}
		}

		server.addChannelToCache(this);
	}

	/**
	 * Sets the name of the channel.
	 *
	 * @param name
	 *            The new name of the channel.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the position of the channel.
	 *
	 * @param position
	 *            The new position of the channel.
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Adds a listener.
	 *
	 * @param clazz
	 *            The listener class.
	 * @param listener
	 *            The listener to add.
	 */
	private void addListener(Class<?> clazz, Object listener) {
		List<Object> classListeners = listeners.computeIfAbsent(clazz, c -> new ArrayList<>());
		classListeners.add(listener);
	}

	/**
	 * Gets all listeners of the given class.
	 *
	 * @param clazz
	 *            The class of the listener.
	 * @param <T>
	 *            The class of the listener.
	 * @return A list with all listeners of the given type.
	 */
	@SuppressWarnings("unchecked") // We make sure it's the right type when adding elements
	private <T> List<T> getListeners(Class<?> clazz) {
		List<Object> classListeners = listeners.getOrDefault(clazz, new ArrayList<>());
		return classListeners.stream().map(o -> (T) o).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public DiscordApi getApi() {
		return api;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public boolean isNsfw() {
		return nsfw;
	}

	@Override
	public Optional<ChannelCategory> getCategory() {
		return getServer().getChannelCategoryById(parentId);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Server getServer() {
		return server;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public Permissions getOverwrittenPermissions(User user) {
		return overwrittenUserPermissions.getOrDefault(user.getId(), ImplPermissions.EMPTY_PERMISSIONS);
	}

	@Override
	public Permissions getOverwrittenPermissions(Role role) {
		return overwrittenRolePermissions.getOrDefault(role.getId(), ImplPermissions.EMPTY_PERMISSIONS);
	}

	@Override
	public MessageCache getMessageCache() {
		return messageCache;
	}

	@Override
	public String getMentionTag() {
		return "<#" + getId() + ">";
	}

	@Override
	public String toString() {
		return String.format("ServerTextChannel (id: %s, name: %s)", getId(), getName());
	}

	@Override
	public void addMessageCreateListener(MessageCreateListener listener) {
		addListener(MessageCreateListener.class, listener);
	}

	@Override
	public List<MessageCreateListener> getMessageCreateListeners() {
		return getListeners(MessageCreateListener.class);
	}

	@Override
	public void addUserStartTypingListener(UserStartTypingListener listener) {
		addListener(UserStartTypingListener.class, listener);
	}

	@Override
	public List<UserStartTypingListener> getUserStartTypingListeners() {
		return getListeners(UserStartTypingListener.class);
	}

	@Override
	public void addServerChannelDeleteListener(ServerChannelDeleteListener listener) {
		addListener(ServerChannelDeleteListener.class, listener);
	}

	@Override
	public List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
		return getListeners(ServerChannelDeleteListener.class);
	}

	@Override
	public void addMessageDeleteListener(MessageDeleteListener listener) {
		addListener(MessageDeleteListener.class, listener);
	}

	@Override
	public List<MessageDeleteListener> getMessageDeleteListeners() {
		return getListeners(MessageDeleteListener.class);
	}

	@Override
	public void addMessageEditListener(MessageEditListener listener) {
		addListener(MessageEditListener.class, listener);
	}

	@Override
	public List<MessageEditListener> getMessageEditListeners() {
		return getListeners(MessageEditListener.class);
	}

	@Override
	public void addReactionAddListener(ReactionAddListener listener) {
		addListener(ReactionAddListener.class, listener);
	}

	@Override
	public List<ReactionAddListener> getReactionAddListeners() {
		return getListeners(ReactionAddListener.class);
	}

	@Override
	public void addReactionRemoveListener(ReactionRemoveListener listener) {
		addListener(ReactionRemoveListener.class, listener);
	}

	@Override
	public List<ReactionRemoveListener> getReactionRemoveListeners() {
		return getListeners(ReactionRemoveListener.class);
	}

	@Override
	public void addServerChannelChangeNameListener(ServerChannelChangeNameListener listener) {
		addListener(ServerChannelChangeNameListener.class, listener);
	}

	@Override
	public List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {
		return getListeners(ServerChannelChangeNameListener.class);
	}

	@Override
	public void addServerChannelChangePositionListener(ServerChannelChangePositionListener listener) {
		addListener(ServerChannelChangePositionListener.class, listener);
	}

	@Override
	public List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {
		return getListeners(ServerChannelChangePositionListener.class);
	}

	@Override
	public boolean canBeAccessedBy(User user) {
		return ((this.getEffectiveAllowedPermissions(user).contains(PermissionType.READ_MESSAGES))
				&& (this.getEffectiveAllowedPermissions(user).contains(PermissionType.READ_MESSAGE_HISTORY)));
	}
}
