package org.javacord.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.channel.Categorizable;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.channel.impl.ImplChannelCategory;
import org.javacord.entity.channel.impl.ImplGroupChannel;
import org.javacord.entity.channel.impl.ImplServerTextChannel;
import org.javacord.entity.channel.impl.ImplServerVoiceChannel;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.impl.ImplPermissions;
import org.javacord.entity.user.User;
import org.javacord.event.channel.group.GroupChannelChangeNameEvent;
import org.javacord.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.event.channel.server.ServerChannelChangeNsfwFlagEvent;
import org.javacord.event.channel.server.ServerChannelChangeOverwrittenPermissionsEvent;
import org.javacord.event.channel.server.ServerChannelChangePositionEvent;
import org.javacord.event.channel.server.text.ServerTextChannelChangeTopicEvent;
import org.javacord.event.channel.server.voice.ServerVoiceChannelChangeBitrateEvent;
import org.javacord.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;
import org.javacord.listener.channel.group.GroupChannelChangeNameListener;
import org.javacord.listener.channel.server.ServerChannelChangeNameListener;
import org.javacord.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.listener.channel.server.ServerChannelChangePositionListener;
import org.javacord.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;
import org.javacord.util.gateway.PacketHandler;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.channel.Categorizable;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.channel.impl.ImplChannelCategory;
import org.javacord.entity.channel.impl.ImplGroupChannel;
import org.javacord.entity.channel.impl.ImplServerTextChannel;
import org.javacord.entity.channel.impl.ImplServerVoiceChannel;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.impl.ImplPermissions;
import org.javacord.entity.user.User;
import org.javacord.event.channel.group.GroupChannelChangeNameEvent;
import org.javacord.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.event.channel.server.ServerChannelChangeNsfwFlagEvent;
import org.javacord.event.channel.server.ServerChannelChangeOverwrittenPermissionsEvent;
import org.javacord.event.channel.server.ServerChannelChangePositionEvent;
import org.javacord.event.channel.server.text.ServerTextChannelChangeTopicEvent;
import org.javacord.event.channel.server.voice.ServerVoiceChannelChangeBitrateEvent;
import org.javacord.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the channel update packet.
 */
