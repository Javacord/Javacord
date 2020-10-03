package org.javacord.core.dto.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UserDto {

    private final String id;
    private final String username;
    private final String discriminator;
    @Nullable
    @JsonProperty("avatar")
    private final String avatarHash;
    @Nullable
    private final Boolean bot;
    @Nullable
    private final Boolean system;
    @Nullable
    private final Boolean mfaEnabled;
    @Nullable
    private final String locale;
    @Nullable
    private final Boolean verified;
    @Nullable
    private final String email;
    @Nullable
    private final Integer flags;
    @Nullable
    private final Integer premiumType;
    @Nullable
    private final Integer publicFlags;

    @Nullable // Present in mentions array of a message
    private final PartialMemberDto member;

    @JsonCreator
    public UserDto(String id, String username, String discriminator, @Nullable String avatarHash, @Nullable Boolean bot, @Nullable Boolean system, @Nullable Boolean mfaEnabled, @Nullable String locale, @Nullable Boolean verified, @Nullable String email, @Nullable Integer flags, @Nullable Integer premiumType, @Nullable Integer publicFlags, @Nullable PartialMemberDto member) {
        this.id = id;
        this.username = username;
        this.discriminator = discriminator;
        this.avatarHash = avatarHash;
        this.bot = bot;
        this.system = system;
        this.mfaEnabled = mfaEnabled;
        this.locale = locale;
        this.verified = verified;
        this.email = email;
        this.flags = flags;
        this.premiumType = premiumType;
        this.publicFlags = publicFlags;
        this.member = member;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public Optional<String> getAvatarHash() {
        return Optional.ofNullable(avatarHash);
    }

    public Optional<Boolean> getBot() {
        return Optional.ofNullable(bot);
    }

    public Optional<Boolean> getSystem() {
        return Optional.ofNullable(system);
    }

    public Optional<Boolean> getMfaEnabled() {
        return Optional.ofNullable(mfaEnabled);
    }

    public Optional<String> getLocale() {
        return Optional.ofNullable(locale);
    }

    public Optional<Boolean> getVerified() {
        return Optional.ofNullable(verified);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<Integer> getFlags() {
        return Optional.ofNullable(flags);
    }

    public Optional<Integer> getPremiumType() {
        return Optional.ofNullable(premiumType);
    }

    public Optional<Integer> getPublicFlags() {
        return Optional.ofNullable(publicFlags);
    }

    public Optional<PartialMemberDto> getMember() {
        return Optional.ofNullable(member);
    }
}
