package org.javacord.core.entity.activity;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityAssets;
import org.javacord.api.entity.activity.ActivityFlag;
import org.javacord.api.entity.activity.ActivityParty;
import org.javacord.api.entity.activity.ActivitySecrets;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The implementation of {@link Activity}.
 */
public class ActivityImpl implements Activity {

    private final ActivityType type;
    private final String name;
    private final String streamingUrl;
    private final Instant createdAt;
    private final String details;
    private final String state;
    private final ActivityParty party;
    private final ActivityAssets assets;
    private final ActivitySecrets secrets;
    private final Long applicationId;
    private final Long startTime;
    private final Long endTime;
    private final Emoji emoji;
    private final Boolean instance;
    private final EnumSet<ActivityFlag> flags = EnumSet.noneOf(ActivityFlag.class);
    private final List<String> buttonLabels = new ArrayList<>();

    /**
     * Creates a new activity object.
     *
     * @param api  The discord api instance.
     * @param data The json data of the activity.
     */
    public ActivityImpl(DiscordApiImpl api, JsonNode data) {
        this.type = ActivityType.getActivityTypeById(data.get("type").asInt());
        this.name = data.get("name").asText();
        this.streamingUrl = data.hasNonNull("url") ? data.get("url").asText() : null;
        this.details = data.hasNonNull("details") ? data.get("details").asText() : null;
        this.state = data.hasNonNull("state") ? data.get("state").asText() : null;
        this.party = data.hasNonNull("party") ? new ActivityPartyImpl(data.get("party")) : null;
        this.assets = data.hasNonNull("assets") ? new ActivityAssetsImpl(this, data.get("assets")) : null;
        this.secrets = data.hasNonNull("secrets") ? new ActivitySecretsImpl(data.get("secrets")) : null;
        this.applicationId = data.hasNonNull("application_id") ? data.get("application_id").asLong() : null;
        this.createdAt = Instant.ofEpochMilli(data.get("created_at").asLong());
        this.instance = data.hasNonNull("instance") && data.get("instance").asBoolean();

        if (data.has("timestamps")) {
            JsonNode timestamps = data.get("timestamps");
            this.startTime = timestamps.has("start") ? timestamps.get("start").asLong() : null;
            this.endTime = timestamps.has("end") ? timestamps.get("end").asLong() : null;
        } else {
            this.startTime = null;
            this.endTime = null;
        }

        if (data.has("emoji")) {
            JsonNode emoji = data.get("emoji");
            if (emoji.has("id")) {
                this.emoji = api.getKnownCustomEmojiOrCreateCustomEmoji(emoji);
            } else {
                this.emoji = UnicodeEmojiImpl.fromString(emoji.get("name").asText());
            }
        } else {
            this.emoji = null;
        }

        if (data.has("flags")) {
            int flags = data.get("flags").asInt();
            for (ActivityFlag flag : ActivityFlag.values()) {
                if ((flag.asInt() & flags) == flag.asInt()) {
                    this.flags.add(flag);
                }
            }
        }

        data.path("buttons").forEach(buttonLabel -> buttonLabels.add(buttonLabel.asText()));
    }

