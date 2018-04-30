package org.javacord.core.event.user;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.UserChangeNicknameEvent;

import java.util.Optional;

/**
 * The implementation of {@link UserChangeNicknameEvent}.
 */
public class UserChangeNicknameEventImpl extends ServerUserEventImpl implements UserChangeNicknameEvent {

    /**
     * The new nickname of the user.
     */
    private final String newNickname;

    /**
     * The old nickname of the user.
     */
    private final String oldNickname;

    /**
     * Creates a new user change nickname event.
     *
     * @param user The user of the event.
     * @param server The server in which the user changed its nickname.
     * @param newNickname The new nickname of the user.
     * @param oldNickname The old nickname of the user.
     */
    public UserChangeNicknameEventImpl(User user, Server server, String newNickname, String oldNickname) {
        super(user, server);
        this.newNickname = newNickname;
        this.oldNickname = oldNickname;
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
