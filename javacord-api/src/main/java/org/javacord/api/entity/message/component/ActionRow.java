package org.javacord.api.entity.message.component;

import java.util.List;

/**
 * This interface represents an ActionRow component.
 */
public interface ActionRow extends HighLevelComponent {
    /**
     * Get all the components of the action row.
     *
     * @return A list which holds components.
     */
    List<LowLevelComponent> getComponents();

    /**
     * Create a new action row containing the given low level components, e.g. buttons.
     *
     * @param lowLevelComponents The low level components to add to the row.
     * @return the new action row builder to be used with a message
     */
    static ActionRow of(LowLevelComponent... lowLevelComponents) {
        return new ActionRowBuilder()
            .addComponents(lowLevelComponents)
            .build();
    }

    /**
     * Create a new action row containing the given list with low level components, e.g. buttons.
     *
     * @param lowLevelComponents The low level components to add to the row.
     * @return the new action row builder to be used with a message
     */
    static ActionRow of(List<LowLevelComponent> lowLevelComponents) {
        return new ActionRowBuilder()
            .addComponents(lowLevelComponents)
            .build();
    }
}
