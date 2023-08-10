package org.javacord.core.entity.server.scheduledevent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventUser;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import java.util.Optional;

public class ServerScheduledEventUserImpl implements ServerScheduledEventUser {

    private final ServerScheduledEvent serverScheduledEvent;
    private final User user;
    private final User member;

    /**
     * Creates a new server scheduled event user.
     * @param serverScheduledEvent The server scheduled event.
     * @param data The json data of the server scheduled event user.
     */
    public ServerScheduledEventUserImpl(ServerScheduledEvent serverScheduledEvent, JsonNode data) {
        this.serverScheduledEvent = serverScheduledEvent;
        this.user =
                new UserImpl((DiscordApiImpl) serverScheduledEvent.getApi(), data.get("user"), (MemberImpl) null, null);
        this.member = new UserImpl((DiscordApiImpl) serverScheduledEvent.getApi(), data.get("user"), data.get("member"),
                (ServerImpl) serverScheduledEvent.getServer());
    }

    @Override
    public ServerScheduledEvent getServerScheduledEvent() {
        return serverScheduledEvent;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Optional<User> getMember() {
        return Optional.ofNullable(member);
    }
}
