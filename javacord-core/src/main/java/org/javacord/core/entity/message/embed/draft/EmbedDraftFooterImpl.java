package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.core.util.JsonNodeable;

import java.util.Optional;

public class EmbedDraftFooterImpl extends EmbedDraftFileContainerAttachableMember
        implements EmbedDraftFooter, JsonNodeable {
    private final EmbedDraft parent;
    private String text;

    protected EmbedDraftFooterImpl(EmbedDraft parent, String text, String iconUrl) {
        this.parent = parent;

        this.text = text;
        this.fileUri = iconUrl;
    }

    public EmbedDraftFooterImpl(BaseEmbed parent, BaseEmbedFooter baseEmbedFooter) {
        this(parent.toEmbedDraft(),
                baseEmbedFooter.getText(),
                baseEmbedFooter.getIconUrl().orElse(null));
    }

    @Override
    public EmbedDraft getEmbed() {
        return parent;
    }

    @Override
    public EmbedDraftFooter setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public EmbedDraftFooter setIconUrl(String url) {
        this.fileUri = url;
        return this;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Optional<String> getIconUrl() {
        return Optional.ofNullable(fileUri);
    }

    @Override
    public EmbedDraftFooter toEmbedDraftFooter() {
        return this;
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "footer";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("text", text);
        node.put("icon_url", fileUri);

        return jsonFieldName;
    }
}
