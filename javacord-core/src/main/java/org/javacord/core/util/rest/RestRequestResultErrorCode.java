package org.javacord.core.util.rest;

import org.javacord.api.exception.CannotMessageUserException;
import org.javacord.api.exception.DiscordException;
import org.javacord.api.exception.ReactionBlockedException;
import org.javacord.api.exception.UnknownEmojiException;
import org.javacord.api.exception.UnknownMessageException;
import org.javacord.api.util.rest.RestRequestInformation;
import org.javacord.api.util.rest.RestRequestResponseInformation;
import org.javacord.core.util.exception.DiscordExceptionInstantiator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An enum with all rest request result codes as defined by
 * <a href="https://discord.com/developers/docs/topics/opcodes-and-status-codes#json">Discord</a>.
 */
public enum RestRequestResultErrorCode {

    UNKNOWN_ACCOUNT(10001, "Unknown account"),
    UNKNOWN_APPLICATION(10002, "Unknown application"),
    UNKNOWN_CHANNEL(10003, "Unknown channel"),
    UNKNOWN_GUILD(10004, "Unknown guild"),
    UNKNOWN_INTEGRATION(10005, "Unknown integration"),
    UNKNOWN_INVITE(10006, "Unknown invite"),
    UNKNOWN_MEMBER(10007, "Unknown member"),
    UNKNOWN_MESSAGE(10008, "Unknown message", UnknownMessageException::new, RestRequestHttpResponseCode.NOT_FOUND),
    UNKNOWN_OVERWRITE(10009, "Unknown overwrite"),
    UNKNOWN_PROVIDER(10010, "Unknown provider"),
    UNKNOWN_ROLE(10011, "Unknown role"),
    UNKNOWN_TOKEN(10012, "Unknown token"),
    UNKNOWN_USER(10013, "Unknown user"),
    UNKNOWN_EMOJI(10014, "Unknown Emoji", UnknownEmojiException::new, RestRequestHttpResponseCode.BAD_REQUEST),
    UNKNOWN_WEBHOOK(10015, "Unknown webhook"),
    UNKNOWN_WEBHOOK_SERVICE(10016, "Unknown webhook service"),
    UNKNOWN_SESSION(10020, "Unknown session"),
    UNKNOWN_BAN(10026, "Unknown ban"),
    UNKNOWN_SKU(10027, "Unknown SKU"),
    UNKNOWN_STORE_LISTING(10028, "Unknown store listing"),
    UNKNOWN_ENTITLEMENT(10029, "Unknown entitlement"),
    UNKNOWN_BUILD(10030, "Unknown build"),
    UNKNOWN_LOBBY(10031, "Unknown lobby"),
    UNKNOWN_BRANCH(10032, "Unknown branch"),
    UNKNOWN_STORE_DIRECTORY_LAYOUT(10033, "Unknown store directory layout"),
    UNKNOWN_REDISTRIBUTABLE(10036, "Unknown redistributable"),
    UNKNOWN_GIFT_CODE(10038, "Unknown gift code"),
    UNKNOWN_STREAM(10049, "Unknown stream"),
    UNKNOWN_PREMIUM_SERVER_SUBSCRIPTION(10050, "Unknown premium server subscription"),
    UNKNOWN_SERVER_TEMPLATE(10057, "Unknown server template"),
    UNKNOWN_DISCOVERABLE_SERVER_CATEGORY(10059, "Unknown discoverable server category"),
    UNKNOWN_STICKER(10060, "Unknown sticker"),
    UNKNOWN_INTERACTION(10062, "Unknown interaction"),
    UNKNOWN_APPLICATION_COMMAND(10063, "Unknown application command"),
    UNKNOWN_VOICE_STATE(10065, "Unknown voice state"),
    UNKNOWN_APPLICATION_COMMAND_PERMISSIONS(10066, "Unknown application command permissions"),
    UNKNOWN_STAGE_INSTANCE(10067, "Unknown Stage Instance"),
    UNKNOWN_GUILD_MEMBER_VERIFICATION_FORM(10068, "Unknown Guild Member Verification Form"),
    UNKNOWN_GUILD_WELCOME_SCREEN(10069, "Unknown Guild Welcome Screen"),
    UNKNOWN_GUILD_SCHEDULED_EVENT(10070, "Unknown Guild Scheduled Event"),
    UNKNOWN_GUILD_SCHEDULED_EVENT_USER(10071, "Unknown Guild Scheduled Event User"),
    UNKNOWN_TAG(10087, "Unknown Tag"),
    BOTS_CANNOT_USE_THIS_ENDPOINT(20001, "Bots cannot use this endpoint"),
    ONLY_BOTS_CAN_USE_THIS_ENDPOINT(20002, "Only bots can use this endpoint"),
    EXPLICIT_CONTENT_CANNOT_BE_SENT_TO_THE_DESIRED_RECIPIENTS(20009,
            "Explicit content cannot be sent to the desired recipient(s)"),
    YOU_ARE_NOT_AUTHORIZED_TO_PERFORM_THIS_ACTION_ON_THIS_APPLICATION(20012,
            "You are not authorized to perform this action on this application"),
    THIS_ACTION_CANNOT_BE_PERFORMED_DUE_TO_SLOWMODE_RATE_LIMIT(20016,
            "This action cannot be performed due to slowmode rate limit"),
    ONLY_THE_OWNER_OF_THIS_ACCOUNT_CAN_PERFORM_THIS_ACTION(20018,
            "Only the owner of this account can perform this action"),
    MESSAGE_CANNOT_BE_EDITED_DUE_TO_ANNOUNCEMENT_RATE_LIMIT(20022,
            "This message cannot be edited due to announcement rate limits"),
    UNDER_MINIMUM_AGE(20024, "Under minimum age"),
    THE_CHANNEL_YOU_ARE_WRITING_HAS_HIT_THE_WRITE_RATE_LIMIT(20028,
            "The channel you are writing has hit the write rate limit"),
    THE_WRITE_ACTION_YOU_ARE_PERFORMING_ON_THIS_SERVER_HAS_HIT_THE_WRITE_RATE_LIMIT(20029,
            "The write action you are performing on this server has hit the write rate limit"),
    YOUR_STAGE_TOPIC_CONTAINS_WORDS_THAT_ARE_NOT_ALLOWED_FOR_PUBLIC_STAGES(20031,
            "Your Stage topic contains words that are not allowed for public Stages"),
    SERVER_PREMIUM_SUBSCRIPTION_LEVEL_TOO_LOW(20035, "Server premium subscription level too low"),
    MAXIMUM_NUMBER_OF_GUILDS_REACHED(30001, "Maximum number of guilds reached (100)"),
    MAXIMUM_NUMBER_OF_FRIENDS_REACHED(30002, "Maximum number of friends reached (1000)"),
    MAXIMUM_NUMBER_OF_PINS_REACHED(30003, "Maximum number of pins reached (50)"),
    MAXIMUM_NUMBER_OF_RECIPIENTS_REACHED(30004, "Maximum number of recipients reached (10)"),
    MAXIMUM_NUMBER_OF_GUILD_ROLES_REACHED(30005, "Maximum number of guild roles reached (250)"),
    MAXIMUM_NUMBER_OF_WEBHOOKS_REACHED(30007, "Maximum number of webhooks reached (10)"),
    MAXIMUM_NUMBER_OF_EMOJIS_REACHED(30008, "Maximum number of emojis reached (50)"),
    TOO_MANY_REACTIONS(30010, "Too many reactions"),
    MAXIMUM_NUMBER_OF_GUILD_CHANNELS_REACHED(30013, "Maximum number of guild channels reached (500)"),
    MAXIMUM_NUMBER_OF_ATTACHMENTS_IN_A_MESSAGE_REACHED(30015,
            "Maximum number of attachments in a message reached (10)"),
    MAXIMUM_NUMBER_OF_INVITES_REACHED(30016, "Maximum number of invites reached (1000)"),
    MAXIMUM_NUMBER_OF_ANIMATED_EMOJIS_REACHED(30018, "Maximum number of animated emojis reached"),
    MAXIMUM_NUMBER_OF_SERVER_MEMBERS_REACHED(30019, "Maximum number of server members reached"),
    MAXIMUM_NUMBER_OF_SERVER_CATEGORIES_REACHED(30030, "Maximum number of server categories has been reached (5)"),
    SERVER_ALREADY_HAS_A_TEMPLATE(30031, "Server already has a template"),
    MAXIMUM_NUMBER_OF_APPLICATION_COMMANDS_REACHED(30032, "Maximum number of application commands reached"),
    MAXIMUM_NUMBER_OF_THREAD_PARTICIPANTS_REACHED(30033, "Maximum number of thread participants has been reached"),
    MAXIMUM_NUMBER_OF_DAILY_APPLICATION_COMMAND_CREATES_REACHED(30034,
            "Maximum number of daily application command creates has been reached (200)"),
    MAXIMUM_NUMBER_OF_BANS_FOR_NON_GUILD_MEMBERS_EXCEEDED(30035,
            "Maximum number of bans for non-guild members have been exceeded"),
    MAXIMUM_NUMBER_OF_BANS_FETCHES_REACHED(30037, "Maximum number of bans fetches has been reached"),
    MAXIMUM_NUMBER_OF_UNCOMPLETED_GUILD_SCHEDULED_EVENTS_REACHED(30038,
            "Maximum number of uncompleted guild scheduled events reached (100)"),
    MAXIMUM_NUMBER_OF_STICKERS_REACHED(30039, "Maximum number of stickers reached"),
    MAXIMUM_NUMBER_OF_PRUNE_REQUESTS_REACHED(30040,
            "Maximum number of prune requests has been reached. Try again later"),
    MAXIMUM_NUMBER_OF_GUILD_WIDGET_SETTINGS_UPDATES_REACHED(30042,
            "Maximum number of guild widget settings updates has been reached. Try again later"),
    MAXIMUM_NUMBER_OF_EDITS_TO_MESSAGES_OLDER_THAN_1_HOUR_REACHED(30046,
            "Maximum number of edits to messages older than 1 hour reached. Try again later"),
    MAXIMUM_NUMBER_OF_PINNED_THREADS_IN_A_FORUM_CHANNEL_REACHED(30047,
            "Maximum number of pinned threads in a forum channel has been reached"),
    MAXIMUM_NUMBER_OF_TAGS_IN_A_FORUM_CHANNEL_REACHED(30048,
            "Maximum number of tags in a forum channel has been reached"),
    BITRATE_IS_TOO_HIGH_FOR_CHANNEL_OF_THIS_TYPE(30052, "Bitrate is too high for channel of this type"),
    UNAUTHORIZED(40001, "Unauthorized"),
    YOU_NEED_TO_VERIFY_YOUR_ACCOUNT_IN_ORDER_TO_PERFORM_THIS_ACTION(40002,
            "You need to verify your account in order to perform this action"),
    YOUR_ARE_OPENING_DIRECT_MESSAGES_TOO_FAST(40003, "You are opening direct messages too fast"),
    SEND_MESSAGES_HAS_BEEN_TEMPORARILY_DISABLED(40004, "Send messages has been temporarily disabled"),
    REQUEST_ENTITY_TOO_LARGE(40005, "Request entity too large. Try sending something smaller in size"),
    THIS_FEATURE_HAS_BEEN_TEMPORARILY_DISABLED_SERVER_SIDE(40006,
            "This feature has been temporarily disabled server-side"),
    THIS_USER_IS_BANNED_FROM_THIS_SERVER(40007, "The user is banned from this guild"),
    CONNECTION_HAS_BEEN_REVOKED(40012, "Connection has been revoked"),
    TARGET_USER_IS_NOT_CONNECTED_TO_VOICE(40032, "Target user is not connected to voice"),
    THIS_MESSAGE_HAS_ALREADY_BEEN_CROSSPOSTED(40033, "This message has already been crossposted"),
    AN_APPLICATION_COMMAND_WITH_THAT_NAME_ALREADY_EXISTS(40041,
            "An application command with that name already exists"),
    APPLICATION_INTERACTION_FAILED_TO_SEND(40043, "Application interaction failed to send"),
    CANNOT_SEND_A_MESSAGE_IN_A_FORUM_CHANNEL(40058, "Cannot send a message in a forum channel"),
    INTERACTION_HAS_ALREADY_BEEN_ACKNOWLEDGED(40060, "Interaction has already been acknowledged"),
    TAG_NAMES_MUST_BE_UNIQUE(40061, "Tag names must be unique"),
    SERVICE_RESOURCE_IS_BEING_RATE_LIMITED(40062, "Service resource is being rate limited"),
    THERE_ARE_NO_TAGS_AVAILABLE_THAT_CAN_BE_SET_BY_NON_MODERATORS(40066,
            "There are no tags available that can be set by non-moderators"),
    A_TAG_IS_REQUIRED_TO_CREATE_A_FORUM_POST_IN_THIS_CHANNEL(40067,
            "A tag is required to create a forum post in this channel"),
    MISSING_ACCESS(50001, "Missing access"),
    INVALID_ACCOUNT_TYPE(50002, "Invalid account type"),
    CANNOT_EXECUTE_ACTION_ON_A_DM_CHANNEL(50003, "Cannot execute action on a DM channel"),
    WIDGET_DISABLED(50004, "Widget Disabled"),
    CANNOT_EDIT_A_MESSAGE_AUTHORED_BY_ANOTHER_USER(50005, "Cannot edit a message authored by another user"),
    CANNOT_SEND_AN_EMPTY_MESSAGE(50006, "Cannot send an empty message"),
    CANNOT_SEND_MESSAGES_TO_THIS_USER(50007, "Cannot send messages to this user", CannotMessageUserException::new,
            RestRequestHttpResponseCode.FORBIDDEN),
    CANNOT_SEND_MESSAGES_IN_A_VOICE_CHANNEL(50008, "Cannot send messages in a voice channel"),
    CHANNEL_VERIFICATION_LEVEL_IS_TOO_HIGH(50009, "Channel verification level is too high"),
    OAUTH2_APPLICATION_DOES_NOT_HAVE_A_BOT(50010, "OAuth2 application does not have a bot"),
    OAUTH2_APPLICATION_LIMIT_REACHED(50011, "OAuth2 application limit reached"),
    INVALID_OAUTH_STATE(50012, "Invalid OAuth state"),
    MISSING_PERMISSIONS(50013, "Missing permissions"),
    INVALID_AUTHENTICATION_TOKEN(50014, "Invalid authentication token"),
    NOTE_IS_TOO_LONG(50015, "Note is too long"),
    PROVIDED_TOO_FEW_OR_TOO_MANY_MESSAGES_TO_DELETE(
            50016, "Provided too few or too many messages to delete. "
            + "Must provide at least 2 and fewer than 100 messages to delete."),
    INVALID_MFA_LEVEL(50017, "Invalid MFA level"),
    A_MESSAGE_CAN_ONLY_BE_PINNED_TO_THE_CHANNEL_IT_WAS_SENT_IN(
            50019, "A message can only be pinned to the channel it was sent in"),
    INVITE_CODE_WAS_EITHER_INVALID_OR_TAKEN(50020, "Invite code was either invalid or taken"),
    CANNOT_EXECUTE_ACTION_ON_A_SYSTEM_MESSAGE(50021, "Cannot execute action on a system message"),
    CANNOT_EXECUTE_ACTION_ON_THIS_CHANNEL_TYPE(50024, "Cannot execute action on this channel type"),
    INVALID_OAUTH2_ACCESS_TOKEN_PROVIDED(50025, "Invalid OAuth2 access token provided"),
    MISSING_REQUIRED_OAUTH2_SCOPE(50026, "Missing required OAuth2 scope"),
    INVALID_WEBHOOK_TOKEN_PROVIDED(50027, "Invalid webhook token provided"),
    INVALID_ROLE(50028, "Invalid role"),
    INVALID_RECIPIENTS(50033, "Invalid Recipient(s)"),
    A_MESSAGE_PROVIDED_WAS_TOO_OLD_TO_BULK_DELETE(50034, "A message provided was too old to bulk delete"),
    INVALID_FORM_BODY(50035, "Invalid Form Body"),
    AN_INVITE_WAS_ACCEPTED_TO_A_GUILD_THE_APPLICATIONS_BOT_IS_NOT_IN(
            50036, "An invite was accepted to a guild the application's bot is not in"),
    INVALID_ACTIVITY_ACTION(50039, "Invalid activity action"),
    INVALID_API_VERSION(50041, "Invalid API version"),
    FILE_UPLOADED_EXCEEDS_THE_MAXIMUM_SIZE(50045, "File uploaded exceeds the maximum size"),
    INVALID_FILE_UPLOADED(50046, "Invalid file uploaded"),
    CANNOT_SELF_REDEEM_THIS_GIFT(50054, "Cannot self-redeem this gift"),
    INVALID_SERVER(50055, "Invalid server"),
    INVALID_MESSAGE_TYPE(50068, "Invalid message type"),
    PAYMENT_SOURCE_REQUIRED_TO_REDEEM_GIFT(50070, "Payment source required to redeem gift"),
    CANNOT_DELETE_A_CHANNEL_REQUIRED_FOR_COMMUNITY_SERVERS(50074,
            "Cannot delete a channel required for Community guilds"),
    CANNOT_EDIT_STICKERS_WITHING_A_MESSAGE(50080, "Cannot edit stickers within a message"),
    INVALID_STICKER_SENT(50081, "Invalid sticker sent"),
    TRIED_TO_PERFORM_AN_OPERATION_ON_AN_ARCHIVED_THREAD(50083,
            "Tried to perform an operation on an archived thread, "
                    + "such as editing a message or adding a user to the thread"),
    INVALID_THREAD_NOTIFICATION_SETTINGS(50084, "Invalid thread notification settings"),
    BEFORE_VALUE_IS_EARLIER_THAN_THE_THREAD_CREATION_DATE(50085,
            "before value is earlier than the thread creation date"),
    COMMUNITY_SERVER_CHANNELS_MUST_BE_TEXT_CHANNELS(50086, "Community server channels must be text channels"),
    THIS_SERVER_IS_NOT_AVAILABLE_IN_YOUR_LOCATION(50095, "This server is not available in your location"),
    THIS_SERVER_NEEDS_MONETIZATION_ENABLED_IN_ORDER_TO_PERFORM_THIS_ACTION(50097,
            "This server needs monetization enabled in order to perform this action"),
    THIS_SERVER_NEEDS_MORE_BOOSTS_TO_PERFORM_THIS_ACTION(50101, "This server needs more boosts to perform this action"),
    THE_REQUEST_BODY_CONTAINS_INVALID_JSON(50109, "The request body contains invalid JSON."),
    OWNERSHIP_CANNOT_BE_TRANSFERRED_TO_A_BOT_USER(50132, "Ownership cannot be transferred to a bot user"),
    FAILED_TO_RESIZE_ASSET_BELOW_THE_MAXIMUM_SIZE(50138, "Failed to resize asset below the maximum size: 262144"),
    UPLOADED_FILE_NOT_FOUND(50146, "Uploaded file not found."),
    VOICE_MESSAGE_DO_NOT_SUPPORT_ADDITIONAL_CONTENT(50159, "Voice messages do not support additional content."),
    VOICE_MESSAGE_MUST_HAVE_SINGLE_AUDIO_ATTACHMENT(50160, "Voice messages must have a single audio attachment."),
    VOICE_MESSAGE_MUST_HAVE_SUPPORTING_METADATA(50161, "Voice messages must have supporting metadata."),
    VOICE_MESSAGE_CAN_NOT_BE_EDITED(50162, "Voice messages cannot be edited."),
    YOU_CAN_NOT_SEND_VOICE_MESSAGE_IN_THIS_CHANNEL(50173, "You cannot send voice messages in this channel."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_SEND_THIS_STICKER(50600, "You do not have permission to send this sticker."),
    TWO_FACTOR_IS_REQUIRED_FOR_THIS_OPERATION(60003, "Two factor is required for this operation"),
    NO_USERS_WITH_DISCORDTAGS_EXIST(80004, "No users with DiscordTags exist"),
    REACTION_BLOCKED(90001, "Reaction blocked", ReactionBlockedException::new, RestRequestHttpResponseCode.FORBIDDEN),
    USER_CAN_NOT_USE_BURST_REACTION(90002, "User can not use burst reaction"),
    APPLICATION_NOT_YET_AVAILABLE(110001, "\tApplication not yet available. Try again later"),
    API_RESOURCE_IS_CURRENTLY_OVERLOADED(130000, "API resource is currently overloaded. Try again a little later"),
    THE_STAGE_IS_ALREADY_OPEN(150006, "The Stage is already open"),
    CANNOT_REPLY_WITHOUT_PERMISSION_TO_READ_MESSAGE_HISTORY(160002,
            "Cannot reply without permission to read message history"),
    A_THREAD_HAS_ALREADY_BEEN_CREATED_FOR_THIS_MESSAGE(160004, "A thread has already been created for this message"),
    THREAD_IS_LOCKED(160005, "Thread is locked"),
    MAXIMUM_NUMBER_OF_ACTIVE_THREADS_REACHED(160006, "Maximum number of active threads reached"),
    MAXIMUM_NUMBER_OF_ACTIVE_ANNOUNCEMENT_THREADS_REACHED(160007,
            "Maximum number of active announcement threads reached"),
    INVALID_JSON_FOR_UPLOADED_LOTTIE_FILE(170001, "Invalid JSON for uploaded Lottie file"),
    UPLOADED_LOTTIES_CANNOT_CONTAIN_RASTERIZED_IMAGES_SUCH_AS_PNG_OR_JPEG(170002,
            "Uploaded Lotties cannot contain rasterized images such as PNG or JPEG"),
    STICKER_MAXIMUM_FRAMERATE_EXCEEDED(170003, "Sticker maximum framerate exceeded"),
    STICKER_FRAME_COUNT_EXCEEDS_MAXIMUM_OF_1000_FRAMES(170004, "Sticker frame count exceeds maximum of 1000 frames"),
    LOTTIE_ANIMATION_MAXIMUM_DIMENSIONS_EXCEEDED(170005, "Lottie animation maximum dimensions exceeded"),
    STICKER_FRAME_RATE_IS_EITHER_TOO_SMALL_OR_TOO_LARGE(170006,
            "Sticker frame rate is either too small or too large"),
    STICKER_ANIMATION_DURATION_EXCEEDS_MAXIMUM_OF_5_SECONDS(170007,
            "Sticker animation duration exceeds maximum of 5 seconds"),
    CANNOT_UPDATE_A_FINISHED_EVENT(180000, "Cannot update a finished event"),
    FAILED_TO_CREATE_STAGE_NEEDED_FOR_STAGE_EVENT(180002, "Failed to create stage needed for stage event"),
    MESSAGE_WAS_BLOCKED_BY_AUTOMATIC_MODERATION(200000, "Message was blocked by automatic moderation"),
    TITLE_WAS_BLOCKED_BY_AUTOMATIC_MODERATION(200001, "Title was blocked by automatic moderation"),
    WEBHOOKS_POSTED_TO_FORUM_CHANNELS_MUST_HAVE_A_THREAD_NAME_OR_THREAD_ID(220001,
            "Webhooks posted to forum channels must have a thread_name or thread_id"),
    WEBHOOKS_POSTED_TO_FORUM_CHANNELS_CANNOT_HAVE_BOTH_A_THREAD_NAME_AND_THREAD_ID(220002,
            "Webhooks posted to forum channels cannot have both a thread_name and thread_id"),
    WEBHOOKS_CAN_ONLY_CREATE_THREADS_IN_FORUM_CHANNELS(220003, "Webhooks can only create threads in forum channels"),
    WEBHOOK_SERVICES_CANNOT_BE_USED_IN_FORUM_CHANNELS(220004, "Webhook services cannot be used in forum channels"),
    MESSAGE_BLOCKED_BY_HARMFUL_LINKS_FILTER(240000, "Message blocked by harmful links filter");

    /**
     * A map for retrieving the enum instances by code.
     */
    private static final Map<Integer, RestRequestResultErrorCode> instanceByCode;

    /**
     * The actual numeric result code.
     */
    private final int code;

    /**
     * The textual meaning.
     */
    private final String meaning;

    /**
     * The discord exception instantiator that produces instances to throw for this kind of result code.
     */
    private final DiscordExceptionInstantiator<?> discordExceptionInstantiator;

    /**
     * The response code for which the given instantiator should be used.
     */
    private final RestRequestHttpResponseCode responseCode;

    static {
        instanceByCode = Collections.unmodifiableMap(
                Arrays.stream(values())
                        .collect(Collectors.toMap(RestRequestResultErrorCode::getCode, Function.identity())));
    }

    /**
     * Creates a new rest request result error code.
     *
     * @param code    The actual numeric close code.
     * @param meaning The textual meaning.
     */
    RestRequestResultErrorCode(int code, String meaning) {
        this(code, meaning, null, null);
    }

    /**
     * Creates a new rest request result error code.
     *
     * @param code                         The actual numeric close code.
     * @param meaning                      The textual meaning.
     * @param discordExceptionInstantiator The discord exception instantiator that produces
     *                                     instances to throw for this kind of result code.
     * @param responseCode                 The response code for which the given instantiator should be used.
     */
    RestRequestResultErrorCode(int code, String meaning, DiscordExceptionInstantiator<?> discordExceptionInstantiator,
                               RestRequestHttpResponseCode responseCode) {
        this.code = code;
        this.meaning = meaning;
        this.discordExceptionInstantiator = discordExceptionInstantiator;
        this.responseCode = responseCode;
    }

    /**
     * Gets the rest request result error code by actual numeric result code.
     *
     * @param code         The actual numeric close code.
     * @param responseCode The response code.
     * @return The web socket close code with the actual numeric result code.
     */
    public static Optional<RestRequestResultErrorCode> fromCode(int code, RestRequestHttpResponseCode responseCode) {
        return Optional.ofNullable(instanceByCode.get(code))
                .filter(errorCode -> errorCode.responseCode == responseCode);
    }

    /**
     * Gets the actual numeric result code.
     *
     * @return The actual numeric result code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the textual meaning.
     *
     * @return The textual meaning.
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * Gets the discord exception to throw for this kind of result code.
     *
     * @param origin   The origin of the exception.
     * @param message  The message of the exception.
     * @param request  The information about the request.
     * @param response The information about the response.
     * @return The discord exception to throw for this kind of result code.
     */
    public Optional<? extends DiscordException> getDiscordException(Exception origin, String message,
                                                                    RestRequestInformation request,
                                                                    RestRequestResponseInformation response) {
        return Optional.ofNullable(discordExceptionInstantiator)
                .map(instantiator -> instantiator.createInstance(origin, message, request, response))
                .filter(exception -> RestRequestHttpResponseCode.fromDiscordExceptionClass(exception.getClass())
                        .map(RestRequestHttpResponseCode::getCode)
                        .map(code -> code == response.getCode())
                        .orElse(true));
    }

}
