package org.javacord.api.entity.server;

/**
 * An enum with all server features.
 */
public enum ServerFeature {

    /**
     * Server has access to set an invite splash background.
     */
    INVITE_SPLASH,
    /**
     * Server has access to set 384kbps bitrate in voice (previously VIP voice servers).
     */
    VIP_REGIONS,
    /**
     * Server has access to set a vanity URL.
     */
    VANITY_URL,
    /**
     * Server is verified.
     */
    VERIFIED,
    /**
     * Server is partnered.
     */
    PARTNERED,
    /**
     * Server is a community server.
     */
    COMMUNITY,
    /**
     * Server has access to use commerce features (i.e. create store channels).
     */
    COMMERCE,
    /**
     * Server has access to create news channels.
     */
    NEWS,
    /**
     * Server is able to be discovered in the directory.
     */
    DISCOVERABLE,
    /**
     * Server is able to be featured in the directory.
     */
    FEATURABLE,
    /**
     * Server has access to set an animated Server icon.
     */
    ANIMATED_ICON,
    /**
     * Server has access to set a Server banner image.
     */
    BANNER,
    /**
     * Server has enabled the welcome screen.
     */
    WELCOME_SCREEN_ENABLED,
    /**
     * Server has enabled <a href="https://discord.com/developers/docs/resources/guild#membership-screening-object">Membership Screening</a>.
     */
    MEMBER_VERIFICATION_GATE_ENABLED,
    /**
     * Server can be previewed before joining via Membership Screening or the directory.
     */
    PREVIEW_ENABLED
}
