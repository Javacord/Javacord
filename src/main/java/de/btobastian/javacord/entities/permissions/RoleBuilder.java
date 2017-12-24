package de.btobastian.javacord.entities.permissions;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create roles.
 */
public class RoleBuilder {

    /**
     * The server of the role builder.
     */
    private final ImplServer server;

    /**
     * The name of the role.
     */
    private String name = null;

    /**
     * The permissions of the role.
     */
    private Permissions permissions = null;

    /**
     * The color of the role.
     */
    private Color color = null;

    /**
     * Whether the role should be mentionable or not.
     */
    private boolean mentionable = false;

    /**
     * Whether the role should be pinned in the user listing or not.
     */
    private boolean displaySeparately = false;

    /**
     * Creates a new role role builder for the given server.
     *
     * @param server The server for which the role should be created.
     */
    public RoleBuilder(Server server) {
        this.server = (ImplServer) server;
    }

    /**
     * Sets the name of the role.
     * By default it's <code>"new role"</code>.
     *
     * @param name The name of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the permissions of the role.
     * By default it uses the permissions of the @everyone role.
     *
     * @param permissions The permissions to set.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setPermissions(Permissions permissions) {
        this.permissions = permissions;
        return this;
    }

    /**
     * Sets the color of the role.
     *
     * @param color The color of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets if the role is mentionable or not.
     * By default it's set to <code>false</code>.
     *
     * @param mentionable Whether the role should be mentionable or not.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setMentionable(boolean mentionable) {
        this.mentionable = mentionable;
        return this;
    }

    /**
     * Sets if the role should be pinned in the user listing (sometimes called "hoist").
     *
     * @param displaySeparately Whether the role should be pinned in the user listing or not.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setDisplaySeparately(boolean displaySeparately) {
        this.displaySeparately = displaySeparately;
        return this;
    }

    /**
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    public CompletableFuture<Role> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
        }
        if (permissions != null) {
            body.put("permissions", ((ImplPermissions) permissions).getAllowed());
        }
        if (color != null) {
            body.put("color", color.getRGB() & 0xFFFFFF);
        }
        body.put("mentionable", mentionable);
        body.put("hoist", displaySeparately);
        return new RestRequest<Role>(server.getApi(), RestMethod.POST, RestEndpoint.ROLE)
                .setUrlParameters(server.getIdAsString())
                .setBody(body)
                .execute(result -> server.getOrCreateRole(result.getJsonBody()));
    }

}
