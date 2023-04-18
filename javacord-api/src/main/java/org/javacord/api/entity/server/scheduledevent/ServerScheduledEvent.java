package org.javacord.api.entity.server.scheduledevent;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerStageVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ServerScheduledEvent extends DiscordEntity {

    /**
     * The Server id which the scheduled event belongs to.
     *
     * @return The Server id which the scheduled event belongs to.
     */
    long getServerId();

    /**
     * The Server which the scheduled event belongs to.
     *
     * @return The Server which the scheduled event belongs to.
     */
    default Server getServer() {
        return getApi().getServerById(getServerId()).orElseThrow(AssertionError::new);
    }

    /**
     * The channel id in which the scheduled event will be hosted,
     * or an empty optional if scheduled entity type is EXTERNAL.
     *
     * @return The channel id in which the scheduled event will be hosted.
     */
    Optional<Long> getChannelId();

    /**
     * The channel id in which the scheduled event will be hosted,
     * or an empty optional if scheduled entity type is EXTERNAL.
     *
     * @return The channel id in which the scheduled event will be hosted.
     */
    default Optional<RegularServerChannel> getChannel() {
        return getChannelId().flatMap(getApi()::getRegularServerChannelById);
    }

    /**
     * The id of the user that created the scheduled event.
     *
     * @return The id of the user that created the scheduled event.
     */
    Optional<Long> getCreatorId();

    /**
     * The name of the scheduled event.
     *
     * @return The name of the scheduled event.
     */
    String getName();

    /**
     * The description of the scheduled event.
     *
     * @return The description of the scheduled event.
     */
    Optional<String> getDescription();

    /**
     * The time the scheduled event will start.
     *
     * @return The time the scheduled event will start.
     */
    Instant getStartTime();

    /**
     * The time the scheduled event will end, required if entity_type is EXTERNAL.
     *
     * @return The time the scheduled event will end.
     */
    Optional<Instant> getEndTime();

    /**
     * The privacy level of the scheduled event.
     *
     * @return The privacy level of the scheduled event.
     */
    ServerScheduledEventPrivacyLevel getPrivacyLevel();

    /**
     * The status of the scheduled event.
     *
     * @return The status of the scheduled event.
     */
    ServerScheduledEventStatus getStatus();

    /**
     * The type of the scheduled event.
     *
     * @return The type of the scheduled event.
     */
    ServerScheduledEventType getEntityType();

    /**
     * The id of an entity associated with a guild scheduled event.
     *
     * @return The id of an entity associated with a guild scheduled event.
     */
    Optional<Long> getEntityId();

    /**
     * Additional metadata for the guild scheduled event.
     *
     * @return Additional metadata for the guild scheduled event.
     */
    Optional<ServerScheduledEventMetadata> getEntityMetadata();

    /**
     * The user that created the scheduled event.
     *
     * @return The user that created the scheduled event.
     */
    Optional<User> getCreator();

    /**
     * The number of users subscribed to the scheduled event.
     *
     * @return The number of users subscribed to the scheduled event.
     */
    Optional<Integer> getUserCount();

    /**
     * The cover image hash of the scheduled event.
     *
     * @return The cover image hash of the scheduled event.
     */
    Optional<Icon> getImage();

    /**
     * Deletes this scheduled event.
     *
     * @return A future to check if the deletion was successful.
     */
    CompletableFuture<Void> delete();

    /**
     * Starts the event.
     *
     * @return A future to check if the action was successful.
     */
    default CompletableFuture<Void> startEvent() {
        return new ServerScheduledEventUpdater(this).setEventStatus(ServerScheduledEventStatus.ACTIVE).update();
    }

    /**
     * Ends the event.
     *
     * @return A future to check if the action was successful.
     */
    default CompletableFuture<Void> endEvent() {
        return new ServerScheduledEventUpdater(this).setEventStatus(ServerScheduledEventStatus.COMPLETED).update();
    }

    /**
     * Cancels the event.
     *
     * @return A future to check if the action was successful.
     */
    default CompletableFuture<Void> cancelEvent() {
        return new ServerScheduledEventUpdater(this).setEventStatus(ServerScheduledEventStatus.CANCELED).update();
    }

    /**
     * Creates an updater to update this event to an external event with the minimum required fields.
     *
     * @param entityMetadataLocation The location of the event.
     * @param endTime                The time the scheduled event will end.
     * @return A future to check if the action was successful.
     */
    default ServerScheduledEventUpdater createToExternalEventUpdater(String entityMetadataLocation, Instant endTime) {
        return new ServerScheduledEventUpdater(this)
                .setEntityType(ServerScheduledEventType.EXTERNAL)
                .setChannelId(null)
                .setEntityMetadataLocation(entityMetadataLocation)
                .setScheduledEndTime(endTime);
    }

    /**
     * Creates an updater to update this event to a voice channel event with the minimum required fields.
     *
     * @param channel The channel in which the event will be hosted.
     * @return A future to check if the action was successful.
     */
    default ServerScheduledEventUpdater createToVoiceEventUpdater(ServerVoiceChannel channel) {
        return new ServerScheduledEventUpdater(this)
                .setEntityType(ServerScheduledEventType.VOICE)
                .setEntityMetadataLocation(null)
                .setChannel(channel);
    }

    /**
     * Creates an updater to update this event to a stage channel event with the minimum required fields.
     *
     * @param channel The channel in which the event will be hosted.
     * @return A future to check if the action was successful.
     */
    default ServerScheduledEventUpdater createToStageEventUpdater(ServerStageVoiceChannel channel) {
        return new ServerScheduledEventUpdater(this)
                .setEntityType(ServerScheduledEventType.STAGE_INSTANCE)
                .setEntityMetadataLocation(null)
                .setChannel(channel);
    }

    /**
     * Gets all users participating in this event.
     * Note: This method fires <code>ceil((number of participants + 1) / 100)</code> requests to Discord
     * as the API returns the results in pages and Javacord collects all pages into one collection.
     * If you want to control pagination yourself, use {@link #requestParticipants(Integer, Long, Long)}.
     *
     * @return All server scheduled event participants.
     */
    CompletableFuture<Set<ServerScheduledEventUser>> requestParticipants();

    /**
     * Gets up to <code>limit</code> users participating in this event, only taking users with an ID lower than
     * <code>before</code> into account.
     * This can be used to get a specific page of participants.
     * To get all pages / all participants at once, use {@link #requestParticipants()}.
     *
     * @param limit  How many participants should be returned at most. Must be within [0, 100].
     *               If null, it will default to 100
     * @param before Should be a snowflake to only take participants of users with IDs lower
     *               than this parameter into account; can be null. This or <code>after</code> must be set.
     * @param after  Should be a snowflake to only take participants of users with IDs higher
     *               than this parameter into account; can be null. This or <code>before</code> must be set.
     * @return Server scheduled event participants on the given page with at most <code>limit</code> entries.
     */
    CompletableFuture<Set<ServerScheduledEventUser>> requestParticipants(Integer limit, Long before, Long after);


    enum Type {
        /**
         * A scheduled event that is hosted in a voice channel.
         */
        VOICE(1),

        /**
         * A scheduled event that is hosted in a stage channel.
         */
        STAGE_INSTANCE(2),

        /**
         * A scheduled event that is hosted externally.
         */
        EXTERNAL(3);

        private final int type;

        Type(int type) {
            this.type = type;
        }

        /**
         * Gets the type of the scheduled event.
         *
         * @return The type of the scheduled event.
         */
        public int getType() {
            return type;
        }

        /**
         * Gets the type of the scheduled event by its id.
         *
         * @param type The id of the type.
         * @return The type of the scheduled event.
         */
        public static Type getTypeById(int type) {
            for (Type t : values()) {
                if (t.getType() == type) {
                    return t;
                }
            }
            return null;
        }
    }
}
