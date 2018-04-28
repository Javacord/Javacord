package org.javacord.api.entity.permission;

/**
 * This enum represents the state of a {@link PermissionType}.
 */
public enum PermissionState {

    /**
     * The given {@link PermissionType type} is not set.
     */
    NONE(false, false),

    /**
     * The given {@link PermissionType type} is allowed.
     */
    ALLOWED(true, false),

    /**
     * The given {@link PermissionType type} is denied.
     * This is only for overwritten permissions!
     */
    DENIED(false, true);

    private boolean allowed;
    private boolean denied;

    private PermissionState(boolean allowed, boolean denied) {
        this.allowed = allowed;
        this.denied = denied;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public boolean isDenied() {
        return denied;
    }
}
