package de.btobastian.javacord.entity.emoji;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entity.emoji.impl.ImplKnownCustomEmoji;
import de.btobastian.javacord.entity.permission.Role;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
     * The reason for the update.
     */
    private String reason = null;

    /**
     * The name to update.
     */
    private String name = null;

    /**
     * The whitelist to update.
     */
    private Collection<Role> whitelist = null;

    /**
     * Whether the whitelist should be updated or not.
     */
    private boolean updateWhitelist = false;

    /**
     * Creates a new custom emoji updater.
     *
     * @param emoji The custom emoji to update.
     */
    public CustomEmojiUpdater(KnownCustomEmoji emoji) {
        this.emoji = (ImplKnownCustomEmoji) emoji;
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
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
     * Queues a role to be added to the whitelist.
     *
     * @param role The role to add.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater addRoleToWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = emoji.getWhitelistedRoles().map(HashSet::new).orElseGet(HashSet::new);
        }
        updateWhitelist = true;
        whitelist.add(role);
        return this;
    }

    /**
     * Queues a role to be removed from the whitelist.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param role The role to remove.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater removeRoleFromWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = emoji.getWhitelistedRoles().map(HashSet::new).orElseGet(HashSet::new);
        }
        updateWhitelist = true;
        whitelist.remove(role);
        return this;
    }

    /**
     * Queues the whitelist to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater removeWhitelist() {
        updateWhitelist = true;
        whitelist = null;
        return this;
    }

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater setWhitelist(Collection<Role> roles) {
        updateWhitelist = true;
        whitelist = roles == null ? null : new HashSet<>(roles);
        return this;
    }

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    public CustomEmojiUpdater setWhitelist(Role... roles) {
        return setWhitelist(roles == null ? null : Arrays.asList(roles));
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
        if (updateWhitelist) {
            if (whitelist == null) {
                body.putNull("roles");
            } else {
                ArrayNode jsonRoles = body.putArray("roles");
                whitelist.stream().map(Role::getIdAsString).forEach(jsonRoles::add);
            }
            patchEmoji = true;
        }
        // Only make a REST call, if we really want to update something
        if (patchEmoji) {
            return new RestRequest<Void>(emoji.getApi(), RestMethod.PATCH, RestEndpoint.CUSTOM_EMOJI)
                    .setUrlParameters(emoji.getServer().getIdAsString(), emoji.getIdAsString())
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        }
        return CompletableFuture.completedFuture(null);
    }

}