public class ChannelUpdateHandler extends PacketHandler {

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
        int type = packet.get("type").asInt();
        switch (type) {
            case 0:
                handleServerChannel(packet);
                handleServerTextChannel(packet);
                break;
            case 1:
                handlePrivateChannel(packet);
                break;
            case 2:
                handleServerChannel(packet);
                handleServerVoiceChannel(packet);
                break;
            case 3:
                handleGroupChannel(packet);
                break;
            case 4:
                handleServerChannel(packet);
                handleChannelCategory(packet);
                break;
        }
    }

    /**
     * Handles a server channel update.
     *
     * @param jsonChannel The channel data.
     */
    private void handleServerChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        api.getServerChannelById(channelId).ifPresent(channel -> {
            String oldName = channel.getName();
            String newName = jsonChannel.get("name").asText();
            if (!Objects.deepEquals(oldName, newName)) {
                channel.asChannelCategory().ifPresent(cc -> ((ImplChannelCategory) cc).setName(newName));
                channel.asServerTextChannel().ifPresent(stc -> ((ImplServerTextChannel) stc).setName(newName));
                channel.asServerVoiceChannel().ifPresent(svc -> ((ImplServerVoiceChannel) svc).setName(newName));
                ServerChannelChangeNameEvent event =
                        new ServerChannelChangeNameEvent(channel, newName, oldName);

                List<ServerChannelChangeNameListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getServerChannelChangeNameListeners());
                listeners.addAll(channel.getServer().getServerChannelChangeNameListeners());
                listeners.addAll(api.getServerChannelChangeNameListeners());

                api.getEventDispatcher().dispatchEvent(
                        channel.getServer(), listeners, listener -> listener.onServerChannelChangeName(event));
            }

            ChannelCategory oldCategory = channel.asCategorizable().flatMap(Categorizable::getCategory).orElse(null);
            ChannelCategory newCategory = jsonChannel.hasNonNull("parent_id") ?
                    channel.getServer().getChannelCategoryById(jsonChannel.get("parent_id").asLong(-1)).orElse(null) :
                    null;
            int oldRawPosition = channel.getRawPosition();
            int newRawPosition = jsonChannel.get("position").asInt();
            if (oldRawPosition != newRawPosition || !Objects.deepEquals(oldCategory, newCategory)) {
                int oldPosition = channel.getPosition();
                if (channel instanceof ImplServerTextChannel) {
                    ((ImplServerTextChannel) channel).setParentId(newCategory == null ? -1 : newCategory.getId());
                } else if (channel instanceof ImplServerVoiceChannel) {
                    ((ImplServerVoiceChannel) channel).setParentId(newCategory == null ? -1 : newCategory.getId());
                }
                channel.asChannelCategory().ifPresent(cc -> ((ImplChannelCategory) cc).setPosition(newRawPosition));
                channel.asServerTextChannel().ifPresent(stc -> ((ImplServerTextChannel) stc).setPosition(newRawPosition));
                channel.asServerVoiceChannel().ifPresent(svc -> ((ImplServerVoiceChannel) svc).setPosition(newRawPosition));

                int newPosition = channel.getPosition();

                ServerChannelChangePositionEvent event = new ServerChannelChangePositionEvent(channel, newPosition,
                        oldPosition, newRawPosition, oldRawPosition, newCategory, oldCategory);

                List<ServerChannelChangePositionListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getServerChannelChangePositionListeners());
                listeners.addAll(channel.getServer().getServerChannelChangePositionListeners());
                listeners.addAll(api.getServerChannelChangePositionListeners());

                api.getEventDispatcher().dispatchEvent(
                        channel.getServer(), listeners, listener -> listener.onServerChannelChangePosition(event));
            }

            Collection<Long> rolesWithOverwrittenPermissions = new HashSet<>();
            Collection<Long> usersWithOverwrittenPermissions = new HashSet<>();
            if (jsonChannel.has("permission_overwrites") && !jsonChannel.get("permission_overwrites").isNull()) {
                for (JsonNode permissionOverwriteJson : jsonChannel.get("permission_overwrites")) {
                    Permissions oldOverwrittenPermissions = null;
                    DiscordEntity entity = null;
                    ConcurrentHashMap<Long, Permissions> overwrittenPermissions = null;
                    switch (permissionOverwriteJson.get("type").asText()) {
                        case "role":
                            entity = api.getRoleById(permissionOverwriteJson.get("id").asText()).orElseThrow(() ->
                                    new IllegalStateException("Received channel update event with unknown role!"));
                            oldOverwrittenPermissions = channel.getOverwrittenPermissions((Role) entity);
                            if (channel instanceof ImplChannelCategory) {
                                overwrittenPermissions = ((ImplChannelCategory) channel).getOverwrittenRolePermissions();
                            } else if (channel instanceof ImplServerTextChannel) {
                                overwrittenPermissions = ((ImplServerTextChannel) channel).getOverwrittenRolePermissions();
                            } else if (channel instanceof ImplServerVoiceChannel) {
                                overwrittenPermissions = ((ImplServerVoiceChannel) channel).getOverwrittenRolePermissions();
                            }
                            rolesWithOverwrittenPermissions.add(entity.getId());
                            break;
                        case "member":
                            entity = api.getCachedUserById(permissionOverwriteJson.get("id").asText()).orElseThrow(() ->
                                    new IllegalStateException("Received channel update event with unknown user!"));
                            oldOverwrittenPermissions = channel.getOverwrittenPermissions((User) entity);
                            if (channel instanceof ImplChannelCategory) {
                                overwrittenPermissions = ((ImplChannelCategory) channel).getOverwrittenUserPermissions();
                            } else if (channel instanceof ImplServerTextChannel) {
                                overwrittenPermissions = ((ImplServerTextChannel) channel).getOverwrittenUserPermissions();
                            } else if (channel instanceof ImplServerVoiceChannel) {
                                overwrittenPermissions = ((ImplServerVoiceChannel) channel).getOverwrittenUserPermissions();
                            }
                            usersWithOverwrittenPermissions.add(entity.getId());
                            break;
                        default:
                            throw new IllegalStateException(
                                    "Permission overwrite object with unknown type: " + permissionOverwriteJson.toString());
                    }
                    int allow = permissionOverwriteJson.get("allow").asInt(0);
                    int deny = permissionOverwriteJson.get("deny").asInt(0);
                    Permissions newOverwrittenPermissions = new ImplPermissions(allow, deny);
                    if (!newOverwrittenPermissions.equals(oldOverwrittenPermissions)) {
                        overwrittenPermissions.put(entity.getId(), newOverwrittenPermissions);
                        dispatchServerChannelChangeOverwrittenPermissionsEvent(
                                channel, newOverwrittenPermissions, oldOverwrittenPermissions, entity);
                    }
                }
            }
            ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions = null;
            ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions = null;
            if (channel instanceof ImplChannelCategory) {
                overwrittenRolePermissions = ((ImplChannelCategory) channel).getOverwrittenRolePermissions();
                overwrittenUserPermissions = ((ImplChannelCategory) channel).getOverwrittenUserPermissions();
            } else if (channel instanceof ImplServerTextChannel) {
                overwrittenRolePermissions = ((ImplServerTextChannel) channel).getOverwrittenRolePermissions();
                overwrittenUserPermissions = ((ImplServerTextChannel) channel).getOverwrittenUserPermissions();
            } else if (channel instanceof ImplServerVoiceChannel) {
                overwrittenRolePermissions = ((ImplServerVoiceChannel) channel).getOverwrittenRolePermissions();
                overwrittenUserPermissions = ((ImplServerVoiceChannel) channel).getOverwrittenUserPermissions();
            }

            Iterator<Map.Entry<Long, Permissions>> userIt = overwrittenUserPermissions.entrySet().iterator();
            while (userIt.hasNext()) {
                Map.Entry<Long, Permissions> entry = userIt.next();
                if (usersWithOverwrittenPermissions.contains(entry.getKey())) {
                    continue;
                }
                api.getCachedUserById(entry.getKey()).ifPresent(user -> {
                    Permissions oldPermissions = entry.getValue();
                    userIt.remove();
                    dispatchServerChannelChangeOverwrittenPermissionsEvent(
                            channel, ImplPermissions.EMPTY_PERMISSIONS, oldPermissions, user);
                });
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
                    dispatchServerChannelChangeOverwrittenPermissionsEvent(
                            channel, ImplPermissions.EMPTY_PERMISSIONS, oldPermissions, role);
                });
            }
        });
    }

    /**
     * Handles a channel category update.
     *
     * @param jsonChannel The channel data.
     */
    private void handleChannelCategory(JsonNode jsonChannel) {
        long channelCategoryId = jsonChannel.get("id").asLong();
        api.getChannelCategoryById(channelCategoryId).map(ImplChannelCategory.class::cast).ifPresent(channel -> {
            String oldName = channel.getName();
            String newName = jsonChannel.get("name").asText();
            if (!Objects.deepEquals(oldName, newName)) {
                channel.setName(newName);
                ServerChannelChangeNameEvent event =
                        new ServerChannelChangeNameEvent(channel, newName, oldName);

                List<ServerChannelChangeNameListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getServerChannelChangeNameListeners());
                listeners.addAll(channel.getServer().getServerChannelChangeNameListeners());
                listeners.addAll(api.getServerChannelChangeNameListeners());

                api.getEventDispatcher().dispatchEvent(
                        channel.getServer(), listeners, listener -> listener.onServerChannelChangeName(event));
            }

            boolean oldNsfwFlaf = channel.isNsfw();
            boolean newNsfwFlag = jsonChannel.get("nsfw").asBoolean();
            if (oldNsfwFlaf != newNsfwFlag) {
                channel.setNsfwFlag(newNsfwFlag);
                ServerChannelChangeNsfwFlagEvent event =
                        new ServerChannelChangeNsfwFlagEvent(channel, newNsfwFlag, oldNsfwFlaf);

                List<ServerChannelChangeNsfwFlagListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getServerChannelChangeNsfwFlagListeners());
                listeners.addAll(channel.getServer().getServerChannelChangeNsfwFlagListeners());
                listeners.addAll(api.getServerChannelChangeNsfwFlagListeners());

                api.getEventDispatcher().dispatchEvent(
                        channel.getServer(), listeners, listener -> listener.onServerChannelChangeNsfwFlag(event));
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
        api.getTextChannelById(channelId).map(c -> ((ImplServerTextChannel) c)).ifPresent(channel -> {
            String oldTopic = channel.getTopic();
            String newTopic = jsonChannel.has("topic") && !jsonChannel.get("topic").isNull()
                    ? jsonChannel.get("topic").asText() : "";
            if (!oldTopic.equals(newTopic)) {
                channel.setTopic(newTopic);

                ServerTextChannelChangeTopicEvent event =
                        new ServerTextChannelChangeTopicEvent(channel, newTopic, oldTopic);

                List<ServerTextChannelChangeTopicListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getServerTextChannelChangeTopicListeners());
                listeners.addAll(channel.getServer().getServerTextChannelChangeTopicListeners());
                listeners.addAll(api.getServerTextChannelChangeTopicListeners());

                api.getEventDispatcher().dispatchEvent(
                        channel.getServer(), listeners, listener -> listener.onServerTextChannelChangeTopic(event));
            }

            boolean oldNsfwFlaf = channel.isNsfw();
            boolean newNsfwFlag = jsonChannel.get("nsfw").asBoolean();
            if (oldNsfwFlaf != newNsfwFlag) {
                channel.setNsfwFlag(newNsfwFlag);
                ServerChannelChangeNsfwFlagEvent event =
                        new ServerChannelChangeNsfwFlagEvent(channel, newNsfwFlag, oldNsfwFlaf);

                List<ServerChannelChangeNsfwFlagListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getServerChannelChangeNsfwFlagListeners());
                listeners.addAll(channel.getServer().getServerChannelChangeNsfwFlagListeners());
                listeners.addAll(api.getServerChannelChangeNsfwFlagListeners());

                api.getEventDispatcher().dispatchEvent(
                        channel.getServer(), listeners, listener -> listener.onServerChannelChangeNsfwFlag(event));
            }
        });
    }

    /**
     * Handles a server voice channel update.
     *
     * @param jsonChannel The channel data.
     */
    private void handleServerVoiceChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        api.getServerVoiceChannelById(channelId).map(ImplServerVoiceChannel.class::cast).ifPresent(channel -> {
            int oldBitrate = channel.getBitrate();
            int newBitrate = jsonChannel.get("bitrate").asInt();
            if (oldBitrate != newBitrate) {
                channel.setBitrate(newBitrate);
                ServerVoiceChannelChangeBitrateEvent event =
                        new ServerVoiceChannelChangeBitrateEvent(channel, newBitrate, oldBitrate);

                List<ServerVoiceChannelChangeBitrateListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getServerVoiceChannelChangeBitrateListeners());
                listeners.addAll(channel.getServer().getServerVoiceChannelChangeBitrateListeners());
                listeners.addAll(api.getServerVoiceChannelChangeBitrateListeners());

                api.getEventDispatcher().dispatchEvent(
                        channel.getServer(), listeners, listener -> listener.onServerVoiceChannelChangeBitrate(event));
            }

            int oldUserLimit = channel.getUserLimit().orElse(0);
            int newUserLimit = jsonChannel.get("user_limit").asInt();
            if (oldUserLimit != newUserLimit) {
                channel.setUserLimit(newUserLimit);
                ServerVoiceChannelChangeUserLimitEvent event =
                        new ServerVoiceChannelChangeUserLimitEvent(channel, newUserLimit, oldUserLimit);

                List<ServerVoiceChannelChangeUserLimitListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getServerVoiceChannelChangeUserLimitListeners());
                listeners.addAll(channel.getServer().getServerVoiceChannelChangeUserLimitListeners());
                listeners.addAll(api.getServerVoiceChannelChangeUserLimitListeners());

                api.getEventDispatcher().dispatchEvent(channel.getServer(),
                        listeners, listener -> listener.onServerVoiceChannelChangeUserLimit(event));
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
     * Handles a group channel update.
     *
     * @param jsonChannel The channel data.
     */
    private void handleGroupChannel(JsonNode jsonChannel) {
        long channelId = jsonChannel.get("id").asLong();
        api.getGroupChannelById(channelId).map(ImplGroupChannel.class::cast).ifPresent(channel -> {
            String oldName = channel.getName().orElseThrow(AssertionError::new);
            String newName = jsonChannel.get("name").asText();
            if (!Objects.equals(oldName, newName)) {
                channel.setName(newName);

                GroupChannelChangeNameEvent event =
                        new GroupChannelChangeNameEvent(channel, newName, oldName);

                List<GroupChannelChangeNameListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getGroupChannelChangeNameListeners());
                channel.getMembers().stream()
                        .map(User::getGroupChannelChangeNameListeners)
                        .forEach(listeners::addAll);
                listeners.addAll(api.getGroupChannelChangeNameListeners());

                api.getEventDispatcher()
                        .dispatchEvent(api, listeners, listener -> listener.onGroupChannelChangeName(event));
            }
        });
    }

    /**
     * Dispatches a ServerChannelChangeOverwrittenPermissionsEvent.
     *
     * @param channel The channel of the event.
     * @param newPermissions The new overwritten permissions.
     * @param oldPermissions The old overwritten permissions.
     * @param entity The entity of the event.
     */
    private void dispatchServerChannelChangeOverwrittenPermissionsEvent(
            ServerChannel channel, Permissions newPermissions, Permissions oldPermissions, DiscordEntity entity) {
        if (newPermissions.equals(oldPermissions)) {
            // This can be caused by adding a user/role in a channels overwritten permissions without modifying
            // any of its values. We don't need to dispatch an event for this.
            return;
        }
        ServerChannelChangeOverwrittenPermissionsEvent event =
                new ServerChannelChangeOverwrittenPermissionsEvent(
                        channel, newPermissions, oldPermissions, entity);

        List<ServerChannelChangeOverwrittenPermissionsListener> listeners = new ArrayList<>();
        if (entity instanceof User) {
            listeners.addAll(((User) entity).getServerChannelChangeOverwrittenPermissionsListeners());
        }
        if (entity instanceof Role) {
            listeners.addAll(((Role) entity).getServerChannelChangeOverwrittenPermissionsListeners());
        }
        listeners.addAll(channel.getServerChannelChangeOverwrittenPermissionsListeners());
        listeners.addAll(channel.getServer().getServerChannelChangeOverwrittenPermissionsListeners());
        listeners.addAll(api.getServerChannelChangeOverwrittenPermissionsListeners());

        api.getEventDispatcher().dispatchEvent(channel.getServer(),
                listeners, listener -> listener.onServerChannelChangeOverwrittenPermissions(event));
    }

}