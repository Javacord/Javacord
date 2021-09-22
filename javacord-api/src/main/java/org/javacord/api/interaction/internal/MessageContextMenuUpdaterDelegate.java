package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.MessageContextMenu;

public interface MessageContextMenuUpdaterDelegate extends ApplicationCommandUpdaterDelegate<MessageContextMenu> {

    /**
     * Sets the new name of the message context menu.
     *
     * @param name The name to set.
     */
    void setName(String name);

    /**
     * Sets the new application command default permission.
     *
     * @param defaultPermission The default permission to set.
     */
    void setDefaultPermission(boolean defaultPermission);
}
