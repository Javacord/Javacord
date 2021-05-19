package org.javacord.core.event.command;

import org.javacord.api.command.Interaction;
import org.javacord.api.entity.message.InteractionMessageBuilder;
import org.javacord.api.event.command.InteractionCreateEvent;
import org.javacord.core.entity.message.InteractionCallbackType;
import org.javacord.core.event.EventImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link InteractionCreateEventImpl}.
 */
public class InteractionCreateEventImpl extends EventImpl implements InteractionCreateEvent {

    private static final String RESPOND_LATER_BODY =
            "{\"type\": " + InteractionCallbackType.DeferredChannelMessageWithSource.getId() + "}";

    private final Interaction interaction;

    /**
     * Creates a new interaction create event.
     *
     * @param interaction The created interaction.
     */
    public InteractionCreateEventImpl(Interaction interaction) {
        super(interaction.getApi());
        this.interaction = interaction;
    }

    @Override
    public Interaction getInteraction() {
        return interaction;
    }

    @Override
    public InteractionMessageBuilder respond() {
        return new InteractionMessageBuilder();
    }

    @Override
    public CompletableFuture<Void> respondLater() {
        return new RestRequest<Void>(this.api, RestMethod.POST, RestEndpoint.INTERACTION_RESPONSE)
                .setUrlParameters(interaction.getIdAsString(), interaction.getToken())
                .setBody(RESPOND_LATER_BODY)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> deleteInitialResponse() {
        return new RestRequest<Void>(interaction.getApi(),
                RestMethod.DELETE, RestEndpoint.ORIGINAL_INTERACTION_RESPONSE)
                .setUrlParameters(Long.toUnsignedString(interaction.getApplicationId()), interaction.getToken())
                .execute(result -> null);
    }
}
