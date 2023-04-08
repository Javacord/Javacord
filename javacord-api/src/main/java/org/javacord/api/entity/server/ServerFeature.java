package org.javacord.api.entity.server;

/**
 * An enum with all server features.
 */
public enum ServerFeature {

    /**
     * Server has access to set an animated server banner image.
     */
    ANIMATED_BANNER,
    /**
     * Server has access to set an animated Server icon.
     */
    ANIMATED_ICON,
    /**
     * Server is using the old permissions configuration behavior.
     */
    APPLICATION_COMMAND_PERMISSIONS_V2,
    /**
     * Server has set up auto moderation rules.
     */
    AUTO_MODERATION,
    /**
     * Server has access to set a Server banner image.
     */
    BANNER,
    /**
     * Server is a community server.
     */
    COMMUNITY,
    /**
     * Server has enabled monetization.
     */
    CREATOR_MONETIZABLE_PROVISIONAL,
    /**
     * Server has enabled the role subscription promo page.
     */
    CREATOR_STORE_PAGE,
    /**
     * Server has been set as a support server on the App Directory.
     */
    DEVELOPER_SUPPORT_SERVER,
    /**
     * Server is able to be discovered in the directory.
     */
    DISCOVERABLE,
    /**
     * Server is able to be featured in the directory.
     */
    FEATURABLE,
    /**
     * Server has paused invites, preventing new users from joining.
     */
    INVITES_DISABLED,
    /**
     * Server has access to set an invite splash background.
     */
    INVITE_SPLASH,
    /**
     * Server has enabled <a href="https://discord.com/developers/docs/resources/#membership-screening-object">Membership Screening</a>.
     */
    MEMBER_VERIFICATION_GATE_ENABLED,
    /**
     * Server has increased custom sticker slots.
     */
    MORE_STICKERS,
    /**
     * Server has access to create news channels.
     */
    NEWS,
    /**
     * Server is partnered.
     */
    PARTNERED,
    /**
     * Server can be previewed before joining via Membership Screening or the directory.
     */
    PREVIEW_ENABLED,
    /**
     * Server is able to set role icons.
     */
    ROLE_ICONS,
    /**
     * Server has role subscriptions that can be purchased.
     */
    ROLE_SUBSCRIPTIONS_AVAILABLE_FOR_PURCHASE,
    /**
     * Server has enabled role subscriptions.
     */
    ROLE_SUBSCRIPTIONS_ENABLED,
    /**
     * Server has enabled ticketed events.
     */
    TICKETED_EVENTS_ENABLED,
    /**
     * Server has access to set a vanity URL.
     */
    VANITY_URL,
    /**
     * Server is verified.
     */
    VERIFIED,
    /**
     * Server has access to set 384kbps bitrate in voice (previously VIP voice servers).
     */
    VIP_REGIONS,
    /**
     * Server has enabled the welcome screen.
     */
    WELCOME_SCREEN_ENABLED,
}
