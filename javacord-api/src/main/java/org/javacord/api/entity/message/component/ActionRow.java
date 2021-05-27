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
}
