package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.interaction.AutocompleteInteraction;
import org.javacord.api.interaction.InteractionType;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.InteractionCallbackType;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AutocompleteInteractionImpl extends SlashCommandInteractionImpl implements AutocompleteInteraction {

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public AutocompleteInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);
    }

    @Override
    public InteractionType getType() {
        return InteractionType.APPLICATION_COMMAND_AUTOCOMPLETE;
    }

    @Override
    public CompletableFuture<Void> respondWithChoices(List<SlashCommandOptionChoice> choices) {
        ObjectNode topBody = JsonNodeFactory.instance.objectNode();
        ObjectNode data = JsonNodeFactory.instance.objectNode();
        topBody.put("type", InteractionCallbackType.APPLICATION_COMMAND_AUTOCOMPLETE_RESULT.getId());

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        for (SlashCommandOptionChoice choice : choices) {
            arrayNode.add(((SlashCommandOptionChoiceImpl) choice).toJsonNode());
        }
        data.set("choices", arrayNode);
        topBody.set("data", data);

        return new RestRequest<Void>(getApi(),
                RestMethod.POST, RestEndpoint.INTERACTION_RESPONSE)
                .setUrlParameters(getIdAsString(), getToken())
                .setBody(topBody)
                .execute(result -> null);
    }

    @Override
    public SlashCommandInteractionOption getFocusedOption() {
        return getArguments().stream()
                .filter(slashCommandInteractionOption -> slashCommandInteractionOption.isFocused().orElse(false))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Autocomplete interaction does not have a focused option"));
    }

    /**
     * This method cannot be used by autocomplete interactions.
     *
     * @return Throws an {@link UnsupportedOperationException}.
     */
    @Override
    public InteractionImmediateResponseBuilder createImmediateResponder() {
        throw new UnsupportedOperationException("This method is not supported by this interaction");
    }

    /**
     * This method cannot be used by autocomplete interactions.
     *
     * @return Throws an {@link UnsupportedOperationException}.
     */
    @Override
    public CompletableFuture<InteractionOriginalResponseUpdater> respondLater() {
        throw new UnsupportedOperationException("This method is not supported by this interaction");
    }

    /**
     * This method cannot be used by autocomplete interactions.
     *
     * @return Throws an {@link UnsupportedOperationException}.
     */
    @Override
    public CompletableFuture<InteractionOriginalResponseUpdater> respondLater(boolean ephemeral) {
        throw new UnsupportedOperationException("This method is not supported by this interaction");
    }

    /**
     * This method cannot be used by autocomplete interactions.
     *
     * @return Throws an {@link UnsupportedOperationException}.
     */
    @Override
    public InteractionFollowupMessageBuilder createFollowupMessageBuilder() {
        throw new UnsupportedOperationException("This method is not supported by this interaction");
    }
}
