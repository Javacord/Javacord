package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.event.server.sticker.StickerChangeDescriptionEvent;
import org.javacord.api.event.server.sticker.StickerChangeNameEvent;
import org.javacord.api.event.server.sticker.StickerChangeTagsEvent;
import org.javacord.api.event.server.sticker.StickerCreateEvent;
import org.javacord.api.event.server.sticker.StickerDeleteEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.sticker.StickerImpl;
import org.javacord.core.event.server.sticker.StickerChangeDescriptionEventImpl;
import org.javacord.core.event.server.sticker.StickerChangeNameEventImpl;
import org.javacord.core.event.server.sticker.StickerChangeTagsEventImpl;
import org.javacord.core.event.server.sticker.StickerCreateEventImpl;
import org.javacord.core.event.server.sticker.StickerDeleteEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Handles the guild sticker update packet.
 */
public class GuildStickersUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api   The api.
     */
    public GuildStickersUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_STICKERS_UPDATE");
    }

    @Override
    protected void handle(JsonNode packet) {
        long serverId = packet.get("guild_id").asLong();

        api.getPossiblyUnreadyServerById(serverId).map(server -> (ServerImpl) server).ifPresent(server -> {
            HashMap<Long, JsonNode> stickers = new HashMap<>();
            for (JsonNode sticketJson : packet.get("stickers")) {
                stickers.put(sticketJson.get("id").asLong(), sticketJson);
            }

            stickers.forEach((key, value) -> {
                Optional<Sticker> optionalSticker = server.getStickerById(key);
                if (optionalSticker.isPresent()) {
                    Sticker sticker = optionalSticker.get();
                    String oldName = sticker.getName();
                    String newName = value.get("name").asText();
                    if (!Objects.deepEquals(oldName, newName)) {
                        StickerChangeNameEvent event =
                                new StickerChangeNameEventImpl(sticker, oldName, newName);

                        ((StickerImpl) sticker).setName(newName);
                        api.getEventDispatcher().dispatchStickerChangeNameEvent(server, server, sticker, event);
                    }

                    String oldDescription = sticker.getDescription();
                    String newDescription = value.get("description").asText();
                    if (!Objects.deepEquals(oldDescription, newDescription)) {
                        StickerChangeDescriptionEvent event =
                                new StickerChangeDescriptionEventImpl(sticker, oldDescription, newDescription);
                        ((StickerImpl) sticker).setDescription(newDescription);

                        api.getEventDispatcher().dispatchStickerChangeDescriptionEvent(server, server, sticker, event);
                    }

                    String oldTags = sticker.getTags();
                    String newTags = value.get("tags").asText();
                    if (!Objects.deepEquals(oldTags, newTags)) {
                        StickerChangeTagsEvent event
                                = new StickerChangeTagsEventImpl(sticker, oldTags, newTags);
                        ((StickerImpl) sticker).setTags(newTags);

                        api.getEventDispatcher().dispatchStickerChangeTagsEvent(server, server, sticker, event);
                    }
                } else {
                    Sticker sticker = api.getOrCreateSticker(value);
                    server.addSticker(sticker);

                    StickerCreateEvent event = new StickerCreateEventImpl(sticker);

                    api.getEventDispatcher().dispatchStickerCreateEvent(server, server, event);
                }
            });

            Set<Long> stickerIds = stickers.keySet();
            server.getStickers().stream()
                    .filter(sticker -> !stickerIds.contains(sticker.getId()))
                    .forEach(sticker -> {
                        api.removeSticker(sticker);
                        server.removeSticker(sticker);

                        StickerDeleteEvent event = new StickerDeleteEventImpl(sticker);

                        api.getEventDispatcher().dispatchStickerDeleteEvent(server, server, sticker, event);
                    });

        });
    }
}
