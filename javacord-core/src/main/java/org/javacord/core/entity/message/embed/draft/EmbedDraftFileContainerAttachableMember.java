package org.javacord.core.entity.message.embed.draft;

import org.javacord.core.util.FileContainer;

public abstract class EmbedDraftFileContainerAttachableMember {
    protected String fileUri;
    protected FileContainer container;

    public boolean hasAttachment() {
        return container != null;
    }

    public FileContainer getContainer() {
        return container;
    }

    public void attachContainer(FileContainer container) {
        if (container == null) return;
        this.container = container;
        this.fileUri = "attachment://" + container.getFileTypeOrName();
    }
}
