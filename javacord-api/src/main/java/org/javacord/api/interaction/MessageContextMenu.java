package org.javacord.api.interaction;

public interface MessageContextMenu extends ContextMenu {

    /**
     * Creates a message context menu updater from this MessageContextMenu instance.
     *
     * @return The message context menu updater for this MessageContextMenu instance.
     */
    default MessageContextMenuUpdater createMessageContextMenuUpdater() {
        return new MessageContextMenuUpdater(this.getId());
    }

}
