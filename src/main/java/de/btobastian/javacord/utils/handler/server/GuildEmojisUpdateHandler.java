package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.message.emoji.KnownCustomEmoji;
import de.btobastian.javacord.events.server.emoji.CustomEmojiChangeNameEvent;
import de.btobastian.javacord.events.server.emoji.CustomEmojiCreateEvent;
import de.btobastian.javacord.events.server.emoji.CustomEmojiDeleteEvent;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiChangeNameListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.listeners.server.emoji.CustomEmojiDeleteListener;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    public void handle(JsonNode packet) {
        long id = packet.get("guild_id").asLong();
        api.getServerById(id).map(server -> (ImplServer) server).ifPresent(server -> {
            HashMap<Long, JsonNode> emojis = new HashMap<>();
            for (JsonNode emojiJson : packet.get("emojis")) {
                emojis.put(emojiJson.get("id").asLong(), emojiJson);
            }

            emojis.entrySet().stream().forEach(entry -> {
                Optional<KnownCustomEmoji> optionalEmoji = server.getCustomEmojiById(entry.getKey());
                if (optionalEmoji.isPresent()) {
                    KnownCustomEmoji emoji = optionalEmoji.get();
                    String oldName = emoji.getName();
                    String newName = entry.getValue().get("name").asText();
                    if (!Objects.deepEquals(oldName, newName)) {
                        CustomEmojiChangeNameEvent event =
                                new CustomEmojiChangeNameEvent(emoji, emoji.getName(), oldName);

                        List<CustomEmojiChangeNameListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getCustomEmojiChangeNameListeners());
                        listeners.addAll(server.getCustomEmojiChangeNameListeners());
                        listeners.addAll(api.getCustomEmojiChangeNameListeners());

                        api.getEventDispatcher().dispatchEvent(server,
                                listeners, listener -> listener.onCustomEmojiChangeName(event));
                    }
                } else {
                    KnownCustomEmoji emoji = api.getOrCreateKnownCustomEmoji(server, entry.getValue());
                    server.addCustomEmoji(emoji);

                    CustomEmojiCreateEvent event = new CustomEmojiCreateEvent(emoji);

                    List<CustomEmojiCreateListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getCustomEmojiCreateListeners());
                    listeners.addAll(api.getCustomEmojiCreateListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onCustomEmojiCreate(event));
                }
            });

            Set<Long> emojiIds = emojis.keySet();
            server.getCustomEmojis().stream()
                    .filter(emoji -> !emojiIds.contains(emoji.getId()))
                    .forEach(emoji -> {
                        api.removeCustomEmoji(emoji);
                        server.removeCustomEmoji(emoji);

                        CustomEmojiDeleteEvent event = new CustomEmojiDeleteEvent(emoji);

                        List<CustomEmojiDeleteListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getCustomEmojiDeleteListeners());
                        listeners.addAll(server.getCustomEmojiDeleteListeners());
                        listeners.addAll(api.getCustomEmojiDeleteListeners());

                        api.getEventDispatcher().dispatchEvent(server,
                                listeners, listener -> listener.onCustomEmojiDelete(event));
                    });
        });
    }

}