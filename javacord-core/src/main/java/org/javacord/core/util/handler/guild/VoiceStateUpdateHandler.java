package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberJoinEvent;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.core.entity.channel.GroupChannelImpl;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.channel.ServerVoiceChannelImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelMemberJoinEventImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelMemberLeaveEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
                .map(UserImpl.class::cast)
                .ifPresent(user -> {
                    if (packet.hasNonNull("guild_id")) {
                        handleServerVoiceChannel(packet, user);
                    } else if (packet.hasNonNull("channel_id")) {
                        long channelId = packet.get("channel_id").asLong();
                        api.getVoiceChannelById(channelId).ifPresent(voiceChannel -> {
                            if (voiceChannel instanceof PrivateChannel) {
                                handlePrivateChannel(user, ((PrivateChannelImpl) voiceChannel));
                            } else if (voiceChannel instanceof GroupChannel) {
                                handleGroupChannel(user, ((GroupChannelImpl) voiceChannel));
                            }
                        });
                    }
                });
    }

    private void handleServerVoiceChannel(JsonNode packet, UserImpl user) {
        Optional<Server> optionalServer = api.getServerById(packet.get("guild_id").asLong());
        Optional<ServerVoiceChannelImpl> oldChannel = optionalServer
                .flatMap(server -> server.getConnectedVoiceChannel(user))
                .map(ServerVoiceChannelImpl.class::cast);

        optionalServer.ifPresent(server -> {
            Optional<ServerVoiceChannelImpl> newChannel;
            if (packet.hasNonNull("channel_id")) {
                newChannel = server
                        .getVoiceChannelById(packet.get("channel_id").asLong())
                        .map(ServerVoiceChannelImpl.class::cast);

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

    private void handlePrivateChannel(UserImpl user, PrivateChannelImpl channel) {
        //channel.addConnectedUser(user);
        //user.addConnectedChannel(channel);
        //dispatchVoiceChannelMemberJoinEvent(user, channel);
    }

    private void handleGroupChannel(UserImpl user, GroupChannelImpl channel) {
        //channel.addConnectedUser(user);
        //user.addConnectedChannel(channel);
        //dispatchVoiceChannelMemberJoinEvent(user, channel);
    }

    private void dispatchServerVoiceChannelMemberJoinEvent(
            User user, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel, Server server) {
        ServerVoiceChannelMemberJoinEvent event = new ServerVoiceChannelMemberJoinEventImpl(
                user, newChannel, oldChannel);

        List<ServerVoiceChannelMemberJoinListener> listeners = new ArrayList<>();
        listeners.addAll(user.getServerVoiceChannelMemberJoinListeners());
        listeners.addAll(newChannel.getServerVoiceChannelMemberJoinListeners());
        if (server != null) {
            listeners.addAll(server.getServerVoiceChannelMemberJoinListeners());
        }
        listeners.addAll(api.getServerVoiceChannelMemberJoinListeners());

        api.getEventDispatcher().dispatchEvent(server,
                listeners, listener -> listener.onServerVoiceChannelMemberJoin(event));
    }

    private void dispatchServerVoiceChannelMemberLeaveEvent(
            User user, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel, Server server) {
        ServerVoiceChannelMemberLeaveEvent event = new ServerVoiceChannelMemberLeaveEventImpl(
                user, newChannel, oldChannel);

        List<ServerVoiceChannelMemberLeaveListener> listeners = new ArrayList<>();
        listeners.addAll(user.getServerVoiceChannelMemberLeaveListeners());
        listeners.addAll(oldChannel.getServerVoiceChannelMemberLeaveListeners());
        if (server != null) {
            listeners.addAll(server.getServerVoiceChannelMemberLeaveListeners());
        }
        listeners.addAll(api.getServerVoiceChannelMemberLeaveListeners());

        api.getEventDispatcher().dispatchEvent(server,
                listeners, listener -> listener.onServerVoiceChannelMemberLeave(event));
    }

}
