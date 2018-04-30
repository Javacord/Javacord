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
import org.javacord.api.event.user.UserChangeMutedEvent;
import org.javacord.api.event.user.UserChangeSelfDeafenedEvent;
import org.javacord.api.event.user.UserChangeSelfMutedEvent;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.api.listener.user.UserChangeMutedListener;
import org.javacord.api.listener.user.UserChangeSelfDeafenedListener;
import org.javacord.api.listener.user.UserChangeSelfMutedListener;
import org.javacord.core.entity.channel.GroupChannelImpl;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.channel.ServerVoiceChannelImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelMemberJoinEventImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelMemberLeaveEventImpl;
import org.javacord.core.event.user.UserChangeMutedEventImpl;
import org.javacord.core.event.user.UserChangeSelfDeafenedEventImpl;
import org.javacord.core.event.user.UserChangeSelfMutedEventImpl;
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

        long userId = packet.get("user_id").asLong();
        if (packet.hasNonNull("guild_id")) {
            handleServerVoiceChannel(packet, userId);
        } else if (packet.hasNonNull("channel_id")) {
            long channelId = packet.get("channel_id").asLong();
            api.getVoiceChannelById(channelId).ifPresent(voiceChannel -> {
                if (voiceChannel instanceof PrivateChannel) {
                    handlePrivateChannel(userId, ((PrivateChannelImpl) voiceChannel));
                } else if (voiceChannel instanceof GroupChannel) {
                    handleGroupChannel(userId, ((GroupChannelImpl) voiceChannel));
                }
            });
        }
    }

    private void handleServerVoiceChannel(JsonNode packet, long userId) {
        api.getAllServerById(packet.get("guild_id").asLong()).map(ServerImpl.class::cast).ifPresent(server -> {
            Optional<ServerVoiceChannelImpl> oldChannel = server
                    .getConnectedVoiceChannel(userId)
                    .map(ServerVoiceChannelImpl.class::cast);

            Optional<ServerVoiceChannelImpl> newChannel;
            if (packet.hasNonNull("channel_id")) {
                newChannel = server
                        .getVoiceChannelById(packet.get("channel_id").asLong())
                        .map(ServerVoiceChannelImpl.class::cast);
            } else {
                newChannel = Optional.empty();
            }

            if (!newChannel.equals(oldChannel)) {
                oldChannel.ifPresent(channel -> {
                    channel.removeConnectedUser(userId);
                    dispatchServerVoiceChannelMemberLeaveEvent(userId, newChannel.orElse(null), channel, server);
                });

                newChannel.ifPresent(channel -> {
                    channel.addConnectedUser(userId);
                    dispatchServerVoiceChannelMemberJoinEvent(userId, channel, oldChannel.orElse(null), server);
                });
            }

            boolean newSelfMuted = packet.get("self_mute").asBoolean();
            boolean oldSelfMuted = server.isSelfMuted(userId);
            if (newSelfMuted != oldSelfMuted) {
                server.setSelfMuted(userId, newSelfMuted);
                dispatchUserChangeSelfMutedEvent(userId, server, newSelfMuted, oldSelfMuted);
            }

            boolean newSelfDeafened = packet.get("self_deaf").asBoolean();
            boolean oldSelfDeafened = server.isSelfDeafened(userId);
            if (newSelfDeafened != oldSelfDeafened) {
                server.setSelfDeafened(userId, newSelfDeafened);
                dispatchUserChangeSelfDeafenedEvent(userId, server, newSelfDeafened, oldSelfDeafened);
            }

            boolean newMuted = packet.get("mute").asBoolean();
            boolean oldMuted = server.isMuted(userId);
            if (newMuted != oldMuted) {
                server.setMuted(userId, newMuted);
                dispatchUserChangeMutedEvent(userId, server, newMuted, oldMuted);
            }
        });
    }

    private void handlePrivateChannel(long userId, PrivateChannelImpl channel) {
        //channel.addConnectedUser(user);
        //dispatchVoiceChannelMemberJoinEvent(user, channel);
    }

    private void handleGroupChannel(long userId, GroupChannelImpl channel) {
        //channel.addConnectedUser(user);
        //dispatchVoiceChannelMemberJoinEvent(user, channel);
    }

    private void dispatchServerVoiceChannelMemberJoinEvent(
            Long userId, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel, Server server) {
        ServerVoiceChannelMemberJoinEvent event = new ServerVoiceChannelMemberJoinEventImpl(
                userId, newChannel, oldChannel);

        List<ServerVoiceChannelMemberJoinListener> listeners = new ArrayList<>();
        listeners.addAll(api.getObjectListeners(User.class, userId, ServerVoiceChannelMemberJoinListener.class));
        listeners.addAll(newChannel.getServerVoiceChannelMemberJoinListeners());
        if (server != null) {
            listeners.addAll(server.getServerVoiceChannelMemberJoinListeners());
        }
        listeners.addAll(api.getServerVoiceChannelMemberJoinListeners());

        api.getEventDispatcher().dispatchEvent(server,
                listeners, listener -> listener.onServerVoiceChannelMemberJoin(event));
    }

    private void dispatchServerVoiceChannelMemberLeaveEvent(
            Long userId, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel, Server server) {
        ServerVoiceChannelMemberLeaveEvent event = new ServerVoiceChannelMemberLeaveEventImpl(
                userId, newChannel, oldChannel);

        List<ServerVoiceChannelMemberLeaveListener> listeners = new ArrayList<>();
        listeners.addAll(api.getObjectListeners(User.class, userId, ServerVoiceChannelMemberLeaveListener.class));
        listeners.addAll(oldChannel.getServerVoiceChannelMemberLeaveListeners());
        if (server != null) {
            listeners.addAll(server.getServerVoiceChannelMemberLeaveListeners());
        }
        listeners.addAll(api.getServerVoiceChannelMemberLeaveListeners());

        api.getEventDispatcher().dispatchEvent(server,
                listeners, listener -> listener.onServerVoiceChannelMemberLeave(event));
    }

    private void dispatchUserChangeSelfMutedEvent(
            Long userId, Server server, boolean newSelfMuted, boolean oldSelfMuted) {
        UserChangeSelfMutedEvent event =
                new UserChangeSelfMutedEventImpl(userId, server, newSelfMuted, oldSelfMuted);

        List<UserChangeSelfMutedListener> listeners = new ArrayList<>();
        listeners.addAll(api.getObjectListeners(User.class, userId, UserChangeSelfMutedListener.class));
        listeners.addAll(server.getUserChangeSelfMutedListeners());
        listeners.addAll(api.getUserChangeSelfMutedListeners());

        api.getEventDispatcher().dispatchEvent(server,
                listeners, listener -> listener.onUserChangeSelfMuted(event));
    }

    private void dispatchUserChangeSelfDeafenedEvent(
            Long userId, Server server, boolean newSelfDeafened, boolean oldSelfDeafened) {
        UserChangeSelfDeafenedEvent event =
                new UserChangeSelfDeafenedEventImpl(userId, server, newSelfDeafened, oldSelfDeafened);

        List<UserChangeSelfDeafenedListener> listeners = new ArrayList<>();
        listeners.addAll(api.getObjectListeners(User.class, userId, UserChangeSelfDeafenedListener.class));
        listeners.addAll(server.getUserChangeSelfDeafenedListeners());
        listeners.addAll(api.getUserChangeSelfDeafenedListeners());

        api.getEventDispatcher().dispatchEvent(server,
                listeners, listener -> listener.onUserChangeSelfDeafened(event));
    }

    private void dispatchUserChangeMutedEvent(Long userId, Server server, boolean newMuted, boolean oldMuted) {
        UserChangeMutedEvent event =
                new UserChangeMutedEventImpl(userId, server, newMuted, oldMuted);

        List<UserChangeMutedListener> listeners = new ArrayList<>();
        listeners.addAll(api.getObjectListeners(User.class, userId, UserChangeMutedListener.class));
        listeners.addAll(server.getUserChangeMutedListeners());
        listeners.addAll(api.getUserChangeMutedListeners());

        api.getEventDispatcher().dispatchEvent(server,
                listeners, listener -> listener.onUserChangeMuted(event));
    }

}
