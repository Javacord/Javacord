package org.javacord.core.entity.message.embed.draft;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.core.util.JsonNodeable;

import java.util.Optional;

public class EmbedDraftAuthorImpl extends EmbedDraftFileContainerAttachableMember
        implements EmbedDraftAuthor, JsonNodeable {
    private final EmbedDraft parent;
    private String name;
    private String url;

    protected EmbedDraftAuthorImpl(EmbedDraft parent, String name, String url, String iconUrl) {
        this.parent = parent;

        this.name = name;
        this.url = url;
        this.fileUri = iconUrl;
    }

    public EmbedDraftAuthorImpl(BaseEmbed parent, BaseEmbedAuthor baseEmbedAuthor) {
        this(
                parent.toEmbedDraft(),
                baseEmbedAuthor.getName(),
                baseEmbedAuthor.getUrl().orElse(null),
                baseEmbedAuthor.getUrl().orElse(null)
        );
    }

    @Override
    public EmbedDraft getEmbed() {
        return parent;
    }

    @Override
    public EmbedDraftAuthor setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public EmbedDraftAuthor setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public EmbedDraftAuthor setIconUrl(String url) {
        this.fileUri = url;
        return this;
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public Optional<String> getIconUrl() {
        return Optional.ofNullable(fileUri);
    }

    @Override
    public EmbedDraftAuthor toEmbedDraftAuthor() {
        return this;
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
        node.put("url", url);
        node.put("icon_url", fileUri);

        return jsonFieldName;
    }
}
