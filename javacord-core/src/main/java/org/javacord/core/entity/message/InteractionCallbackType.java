package org.javacord.core.entity.message;

/**
 * Represents an interaction callback type.
 */
public enum InteractionCallbackType {

    /**
     * ACK a Ping.
     */
    PONG(1),
    /**
     * Respond to an interaction with a message.
     */
    ChannelMessageWithSource(4),
    /**
     * ACK an interaction and edit a response later, the user sees a loading state.
     */
    DeferredChannelMessageWithSource(5),
    /**
     * For components, ACK an interaction and edit the original message later; the user does not see a loading state.
     */
    DeferredUpdateMessage(6),
    /**
     * For components, edit the message the component was attached to.
     */
    UpdateMessage(7),

    /**
     * An unknown interaction callback type.
     */
    UNKNOWN(-1);

    /**
     * The id of the interaction callback type.
     */
    private final int id;

    /**
     * Creates a new interaction callback type.
     *
     * @param id The id of the interaction callback type.
     */
    InteractionCallbackType(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the interaction callback type.
     *
     * @return The id of the interaction callback type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the interaction callback type by its id.
     *
     * @param id The id of the interaction callback type.
     * @return The interaction callback type with the given id or {@link InteractionCallbackType#UNKNOWN} if unknown id.
     */
    public static InteractionCallbackType getInteractionCallbackTypeById(int id) {
        switch (id) {
            case 1:
                return PONG;
            case 4:
                return ChannelMessageWithSource;
            case 5:
                return DeferredChannelMessageWithSource;
            case 6:
                return DeferredUpdateMessage;
            case 7:
                return UpdateMessage;
            default:
                return UNKNOWN;
        }
    }
}
