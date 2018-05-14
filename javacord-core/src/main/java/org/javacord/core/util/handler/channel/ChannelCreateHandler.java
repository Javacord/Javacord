package org.javacord.core.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.group.GroupChannelCreateEvent;
import org.javacord.api.event.channel.server.ServerChannelCreateEvent;
import org.javacord.api.event.channel.user.PrivateChannelCreateEvent;
import org.javacord.api.listener.channel.group.GroupChannelCreateListener;
import org.javacord.api.listener.channel.server.ServerChannelCreateListener;
import org.javacord.api.listener.channel.user.PrivateChannelCreateListener;
import org.javacord.core.entity.channel.GroupChannelImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.channel.group.GroupChannelCreateEventImpl;
import org.javacord.core.event.channel.server.ServerChannelCreateEventImpl;
import org.javacord.core.event.channel.user.PrivateChannelCreateEventImpl;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the channel create packet.
 */
public class ChannelCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelCreateHandler(DiscordApi api) {
        super(api, true, "CHANNEL_CREATE");
    }

    @Override
    public void handle(JsonNode packet) {
        int type = packet.get("type").asInt();
        switch (type) {
            case 0:
                handleServerTextChannel(packet);
                break;
            case 1:
                handlePrivateChannel(packet);
                break;
            case 2:
                handleServerVoiceChannel(packet);
                break;
            case 3:
                handleGroupChannel(packet);
                break;
            case 4:
                handleChannelCategory(packet);
                break;
            default:
                LoggerUtil.getLogger(ChannelCreateHandler.class).warn("Unexpected packet type. Your Javacord version"
                        + " might be out of date.");
        }
    }

    /**
     * Handles channel category creation.
     *
     * @param channel The channel data.
     */
    private void handleChannelCategory(JsonNode channel) {
        long serverId = channel.get("guild_id").asLong();
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ChannelCategory channelCategory = ((ServerImpl) server).getOrCreateChannelCategory(channel);
            ServerChannelCreateEvent event = new ServerChannelCreateEventImpl(channelCategory);

            List<ServerChannelCreateListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerChannelCreateListeners());
            listeners.addAll(api.getServerChannelCreateListeners());

            api.getEventDispatcher().dispatchEvent(server, listeners,
                    listener -> listener.onServerChannelCreate(event));
        });
    }

    /**
     * Handles server text channel creation.
     *
     * @param channel The channel data.
     */
    private void handleServerTextChannel(JsonNode channel) {
        long serverId = channel.get("guild_id").asLong();
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ServerTextChannel textChannel = ((ServerImpl) server).getOrCreateServerTextChannel(channel);
            ServerChannelCreateEvent event = new ServerChannelCreateEventImpl(textChannel);

            List<ServerChannelCreateListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerChannelCreateListeners());
            listeners.addAll(api.getServerChannelCreateListeners());

            api.getEventDispatcher().dispatchEvent(server, listeners,
                    listener -> listener.onServerChannelCreate(event));
        });
    }

    /**
     * Handles server voice channel creation.
     *
     * @param channel The channel data.
     */
    private void handleServerVoiceChannel(JsonNode channel) {
        long serverId = channel.get("guild_id").asLong();
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> {
            ServerVoiceChannel voiceChannel = ((ServerImpl) server).getOrCreateServerVoiceChannel(channel);
            ServerChannelCreateEvent event = new ServerChannelCreateEventImpl(voiceChannel);

            List<ServerChannelCreateListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerChannelCreateListeners());
            listeners.addAll(api.getServerChannelCreateListeners());

            api.getEventDispatcher().dispatchEvent(server, listeners,
                    listener -> listener.onServerChannelCreate(event));
        });
    }

    /**
     * Handles a private channel creation.
     *
     * @param channel The channel data.
     */
    private void handlePrivateChannel(JsonNode channel) {
        // A CHANNEL_CREATE packet is sent every time a bot account receives a message, see
        // https://github.com/hammerandchisel/discord-api-docs/issues/184
        UserImpl recipient = (UserImpl) api.getOrCreateUser(channel.get("recipients").get(0));
        if (!recipient.getPrivateChannel().isPresent()) {
            PrivateChannel privateChannel = recipient.getOrCreateChannel(channel);
            PrivateChannelCreateEvent event = new PrivateChannelCreateEventImpl(privateChannel);

            List<PrivateChannelCreateListener> listeners = new ArrayList<>();
            listeners.addAll(recipient.getPrivateChannelCreateListeners());
            listeners.addAll(api.getPrivateChannelCreateListeners());

            api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onPrivateChannelCreate(event));
        }
    }

    /**
     * Handles a group channel creation.
     *
     * @param channel The channel data.
     */
    private void handleGroupChannel(JsonNode channel) {
        long channelId = channel.get("id").asLong();
        if (!api.getGroupChannelById(channelId).isPresent()) {
            GroupChannel groupChannel = new GroupChannelImpl(api, channel);
            GroupChannelCreateEvent event = new GroupChannelCreateEventImpl(groupChannel);

            List<GroupChannelCreateListener> listeners = new ArrayList<>();
            groupChannel.getMembers().stream()
                    .map(User::getGroupChannelCreateListeners)
                    .forEach(listeners::addAll);
            listeners.addAll(api.getGroupChannelCreateListeners());

            api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onGroupChannelCreate(event));
        }
    }

}
