package de.btobastian.javacord.utils.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.channels.impl.ImplGroupChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.events.group.channel.GroupChannelCreateEvent;
import de.btobastian.javacord.events.server.channel.ServerChannelCreateEvent;
import de.btobastian.javacord.events.user.channel.PrivateChannelCreateEvent;
import de.btobastian.javacord.listeners.group.channel.GroupChannelCreateListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelCreateListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelCreateListener;
import de.btobastian.javacord.utils.PacketHandler;

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
        }
    }

    /**
     * Handles channel category creation.
     *
     * @param channel The channel data.
     */
    private void handleChannelCategory(JsonNode channel) {
        long serverId = channel.get("guild_id").asLong();
        api.getServerById(serverId).ifPresent(server -> {
            ChannelCategory channelCategory = ((ImplServer) server).getOrCreateChannelCategory(channel);
            ServerChannelCreateEvent event = new ServerChannelCreateEvent(channelCategory);

            List<ServerChannelCreateListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerChannelCreateListeners());
            listeners.addAll(api.getServerChannelCreateListeners());

            dispatchEvent(listeners, listener -> listener.onServerChannelCreate(event));
        });
    }

    /**
     * Handles server text channel creation.
     *
     * @param channel The channel data.
     */
    private void handleServerTextChannel(JsonNode channel) {
        long serverId = channel.get("guild_id").asLong();
        api.getServerById(serverId).ifPresent(server -> {
            ServerTextChannel textChannel = ((ImplServer) server).getOrCreateServerTextChannel(channel);
            ServerChannelCreateEvent event = new ServerChannelCreateEvent(textChannel);

            List<ServerChannelCreateListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerChannelCreateListeners());
            listeners.addAll(api.getServerChannelCreateListeners());

            dispatchEvent(listeners, listener -> listener.onServerChannelCreate(event));
        });
    }

    /**
     * Handles server voice channel creation.
     *
     * @param channel The channel data.
     */
    private void handleServerVoiceChannel(JsonNode channel) {
        long serverId = channel.get("guild_id").asLong();
        api.getServerById(serverId).ifPresent(server -> {
            ServerVoiceChannel voiceChannel = ((ImplServer) server).getOrCreateServerVoiceChannel(channel);
            ServerChannelCreateEvent event = new ServerChannelCreateEvent(voiceChannel);

            List<ServerChannelCreateListener> listeners = new ArrayList<>();
            listeners.addAll(server.getServerChannelCreateListeners());
            listeners.addAll(api.getServerChannelCreateListeners());

            dispatchEvent(listeners, listener -> listener.onServerChannelCreate(event));
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
        ImplUser recipient = (ImplUser) api.getOrCreateUser(channel.get("recipients").get(0));
        if (!recipient.getPrivateChannel().isPresent()) {
            PrivateChannel privateChannel = recipient.getOrCreateChannel(channel);
            PrivateChannelCreateEvent event = new PrivateChannelCreateEvent(privateChannel);

            List<PrivateChannelCreateListener> listeners = new ArrayList<>();
            listeners.addAll(recipient.getPrivateChannelCreateListeners());
            listeners.addAll(api.getPrivateChannelCreateListeners());

            dispatchEvent(listeners, listener -> listener.onPrivateChannelCreate(event));
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
            GroupChannel groupChannel = new ImplGroupChannel(api, channel);
            GroupChannelCreateEvent event = new GroupChannelCreateEvent(groupChannel);

            List<GroupChannelCreateListener> listeners = new ArrayList<>();
            groupChannel.getMembers().stream()
                    .map(User::getGroupChannelCreateListeners)
                    .forEach(listeners::addAll);
            listeners.addAll(api.getGroupChannelCreateListeners());

            dispatchEvent(listeners, listener -> listener.onGroupChannelCreate(event));
        }
    }

}