package org.javacord.entity.emoji.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.emoji.CustomEmojiUpdater;
import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.entity.permission.Role;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link CustomEmojiUpdater}.
 */
public class ImplCustomEmojiUpdater implements CustomEmojiUpdater {

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
    public ImplCustomEmojiUpdater(KnownCustomEmoji emoji) {
        this.emoji = (ImplKnownCustomEmoji) emoji;
    }

    @Override
    public ImplCustomEmojiUpdater setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public ImplCustomEmojiUpdater setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ImplCustomEmojiUpdater addRoleToWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = emoji.getWhitelistedRoles().map(HashSet::new).orElseGet(HashSet::new);
        }
        updateWhitelist = true;
        whitelist.add(role);
        return this;
    }

    @Override
    public ImplCustomEmojiUpdater removeRoleFromWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = emoji.getWhitelistedRoles().map(HashSet::new).orElseGet(HashSet::new);
        }
        updateWhitelist = true;
        whitelist.remove(role);
        return this;
    }

    @Override
    public ImplCustomEmojiUpdater removeWhitelist() {
        updateWhitelist = true;
        whitelist = null;
        return this;
    }

    @Override
    public ImplCustomEmojiUpdater setWhitelist(Collection<Role> roles) {
        updateWhitelist = true;
        whitelist = roles == null ? null : new HashSet<>(roles);
        return this;
    }

    @Override
    public ImplCustomEmojiUpdater setWhitelist(Role... roles) {
        return setWhitelist(roles == null ? null : Arrays.asList(roles));
    }

    @Override
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
