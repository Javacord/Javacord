package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedField;

/**
 * Draft representation of an embed's field.
 */
public interface EmbedDraftField extends BaseEmbedField {

    @Override
    EmbedDraft getEmbed();

    /**
     * Sets the name of the field.
     *
     * @param name The name to set.
     * @return This instance of the field.
     */
    EmbedDraftField setName(String name);

    /**
     * Sets the value of the field.
     *
     * @param value The value to set.
     * @return This instance of the field.
     */
    EmbedDraftField setValue(String value);

    /**
     * Sets whether the field should be inline.
     *
     * @param inline Whether the field should be inline.
     * @return This instance of the field.
     */
    EmbedDraftField setInline(boolean inline);

}
