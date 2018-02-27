package de.btobastian.javacord.util.handler.server;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.emoji.KnownCustomEmoji;
import de.btobastian.javacord.entity.emoji.impl.ImplKnownCustomEmoji;
import de.btobastian.javacord.entity.permission.Role;
import de.btobastian.javacord.entity.server.impl.ImplServer;
import de.btobastian.javacord.event.server.emoji.CustomEmojiChangeNameEvent;
import de.btobastian.javacord.event.server.emoji.CustomEmojiChangeWhitelistedRolesEvent;
import de.btobastian.javacord.event.server.emoji.CustomEmojiCreateEvent;
import de.btobastian.javacord.event.server.emoji.CustomEmojiDeleteEvent;
import de.btobastian.javacord.listener.server.emoji.CustomEmojiChangeNameListener;
import de.btobastian.javacord.listener.server.emoji.CustomEmojiChangeWhitelistedRolesListener;
import de.btobastian.javacord.listener.server.emoji.CustomEmojiCreateListener;
import de.btobastian.javacord.listener.server.emoji.CustomEmojiDeleteListener;
import de.btobastian.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
                        CustomEmojiChangeNameEvent event = new CustomEmojiChangeNameEvent(emoji, newName, oldName);
                        ((ImplKnownCustomEmoji) emoji).setName(newName);

                        List<CustomEmojiChangeNameListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getCustomEmojiChangeNameListeners());
                        listeners.addAll(server.getCustomEmojiChangeNameListeners());
                        listeners.addAll(api.getCustomEmojiChangeNameListeners());

                        api.getEventDispatcher().dispatchEvent(server,
                                listeners, listener -> listener.onCustomEmojiChangeName(event));
                    }

                    Collection<Role> oldWhitelist = emoji.getWhitelistedRoles().orElse(Collections.emptySet());
                    JsonNode newWhitelistJson = entry.getValue().get("roles");
                    Collection<Role> newWhitelist = new ArrayList<>();
                    if (newWhitelistJson != null && !newWhitelistJson.isNull()) {
                        for (JsonNode role : newWhitelistJson) {
                            server.getRoleById(role.asLong()).ifPresent(newWhitelist::add);
                        }
                    }
                    if (!newWhitelist.containsAll(oldWhitelist) || !oldWhitelist.containsAll(newWhitelist)) {
                        CustomEmojiChangeWhitelistedRolesEvent event =
                                new CustomEmojiChangeWhitelistedRolesEvent(emoji, newWhitelist, oldWhitelist);
                        ((ImplKnownCustomEmoji) emoji).setWhitelist(newWhitelist);

                        List<CustomEmojiChangeWhitelistedRolesListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getCustomEmojiChangeWhitelistedRolesListeners());
                        listeners.addAll(server.getCustomEmojiChangeWhitelistedRolesListeners());
                        listeners.addAll(api.getCustomEmojiChangeWhitelistedRolesListeners());

                        api.getEventDispatcher().dispatchEvent(server,
                                listeners, listener -> listener.onCustomEmojiChangeWhitelistedRoles(event));
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