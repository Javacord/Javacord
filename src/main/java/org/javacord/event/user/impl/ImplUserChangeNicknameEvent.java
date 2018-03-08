package org.javacord.event.user.impl;

import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.event.user.UserChangeNicknameEvent;

import java.util.Optional;

/**
 * The implementation of {@link UserChangeNicknameEvent}.
 */
public class ImplUserChangeNicknameEvent extends ImplUserEvent implements UserChangeNicknameEvent {

    /**
     * The new nickname of the user.
     */
    private final String newNickname;

    /**
     * The old nickname of the user.
     */
    private final String oldNickname;

    /**
     * The server in which the user changed its nickname.
     */
    private final Server server;

    /**
     * Creates a new user change nickname event.
     *
     * @param user The user of the event.
     * @param server The server in which the user changed its nickname.
     * @param newNickname The new nickname of the user.
     * @param oldNickname The old nickname of the user.
     */
    public ImplUserChangeNicknameEvent(User user, Server server, String newNickname, String oldNickname) {
        super(user);
        this.server = server;
        this.newNickname = newNickname;
        this.oldNickname = oldNickname;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Optional<String> getNewNickname() {
        return Optional.ofNullable(newNickname);
    }

    @Override
    public Optional<String> getOldNickname() {
        return Optional.ofNullable(oldNickname);
    }

}
