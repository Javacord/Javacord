package org.javacord.entity.server.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.Icon;
import org.javacord.entity.Region;
import org.javacord.entity.activity.Activity;
import org.javacord.entity.activity.impl.ImplActivity;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ChannelCategoryBuilder;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerTextChannelBuilder;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.channel.ServerVoiceChannelBuilder;
import org.javacord.entity.channel.impl.ImplChannelCategory;
import org.javacord.entity.channel.impl.ImplChannelCategoryBuilder;
import org.javacord.entity.channel.impl.ImplServerTextChannel;
import org.javacord.entity.channel.impl.ImplServerTextChannelBuilder;
import org.javacord.entity.channel.impl.ImplServerVoiceChannel;
import org.javacord.entity.channel.impl.ImplServerVoiceChannelBuilder;
import org.javacord.entity.emoji.CustomEmojiBuilder;
import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.entity.emoji.impl.ImplCustomEmojiBuilder;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.impl.ImplRole;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.ExplicitContentFilterLevel;
import org.javacord.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.ServerUpdater;
import org.javacord.entity.server.VerificationLevel;
import org.javacord.entity.user.User;
import org.javacord.entity.user.UserStatus;
import org.javacord.entity.user.impl.ImplUser;
import org.javacord.util.Cleanupable;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.Icon;
import org.javacord.entity.Region;
import org.javacord.entity.emoji.CustomEmojiBuilder;
import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.entity.emoji.impl.ImplCustomEmojiBuilder;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.ExplicitContentFilterLevel;
import org.javacord.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.ServerUpdater;
import org.javacord.entity.server.VerificationLevel;
import org.javacord.entity.user.User;
import org.javacord.entity.user.UserStatus;
import org.javacord.entity.user.impl.ImplUser;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link Server}.
 */
public class ImplServer implements Server, Cleanupable {

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
     * The application id of the owner.
     */
    private long applicationId = -1;

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
     * The multi factor authentication level of the server.
     */
    private MultiFactorAuthenticationLevel multiFactorAuthenticationLevel;

    /**
     * The amount of members in this server.
     */
    private int memberCount;

    /**
     * The icon hash of the server. Might be <code>null</code>.
     */
    private String iconHash;

    /**
     * The splash of the server. Might be <code>null</code>.
     */
    private String splash;

    /**
     * The id of the server's system channel.
     */
    private long systemChannelId = -1;

    /**
     * The id of the server's afk channel.
     */
    private long afkChannelId = -1;

    /**
     * The server's afk timeout.
     */
    private int afkTimeout = 0;

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
     * A map with all joinedAt instants. The key is the user id.
     */
    private final ConcurrentHashMap<Long, Instant> joinedAtTimestamps = new ConcurrentHashMap<>();

