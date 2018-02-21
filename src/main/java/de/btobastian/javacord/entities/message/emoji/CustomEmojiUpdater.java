package de.btobastian.javacord.entities.message.emoji;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entities.message.emoji.impl.ImplKnownCustomEmoji;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update known custom emojis.
 */
public class CustomEmojiUpdater {

    /**
     * The custom emoji to update.
     */
    private final ImplKnownCustomEmoji emoji;

    /**
     * The name to update.
     */
    private String name = null;

    /**
     * Creates a new custom emoji updater.
     *
     * @param emoji The custom emoji to update.
     */
    public CustomEmojiUpdater(KnownCustomEmoji emoji) {
        this.emoji = (ImplKnownCustomEmoji) emoji;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the emoji.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        // Server settings
        boolean patchEmoji = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
            patchEmoji = true;
        }
        // Only make a REST call, if we really want to update something
        if (patchEmoji) {
            return new RestRequest<Void>(emoji.getApi(), RestMethod.PATCH, RestEndpoint.CUSTOM_EMOJI)
                    .setUrlParameters(emoji.getServer().getIdAsString(), emoji.getIdAsString())
                    .setBody(body)
                    .execute(result -> null);
        }
        return CompletableFuture.completedFuture(null);
    }

}
