package org.javacord.api.interaction;

import org.javacord.api.entity.message.component.HighLevelComponent;
import java.util.List;
import java.util.Optional;

public interface ModalInteraction extends InteractionBase {

    /**
     * Get the custom id of the select menu.
     *
     * @return The custom ID.
     */
    String getCustomId();

    /**
     * Get the components of the modal.
     *
     * @return The components.
     */
    List<HighLevelComponent> getComponents();

    /**
     * Gets the values of each text input.
     * This is a shorthand method to avoid iterating the nested components by yourself.
     *
     * @return The values of the text input components.
     */
    List<String> getTextInputValues();

    /**
     * Get the value of a text input by its custom id.
     * This is a shorthand method to avoid iterating the nested components by yourself.
     *
     * @param customId The custom ID of the component.
     * @return The value of the text input component with the id.
     */
    Optional<String> getTextInputValueByCustomId(String customId);

}
