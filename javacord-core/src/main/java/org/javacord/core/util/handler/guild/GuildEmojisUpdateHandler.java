package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.emoji.KnownCustomEmojiChangeNameEvent;
import org.javacord.api.event.server.emoji.KnownCustomEmojiChangeWhitelistedRolesEvent;
import org.javacord.api.event.server.emoji.KnownCustomEmojiCreateEvent;
import org.javacord.api.event.server.emoji.KnownCustomEmojiDeleteEvent;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeNameListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeWhitelistedRolesListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiCreateListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiDeleteListener;
import org.javacord.core.entity.emoji.KnownCustomEmojiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.emoji.KnownCustomEmojiChangeNameEventImpl;
import org.javacord.core.event.server.emoji.KnownCustomEmojiChangeWhitelistedRolesEventImpl;
import org.javacord.core.event.server.emoji.KnownCustomEmojiCreateEventImpl;
import org.javacord.core.event.server.emoji.KnownCustomEmojiDeleteEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
        api.getPossiblyUnreadyServerById(id).map(server -> (ServerImpl) server).ifPresent(server -> {
            HashMap<Long, JsonNode> emojis = new HashMap<>();
            for (JsonNode emojiJson : packet.get("emojis")) {
                emojis.put(emojiJson.get("id").asLong(), emojiJson);
            }

            emojis.forEach((key, value) -> {
                Optional<KnownCustomEmoji> optionalEmoji = server.getCustomEmojiById(key);
                if (optionalEmoji.isPresent()) {
                    KnownCustomEmoji emoji = optionalEmoji.get();
                    String oldName = emoji.getName();
                    String newName = value.get("name").asText();
                    if (!Objects.deepEquals(oldName, newName)) {
                        KnownCustomEmojiChangeNameEvent event =
                                new KnownCustomEmojiChangeNameEventImpl(emoji, newName, oldName);
                        ((KnownCustomEmojiImpl) emoji).setName(newName);

                        List<KnownCustomEmojiChangeNameListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getKnownCustomEmojiChangeNameListeners());
                        listeners.addAll(server.getKnownCustomEmojiChangeNameListeners());
                        listeners.addAll(api.getKnownCustomEmojiChangeNameListeners());

                        api.getEventDispatcher().dispatchEvent(server,
                                listeners, listener -> listener.onKnownCustomEmojiChangeName(event));
                    }

                    Collection<Role> oldWhitelist = emoji.getWhitelistedRoles().orElse(Collections.emptySet());
                    JsonNode newWhitelistJson = value.get("roles");
                    Collection<Role> newWhitelist = new ArrayList<>();
                    if (newWhitelistJson != null && !newWhitelistJson.isNull()) {
                        for (JsonNode role : newWhitelistJson) {
                            server.getRoleById(role.asLong()).ifPresent(newWhitelist::add);
                        }
                    }
                    if (!newWhitelist.containsAll(oldWhitelist) || !oldWhitelist.containsAll(newWhitelist)) {
                        KnownCustomEmojiChangeWhitelistedRolesEvent event =
                                new KnownCustomEmojiChangeWhitelistedRolesEventImpl(emoji, newWhitelist, oldWhitelist);
                        ((KnownCustomEmojiImpl) emoji).setWhitelist(newWhitelist);

                        List<KnownCustomEmojiChangeWhitelistedRolesListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getKnownCustomEmojiChangeWhitelistedRolesListeners());
                        listeners.addAll(server.getKnownCustomEmojiChangeWhitelistedRolesListeners());
                        listeners.addAll(api.getKnownCustomEmojiChangeWhitelistedRolesListeners());

                        api.getEventDispatcher().dispatchEvent(server,
                                listeners, listener -> listener.onKnownCustomEmojiChangeWhitelistedRoles(event));
                    }
                } else {
                    KnownCustomEmoji emoji = api.getOrCreateKnownCustomEmoji(server, value);
                    server.addCustomEmoji(emoji);

                    KnownCustomEmojiCreateEvent event = new KnownCustomEmojiCreateEventImpl(emoji);

                    List<KnownCustomEmojiCreateListener> listeners = new ArrayList<>();
                    listeners.addAll(server.getKnownCustomEmojiCreateListeners());
                    listeners.addAll(api.getKnownCustomEmojiCreateListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onKnownCustomEmojiCreate(event));
                }
            });

            Set<Long> emojiIds = emojis.keySet();
            server.getCustomEmojis().stream()
                    .filter(emoji -> !emojiIds.contains(emoji.getId()))
                    .forEach(emoji -> {
                        api.removeCustomEmoji(emoji);
                        server.removeCustomEmoji(emoji);

                        KnownCustomEmojiDeleteEvent event = new KnownCustomEmojiDeleteEventImpl(emoji);

                        List<KnownCustomEmojiDeleteListener> listeners = new ArrayList<>();
                        listeners.addAll(emoji.getKnownCustomEmojiDeleteListeners());
                        listeners.addAll(server.getKnownCustomEmojiDeleteListeners());
                        listeners.addAll(api.getKnownCustomEmojiDeleteListeners());

                        api.getEventDispatcher().dispatchEvent(server,
                                listeners, listener -> listener.onKnownCustomEmojiDelete(event));
                    });
        });
    }

}