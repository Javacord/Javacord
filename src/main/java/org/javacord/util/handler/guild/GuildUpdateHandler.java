package org.javacord.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.Region;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.ExplicitContentFilterLevel;
import org.javacord.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.entity.server.VerificationLevel;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.user.User;
import org.javacord.event.server.ServerChangeAfkChannelEvent;
import org.javacord.event.server.ServerChangeAfkTimeoutEvent;
import org.javacord.event.server.ServerChangeDefaultMessageNotificationLevelEvent;
import org.javacord.event.server.ServerChangeExplicitContentFilterLevelEvent;
import org.javacord.event.server.ServerChangeIconEvent;
import org.javacord.event.server.ServerChangeMultiFactorAuthenticationLevelEvent;
import org.javacord.event.server.ServerChangeNameEvent;
import org.javacord.event.server.ServerChangeOwnerEvent;
import org.javacord.event.server.ServerChangeRegionEvent;
import org.javacord.event.server.ServerChangeSplashEvent;
import org.javacord.event.server.ServerChangeSystemChannelEvent;
import org.javacord.event.server.ServerChangeVerificationLevelEvent;
import org.javacord.listener.server.ServerChangeAfkChannelListener;
import org.javacord.listener.server.ServerChangeAfkTimeoutListener;
import org.javacord.listener.server.ServerChangeDefaultMessageNotificationLevelListener;
import org.javacord.listener.server.ServerChangeExplicitContentFilterLevelListener;
import org.javacord.listener.server.ServerChangeIconListener;
import org.javacord.listener.server.ServerChangeMultiFactorAuthenticationLevelListener;
import org.javacord.listener.server.ServerChangeNameListener;
import org.javacord.listener.server.ServerChangeOwnerListener;
import org.javacord.listener.server.ServerChangeRegionListener;
import org.javacord.listener.server.ServerChangeSplashListener;
import org.javacord.listener.server.ServerChangeSystemChannelListener;
import org.javacord.listener.server.ServerChangeVerificationLevelListener;
import org.javacord.util.gateway.PacketHandler;
import org.javacord.entity.Region;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.ExplicitContentFilterLevel;
import org.javacord.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.entity.server.VerificationLevel;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.user.User;
import org.javacord.event.server.ServerChangeAfkChannelEvent;
import org.javacord.event.server.ServerChangeAfkTimeoutEvent;
import org.javacord.event.server.ServerChangeDefaultMessageNotificationLevelEvent;
import org.javacord.event.server.ServerChangeExplicitContentFilterLevelEvent;
import org.javacord.event.server.ServerChangeIconEvent;
import org.javacord.event.server.ServerChangeMultiFactorAuthenticationLevelEvent;
import org.javacord.event.server.ServerChangeNameEvent;
import org.javacord.event.server.ServerChangeOwnerEvent;
import org.javacord.event.server.ServerChangeRegionEvent;
import org.javacord.event.server.ServerChangeSplashEvent;
import org.javacord.event.server.ServerChangeSystemChannelEvent;
import org.javacord.event.server.ServerChangeVerificationLevelEvent;
import org.javacord.listener.server.ServerChangeIconListener;
import org.javacord.listener.server.ServerChangeNameListener;
import org.javacord.listener.server.ServerChangeOwnerListener;
import org.javacord.listener.server.ServerChangeRegionListener;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles the guild update packet.
 */
