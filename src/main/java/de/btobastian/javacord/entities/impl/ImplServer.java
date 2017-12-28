package de.btobastian.javacord.entities.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ExplicitContentFilterLevel;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.*;
import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.channels.impl.ImplChannelCategory;
import de.btobastian.javacord.entities.channels.impl.ImplServerTextChannel;
import de.btobastian.javacord.entities.channels.impl.ImplServerVoiceChannel;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
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
     * The verification level of the server.
     */
    private VerificationLevel verificationLevel;

    /**
     * The explicit content filter level of the server.
     */
    private ExplicitContentFilterLevel explicitContentFilterLevel;

    /**
     * The default message notification level of the server.
     */
    private DefaultMessageNotificationLevel defaultMessageNotificationLevel;

    /**
     * The amount of members in this server.
     */
    private int memberCount = -1;

    /**
     * The icon hash of the server. Might be <code>null</code>.
     */
    private String iconHash;

    /**
     * The splash of the server. Might be <code>null</code>.
     */
    private String splash;

    /**
     * A map with all roles of the server.
     */
    private final ConcurrentHashMap<Long, Role> roles = new ConcurrentHashMap<>();

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
     * Creates a new server object.
     *
     * @param api The discord api instance.
     * @param data The json data of the server.
     */
    public ImplServer(ImplDiscordApi api, JsonNode data) {
        this.api = api;

        id = Long.parseLong(data.get("id").asText());
        name = data.get("name").asText();
        region = Region.getRegionByKey(data.get("region").asText());
        large = data.get("large").asBoolean();
        memberCount = data.get("member_count").asInt();
        ownerId = Long.parseLong(data.get("owner_id").asText());
        verificationLevel = VerificationLevel.fromId(data.get("verification_level").asInt());
        explicitContentFilterLevel = ExplicitContentFilterLevel.fromId(data.get("explicit_content_filter").asInt());
        defaultMessageNotificationLevel =
                DefaultMessageNotificationLevel.fromId(data.get("default_message_notifications").asInt());
        if (data.has("icon") && !data.get("icon").isNull()) {
            iconHash = data.get("icon").asText();
        }
        if (data.has("splash") && !data.get("splash").isNull()) {
            splash = data.get("splash").asText();
        }

        if (data.has("channels")) {
            for (JsonNode channel : data.get("channels")) {
                switch (channel.get("type").asInt()) {
                    case 0:
                        getOrCreateServerTextChannel(channel);
                        break;
                    case 2:
                        getOrCreateServerVoiceChannel(channel);
                        break;
                    case 4:
                        getOrCreateChannelCategory(channel);
                        break;
                }
            }
        }

        if (data.has("roles")) {
            for (JsonNode roleJson : data.get("roles")) {
                Role role = new ImplRole(api, this, roleJson);
                this.roles.put(role.getId(), role);
            }
        }

        if (data.has("members")) {
            addMembers(data.get("members"));
        }

        if (isLarge() && getMembers().size() < getMemberCount()) {
            ObjectNode requestGuildMembersPacket = JsonNodeFactory.instance.objectNode()
                    .put("op", 8);
            requestGuildMembersPacket.putObject("d")
                            .put("guild_id", String.valueOf(getId()))
                            .put("query","")
                            .put("limit", 0);
            logger.debug("Sending request guild members packet for server {}", this);
            this.api.getWebSocketAdapter().getWebSocket().sendText(requestGuildMembersPacket.toString());
        }

        if (data.has("emojis")) {
            for (JsonNode emojiJson : data.get("emojis")) {
                CustomEmoji emoji = api.getOrCreateCustomEmoji(this, emojiJson);
                addCustomEmoji(emoji);
            }
        }

        if (data.has("presences")) {
            for (JsonNode presenceJson : data.get("presences")) {
                long userId = Long.parseLong(presenceJson.get("user").get("id").asText());
                api.getUserById(userId).map(user -> ((ImplUser) user)).ifPresent(user -> {
                    if (presenceJson.has("game")) {
                        Game game = null;
                        if (!presenceJson.get("game").isNull()) {
                            int gameType = presenceJson.get("game").get("type").asInt();
                            String name = presenceJson.get("game").get("name").asText();
                            String streamingUrl =
                                    presenceJson.get("game").has("url") &&
                                            !presenceJson.get("game").get("url").isNull() ?
                                            presenceJson.get("game").get("url").asText() : null;
                            game = new ImplGame(GameType.getGameTypeById(gameType), name, streamingUrl);
                        }
                        user.setGame(game);
                    }
                    if (presenceJson.has("status")) {
                        UserStatus status = UserStatus.fromString(presenceJson.get("status").asText());
                        user.setStatus(status);
                    }
                });
            }
        }

        api.addServerToCache(this);
    }

    /**
     * Gets the icon hash of the server.
     *
     * @return The icon hash of the server.
     */
    public String getIconHash() {
        return iconHash;
    }

    /**
     * Sets the icon hash of the server.
     *
     * @param iconHash The icon hash of the server.
     */
    public void setIconHash(String iconHash) {
        this.iconHash = iconHash;
    }

    /**
     * Sets the verification level of the server.
     *
     * @param verificationLevel The verification level of the server.
     */
    public void setVerificationLevel(VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    /**
     * Sets the region of the server.
     *
     * @param region The region of the server.
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Sets the default message notification level of the server.
     *
     * @param defaultMessageNotificationLevel The default message notification level to set.
     */
    public void setDefaultMessageNotificationLevel(DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
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
     * @param channelId The id of the channel to remove.
     */
    public void removeChannelFromCache(long channelId) {
        channels.remove(channelId);
    }

    /**
     * Removes a role from the cache.
     *
     * @param roleId The id of the role to remove.
     */
    public void removeRole(long roleId) {
        roles.remove(roleId);
    }

    /**
     * Adds a custom emoji.
     *
     * @param emoji The emoji to add.
     */
    public void addCustomEmoji(CustomEmoji emoji) {
        customEmojis.add(emoji);
    }

    /**
     * Removes a custom emoji.
     *
     * @param emoji The emoji to remove.
     */
    public void removeCustomEmoji(CustomEmoji emoji) {
        customEmojis.remove(emoji);
    }

    /**
     * Gets or create a new role.
     *
     * @param data The json data of the role.
     * @return The role.
     */
    public Role getOrCreateRole(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        synchronized (this) {
            return getRoleById(id).orElseGet(() -> {
                Role role = new ImplRole(api, this, data);
                this.roles.put(role.getId(), role);
                return role;
            });
        }
    }

    /**
     * Gets or creates a channel category.
     *
     * @param data The json data of the channel.
     * @return The server text channel.
     */
    public ChannelCategory getOrCreateChannelCategory(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        int type = data.get("type").asInt();
        synchronized (this) {
            if (type == 4) {
                return getChannelCategoryById(id).orElseGet(() -> new ImplChannelCategory(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Gets or creates a server text channel.
     *
     * @param data The json data of the channel.
     * @return The server text channel.
     */
    public ServerTextChannel getOrCreateServerTextChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        int type = data.get("type").asInt();
        synchronized (this) {
            if (type == 0) {
                return getTextChannelById(id).orElseGet(() -> new ImplServerTextChannel(api, this, data));
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
    public ServerVoiceChannel getOrCreateServerVoiceChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        int type = data.get("type").asInt();
        synchronized (this) {
            if (type == 2) {
                return getVoiceChannelById(id).orElseGet(() -> new ImplServerVoiceChannel(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Removes a member from the server.
     *
     * @param user The user to remove.
     */
    public void removeMember(User user) {
        members.remove(user.getId());
        nicknames.remove(user.getId());
        getRoles().forEach(role -> ((ImplRole) role).removeUserFromCache(user));
    }

    /**
     * Adds a member to the server.
     *
     * @param member The user to add.
     */
    public void addMember(JsonNode member) {
        User user = api.getOrCreateUser(member.get("user"));
        members.put(user.getId(), user);
        if (member.has("nick") && !member.get("nick").isNull()) {
            nicknames.put(user.getId(), member.get("nick").asText());
        }

        for (JsonNode roleIds : member.get("roles")) {
            long roleId = Long.parseLong(roleIds.asText());
            getRoleById(roleId).map(role -> ((ImplRole) role)).ifPresent(role -> role.addUserToCache(user));
        }
    }

    /**
     * Sets the nickname of the user.
     *
     * @param user The user.
     * @param nickname The nickname to set.
     */
    public void setNickname(User user, String nickname) {
        if (nickname == null) {
            nicknames.remove(user.getId());
        } else {
            nicknames.put(user.getId(), nickname);
        }
    }

    /**
     * Adds members to the server.
     *
     * @param members An array of guild member objects.
     */
    public void addMembers(JsonNode members) {
        for (JsonNode member : members) {
            addMember(member);
        }
    }

    /**
     * Sets the name of the server.
     *
     * @param name The name of the server.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets an unordered collection with all channels in the server.
     *
     * @return An unordered collection with all channels in the server.
     */
    public Collection<ServerChannel> getUnorderedChannels() {
        return channels.values();
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
    public VerificationLevel getVerificationLevel() {
        return verificationLevel;
    }

    @Override
    public ExplicitContentFilterLevel getExplicitContentFilterLevel() {
        return explicitContentFilterLevel;
    }

    @Override
    public DefaultMessageNotificationLevel getDefaultMessageNotificationLevel() {
        return defaultMessageNotificationLevel;
    }

    @Override
    public Optional<Icon> getIcon() {
        if (iconHash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(
                    getApi(), new URL("https://cdn.discordapp.com/icons/" + getId() + "/" + iconHash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Icon> getSplash() {
        if (splash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(
                    getApi(), new URL("https://cdn.discordapp.com/splashes/" + getId() + "/" + splash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public List<Role> getRoles() {
        return roles.values().stream()
                .sorted(Comparator.comparingInt(Role::getPosition))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Role> getRoleById(long id) {
        return Optional.ofNullable(roles.get(id));
    }

    @Override
    public Collection<CustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableCollection(customEmojis);
    }

    @Override
    public List<ServerChannel> getChannels() {
        Collection<ServerChannel> channelsUnordered = this.channels.values();
        List<ServerChannel> channels = new ArrayList<>();
        channelsUnordered.stream()
                .filter(channel -> !channel.asChannelCategory().isPresent())
                .filter(channel -> channel.asServerTextChannel().isPresent())
                .filter(channel -> !channel.asServerTextChannel().get().getCategory().isPresent())
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .forEachOrdered(channels::add);
        channelsUnordered.stream()
                .filter(channel -> !channel.asChannelCategory().isPresent())
                .filter(channel -> channel.asServerVoiceChannel().isPresent())
                .filter(channel -> !channel.asServerVoiceChannel().get().getCategory().isPresent())
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .forEachOrdered(channels::add);
        getChannelCategories().forEach(category -> {
                    channels.add(category);
                    channels.addAll(category.getChannels());
                });
        return channels;
    }

    @Override
    public Optional<ServerChannel> getChannelById(long id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public String toString() {
        return String.format("Server (id: %s, name: %s)", getId(), getName());
    }

}
