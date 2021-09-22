package org.javacord.api.interaction;

public interface UserContextMenu extends ContextMenu {

    /**
     * Creates a user context menu updater from this UserContextMenu instance.
     *
     * @return The user context menu updater for this UserContextMenu instance.
     */
    default UserContextMenuUpdater createUserContextMenuUpdater() {
        return new UserContextMenuUpdater(this.getId());
    }
}
