package de.btobastian.javacord.entities.impl;

import de.btobastian.javacord.entities.Activity;
import de.btobastian.javacord.entities.ActivityType;

import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link Activity}.
 */
public class ImplActivity implements Activity {

    private final ActivityType type;
    private final String name;
    private final String streamingUrl;

    /**
     * Creates a new activity object.
     *
     * @param type The type of the activity.
     * @param name The name of the activity.
     * @param streamingUrl The streaming url of the activity. May be <code>null</code>.
     */
    public ImplActivity(ActivityType type, String name, String streamingUrl) {
        this.type = type;
        this.name = name;
        this.streamingUrl = streamingUrl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getStreamingUrl() {
        return Optional.ofNullable(streamingUrl);
    }

    @Override
    public ActivityType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImplActivity)) {
            return false;
        }
        ImplActivity otherActivity = (ImplActivity) obj;
        return Objects.deepEquals(name, otherActivity.name) && Objects.deepEquals(streamingUrl, otherActivity.streamingUrl);
    }

    @Override
    public int hashCode() {
        int hash = 42;
        int typeHash = type.hashCode();
        int nameHash = name == null ? 0 : name.hashCode();
        int streamingUrlHash = streamingUrl == null ? 0 : streamingUrl.hashCode();

        hash = hash * 11 + typeHash;
        hash = hash * 17 + nameHash;
        hash = hash * 19 + streamingUrlHash;
        return hash;
    }
}