public class GuildUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        if (packet.has("unavailable") && packet.get("unavailable").asBoolean()) {
            return;
        }
        long id = packet.get("id").asLong();
        api.getServerById(id).map(server -> (ImplServer) server).ifPresent(server -> {
            long oldApplicationId = server.getApplicationId().orElse(-1L);
            long newApplicationId = packet.hasNonNull("application_id") ? packet.get("application_id").asLong() : -1L;
            if (oldApplicationId != newApplicationId) {
                server.setApplicationId(newApplicationId);
            }

            String newName = packet.get("name").asText();
            String oldName = server.getName();
            if (!Objects.deepEquals(oldName, newName)) {
                server.setName(newName);
                ServerChangeNameEvent event = new ServerChangeNameEvent(api, server, newName, oldName);

                List<ServerChangeNameListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeNameListeners());
                listeners.addAll(api.getServerChangeNameListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeName(event));
            }

            String newIconHash = packet.get("icon").asText(null);
            String oldIconHash = server.getIconHash();
            if (!Objects.deepEquals(oldIconHash, newIconHash)) {
                server.setIconHash(newIconHash);
                ServerChangeIconEvent event = new ServerChangeIconEvent(api, server, newIconHash, oldIconHash);

                List<ServerChangeIconListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeIconListeners());
                listeners.addAll(api.getServerChangeIconListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeIcon(event));
            }

            String newSplashHash = packet.get("splash").asText(null);
            String oldSplashHash = server.getSplashHash();
            if (!Objects.deepEquals(oldSplashHash, newSplashHash)) {
                server.setSplashHash(newSplashHash);
                ServerChangeSplashEvent event = new ServerChangeSplashEvent(api, server, newSplashHash, oldSplashHash);

                List<ServerChangeSplashListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeSplashListeners());
                listeners.addAll(api.getServerChangeSplashListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeSplash(event));
            }

            VerificationLevel newVerificationLevel = VerificationLevel.fromId(packet.get("verification_level").asInt());
            VerificationLevel oldVerificationLevel = server.getVerificationLevel();
            if (newVerificationLevel != oldVerificationLevel) {
                server.setVerificationLevel(newVerificationLevel);
                ServerChangeVerificationLevelEvent event = new ServerChangeVerificationLevelEvent(
                        api, server, newVerificationLevel, oldVerificationLevel);

                List<ServerChangeVerificationLevelListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeVerificationLevelListeners());
                listeners.addAll(api.getServerChangeVerificationLevelListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeVerificationLevel(event));
            }

            Region newRegion = Region.getRegionByKey(packet.get("region").asText());
            Region oldRegion = server.getRegion();
            if (oldRegion != newRegion) {
                server.setRegion(newRegion);
                ServerChangeRegionEvent event = new ServerChangeRegionEvent(api, server, newRegion, oldRegion);

                List<ServerChangeRegionListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeRegionListeners());
                listeners.addAll(api.getServerChangeRegionListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeRegion(event));
            }

            DefaultMessageNotificationLevel newDefaultMessageNotificationLevel =
                    DefaultMessageNotificationLevel.fromId(packet.get("default_message_notifications").asInt());
            DefaultMessageNotificationLevel oldDefaultMessageNotificationLevel =
                    server.getDefaultMessageNotificationLevel();
            if (newDefaultMessageNotificationLevel != oldDefaultMessageNotificationLevel) {
                server.setDefaultMessageNotificationLevel(newDefaultMessageNotificationLevel);
                ServerChangeDefaultMessageNotificationLevelEvent event =
                        new ServerChangeDefaultMessageNotificationLevelEvent(
                                api, server, newDefaultMessageNotificationLevel, oldDefaultMessageNotificationLevel);

                List<ServerChangeDefaultMessageNotificationLevelListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeDefaultMessageNotificationLevelListeners());
                listeners.addAll(api.getServerChangeDefaultMessageNotificationLevelListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeDefaultMessageNotificationLevel(event));
            }

            User newOwner = api.getCachedUserById(packet.get("owner_id").asText()).orElse(null);
            User oldOwner = server.getOwner();
            if (oldOwner != newOwner) {
                server.setOwnerId(newOwner.getId());
                ServerChangeOwnerEvent event = new ServerChangeOwnerEvent(api, server, newOwner, oldOwner);

                List<ServerChangeOwnerListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeOwnerListeners());
                listeners.addAll(api.getServerChangeOwnerListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeOwner(event));
            }

            if (packet.has("system_channel_id")) {
                ServerTextChannel newSystemChannel = packet.get("system_channel_id").isNull() ?
                        null : server.getTextChannelById(packet.get("system_channel_id").asLong()).orElse(null);
                ServerTextChannel oldSystemChannel = server.getSystemChannel().orElse(null);
                if (oldSystemChannel != newSystemChannel) {
                    server.setSystemChannelId(newSystemChannel == null ? -1 : newSystemChannel.getId());
                    ServerChangeSystemChannelEvent event =
                            new ServerChangeSystemChannelEvent(api, server, newSystemChannel, oldSystemChannel);

                    List<ServerChangeSystemChannelListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerChangeSystemChannelListeners());
                    listeners.addAll(api.getServerChangeSystemChannelListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onServerChangeSystemChannel(event));
                }
            }

            if (packet.has("afk_channel_id")) {
                ServerVoiceChannel newAfkChannel = packet.get("afk_channel_id").isNull() ?
                        null : server.getVoiceChannelById(packet.get("afk_channel_id").asLong()).orElse(null);
                ServerVoiceChannel oldAfkChannel = server.getAfkChannel().orElse(null);
                if (oldAfkChannel != newAfkChannel) {
                    server.setAfkChannelId(newAfkChannel == null ? -1 : newAfkChannel.getId());
                    ServerChangeAfkChannelEvent event =
                            new ServerChangeAfkChannelEvent(api, server, newAfkChannel, oldAfkChannel);

                    List<ServerChangeAfkChannelListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getServerChangeAfkChannelListeners());
                    listeners.addAll(api.getServerChangeAfkChannelListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onServerChangeAfkChannel(event));
                }
            }

            int newAfkTimeout = packet.get("afk_timeout").asInt();
            int oldAfkTimeout = server.getAfkTimeoutInSeconds();
            if (oldAfkTimeout != newAfkTimeout) {
                server.setAfkTimeout(newAfkTimeout);
                ServerChangeAfkTimeoutEvent event =
                        new ServerChangeAfkTimeoutEvent(api, server, newAfkTimeout, oldAfkTimeout);

                List<ServerChangeAfkTimeoutListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeAfkTimeoutListeners());
                listeners.addAll(api.getServerChangeAfkTimeoutListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeAfkTimeout(event));
            }

            ExplicitContentFilterLevel newExplicitContentFilterLevel =
                    ExplicitContentFilterLevel.fromId(packet.get("explicit_content_filter").asInt());
            ExplicitContentFilterLevel oldExplicitContentFilterLevel = server.getExplicitContentFilterLevel();
            if (oldExplicitContentFilterLevel != newExplicitContentFilterLevel) {
                server.setExplicitContentFilterLevel(newExplicitContentFilterLevel);
                ServerChangeExplicitContentFilterLevelEvent event = new ServerChangeExplicitContentFilterLevelEvent(
                        api, server, newExplicitContentFilterLevel, oldExplicitContentFilterLevel);

                List<ServerChangeExplicitContentFilterLevelListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeExplicitContentFilterLevelListeners());
                listeners.addAll(api.getServerChangeExplicitContentFilterLevelListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeExplicitContentFilterLevel(event));
            }

            MultiFactorAuthenticationLevel newMultiFactorAuthenticationLevel =
                    MultiFactorAuthenticationLevel.fromId(packet.get("mfa_level").asInt());
            MultiFactorAuthenticationLevel oldMultiFactorAuthenticationLevel =
                    server.getMultiFactorAuthenticationLevel();
            if (oldMultiFactorAuthenticationLevel != newMultiFactorAuthenticationLevel) {
                server.setMultiFactorAuthenticationLevel(newMultiFactorAuthenticationLevel);
                ServerChangeMultiFactorAuthenticationLevelEvent event =
                        new ServerChangeMultiFactorAuthenticationLevelEvent(api,
                                server, newMultiFactorAuthenticationLevel, oldMultiFactorAuthenticationLevel);

                List<ServerChangeMultiFactorAuthenticationLevelListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeMultiFactorAuthenticationLevelListeners());
                listeners.addAll(api.getServerChangeMultiFactorAuthenticationLevelListeners());

                api.getEventDispatcher().dispatchEvent(server,
                        listeners, listener -> listener.onServerChangeMultiFactorAuthenticationLevel(event));
            }
        });
    }

}