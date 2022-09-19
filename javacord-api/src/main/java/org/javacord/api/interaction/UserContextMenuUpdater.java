package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.UserContextMenuUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class UserContextMenuUpdater
        extends ApplicationCommandUpdater<UserContextMenu, UserContextMenuUpdaterDelegate, UserContextMenuUpdater> {

    //Uncomment once there are MessageContextMenuUpdater specific properties.
    //private final UserContextMenuUpdaterDelegate delegate;

    /**
     * Creates a new user context menu updater.
     *
     * @param commandId The user context menu id which should be updated.
     */
    public UserContextMenuUpdater(long commandId) {
        super(DelegateFactory.createUserContextMenuUpdaterDelegate(commandId));
        //delegate = super.getDelegate();
    }

}
