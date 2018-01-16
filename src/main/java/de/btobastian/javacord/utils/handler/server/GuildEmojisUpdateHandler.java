package de.btobastian.javacord.utils.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplCustomEmoji;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                Optional<CustomEmoji> optionalEmoji = server.getCustomEmojiById(entry.getKey());
                if (optionalEmoji.isPresent()) {
                    CustomEmoji emoji = optionalEmoji.get();
                    String oldName = emoji.getName();
                    if (((ImplCustomEmoji) emoji).updateFromJson(entry.getValue())) {
                        CustomEmojiChangeNameEvent event =
                                new CustomEmojiChangeNameEvent(emoji, emoji.getName(), oldName);

                        List<CustomEmojiChangeNameListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getCustomEmojiChangeNameListeners());
                        listeners.addAll(server.getCustomEmojiChangeNameListeners());
                        listeners.addAll(api.getCustomEmojiChangeNameListeners());

                        dispatchEvent(listeners, listener -> listener.onCustomEmojiChangeName(event));
                    }
                } else {
                    CustomEmoji emoji = api.getOrCreateCustomEmoji(server, entry.getValue());
                    // update in case it was already present on another server
                    ((ImplCustomEmoji) emoji).updateFromJson(entry.getValue());
                    server.addCustomEmoji(emoji);

                    CustomEmojiCreateEvent event = new CustomEmojiCreateEvent(emoji);

                    List<CustomEmojiCreateListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getCustomEmojiCreateListeners());
                    listeners.addAll(api.getCustomEmojiCreateListeners());

                    dispatchEvent(listeners, listener -> listener.onCustomEmojiCreate(event));
                }
            });

            Set<Long> emojiIds = emojis.keySet();
            server.getCustomEmojis().stream()
                    .filter(emoji -> !emojiIds.contains(emoji.getId()))
                    // decouple from original collection
                    .collect(Collectors.toList())
                    .stream()
                    .forEach(emoji -> {
                        server.removeCustomEmoji(emoji);

                        CustomEmojiDeleteEvent event = new CustomEmojiDeleteEvent(emoji);

                        List<CustomEmojiDeleteListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getCustomEmojiDeleteListeners());
                        listeners.addAll(server.getCustomEmojiDeleteListeners());
                        listeners.addAll(api.getCustomEmojiDeleteListeners());

                        dispatchEvent(listeners, listener -> listener.onCustomEmojiDelete(event));
                    });
        });
    }

}