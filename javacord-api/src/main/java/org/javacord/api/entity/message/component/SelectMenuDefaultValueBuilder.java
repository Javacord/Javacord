package org.javacord.api.entity.message.component;

import org.javacord.api.entity.message.component.internal.SelectMenuDefaultValueBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class SelectMenuDefaultValueBuilder {

    private final SelectMenuDefaultValueBuilderDelegate delegate =
            DelegateFactory.createSelectMenuDefaultValueBuilderDelegate();

    /**
     * Set the id for the select menu default value.
     *
     * @param id The id.
     * @return The builder.
     */
    public SelectMenuDefaultValueBuilder setId(long id) {
        delegate.setId(id);
        return this;
    }

    /**
     * Set the type for the select menu default value.
     *
     * @param type The type.
     * @return The builder.
     */
    public SelectMenuDefaultValueBuilder setType(SelectMenuDefaultValueType type) {
        delegate.setType(type);
        return this;
    }

    /**
     * Creates a {@link SelectMenuDefaultValue} instance with the given values.
     *
     * @return The created select menu default value instance.
     */
    public SelectMenuDefaultValue build() {
        return delegate.build();
    }
}
