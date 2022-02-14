package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.interaction.InteractionType;
import org.javacord.api.interaction.ModalInteraction;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.component.ActionRowImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModalInteractionImpl extends InteractionImpl implements ModalInteraction {

    private final String customId;
    private final List<HighLevelComponent> components = new ArrayList<>();

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public ModalInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);
        JsonNode data = jsonData.get("data");
        customId = data.get("custom_id").asText();

        for (JsonNode jsonNode : data.get("components")) {
            switch (ComponentType.fromId(jsonNode.get("type").asInt())) {
                case ACTION_ROW:
                    components.add(new ActionRowImpl(jsonNode));
                    break;
                default:
                    throw new IllegalStateException("Received a HighLevelComponent not handled in modals");
            }
        }
    }

    @Override
    public InteractionType getType() {
        return InteractionType.MODAL_SUBMIT;
    }

    /**
     * This method cannot be used by modal interactions.
     *
     * @return Throws an {@link UnsupportedOperationException}.
     */
    @Override
    public CompletableFuture<Void> respondWithModal(String customId, String title,
                                                    List<HighLevelComponent> components) {
        throw new UnsupportedOperationException("This method is not supported by this interaction");
    }

    @Override
    public String getCustomId() {
        return customId;
    }

    @Override
    public List<HighLevelComponent> getComponents() {
        return components;
    }

    @Override
    public List<String> getTextInputValues() {
        return getComponents().stream()
                .map(HighLevelComponent::asActionRow)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ActionRow::getComponents)
                .flatMap(Collection::stream)
                .map(LowLevelComponent::asTextInput)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(TextInput::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> getTextInputValueByCustomId(String customId) {
        return getComponents().stream()
                .map(HighLevelComponent::asActionRow)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ActionRow::getComponents)
                .flatMap(Collection::stream)
                .map(LowLevelComponent::asTextInput)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(textInput -> textInput.getCustomId().equals(customId))
                .map(TextInput::getValue)
                .findFirst();
    }

}
