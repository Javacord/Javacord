package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.UserContextMenuBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

/**
 * This class is used to create new user context menus.
 */
public class UserContextMenuBuilder
        extends ApplicationCommandBuilder<UserContextMenu, UserContextMenuBuilderDelegate, UserContextMenuBuilder> {

    //Uncomment once there are UserContextMenuBuilder specific properties.
    //private final UserContextMenuBuilderDelegate delegate;

    /**
     * Creates a new user context menu builder.
     */
    public UserContextMenuBuilder() {
        super(DelegateFactory.createUserContextMenuBuilderDelegate());
        //delegate = super.getDelegate();
    }

}
