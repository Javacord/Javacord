package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.VanityUrlCode;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.auditlog.AuditLog;
import org.javacord.api.entity.auditlog.AuditLogActionType;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.ServerStageVoiceChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextableRegularServerChannel;
import org.javacord.api.entity.channel.UnknownRegularServerChannel;
import org.javacord.api.entity.channel.UnknownServerChannel;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.ActiveThreads;
import org.javacord.api.entity.server.Ban;
import org.javacord.api.entity.server.BoostLevel;
import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.api.entity.server.NsfwLevel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerFeature;
import org.javacord.api.entity.server.SystemChannelFlag;
import org.javacord.api.entity.server.VerificationLevel;
import org.javacord.api.entity.server.invite.RichInvite;
import org.javacord.api.entity.server.invite.WelcomeScreen;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.audio.AudioConnectionImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.entity.VanityUrlCodeImpl;
import org.javacord.core.entity.activity.ActivityImpl;
import org.javacord.core.entity.auditlog.AuditLogImpl;
import org.javacord.core.entity.channel.ChannelCategoryImpl;
import org.javacord.core.entity.channel.RegularServerChannelImpl;
import org.javacord.core.entity.channel.ServerForumChannelImpl;
import org.javacord.core.entity.channel.ServerStageVoiceChannelImpl;
import org.javacord.core.entity.channel.ServerTextChannelImpl;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;
import org.javacord.core.entity.channel.ServerVoiceChannelImpl;
import org.javacord.core.entity.channel.UnknownRegularServerChannelImpl;
import org.javacord.core.entity.channel.UnknownServerChannelImpl;
import org.javacord.core.entity.permission.RoleImpl;
import org.javacord.core.entity.server.invite.InviteImpl;
import org.javacord.core.entity.server.invite.WelcomeScreenImpl;
import org.javacord.core.entity.server.scheduledevent.ServerScheduledEventImpl;
import org.javacord.core.entity.sticker.StickerImpl;
import org.javacord.core.entity.user.Member;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.entity.webhook.IncomingWebhookImpl;
import org.javacord.core.entity.webhook.WebhookImpl;
import org.javacord.core.listener.server.InternalServerAttachableListenerManager;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import org.javacord.core.util.rest.RestRequestResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The implementation of {@link Server}.
 */
