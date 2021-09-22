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
 * <a href="https://discord.com/developers/docs/topics/response-codes#json-error-response">Discord</a>.
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
    UNKNOWN_BAN(10026, "Unknown ban"),
    UNKNOWN_SKU(10027, "Unknown SKU"),
    UNKNOWN_STORE_LISTING(10028, "Unknown store listing"),
    UNKNOWN_ENTITLEMENT(10029, "Unknown entitlement"),
    UNKNOWN_BUILD(10030, "Unknown build"),
    UNKNOWN_LOBBY(10031, "Unknown lobby"),
    UNKNOWN_BRANCH(10032, "Unknown branch"),
    UNKNOWN_REDISTRIBUTABLE(10036, "Unknown redistributable"),
    UNKNOWN_SERVER_TEMPLATE(10057, "Unknown server template"),
    UNKNOWN_INTERACTION(10062, "Unknown interaction"),
    UNKNOWN_APPLICATION_COMMAND(10063, "Unknown application command"),
    BOTS_CANNOT_USE_THIS_ENDPOINT(20001, "Bots cannot use this endpoint"),
    ONLY_BOTS_CAN_USE_THIS_ENDPOINT(20002, "Only bots can use this endpoint"),
    MESSAGE_CANNOT_BE_EDITED_DUE_TO_ANNOUNCEMENT_RATE_LIMIT(20022,
            "This message cannot be edited due to announcement rate limits"),
    THE_CHANNEL_YOU_ARE_WRITING_HAS_HIT_THE_WRITE_RATE_LIMIT(20028,
            "The channel you are writing has hit the write rate limit"),
    YOUR_STAGE_TOPIC_CONTAINS_WORDS_THAT_ARE_NOT_ALLOWED_FOR_PUBLIC_STAGES(20031,
            "Your Stage topic contains words that are not allowed for public Stages"),
    MAXIMUM_NUMBER_OF_GUILDS_REACHED(30001, "Maximum number of guilds reached (100)"),
    MAXIMUM_NUMBER_OF_FRIENDS_REACHED(30002, "Maximum number of friends reached (1000)"),
    MAXIMUM_NUMBER_OF_PINS_REACHED(30003, "Maximum number of pins reached (50)"),
    MAXIMUM_NUMBER_OF_GUILD_ROLES_REACHED(30005, "Maximum number of guild roles reached (250)"),
    MAXIMUM_NUMBER_OF_WEBHOOKS_REACHED(30007, "Maximum number of webhooks reached (10)"),
    TOO_MANY_REACTIONS(30010, "Too many reactions"),
    MAXIMUM_NUMBER_OF_GUILD_CHANNELS_REACHED(30013, "Maximum number of guild channels reached (500)"),
    MAXIMUM_NUMBER_OF_ATTACHMENTS_IN_A_MESSAGE_REACHED(30015,
            "Maximum number of attachments in a message reached (10)"),
    MAXIMUM_NUMBER_OF_INVITES_REACHED(30016, "Maximum number of invites reached (1000)"),
    MAXIMUM_NUMBER_OF_ANIMATED_EMOJIS_REACHED(30018, "Maximum number of animated emojis reached"),
    MAXIMUM_NUMBER_OF_SERVER_MEMBERS_REACHED(30019, "Maximum number of server members reached"),
    SERVER_ALREADY_HAS_A_TEMPLATE(30031, "Server already has a template"),
    UNAUTHORIZED(40001, "Unauthorized"),
    YOU_NEED_TO_VERIFY_YOUR_ACCOUNT_IN_ORDER_TO_PERFORM_THIS_ACTION(40002,
            "You need to verify your account in order to perform this action"),
    REQUEST_ENTITY_TOO_LARGE(40005, "Request entity too large. Try sending something smaller in size"),
    THIS_FEATURE_HAS_BEEN_TEMPORARILY_DISABLED_SERVER_SIDE(40006,
            "This feature has been temporarily disabled server-side"),
    THIS_USER_IS_BANNED_FROM_THIS_SERVER(40007, "The user is banned from this guild"),
    THIS_MESSAGE_HAS_ALREADY_BEEN_CROSSPOSTED(40033, "This message has already been crossposted"),
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
    A_MESSAGE_CAN_ONLY_BE_PINNED_TO_THE_CHANNEL_IT_WAS_SENT_IN(
            50019, "A message can only be pinned to the channel it was sent in"),
    INVITE_CODE_WAS_EITHER_INVALID_OR_TAKEN(50020, "Invite code was either invalid or taken"),
    CANNOT_EXECUTE_ACTION_ON_A_SYSTEM_MESSAGE(50021, "Cannot execute action on a system message"),
    CANNOT_EXECUTE_ACTION_ON_THIS_CHANNEL_TYPE(50024, "Cannot execute action on this channel type"),
    INVALID_OAUTH2_ACCESS_TOKEN_PROVIDED(50025, "Invalid OAuth2 access token provided"),
    INVALID_WEBHOOK_TOKEN_PROVIDED(50027, "Invalid webhook token provided"),
    INVALID_RECIPIENTS(50033, "Invalid Recipient(s)"),
    A_MESSAGE_PROVIDED_WAS_TOO_OLD_TO_BULK_DELETE(50034, "A message provided was too old to bulk delete"),
    INVALID_FORM_BODY(50035, "Invalid Form Body"),
    AN_INVITE_WAS_ACCEPTED_TO_A_GUILD_THE_APPLICATIONS_BOT_IS_NOT_IN(
            50036, "An invite was accepted to a guild the application's bot is not in"),
    INVALID_API_VERSION(50041, "Invalid API version"),
    CANNOT_DELETE_A_CHANNEL_REQUIRED_FOR_COMMUNITY_SERVERS(50074,
            "Cannot delete a channel required for Community guilds"),
    INVALID_STICKER_SENT(50081, "Invalid sticker sent"),
    TWO_FACTOR_IS_REQUIRED_FOR_THIS_OPERATION(60003, "Two factor is required for this operation"),
    REACTION_BLOCKED(90001, "Reaction blocked", ReactionBlockedException::new, RestRequestHttpResponseCode.FORBIDDEN),
    API_RESOURCE_IS_CURRENTLY_OVERLOADED(130000, "API resource is currently overloaded. Try again a little later");

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
     * @param code The actual numeric close code.
     * @param meaning The textual meaning.
     */
    RestRequestResultErrorCode(int code, String meaning) {
        this(code, meaning, null, null);
    }

    /**
     * Creates a new rest request result error code.
     *
     * @param code The actual numeric close code.
     * @param meaning The textual meaning.
     * @param discordExceptionInstantiator The discord exception instantiator that produces
     *                                     instances to throw for this kind of result code.
     * @param responseCode The response code for which the given instantiator should be used.
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
     * @param code The actual numeric close code.
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
     * @param origin The origin of the exception.
     * @param message The message of the exception.
     * @param request The information about the request.
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
