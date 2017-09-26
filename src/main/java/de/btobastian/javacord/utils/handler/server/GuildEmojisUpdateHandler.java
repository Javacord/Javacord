package de.btobastian.javacord.utils.handler.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.events.server.emoji.CustomEmojiCreateEvent;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the guild update packet.
 */
public class GuildEmojisUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildEmojisUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_EMOJIS_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        long id = Long.valueOf(packet.getString("guild_id"));
        api.getServerById(id).map(server -> (ImplServer) server).ifPresent(server -> {
            JSONArray emojisJson = packet.getJSONArray("emojis");
            HashMap<Long, JSONObject> emojis = new HashMap<>();
            for (int i = 0; i < emojisJson.length(); i++) {
                JSONObject emojiJson = emojisJson.getJSONObject(i);
                emojis.put(Long.parseLong(emojiJson.getString("id")), emojiJson);
            }

            emojis.entrySet().stream().forEach(entry -> {
                if (!server.getCustomEmojiById(entry.getKey()).isPresent()) {
                    CustomEmoji emoji = api.getOrCreateCustomEmoji(server, entry.getValue());
                    server.addCustomEmoji(emoji);

                    CustomEmojiCreateEvent event = new CustomEmojiCreateEvent(api, server, emoji);

                    List<CustomEmojiCreateListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getCustomEmojiCreateListeners());
                    listeners.addAll(api.getCustomEmojiCreateListeners());

                    dispatchEvent(listeners, listener -> listener.onCustomEmojiCreate(event));
                }
            });
        });
    }

}