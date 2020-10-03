package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.permission.Role;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public class GuildDto {

    private final String id;
    private final String name;
    @Nullable
    @JsonProperty("icon")
    private final String iconHash;
    @Nullable
    @JsonProperty("splash")
    private final String splashHash;
    @Nullable
    @JsonProperty("discovery_splash")
    private final String discoverySplashHash;
    @Nullable
    private final Boolean owner;
    private final String ownerId;
    private final String permissions;
    private final String region;
    @Nullable
    private final String afkChannelId;
    @JsonProperty("afk_timeout")
    private final int afkTimeoutSeconds;
    @Nullable
    private final Boolean widgetEnabled;
    @Nullable
    private final String widgetChannelId;
    private final int verificationLevel;
    private final int defaultMessageNotifications;
    private final int explicitContentFilter;
    private final Role[] roles;
    private final Emoji[] emojis;
    private final String[] features;
    private final int mfaLevel;
    @Nullable
    private final String applicationId;
    @Nullable
    private final String systemChannelId;
    private final int systemChannelFlags;
    @Nullable
    private final String rolesChannelId;
    @Nullable
    private final Instant joinedAt;
    @Nullable
    private final Boolean large;
    @Nullable
    private final Boolean unavailable;
    @Nullable
    private final Integer memberCount;
    @Nullable
    private final VoiceStateDto[] voiceStates;
    @Nullable
    private final MemberDto[] members;
    @Nullable
    private final ChannelDto[] channels;
    @Nullable
    private final PresenceDto[] presences;
    @Nullable
    private final Integer maxPresences;
    @Nullable
    private final Integer maxMembers;
    @Nullable
    private final String vanityUrlCode;
    @Nullable
    private final String description;
    @Nullable
    @JsonProperty("banner")
    private final String bannerHash;
    private final int premiumTier;
    @Nullable
    private final Integer premiumSubscriptionCount;
    private final String preferredLocale;
    @Nullable
    private final String publicUpdatesChannelId;
    @Nullable
    private final Integer maxVideoChannelUsers;
    @Nullable
    private final Integer approximateMemberCount;
    @Nullable
    private final Integer approximatePresenceCount;

    @JsonCreator
    public GuildDto(String id, String name, @Nullable String iconHash, @Nullable String splashHash, @Nullable String discoverySplashHash, @Nullable Boolean owner, String ownerId, String permissions, String region, @Nullable String afkChannelId, int afkTimeoutSeconds, @Nullable Boolean widgetEnabled, @Nullable String widgetChannelId, int verificationLevel, int defaultMessageNotifications, int explicitContentFilter, Role[] roles, Emoji[] emojis, String[] features, int mfaLevel, @Nullable String applicationId, @Nullable String systemChannelId, int systemChannelFlags, @Nullable String rolesChannelId, @Nullable Instant joinedAt, @Nullable Boolean large, @Nullable Boolean unavailable, @Nullable Integer memberCount, @Nullable VoiceStateDto[] voiceStates, @Nullable MemberDto[] members, @Nullable ChannelDto[] channels, @Nullable PresenceDto[] presences, @Nullable Integer maxPresences, @Nullable Integer maxMembers, @Nullable String vanityUrlCode, @Nullable String description, @Nullable String bannerHash, int premiumTier, @Nullable Integer premiumSubscriptionCount, String preferredLocale, @Nullable String publicUpdatesChannelId, @Nullable Integer maxVideoChannelUsers, @Nullable Integer approximateMemberCount, @Nullable Integer approximatePresenceCount) {
        this.id = id;
        this.name = name;
        this.iconHash = iconHash;
        this.splashHash = splashHash;
        this.discoverySplashHash = discoverySplashHash;
        this.owner = owner;
        this.ownerId = ownerId;
        this.permissions = permissions;
        this.region = region;
        this.afkChannelId = afkChannelId;
        this.afkTimeoutSeconds = afkTimeoutSeconds;
        this.widgetEnabled = widgetEnabled;
        this.widgetChannelId = widgetChannelId;
        this.verificationLevel = verificationLevel;
        this.defaultMessageNotifications = defaultMessageNotifications;
        this.explicitContentFilter = explicitContentFilter;
        this.roles = roles;
        this.emojis = emojis;
        this.features = features;
        this.mfaLevel = mfaLevel;
        this.applicationId = applicationId;
        this.systemChannelId = systemChannelId;
        this.systemChannelFlags = systemChannelFlags;
        this.rolesChannelId = rolesChannelId;
        this.joinedAt = joinedAt;
        this.large = large;
        this.unavailable = unavailable;
        this.memberCount = memberCount;
        this.voiceStates = voiceStates;
        this.members = members;
        this.channels = channels;
        this.presences = presences;
        this.maxPresences = maxPresences;
        this.maxMembers = maxMembers;
        this.vanityUrlCode = vanityUrlCode;
        this.description = description;
        this.bannerHash = bannerHash;
        this.premiumTier = premiumTier;
        this.premiumSubscriptionCount = premiumSubscriptionCount;
        this.preferredLocale = preferredLocale;
        this.publicUpdatesChannelId = publicUpdatesChannelId;
        this.maxVideoChannelUsers = maxVideoChannelUsers;
        this.approximateMemberCount = approximateMemberCount;
        this.approximatePresenceCount = approximatePresenceCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getIconHash() {
        return Optional.ofNullable(iconHash);
    }

    public Optional<String> getSplashHash() {
        return Optional.ofNullable(splashHash);
    }

    public Optional<String> getDiscoverySplashHash() {
        return Optional.ofNullable(discoverySplashHash);
    }

    public Optional<Boolean> getOwner() {
        return Optional.ofNullable(owner);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getPermissions() {
        return permissions;
    }

    public String getRegion() {
        return region;
    }

    public Optional<String> getAfkChannelId() {
        return Optional.ofNullable(afkChannelId);
    }

    public int getAfkTimeoutSeconds() {
        return afkTimeoutSeconds;
    }

    public Optional<Boolean> getWidgetEnabled() {
        return Optional.ofNullable(widgetEnabled);
    }

    public Optional<String> getWidgetChannelId() {
        return Optional.ofNullable(widgetChannelId);
    }

    public int getVerificationLevel() {
        return verificationLevel;
    }

    public int getDefaultMessageNotifications() {
        return defaultMessageNotifications;
    }

    public int getExplicitContentFilter() {
        return explicitContentFilter;
    }

    public Role[] getRoles() {
        return roles;
    }

    public Emoji[] getEmojis() {
        return emojis;
    }

    public String[] getFeatures() {
        return features;
    }

    public int getMfaLevel() {
        return mfaLevel;
    }

    public Optional<String> getApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public Optional<String> getSystemChannelId() {
        return Optional.ofNullable(systemChannelId);
    }

    public int getSystemChannelFlags() {
        return systemChannelFlags;
    }

    public Optional<String> getRolesChannelId() {
        return Optional.ofNullable(rolesChannelId);
    }

    public Optional<Instant> getJoinedAt() {
        return Optional.ofNullable(joinedAt);
    }

    public Optional<Boolean> getLarge() {
        return Optional.ofNullable(large);
    }

    public Optional<Boolean> getUnavailable() {
        return Optional.ofNullable(unavailable);
    }

    public Optional<Integer> getMemberCount() {
        return Optional.ofNullable(memberCount);
    }

    public Optional<VoiceStateDto[]> getVoiceStates() {
        return Optional.ofNullable(voiceStates);
    }

    public Optional<MemberDto[]> getMembers() {
        return Optional.ofNullable(members);
    }

    public Optional<ChannelDto[]> getChannels() {
        return Optional.ofNullable(channels);
    }

    public Optional<PresenceDto[]> getPresences() {
        return Optional.ofNullable(presences);
    }

    public Optional<Integer> getMaxPresences() {
        return Optional.ofNullable(maxPresences);
    }

    public Optional<Integer> getMaxMembers() {
        return Optional.ofNullable(maxMembers);
    }

    public Optional<String> getVanityUrlCode() {
        return Optional.ofNullable(vanityUrlCode);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<String> getBannerHash() {
        return Optional.ofNullable(bannerHash);
    }

    public int getPremiumTier() {
        return premiumTier;
    }

    public Optional<Integer> getPremiumSubscriptionCount() {
        return Optional.ofNullable(premiumSubscriptionCount);
    }

    public String getPreferredLocale() {
        return preferredLocale;
    }

    public Optional<String> getPublicUpdatesChannelId() {
        return Optional.ofNullable(publicUpdatesChannelId);
    }

    public Optional<Integer> getMaxVideoChannelUsers() {
        return Optional.ofNullable(maxVideoChannelUsers);
    }

    public Optional<Integer> getApproximateMemberCount() {
        return Optional.ofNullable(approximateMemberCount);
    }

    public Optional<Integer> getApproximatePresenceCount() {
        return Optional.ofNullable(approximatePresenceCount);
    }
}
