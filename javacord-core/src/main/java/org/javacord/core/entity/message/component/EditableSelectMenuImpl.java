package org.javacord.core.entity.message.component;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.EditableSelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * The implementation of {@link EditableSelectMenu}.
 */
public class EditableSelectMenuImpl implements EditableSelectMenu {

    private SelectMenuImpl delegate;

    /**
     * Creates a new editable select menu.
     *
     * @param selectMenu The select menu to be edited.
     */
    public EditableSelectMenuImpl(SelectMenuImpl selectMenu) {
        delegate = selectMenu;
    }

    /**
     * Clears the delegate of this editable select menu and thus makes this instance unusable.
     */
    public void clearDelegate() {
        delegate = null;
    }

    @Override
    public ComponentType getType() {
        return delegate.getType();
    }

    @Override
    public EnumSet<ChannelType> getChannelTypes() {
        return delegate.getChannelTypes();
    }

    @Override
    public Optional<String> getPlaceholder() {
        return delegate.getPlaceholder();
    }

    @Override
    public String getCustomId() {
        return delegate.getCustomId();
    }

    @Override
    public int getMinimumValues() {
        return delegate.getMinimumValues();
    }

    @Override
    public int getMaximumValues() {
        return delegate.getMaximumValues();
    }

    @Override
    public List<SelectMenuOption> getOptions() {
        return delegate.getOptions();
    }

    @Override
    public boolean isDisabled() {
        return delegate.isDisabled();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        delegate.setPlaceholder(placeholder);
    }

    @Override
    public void setCustomId(String customId) {
        delegate.setCustomId(customId);
    }

    @Override
    public void setMinimumValues(int minimumValues) {
        delegate.setMinimumValues(minimumValues);
    }

    @Override
    public void setMaximumValues(int maximumValues) {
        delegate.setMaximumValues(maximumValues);
    }

    @Override
    public void setDisabled(boolean disabled) {
        delegate.setDisabled(disabled);
    }

    @Override
    public void setOptions(List<SelectMenuOption> options) {
        delegate.setOptions(options);
    }
}
