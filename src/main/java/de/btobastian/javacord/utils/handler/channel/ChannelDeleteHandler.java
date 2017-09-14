package de.btobastian.javacord.utils.handler.channel;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.channel.ServerChannelDeleteEvent;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.utils.PacketHandler;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the channel create packet.
 */
public class ChannelDeleteHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ChannelDeleteHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelDeleteHandler(DiscordApi api) {
        super(api, true, "CHANNEL_DELETE");
    }

    @Override
    public void handle(JSONObject packet) {
        int type = packet.getInt("type");
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
        }
    }

    /**
     * Handles server text channel deletion.
     *
     * @param channel The channel data.
     */
    private void handleServerTextChannel(JSONObject channel) {
        long serverId = Long.parseLong(channel.getString("guild_id"));
        api.getServerById(serverId).ifPresent(server -> server.getTextChannelById(serverId).ifPresent(textChannel -> {
            ServerChannelDeleteEvent event = new ServerChannelDeleteEvent(api, server, textChannel);
            listenerExecutorService.submit(() -> {
                List<ServerChannelDeleteListener> listeners = new ArrayList<>();
                listeners.addAll(textChannel.getServerChannelDeleteListeners());
                listeners.addAll(server.getServerChannelDeleteListeners());
                listeners.addAll(api.getServerChannelDeleteListeners());
                listeners.forEach(listener -> listener.onServerChannelDelete(event));
            });
            ((ImplServer) server).removeChannelFromCache(textChannel.getId());
        }));
    }

    /**
     * Handles server voice channel deletion.
     *
     * @param channel The channel data.
     */
    private void handleServerVoiceChannel(JSONObject channel) {
        long serverId = Long.parseLong(channel.getString("guild_id"));
        api.getServerById(serverId).ifPresent(server -> server.getVoiceChannelById(serverId).ifPresent(voiceChannel -> {
            ServerChannelDeleteEvent event = new ServerChannelDeleteEvent(api, server, voiceChannel);
            listenerExecutorService.submit(() -> {
                List<ServerChannelDeleteListener> listeners = new ArrayList<>();
                listeners.addAll(voiceChannel.getServerChannelDeleteListeners());
                listeners.addAll(server.getServerChannelDeleteListeners());
                listeners.addAll(api.getServerChannelDeleteListeners());
                listeners.forEach(listener -> listener.onServerChannelDelete(event));
            });
            ((ImplServer) server).removeChannelFromCache(voiceChannel.getId());
        }));
    }

    /**
     * Handles a private channel deletion.
     *
     * @param channel The channel data.
     */
    private void handlePrivateChannel(JSONObject channel) {
        // TODO handle private channel deletion -> only for client bots
    }

}