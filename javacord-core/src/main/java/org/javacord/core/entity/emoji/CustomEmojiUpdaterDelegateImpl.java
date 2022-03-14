package org.javacord.core.entity.emoji;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.emoji.internal.CustomEmojiUpdaterDelegate;
import org.javacord.api.entity.permission.Role;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link CustomEmojiUpdaterDelegate}.
 */
public class CustomEmojiUpdaterDelegateImpl implements CustomEmojiUpdaterDelegate {

    /**
     * The custom emoji to update.
     */
    private final KnownCustomEmoji emoji;

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
    private Set<Role> whitelist = null;

    /**
     * Whether the whitelist should be updated or not.
     */
    private boolean updateWhitelist = false;

    /**
     * Creates a new custom emoji updater delegate.
     *
     * @param emoji The custom emoji to update.
     */
    public CustomEmojiUpdaterDelegateImpl(KnownCustomEmoji emoji) {
        this.emoji = emoji;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addRoleToWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = emoji.getWhitelistedRoles().map(HashSet::new).orElseGet(HashSet::new);
        }
        updateWhitelist = true;
        whitelist.add(role);
    }

    @Override
    public void removeRoleFromWhitelist(Role role) {
        if (whitelist == null) {
            whitelist = emoji.getWhitelistedRoles().map(HashSet::new).orElseGet(HashSet::new);
        }
        updateWhitelist = true;
        whitelist.remove(role);
    }

    @Override
    public void removeWhitelist() {
        updateWhitelist = true;
        whitelist = null;
    }

    @Override
    public void setWhitelist(Collection<Role> roles) {
        updateWhitelist = true;
        whitelist = roles == null ? null : new HashSet<>(roles);
    }

    @Override
    public void setWhitelist(Role... roles) {
        setWhitelist(roles == null ? null : Arrays.asList(roles));
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
