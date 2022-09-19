package org.javacord.core.entity.activity;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.ApplicationInfo;
import org.javacord.api.entity.ApplicationOwner;
import org.javacord.api.entity.team.Team;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.ApplicationOwnerImpl;
import org.javacord.core.entity.team.TeamImpl;

import java.util.Optional;

/**
 * The implementation of {@link ApplicationInfo}.
 */
public class ApplicationInfoImpl implements ApplicationInfo {

    private final long clientId;
    private final String name;
    private final String description;
    private final boolean publicBot;
    private final boolean botRequiresCodeGrant;
    private final ApplicationOwner owner;
    private final Team team;

    /**
     * Creates a new application info object.
     *
     * @param api The discord api.
     * @param data The json data of the application.
     */
    public ApplicationInfoImpl(DiscordApi api, JsonNode data) {

        clientId = data.get("id").asLong();
        name = data.get("name").asText();
        description = data.get("description").asText();
        publicBot = data.get("bot_public").asBoolean();
        botRequiresCodeGrant = data.get("bot_require_code_grant").asBoolean();
        team = data.hasNonNull("team") ? new TeamImpl((DiscordApiImpl) api, data.get("team")) : null;
        // Discord appears to send dummy owner data as a fallback, so we have to check for a team
        owner = (team == null) ? new ApplicationOwnerImpl(api, data.get("owner")) : null;
    }

    @Override
    public long getClientId() {
        return clientId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isPublicBot() {
        return publicBot;
    }

    @Override
    public boolean botRequiresCodeGrant() {
        return botRequiresCodeGrant;
    }

    @Override
    public Optional<ApplicationOwner> getOwner() {
        return Optional.ofNullable(owner);
    }

    @Override
    public Optional<Team> getTeam() {
        return Optional.ofNullable(team);
    }
}
