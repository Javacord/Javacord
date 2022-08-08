package org.javacord.core.entity.sticker;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.sticker.StickerFormatType;
import org.javacord.api.entity.sticker.StickerType;
import org.javacord.api.entity.sticker.StickerUpdater;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.listener.server.sticker.InternalStickerAttachableListenerManager;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class StickerImpl implements Sticker, InternalStickerAttachableListenerManager {

    private final DiscordApiImpl api;
    private final long id;
    private final Long packId;
    private final StickerType type;
    private final StickerFormatType formatType;
    private final Boolean available;
    private final Long serverId;
    private final User user;
    private final Integer sortValue;

    private String name;
    private String description;
    private String tags;

    /**
     * Creates a new sticker implementation.
     *
     * @param api The Discord API.
     * @param data The packet provided by Discord.
     */
    public StickerImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        this.id = data.get("id").asLong();
        this.packId = data.has("pack_id") ? data.get("pack_id").asLong() : null;
        this.name = data.get("name").asText();
        this.description = data.hasNonNull("description") ? data.get("description").asText() : "";
        this.tags = data.get("tags").asText();
        this.type = StickerType.fromId(data.get("type").asInt());
        this.formatType = StickerFormatType.fromId(data.get("format_type").asInt());
        this.available = data.has("available") ? data.get("available").asBoolean() : null;
        this.serverId = data.has("guild_id") ? data.get("guild_id").asLong() : null;
        this.user = data.has("user") ? new UserImpl(api, data.get("user")) : null;

        this.sortValue = data.has("sort_value") ? data.get("sort_value").asInt() : null;
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
    public Optional<Long> getPackId() {
        return Optional.ofNullable(packId);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTags() {
        return tags;
    }

    @Override
    public StickerType getType() {
        return type;
    }

    @Override
    public StickerFormatType getFormatType() {
        return formatType;
    }

    @Override
    public Optional<Boolean> isAvailable() {
        return Optional.ofNullable(available);
    }

    @Override
    public Optional<Long> getServerId() {
        return Optional.ofNullable(serverId);
    }

    @Override
    public Optional<Server> getServer() {
        return getServerId().flatMap(api::getServerById);
    }

    @Override
    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<Integer> getSortValue() {
        return Optional.ofNullable(sortValue);
    }

    /**
     * Sets the name of the sticker.
     *
     * @param name The name of the sticker.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description of the sticker.
     *
     * @param description The description of the sticker.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the tags of the sticker.
     *
     * @param tags The tags of the sticker
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public CompletableFuture<Void> delete() {
        if (getServer().isPresent()) {
            return delete(null);
        } else {
            throw new IllegalArgumentException("The server is not present.");
        }
    }

    @Override
    public CompletableFuture<Void> delete(String reason) {
        if (getServer().isPresent()) {
            return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.SERVER_STICKER)
                    .setUrlParameters(getServer().get().getIdAsString(), String.valueOf(id))
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            throw new IllegalArgumentException("The server is not present.");
        }
    }

    @Override
    public StickerUpdater createUpdater() {
        if (getServer().isPresent()) {
            return new StickerUpdater(getServer().get(), id);
        } else {
            throw new IllegalArgumentException("The server is not present.");
        }
    }
}
