
package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.VoiceChannel;

import java.util.Optional;

/**
 * A user change activity event.
 */
public class UserJoinVoiceChannelEvent extends UserEvent {

    /**
     * The new voice-channel of the user.
     */
    private final VoiceChannel newChannel;

    /**
     * The previous voice-channel of the user.
     */
    private final VoiceChannel previousChannel;

    /**
     * Indicates whether this event is a move from one voice-channel to another.
     */
    private final boolean isMove;

    /**
     * Creates a new user change voice-channel event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param newChannel The new voice-channel of the user.
     * @param oldChannel The old voice-channel of the user.
     */
    public UserJoinVoiceChannelEvent(DiscordApi api, User user, VoiceChannel newChannel, VoiceChannel previousChannel) {
        super(api, user);
        this.newChannel = newChannel;
        this.previousChannel = previousChannel;
        this.isMove = previousChannel != null;
    }

    /**
     * Gets the new voice-channel of the user.
     *
     * @return The new voice-channel of the user.
     */
    public Optional<VoiceChannel> getNewChannel() {
        return Optional.ofNullable(this.newChannel);
    }

    /**
     * Gets the previous voice-channel of the user.
     *
     * @return The previous voice-channel of the user.
     */
    public Optional<VoiceChannel> getPreviousChannel() {
        return Optional.ofNullable(this.previousChannel);
    }

    /**
     * Tells whether this event is a move from one voice-channel to another.
     *
     * @return true if this event is a move from one voice-channel to another.
     */
    public boolean isMove() {
        return isMove;
    }

}
