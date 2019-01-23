package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.sent.SentEmbedFooter;
import org.javacord.core.util.JsonNodeable;

import java.net.URL;
import java.util.Optional;

public class EmbedDraftFooterImpl extends EmbedDraftFileContainerAttachableMember<EmbedDraftFooter, SentEmbedFooter>
        implements EmbedDraftFooter, JsonNodeable {
    private String text;
    private URL icon;

    protected EmbedDraftFooterImpl(EmbedDraft parent, String text, URL iconUrl) {
        super(parent, EmbedDraftFooter.class, SentEmbedFooter.class);

        this.text = text;
        this.icon = iconUrl;
    }

    public EmbedDraftFooterImpl(BaseEmbed parent, BaseEmbedFooter baseEmbedFooter) {
        this(parent.toEmbedDraft(),
                baseEmbedFooter.getText(),
                baseEmbedFooter.getIconUrl().orElse(null));
    }

    public EmbedDraftFooterImpl(EmbedDraft parent, String text, String url) {
        this(parent, text, urlOrNull(url));
    }

    @Override
    public EmbedDraftFooter setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public EmbedDraftFooter setIconUrl(URL url) {
        this.icon = url;
        return this;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Optional<URL> getIconUrl() {
        return Optional.ofNullable(icon);
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "footer";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("text", text);

        Optional<String> iconUrl = getAttachmentUrlAsString();
        if (!iconUrl.isPresent()) {
            iconUrl = getIconUrl().map(URL::toExternalForm);
        }
        iconUrl.ifPresent(url -> node.put("icon_url", url));

        return jsonFieldName;
    }
}
