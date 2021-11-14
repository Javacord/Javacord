package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.internal.RegularServerChannelUpdaterDelegate;
import org.javacord.api.entity.channel.internal.ServerChannelUpdaterDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of {@link ServerChannelUpdaterDelegate}.
 */
public class RegularServerChannelUpdaterDelegateImpl extends ServerChannelUpdaterDelegateImpl
        implements RegularServerChannelUpdaterDelegate {

    /**
     * The channel to update.
     */
    protected final RegularServerChannel channel;

    /**
     * The position to update.
     */
    protected Integer position = null;

    /**
     * A map with all overwritten user permissions.
     */
    protected Map<Long, Permissions> overwrittenUserPermissions = null;

    /**
     * A map with all overwritten role permissions.
     */
    protected Map<Long, Permissions> overwrittenRolePermissions = null;

    /**
     * Creates a new server channel updater delegate.
     *
     * @param channel The channel to update.
     */
    public RegularServerChannelUpdaterDelegateImpl(RegularServerChannel channel) {
        super(channel);
        this.channel = channel;
    }


    @Override
    public void setRawPosition(int rawPosition) {
        this.position = rawPosition;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> void addPermissionOverwrite(T permissionable,
                                                                                  Permissions permissions) {
        populatePermissionOverwrites();
        if (permissionable instanceof Role) {
            overwrittenRolePermissions.put(permissionable.getId(), permissions);
        } else if (permissionable instanceof User) {
            overwrittenUserPermissions.put(permissionable.getId(), permissions);
        }
    }

    @Override
    public <T extends Permissionable & DiscordEntity> void removePermissionOverwrite(T permissionable) {
        populatePermissionOverwrites();
        if (permissionable instanceof Role) {
            overwrittenRolePermissions.remove(permissionable.getId());
        } else if (permissionable instanceof User) {
            overwrittenUserPermissions.remove(permissionable.getId());
        }
    }

    @Override
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = super.prepareUpdateBody(body);

        if (position != null) {
            body.put("position", position.intValue());
            patchChannel = true;
        }

        ArrayNode permissionOverwrites = null;
        if (overwrittenUserPermissions != null || overwrittenRolePermissions != null) {
            permissionOverwrites = body.putArray("permission_overwrites");
            patchChannel = true;
        }
        if (overwrittenUserPermissions != null) {
            for (Map.Entry<Long, Permissions> entry : overwrittenUserPermissions.entrySet()) {
                permissionOverwrites.addObject()
                        .put("id", Long.toUnsignedString(entry.getKey()))
                        .put("type", 1)
                        .put("allow", entry.getValue().getAllowedBitmask())
                        .put("deny", entry.getValue().getDeniedBitmask());
            }
        }
        if (overwrittenRolePermissions != null) {
            for (Map.Entry<Long, Permissions> entry : overwrittenRolePermissions.entrySet()) {
                permissionOverwrites.addObject()
                        .put("id", Long.toUnsignedString(entry.getKey()))
                        .put("type", 0)
                        .put("allow", entry.getValue().getAllowedBitmask())
                        .put("deny", entry.getValue().getDeniedBitmask());
            }
        }

        return patchChannel;
    }

    /**
     * Populates the maps which contain the permission overwrites.
     */
    private void populatePermissionOverwrites() {
        if (overwrittenUserPermissions == null) {
            overwrittenUserPermissions = new HashMap<>();
            overwrittenUserPermissions.putAll(
                    ((RegularServerChannelImpl) channel).getInternalOverwrittenUserPermissions());
        }
        if (overwrittenRolePermissions == null) {
            overwrittenRolePermissions = new HashMap<>();
            overwrittenRolePermissions.putAll(
                    ((RegularServerChannelImpl) channel).getInternalOverwrittenRolePermissions());
        }
    }

}
