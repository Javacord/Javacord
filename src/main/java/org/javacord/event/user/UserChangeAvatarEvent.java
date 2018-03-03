package org.javacord.event.user;

import org.javacord.DiscordApi;
import org.javacord.entity.Icon;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.user.User;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.DiscordApi;
import org.javacord.entity.Icon;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.user.User;
import org.javacord.util.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A user change avatar event.
 */
public class UserChangeAvatarEvent extends UserEvent {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(UserChangeAvatarEvent.class);

    /**
     * The new avatar hash of the user.
     */
    private final String newAvatarHash;

    /**
     * The old avatar hash of the user.
     */
    private final String oldAvatarHash;

    /**
     * Creates a new user change avatar event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param newAvatarHash The new avatar hash of the user.
     * @param oldAvatarHash The old avatar hash of the user.
     */
    public UserChangeAvatarEvent(DiscordApi api, User user, String newAvatarHash, String oldAvatarHash) {
      super(api, user);
      this.newAvatarHash = newAvatarHash;
      this.oldAvatarHash = oldAvatarHash;
    }

    /**
     * Gets the new avatar of the user.
     *
     * @return The new avatar of the user.
     */
    public Icon getNewAvatar() {
        return getAvatar(newAvatarHash);
    }

    /**
     * Checks if the new avatar is a default avatar.
     *
     * @return Whether the new avatar is a default avatar or not.
     */
    public boolean newAvatarIsDefaultAvatar() {
        return newAvatarHash == null;
    }

    /**
     * Gets the old avatar of the user.
     *
     * @return The old avatar of the user.
     */
    public Icon getOldAvatar() {
        return getAvatar(oldAvatarHash);
    }

    /**
     * Checks if the old avatar is a default avatar.
     *
     * @return Whether the old avatar is a default avatar or not.
     */
    public boolean oldAvatarIsDefaultAvatar() {
        return oldAvatarHash == null;
    }

    /**
     * Gets the avatar by its hash.
     *
     * @param avatarHash The hash of the avatar.
     * @return The icon with the given hash.
     */
    private Icon getAvatar(String avatarHash) {
        String url = "https://cdn.discordapp.com/embed/avatars/" +
                Integer.parseInt(getUser().getDiscriminator()) % 5 + ".png";
        if (avatarHash != null) {
            url = "https://cdn.discordapp.com/avatars/" + getUser().getIdAsString() + "/" + avatarHash +
                    (avatarHash.startsWith("a_") ? ".gif" : ".png");
        }
        try {
            return new ImplIcon(getApi(), new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
    }

}
