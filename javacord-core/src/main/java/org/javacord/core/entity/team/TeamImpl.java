package org.javacord.core.entity.team;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.team.Team;
import org.javacord.api.entity.team.TeamMember;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TeamImpl implements Team {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(TeamImpl.class);

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The id of the team.
     */
    private final long id;

    /**
     * The id of the owner.
     */
    private final long ownerId;

    /**
     * The avatar hash of the team.
     */
    private final String iconHash;

    /**
     * The name of the team.
     */
    private final String name;

    /**
     * The members of the team.
     */
    private final Set<TeamMember> members = new HashSet<>();

    /**
     * Creates a new team.
     *
     * @param api The discord api instance.
     * @param data The json data of the team.
     */
    public TeamImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;

        id = data.get("id").asLong();
        iconHash = data.path("icon").asText(null);
        name = data.get("name").asText();
        ownerId = data.get("owner_user_id").asLong();
        for (JsonNode member : data.get("members")) {
            members.add(new TeamMemberImpl(api, member));
        }
    }

    @Override
    public Optional<Icon> getIcon() {
        if (iconHash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new IconImpl(
                    getApi(),
                    new URL("https://" + Javacord.DISCORD_CDN_DOMAIN
                            + "/team-icons/" + getIdAsString() + "/" + iconHash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Set<TeamMember> getTeamMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public long getOwnerId() {
        return ownerId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }
}
