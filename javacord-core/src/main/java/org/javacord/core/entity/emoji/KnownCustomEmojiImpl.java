package org.javacord.core.entity.emoji;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.listener.server.emoji.InternalKnownCustomEmojiAttachableListenerManager;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link KnownCustomEmoji}.
 */
public class KnownCustomEmojiImpl extends CustomEmojiImpl
        implements KnownCustomEmoji, InternalKnownCustomEmojiAttachableListenerManager {

    /**
     * The server of the emoji.
     */
    private final Server server;

    /**
     * All whitelisted roles.
     * Might be <code>null</code>!
     */
    private volatile Collection<Role> whitelist;

    /**
     * Whether this emoji must be wrapped in colons or not.
     */
    private final boolean requiresColons;

    /**
     * Whether this emoji is managed or not.
     */
    private final boolean managed;

    /**
     * The id of the user who created the emoji. 0 if no user was given.
     */
    private volatile long creatorId;

    /**
     * Creates a new known custom emoji.
     *
     * @param api The discord api instance.
     * @param server The server of the emoji.
     * @param data The json data of the emoji.
     */
    public KnownCustomEmojiImpl(DiscordApiImpl api, Server server, JsonNode data) {
        super(api, data);
        this.server = server;
        if (data.hasNonNull("roles")) {
            whitelist = new HashSet<>();
            for (JsonNode roleIdJson : data.get("roles")) {
                server.getRoleById(roleIdJson.asLong()).ifPresent(whitelist::add);
            }
        }
        requiresColons = !data.hasNonNull("require_colons") || data.get("require_colons").asBoolean();
        creatorId = data.has("user") ? data.get("user").asLong() : 0L;
        managed = data.get("managed").asBoolean(false);
    }

    /**
     * Sets the name of the custom emoji.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the whitelist of the custom emoji.
     *
     * @param whitelist The whitelist to set.
     */
    public void setWhitelist(Collection<Role> whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public CompletableFuture<Void> delete(String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.CUSTOM_EMOJI)
                .setUrlParameters(getServer().getIdAsString(), getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public Optional<Set<Role>> getWhitelistedRoles() {
        return whitelist == null || whitelist.isEmpty()
                ? Optional.empty()
                : Optional.of(Collections.unmodifiableSet(new HashSet<>(whitelist)));
    }

    @Override
    public boolean requiresColons() {
        return requiresColons;
    }

    @Override
    public boolean isManaged() {
        return managed;
    }

    @Override
    public CompletableFuture<Optional<User>> getCreator() {
        if (creatorId == 0L) {
            return new RestRequest<Optional<User>>(getApi(), RestMethod.GET, RestEndpoint.CUSTOM_EMOJI)
                    .setUrlParameters(server.getIdAsString(), this.getIdAsString())
                    .execute(result -> {
                        JsonNode userJson = result.getJsonBody().get("user");
                        if (userJson.isMissingNode()) {
                            return Optional.empty();
                        } else {
                            creatorId = userJson.get("id").asLong();
                            return Optional.of(new UserImpl((DiscordApiImpl) getApi(), userJson));
                        }
                    });
        } else {
            return getApi().getUserById(creatorId).thenApply(Optional::of);
        }
    }

    @Override
    public String toString() {
        return String.format("KnownCustomEmoji (id: %s, name: %s, animated: %b, server: %#s)",
                getIdAsString(), getName(), isAnimated(), getServer());
    }

}