    /**
     * Creates a new activity object.
     *
     * @param type         The type of the activity.
     * @param name         The name of the activity.
     * @param streamingUrl The streamingUrl of the activity.
     * @param state        The user's party status.
     */
    public ActivityImpl(ActivityType type, String name, String streamingUrl, String state) {
        final Pattern allowedStreamingUrls = Pattern.compile(
                "https?://(www\\.)?(twitch\\.tv/|youtube\\.com/watch\\?v=).+", Pattern.CASE_INSENSITIVE);

        if (name != null && name.length() > 128) {
            throw new IllegalArgumentException("The name of the activity must not be longer than 128 characters!");
        }

        if (streamingUrl != null && !allowedStreamingUrls.matcher(streamingUrl).matches()) {
            throw new IllegalArgumentException("The url must be a valid twitch or youtube url!");
        }

        if (state != null && state.length() > 128) {
            throw new IllegalArgumentException(
                    "The state of the activity must not be longer than 128 characters!");
        }

        this.type = type;
        this.name = name;
        this.streamingUrl = streamingUrl;
        this.state = state;
        this.emoji = null;
        this.createdAt = null;
        this.details = null;
        this.party = null;
        this.assets = null;
        this.secrets = null;
        this.applicationId = null;
        this.startTime = null;
        this.endTime = null;
        this.instance = null;
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
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public ActivityType getType() {
        return type;
    }

    @Override
    public Optional<String> getDetails() {
        return Optional.ofNullable(details);
    }

    @Override
    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    @Override
    public Optional<ActivityParty> getParty() {
        return Optional.ofNullable(party);
    }

    @Override
    public Optional<ActivityAssets> getAssets() {
        return Optional.ofNullable(assets);
    }

    @Override
    public Optional<ActivitySecrets> getSecrets() {
        return Optional.ofNullable(secrets);
    }

    @Override
    public Optional<Long> getApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    @Override
    public Optional<Instant> getStartTime() {
        return Optional.ofNullable(startTime).map(Instant::ofEpochMilli);
    }

    @Override
    public Optional<Instant> getEndTime() {
        return Optional.ofNullable(endTime).map(Instant::ofEpochMilli);
    }

    @Override
    public Optional<Emoji> getEmoji() {
        return Optional.ofNullable(emoji);
    }

    @Override
    public Optional<Boolean> getInstance() {
        return Optional.ofNullable(instance);
    }

    @Override
    public EnumSet<ActivityFlag> getFlags() {
        return EnumSet.copyOf(flags);
    }

    @Override
    public List<String> getButtonLabels() {
        return Collections.unmodifiableList(buttonLabels);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ActivityImpl)) {
            return false;
        }
        ActivityImpl otherActivity = (ActivityImpl) obj;
        return Objects.deepEquals(type, otherActivity.type)
                && Objects.deepEquals(name, otherActivity.name)
                && Objects.deepEquals(streamingUrl, otherActivity.streamingUrl)
                && Objects.deepEquals(details, otherActivity.details)
                && Objects.deepEquals(state, otherActivity.state)
                && Objects.deepEquals(party, otherActivity.party)
                && Objects.deepEquals(assets, otherActivity.assets)
                && Objects.deepEquals(applicationId, otherActivity.applicationId)
                && Objects.deepEquals(startTime, otherActivity.startTime)
                && Objects.deepEquals(endTime, otherActivity.endTime)
                && Objects.deepEquals(emoji, otherActivity.emoji)
                && Objects.deepEquals(flags, otherActivity.flags)
                && Objects.deepEquals(buttonLabels, otherActivity.buttonLabels)
                && Objects.deepEquals(secrets, otherActivity.secrets)
                && Objects.deepEquals(instance, otherActivity.instance);
    }

    @Override
    public int hashCode() {
        int hash = 42;
        int typeHash = type.hashCode();
        int nameHash = name == null ? 0 : name.hashCode();
        int streamingUrlHash = streamingUrl == null ? 0 : streamingUrl.hashCode();
        int detailsHash = details == null ? 0 : details.hashCode();
        int stateHash = state == null ? 0 : state.hashCode();
        int partyHash = party == null ? 0 : party.hashCode();
        int assetsHash = assets == null ? 0 : assets.hashCode();
        int applicationIdHash = applicationId == null ? 0 : applicationId.toString().hashCode();
        int startTimeHash = startTime == null ? 0 : startTime.toString().hashCode();
        int endTimeHash = endTime == null ? 0 : endTime.toString().hashCode();
        int emojiHash = emoji == null ? 0 : emoji.hashCode();
        int flagsHash = flags == null ? 0 : flags.hashCode();
        int createdAtHash = createdAt == null ? 0 : createdAt.hashCode();
        int secretsHash = secrets == null ? 0 : secrets.hashCode();
        int instanceHash = instance == null ? 0 : instance.hashCode();
        int buttonsHash = buttonLabels == null ? 0 : buttonLabels.hashCode();

        hash = hash * 11 + typeHash;
        hash = hash * 13 + nameHash;
        hash = hash * 17 + streamingUrlHash;
        hash = hash * 19 + detailsHash;
        hash = hash * 23 + stateHash;
        hash = hash * 29 + partyHash;
        hash = hash * 31 + assetsHash;
        hash = hash * 37 + applicationIdHash;
        hash = hash * 41 + startTimeHash;
        hash = hash * 43 + endTimeHash;
        hash = hash * 47 + emojiHash;
        hash = hash * 53 + flagsHash;
        hash = hash * 59 + createdAtHash;
        hash = hash * 61 + secretsHash;
        hash = hash * 67 + instanceHash;
        hash = hash * 71 + buttonsHash;
        return hash;
    }
}
