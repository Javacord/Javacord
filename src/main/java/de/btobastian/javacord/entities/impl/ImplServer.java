package de.btobastian.javacord.entities.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Region;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.channels.impl.ImplServerTextChannel;
import de.btobastian.javacord.entities.channels.impl.ImplServerVoiceChannel;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.listeners.server.ServerBecomesUnavailableListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelCreateListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.listeners.user.UserStartTypingListener;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link de.btobastian.javacord.entities.Server}.
 */
public class ImplServer implements Server {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplServer.class);

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the server.
     */
    private final long id;

    /**
     * The name of the server.
     */
    private String name;

    /**
     * The region of the server.
     */
    private Region region;

    /**
     * Whether the server is considered as large or not.
     */
    private boolean large;

    /**
     * The id of the owner.
     */
    private long ownerId;

    /**
     * The amount of members in this server.
     */
    private int memberCount = -1;

    /**
     * The icon id of the server. Might be <code>null</code>.
     */
    private String iconId;

    /**
     * A map with all channels of the server.
     */
    private final ConcurrentHashMap<Long, ServerChannel> channels = new ConcurrentHashMap<>();

    /**
     * A map with all members of the server.
     */
    private final ConcurrentHashMap<Long, User> members = new ConcurrentHashMap<>();

    /**
     * A map with all nicknames. The key is the user id.
     */
    private final ConcurrentHashMap<Long, String> nicknames = new ConcurrentHashMap<>();

    /**
     * A list with all custom emojis from this server.
     */
    private final Collection<CustomEmoji> customEmojis = new ArrayList<>();

    /**
     * A map which contains all listeners.
     * The key is the class of the listener.
     */
    private final ConcurrentHashMap<Class<?>, List<Object>> listeners = new ConcurrentHashMap<>();

    /**
     * Creates a new server object.
     *
     * @param api The discord api instance.
     * @param data The json data of the server.
     */
    public ImplServer(ImplDiscordApi api, JSONObject data) {
        this.api = api;

        id = Long.parseLong(data.getString("id"));
        name = data.getString("name");
        region = Region.getRegionByKey(data.getString("region"));
        large = data.getBoolean("large");
        memberCount = data.getInt("member_count");
        ownerId = Long.parseLong(data.getString("owner_id"));
        if (data.has("icon") && !data.isNull("icon")) {
            iconId = data.getString("icon");
        }

        if (data.has("channels")) {
            JSONArray channels = data.getJSONArray("channels");
            for (int i = 0; i < channels.length(); i++) {
                JSONObject channel = channels.getJSONObject(i);
                switch (channel.getInt("type")) {
                    case 0:
                        getOrCreateServerTextChannel(channel);
                        break;
                    case 2:
                        getOrCreateServerVoiceChannel(channel);
                        break;
                }
            }
        }

        JSONArray members = new JSONArray();
        if (data.has("members")) {
            members = data.getJSONArray("members");
        }
        addMembers(members);

        if (isLarge() && getMembers().size() < getMemberCount()) {
            JSONObject requestGuildMembersPacket = new JSONObject()
                    .put("op", 8)
                    .put("d", new JSONObject()
                            .put("guild_id", String.valueOf(getId()))
                            .put("query","")
                            .put("limit", 0));
            logger.debug("Sending request guild members packet for server {}", this);
            this.api.getWebSocketAdapter().getWebSocket().sendText(requestGuildMembersPacket.toString());
        }

        JSONArray emojis = data.has("emojis") ? data.getJSONArray("emojis") : new JSONArray();
        for (int i = 0; i < emojis.length(); i++) {
            CustomEmoji emoji = api.getOrCreateCustomEmoji(emojis.getJSONObject(i));
            this.customEmojis.add(emoji);
        }

        api.addServerToCache(this);
    }

    /**
     * Adds a channel to the cache.
     *
     * @param channel The channel to add.
     */
    public void addChannelToCache(ServerChannel channel) {
        channels.put(channel.getId(), channel);
    }

    /**
     * Removes a channel from the cache.
     *
     * @param channelId The if of the channel to remove.
     */
    public void removeChannelFromCache(long channelId) {
        channels.remove(channelId);
    }

    /**
     * Gets or creates a server text channel.
     *
     * @param data The json data of the channel.
     * @return The server text channel.
     */
    public ServerTextChannel getOrCreateServerTextChannel(JSONObject data) {
        long id = Long.parseLong(data.getString("id"));
        int type = data.getInt("type");
        synchronized (this) {
            if (type == 0) {
                return getTextChannelById(id).orElse(new ImplServerTextChannel(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Gets or creates a server voice channel.
     *
     * @param data The json data of the channel.
     * @return The server voice channel.
     */
    public ServerVoiceChannel getOrCreateServerVoiceChannel(JSONObject data) {
        long id = Long.parseLong(data.getString("id"));
        int type = data.getInt("type");
        synchronized (this) {
            if (type == 2) {
                return getVoiceChannelById(id).orElse(new ImplServerVoiceChannel(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Adds a user to the server.
     *
     * @param user The user to add.
     */
    public void addMember(User user) {
        members.put(user.getId(), user);
    }

    /**
     * Adds members to the server.
     *
     * @param members An array of guild member objects.
     */
    public void addMembers(JSONArray members) {
        for (int i = 0; i < members.length(); i++) {
            User member = api.getOrCreateUser(members.getJSONObject(i).getJSONObject("user"));
            if (members.getJSONObject(i).has("nick") && !members.getJSONObject(i).isNull("nick")) {
                nicknames.put(member.getId(), members.getJSONObject(i).getString("nick"));
            }
            addMember(member);

            JSONArray memberRoles = members.getJSONObject(i).getJSONArray("roles");
            for (int j = 0; j < memberRoles.length(); j++) {
                // TODO add to roles
            }
        }
    }

    /**
     * Adds a listener.
     *
     * @param clazz The listener class.
     * @param listener The listener to add.
     */
    private void addListener(Class<?> clazz, Object listener) {
        List<Object> classListeners = listeners.computeIfAbsent(clazz, c -> new ArrayList<>());
        classListeners.add(listener);
    }

    /**
     * Gets all listeners of the given class.
     *
     * @param clazz The class of the listener.
     * @param <T> The class of the listener.
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
    public String getName() {
        return name;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public Optional<String> getNickname(User user) {
        return Optional.ofNullable(nicknames.get(user.getId()));
    }

    @Override
    public Collection<User> getMembers() {
        return members.values();
    }

    @Override
    public boolean isLarge() {
        return large;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public User getOwner() {
        return api.getUserById(ownerId)
                .orElseThrow(() -> new IllegalStateException("Owner of server " + toString() + " is not cached!"));
    }

    @Override
    public Optional<URL> getIconUrl() {
        if (iconId == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL("https://cdn.discordapp.com/icons/" + getId() + "/" + iconId + ".png"));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public Collection<CustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableCollection(customEmojis);
    }

    @Override
    public Collection<ServerChannel> getChannels() {
        return channels.values();
    }

    @Override
    public Optional<ServerChannel> getChannelById(long id) {
        return Optional.ofNullable(channels.get(id));
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
    public void addServerLeaveListener(ServerLeaveListener listener) {
        addListener(ServerLeaveListener.class, listener);
    }

    @Override
    public List<ServerLeaveListener> getServerLeaveListeners() {
        return getListeners(ServerLeaveListener.class);
    }

    @Override
    public void addServerBecomesUnavailableListener(ServerBecomesUnavailableListener listener) {
        addListener(ServerBecomesUnavailableListener.class, listener);
    }

    @Override
    public List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners() {
        return getListeners(ServerBecomesUnavailableListener.class);
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
    public void addServerChannelCreateListener(ServerChannelCreateListener listener) {
        addListener(ServerChannelCreateListener.class, listener);
    }

    @Override
    public List<ServerChannelCreateListener> getServerChannelCreateListeners() {
        return getListeners(ServerChannelCreateListener.class);
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
}
