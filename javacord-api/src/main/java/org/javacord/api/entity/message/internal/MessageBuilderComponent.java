package org.javacord.api.entity.message.internal;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.component.LowLevelComponent;
import java.util.Arrays;
import java.util.List;

public interface MessageBuilderComponent<T extends MessageBuilderComponent<T>> {

    /**
     * Gets the delegate of this message builder.
     *
     * @return The delegate of this message builder.
     */
    MessageBuilderBaseDelegate getDelegate();

    /**
     * Add multiple high level components to the message.
     *
     * @param components The high level components.
     * @return The current instance in order to chain call methods.
     */
    default T addComponents(HighLevelComponent... components) {
        getDelegate().addComponents(Arrays.asList(components));
        return (T) this;
    }

    /**
     * Add multiple high level components to the message.
     *
     * @param components The high level components.
     * @return The current instance in order to chain call methods.
     */
    default T addComponents(List<HighLevelComponent> components) {
        getDelegate().addComponents(components);
        return (T) this;
    }

    /**
     * Add multiple low level components, wrapped in an ActionRow, to the message.
     *
     * @param components The low level components.
     * @return The current instance in order to chain call methods.
     */
    default T addActionRow(LowLevelComponent... components) {
        return addComponents(ActionRow.of(components));
    }

    /**
     * Remove all high-level components from the message.
     *
     * @return The current instance in order to chain call methods.
     */
    default T removeAllComponents() {
        getDelegate().removeAllComponents();
        return (T) this;
    }
}
