package org.javacord.core.entity.message.embed;

import org.javacord.api.entity.message.embed.BaseEmbedField;

/**
 * The implementation of {@link BaseEmbedField}.
 */
public abstract class EmbedFieldImpl implements BaseEmbedField {

    private final Updater updater;
    protected String name;
    protected String value;
    protected boolean inline;

    /**
     * Creates a new embed field.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether or not this field should display inline.
     */
    protected EmbedFieldImpl(String name, String value, boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;

        updater = new Updater();
    }

    public Updater getUpdater() {
        return updater;
    }

    protected class Updater {
        public void setName(String name) {
            EmbedFieldImpl.this.name = name;
        }

        public void setValue(String value) {
            EmbedFieldImpl.this.value = value;
        }

        public void setInline(boolean inline) {
            EmbedFieldImpl.this.inline = inline;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInline() {
        return inline;
    }

}
