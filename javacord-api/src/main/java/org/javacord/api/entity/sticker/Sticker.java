package org.javacord.api.entity.sticker;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.server.sticker.StickerAttachableListenerManager;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a Discord sticker object.
 *
 * @see <a href="https://discord.com/developers/docs/resources/sticker#sticker-object-sticker-structure">Discord Docs</a>
 */
public interface Sticker extends DiscordEntity, Nameable, StickerAttachableListenerManager {

    /**
     * Gets the ID of the sticker pack.
     * Only present for standard stickers when they are from a sticker pack.
     *
     * @return The ID of the sticker pack.
     */
    Optional<Long> getPackId();

    /**
     * Gets the description of the sticker.
     *
     * @return The description of the sticker.
     */
    String getDescription();

    /**
     * Tags for autocompletion/suggestion of the sticker. Character limit of 200 characters.
     *
     * @return The tags of the sticker.
     */
    String getTags();

    /**
     * Gets the sticker's type.
     *
     * @return The sticker's type.
     */
    StickerType getType();

    /**
     * Gets the sticker's format type.
     *
     * @return The sticker's format type.
     */
    StickerFormatType getFormatType();

    /**
     * Whether the sticker can be used or not if it's from a server. Stickers may be locked due to not enough server
     * boosts.
     *
     * @return {@code true}, if the sticker is available to use. {@code false} otherwise.
     */
    Optional<Boolean> isAvailable();

    /**
     * The ID of the server that owns this sticker.
     *
     * @return The ID of the server that owns this sticker.
     */
    Optional<Long> getServerId();

    /**
     * The server that owns this sticker. May not be present if the sticker is from a server the bot is not invited to.
     *
     * @return The server that owns this sticker.
     */
    Optional<Server> getServer();

    /**
     * The user that uploaded the sticker on the server.
     *
     * @return The user that uploaded the sticker on the server.
     */
    Optional<User> getUser();

    /**
     * Gets the sticker's sort order within it's sticker pack.
     *
     * @return The sticker's sort order within it's sticker pack.
     */
    Optional<Integer> getSortValue();

    /**
     * Updates the sticker with the given values.
     *
     * @param name        The new name of the sticker.
     * @param description The new description of the sticker.
     * @param tags        The new tags of the sticker.
     * @return A future of the updated sticker object.
     */
    default CompletableFuture<Sticker> update(String name, String description, String tags) {
        return update(name, description, tags, null);
    }

    /**
     * Updates the sticker with the given values.
     *
     * @param name        The new name of the sticker.
     * @param description The new description of the sticker.
     * @param tags        The new tags of the sticker.
     * @param reason      The reason for the audit log.
     * @return A future of the updated sticker.
     */
    default CompletableFuture<Sticker> update(String name, String description, String tags, String reason) {
        return createUpdater()
                .setName(name)
                .setDescription(description)
                .setTags(tags)
                .update(reason);
    }

    /**
     * Updates the name of the sticker.
     *
     * @param name   The new name of the sticker.
     * @return A future of the updated sticker object.
     */
    default CompletableFuture<Sticker> updateName(String name) {
        return updateName(name, null);
    }

    /**
     * Updates the name of the sticker.
     *
     * @param name   The new name of the sticker.
     * @param reason The reason for the audit log.
     * @return A future of the updated sticker object.
     */
    default CompletableFuture<Sticker> updateName(String name, String reason) {
        return createUpdater()
                .setName(name)
                .update(reason);
    }

    /**
     * Updates the description of the sticker.
     *
     * @param description The new description of the sticker.
     * @return A future of the updated sticker object.
     */
    default CompletableFuture<Sticker> updateDescription(String description) {
        return createUpdater()
                .setDescription(description)
                .update(null);
    }

    /**
     * Updates the description of the sticker.
     *
     * @param description The new description of the sticker.
     * @param reason      The reason for the audit log.
     * @return A future of the updated sticker object.
     */
    default CompletableFuture<Sticker> updateDescription(String description, String reason) {
        return createUpdater()
                .setDescription(description)
                .update(reason);
    }

    /**
     * Updates the tags of the sticker.
     *
     * @param tags   The new tags of the sticker.
     * @return A future of the updated sticker object.
     */
    default CompletableFuture<Sticker> updateTags(String tags) {
        return updateTags(tags, null);
    }

    /**
     * Updates the tags of the sticker.
     *
     * @param tags   The new tags of the sticker.
     * @param reason The reason for th audit log.
     * @return A future of the updated sticker object.
     */
    default CompletableFuture<Sticker> updateTags(String tags, String reason) {
        return createUpdater()
                .setTags(tags)
                .update(reason);
    }

    /**
     * Creates a new sticker updater for the sticker.
     *
     * @return A new sticker updater for the sticker.
     */
    StickerUpdater createUpdater();

    /**
     * Deletes the sticker.
     *
     * @return A future.
     */
    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    /**
     * Deletes the sticker.
     *
     * @param reason The reason for the audit log.
     * @return A future.
     */
    CompletableFuture<Void> delete(String reason);

    /**
     * Creates a new sticker with the given values.
     *
     * @param server The server where the sticker should be created on.
     * @param name   The name of the sticker.
     * @param tags   The tags of the sticker.
     * @param file   The file to upload as sticker.
     * @return A future of the final sticker object.
     */
    static CompletableFuture<Sticker> create(Server server, String name, String tags, File file) {
        return create(server, name, "", tags, file);
    }

    /**
     * Creates a new sticker with the given values.
     *
     * @param server      The server where the sticker should be created on.
     * @param name        The name of the sticker.
     * @param description The description of the sticker.
     * @param tags        The tags of the sticker.
     * @param file        The file to upload as sticker.
     * @return A future of the final sticker object.
     */
    static CompletableFuture<Sticker> create(Server server, String name, String description, String tags, File file) {
        return create(server, name, description, tags, file, null);
    }

    /**
     * Creates a new sticker with the given values.
     *
     * @param server      The server where the sticker should be created on.
     * @param name        The name of the sticker.
     * @param description The description of the sticker.
     * @param tags        The tags of the sticker.
     * @param file        The file to upload as sticker.
     * @param reason      The reason for the audit log.
     * @return A future of the final sticker object.
     */
    static CompletableFuture<Sticker> create(Server server, String name, String description, String tags, File file,
                                             String reason) {
        return new StickerBuilder(server)
                .setName(name)
                .setDescription(description)
                .setTags(tags)
                .setFile(file)
                .create(reason);
    }
}
