package org.javacord.api.entity.message;

public interface CountDetails {

    /**
     * Gets the count of super reactions.
     *
     * @return The count of super reactions.
     */
    int getBurstCount();

    /**
     * Gets the count of normal reactions.
     *
     * @return The count of normal reactions.
     */
    int getNormalCount();

    /**
     * Decrements the count of the reaction.
     *
     * @param isSuperReaction Whether the reaction is a super reaction.
     */
    void decrementCount(boolean isSuperReaction);

    /**
     * Increments the count of the reaction.
     *
     * @param isSuperReaction Whether the reaction is a super reaction.
     */
    void incrementCount(boolean isSuperReaction);
}