    /**
     * A list with all custom emojis from this server.
     */
    private final Collection<KnownCustomEmoji> customEmojis = new ArrayList<>();

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
        multiFactorAuthenticationLevel = MultiFactorAuthenticationLevel.fromId(data.get("mfa_level").asInt());
        if (data.has("icon") && !data.get("icon").isNull()) {
            iconHash = data.get("icon").asText();
        }
        if (data.has("splash") && !data.get("splash").isNull()) {
            splash = data.get("splash").asText();
        }
        if (data.hasNonNull("afk_channel_id")) {
            afkChannelId = data.get("afk_channel_id").asLong();
        }
        if (data.hasNonNull("afk_timeout")) {
            afkTimeout = data.get("afk_timeout").asInt();
        }
        if (data.hasNonNull("system_channel_id")) {
            systemChannelId = data.get("system_channel_id").asLong();
        }
        if (data.hasNonNull("application_id")) {
            applicationId = data.get("application_id").asLong();
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

        if (data.hasNonNull("voice_states")) {
            for (JsonNode voiceStateJson : data.get("voice_states")) {
                ImplServerVoiceChannel channel =
                        (ImplServerVoiceChannel) getVoiceChannelById(voiceStateJson.get("channel_id").asLong())
                                .orElseThrow(AssertionError::new);
                ImplUser user = (ImplUser) api.getCachedUserById(voiceStateJson.get("user_id").asLong())
                        .orElseThrow(AssertionError::new);
                channel.addConnectedUser(user);
                user.addConnectedVoiceChannel(channel);
            }
        }

        if (isLarge() && getMembers().size() < getMemberCount()) {
            this.api.getWebSocketAdapter().queueRequestGuildMembers(this);
        }

        if (data.has("emojis")) {
            for (JsonNode emojiJson : data.get("emojis")) {
                KnownCustomEmoji emoji = api.getOrCreateKnownCustomEmoji(this, emojiJson);
                addCustomEmoji(emoji);
            }
        }

        if (data.has("presences")) {
            for (JsonNode presenceJson : data.get("presences")) {
                long userId = Long.parseLong(presenceJson.get("user").get("id").asText());
                api.getCachedUserById(userId).map(ImplUser.class::cast).ifPresent(user -> {
                    if (presenceJson.has("game")) {
                        Activity activity = null;
                        if (!presenceJson.get("game").isNull()) {
                            activity = new ImplActivity(presenceJson.get("game"));
                        }
                        user.setActivity(activity);
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
     * Gets the splash hash of the server.
     *
     * @return The splash hash of the server.
     */
    public String getSplashHash() {
        return splash;
    }

    /**
     * Sets the splash hash of the server.
     *
     * @param splashHash The splash hash of the server.
     */
    public void setSplashHash(String splashHash) {
        this.splash = splashHash;
    }

    /**
     * Sets the system channel id of the server.
     *
     * @param systemChannelId The system channel id of the server.
     */
    public void setSystemChannelId(long systemChannelId) {
        this.systemChannelId = systemChannelId;
    }

    /**
     * Sets the afk channel id of the server.
     *
     * @param afkChannelId The afk channel id of the server.
     */
    public void setAfkChannelId(long afkChannelId) {
        this.afkChannelId = afkChannelId;
    }

    /**
     * Sets the afk timeout of the server.
     *
     * @param afkTimeout The afk timeout to set.
     */
    public void setAfkTimeout(int afkTimeout) {
        this.afkTimeout = afkTimeout;
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
     * Sets the server owner id.
     *
     * @param ownerId The owner id to set.
     */
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Sets the application id.
     *
     * @param applicationId The application id to set.
     */
    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Sets the explicit content filter level of the server.
     *
     * @param explicitContentFilterLevel The explicit content filter level to set.
     */
    public void setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel) {
        this.explicitContentFilterLevel = explicitContentFilterLevel;
    }

    /**
     * Sets the multi factor authentication level of the server.
     *
     * @param multiFactorAuthenticationLevel The multi factor authentication level to set.
     */
    public void setMultiFactorAuthenticationLevel(MultiFactorAuthenticationLevel multiFactorAuthenticationLevel) {
        this.multiFactorAuthenticationLevel = multiFactorAuthenticationLevel;
    }

    /**
     * Adds a channel to the cache.
     *
     * @param channel The channel to add.
     */
    public void addChannelToCache(ServerChannel channel) {
        ServerChannel oldChannel = channels.put(channel.getId(), channel);
        if ((oldChannel instanceof Cleanupable) && (oldChannel != channel)) {
            ((Cleanupable) oldChannel).cleanup();
        }
    }

    /**
     * Removes a channel from the cache.
     *
     * @param channelId The id of the channel to remove.
     */
    public void removeChannelFromCache(long channelId) {
        channels.computeIfPresent(channelId, (key, channel) -> {
            if (channel instanceof Cleanupable) {
                ((Cleanupable) channel).cleanup();
            }
            return null;
        });
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
    public void addCustomEmoji(KnownCustomEmoji emoji) {
        customEmojis.add(emoji);
    }

    /**
     * Removes a custom emoji.
     *
     * @param emoji The emoji to remove.
     */
    public void removeCustomEmoji(KnownCustomEmoji emoji) {
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
        joinedAtTimestamps.remove(user.getId());
    }

    /**
     * Decrements the member count.
     */
    public void decrementMemberCount() {
        memberCount--;
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

        joinedAtTimestamps.put(user.getId(), OffsetDateTime.parse(member.get("joined_at").asText()).toInstant());
    }

    /**
     * Increments the member count.
     */
    public void incrementMemberCount() {
        memberCount++;
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
    public Optional<Instant> getJoinedAtTimestamp(User user) {
        return Optional.ofNullable(joinedAtTimestamps.get(user.getId()));
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
        return api.getCachedUserById(ownerId)
                .orElseThrow(() -> new IllegalStateException("Owner of server " + toString() + " is not cached!"));
    }

    @Override
    public Optional<Long> getApplicationId() {
        return applicationId != -1 ? Optional.of(applicationId) : Optional.empty();
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
    public MultiFactorAuthenticationLevel getMultiFactorAuthenticationLevel() {
        return multiFactorAuthenticationLevel;
    }

    @Override
    public Optional<Icon> getIcon() {
        if (iconHash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(
                    getApi(),
                    new URL("https://cdn.discordapp.com/icons/" + getIdAsString() + "/" + iconHash + ".png")));
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
                    getApi(),
                    new URL("https://cdn.discordapp.com/splashes/" + getIdAsString() + "/" + splash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ServerTextChannel> getSystemChannel() {
        return getTextChannelById(systemChannelId);
    }

    @Override
    public Optional<ServerVoiceChannel> getAfkChannel() {
        return getVoiceChannelById(afkChannelId);
    }

    @Override
    public int getAfkTimeoutInSeconds() {
        return afkTimeout;
    }

    @Override
    public Collection<User> getMembers() {
        return Collections.unmodifiableList(new ArrayList<>(members.values()));
    }

    @Override
    public Optional<User> getMemberById(long id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles.values().stream()
                .sorted(Comparator.comparingInt(Role::getPosition))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<Role> getRoleById(long id) {
        return Optional.ofNullable(roles.get(id));
    }

    @Override
    public CustomEmojiBuilder createCustomEmojiBuilder() {
        return new ImplCustomEmojiBuilder(this);
    }

    @Override
    public ServerUpdater createUpdater() {
        return new ImplServerUpdater(this);
    }

    @Override
    public Collection<KnownCustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableCollection(new ArrayList<>(customEmojis));
    }

    @Override
    public ChannelCategoryBuilder createChannelCategoryBuilder() {
        return new ImplChannelCategoryBuilder(this);
    }

    @Override
    public ServerTextChannelBuilder createTextChannelBuilder() {
        return new ImplServerTextChannelBuilder(this);
    }

    @Override
    public ServerVoiceChannelBuilder createVoiceChannelBuilder() {
        return new ImplServerVoiceChannelBuilder(this);
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
        return Collections.unmodifiableList(channels);
    }

    @Override
    public Optional<ServerChannel> getChannelById(long id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public void cleanup() {
        channels.values().stream()
                .filter(Cleanupable.class::isInstance)
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
               || !((o == null)
                    || (getClass() != o.getClass())
                    || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Server (id: %s, name: %s)", getIdAsString(), getName());
    }

}
