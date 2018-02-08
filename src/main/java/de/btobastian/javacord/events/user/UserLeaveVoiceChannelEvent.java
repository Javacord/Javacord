
package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.VoiceChannel;

import java.util.Optional;

/**
 * A user change activity event.
 */
public class UserLeaveVoiceChannelEvent extends UserEvent {

    /**
     * The old voice-channel of the user.
     */
    private final VoiceChannel oldChannel;

    /**
     * Creates a new user change voice-channel event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param newChannel The new voice-channel of the user.
     * @param oldChannel The old voice-channel of the user.
     */
    public UserLeaveVoiceChannelEvent(DiscordApi api, User user, VoiceChannel oldChannel) {
        super(api, user);
        this.oldChannel = oldChannel;
    }

    /**
     * Gets the old voice-channel of the user.
     *
     * @return The old voice-channel of the user.
     */
    public Optional<VoiceChannel> getOldChannel() {
        return Optional.ofNullable(oldChannel);
    }

}
