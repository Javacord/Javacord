package de.btobastian.javacord.utils.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.channel.ServerChannelDeleteEvent;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the channel delete packet.
 */
public class ChannelDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelDeleteHandler(DiscordApi api) {
        super(api, true, "CHANNEL_DELETE");
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
            case 4:
                handleCategory(packet);
                break;
        }
    }

    /**
     * Handles server text channel deletion.
     *
     * @param channelJson The channel data.
     */
    private void handleCategory(JsonNode channelJson) {
        long serverId = channelJson.get("guild_id").asLong();
        long channelId = channelJson.get("id").asLong();
        api.getServerById(serverId).ifPresent(server -> server.getChannelCategoryById(channelId).ifPresent(channel -> {
            dispatchServerChannelDeleteEvent(channel);
            ((ImplServer) server).removeChannelFromCache(channel.getId());
        }));
    }

    /**
     * Handles server text channel deletion.
     *
     * @param channelJson The channel data.
     */
    private void handleServerTextChannel(JsonNode channelJson) {
        long serverId = channelJson.get("guild_id").asLong();
        long channelId = channelJson.get("id").asLong();
        api.getServerById(serverId).ifPresent(server -> server.getTextChannelById(channelId).ifPresent(channel -> {
            dispatchServerChannelDeleteEvent(channel);
            ((ImplServer) server).removeChannelFromCache(channel.getId());
        }));
    }

    /**
     * Handles server voice channel deletion.
     *
     * @param channelJson The channel data.
     */
    private void handleServerVoiceChannel(JsonNode channelJson) {
        long serverId = channelJson.get("guild_id").asLong();
        long channelId = channelJson.get("id").asLong();
        api.getServerById(serverId).ifPresent(server -> server.getVoiceChannelById(channelId).ifPresent(channel -> {
            dispatchServerChannelDeleteEvent(channel);
            ((ImplServer) server).removeChannelFromCache(channel.getId());
        }));
    }

    /**
     * Handles a private channel deletion.
     *
     * @param channel The channel data.
     */
    private void handlePrivateChannel(JsonNode channel) {
        // TODO handle private channel deletion -> only for client bots
    }

    /**
     * Dispatches a server channel delete event.
     *
     * @param channel The channel of the event.
     */
    private void dispatchServerChannelDeleteEvent(ServerChannel channel) {
        ServerChannelDeleteEvent event = new ServerChannelDeleteEvent(channel);

        List<ServerChannelDeleteListener> listeners = new ArrayList<>();
        listeners.addAll(channel.getServerChannelDeleteListeners());
        listeners.addAll(channel.getServer().getServerChannelDeleteListeners());
        listeners.addAll(api.getServerChannelDeleteListeners());

        dispatchEvent(listeners, listener -> listener.onServerChannelDelete(event));
    }

}