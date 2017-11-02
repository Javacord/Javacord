package de.btobastian.javacord.entities;

/**
 * Represents a game type.
 *
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#game-object-game-types">Discord docs</a>
 */
public enum GameType {

    /**
     * Represents a normal game, represented as "Playing Half-Life 3" for example.
     */
    GAME(0),

    /**
     * Represents streaming a game, represented as "Streaming Half-Life 3" for example.
     */
    STREAMING(1),

    /**
     * Represents listening to an application, represented as "Listening to Half-Life 3" for example.
     */
    LISTENING(2),

    /**
     * Represents watching an application, represented as "Watching Half-Life 3", for example.
     */
    WATCHING(3);

    private final int id;

    /**
     * Class constructor.
     *
     * @param id The id of the game type
     */
    GameType(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the game type.
     *
     * @return The id of the game type.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the game type by it's id.
     *
     * @param id The id of the game type
     * @return The game type with the given id or {@link GameType#GAME} if unknown id.
     */
    public static GameType getGameTypeById(int id) {
        switch (id) {
            case 0:
                return GAME;
            case 1:
                return STREAMING;
            default:
                return GAME;
        }
    }

}
