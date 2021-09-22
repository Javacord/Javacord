package org.javacord.core.interaction;

import org.javacord.api.interaction.ApplicationCommandPermissionType;
import org.javacord.api.interaction.ApplicationCommandPermissions;
import org.javacord.api.interaction.internal.ApplicationCommandPermissionsBuilderDelegate;

import java.util.Objects;

public class ApplicationCommandPermissionsBuilderDelegateImpl implements ApplicationCommandPermissionsBuilderDelegate {

    private Long id;
    private ApplicationCommandPermissionType type;
    private Boolean permission;

    /**
     * Class constructor.
     */
    public ApplicationCommandPermissionsBuilderDelegateImpl() {
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setType(ApplicationCommandPermissionType type) {
        Objects.requireNonNull(type,"The type can not be null!");
        this.type = type;
    }

    @Override
    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    @Override
    public ApplicationCommandPermissions build() {
        if (null == type) {
            throw new IllegalStateException("Type can not be null!");
        } else if (null == permission) {
            throw new IllegalStateException("Permission can not be null!");
        } else if (null == id) {
            throw new IllegalStateException("ID can not be null!");
        }
        return new ApplicationCommandPermissionsImpl(id, type, permission);
    }
}
