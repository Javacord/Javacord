package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedField;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftField;
import org.javacord.core.entity.message.embed.EmbedFieldImpl;
import org.javacord.core.util.JsonNodeable;

public class EmbedDraftFieldImpl extends EmbedFieldImpl implements EmbedDraftField, JsonNodeable {
    private final EmbedDraft parent;

    public EmbedDraftFieldImpl(EmbedDraft parent, String name, String value, boolean inline) {
        super(name, value, inline);
        this.parent = parent;
    }

    public EmbedDraftFieldImpl(BaseEmbed parent, BaseEmbedField baseEmbedField) {
        this(parent.toEmbedDraft(), baseEmbedField.getName(), baseEmbedField.getValue(), baseEmbedField.isInline());
    }

    @Override
    public EmbedDraft getEmbed() {
        return parent;
    }

    @Override
    public EmbedDraftField setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public EmbedDraftField setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public EmbedDraftField setInline(boolean inline) {
        this.inline = inline;
        return this;
    }

    @Override
    public EmbedDraftField toEmbedDraftField() {
        return this;
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "fields";
        ArrayNode node = frame.withArray(jsonFieldName);
        ObjectNode self = node.addObject();

        self.put("name", name)
                .put("value", value)
                .put("inline", inline);

        return jsonFieldName;
    }
}
