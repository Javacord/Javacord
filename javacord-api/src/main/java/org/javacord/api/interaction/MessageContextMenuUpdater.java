package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.MessageContextMenuUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class MessageContextMenuUpdater
        extends
        ApplicationCommandUpdater<MessageContextMenu, MessageContextMenuUpdaterDelegate, MessageContextMenuUpdater> {

    //Uncomment once there are MessageContextMenuUpdater specific properties.
    //private final MessageContextMenuUpdaterDelegate delegate;

    /**
     * Creates a new message context menu updater.
     *
     * @param commandId The message context menu id which should be updated.
     */
    public MessageContextMenuUpdater(long commandId) {
        super(DelegateFactory.createMessageContextMenuUpdaterDelegate(commandId));
        //delegate = super.getDelegate();
    }

}
