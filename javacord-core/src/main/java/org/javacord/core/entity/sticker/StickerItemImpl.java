package org.javacord.core.entity.sticker;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.sticker.StickerFormatType;
import org.javacord.api.entity.sticker.StickerItem;
import org.javacord.core.DiscordApiImpl;

public class StickerItemImpl implements StickerItem {

    private final DiscordApiImpl api;
    private final long id;
    private final String name;
    private final StickerFormatType formatType;

    /**
     * Creates a new implementation of a sticker item.
     *
     * @param api The Discord API.
     * @param data The data provided by Discord.
     */
    public StickerItemImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        this.formatType = StickerFormatType.fromId(data.get("format_type").asInt());
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public StickerFormatType getFormatType() {
        return formatType;
    }
}
