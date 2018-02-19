package de.btobastian.javacord.entities.permissions;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the settings of a role.
 */
public class RoleUpdater {

    /**
     * The role to update.
     */
    private final Role role;

    /**
     * The name to update.
     */
    private String name = null;

    /**
     * The permissions to update.
     */
    private Permissions permissions = null;

    /**
     * The color to update.
     */
    private Color color = null;

    /**
     * The display separately flag to update.
     */
    private Boolean displaySeparately = null;

    /**
     * The mentionable flag to update.
     */
    private Boolean mentionable = null;

    /**
     * Creates a new role updater.
     *
     * @param role The role to update.
     */
    public RoleUpdater(Role role) {
        this.role = role;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Queues the permissions to be updated.
     *
     * @param permissions The new permissions of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setPermissions(Permissions permissions) {
        this.permissions = permissions;
        return this;
    }

    /**
     * Queues the color to be updated.
     *
     * @param color The new color of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Queues the display separately flag (sometimes called "hoist") to be updated.
     *
     * @param displaySeparately The new display separately flag of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setDisplaySeparatelyFlag(boolean displaySeparately) {
        this.displaySeparately = displaySeparately;
        return this;
    }

    /**
     * Queues the mentionable flag to be updated.
     *
     * @param mentionable The new mentionable flag of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setMentionableFlag(boolean mentionable) {
        this.mentionable = mentionable;
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        boolean patchRole = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
            patchRole = true;
        }
        if (permissions != null) {
            body.put("permissions", ((ImplPermissions) permissions).getAllowed());
            patchRole = true;
        }
        if (color != null) {
            body.put("color", color.getRGB() & 0xFFFFFF);
            patchRole = true;
        }
        if (displaySeparately != null) {
            body.put("hoist", displaySeparately.booleanValue());
            patchRole = true;
        }
        if (mentionable != null) {
            body.put("mentionable", mentionable.booleanValue());
            patchRole = true;
        }
        if (patchRole) {
            return new RestRequest<Void>(role.getApi(), RestMethod.PATCH, RestEndpoint.ROLE)
                    .setUrlParameters(role.getServer().getIdAsString(), role.getIdAsString())
                    .setBody(body)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

}
