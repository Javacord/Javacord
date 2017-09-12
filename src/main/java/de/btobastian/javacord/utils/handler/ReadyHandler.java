package de.btobastian.javacord.utils.handler;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class handles the ready packet.
 */
public class ReadyHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ReadyHandler(DiscordApi api) {
        super(api, false, "READY");
    }

    @Override
    public void handle(JSONObject packet) {
        JSONArray guilds = packet.getJSONArray("guilds");
        for (int i = 0; i < guilds.length(); i++) {
            JSONObject guild = guilds.getJSONObject(i);
            if (guild.has("unavailable") && guild.getBoolean("unavailable")) {
                api.addUnavailableServerToCache(Long.valueOf(guild.getString("id")));
                continue;
            }
            new ImplServer(api, guild);
        }

        JSONArray privateChannels = packet.getJSONArray("private_channels");
        for (int i = 0; i < privateChannels.length(); i++) {
            JSONObject privateChannel = privateChannels.getJSONObject(i);
            String id = privateChannel.getString("id");
            if (privateChannel.has("recipient")) {
                // TODO Create private channel object
            }
        }

        // TODO api.setYourself(api.getOrCreateUser(packet.getJSONObject("user")));
    }

}