package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class ApplicationCommandImpl implements ApplicationCommand {

    private final DiscordApiImpl api;
    private final long id;
    private final long applicationId;
    private final String name;
    private final Map<DiscordLocale, String> nameLocalizations = new HashMap<>();
    private final String description;
    private final Map<DiscordLocale, String> descriptionLocalizations = new HashMap<>();
    private final EnumSet<PermissionType> defaultMemberPermission;
    private final boolean dmPermission;
    private final boolean nsfw;

    private final Server server;
    private final Long serverId;

    /**
     * Class constructor.
     *
     * @param api  The api instance.
     * @param data The json data of the application command.
     */
    protected ApplicationCommandImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        id = data.get("id").asLong();
        applicationId = data.get("application_id").asLong();
        name = data.get("name").asText();
        data.path("name_localizations").fields().forEachRemaining(e ->
                nameLocalizations.put(DiscordLocale.fromLocaleCode(e.getKey()), e.getValue().asText()));
        description = data.get("description").asText();
        Long defaultMemberPermissionsBitset = data.hasNonNull("default_member_permissions")
                ? data.get("default_member_permissions").asLong() : null;
        defaultMemberPermission = defaultMemberPermissionsBitset != null
                ? Arrays.stream(PermissionType.values())
                .filter(type -> type.isSet(defaultMemberPermissionsBitset))
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PermissionType.class))) : null;
        data.path("description_localizations").fields().forEachRemaining(e ->
                descriptionLocalizations.put(DiscordLocale.fromLocaleCode(e.getKey()), e.getValue().asText()));
        serverId = data.has("guild_id")
                ? data.get("guild_id").asLong()
                : null;
        server = serverId != null
                ? api.getPossiblyUnreadyServerById(serverId).orElse(null)
                : null;
        dmPermission = server == null && (!data.has("dm_permission") || data.get("dm_permission").asBoolean());
        nsfw = data.has("nsfw") && data.get("nsfw").asBoolean();
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getApplicationId() {
        return applicationId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<DiscordLocale, String> getNameLocalizations() {
        return Collections.unmodifiableMap(nameLocalizations);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Map<DiscordLocale, String> getDescriptionLocalizations() {
        return Collections.unmodifiableMap(descriptionLocalizations);
    }

    @Override
    public Optional<EnumSet<PermissionType>> getDefaultRequiredPermissions() {
        return defaultMemberPermission != null
                ? Optional.of(EnumSet.copyOf(defaultMemberPermission)) : Optional.empty();
    }

    @Override
    public boolean isDisabledByDefault() {
        return defaultMemberPermission.isEmpty();
    }

    @Override
    public boolean isEnabledInDms() {
        return server != null && dmPermission;
    }

    @Override
    public Optional<Long> getServerId() {
        return Optional.ofNullable(serverId);
    }

    @Override
    public Optional<Server> getServer() {
        return Optional.ofNullable(server);
    }

    @Override
    public boolean isGlobalApplicationCommand() {
        return serverId == null;
    }

    @Override
    public boolean isServerApplicationCommand() {
        return serverId != null;
    }

    @Override
    public boolean isNsfw() {
        return nsfw;
    }

    @Override
    public CompletableFuture<Void> delete() {
        return (isGlobalApplicationCommand()
                ? new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(getApplicationId()), getIdAsString())
                : new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(getApplicationId()), Long.toUnsignedString(serverId), getIdAsString()))
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> deleteGlobal() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(getApplicationId()), getIdAsString())
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> deleteForServer(long server) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_APPLICATION_COMMANDS)
                .setUrlParameters(String.valueOf(getApplicationId()), String.valueOf(server), getIdAsString())
                .execute(result -> null);
    }
}
