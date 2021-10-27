package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.MessageContextMenuBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

/**
 * This class is used to create new message context menus.
 */
public class MessageContextMenuBuilder extends
        ApplicationCommandBuilder<MessageContextMenu, MessageContextMenuBuilderDelegate, MessageContextMenuBuilder> {

    //Uncomment once there are MessageContextMenuBuilder specific properties.
    //private final MessageContextMenuBuilderDelegate delegate;

    /**
     * Creates a new message context menu builder.
     */
    public MessageContextMenuBuilder() {
        super(DelegateFactory.createMessageContextMenuBuilderDelegate());
        //delegate = super.getDelegate();
    }

}
