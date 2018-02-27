package de.btobastian.javacord.entity.permission;

import de.btobastian.javacord.entity.permission.impl.ImplPermissions;

/**
 * A class to create {@link Permissions permissions} objects.
 */
public class PermissionsBuilder {

    private int allowed = 0;
    private int denied = 0;

    /**
     * Creates a new permissions builder with all types set to {@link PermissionState#NONE}.
     */
    public PermissionsBuilder() { }

    /**
     * Creates a new permissions builder with the states of the given permissions object.
     *
     * @param permissions The permissions which should be copied.
     */
    public PermissionsBuilder(Permissions permissions) {
        allowed = ((ImplPermissions) permissions).getAllowed();
        denied = ((ImplPermissions) permissions).getDenied();
    }

    /**
     * Sets the new state of the given type.
     *
     * @param type The type to change.
     * @param state The state to set.
     * @return The current instance in order to chain call methods.
     */
    public PermissionsBuilder setState(PermissionType type, PermissionState state) {
        switch (state) {
            case ALLOWED:
                allowed = type.set(allowed, true);
                denied = type.set(denied, false);
                break;
            case DENIED:
                allowed = type.set(allowed, false);
                denied = type.set(denied, true);
                break;
            case NONE:
                allowed = type.set(allowed, false);
                denied = type.set(denied, false);
                break;
        }
        return this;
    }

    /**
     * Gets the state of the given type.
     *
     * @param type The type to check.
     * @return The state of the given type.
     */
    public PermissionState getState(PermissionType type) {
        if (type.isSet(allowed)) {
            return PermissionState.ALLOWED;
        }
        if (type.isSet(denied)) {
            return PermissionState.DENIED;
        }
        return PermissionState.NONE;
    }

    /**
     * Creates a {@link Permissions} instance with the given values.
     *
     * @return The created permissions instance.
     */
    public Permissions build() {
        return new ImplPermissions(allowed, denied);
    }

}
