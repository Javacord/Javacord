package de.btobastian.javacord.entities.message.emoji.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.message.emoji.KnownCustomEmoji;
import de.btobastian.javacord.entities.permissions.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

/**
 * The implementation of {@link CustomEmoji}.
 */
public class ImplKnownCustomEmoji extends ImplCustomEmoji implements KnownCustomEmoji {

    /**
     * The server of the emoji.
     */
    private final Server server;

    /**
     * A list with all whitelisted roles.
     * Might be <code>null</code>!
     */
    private Collection<Role> whitelist;

    /**
     * Whether this emoji must be wrapped in colons or not.
     */
    private boolean requiresColons = true;

    /**
     * Creates a new known custom emoji.
     *
     * @param api The discord api instance.
     * @param server The server of the emoji.
     * @param data The json data of the emoji.
     */
    public ImplKnownCustomEmoji(ImplDiscordApi api, Server server, JsonNode data) {
        super(api, data);
        this.server = server;
        if (data.hasNonNull("roles")) {
            whitelist = new HashSet<>();
            for (JsonNode roleIdJson : data.get("roles")) {
                server.getRoleById(roleIdJson.asLong()).ifPresent(whitelist::add);
            }
        }
        if (data.hasNonNull("require_colons")) {
            requiresColons = data.get("require_colons").asBoolean();
        }
    }

    /**
     * Sets the name of the custom emoji.
     *
     * @param name The name of the custom emoji.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Optional<Collection<Role>> getWhitelistedRoles() {
        return whitelist == null || whitelist.isEmpty() ?
                Optional.empty() :
                Optional.of(Collections.unmodifiableCollection(new HashSet<>(whitelist)));
    }

    @Override
    public boolean requiresColons() {
        return requiresColons;
    }

    @Override
    public String toString() {
        return String.format("KnownCustomEmoji (id: %s, name: %s, animated: %b, server: %s)",
                getId(), getName(), isAnimated(), getServer());
    }
}