public class ServerImpl implements Server, Cleanupable, InternalServerAttachableListenerManager, DispatchQueueSelector {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ServerImpl.class);

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The id of the server.
     */
    private final long id;

    /**
     * The name of the server.
     */
    private volatile String name;

    /**
     * The region of the server.
     */
    private volatile Region region;

    /**
     * Whether the server is considered as large or not.
     */
    private final boolean large;

    /**
     * The id of the owner.
     */
    private volatile long ownerId;

    /**
     * The application id of the owner.
     */
    private volatile long applicationId = -1;

    /**
     * The verification level of the server.
     */
    private volatile VerificationLevel verificationLevel;

    /**
     * The explicit content filter level of the server.
     */
    private volatile ExplicitContentFilterLevel explicitContentFilterLevel;

    /**
     * The default message notification level of the server.
     */
    private volatile DefaultMessageNotificationLevel defaultMessageNotificationLevel;

    /**
     * The multi factor authentication level of the server.
     */
    private volatile MultiFactorAuthenticationLevel multiFactorAuthenticationLevel;

    /**
     * The amount of members in this server.
     */
    private final AtomicInteger memberCount = new AtomicInteger();

    /**
     * The icon hash of the server. Might be <code>null</code>.
     */
    private volatile String iconHash;

    /**
     * The splash of the server. Might be <code>null</code>.
     */
    private volatile String splash;

    /**
     * The id of the server's system channel.
     */
    private volatile long systemChannelId = -1;

    /**
     * The id of the server's afk channel.
     */
    private volatile long afkChannelId = -1;

    /**
     * The server's afk timeout.
     */
    private volatile int afkTimeout = 0;

    /**
     * If the server is ready (all members are cached).
     */
    private volatile boolean ready;

    /**
     * A lock that is used ti prevent lock on {@code audioConnection} and {@code pendingAudioConnection}.
     */
    private final ReentrantLock audioConnectionLock = new ReentrantLock();

    /**
     * All consumers who will be informed when the server is ready.
     */
    private final List<Consumer<Server>> readyConsumers = new ArrayList<>();

    /**
     * All roles of the server.
     */
    private final ConcurrentHashMap<Long, Role> roles = new ConcurrentHashMap<>();

    /**
     * All custom emojis from this server.
     */
    private final Set<KnownCustomEmoji> customEmojis = new HashSet<>();

    /**
     * All features from this server.
     */
    private final Collection<ServerFeature> serverFeatures = new ArrayList<>();

    /**
     * All stickers from this server.
     */
    private final ConcurrentHashMap<Long, Sticker> stickers = new ConcurrentHashMap<>();

    /**
     * The scheduled events for this server.
     */
    private final Map<Long, ServerScheduledEvent> scheduledEvents = new ConcurrentHashMap<>();

    /**
     * The premium tier level of the server.
     */
    private volatile BoostLevel boostLevel;

    /**
     * The NSFW Level of the server.
     */
    private volatile NsfwLevel nsfwLevel;

    /**
     * The server's premium subscription count.
     */
    private volatile int serverBoostCount = 0;

    /**
     * The server's rules channel id.
     */
    private volatile long rulesChannelId = -1;
    /**
     * The servers description.
     */
    private volatile String description;

    /**
     * The id of the server's moderators-only channel.
     */
    private volatile long moderatorsOnlyChannelId = -1;

    /**
     * The servers preferred locale.
     */
    private volatile Locale preferredLocale;

    /**
     * The servers vanity.
     */
    private volatile VanityUrlCode vanityUrlCode;

    /**
     * The discovery splash of the server. Might be <code>null</code>.
     */
    private volatile String discoverySplash;

    /**
     * True if the server widget is enabled.
     */
    private volatile boolean widgetEnabled;

    /**
     * The channel id that the widget will generate an invite to, or null if set to no invite.
     */
    private volatile Long widgetChannelId;

    /**
     * The maximum number of presences for the guild (null is always returned, apart from the largest of guilds).
     */
    private volatile Integer maxPresences;

    /**
     * The maximum number of members for the guild.
     */
    private volatile Integer maxMembers;

    /**
     * The maximum amount of users in a video channel.
     */
    private volatile Integer maxVideoChannelUsers;

    /**
     * The welcome screen of a community server, shown to new members.
     */
    private volatile WelcomeScreen welcomeScreen;

    /**
     * Whether the server's boost progress bar is enabled or not.
     */
    private volatile boolean premiumProgressBarEnabled;

    /**
     * The enum set of all server system channel flags.
     */
    private final EnumSet<SystemChannelFlag> systemChannelFlags =
            EnumSet.noneOf(SystemChannelFlag.class);


    /**
     * Creates a new server object.
     *
     * @param api  The discord api instance.
     * @param data The json data of the server.
     */
    public ServerImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        ready = !api.hasUserCacheEnabled() || !api.isWaitingForUsersOnStartup();

        id = Long.parseLong(data.get("id").asText());
        name = data.get("name").asText();
        region = Region.getRegionByKey(data.get("region").asText());
        large = data.get("large").asBoolean();
        memberCount.set(data.get("member_count").asInt());
        ownerId = Long.parseLong(data.get("owner_id").asText());
        verificationLevel = VerificationLevel.fromId(data.get("verification_level").asInt());
        explicitContentFilterLevel = ExplicitContentFilterLevel.fromId(data.get("explicit_content_filter").asInt());
        defaultMessageNotificationLevel =
                DefaultMessageNotificationLevel.fromId(data.get("default_message_notifications").asInt());
        multiFactorAuthenticationLevel = MultiFactorAuthenticationLevel.fromId(data.get("mfa_level").asInt());
        boostLevel = BoostLevel.fromId(data.get("premium_tier").asInt());
        nsfwLevel = NsfwLevel.fromId(data.get("nsfw_level").asInt());
        preferredLocale = new Locale.Builder().setLanguageTag(data.get("preferred_locale").asText()).build();
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
        if (data.has("features")) {
            data.get("features").forEach(jsonNode -> addFeature(jsonNode.asText()));
        }
        if (data.has("premium_subscription_count")) {
            serverBoostCount = data.get("premium_subscription_count").asInt();
        }
        if (data.hasNonNull("rules_channel_id")) {
            rulesChannelId = data.get("rules_channel_id").asLong();
        }
        if (data.hasNonNull("description")) {
            description = data.get("description").asText();
        }
        if (data.hasNonNull("public_updates_channel_id")) {
            moderatorsOnlyChannelId = data.get("public_updates_channel_id").asLong();
        }
        if (data.hasNonNull("discovery_splash")) {
            discoverySplash = data.get("discovery_splash").asText();
        }
        if (data.hasNonNull("vanity_url_code")) {
            vanityUrlCode = new VanityUrlCodeImpl(data.get("vanity_url_code").asText());
        }
        if (data.hasNonNull("system_channel_flags")) {
            int systemChannelFlag = data.get("system_channel_flags").asInt();
            setSystemChannelFlag(systemChannelFlag);
        }

        if (data.has("guild_scheduled_events")) {
            for (JsonNode event : data.get("guild_scheduled_events")) {
                addScheduledEvent(new ServerScheduledEventImpl(api, event));
            }
        }

        if (data.has("channels")) {
            for (JsonNode channel : data.get("channels")) {

                switch (ChannelType.fromId(channel.get("type").asInt())) {
                    case SERVER_TEXT_CHANNEL:
                        getOrCreateServerTextChannel(channel);
                        break;
                    case SERVER_FORUM_CHANNEL:
                        getOrCreateServerForumChannel(channel);
                        break;
                    case SERVER_VOICE_CHANNEL:
                        getOrCreateServerVoiceChannel(channel);
                        break;
                    case SERVER_STAGE_VOICE_CHANNEL:
                        getOrCreateServerStageVoiceChannel(channel);
                        break;
                    case CHANNEL_CATEGORY:
                        getOrCreateChannelCategory(channel);
                        break;
                    case SERVER_NEWS_CHANNEL:
                        // TODO Handle server news channel differently
                        logger.debug("{} has a news channel. In this Javacord version it is treated as a normal "
                                + "text channel!", this);
                        getOrCreateServerTextChannel(channel);
                        break;
                    case SERVER_STORE_CHANNEL:
                        // TODO Handle store channels
                        logger.debug("{} has a store channel. These are not supported in this Javacord version"
                                + " and get ignored!", this);
                        break;
                    default: {
                        try {
                            if (channel.has("position")) {
                                showFallbackWarningMessage(channel.get("type").asInt(), "UnknownRegularServerChannel");
                                getOrCreateUnknownRegularServerChannel(channel);
                            } else {
                                showFallbackWarningMessage(channel.get("type").asInt(), "UnknownServerChannel");
                                getOrCreateUnknownServerChannel(channel);
                            }
                        } catch (Exception exception) {
                            logger.warn("An error occurred when trying to use a fallback channel implementation",
                                    exception);
                        }
                    }

                }
            }
        }

        if (data.has("threads")) {
            for (JsonNode channel : data.get("threads")) {
                switch (ChannelType.fromId(channel.get("type").asInt())) {
                    case SERVER_PUBLIC_THREAD:
                    case SERVER_PRIVATE_THREAD:
                    case SERVER_NEWS_THREAD:
                        getOrCreateServerThreadChannel(channel);
                        break;
                    default:
                        logger.warn("Unknown or unexpected channel type. Your Javacord version might be outdated!");
                }
            }
        }

        if (data.has("roles")) {
            for (JsonNode roleJson : data.get("roles")) {
                Role role = new RoleImpl(api, this, roleJson);
                this.roles.put(role.getId(), role);
            }
        }

        if (data.has("members")) {
            addMembers(data.get("members"));
        }

        if (data.hasNonNull("voice_states")) {
            for (JsonNode voiceStateJson : data.get("voice_states")) {
                Optional<ServerVoiceChannelImpl> channel =
                        getVoiceChannelById(voiceStateJson.get("channel_id").asLong())
                                .map(ch -> (ServerVoiceChannelImpl) ch);
                // This gracefully disregards any channels in voice_states that are not present in the channels field.
                // Bug occurred on a particular guild and prevented the guild from using the bot entirely.
                // This log should happen almost never but should protect any guilds from failing creation due to
                // mismatching guild data from Discord. https://github.com/discord/discord-api-docs/issues/4455
                if (channel.isPresent()) {
                    long userId = voiceStateJson.get("user_id").asLong();

                    channel.get().addConnectedUser(userId);

                    MemberImpl oldMember = (MemberImpl) getRealMemberById(userId).orElse(null);

                    if (oldMember != null)  {
                        MemberImpl newMember = oldMember
                                .setMuted(voiceStateJson.get("mute").asBoolean())
                                .setDeafened(voiceStateJson.get("deaf").asBoolean())
                                .setSelfMuted(voiceStateJson.get("self_mute").asBoolean())
                                .setSelfDeafened(voiceStateJson.get("self_deaf").asBoolean());

                        api.addMemberToCacheOrReplaceExisting(newMember);
                    }
                } else {
                    logger.warn("Channel " + voiceStateJson.get("channel_id").asLong()
                            + " was found in the voice_states property for server " + this.id
                            + " but was not found in the channels property. It will not be"
                            + " loaded.");
                }
            }
        }

        if (
                (isLarge() || !api.getIntents().contains(Intent.GUILD_PRESENCES))
                        && getMembers().size() < getMemberCount()
                        && api.hasUserCacheEnabled()
        ) {
            api.getWebSocketAdapter().queueRequestGuildMembers(this);
        }

        if (data.has("emojis")) {
            for (JsonNode emojiJson : data.get("emojis")) {
                KnownCustomEmoji emoji = api.getOrCreateKnownCustomEmoji(this, emojiJson);
                addCustomEmoji(emoji);
            }
        }

        if (data.has("stickers")) {
            for (JsonNode stickerJson : data.get("stickers")) {
                Sticker sticker = api.getOrCreateSticker(stickerJson);
                addSticker(sticker);
            }
        }

        if (data.has("presences")) {
            for (JsonNode presenceJson : data.get("presences")) {
                long userId = Long.parseLong(presenceJson.get("user").get("id").asText());
                UserImpl user = api.getCachedUserById(userId)
                        .map(UserImpl.class::cast)
                        .orElse(null);

                if (user == null) {
                    // Ignore rogue presences.
                    // It might be a similar issue than https://github.com/discordapp/discord-api-docs/issues/855
                    continue;
                }

                if (presenceJson.hasNonNull("activities")) {
                    Set<Activity> activities = new HashSet<>();
                    for (JsonNode activityJson : presenceJson.get("activities")) {
                        if (!activityJson.isNull()) {
                            activities.add(new ActivityImpl(api, activityJson));
                        }
                    }
                    api.updateUserPresence(userId, presence -> presence.setActivities(activities));
                }
                if (presenceJson.has("status")) {
                    UserStatus status = UserStatus.fromString(presenceJson.get("status").asText());
                    api.updateUserPresence(userId, presence -> presence.setStatus(status));
                }

                if (presenceJson.has("client_status")) {
                    JsonNode clientStatus = presenceJson.get("client_status");
                    for (DiscordClient client : DiscordClient.values()) {
                        if (clientStatus.hasNonNull(client.getName())) {
                            UserStatus status = UserStatus.fromString(clientStatus.get(client.getName()).asText());
                            api.updateUserPresence(userId, presence -> presence
                                    .setClientStatus(presence.getClientStatus().put(client, status)));
                        } else {
                            api.updateUserPresence(userId, presence -> presence
                                    .setClientStatus(presence.getClientStatus().put(client, UserStatus.OFFLINE)));
                        }
                    }
                }
            }
        }

        if (data.has("welcome_screen")) {
            this.welcomeScreen = new WelcomeScreenImpl(data.get("welcome_screen"));
        } else {
            this.welcomeScreen = null;
        }

        this.widgetEnabled = data.path("widget_enabled").asBoolean(false);
        this.widgetChannelId = data.hasNonNull("widget_channel_id")
                            ? data.get("widget_channel_id").asLong() : null;
        this.maxPresences = data.hasNonNull("max_presences")
                            ? data.get("max_presences").asInt() : null;
        this.maxMembers = data.hasNonNull("max_members")
                            ? data.get("max_members").asInt() : null;
        this.maxVideoChannelUsers = data.hasNonNull("max_video_channel_users")
                            ? data.get("max_video_channel_users").asInt() : null;
        this.premiumProgressBarEnabled = data.path("premium_progress_bar_enabled").asBoolean(false);

        api.addServerToCache(this);
    }

    private void showFallbackWarningMessage(int channelType, String fallbackName) {
        logger.warn("Encountered not handled channel type: {}. "
                        + "Trying to use the {} fallback implementation",
                channelType, fallbackName);
    }

    /**
     * Sets the system channel flags.
     *
     * @param systemChannelFlag The system channel flag.
     */
    public void setSystemChannelFlag(int systemChannelFlag) {
        for (SystemChannelFlag flag : SystemChannelFlag.values()) {
            if ((flag.asInt() & systemChannelFlag) == flag.asInt()) {
                systemChannelFlags.add(flag);
            }
        }
    }

    /**
     * Adds the feature to the collection.
     *
     * @param feature The feature to add.
     */
    private void addFeature(String feature) {
        try {
            serverFeatures.add(ServerFeature.valueOf(feature));
        } catch (Exception ignored) {
            logger.debug("Encountered server with unknown feature {}. Please update to the latest "
                    + "Javacord version or create an issue on the Javacord GitHub page if you are "
                    + "already on the latest version.", feature);
        }
    }

    /**
     * Checks if the server is ready (all members are cached).
     *
     * @return Whether the server is ready or not.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Adds a consumer which will be informed once the server is ready.
     * If the server is already ready, it will immediately call the consumer, otherwise it will be called from the
     * websocket reading thread.
     *
     * @param consumer The consumer which should be called.
     */
    public void addServerReadyConsumer(Consumer<Server> consumer) {
        synchronized (readyConsumers) {
            if (ready) {
                consumer.accept(this);
            } else {
                readyConsumers.add(consumer);
            }
        }
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
                Role role = new RoleImpl(api, this, data);
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
        ChannelType type = ChannelType.fromId(data.get("type").asInt());
        synchronized (this) {
            if (type == ChannelType.CHANNEL_CATEGORY) {
                return getChannelCategoryById(id).orElseGet(() -> new ChannelCategoryImpl(api, this, data));
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
        ChannelType type = ChannelType.fromId(data.get("type").asInt());
        synchronized (this) {
            switch (type) {
                case SERVER_TEXT_CHANNEL:
                case SERVER_NEWS_CHANNEL: // TODO Treat news channels differently
                    return getTextChannelById(id).orElseGet(() -> new ServerTextChannelImpl(api, this, data));
                default:
                    // Invalid channel type
                    return null;
            }
        }
    }

    /**
     * Gets or creates a server text channel.
     *
     * @param data The json data of the channel.
     * @return The server text channel.
     */
    public ServerThreadChannel getOrCreateServerThreadChannel(final JsonNode data) {
        final long id = Long.parseLong(data.get("id").asText());
        final ChannelType type = ChannelType.fromId(data.get("type").asInt());
        synchronized (this) {
            switch (type) {
                case SERVER_PUBLIC_THREAD:
                case SERVER_PRIVATE_THREAD:
                case SERVER_NEWS_THREAD:
                    return getThreadChannelById(id).orElseGet(() -> new ServerThreadChannelImpl(api, this, data));
                default:
                    // Invalid channel type
                    return null;
            }
        }
    }

    /**
     * Gets or creates a server voice channel.
     *
     * @param data The json data of the channel.
     * @return The server voice channel.
     */
    public ServerVoiceChannel getOrCreateServerVoiceChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        ChannelType type = ChannelType.fromId(data.get("type").asInt());
        synchronized (this) {
            if (type == ChannelType.SERVER_VOICE_CHANNEL) {
                return getVoiceChannelById(id).orElseGet(() -> new ServerVoiceChannelImpl(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Gets or creates a server stage voice channel.
     *
     * @param data The json data of the channel.
     * @return The server stage voice channel.
     */
    public ServerStageVoiceChannel getOrCreateServerStageVoiceChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        ChannelType type = ChannelType.fromId(data.get("type").asInt());
        synchronized (this) {
            if (type == ChannelType.SERVER_STAGE_VOICE_CHANNEL) {
                return getStageVoiceChannelById(id).orElseGet(() -> new ServerStageVoiceChannelImpl(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Gets or creates a server forum channel.
     *
     * @param data The json data of the channel.
     * @return The server forum channel.
     */
    public ServerForumChannel getOrCreateServerForumChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        ChannelType type = ChannelType.fromId(data.get("type").asInt());
        synchronized (this) {
            switch (type) {
                case SERVER_FORUM_CHANNEL:
                    return getForumChannelById(id).orElseGet(() -> new ServerForumChannelImpl(api, this, data));
                default:
                    // Invalid channel type
                    return null;
            }
        }
    }

    /**
     * Gets or creates an unknown server channel.
     *
     * @param data The json data of the channel.
     * @return The unknown server channel.
     */
    public UnknownServerChannel getOrCreateUnknownServerChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        ChannelType type = ChannelType.fromId(data.get("type").asInt());
        synchronized (this) {
            return getUnknownChannelById(id).orElseGet(() -> new UnknownServerChannelImpl(api, this, data));
        }
    }

    /**
     * Gets or creates an unknown regular server channel.
     *
     * @param data The json data of the channel.
     * @return The unknown regular server channel.
     */
    public UnknownRegularServerChannel getOrCreateUnknownRegularServerChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        ChannelType type = ChannelType.fromId(data.get("type").asInt());
        synchronized (this) {
            return getUnknownRegularChannelById(id)
                    .orElseGet(() -> new UnknownRegularServerChannelImpl(api, this, data));
        }
    }

    /**
     * Removes a member from the server.
     *
     * @param userId The id of the user to remove.
     */
    public void removeMember(long userId) {
        api.removeMemberFromCache(userId, getId());
    }

    /**
     * Decrements the member count.
     */
    public void decrementMemberCount() {
        memberCount.decrementAndGet();
    }

    /**
     * Adds a member to the server.
     *
     * @param memberJson The user to add.
     * @return The member.
     */
    public MemberImpl addMember(JsonNode memberJson) {
        MemberImpl member = new MemberImpl(api, this, memberJson, null);
        api.addMemberToCacheOrReplaceExisting(member);

        synchronized (readyConsumers) {
            if (!ready && getRealMembers().size() == getMemberCount()) {
                ready = true;
                readyConsumers.forEach(consumer -> consumer.accept(this));
                readyConsumers.clear();
            }
        }
        return member;
    }

    /**
     * Increments the member count.
     */
    public void incrementMemberCount() {
        memberCount.incrementAndGet();
    }

    /**
     * Adds members to the server.
     *
     * @param membersJson An array of guild member objects.
     */
    public void addMembers(JsonNode membersJson) {
        for (JsonNode memberJson : membersJson) {
            addMember(memberJson);
        }
    }

    /**
     * Adds members to the server and returns the added members.
     *
     * @param membersJson An array of guild member objects.
     * @return The added members.
     */
    public List<Member> addAndGetMembers(JsonNode membersJson) {
        List<Member> members = new ArrayList<>();
        for (JsonNode memberJson : membersJson) {
            Member member = addMember(memberJson);
            members.add(member);
        }
        return members;
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
     * Sets the rules channel of the server.
     *
     * @param rulesChannelId The rules channel of the server.
     */
    public void setRulesChannelId(long rulesChannelId) {
        this.rulesChannelId = rulesChannelId;
    }

    /**
     * Sets the moderators-only channel of the server.
     *
     * @param moderatorsOnlyChannelId The moderators-only channel of the server.
     */
    public void setModeratorsOnlyChannelId(long moderatorsOnlyChannelId) {
        this.moderatorsOnlyChannelId = moderatorsOnlyChannelId;
    }

    /**
     * Sets the boost level of the server.
     *
     * @param boostLevel The boost level of the server.
     */
    public void setBoostLevel(BoostLevel boostLevel) {
        this.boostLevel = boostLevel;
    }

    /**
     * Sets the NSFW level of the server.
     *
     * @param nsfwLevel The NSFW level of the server.
     */
    public void setNsfwLevel(NsfwLevel nsfwLevel) {
        this.nsfwLevel = nsfwLevel;
    }

    /**
     * Sets the preferred locale of the server.
     *
     * @param preferredLocale The preferred locale of the server.
     */
    public void setPreferredLocale(Locale preferredLocale) {
        this.preferredLocale = preferredLocale;
    }

    /**
     * Sets the server boost count of the server.
     *
     * @param serverBoostCount The server boost count of the server.
     */
    public void setServerBoostCount(int serverBoostCount) {
        this.serverBoostCount = serverBoostCount;
    }

    /**
     * Sets the description of the server.
     *
     * @param description The description of the server.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the discovery splash hash of the server.
     *
     * @param discoverySplashHash The discovery splash hash of the server.
     */
    public void setDiscoverySplashHash(String discoverySplashHash) {
        discoverySplash = discoverySplashHash;
    }

    /**
     * Gets the discovery splash hash of the server.
     *
     * @return The discovery splash hash of the server.
     */
    public String getDiscoverySplashHash() {
        return discoverySplash;
    }

    /**
     * Sets the vanity url code of the server.
     *
     * @param vanityUrlCode The vanity url code of the server.
     */
    public void setVanityUrlCode(VanityUrlCode vanityUrlCode) {
        this.vanityUrlCode = vanityUrlCode;
    }

    /**
     * Sets the server feature of the server.
     *
     * @param serverFeatures The server feature of the server.
     */
    public void setServerFeatures(Collection<ServerFeature> serverFeatures) {
        this.serverFeatures.clear();
        this.serverFeatures.addAll(serverFeatures);
    }

    @Override
    public Set<ServerChannel> getUnorderedChannels() {
        return api.getEntityCache().get().getChannelCache().getChannelsOfServer(getId());
    }

    /**
     * Sets the audio connection of the server.
     *
     * @param audioConnection The audio connection.
     */
    public void setAudioConnection(AudioConnectionImpl audioConnection) {
        audioConnectionLock.lock();
        try {
            api.setAudioConnection(getId(), audioConnection);
        } finally {
            audioConnectionLock.unlock();
        }
    }

    /**
     * Sets the pending audio connection of the server.
     *
     * <p>A pending connection is a connect that is currently trying to connect to a websocket and establish an udp
     * connection but has not finished.
     *
     * @param audioConnection The audio connection.
     */
    public void setPendingAudioConnection(AudioConnectionImpl audioConnection) {
        audioConnectionLock.lock();
        try {
            api.setPendingAudioConnection(getId(), audioConnection);
        } finally {
            audioConnectionLock.unlock();
        }
    }

    /**
     * Removes an audio connection from the server.
     *
     * @param audioConnection The audio connection to remove.
     */
    public void removeAudioConnection(AudioConnection audioConnection) {
        audioConnectionLock.lock();
        try {
            if (api.getPendingAudioConnectionByServerId(getId()) == audioConnection) {
                api.removePendingAudioConnection(getId());
            }
            if (api.getAudioConnectionByServerId(getId()) == audioConnection) {
                api.removeAudioConnection(getId());
            }
        } finally {
            audioConnectionLock.unlock();
        }
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
    public Optional<AudioConnection> getAudioConnection() {
        return Optional.ofNullable(api.getAudioConnectionByServerId(getId()));
    }

    @Override
    public Set<ServerFeature> getFeatures() {
        return Collections.unmodifiableSet(new HashSet<>(serverFeatures));
    }

    @Override
    public BoostLevel getBoostLevel() {
        return boostLevel;
    }

    @Override
    public int getBoostCount() {
        return serverBoostCount;
    }

    @Override
    public Optional<ServerTextChannel> getRulesChannel() {
        return getTextChannelById(rulesChannelId);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public NsfwLevel getNsfwLevel() {
        return nsfwLevel;
    }

    @Override
    public Optional<ServerTextChannel> getModeratorsOnlyChannel() {
        return getTextChannelById(moderatorsOnlyChannelId);
    }

    @Override
    public Optional<VanityUrlCode> getVanityUrlCode() {
        return Optional.ofNullable(vanityUrlCode);
    }

    @Override
    public Optional<Icon> getDiscoverySplash() {
        if (splash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new IconImpl(
                    getApi(),
                    new URL("https://" + Javacord.DISCORD_CDN_DOMAIN + "/discovery-splashes/" + getIdAsString() + "/"
                            + discoverySplash + ".png")));
        } catch (MalformedURLException e) {
            throw new AssertionError("Unexpected malformed discovery splash url", e);
        }
    }

    @Override
    public Locale getPreferredLocale() {
        return preferredLocale;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public Optional<String> getNickname(User user) {
        return getRealMemberById(user.getId())
                .flatMap(Member::getNickname);
    }

    @Override
    public Optional<Instant> getServerBoostingSinceTimestamp(User user) {
        return getRealMemberById(user.getId())
                .flatMap(Member::getServerBoostingSinceTimestamp);
    }

    @Override
    public Optional<Instant> getTimeout(User user) {
        return getRealMemberById(user.getId())
                .flatMap(Member::getTimeout);
    }

    @Override
    public Optional<String> getUserServerAvatarHash(User user) {
        return getRealMemberById(user.getId())
                .flatMap(Member::getServerAvatarHash);
    }

    @Override
    public Optional<Icon> getUserServerAvatar(User user) {
        return getRealMemberById(user.getId())
                .flatMap(Member::getServerAvatar);
    }

    @Override
    public Optional<Icon> getUserServerAvatar(User user, int size) {
        return getRealMemberById(user.getId())
                .flatMap(member -> member.getServerAvatar(size));
    }

    @Override
    public boolean isPending(long userId) {
        return getRealMemberById(userId)
                .map(Member::isPending)
                .orElse(false);
    }

    @Override
    public boolean isSelfMuted(long userId) {
        return getRealMemberById(userId)
                .map(Member::isSelfMuted)
                .orElse(false);
    }

    @Override
    public boolean isSelfDeafened(long userId) {
        return getRealMemberById(userId)
                .map(Member::isSelfDeafened)
                .orElse(false);
    }

    @Override
    public boolean isMuted(long userId) {
        return getRealMemberById(userId)
                .map(Member::isMuted)
                .orElse(false);
    }

    @Override
    public boolean isDeafened(long userId) {
        return getRealMemberById(userId)
                .map(Member::isDeafened)
                .orElse(false);
    }

    @Override
    public Optional<Instant> getJoinedAtTimestamp(User user) {
        return getRealMemberById(user.getId())
                .map(Member::getJoinedAtTimestamp);
    }

    @Override
    public boolean isLarge() {
        return large;
    }

    @Override
    public int getMemberCount() {
        return memberCount.get();
    }

    @Override
    public Optional<User> getOwner() {
        return api.getCachedUserById(ownerId);
    }

    @Override
    public long getOwnerId() {
        return ownerId;
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
            return Optional.of(new IconImpl(
                    getApi(),
                    new URL("https://" + Javacord.DISCORD_CDN_DOMAIN
                            + "/icons/" + getIdAsString() + "/" + iconHash + ".png")));
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
            return Optional.of(new IconImpl(
                    getApi(),
                    new URL("https://" + Javacord.DISCORD_CDN_DOMAIN
                            + "/splashes/" + getIdAsString() + "/" + splash + ".png")));
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
    public CompletableFuture<Integer> getPruneCount(int days) {
        return new RestRequest<Integer>(getApi(), RestMethod.GET, RestEndpoint.SERVER_PRUNE)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("days", String.valueOf(days))
                .execute(result -> result.getJsonBody().get("pruned").asInt());
    }

    @Override
    public CompletableFuture<Integer> pruneMembers(int days, String reason) {
        return new RestRequest<Integer>(getApi(), RestMethod.POST, RestEndpoint.SERVER_PRUNE)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("days", String.valueOf(days))
                .setAuditLogReason(reason)
                .execute(result -> result.getJsonBody().get("pruned").asInt());
    }

    @Override
    public CompletableFuture<Set<RichInvite>> getInvites() {
        return new RestRequest<Set<RichInvite>>(getApi(), RestMethod.GET, RestEndpoint.SERVER_INVITE)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Set<RichInvite> invites = new HashSet<>();
                    for (JsonNode inviteJson : result.getJsonBody()) {
                        invites.add(new InviteImpl(getApi(), inviteJson));
                    }
                    return Collections.unmodifiableSet(invites);
                });
    }

    /**
     * Add a scheduled event to the server.
     *
     * @param scheduledEvent The scheduled event to add.
     */
    public void addScheduledEvent(final ServerScheduledEvent scheduledEvent) {
        scheduledEvents.put(scheduledEvent.getId(), scheduledEvent);
    }

    /**
     * Remove a scheduled event from the server.
     *
     * @param scheduledEventId The id of the scheduled event to remove.
     */
    public void removeScheduledEvent(final long scheduledEventId) {
        scheduledEvents.remove(scheduledEventId);
    }

    @Override
    public Set<ServerScheduledEvent> getScheduledEvents() {
        //TODO: Throw a warning if the intent is not set
        return Collections.unmodifiableSet(new HashSet<>(scheduledEvents.values()));
    }

    @Override
    public Optional<ServerScheduledEvent> getScheduledEventById(final long eventId) {
        //TODO: Throw a warning if the intent is not set
        return Optional.ofNullable(scheduledEvents.getOrDefault(eventId, null));
    }

    @Override
    public CompletableFuture<ServerScheduledEvent> requestScheduledEventById(final long eventId,
                                                                             final boolean includeUserCount) {
        return new RestRequest<ServerScheduledEvent>(getApi(), RestMethod.GET, RestEndpoint.SCHEDULED_EVENTS)
                .setUrlParameters(getIdAsString())
                .setUrlParameters(String.valueOf(eventId))
                .addQueryParameter("with_user_count", String.valueOf(includeUserCount))
                .execute(result -> new ServerScheduledEventImpl(api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<Set<ServerScheduledEvent>> requestScheduledEvents(boolean includeUserCount) {
        return new RestRequest<Set<ServerScheduledEvent>>(getApi(), RestMethod.GET, RestEndpoint.SCHEDULED_EVENTS)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("with_user_count", String.valueOf(includeUserCount))
                .execute(result -> {
                    Set<ServerScheduledEvent> events = new HashSet<>();
                    for (JsonNode eventJson : result.getJsonBody()) {
                        events.add(new ServerScheduledEventImpl(api, eventJson));
                    }
                    return Collections.unmodifiableSet(events);
                });
    }

    @Override
    public boolean hasAllMembersInCache() {
        return getRealMembers().size() >= getMemberCount();
    }

    @Override
    public void requestMembersChunks() {
        api.getWebSocketAdapter().queueRequestGuildMembers(this);
    }

    @Override
    public Set<User> getMembers() {
        return api.getEntityCache().get().getMemberCache()
                .getMembersByServer(getId())
                .stream()
                .map(Member::getUser)
                .collect(Collectors.toSet());
    }

    /**
     * Gets the real member objects of the server.
     *
     * @return The real members.
     */
    public Set<Member> getRealMembers() {
        return api.getEntityCache().get().getMemberCache()
                .getMembersByServer(getId());
    }

    @Override
    public Optional<User> getMemberById(long id) {
        return api.getEntityCache().get().getMemberCache()
                .getMemberByIdAndServer(id, getId())
                .map(Member::getUser);
    }

    /**
     * Gets the real member object for the user with the given id.
     *
     * @param userId The id of the user.
     * @return The real member.
     */
    public Optional<Member> getRealMemberById(long userId) {
        return api.getEntityCache().get().getMemberCache()
                .getMemberByIdAndServer(userId, getId());
    }

    @Override
    public boolean isMember(User user) {
        return api.getEntityCache().get().getMemberCache()
                .getMemberByIdAndServer(user.getId(), getId())
                .isPresent();
    }

    @Override
    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles.values().stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    @Override
    public List<Role> getRoles(User user) {
        return ((UserImpl) user).getMember()
                .filter(member -> member.getServer().equals(this))
                .map(Member::getRoles)
                .orElseGet(() ->
                        getRealMemberById(user.getId())
                                .map(Member::getRoles).orElseGet(Collections::emptyList));
    }

    @Override
    public boolean isWidgetEnabled() {
        return widgetEnabled;
    }

    @Override
    public Optional<Long> getWidgetChannelId() {
        return Optional.ofNullable(widgetChannelId);
    }

    @Override
    public Optional<Integer> getMaxPresences() {
        return Optional.ofNullable(maxPresences);
    }

    @Override
    public Optional<Integer> getMaxMembers() {
        return Optional.ofNullable(maxMembers);
    }

    @Override
    public Optional<Integer> getMaxVideoChannelUsers() {
        return Optional.ofNullable(maxVideoChannelUsers);
    }

    @Override
    public Optional<WelcomeScreen> getWelcomeScreen() {
        return Optional.ofNullable(welcomeScreen);
    }

    @Override
    public boolean isPremiumProgressBarEnabled() {
        return premiumProgressBarEnabled;
    }

    @Override
    public Optional<Role> getRoleById(long id) {
        return Optional.ofNullable(roles.get(id));
    }


    @Override
    public CompletableFuture<Void> delete(String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER)
                .setUrlParameters(getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> leave() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_SELF)
                .setUrlParameters(getIdAsString())
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> addRoleToUser(User user, Role role, String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.PUT, RestEndpoint.SERVER_MEMBER_ROLE)
                .setUrlParameters(getIdAsString(), user.getIdAsString(), role.getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> removeRoleFromUser(User user, Role role, String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_MEMBER_ROLE)
                .setUrlParameters(getIdAsString(), user.getIdAsString(), role.getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> reorderRoles(List<Role> roles, String reason) {
        roles = new ArrayList<>(roles); // Copy the list to safely modify it
        ArrayNode body = JsonNodeFactory.instance.arrayNode();
        roles.removeIf(Role::isEveryoneRole);
        for (int i = 0; i < roles.size(); i++) {
            body.addObject()
                    .put("id", roles.get(i).getIdAsString())
                    .put("position", i + 1);
        }
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.ROLE)
                .setUrlParameters(getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public void selfMute() {
        api.getWebSocketAdapter().sendVoiceStateUpdate(
                this, getConnectedVoiceChannel(api.getYourself()).orElse(null), true, null);
    }

    @Override
    public void selfUnmute() {
        api.getWebSocketAdapter().sendVoiceStateUpdate(
                this, getConnectedVoiceChannel(api.getYourself()).orElse(null), false, null);
    }

    @Override
    public void selfDeafen() {
        api.getWebSocketAdapter().sendVoiceStateUpdate(
                this, getConnectedVoiceChannel(api.getYourself()).orElse(null), null, true);
    }

    @Override
    public void selfUndeafen() {
        api.getWebSocketAdapter().sendVoiceStateUpdate(
                this, getConnectedVoiceChannel(api.getYourself()).orElse(null), null, false);
    }

    @Override
    public CompletableFuture<User> requestMember(long userId) {
        return new RestRequest<User>(getApi(), RestMethod.GET, RestEndpoint.SERVER_MEMBER)
                .setUrlParameters(getIdAsString(), Long.toUnsignedString(userId))
                .execute(result -> new MemberImpl(api, this, result.getJsonBody(), null).getUser());
    }

    @Override
    public CompletableFuture<Void> kickUser(User user, String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_MEMBER)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> banUser(String userId, long deleteMessageDuration, TimeUnit unit, String reason) {
        RestRequest<Void> request = new RestRequest<Void>(getApi(), RestMethod.PUT, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), userId);
        if (deleteMessageDuration > 0) {
            request.setBody(JsonNodeFactory.instance.objectNode()
                    .put("delete_message_seconds", unit.toSeconds(deleteMessageDuration)));
        }

        if (reason != null) {
            request.setAuditLogReason(reason);
        }

        return request.execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> unbanUser(long userId, String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), Long.toUnsignedString(userId))
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Ban> requestBan(long userId) {
        return new RestRequest<Ban>(getApi(), RestMethod.GET, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), Long.toUnsignedString(userId))
                .execute(result -> new BanImpl(this, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<Set<Ban>> getBans(Integer limit, Long after) {
        RestRequest<Set<Ban>> request = new RestRequest<Set<Ban>>(
                getApi(),
                RestMethod.GET,
                RestEndpoint.BAN
        ).setUrlParameters(getIdAsString());

        if (limit != null) {
            request.addQueryParameter("limit", String.valueOf(limit));
        }

        if (after != null) {
            request.addQueryParameter("after", String.valueOf(after));
        }

        return request.execute(result -> {
            Set<Ban> bans = new HashSet<>();
            for (JsonNode ban : result.getJsonBody()) {
                bans.add(new BanImpl(this, ban));
            }
            return Collections.unmodifiableSet(bans);
        });
    }

    @Override
    public CompletableFuture<Set<Ban>> getBans() {
        CompletableFuture<Set<Ban>> future = new CompletableFuture<>();
        ArrayList<Ban> bans = new ArrayList<>();

        fetchBansPageAndAddAllToCollection(null, bans, future);

        return future;
    }

    private void fetchBansPageAndAddAllToCollection(Long after,
                                                    ArrayList<Ban> banList,
                                                    CompletableFuture<Set<Ban>> futureToComplete) {
        this.getBans(1000, after).thenAccept((page) -> {
            banList.addAll(page);

            // If the response was smaller than 1000 entries, this was the last page.
            // Let's pass the full list to the future!
            if (page.size() < 1000) {
                futureToComplete.complete(Collections.unmodifiableSet(new HashSet<>(banList)));
                return;
            }

            // The response contained 1000 bans. There could be more, so let's request the next page
            fetchBansPageAndAddAllToCollection(
                    banList.get(banList.size() - 1).getUser().getId(),
                    banList,
                    futureToComplete
            );
        }).exceptionally(t -> {
            futureToComplete.completeExceptionally(t);
            return null;
        });
    }

    @Override
    public CompletableFuture<List<Webhook>> getWebhooks() {
        return new RestRequest<List<Webhook>>(getApi(), RestMethod.GET, RestEndpoint.SERVER_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    List<Webhook> webhooks = new ArrayList<>();
                    for (JsonNode webhookJson : result.getJsonBody()) {
                        webhooks.add(WebhookImpl.createWebhook(getApi(), webhookJson));
                    }
                    return Collections.unmodifiableList(webhooks);
                });
    }

    @Override
    public CompletableFuture<List<Webhook>> getAllIncomingWebhooks() {
        return new RestRequest<List<Webhook>>(getApi(), RestMethod.GET, RestEndpoint.SERVER_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result -> WebhookImpl.createAllIncomingWebhooksFromJsonArray(getApi(), result.getJsonBody()));
    }

    @Override
    public CompletableFuture<List<IncomingWebhook>> getIncomingWebhooks() {
        return new RestRequest<List<IncomingWebhook>>(getApi(), RestMethod.GET, RestEndpoint.SERVER_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result ->
                        IncomingWebhookImpl.createIncomingWebhooksFromJsonArray(getApi(), result.getJsonBody()));
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLog(int limit) {
        return getAuditLogBefore(limit, null, null);
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLog(int limit, AuditLogActionType type) {
        return getAuditLogBefore(limit, null, type);
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLogBefore(int limit, AuditLogEntry before) {
        return getAuditLogBefore(limit, before, null);
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLogBefore(int limit, AuditLogEntry before, AuditLogActionType type) {
        CompletableFuture<AuditLog> future = new CompletableFuture<>();
        api.getThreadPool().getExecutorService().submit(() -> {
            try {
                AuditLogImpl auditLog = new AuditLogImpl(this);
                boolean requestMore = true;
                while (requestMore) {
                    int requestAmount = limit - auditLog.getEntries().size();
                    requestAmount = Math.min(requestAmount, 100);
                    RestRequest<JsonNode> request =
                            new RestRequest<JsonNode>(getApi(), RestMethod.GET, RestEndpoint.AUDIT_LOG)
                                    .setUrlParameters(getIdAsString())
                                    .addQueryParameter("limit", String.valueOf(requestAmount));
                    List<AuditLogEntry> lastAuditLogEntries = auditLog.getEntries();

                    if (!lastAuditLogEntries.isEmpty()) {
                        // It's not the first request, so append a "before"
                        request.addQueryParameter(
                                "before", lastAuditLogEntries.get(lastAuditLogEntries.size() - 1).getIdAsString());
                    } else if (before != null) {
                        // It's the first request, and we have a non-null "before" parameter
                        request.addQueryParameter("before", before.getIdAsString());
                    }

                    if (type != null) {
                        request.addQueryParameter("action_type", String.valueOf(type.getValue()));
                    }

                    JsonNode data = request.execute(RestRequestResult::getJsonBody).join();
                    // Add the new entries
                    auditLog.addEntries(data);
                    // Check if we have to make another request
                    requestMore = auditLog.getEntries().size() < limit
                            && data.get("audit_log_entries").size() >= requestAmount;
                }
                future.complete(auditLog);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    @Override
    public Set<KnownCustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableSet(customEmojis);
    }

    @Override
    public CompletableFuture<Set<SlashCommand>> getSlashCommands() {
        return api.getServerSlashCommands(this);
    }

    @Override
    public CompletableFuture<SlashCommand> getSlashCommandById(long commandId) {
        return api.getServerSlashCommandById(this, commandId);
    }

    @Override
    public List<ServerChannel> getChannels() {
        final List<ServerChannel> channels = getUnorderedChannels().stream()
                .filter(channel -> channel.asCategorizable()
                        .map(categorizable -> !categorizable.getCategory().isPresent())
                        .orElse(false))
                .map(Channel::asRegularServerChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator
                        .<RegularServerChannel>comparingInt(channel -> channel.getType().getId())
                        .thenComparing(RegularServerChannelImpl.COMPARE_BY_RAW_POSITION))
                .collect(Collectors.toList());
        getChannelCategories().forEach(category -> {
            channels.add(category);
            channels.addAll(category.getChannels());
        });

        final Map<RegularServerChannel, List<ServerThreadChannel>> regularServerChannelThreads = new HashMap<>();
        getThreadChannels().forEach(serverThreadChannel -> {
            final RegularServerChannel regularServerChannel = serverThreadChannel.getParent();
            regularServerChannelThreads.merge(regularServerChannel,
                    new ArrayList<>(Collections.singletonList(serverThreadChannel)),
                    (serverThreadChannels, serverThreadChannels2) -> {
                        serverThreadChannels.addAll(serverThreadChannels2);
                        return new ArrayList<>(serverThreadChannels);
                    });
        });
        regularServerChannelThreads.forEach(
                (serverTextChannel, serverThreadChannels) -> channels.addAll(channels.indexOf(serverTextChannel) + 1,
                        serverThreadChannels));

        return Collections.unmodifiableList(channels);
    }

    @Override
    public List<RegularServerChannel> getRegularChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(RegularServerChannel.class::isInstance)
                .map(Channel::asRegularServerChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(RegularServerChannelImpl.COMPARE_BY_RAW_POSITION)
                .collect(Collectors.toList()));
    }

    @Override
    public List<TextableRegularServerChannel> getTextableRegularChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(TextableRegularServerChannel.class::isInstance)
                .map(Channel::asTextableRegularServerChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(RegularServerChannelImpl.COMPARE_BY_RAW_POSITION)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ChannelCategory> getChannelCategories() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(ChannelCategory.class::isInstance)
                .map(Channel::asChannelCategory)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(RegularServerChannelImpl.COMPARE_BY_RAW_POSITION)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ServerTextChannel> getTextChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(ServerTextChannel.class::isInstance)
                .map(Channel::asServerTextChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(RegularServerChannelImpl.COMPARE_BY_RAW_POSITION)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ServerForumChannel> getForumChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(ServerForumChannel.class::isInstance)
                .map(Channel::asServerForumChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(RegularServerChannelImpl.COMPARE_BY_RAW_POSITION)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ServerVoiceChannel> getVoiceChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(ServerVoiceChannel.class::isInstance)
                .map(Channel::asServerVoiceChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(RegularServerChannelImpl.COMPARE_BY_RAW_POSITION)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ServerStageVoiceChannel> getStageVoiceChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(ServerStageVoiceChannel.class::isInstance)
                .map(Channel::asServerStageVoiceChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(RegularServerChannelImpl.COMPARE_BY_RAW_POSITION)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ServerThreadChannel> getThreadChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(ServerThreadChannel.class::isInstance)
                .map(Channel::asServerThreadChannel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(channel -> channel.getMetadata().getArchiveTimestamp()))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<ServerChannel> getChannelById(long id) {
        return api.getEntityCache().get().getChannelCache().getChannelById(id)
                .filter(ServerChannel.class::isInstance)
                .map(ServerChannel.class::cast);
    }

    @Override
    public Optional<RegularServerChannel> getRegularChannelById(long id) {
        return api.getEntityCache().get().getChannelCache().getChannelById(id)
                .filter(RegularServerChannel.class::isInstance)
                .map(RegularServerChannel.class::cast);
    }

    @Override
    public Optional<TextableRegularServerChannel> getTextableRegularChannelById(long id) {
        return api.getEntityCache().get().getChannelCache().getChannelById(id)
                .filter(TextableRegularServerChannel.class::isInstance)
                .map(TextableRegularServerChannel.class::cast);
    }

    @Override
    public CompletableFuture<Void> joinServerThreadChannel(long channelId) {
        return new RestRequest<Void>(getApi(), RestMethod.PUT, RestEndpoint.JOIN_LEAVE_THREAD)
                .setUrlParameters(String.valueOf(channelId))
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> leaveServerThreadChannel(long channelId) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.JOIN_LEAVE_THREAD)
                .setUrlParameters(String.valueOf(channelId))
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<ActiveThreads> getActiveThreads() {
        return new RestRequest<ActiveThreads>(getApi(), RestMethod.GET, RestEndpoint.LIST_ACTIVE_THREADS)
                .setUrlParameters(getIdAsString())
                .execute(result -> new ActiveThreadsImpl((DiscordApiImpl) getApi(), this,
                        result.getJsonBody()));
    }

    @Override
    public Set<Sticker> getStickers() {
        return Collections.unmodifiableSet(new HashSet<>(stickers.values()));
    }

    @Override
    public CompletableFuture<Set<Sticker>> requestStickers() {
        return new RestRequest<Set<Sticker>>(api, RestMethod.GET, RestEndpoint.SERVER_STICKER)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Set<Sticker> stickers = new HashSet<>();
                    for (JsonNode stickerJson : result.getJsonBody()) {
                        stickers.add(new StickerImpl(api, stickerJson));
                    }

                    return stickers;
                });
    }

    @Override
    public CompletableFuture<Sticker> requestStickerById(long id) {
        return new RestRequest<Sticker>(api, RestMethod.GET, RestEndpoint.SERVER_STICKER)
                .setUrlParameters(getIdAsString())
                .execute(result -> new StickerImpl(api, result.getJsonBody()));
    }

    /**
     * Adds a sticker to the server's cache.
     *
     * @param sticker The sticker to add to the server's cache.
     */
    public void addSticker(Sticker sticker) {
        stickers.put(sticker.getId(), sticker);
    }

    /**
     * Removes a sticker from the server's cache.
     *
     * @param sticker The sticker to remove from the server's cache.
     */
    public void removeSticker(Sticker sticker) {
        stickers.remove(sticker.getId());
    }

    @Override
    public void cleanup() {
        getUnorderedChannels().stream()
                .map(ServerChannel::getId)
                .forEach(api::removeChannelFromCache);

        getMembers().forEach(user -> removeMember(user.getId()));
        stickers.values().forEach(api::removeSticker);
        getAudioConnection().ifPresent(this::removeAudioConnection);
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

    @Override
    public EnumSet<SystemChannelFlag> getSystemChannelFlags() {
        return EnumSet.copyOf(systemChannelFlags);
    }
}
