package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.internal.ApplicationCommandUpdaterDelegate;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public abstract class ApplicationCommandUpdaterDelegateImpl<T extends ApplicationCommand>
        implements ApplicationCommandUpdaterDelegate<T> {

    protected long commandId;

    protected String name = null;

    protected Map<DiscordLocale, String> nameLocalizations = new HashMap<>();

    protected String description = null;

    protected Map<DiscordLocale, String> descriptionLocalizations = new HashMap<>();

    protected Long defaultMemberPermissions = -1L;

    protected Boolean dmPermission = null;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addNameLocalization(DiscordLocale locale, String localization) {
        nameLocalizations.put(locale, localization);
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void addDescriptionLocalization(DiscordLocale locale, String localization) {
        descriptionLocalizations.put(locale, localization);
    }

    @Override
    public void setDefaultEnabledForPermissions(EnumSet<PermissionType> requiredPermissions) {
        this.defaultMemberPermissions = requiredPermissions.stream().mapToLong(PermissionType::getValue).sum();
    }

    @Override
    public void setDefaultEnabledForEveryone() {
        this.defaultMemberPermissions = null;
    }

    @Override
    public void setDefaultDisabled() {
        this.defaultMemberPermissions = 0L;
    }

    @Override
    public void setEnabledInDms(boolean enabledInDms) {
        this.dmPermission = enabledInDms;
    }

    protected void prepareBody(ObjectNode body) {
        if (name != null && !name.isEmpty()) {
            body.put("name", name);
        }

        if (!nameLocalizations.isEmpty()) {
            ObjectNode nameLocalizationsJsonObject = body.putObject("name_localizations");
            nameLocalizations.forEach(
                    (locale, localization) -> nameLocalizationsJsonObject.put(locale.getLocaleCode(), localization));
        }

        if (description != null && !description.isEmpty()) {
            body.put("description", description);
        }

        if (!descriptionLocalizations.isEmpty()) {
            ObjectNode descriptionLocalizationsJsonObject = body.putObject("description_localizations");
            descriptionLocalizations.forEach(
                    (locale, localization) ->
                            descriptionLocalizationsJsonObject.put(locale.getLocaleCode(), localization));
        }

        if (defaultMemberPermissions == null || defaultMemberPermissions != -1L) {
            body.put("default_member_permissions",
                    defaultMemberPermissions != null ? String.valueOf(defaultMemberPermissions) : null);
        }

        if (dmPermission != null) {
            body.put("dm_permission", dmPermission);
        }
    }
}
