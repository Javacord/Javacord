package org.javacord.core.entity.message.embed;

import org.javacord.api.entity.message.embed.EditableEmbedField;

/**
 * The implementation of {@link EditableEmbedField}.
 */
public class EditableEmbedFieldImpl implements EditableEmbedField {

    private EmbedFieldImpl delegate;

    /**
     * Creates a new editable embed field.
     *
     * @param field The field to be edited.
     */
    public EditableEmbedFieldImpl(EmbedFieldImpl field) {
        delegate = field;
    }

    /**
     * Clears the delegate of this editable embed field and thus makes this instance unusable.
     */
    public void clearDelegate() {
        delegate = null;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getValue() {
        return delegate.getValue();
    }

    @Override
    public boolean isInline() {
        return delegate.isInline();
    }

    @Override
    public void setName(String name) {
        delegate.setName(name);
    }

    @Override
    public void setValue(String value) {
        delegate.setValue(value);
    }

    @Override
    public void setInline(boolean inline) {
        delegate.setInline(inline);
    }
}
