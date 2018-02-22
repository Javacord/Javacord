package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.DefaultMessageNotificationLevel;
import de.btobastian.javacord.entities.ExplicitContentFilterLevel;
import de.btobastian.javacord.entities.MultiFactorAuthenticationLevel;
import de.btobastian.javacord.entities.Region;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.VerificationLevel;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.ServerChangeAfkChannelEvent;
import de.btobastian.javacord.events.server.ServerChangeAfkTimeoutEvent;
import de.btobastian.javacord.events.server.ServerChangeDefaultMessageNotificationLevelEvent;
import de.btobastian.javacord.events.server.ServerChangeExplicitContentFilterLevelEvent;
import de.btobastian.javacord.events.server.ServerChangeIconEvent;
import de.btobastian.javacord.events.server.ServerChangeMultiFactorAuthenticationLevelEvent;
import de.btobastian.javacord.events.server.ServerChangeNameEvent;
import de.btobastian.javacord.events.server.ServerChangeOwnerEvent;
import de.btobastian.javacord.events.server.ServerChangeRegionEvent;
import de.btobastian.javacord.events.server.ServerChangeSplashEvent;
import de.btobastian.javacord.events.server.ServerChangeVerificationLevelEvent;
import de.btobastian.javacord.listeners.server.ServerChangeAfkChannelListener;
import de.btobastian.javacord.listeners.server.ServerChangeAfkTimeoutListener;
import de.btobastian.javacord.listeners.server.ServerChangeDefaultMessageNotificationLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeExplicitContentFilterLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeIconListener;
import de.btobastian.javacord.listeners.server.ServerChangeMultiFactorAuthenticationLevelListener;
import de.btobastian.javacord.listeners.server.ServerChangeNameListener;
import de.btobastian.javacord.listeners.server.ServerChangeOwnerListener;
import de.btobastian.javacord.listeners.server.ServerChangeRegionListener;
import de.btobastian.javacord.listeners.server.ServerChangeSplashListener;
import de.btobastian.javacord.listeners.server.ServerChangeVerificationLevelListener;
import de.btobastian.javacord.utils.PacketHandler;

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
            String newName = packet.get("name").asText();
            String oldName = server.getName();
            if (!Objects.deepEquals(oldName, newName)) {
                server.setName(newName);
                ServerChangeNameEvent event = new ServerChangeNameEvent(api, server, newName, oldName);

                List<ServerChangeNameListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeNameListeners());
                listeners.addAll(api.getServerChangeNameListeners());

                dispatchEvent(listeners, listener -> listener.onServerChangeName(event));
            }

            String newIconHash = packet.get("icon").asText(null);
            String oldIconHash = server.getIconHash();
            if (!Objects.deepEquals(oldIconHash, newIconHash)) {
                server.setIconHash(newIconHash);
                ServerChangeIconEvent event = new ServerChangeIconEvent(api, server, newIconHash, oldIconHash);

                List<ServerChangeIconListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeIconListeners());
                listeners.addAll(api.getServerChangeIconListeners());

                dispatchEvent(listeners, listener -> listener.onServerChangeIcon(event));
            }

            String newSplashHash = packet.get("splash").asText(null);
            String oldSplashHash = server.getSplashHash();
            if (!Objects.deepEquals(oldSplashHash, newSplashHash)) {
                server.setSplashHash(newSplashHash);
                ServerChangeSplashEvent event = new ServerChangeSplashEvent(api, server, newSplashHash, oldSplashHash);

                List<ServerChangeSplashListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeSplashListeners());
                listeners.addAll(api.getServerChangeSplashListeners());

                dispatchEvent(listeners, listener -> listener.onServerChangeSplash(event));
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

                dispatchEvent(listeners, listener -> listener.onServerChangeVerificationLevel(event));
            }

            Region newRegion = Region.getRegionByKey(packet.get("region").asText());
            Region oldRegion = server.getRegion();
            if (oldRegion != newRegion) {
                server.setRegion(newRegion);
                ServerChangeRegionEvent event = new ServerChangeRegionEvent(api, server, newRegion, oldRegion);

                List<ServerChangeRegionListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeRegionListeners());
                listeners.addAll(api.getServerChangeRegionListeners());

                dispatchEvent(listeners, listener -> listener.onServerChangeRegion(event));
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

                dispatchEvent(listeners, listener -> listener.onServerChangeDefaultMessageNotificationLevel(event));
            }

            User newOwner = api.getCachedUserById(packet.get("owner_id").asText()).orElse(null);
            User oldOwner = server.getOwner();
            if (oldOwner != newOwner) {
                server.setOwnerId(newOwner.getId());
                ServerChangeOwnerEvent event = new ServerChangeOwnerEvent(api, server, newOwner, oldOwner);

                List<ServerChangeOwnerListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeOwnerListeners());
                listeners.addAll(api.getServerChangeOwnerListeners());

                dispatchEvent(listeners, listener -> listener.onServerChangeOwner(event));
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

                    dispatchEvent(listeners, listener -> listener.onServerChangeAfkChannel(event));
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

                dispatchEvent(listeners, listener -> listener.onServerChangeAfkTimeout(event));
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

                dispatchEvent(listeners, listener -> listener.onServerChangeExplicitContentFilterLevel(event));
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

                dispatchEvent(listeners, listener -> listener.onServerChangeMultiFactorAuthenticationLevel(event));
            }
        });
    }

}