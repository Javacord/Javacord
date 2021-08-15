package org.javacord.api.entity.message.component;

import org.javacord.api.util.Specializable;
import java.util.Optional;

public interface HighLevelComponent extends Component, Specializable<HighLevelComponent> {

    /**
     * Whether this component is of this type.
     *
     * @return True if it's of that type.
     */
    default boolean isActionRow() {
        return getType() == ComponentType.ACTION_ROW;
    }

    /**
     * Gets the component as an ActionRow if it's of that type.
     *
     * @return The ActionRow.
     */
    default Optional<ActionRow> asActionRow() {
        return isActionRow() ? Optional.of((ActionRow) this) : Optional.empty();
    }

}
