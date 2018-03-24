package org.javacord.core.entity.permission;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.permission.internal.RoleBuilderDelegate;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link RoleBuilderDelegate}.
 */
public class RoleBuilderDelegateImpl implements RoleBuilderDelegate {

    /**
     * The server of the role.
     */
    private final ServerImpl server;

    /**
     * The reason for the creation.
     */
    private String reason = null;

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
     * Creates a new role builder delegate for the given server.
     *
     * @param server The server for which the role should be created.
     */
    public RoleBuilderDelegateImpl(ServerImpl server) {
        this.server = server;
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
    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setMentionable(boolean mentionable) {
        this.mentionable = mentionable;
    }

    @Override
    public void setDisplaySeparately(boolean displaySeparately) {
        this.displaySeparately = displaySeparately;
    }

    @Override
    public CompletableFuture<Role> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
        }
        if (permissions != null) {
            body.put("permissions", permissions.getAllowedBitmask());
        }
        if (color != null) {
            body.put("color", color.getRGB() & 0xFFFFFF);
        }
        body.put("mentionable", mentionable);
        body.put("hoist", displaySeparately);
        return new RestRequest<Role>(server.getApi(), RestMethod.POST, RestEndpoint.ROLE)
                .setUrlParameters(server.getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> server.getOrCreateRole(result.getJsonBody()));
    }

}
