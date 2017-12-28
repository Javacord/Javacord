package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.DefaultMessageNotificationLevel;
import de.btobastian.javacord.entities.Region;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.VerificationLevel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.*;
import de.btobastian.javacord.listeners.server.*;
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

            User newOwner = api.getUserById(packet.get("owner_id").asText()).orElse(null);
            User oldOwner = server.getOwner();
            if (oldOwner != newOwner) {
                server.setOwnerId(newOwner.getId());
                ServerChangeOwnerEvent event = new ServerChangeOwnerEvent(api, server, newOwner, oldOwner);

                List<ServerChangeOwnerListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeOwnerListeners());
                listeners.addAll(api.getServerChangeOwnerListeners());

                dispatchEvent(listeners, listener -> listener.onServerChangeOwner(event));
            }

            String oldAfkChannelId = "...";
            String newAfkChannelId =
                    packet.get("afk_channel_id").isNull() ? null : packet.get("afk_channel_id").asText();
            if (!Objects.deepEquals(oldAfkChannelId, newAfkChannelId)) {

            }

            int oldAfkTimeout = -1;
            int newAfkTimeout = packet.get("afk_timeout").asInt();
            if (oldAfkTimeout != newAfkTimeout) {

            }

            boolean oldEmbedEnabled = false;
            boolean newEmbedEnabled = packet.get("embed_enabled").asBoolean();
            if (oldEmbedEnabled != newEmbedEnabled) {

            }

            String oldEmbedChannelId = "...";
            String newEmbedChannelId =
                    packet.get("embed_channel_id").isNull() ? null : packet.get("embed_channel_id").asText();
            if (!Objects.deepEquals(oldEmbedChannelId, newEmbedChannelId)) {

            }

            // TODO verification_level
            // TODO default_message_notifications
            // TODO explicit_content_filter
            // TODO roles
            // TODO emojis
            // TODO features
            // TODO mfa_level
            // TODO application_id
            // TODO widget_enabled
            // TODO widget_channel_id

        });
    }

}