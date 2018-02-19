package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.channels.impl.ImplGroupChannel;
import de.btobastian.javacord.entities.channels.impl.ImplPrivateChannel;
import de.btobastian.javacord.entities.channels.impl.ImplServerVoiceChannel;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.events.server.channel.ServerVoiceChannelMemberJoinEvent;
import de.btobastian.javacord.events.server.channel.ServerVoiceChannelMemberLeaveEvent;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelMemberJoinListener;
import de.btobastian.javacord.listeners.server.channel.ServerVoiceChannelMemberLeaveListener;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the voice state update packet.
 */
public class VoiceStateUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public VoiceStateUpdateHandler(DiscordApi api) {
        super(api, true, "VOICE_STATE_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        if (!packet.hasNonNull("user_id")) {
            return;
        }

        api.getCachedUserById(packet.get("user_id").asLong())
                .map(ImplUser.class::cast)
                .ifPresent(user -> {
                    if (packet.hasNonNull("guild_id")) {
                        handleServerVoiceChannel(packet, user);
                    } else if (packet.hasNonNull("channel_id")) {
                        long channelId = packet.get("channel_id").asLong();
                        api.getVoiceChannelById(channelId).ifPresent(voiceChannel -> {
                            if (voiceChannel instanceof PrivateChannel) {
                                handlePrivateChannel(user, ((ImplPrivateChannel) voiceChannel));
                            } else if (voiceChannel instanceof GroupChannel) {
                                handleGroupChannel(user, ((ImplGroupChannel) voiceChannel));
                            }
                        });
                    }
                });
    }

    private void handleServerVoiceChannel(JsonNode packet, ImplUser user) {
        Optional<Server> optionalServer = api.getServerById(packet.get("guild_id").asLong());
        Optional<ImplServerVoiceChannel> oldChannel = optionalServer
                .flatMap(server -> server.getConnectedVoiceChannel(user))
                .map(ImplServerVoiceChannel.class::cast);

        optionalServer.ifPresent(server -> {
            Optional<ImplServerVoiceChannel> newChannel;
            if (packet.hasNonNull("channel_id")) {
                newChannel = server
                        .getVoiceChannelById(packet.get("channel_id").asLong())
                        .map(ImplServerVoiceChannel.class::cast);

                if (newChannel.equals(oldChannel)) {
                    return;
                }
            } else {
                newChannel = Optional.empty();
            }

            oldChannel.ifPresent(channel -> {
                user.removeConnectedVoiceChannel(channel);
                channel.removeConnectedUser(user);
                dispatchServerVoiceChannelMemberLeaveEvent(user, newChannel.orElse(null), channel, server);
            });

            newChannel.ifPresent(channel -> {
                channel.addConnectedUser(user);
                user.addConnectedVoiceChannel(channel);
                dispatchServerVoiceChannelMemberJoinEvent(user, channel, oldChannel.orElse(null), server);
            });
        });
    }

    private void handlePrivateChannel(ImplUser user, ImplPrivateChannel channel) {
        //channel.addConnectedUser(user);
        //user.addConnectedChannel(channel);
        //dispatchVoiceChannelMemberJoinEvent(user, channel);
    }

    private void handleGroupChannel(ImplUser user, ImplGroupChannel channel) {
        //channel.addConnectedUser(user);
        //user.addConnectedChannel(channel);
        //dispatchVoiceChannelMemberJoinEvent(user, channel);
    }

    private void dispatchServerVoiceChannelMemberJoinEvent(
            User user, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel, Server server) {
        ServerVoiceChannelMemberJoinEvent event = new ServerVoiceChannelMemberJoinEvent(user, newChannel, oldChannel);

        List<ServerVoiceChannelMemberJoinListener> listeners = new ArrayList<>();
        listeners.addAll(user.getServerVoiceChannelMemberJoinListeners());
        listeners.addAll(newChannel.getServerVoiceChannelMemberJoinListeners());
        if (server != null) {
            listeners.addAll(server.getServerVoiceChannelMemberJoinListeners());
        }
        listeners.addAll(api.getServerVoiceChannelMemberJoinListeners());

        dispatchEvent(listeners, listener -> listener.onServerVoiceChannelMemberJoin(event));
    }

    private void dispatchServerVoiceChannelMemberLeaveEvent(
            User user, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel, Server server) {
        ServerVoiceChannelMemberLeaveEvent event = new ServerVoiceChannelMemberLeaveEvent(user, newChannel, oldChannel);

        List<ServerVoiceChannelMemberLeaveListener> listeners = new ArrayList<>();
        listeners.addAll(user.getServerVoiceChannelMemberLeaveListeners());
        listeners.addAll(oldChannel.getServerVoiceChannelMemberLeaveListeners());
        if (server != null) {
            listeners.addAll(server.getServerVoiceChannelMemberLeaveListeners());
        }
        listeners.addAll(api.getServerVoiceChannelMemberLeaveListeners());

        dispatchEvent(listeners, listener -> listener.onServerVoiceChannelMemberLeave(event));
    }

}
