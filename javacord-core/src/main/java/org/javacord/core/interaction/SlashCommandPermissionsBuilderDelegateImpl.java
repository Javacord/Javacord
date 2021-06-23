package org.javacord.core.interaction;

import org.javacord.api.interaction.SlashCommandPermissionType;
import org.javacord.api.interaction.SlashCommandPermissions;
import org.javacord.api.interaction.internal.SlashCommandPermissionsBuilderDelegate;

import java.util.Objects;

public class SlashCommandPermissionsBuilderDelegateImpl implements SlashCommandPermissionsBuilderDelegate {

    private Long id;
    private SlashCommandPermissionType type;
    private Boolean permission;

    /**
     * Class constructor.
     */
    public SlashCommandPermissionsBuilderDelegateImpl() {
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setType(SlashCommandPermissionType type) {
        Objects.requireNonNull(type,"The type can not be null!");
        this.type = type;
    }

    @Override
    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    @Override
    public SlashCommandPermissions build() {
        if (null == type) {
            throw new IllegalStateException("Type can not be null!");
        } else if (null == permission) {
            throw new IllegalStateException("Permission can not be null!");
        } else if (null == id) {
            throw new IllegalStateException("ID can not be null!");
        }
        return new SlashCommandPermissionsImpl(id, type, permission);
    }
}
