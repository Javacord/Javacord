package de.btobastian.javacord.entities.auditlog.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.DefaultMessageNotificationLevel;
import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.ExplicitContentFilterLevel;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.Region;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.VerificationLevel;
import de.btobastian.javacord.entities.auditlog.AuditLog;
import de.btobastian.javacord.entities.auditlog.AuditLogActionType;
import de.btobastian.javacord.entities.auditlog.AuditLogChange;
import de.btobastian.javacord.entities.auditlog.AuditLogChangeType;
import de.btobastian.javacord.entities.auditlog.AuditLogEntry;
import de.btobastian.javacord.entities.auditlog.AuditLogEntryTarget;
import de.btobastian.javacord.entities.impl.ImplIcon;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link AuditLogEntry}.
 */
public class ImplAuditLogEntry implements AuditLogEntry {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplAuditLogEntry.class);

    /**
     * The id of the entry.
     */
    private final long id;

    /**
     * The audit log this entry belongs to.
     */
    private final AuditLog auditLog;

    /**
     * The id of the user who made the changes.
     */
    private final long userId;

    /**
     * The reason of the entry.
     */
    private final String reason;

    /**
     * The type of the entry.
     */
    private final AuditLogActionType actionType;

    /**
     * The target of the entry.
     */
    private final AuditLogEntryTarget target;

    /**
     * A list with all changes.
     */
    private final List<AuditLogChange<?>> changes = new ArrayList<>();

    /**
     * Creates a new audit log entry.
     *
     * @param auditLog The audit log this entry belongs to.
     * @param data The json data of the audit log entry.
     */
    public ImplAuditLogEntry(AuditLog auditLog, JsonNode data) {
        this.auditLog = auditLog;
        this.id = data.get("id").asLong();
        this.userId = data.get("user_id").asLong();
        this.reason = data.has("reason") ? data.get("reason").asText() : null;
        this.actionType = AuditLogActionType.fromValue(data.get("action_type").asInt());
        this.target = data.has("target_id") && !data.get("target_id").isNull() ?
                new ImplAuditLogEntryTarget(data.get("target_id").asLong(), this) : null;
        if (data.has("changes")) {
            for (JsonNode changeJson : data.get("changes")) {
                AuditLogChangeType type = AuditLogChangeType.fromName(changeJson.get("key").asText());
                JsonNode oldValue = changeJson.get("old_value");
                JsonNode newValue = changeJson.get("new_value");
                AuditLogChange<?> change;
                String baseUrl;
                switch (type) {
                    // Strings
                    case NAME:
                    case VANITY_URL_CODE:
                    case TOPIC:
                    case CODE:
                    case NICK:
                        change = new ImplAuditLogChange<>(type, oldValue != null ? oldValue.asText() : null,
                                newValue != null ? newValue.asText() : null);
                        break;
                    // Snowflakes (aka ids)
                    case OWNER_ID:
                    case AFK_CHANNEL_ID:
                    case WIDGET_CHANNEL_ID:
                    case APPLICATION_ID:
                    case CHANNEL_ID:
                    case INVITER_ID:
                    case ID:
                        change = new ImplAuditLogChange<>(type, oldValue != null ? oldValue.asLong() : null,
                                newValue != null ? newValue.asLong() : null);
                        break;
                    // Integers
                    case AFK_TIMEOUT:
                    case POSITION:
                    case BITRATE:
                    case MAX_USES:
                    case USES:
                    case MAX_AGE:
                        change = new ImplAuditLogChange<>(type, oldValue != null ? oldValue.asInt() : null,
                                newValue != null ? newValue.asInt() : null);
                        break;
                    // Boolean
                    case WIDGET_ENABLED:
                    case NSFW:
                    case DISPLAY_SEPARATELY:
                    case MENTIONABLE:
                    case TEMPORARY:
                    case DEAF:
                    case MUTE:
                        change = new ImplAuditLogChange<>(type, oldValue != null ? oldValue.asBoolean() : null,
                                newValue != null ? newValue.asBoolean() : null);
                        break;
                    // Misc
                    case ICON:
                        baseUrl = "https://cdn.discordapp.com/icons/";
                    case SPLASH:
                        baseUrl = "https://cdn.discordapp.com/splashes/";
                        try {
                            Icon oldIcon = oldValue != null ? new ImplIcon(getApi(), new URL(baseUrl + getTarget()
                                    .map(DiscordEntity::getId).orElse(0L) + "/" + oldValue.asText() + ".png")) : null;
                            Icon newIcon = newValue != null ? new ImplIcon(getApi(), new URL(baseUrl + getTarget()
                                    .map(DiscordEntity::getId).orElse(0L) + "/" + oldValue.asText() + ".png")) : null;
                            change = new ImplAuditLogChange<>(type, oldIcon, newIcon);
                        } catch (MalformedURLException e) {
                            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
                            change = new ImplAuditLogChange<>(AuditLogChangeType.UNKNOWN, oldValue, newValue);
                        }
                        break;
                    case REGION:
                        Region oldRegion = Region.getRegionByKey(oldValue != null ? oldValue.asText() : "");
                        Region newRegion = Region.getRegionByKey(newValue != null ? newValue.asText() : "");
                        change = new ImplAuditLogChange<>(type, oldRegion, newRegion);
                        break;
                    case MFA_LEVEL:
                        // TODO as soon as the enum exists
                        change = new ImplAuditLogChange<>(type, oldValue, newValue);
                        break;
                    case VERIFICATION_LEVEL:
                        VerificationLevel oldVerificationLevel =
                                VerificationLevel.fromId(oldValue != null ? oldValue.asInt() : -1);
                        VerificationLevel newVerificationLevel =
                                VerificationLevel.fromId(newValue != null ? newValue.asInt() : -1);
                        change = new ImplAuditLogChange<>(type, oldVerificationLevel, newVerificationLevel);
                        break;
                    case EXPLICIT_CONTENT_FILTER:
                        ExplicitContentFilterLevel oldExplicitContentFilterLevel =
                                ExplicitContentFilterLevel.fromId(oldValue != null ? oldValue.asInt() : -1);
                        ExplicitContentFilterLevel newExplicitContentFilterLevel =
                                ExplicitContentFilterLevel.fromId(newValue != null ? newValue.asInt() : -1);
                        change = new ImplAuditLogChange<>(
                                type, oldExplicitContentFilterLevel, newExplicitContentFilterLevel);
                        break;
                    case DEFAULT_MESSAGE_NOTIFICATIONS:
                        DefaultMessageNotificationLevel oldDefaultMessageNotificationLevel =
                                DefaultMessageNotificationLevel.fromId(oldValue != null ? oldValue.asInt() : -1);
                        DefaultMessageNotificationLevel newDefaultMessageNotificationLevel =
                                DefaultMessageNotificationLevel.fromId(newValue != null ? newValue.asInt() : -1);
                        change = new ImplAuditLogChange<>(
                                type, oldDefaultMessageNotificationLevel, newDefaultMessageNotificationLevel);
                        break;
                    case ROLE_ADD:
                    case ROLE_REMOVE:
                        // TODO Find a way to include role objects, without having duplicates
                        change = new ImplAuditLogChange<>(type, oldValue, newValue);
                        break;
                    case PERMISSION_OVERWRITES:
                        // TODO Idea: Map<DiscordEntity, Permissions>
                        change = new ImplAuditLogChange<>(type, oldValue, newValue);
                        break;
                    case PERMISSIONS:
                    case ALLOWED_PERMISSIONS:
                        Permissions oldPermissions = oldValue != null ? new ImplPermissions(oldValue.asInt()) : null;
                        Permissions newPermissions = newValue != null ? new ImplPermissions(newValue.asInt()) : null;
                        change = new ImplAuditLogChange<>(type, oldPermissions, newPermissions);
                        break;
                    case COLOR:
                        Color oldColor = oldValue != null ? new Color(oldValue.asInt()) : null;
                        Color newColor = newValue != null ? new Color(newValue.asInt()) : null;
                        change = new ImplAuditLogChange<>(type, oldColor, newColor);
                        break;
                    case DENIED_PERMISSIONS:
                        oldPermissions = oldValue != null ? new ImplPermissions(0, oldValue.asInt()) : null;
                        newPermissions = newValue != null ? new ImplPermissions(0, newValue.asInt()) : null;
                        change = new ImplAuditLogChange<>(type, oldPermissions, newPermissions);
                        break;
                    case AVATAR:
                        baseUrl = "https://cdn.discordapp.com/avatars/" +
                                getTarget().map(DiscordEntity::getId).orElse(0L) + "/";
                        String oldUrl = oldValue != null ? (baseUrl + oldValue.asText() +
                                (oldValue.asText().startsWith("a_") ? ".gif" : ".png")) : null;
                        String newUrl = newValue != null ? (baseUrl + newValue.asText() +
                                (newValue.asText().startsWith("a_") ? ".gif" : ".png")) : null;
                        try {
                            Icon oldIcon = oldValue != null ? new ImplIcon(getApi(), new URL(oldUrl)) : null;
                            Icon newIcon = newValue != null ? new ImplIcon(getApi(), new URL(newUrl)) : null;
                            change = new ImplAuditLogChange<>(type, oldIcon, newIcon);
                        } catch (MalformedURLException e) {
                            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
                            change = new ImplAuditLogChange<>(AuditLogChangeType.UNKNOWN, oldValue, newValue);
                        }
                        break;
                    case TYPE:
                        // TODO Find a good way to both handle integers (channel type) and strings
                        change = new ImplAuditLogChange<>(type, oldValue, newValue);
                        break;
                    default:
                        change = new ImplAuditLogChange<>(type, oldValue, newValue);
                        break;
                }
                changes.add(change);
            }
        }
    }

    @Override
    public DiscordApi getApi() {
        return auditLog.getApi();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public AuditLog getAuditLog() {
        return auditLog;
    }

    @Override
    public CompletableFuture<User> getUser() {
        return getApi().getUserById(userId);
    }

    @Override
    public Optional<String> getReason() {
        return Optional.ofNullable(reason);
    }

    @Override
    public AuditLogActionType getType() {
        return actionType;
    }

    @Override
    public Optional<AuditLogEntryTarget> getTarget() {
        return Optional.ofNullable(target);
    }

    @Override
    public List<AuditLogChange<?>> getChanges() {
        return changes;
    }
}
