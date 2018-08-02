package org.javacord.api.entity.permission;

import org.javacord.api.entity.permission.internal.PermissionsBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

/**
 * A class to create {@link Permissions permissions} objects.
 */
public class PermissionsBuilder {

    /**
     * The permissions delegate used by this instance.
     */
    private final PermissionsBuilderDelegate delegate;

    /**
     * Creates a new permissions builder with all types set to {@link PermissionState#UNSET}.
     */
    public PermissionsBuilder() {
        delegate = DelegateFactory.createPermissionsBuilderDelegate();
    }

    /**
     * Creates a new permissions builder with the states of the given permissions object.
     *
     * @param permissions The permissions which should be copied.
     */
    public PermissionsBuilder(Permissions permissions) {
        delegate = DelegateFactory.createPermissionsBuilderDelegate(permissions);
    }

    /**
     * Sets the new state of the given type.
     *
     * @param type The type to change.
     * @param state The state to set.
     * @return The current instance in order to chain call methods.
     */
    public PermissionsBuilder setState(PermissionType type, PermissionState state) {
        delegate.setState(type, state);
        return this;
    }

    /**
     * Sets the state for the given types to {@link PermissionState#ALLOWED}.
     *
     * @param types The types to change.
     * @return The current instance in order to chain call methods.
     */
    public PermissionsBuilder setAllowed(PermissionType... types) {
        for (PermissionType type : types) {
            setState(type, PermissionState.ALLOWED);
        }
        return this;
    }

    /**
     * Sets all states to {@link PermissionState#ALLOWED}.
     *
     * @return The current instance in order to chain call methods.
     */
    public PermissionsBuilder setAllAllowed() {
        return setAllowed(PermissionType.values());
    }

    /**
     * Sets the state for the given types to {@link PermissionState#DENIED}.
     *
     * @param types The types to change.
     * @return The current instance in order to chain call methods.
     */
    public PermissionsBuilder setDenied(PermissionType... types) {
        for (PermissionType type : types) {
            setState(type, PermissionState.DENIED);
        }
        return this;
    }

    /**
     * Sets all states to {@link PermissionState#DENIED}.
     *
     * @return The current instance in order to chain call methods.
     */
    public PermissionsBuilder setAllDenied() {
        return setDenied(PermissionType.values());
    }

    /**
     * Sets the state for the given types to {@link PermissionState#UNSET}.
     *
     * @param types The types to change.
     * @return The current instance in order to chain call methods.
     */
    public PermissionsBuilder setUnset(PermissionType... types) {
        for (PermissionType type : types) {
            setState(type, PermissionState.UNSET);
        }
        return this;
    }

    /**
     * Sets all states to {@link PermissionState#UNSET}.
     *
     * @return The current instance in order to chain call methods.
     */
    public PermissionsBuilder setAllUnset() {
        return setUnset(PermissionType.values());
    }

    /**
     * Gets the state of the given type.
     *
     * @param type The type to check.
     * @return The state of the given type.
     */
    public PermissionState getState(PermissionType type) {
        return delegate.getState(type);
    }

    /**
     * Creates a {@link Permissions} instance with the given values.
     *
     * @return The created permissions instance.
     */
    public Permissions build() {
        return delegate.build();
    }

}
