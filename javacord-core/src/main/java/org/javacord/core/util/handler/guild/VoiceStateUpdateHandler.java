package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberJoinEvent;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import org.javacord.api.event.server.VoiceStateUpdateEvent;
import org.javacord.api.event.user.UserChangeDeafenedEvent;
import org.javacord.api.event.user.UserChangeMutedEvent;
import org.javacord.api.event.user.UserChangeSelfDeafenedEvent;
import org.javacord.api.event.user.UserChangeSelfMutedEvent;
import org.javacord.core.audio.AudioConnectionImpl;
import org.javacord.core.entity.channel.GroupChannelImpl;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.channel.ServerVoiceChannelImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelMemberJoinEventImpl;
import org.javacord.core.event.channel.server.voice.ServerVoiceChannelMemberLeaveEventImpl;
import org.javacord.core.event.server.VoiceStateUpdateEventImpl;
import org.javacord.core.event.user.UserChangeDeafenedEventImpl;
import org.javacord.core.event.user.UserChangeMutedEventImpl;
import org.javacord.core.event.user.UserChangeSelfDeafenedEventImpl;
import org.javacord.core.event.user.UserChangeSelfMutedEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

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

        if (api.getYourself().getId() == userId) {
            handleSelf(packet);
        }
    }

    private void handleSelf(JsonNode packet) {
        // We need the session id to connect to an audio websocket
        String sessionId = packet.get("session_id").asText();
        long channelId = packet.get("channel_id").asLong();
        api.getServerVoiceChannelById(channelId).ifPresent(channel -> {
            dispatchVoiceStateUpdateEvent(
                    channel, channel.getServer(), packet.get("session_id").asText());

            AudioConnectionImpl pendingAudioConnection =
                    api.getPendingAudioConnectionByServerId(channel.getServer().getId());
            if (pendingAudioConnection != null) {
                pendingAudioConnection.setSessionId(sessionId);
                pendingAudioConnection.tryConnect();
            }

            channel.getServer().getAudioConnection().ifPresent(connection -> {
                ((AudioConnectionImpl) connection).setSessionId(sessionId);
                ((AudioConnectionImpl) connection).tryConnect();
            });
        });
    }

    private void handleServerVoiceChannel(JsonNode packet, long userId) {
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong())
                .map(ServerImpl.class::cast).ifPresent(server -> {
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
                            dispatchServerVoiceChannelMemberLeaveEvent(
                                    userId, newChannel.orElse(null), channel, server);
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

                    boolean newDeafened = packet.get("deaf").asBoolean();
                    boolean oldDeafened = server.isDeafened(userId);
                    if (newDeafened != oldDeafened) {
                        server.setDeafened(userId, newDeafened);
                        dispatchUserChangeDeafenedEvent(userId, server, newDeafened, oldDeafened);
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

    private void dispatchVoiceStateUpdateEvent(ServerVoiceChannel newChannel, Server server, String sessionId) {
        VoiceStateUpdateEvent event = new VoiceStateUpdateEventImpl(newChannel, sessionId);

        api.getEventDispatcher().dispatchVoiceStateUpdateEvent((DispatchQueueSelector) server, newChannel, event);
    }

    private void dispatchServerVoiceChannelMemberJoinEvent(
            Long userId, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel, Server server) {
        ServerVoiceChannelMemberJoinEvent event = new ServerVoiceChannelMemberJoinEventImpl(
                userId, newChannel, oldChannel);

        api.getEventDispatcher().dispatchServerVoiceChannelMemberJoinEvent(
                (DispatchQueueSelector) server, server, newChannel, userId, event);
    }

    private void dispatchServerVoiceChannelMemberLeaveEvent(
            Long userId, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel, Server server) {
        ServerVoiceChannelMemberLeaveEvent event = new ServerVoiceChannelMemberLeaveEventImpl(
                userId, newChannel, oldChannel);

        api.getEventDispatcher().dispatchServerVoiceChannelMemberLeaveEvent(
                (DispatchQueueSelector) server, server, oldChannel, userId, event);
    }

    private void dispatchUserChangeSelfMutedEvent(
            Long userId, Server server, boolean newSelfMuted, boolean oldSelfMuted) {
        UserChangeSelfMutedEvent event =
                new UserChangeSelfMutedEventImpl(userId, server, newSelfMuted, oldSelfMuted);

        api.getEventDispatcher().dispatchUserChangeSelfMutedEvent(
                (DispatchQueueSelector) server, server, userId, event);
    }

    private void dispatchUserChangeSelfDeafenedEvent(
            Long userId, Server server, boolean newSelfDeafened, boolean oldSelfDeafened) {
        UserChangeSelfDeafenedEvent event =
                new UserChangeSelfDeafenedEventImpl(userId, server, newSelfDeafened, oldSelfDeafened);

        api.getEventDispatcher().dispatchUserChangeSelfDeafenedEvent(
                (DispatchQueueSelector) server, server, userId, event);
    }

    private void dispatchUserChangeMutedEvent(Long userId, Server server, boolean newMuted, boolean oldMuted) {
        UserChangeMutedEvent event =
                new UserChangeMutedEventImpl(userId, server, newMuted, oldMuted);

        api.getEventDispatcher().dispatchUserChangeMutedEvent((DispatchQueueSelector) server, server, userId, event);
    }

    private void dispatchUserChangeDeafenedEvent(
            Long userId, Server server, boolean newDeafened, boolean oldDeafened) {
        UserChangeDeafenedEvent event =
                new UserChangeDeafenedEventImpl(userId, server, newDeafened, oldDeafened);

        api.getEventDispatcher().dispatchUserChangeDeafenedEvent((DispatchQueueSelector) server, server, userId, event);
    }

}
