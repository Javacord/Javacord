package org.javacord.core.entity.permission;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.permission.internal.RoleUpdaterDelegate;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link RoleUpdaterDelegate}.
 */
public class RoleUpdaterDelegateImpl implements RoleUpdaterDelegate {

    /**
     * The role to update.
     */
    private final Role role;

    /**
     * The reason for the update.
     */
    private String reason = null;

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
     * The icon to update.
     */
    private FileContainer icon = null;

    /**
     * Whether the icon should be updated or not.
     */
    protected boolean updateIcon = false;

    /**
     * Creates a new role updater delegate.
     *
     * @param role The role to update.
     */
    public RoleUpdaterDelegateImpl(Role role) {
        this.role = role;
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
    public void setDisplaySeparatelyFlag(boolean displaySeparately) {
        this.displaySeparately = displaySeparately;
    }

    @Override
    public void setMentionableFlag(boolean mentionable) {
        this.mentionable = mentionable;
    }

    @Override
    public void setIcon(BufferedImage icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
    }

    @Override
    public void setIcon(BufferedImage icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
    }

    @Override
    public void setIcon(File icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
    }

    @Override
    public void setIcon(URL icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon);
        updateIcon = true;
    }

    @Override
    public void setIcon(byte[] icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
    }

    @Override
    public void setIcon(byte[] icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
    }

    @Override
    public void setIcon(InputStream icon) {
        this.icon = (icon == null) ? null : new FileContainer(icon, "png");
        updateIcon = true;
    }

    @Override
    public void setIcon(InputStream icon, String fileType) {
        this.icon = (icon == null) ? null : new FileContainer(icon, fileType);
        updateIcon = true;
    }

    @Override
    public void removeIcon() {
        this.icon = null;
        updateIcon = true;
    }

    @Override
    public CompletableFuture<Void> update() {
        boolean patchRole = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
            patchRole = true;
        }
        if (permissions != null) {
            body.put("permissions", permissions.getAllowedBitmask());
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
        if (updateIcon) {
            if (icon == null) {
                body.putNull("icon");
            }
            patchRole = true;
        }
        if (patchRole) {
            if (icon != null) {
                return icon.asByteArray(role.getApi()).thenAccept(bytes -> {
                    String base64Icon = "data:image/" + icon.getFileType() + ";base64,"
                            + Base64.getEncoder().encodeToString(bytes);
                    body.put("icon", base64Icon);
                }).thenCompose(aVoid ->
                        new RestRequest<Void>(role.getApi(), RestMethod.PATCH, RestEndpoint.ROLE)
                                .setUrlParameters(role.getServer().getIdAsString(), role.getIdAsString())
                                .setBody(body)
                                .setAuditLogReason(reason)
                                .execute(result -> null));
            }
            return new RestRequest<Void>(role.getApi(), RestMethod.PATCH, RestEndpoint.ROLE)
                    .setUrlParameters(role.getServer().getIdAsString(), role.getIdAsString())
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

}
