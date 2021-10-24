package org.javacord.core.entity.sticker;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.sticker.internal.StickerUpdaterDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

public class StickerUpdaterDelegateImpl implements StickerUpdaterDelegate {

    private final Logger logger = LoggerUtil.getLogger(StickerUpdaterDelegateImpl.class);
    private final ServerImpl server;
    private final DiscordApiImpl api;
    private final long id;

    private String name;
    private String description;
    private String tags;

    /**
     * Creates a new implementation of a sticker updater delegate.
     *
     * @param server The server that owns the sticker.
     * @param id The ID of the sticker.
     */
    public StickerUpdaterDelegateImpl(ServerImpl server, long id) {
        this.server = server;
        this.api = (DiscordApiImpl) server.getApi();
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public CompletableFuture<Sticker> update() {
        return update(null);
    }

    @Override
    public CompletableFuture<Sticker> update(String reason) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();

        if (name != null) {
            body.put("name", name);
        }
        if (description != null) {
            body.put("description", description);
        }
        if (tags != null) {
            body.put("tags", tags);
        }

        return new RestRequest<Sticker>(api, RestMethod.PATCH, RestEndpoint.SERVER_STICKER)
                .setUrlParameters(server.getIdAsString(), String.valueOf(id))
                .setAuditLogReason(reason)
                .setBody(body)
                .execute(result -> new StickerImpl(api, result.getJsonBody()));
    }
}
