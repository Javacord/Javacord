package org.javacord.core.event.server.member;

import org.javacord.api.entity.server.Ban;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberBanEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerMemberBanEvent}.
 */
public class ServerMemberBanEventImpl extends ServerMemberEventImpl implements ServerMemberBanEvent {

    /**
     * Creates a new server member ban event.
     *
     * @param server The server of the event.
     * @param user The user of the event.
     */
    public ServerMemberBanEventImpl(Server server, User user) {
        super(server, user);
    }

    @Override
    public CompletableFuture<Optional<Ban>> requestBan() {
        return getServer().getBans()
                .thenApply(bans -> bans.stream()
                        .filter(ban -> ban.getUser().equals(getUser()))
                        .findAny());
    }
}
