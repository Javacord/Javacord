package org.javacord.api.interaction.callback;

import org.javacord.api.entity.Deletable;
import org.javacord.api.entity.message.Message;

import java.util.concurrent.CompletableFuture;

public interface InteractionOriginalResponseUpdater
        extends ExtendedInteractionMessageBuilderBase<InteractionOriginalResponseUpdater>, Deletable {
    /**
     * Updates your initial response to the interaction.
     * Note: You can not update the message using any of the edit methods on the message entity as
     *     these are webhook messages.
     * Please re-use this updater to apply further updates on the message.
     *
     * @return the message object representing the edited message
     */
    CompletableFuture<Message> update();

}
