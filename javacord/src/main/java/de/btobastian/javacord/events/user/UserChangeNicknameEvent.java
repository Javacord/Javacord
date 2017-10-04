package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

import java.util.Optional;

/**
 * A user change nickname event.
 */
public class UserChangeNicknameEvent extends UserEvent {

    /**
     * The old nickname of the user.
     */
    private final String oldNickname;

    /**
     * The server in which the user changed his nickname.
     */
    private final Server server;

    /**
     * Creates a new user change nickname event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param oldNickname The old nickname of the user.
     * @param server The server in which the user changed his nickname.
     */
    public UserChangeNicknameEvent(DiscordApi api, User user, String oldNickname, Server server) {
        super(api, user);
        this.oldNickname = oldNickname;
        this.server = server;
    }

    /**
     * Gets the old nickname of the user.
     *
     * @return The old nickname of the user.
     */
    public Optional<String> getOldNickname() {
        return Optional.ofNullable(oldNickname);
    }

    /**
     * Gets the new nickname of the user.
     *
     * @return The new nickname of the user.
     */
    public Optional<String> getNewNickname() {
        // TODO return getUser().getNickname(server);
        return Optional.empty();
    }

}
