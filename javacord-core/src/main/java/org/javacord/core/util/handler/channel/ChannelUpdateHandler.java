package org.javacord.core.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.Categorizable;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelFlag;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.channel.forum.DefaultReaction;
import org.javacord.api.entity.channel.forum.ForumLayoutType;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.entity.channel.forum.SortOrderType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.api.event.channel.server.ServerChannelChangeNsfwFlagEvent;
import org.javacord.api.event.channel.server.ServerChannelChangeOverwrittenPermissionsEvent;
import org.javacord.api.event.channel.server.ServerChannelChangePositionEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultReactionEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultSortTypeEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultThreadRateLimitPerUserEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeFlagsEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeForumLayoutEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeForumTagsEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeLastMessageIdEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeRateLimitPerUserEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeTopicEvent;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeVersionEvent;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeDefaultAutoArchiveDurationEvent;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeSlowmodeEvent;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeTopicEvent;
import org.javacord.api.event.channel.server.voice.ServerStageVoiceChannelChangeTopicEvent;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeBitrateEvent;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeNsfwEvent;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;
import org.javacord.core.entity.channel.ChannelCategoryImpl;
import org.javacord.core.entity.channel.RegularServerChannelImpl;
import org.javacord.core.entity.channel.ServerChannelImpl;
import org.javacord.core.entity.channel.ServerForumChannelImpl;
import org.javacord.core.entity.channel.ServerStageVoiceChannelImpl;
import org.javacord.core.entity.channel.ServerTextChannelImpl;
import org.javacord.core.entity.channel.ServerVoiceChannelImpl;
import org.javacord.core.entity.channel.forum.DefaultReactionImpl;
import org.javacord.core.entity.channel.forum.ForumTagImpl;
import org.javacord.core.entity.permission.PermissionsImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.server.ServerChannelChangeNameEventImpl;
import org.javacord.core.event.channel.server.ServerChannelChangeNsfwFlagEventImpl;
import org.javacord.core.event.channel.server.ServerChannelChangeOverwrittenPermissionsEventImpl;
import org.javacord.core.event.channel.server.ServerChannelChangePositionEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeDefaultReactionEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeDefaultSortTypeEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeDefaultThreadRateLimitPerUserEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeFlagsEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeForumLayoutEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeForumTagsEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeLastMessageIdEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeRateLimitPerUserEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeTopicEventImpl;
import org.javacord.core.event.channel.server.forum.ServerForumChannelChangeVersionEventImpl;
import org.javacord.core.event.channel.server.text.ServerTextChannelChangeDefaultAutoArchiveDurationEventImpl;
import org.javacord.core.event.channel.server.text.ServerTextChannelChangeSlowmodeEventImpl;
import org.javacord.core.event.channel.server.text.ServerTextChannelChangeTopicEventImpl;
import org.javacord.core.event.channel.server.voice.ServerStageVoiceChannelChangeTopicEventImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelChangeBitrateEventImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelChangeNsfwEventImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEventImpl;
import org.javacord.core.util.cache.MessageCacheImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles the channel update packet.
 */
