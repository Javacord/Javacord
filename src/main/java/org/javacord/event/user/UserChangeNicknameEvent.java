package org.javacord.event.user;

import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;

import java.util.Optional;

/**
 * A user change nickname event.
 */
public class UserChangeNicknameEvent extends UserEvent {

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
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param server The server in which the user changed its nickname.
     * @param newNickname The new nickname of the user.
     * @param oldNickname The old nickname of the user.
     */
    public UserChangeNicknameEvent(DiscordApi api, User user, Server server, String newNickname, String oldNickname) {
        super(api, user);
        this.server = server;
        this.newNickname = newNickname;
        this.oldNickname = oldNickname;
    }

    /**
     * Gets the server in which the user changed its nickname.
     *
     * @return The server in which the user changed its nickname.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Gets the new nickname of the user.
     *
     * @return The new nickname of the user.
     */
    public Optional<String> getNewNickname() {
        return Optional.ofNullable(newNickname);
    }

    /**
     * Gets the old nickname of the user.
     *
     * @return The old nickname of the user.
     */
    public Optional<String> getOldNickname() {
        return Optional.ofNullable(oldNickname);
    }

}
