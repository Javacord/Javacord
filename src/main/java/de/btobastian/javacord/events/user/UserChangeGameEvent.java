package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.User;

import java.util.Optional;

/**
 * A user change game event.
 */
public class UserChangeGameEvent extends UserEvent {

    /**
     * The new game of the user.
     */
    private final Game newGame;

    /**
     * The old game of the user.
     */
    private final Game oldGame;

    /**
     * Creates a new user change game event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param newGame The new game of the user.
     * @param oldGame The old game of the user.
     */
    public UserChangeGameEvent(DiscordApi api, User user, Game newGame, Game oldGame) {
        super(api, user);
        this.newGame = newGame;
        this.oldGame = oldGame;
    }

    /**
     * Gets the old game of the user.
     *
     * @return The old game of the user.
     */
    public Optional<Game> getOldGame() {
        return Optional.ofNullable(oldGame);
    }

    /**
     * Gets the new game of the user.
     *
     * @return The new game of the user.
     */
    public Optional<Game> getNewGame() {
        return Optional.ofNullable(newGame);
    }

}
