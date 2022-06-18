package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.internal.ApplicationCommandUpdaterDelegate;

import java.util.HashMap;
import java.util.Map;

public abstract class ApplicationCommandUpdaterDelegateImpl<T extends ApplicationCommand>
        implements ApplicationCommandUpdaterDelegate<T> {

    protected long commandId;

    protected String name = null;

    protected Map<DiscordLocale, String> nameLocalizations = new HashMap<>();

    protected String description = null;

    protected Map<DiscordLocale, String> descriptionLocalizations = new HashMap<>();

    protected boolean defaultPermission = true;

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
    public void setDefaultPermission(boolean defaultPermission) {
        this.defaultPermission = defaultPermission;
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

        body.put("default_permission", defaultPermission);
    }
}