public class ChannelUpdateHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(ChannelUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelUpdateHandler(DiscordApi api) {
        super(api, true, "CHANNEL_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        ChannelType type = ChannelType.fromId(packet.get("type").asInt());
        switch (type) {
            case SERVER_TEXT_CHANNEL:
                handleServerChannel(packet);
                handleRegularServerChannel(packet);
                handleServerTextChannel(packet);
                break;
            case SERVER_VOICE_CHANNEL:
                handleServerChannel(packet);
                handleRegularServerChannel(packet);
                handleServerVoiceChannel(packet);
                break;
            case SERVER_STAGE_VOICE_CHANNEL:
                handleServerChannel(packet);
                handleRegularServerChannel(packet);
                handleServerVoiceChannel(packet);
                handleServerStageVoiceChannel(packet);
                break;
            case SERVER_FORUM_CHANNEL:
                handleServerChannel(packet);
                handleRegularServerChannel(packet);
                handleServerForumChannel(packet);
                break;
            case SERVER_NEWS_CHANNEL:
                logger.debug("Received CHANNEL_UPDATE packet for a news channel. In this Javacord version it is "
                        + "treated as a normal text channel!");
                handleServerChannel(packet);
                handleRegularServerChannel(packet);
                handleServerTextChannel(packet);
                break;
            case SERVER_STORE_CHANNEL:
                // TODO Handle store channels
                logger.debug("Received CHANNEL_UPDATE packet for a store channel. These are not supported in this"
                        + " Javacord version and get ignored!");
                break;
            case PRIVATE_CHANNEL:
                handlePrivateChannel(packet);
                break;
            case GROUP_CHANNEL:
                logger.info("Received CHANNEL_UPDATE packet for a group channel. This should be impossible.");
                break;
            case CHANNEL_CATEGORY:
                handleServerChannel(packet);
                handleRegularServerChannel(packet);
                handleChannelCategory(packet);
                break;
            default: {
                try {
                    handleServerChannel(packet);
                    if (packet.has("position")) {
                        handleRegularServerChannel(packet);
                    }
                } catch (Exception exception) {
                    logger.warn("An error occurred when trying to update a fallback channel implementation", exception);
                }
            }
        }
    }

    /**
     * Handles a server channel update.
     *
     * @param jsonChannel The channel data.
     */
    private void handleServerChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        long guildId = jsonChannel.get("guild_id").asLong();
        ServerImpl server = api.getPossiblyUnreadyServerById(guildId).map(ServerImpl.class::cast).orElse(null);
        if (server == null) {
            return;
        }
        ServerChannelImpl channel = server.getChannelById(channelId).map(ServerChannelImpl.class::cast).orElse(null);
        if (channel == null) {
            return;
        }
        String oldName = channel.getName();
        String newName = jsonChannel.get("name").asText();
        if (!Objects.deepEquals(oldName, newName)) {
            channel.setName(newName);
            ServerChannelChangeNameEvent event =
                    new ServerChannelChangeNameEventImpl(channel, newName, oldName);

            if (server.isReady()) {
                api.getEventDispatcher().dispatchServerChannelChangeNameEvent(
                        (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
            }
        }
    }

    private void handleRegularServerChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        Optional<RegularServerChannel> optionalChannel = api.getRegularServerChannelById(channelId);
        if (!optionalChannel.isPresent()) {
            LoggerUtil.logMissingChannel(logger, channelId);
            return;
        }

        RegularServerChannelImpl channel = (RegularServerChannelImpl) optionalChannel.get();
        ServerImpl server = (ServerImpl) channel.getServer();

        final AtomicBoolean areYouAffected = new AtomicBoolean(false);
        ChannelCategory oldCategory = channel.asCategorizable().flatMap(Categorizable::getCategory).orElse(null);
        ChannelCategory newCategory = jsonChannel.hasNonNull("parent_id")
                ? channel.getServer().getChannelCategoryById(jsonChannel.get("parent_id").asLong(-1)).orElse(null)
                : null;


        final RegularServerChannelImpl regularServerChannel = (RegularServerChannelImpl) channel;
        final int oldRawPosition = regularServerChannel.getRawPosition();
        final int newRawPosition = jsonChannel.get("position").asInt();

        if (oldRawPosition != newRawPosition || !Objects.deepEquals(oldCategory, newCategory)) {
            final int oldPosition = regularServerChannel.getPosition();
            if (regularServerChannel instanceof ServerTextChannelImpl) {
                ((ServerTextChannelImpl) regularServerChannel).setParentId(
                        newCategory == null ? -1 : newCategory.getId());
            } else if (regularServerChannel instanceof ServerVoiceChannelImpl) {
                ((ServerVoiceChannelImpl) regularServerChannel).setParentId(
                        newCategory == null ? -1 : newCategory.getId());
            } else if (regularServerChannel instanceof ServerForumChannelImpl) {
                ((ServerForumChannelImpl) regularServerChannel).setParentId(
                        newCategory == null ? -1 : newCategory.getId());
            }
            regularServerChannel.setRawPosition(newRawPosition);

            final int newPosition = regularServerChannel.getPosition();

            final ServerChannelChangePositionEvent event = new ServerChannelChangePositionEventImpl(
                    regularServerChannel, newPosition, oldPosition, newRawPosition, oldRawPosition,
                    newCategory, oldCategory);

            if (server.isReady()) {
                api.getEventDispatcher().dispatchServerChannelChangePositionEvent(
                        (DispatchQueueSelector) regularServerChannel.getServer(),
                        regularServerChannel.getServer(), regularServerChannel, event);
            }
        }


        Collection<Long> rolesWithOverwrittenPermissions = new HashSet<>();
        Collection<Long> usersWithOverwrittenPermissions = new HashSet<>();
        if (jsonChannel.has("permission_overwrites") && !jsonChannel.get("permission_overwrites").isNull()) {
            for (JsonNode permissionOverwriteJson : jsonChannel.get("permission_overwrites")) {
                Permissions oldOverwrittenPermissions;
                ConcurrentHashMap<Long, Permissions> overwrittenPermissions;
                long entityId = permissionOverwriteJson.get("id").asLong();
                Optional<DiscordEntity> entity;
                switch (permissionOverwriteJson.get("type").asInt()) {
                    case 0:
                        //Skip role if not present. See: https://github.com/discord/discord-api-docs/issues/4303
                        Optional<Role> optionalRole = server.getRoleById(entityId);
                        if (optionalRole.isPresent()) {
                            Role role = optionalRole.get();
                            entity = Optional.of(role);
                            oldOverwrittenPermissions = regularServerChannel.getOverwrittenPermissions(role);
                            overwrittenPermissions = regularServerChannel.getInternalOverwrittenRolePermissions();
                            rolesWithOverwrittenPermissions.add(entityId);
                        } else {
                            continue;
                        }
                        break;
                    case 1:
                        oldOverwrittenPermissions = regularServerChannel.getOverwrittenUserPermissions()
                                .getOrDefault(entityId, PermissionsImpl.EMPTY_PERMISSIONS);
                        entity = api.getCachedUserById(entityId).map(DiscordEntity.class::cast);
                        overwrittenPermissions = regularServerChannel.getInternalOverwrittenUserPermissions();
                        usersWithOverwrittenPermissions.add(entityId);
                        break;
                    default:
                        throw new IllegalStateException("Permission overwrite object with unknown type: "
                                + permissionOverwriteJson);
                }
                long allow = permissionOverwriteJson.get("allow").asLong(0);
                long deny = permissionOverwriteJson.get("deny").asLong(0);
                Permissions newOverwrittenPermissions = new PermissionsImpl(allow, deny);
                if (!newOverwrittenPermissions.equals(oldOverwrittenPermissions)) {
                    overwrittenPermissions.put(entityId, newOverwrittenPermissions);
                    if (server.isReady()) {
                        dispatchServerChannelChangeOverwrittenPermissionsEvent(
                                channel, newOverwrittenPermissions, oldOverwrittenPermissions, entityId,
                                entity.orElse(null));
                        areYouAffected.compareAndSet(false, entityId == api.getYourself().getId());
                        entity.filter(e -> e instanceof Role)
                                .map(Role.class::cast)
                                .ifPresent(role -> areYouAffected
                                        .compareAndSet(false, role.getUsers().stream().anyMatch(User::isYourself)));
                    }
                }
            }
        }
        ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions;
        ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions;
        overwrittenRolePermissions = regularServerChannel.getInternalOverwrittenRolePermissions();
        overwrittenUserPermissions = regularServerChannel.getInternalOverwrittenUserPermissions();

        Iterator<Map.Entry<Long, Permissions>> userIt = overwrittenUserPermissions.entrySet().iterator();
        while (userIt.hasNext()) {
            Map.Entry<Long, Permissions> entry = userIt.next();
            if (usersWithOverwrittenPermissions.contains(entry.getKey())) {
                continue;
            }
            Permissions oldPermissions = entry.getValue();
            userIt.remove();
            if (server.isReady()) {
                dispatchServerChannelChangeOverwrittenPermissionsEvent(
                        channel, PermissionsImpl.EMPTY_PERMISSIONS, oldPermissions, entry.getKey(),
                        api.getCachedUserById(entry.getKey()).orElse(null));
                areYouAffected.compareAndSet(false, entry.getKey() == api.getYourself().getId());
            }
        }

        Iterator<Map.Entry<Long, Permissions>> roleIt = overwrittenRolePermissions.entrySet().iterator();
        while (roleIt.hasNext()) {
            Map.Entry<Long, Permissions> entry = roleIt.next();
            if (rolesWithOverwrittenPermissions.contains(entry.getKey())) {
                continue;
            }
            api.getRoleById(entry.getKey()).ifPresent(role -> {
                Permissions oldPermissions = entry.getValue();
                roleIt.remove();
                if (server.isReady()) {
                    dispatchServerChannelChangeOverwrittenPermissionsEvent(
                            channel, PermissionsImpl.EMPTY_PERMISSIONS, oldPermissions, role.getId(), role);
                    areYouAffected.compareAndSet(false, role.getUsers().stream().anyMatch(User::isYourself));
                }
            });
        }

        if (areYouAffected.get() && !channel.canYouSee()) {
            api.forEachCachedMessageWhere(
                    msg -> msg.getChannel().getId() == channelId,
                    msg -> {
                        api.removeMessageFromCache(msg.getId());
                        ((MessageCacheImpl) ((TextChannel) channel).getMessageCache()).removeMessage(msg);
                    }
            );
        }
    }

    /**
     * Handles a channel category update.
     *
     * @param jsonChannel The channel data.
     */
    private void handleChannelCategory(JsonNode jsonChannel) {
        long channelCategoryId = jsonChannel.get("id").asLong();
        api.getChannelCategoryById(channelCategoryId).map(ChannelCategoryImpl.class::cast).ifPresent(channel -> {
            boolean oldNsfwFlag = channel.isNsfw();
            boolean newNsfwFlag = jsonChannel.path("nsfw").asBoolean(false);
            if (oldNsfwFlag != newNsfwFlag) {
                channel.setNsfwFlag(newNsfwFlag);
                ServerChannelChangeNsfwFlagEvent event =
                        new ServerChannelChangeNsfwFlagEventImpl(channel, newNsfwFlag, oldNsfwFlag);

                api.getEventDispatcher().dispatchServerChannelChangeNsfwFlagEvent(
                        (DispatchQueueSelector) channel.getServer(), channel, channel.getServer(), null, null,
                        event);
            }
        });
    }

    /**
     * Handles a server text channel update.
     *
     * @param jsonChannel The json channel data.
     */
    private void handleServerTextChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        Optional<ServerTextChannel> optionalChannel = api.getServerTextChannelById(channelId);
        if (!optionalChannel.isPresent()) {
            LoggerUtil.logMissingChannel(logger, channelId);
            return;
        }

        ServerTextChannelImpl channel = (ServerTextChannelImpl) optionalChannel.get();

        String oldTopic = channel.getTopic();
        String newTopic = jsonChannel.has("topic") && !jsonChannel.get("topic").isNull()
                ? jsonChannel.get("topic").asText() : "";
        if (!oldTopic.equals(newTopic)) {
            channel.setTopic(newTopic);

            ServerTextChannelChangeTopicEvent event =
                    new ServerTextChannelChangeTopicEventImpl(channel, newTopic, oldTopic);

            api.getEventDispatcher().dispatchServerTextChannelChangeTopicEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        boolean oldNsfwFlag = channel.isNsfw();
        boolean newNsfwFlag = jsonChannel.get("nsfw").asBoolean();
        if (oldNsfwFlag != newNsfwFlag) {
            channel.setNsfw(newNsfwFlag);
            ServerChannelChangeNsfwFlagEvent event =
                    new ServerChannelChangeNsfwFlagEventImpl(channel, newNsfwFlag, oldNsfwFlag);

            api.getEventDispatcher().dispatchServerChannelChangeNsfwFlagEvent(
                    (DispatchQueueSelector) channel.getServer(), null, channel.getServer(), null, channel,
                    event);
        }

        int oldSlowmodeDelay = channel.getSlowmodeDelayInSeconds();
        //Check if "rate_limit_per_user" exists as a temporary fix until SERVER_NEWS_CHANNEL is handled separately.
        int newSlowmodeDelay = jsonChannel.has("rate_limit_per_user")
                ? jsonChannel.get("rate_limit_per_user").asInt(0) : 0;
        if (oldSlowmodeDelay != newSlowmodeDelay) {
            channel.setSlowmodeDelayInSeconds(newSlowmodeDelay);
            ServerTextChannelChangeSlowmodeEvent event =
                    new ServerTextChannelChangeSlowmodeEventImpl(channel, oldSlowmodeDelay, newSlowmodeDelay);

            api.getEventDispatcher().dispatchServerTextChannelChangeSlowmodeEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event
            );
        }

        int oldDefaultAutoArchiveDuration = channel.getDefaultAutoArchiveDuration();
        int newDefaultAutoArchiveDuration = jsonChannel.has("default_auto_archive_duration")
                ? jsonChannel.get("default_auto_archive_duration").asInt()
                : 1440;
        if (oldDefaultAutoArchiveDuration != newDefaultAutoArchiveDuration) {
            channel.setDefaultAutoArchiveDuration(newDefaultAutoArchiveDuration);

            ServerTextChannelChangeDefaultAutoArchiveDurationEvent event =
                    new ServerTextChannelChangeDefaultAutoArchiveDurationEventImpl(channel,
                            oldDefaultAutoArchiveDuration, newDefaultAutoArchiveDuration);

            api.getEventDispatcher().dispatchServerTextChannelChangeDefaultAutoArchiveDurationEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event
            );

        }
    }

    /**
     * Handles a server forum channel update.
     *
     * @param jsonChannel The json channel data.
     */
    private void handleServerForumChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        Optional<ServerForumChannel> optionalChannel = api.getServerForumChannelById(channelId);
        if (!optionalChannel.isPresent()) {
            LoggerUtil.logMissingChannel(logger, channelId);
            return;
        }

        ServerForumChannelImpl channel = (ServerForumChannelImpl) optionalChannel.get();

        long oldVersion = channel.getVersion();
        long newVersion = jsonChannel.get("version").asLong();
        if (!Objects.equals(oldVersion, newVersion)) {
            channel.setVersion(newVersion);

            ServerForumChannelChangeVersionEvent event =
                    new ServerForumChannelChangeVersionEventImpl(channel, newVersion, oldVersion);

            api.getEventDispatcher().dispatchServerForumChannelChangeVersionEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }


        String oldTopic = channel.getTopic().orElse(null);
        String newTopic = jsonChannel.has("topic") && !jsonChannel.get("topic").isNull()
                ? jsonChannel.get("topic").asText() : null;
        if (!Objects.equals(oldTopic, newTopic)) {
            channel.setTopic(newTopic);

            ServerForumChannelChangeTopicEvent event =
                    new ServerForumChannelChangeTopicEventImpl(channel, newTopic, oldTopic);

            api.getEventDispatcher().dispatchServerForumChannelChangeTopicEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        int oldRateLimitPerUser = channel.getRateLimitPerUser();
        int newRateLimitPerUser = jsonChannel.has("rate_limit_per_user")
                ? jsonChannel.get("rate_limit_per_user").asInt(0) : 0;
        if (oldRateLimitPerUser != newRateLimitPerUser) {
            channel.setRateLimitPerUser(newRateLimitPerUser);

            ServerForumChannelChangeRateLimitPerUserEvent event =
                    new ServerForumChannelChangeRateLimitPerUserEventImpl(channel,
                            oldRateLimitPerUser, newRateLimitPerUser);

            api.getEventDispatcher().dispatchServerForumChannelChangeRateLimitPerUserEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        boolean oldNsfwFlag = channel.isNsfw();
        boolean newNsfwFlag = jsonChannel.get("nsfw").asBoolean();
        if (oldNsfwFlag != newNsfwFlag) {
            channel.setNsfw(newNsfwFlag);

            ServerChannelChangeNsfwFlagEvent event =
                    new ServerChannelChangeNsfwFlagEventImpl(channel, newNsfwFlag, oldNsfwFlag);

            api.getEventDispatcher().dispatchServerChannelChangeNsfwFlagEvent(
                    (DispatchQueueSelector) channel.getServer(), null, channel.getServer(), channel, null, event);
        }

        Long oldMessageId = channel.getLastMessageId().orElse(null);
        Long newMessageId = jsonChannel.has("last_message_id") && !jsonChannel.get("last_message_id").isNull()
                ? jsonChannel.get("last_message_id").asLong() : null;
        if (!Objects.equals(oldMessageId, newMessageId)) {
            channel.setLastMessageId(newMessageId);

            ServerForumChannelChangeLastMessageIdEvent event =
                    new ServerForumChannelChangeLastMessageIdEventImpl(channel, newMessageId, oldMessageId);

            api.getEventDispatcher().dispatchServerForumChannelChangeLastMessageIdEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        EnumSet<ChannelFlag> oldFlags = channel.getFlags();
        EnumSet<ChannelFlag> newFlags = EnumSet.noneOf(ChannelFlag.class);
        if (jsonChannel.hasNonNull("flags")) {
            for (JsonNode flag : jsonChannel.get("flags")) {
                newFlags.add(ChannelFlag.getByValue(flag.asInt()));
            }
        }

        if (!oldFlags.equals(newFlags)) {
            channel.setFlags(newFlags);
            ServerForumChannelChangeFlagsEvent event =
                    new ServerForumChannelChangeFlagsEventImpl(channel, newFlags, oldFlags);

            api.getEventDispatcher().dispatchServerForumChannelChangeFlagsEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        Set<ForumTag> oldAvailableTags = channel.getForumTags();
        Set<ForumTag> newAvailableTags = new HashSet<>();
        if (jsonChannel.hasNonNull("available_tags")) {
            for (JsonNode tag : jsonChannel.get("available_tags")) {
                newAvailableTags.add(new ForumTagImpl(api, tag));
            }
        }

        if (!oldAvailableTags.equals(newAvailableTags)) {
            channel.setAvailableTags(newAvailableTags);
            ServerForumChannelChangeForumTagsEvent event =
                    new ServerForumChannelChangeForumTagsEventImpl(channel, newAvailableTags, oldAvailableTags);

            api.getEventDispatcher().dispatchServerForumChannelChangeForumTagsEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        SortOrderType oldDefaultSortType = channel.getDefaultSortType().orElse(null);
        SortOrderType newDefaultSortType = jsonChannel.hasNonNull("default_sort_type")
                ? SortOrderType.getByValue(jsonChannel.get("default_sort_type").asInt())
                : null;
        if (!Objects.equals(oldDefaultSortType, newDefaultSortType)) {
            channel.setDefaultSortOrder(newDefaultSortType);
            ServerForumChannelChangeDefaultSortTypeEvent event =
                    new ServerForumChannelChangeDefaultSortTypeEventImpl(channel,
                            newDefaultSortType, oldDefaultSortType);

            api.getEventDispatcher().dispatchServerForumChannelChangeDefaultSortTypeEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        DefaultReaction oldDefaultReaction = !channel.getDefaultReaction().isPresent()
                ? null : channel.getDefaultReaction().get();
        DefaultReaction newDefaultReaction = jsonChannel.hasNonNull("default_reaction_emoji")
                ? new DefaultReactionImpl(jsonChannel.get("default_reaction_emoji"))
                : null;

        if (!Objects.equals(oldDefaultReaction, newDefaultReaction)) {
            channel.setDefaultReaction(newDefaultReaction);
            ServerForumChannelChangeDefaultReactionEvent event =
                    new ServerForumChannelChangeDefaultReactionEventImpl(channel, newDefaultReaction,
                            oldDefaultReaction);

            api.getEventDispatcher().dispatchServerForumChannelChangeDefaultReactionEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        int oldDefaultThreadRateLimitPerUser = channel.getDefaultThreadRateLimitPerUser();
        int newDefaultThreadRateLimitPerUser = jsonChannel.hasNonNull("default_auto_archive_duration")
                ? jsonChannel.get("default_auto_archive_duration").asInt()
                : 0;
        if (oldDefaultThreadRateLimitPerUser != newDefaultThreadRateLimitPerUser) {
            channel.setDefaultThreadRateLimitPerUser(newDefaultThreadRateLimitPerUser);
            ServerForumChannelChangeDefaultThreadRateLimitPerUserEvent event =
                    new ServerForumChannelChangeDefaultThreadRateLimitPerUserEventImpl(channel,
                            newDefaultThreadRateLimitPerUser, oldDefaultThreadRateLimitPerUser);

            api.getEventDispatcher().dispatchServerForumChannelChangeDefaultThreadRateLimitPerUserEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        ForumLayoutType oldForumLayoutType = channel.getForumLayoutType().orElse(null);
        ForumLayoutType newForumLayoutType = jsonChannel.hasNonNull("default_forum_layout")
                ? ForumLayoutType.getByValue(jsonChannel.get("default_forum_layout").asInt())
                : null;

        if (!Objects.equals(oldForumLayoutType, newForumLayoutType)) {
            channel.setForumLayoutType(newForumLayoutType);

            ServerForumChannelChangeForumLayoutEvent event =
                    new ServerForumChannelChangeForumLayoutEventImpl(channel, newForumLayoutType,
                            oldForumLayoutType);

            api.getEventDispatcher().dispatchServerForumChannelChangeForumLayoutEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }
    }

    /**
     * Handles a server voice channel update.
     *
     * @param jsonChannel The channel data.
     */
    private void handleServerVoiceChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        Optional<ServerVoiceChannel> optionalChannel = api.getServerVoiceChannelById(channelId);
        if (!optionalChannel.isPresent()) {
            LoggerUtil.logMissingChannel(logger, channelId);
            return;
        }

        ServerVoiceChannelImpl channel = (ServerVoiceChannelImpl) optionalChannel.get();

        int oldBitrate = channel.getBitrate();
        int newBitrate = jsonChannel.get("bitrate").asInt();
        if (oldBitrate != newBitrate) {
            channel.setBitrate(newBitrate);
            ServerVoiceChannelChangeBitrateEvent event =
                    new ServerVoiceChannelChangeBitrateEventImpl(channel, newBitrate, oldBitrate);

            api.getEventDispatcher().dispatchServerVoiceChannelChangeBitrateEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        int oldUserLimit = channel.getUserLimit().orElse(0);
        int newUserLimit = jsonChannel.get("user_limit").asInt();
        if (oldUserLimit != newUserLimit) {
            channel.setUserLimit(newUserLimit);
            ServerVoiceChannelChangeUserLimitEvent event =
                    new ServerVoiceChannelChangeUserLimitEventImpl(channel, newUserLimit, oldUserLimit);

            api.getEventDispatcher().dispatchServerVoiceChannelChangeUserLimitEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }

        boolean oldNsfwFlag = channel.isNsfw();
        boolean newNsfwFlag = jsonChannel.has("nsfw") && jsonChannel.get("nsfw").asBoolean();
        if (oldNsfwFlag != newNsfwFlag) {
            channel.setNsfw(newNsfwFlag);
            ServerVoiceChannelChangeNsfwEvent event =
                    new ServerVoiceChannelChangeNsfwEventImpl(channel, newNsfwFlag, oldNsfwFlag);
            api.getEventDispatcher().dispatchServerVoiceChannelChangeNsfwEvent(
                    (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event);
        }
    }

    /**
     * Handles a server stage voice channel update.
     *
     * @param jsonChannel The channel data.
     */
    private void handleServerStageVoiceChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        api.getServerStageVoiceChannelById(channelId)
                .map(ServerStageVoiceChannelImpl.class::cast).ifPresent(channel -> {
                    String oldTopic = channel.getTopic().orElse(null);
                    String newTopic = jsonChannel.hasNonNull("topic")
                            ? jsonChannel.get("topic").asText()
                            : null;
                    if (!Objects.equals(oldTopic, newTopic)) {
                        channel.setTopic(newTopic);
                        ServerStageVoiceChannelChangeTopicEvent event =
                                new ServerStageVoiceChannelChangeTopicEventImpl(channel, newTopic, oldTopic);
                        api.getEventDispatcher().dispatchServerStageVoiceChannelChangeTopicEvent(
                                (DispatchQueueSelector) channel.getServer(), channel.getServer(), channel, event
                        );
                    }
                });
    }

    /**
     * Handles a private channel update.
     *
     * @param channel The channel data.
     */
    private void handlePrivateChannel(JsonNode channel) {
    }

    /**
     * Dispatches a ServerChannelChangeOverwrittenPermissionsEvent.
     *
     * @param channel        The channel of the event.
     * @param newPermissions The new overwritten permissions.
     * @param oldPermissions The old overwritten permissions.
     * @param entityId       The id of the entity.
     * @param entity         The entity of the event.
     */
    private void dispatchServerChannelChangeOverwrittenPermissionsEvent(
            ServerChannel channel, Permissions newPermissions, Permissions oldPermissions,
            long entityId, DiscordEntity entity) {
        if (newPermissions.equals(oldPermissions)) {
            // This can be caused by adding a user/role in a channels overwritten permissions without modifying
            // any of its values. We don't need to dispatch an event for this.
            return;
        }
        ServerChannelChangeOverwrittenPermissionsEvent event =
                new ServerChannelChangeOverwrittenPermissionsEventImpl(
                        channel, newPermissions, oldPermissions, entityId, entity);

        api.getEventDispatcher().dispatchServerChannelChangeOverwrittenPermissionsEvent(
                (DispatchQueueSelector) channel.getServer(),
                (entity instanceof Role) ? (Role) entity : null,
                channel.getServer(),
                channel,
                (entity instanceof User) ? (User) entity : null,
                event);
    }

}
