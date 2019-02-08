package org.javacord.core.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;
import org.javacord.core.util.FileContainer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public abstract class EmbedDraftFileContainerAttachableMember<D extends EmbedDraftMember, S extends SentEmbedMember>
        extends EmbedDraftMemberImpl<D, S> {
    protected String fileUri;
    protected FileContainer container;

    protected EmbedDraftFileContainerAttachableMember(EmbedDraft parent, Class<D> dClass, Class<S> sClass) {
        super(parent, dClass, sClass);
    }

    public boolean hasAttachment() {
        return container != null;
    }

    public FileContainer getContainer() {
        return container;
    }

    public void attachContainer(FileContainer container) {
        if (container == null) return;
        this.container = container;
        this.fileUri = "attachment://"+container.getFileTypeOrName();
    }
}
