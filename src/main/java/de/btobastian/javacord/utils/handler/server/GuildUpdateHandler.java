package de.btobastian.javacord.utils.handler.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Region;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.events.server.ServerChangeNameEvent;
import de.btobastian.javacord.listeners.server.ServerChangeNameListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

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
    public void handle(JSONObject packet) {
        if (packet.has("unavailable") && packet.getBoolean("unavailable")) {
            return;
        }
        long id = Long.valueOf(packet.getString("id"));
        api.getServerById(id).map(server -> (ImplServer) server).ifPresent(server -> {
            String newName = packet.getString("name");
            String oldName = server.getName();
            if (!Objects.deepEquals(oldName, newName)) {
                server.setName(newName);
                ServerChangeNameEvent event = new ServerChangeNameEvent(api, server, newName, oldName);

                List<ServerChangeNameListener> listeners = new ArrayList<>();
                listeners.addAll(server.getServerChangeNameListeners());
                listeners.addAll(api.getServerChangeNameListeners());

                dispatchEvent(listeners, listener -> listener.onServerChangeName(event));
            }

            User newOwner = api.getUserById(packet.getString("owner_id")).orElse(null);
            User oldOwner = server.getOwner();
            if (oldOwner != newOwner) {

            }

            Region newRegion = Region.getRegionByKey(packet.getString("region"));
            Region oldRegion = server.getRegion();
            if (oldRegion != newRegion) {

            }

            String oldAfkChannelId = "...";
            String newAfkChannelId = packet.isNull("afk_channel_id") ? null : packet.getString("afk_channel_id");
            if (!Objects.deepEquals(oldAfkChannelId, newAfkChannelId)) {

            }

            int oldAfkTimeout = -1;
            int newAfkTimeout = packet.getInt("afk_timeout");
            if (oldAfkTimeout != newAfkTimeout) {

            }

            boolean oldEmbedEnabled = false;
            boolean newEmbedEnabled = packet.getBoolean("embed_enabled");
            if (oldEmbedEnabled != newEmbedEnabled) {

            }

            String oldEmbedChannelId = "...";
            String newEmbedChannelId = packet.isNull("embed_channel_id") ? null : packet.getString("embed_channel_id");
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