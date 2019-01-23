package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.sent.SentEmbedAuthor;
import org.javacord.core.util.JsonNodeable;

import java.net.URL;
import java.util.Optional;

public class EmbedDraftAuthorImpl extends EmbedDraftFileContainerAttachableMember<EmbedDraftAuthor, SentEmbedAuthor>
        implements EmbedDraftAuthor, JsonNodeable {
    private String name;
    private URL url;
    private URL icon;

    protected EmbedDraftAuthorImpl(EmbedDraft parent, String name, URL url, URL iconUrl) {
        super(parent, EmbedDraftAuthor.class, SentEmbedAuthor.class);

        this.name = name;
        this.url = url;
        this.icon = iconUrl;
    }

    public EmbedDraftAuthorImpl(BaseEmbed parent, BaseEmbedAuthor baseEmbedAuthor) {
        this(parent.toEmbedDraft(),
                baseEmbedAuthor.getName(),
                baseEmbedAuthor.getUrl().orElse(null),
                baseEmbedAuthor.getUrl().orElse(null));
    }

    public EmbedDraftAuthorImpl(EmbedDraft parent, String name, String url, String iconUrl) {
        this(parent, name, urlOrNull(url), urlOrNull(iconUrl));
    }

    @Override
    public EmbedDraftAuthor setUrl(URL url) {
        this.url = url;
        return this;
    }

    @Override
    public EmbedDraftAuthor setIconUrl(URL url) {
        this.icon = url;
        return this;
    }

    @Override
    public Optional<URL> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public Optional<URL> getIconUrl() {
        return Optional.ofNullable(icon);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String applyToNode(final ObjectNode frame) {
        final String jsonFieldName = "author";
        ObjectNode node = frame.putObject(jsonFieldName);

        node.put("name", name);
        getUrl().ifPresent(url -> node.put("url", url.toExternalForm()));

        Optional<String> iconUrl = getAttachmentUrlAsString();
        if (!iconUrl.isPresent()) {
            iconUrl = getIconUrl().map(URL::toExternalForm);
        }
        iconUrl.ifPresent(url -> node.put("icon_url", url));
        return jsonFieldName;
    }
}
