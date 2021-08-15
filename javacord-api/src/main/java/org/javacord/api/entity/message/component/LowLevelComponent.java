package org.javacord.api.entity.message.component;

import org.javacord.api.util.Specializable;
import java.util.Optional;

public interface LowLevelComponent extends Component, Specializable<LowLevelComponent> {

    /**
     * Whether this component is of this type.
     *
     * @return True if it's of that type.
     */
    default boolean isButton() {
        return getType() == ComponentType.BUTTON;
    }

    /**
     * Gets the component as a Button if it's of that type.
     *
     * @return The Button.
     */
    default Optional<Button> asButton() {
        return isButton() ? Optional.of((Button) this) : Optional.empty();
    }

    /**
     * Whether this component is of this type.
     *
     * @return True if it's of that type.
     */
    default boolean isSelectMenu() {
        return getType() == ComponentType.SELECT_MENU;
    }

    /**
     * Gets the component as a SelectMenu if it's of that type.
     *
     * @return The SelectMenu.
     */
    default Optional<SelectMenu> asSelectMenu() {
        return isSelectMenu() ? Optional.of((SelectMenu) this) : Optional.empty();
    }

}
