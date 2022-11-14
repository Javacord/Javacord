package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.internal.ApplicationCommandBuilderDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ApplicationCommandBuilderDelegateImpl<T extends ApplicationCommand>
        implements ApplicationCommandBuilderDelegate<T> {

    protected String name;
    protected Map<DiscordLocale, String> nameLocalizations = new HashMap<>();
    protected String description;
    protected Map<DiscordLocale, String> descriptionLocalizations = new HashMap<>();

    protected Long defaultMemberPermissions = null;
    protected Boolean dmPermission = true;
    protected Boolean nsfw = false;

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

    @Override
    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    @Override
    public CompletableFuture<T> createGlobal(DiscordApi api) {
        return new RestRequest<T>(api, RestMethod.POST, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()))
                .setBody(getJsonBodyForApplicationCommand())
                .execute(result -> createInstance((DiscordApiImpl) api, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<T> createForServer(DiscordApi api, long server) {
        return new RestRequest<T>(
                api, RestMethod.POST, RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(api.getClientId()), String.valueOf(server))
                .setBody(getJsonBodyForApplicationCommand())
                .execute(result -> createInstance((DiscordApiImpl) api, result.getJsonBody()));
    }

    /**
     * Gets the JSON body for this application command.
     *
     * @return The JSON body for this application command.
     */
    public ObjectNode getJsonBodyForApplicationCommand() {
        ObjectNode jsonBody = JsonNodeFactory.instance.objectNode()
                .put("name", name);

        if (!nameLocalizations.isEmpty()) {
            ObjectNode nameLocalizationsJsonObject = jsonBody.putObject("name_localizations");
            nameLocalizations.forEach(
                    (locale, localization) -> nameLocalizationsJsonObject.put(locale.getLocaleCode(), localization));
        }

        jsonBody.put("description", description);

        if (!descriptionLocalizations.isEmpty()) {
            ObjectNode descriptionLocalizationsJsonObject = jsonBody.putObject("description_localizations");
            descriptionLocalizations.forEach(
                    (locale, localization) ->
                            descriptionLocalizationsJsonObject.put(locale.getLocaleCode(), localization));
        }

        jsonBody.put("default_member_permissions",
                defaultMemberPermissions != null ? String.valueOf(defaultMemberPermissions) : null);

        jsonBody.put("dm_permission", dmPermission);

        jsonBody.put("nsfw", nsfw);

        return jsonBody;
    }

    /**
     * Returns a created instance for the application command the builder is for.
     *
     * @param api      The DiscordApiImpl.
     * @param jsonNode The json of the application command.
     * @return An instance of application command from the JSON.
     */
    public abstract T createInstance(DiscordApiImpl api, JsonNode jsonNode);

}
