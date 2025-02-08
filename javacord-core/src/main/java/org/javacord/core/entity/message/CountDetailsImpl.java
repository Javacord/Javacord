package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.CountDetails;

import java.util.concurrent.atomic.AtomicInteger;

public class CountDetailsImpl implements CountDetails {
    /**
     * The count of super reactions.
     */
    private final AtomicInteger burstCount = new AtomicInteger();

    /**
     * The count of normal reactions.
     */
    private final AtomicInteger normalCount = new AtomicInteger();

    /**
     * Creates a new count details object.
     *
     * @param data – The json data of the count details.
     */
    public CountDetailsImpl(JsonNode data) {
        this.burstCount.set(data.get("burst").asInt());
        this.normalCount.set(data.get("normal").asInt());
    }

    /**
     * Creates a new count details object.
     *
     * @param isSuperReaction Whether the reaction is a super reaction.
     */
    public CountDetailsImpl(boolean isSuperReaction) {
        if (isSuperReaction) {
            this.burstCount.set(1);
        } else {
            this.normalCount.set(1);
        }
    }

    @Override
    public int getBurstCount() {
        return burstCount.get();
    }

    @Override
    public int getNormalCount() {
        return normalCount.get();
    }

    @Override
    public void decrementCount(boolean isSuperReaction) {
        if (isSuperReaction) {
            decrementBurstCount();
        } else {
            decrementNormalCount();
        }
    }

    @Override
    public void incrementCount(boolean isSuperReaction) {
        if (isSuperReaction) {
            incrementBurstCount();
        } else {
            incrementNormalCount();
        }
    }


    /**
     * Increments the number of burst reactions.
     */
    public void incrementBurstCount() {
        burstCount.incrementAndGet();
    }

    /**
     * Decrements the number of burst reactions.
     */
    public void decrementBurstCount() {
        burstCount.decrementAndGet();
    }

    /**
     * Increments the number of normal reactions.
     */
    public void incrementNormalCount() {
        normalCount.incrementAndGet();
    }

    /**
     * Decrements the number of normal reactions.
     */
    public void decrementNormalCount() {
        normalCount.decrementAndGet();
    }
}
