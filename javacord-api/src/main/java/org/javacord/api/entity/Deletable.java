package org.javacord.api.entity;

import java.util.concurrent.CompletableFuture;

/**
 * An entity that can be deleted.
 */
public interface Deletable {

    /**
     * Deletes the entity.
     *
     * @return A future to tell if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    /**
     * Deletes the entity.
     *
     * @param reason The audit log reason for the deletion.
     * @return A future to tell if the deletion was successful.
     */
    CompletableFuture<Void> delete(String reason);

}
