package org.javacord.core.entity.sticker;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.sticker.StickerPack;
import org.javacord.core.DiscordApiImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StickerPackImpl implements StickerPack {

    private final DiscordApiImpl api;
    private final long id;
    private final Set<Sticker> stickers = new HashSet<>();
    private final String name;
    private final long skuId;
    private final Long coverStickerId;
    private final String description;
    private final long bannerAssetId;

    /**
     * Creates a new implementation of a sticker pack.
     *
     * @param api The Discord API.
     * @param data The json data provided by Discord.
     */
    public StickerPackImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        this.id = data.get("id").asLong();
        for (JsonNode stickerJson : data.get("stickers")) {
            Sticker sticker = api.getOrCreateSticker(stickerJson);
            this.stickers.add(sticker);
        }

        this.name = data.get("name").asText();
        this.skuId = data.get("sku_id").asLong();
        this.coverStickerId = data.has("cover_sticker_id") ? data.get("cover_sticker_id").asLong() : null;
        this.description = data.get("description").asText();
        this.bannerAssetId = data.get("banner_asset_id").asLong();
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
    public Set<Sticker> getStickers() {
        return stickers;
    }

    @Override
    public long getSkuId() {
        return skuId;
    }

    @Override
    public Optional<Long> getCoverStickerId() {
        return Optional.ofNullable(coverStickerId);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public long getBannerAssetId() {
        return bannerAssetId;
    }
}
