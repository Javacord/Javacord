package org.javacord.core.entity.auditlog;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.auditlog.AuditLog;
import org.javacord.api.entity.auditlog.AuditLogActionType;
import org.javacord.api.entity.auditlog.AuditLogChange;
import org.javacord.api.entity.auditlog.AuditLogChangeType;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.auditlog.AuditLogEntryTarget;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.VerificationLevel;
import org.javacord.api.entity.user.User;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.entity.permission.PermissionsImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link AuditLogEntry}.
 */
public class AuditLogEntryImpl implements AuditLogEntry {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(AuditLogEntryImpl.class);

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
    public AuditLogEntryImpl(AuditLog auditLog, JsonNode data) {
        this.auditLog = auditLog;
        this.id = data.get("id").asLong();
        this.userId = data.get("user_id").asLong();
        this.reason = data.has("reason") ? data.get("reason").asText() : null;
        this.actionType = AuditLogActionType.fromValue(data.get("action_type").asInt());
        this.target = data.has("target_id") && !data.get("target_id").isNull()
                ? new AuditLogEntryTargetImpl(data.get("target_id").asLong(), this) : null;
        if (data.has("changes")) {
            for (JsonNode changeJson : data.get("changes")) {
                AuditLogChangeType type = AuditLogChangeType.fromName(changeJson.get("key").asText());
                JsonNode oldValue = changeJson.get("old_value");
                JsonNode newValue = changeJson.get("new_value");
                AuditLogChange<?> change;
                String baseUrl = null;
                switch (type) {
                    // Strings
                    case NAME:
                    case VANITY_URL_CODE:
                    case TOPIC:
                    case CODE:
                    case NICK:
                        change = new AuditLogChangeImpl<>(type, oldValue != null ? oldValue.asText() : null,
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
                        change = new AuditLogChangeImpl<>(type, oldValue != null ? oldValue.asLong() : null,
                                newValue != null ? newValue.asLong() : null);
                        break;
                    // Integers
                    case AFK_TIMEOUT:
                    case POSITION:
                    case BITRATE:
                    case MAX_USES:
                    case USES:
                    case MAX_AGE:
                        change = new AuditLogChangeImpl<>(type, oldValue != null ? oldValue.asInt() : null,
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
                        change = new AuditLogChangeImpl<>(type, oldValue != null ? oldValue.asBoolean() : null,
                                newValue != null ? newValue.asBoolean() : null);
                        break;
                    // Misc
                    case ICON:
                        change = iconChange(
                                "https://" + Javacord.DISCORD_CDN_DOMAIN + "/icons/", type, oldValue, newValue);
                        break;
                    case SPLASH:
                        change = iconChange(
                                "https://" + Javacord.DISCORD_CDN_DOMAIN + "/splashes/", type, oldValue, newValue);
                        break;
                    case REGION:
                        Region oldRegion = Region.getRegionByKey(oldValue != null ? oldValue.asText() : "");
                        Region newRegion = Region.getRegionByKey(newValue != null ? newValue.asText() : "");
                        change = new AuditLogChangeImpl<>(type, oldRegion, newRegion);
                        break;
                    case MFA_LEVEL:
                        // TODO as soon as the enum exists
                        change = new AuditLogChangeImpl<>(type, oldValue, newValue);
                        break;
                    case VERIFICATION_LEVEL:
                        VerificationLevel oldVerificationLevel =
                                VerificationLevel.fromId(oldValue != null ? oldValue.asInt() : -1);
                        VerificationLevel newVerificationLevel =
                                VerificationLevel.fromId(newValue != null ? newValue.asInt() : -1);
                        change = new AuditLogChangeImpl<>(type, oldVerificationLevel, newVerificationLevel);
                        break;
                    case EXPLICIT_CONTENT_FILTER:
                        ExplicitContentFilterLevel oldExplicitContentFilterLevel =
                                ExplicitContentFilterLevel.fromId(oldValue != null ? oldValue.asInt() : -1);
                        ExplicitContentFilterLevel newExplicitContentFilterLevel =
                                ExplicitContentFilterLevel.fromId(newValue != null ? newValue.asInt() : -1);
                        change = new AuditLogChangeImpl<>(
                                type, oldExplicitContentFilterLevel, newExplicitContentFilterLevel);
                        break;
                    case DEFAULT_MESSAGE_NOTIFICATIONS:
                        DefaultMessageNotificationLevel oldDefaultMessageNotificationLevel =
                                DefaultMessageNotificationLevel.fromId(oldValue != null ? oldValue.asInt() : -1);
                        DefaultMessageNotificationLevel newDefaultMessageNotificationLevel =
                                DefaultMessageNotificationLevel.fromId(newValue != null ? newValue.asInt() : -1);
                        change = new AuditLogChangeImpl<>(
                                type, oldDefaultMessageNotificationLevel, newDefaultMessageNotificationLevel);
                        break;
                    case ROLE_ADD:
                    case ROLE_REMOVE:
                        // TODO Find a way to include role objects, without having duplicates
                        change = new AuditLogChangeImpl<>(type, oldValue, newValue);
                        break;
                    case PERMISSION_OVERWRITES:
                        // TODO Idea: Map<DiscordEntity, Permissions>
                        change = new AuditLogChangeImpl<>(type, oldValue, newValue);
                        break;
                    case PERMISSIONS:
                    case ALLOWED_PERMISSIONS:
                        Permissions oldPermissions = oldValue != null ? new PermissionsImpl(oldValue.asInt()) : null;
                        Permissions newPermissions = newValue != null ? new PermissionsImpl(newValue.asInt()) : null;
                        change = new AuditLogChangeImpl<>(type, oldPermissions, newPermissions);
                        break;
                    case COLOR:
                        Color oldColor = oldValue != null ? new Color(oldValue.asInt()) : null;
                        Color newColor = newValue != null ? new Color(newValue.asInt()) : null;
                        change = new AuditLogChangeImpl<>(type, oldColor, newColor);
                        break;
                    case DENIED_PERMISSIONS:
                        oldPermissions = oldValue != null ? new PermissionsImpl(0, oldValue.asInt()) : null;
                        newPermissions = newValue != null ? new PermissionsImpl(0, newValue.asInt()) : null;
                        change = new AuditLogChangeImpl<>(type, oldPermissions, newPermissions);
                        break;
                    case AVATAR:
                        baseUrl = "https://" + Javacord.DISCORD_CDN_DOMAIN + "/avatars/"
                                + getTarget().map(DiscordEntity::getIdAsString).orElse("0") + "/";
                        String oldUrl = oldValue != null ? (baseUrl + oldValue.asText()
                                + (oldValue.asText().startsWith("a_") ? ".gif" : ".png")) : null;
                        String newUrl = newValue != null ? (baseUrl + newValue.asText()
                                + (newValue.asText().startsWith("a_") ? ".gif" : ".png")) : null;
                        try {
                            Icon oldIcon = oldValue != null ? new IconImpl(getApi(), new URL(oldUrl)) : null;
                            Icon newIcon = newValue != null ? new IconImpl(getApi(), new URL(newUrl)) : null;
                            change = new AuditLogChangeImpl<>(type, oldIcon, newIcon);
                        } catch (MalformedURLException e) {
                            logger.warn("Seems like the icon's url is malformed! Please contact the developer!", e);
                            change = new AuditLogChangeImpl<>(AuditLogChangeType.UNKNOWN, oldValue, newValue);
                        }
                        break;
                    case TYPE:
                        // TODO Find a good way to both handle integers (channel type) and strings
                        change = new AuditLogChangeImpl<>(type, oldValue, newValue);
                        break;
                    default:
                        change = new AuditLogChangeImpl<>(type, oldValue, newValue);
                        break;
                }
                changes.add(change);
            }
        }
    }

    private AuditLogChange<?> iconChange(String baseUrl, AuditLogChangeType type, JsonNode oldVal, JsonNode newVal) {
        try {
            Icon oldIcon = oldVal != null ? new IconImpl(getApi(), new URL(baseUrl + getTarget()
                    .map(DiscordEntity::getIdAsString).orElse("0") + "/" + oldVal.asText() + ".png")) : null;
            Icon newIcon = newVal != null ? new IconImpl(getApi(), new URL(baseUrl + getTarget()
                    .map(DiscordEntity::getIdAsString).orElse("0") + "/" + newVal.asText() + ".png")) : null;
            return new AuditLogChangeImpl<>(type, oldIcon, newIcon);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the icon's url is malformed! Please contact the developer!", e);
            return new AuditLogChangeImpl<>(AuditLogChangeType.UNKNOWN, oldVal, newVal);
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

    @Override
    public boolean equals(Object o) {
        return (this == o)
               || !((o == null)
                    || (getClass() != o.getClass())
                    || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("AuditLogEntry (id: %s)", getIdAsString());
    }

}
