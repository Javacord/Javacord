package org.javacord.core.event.user;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.UserChangeAvatarEvent;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.util.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of {@link UserChangeAvatarEvent}.
 */
public class UserChangeAvatarEventImpl extends UserEventImpl implements UserChangeAvatarEvent {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(UserChangeAvatarEvent.class);

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
     * @param user The user of the event.
     * @param newAvatarHash The new avatar hash of the user.
     * @param oldAvatarHash The old avatar hash of the user.
     */
    public UserChangeAvatarEventImpl(User user, String newAvatarHash, String oldAvatarHash) {
        super(user);
        this.newAvatarHash = newAvatarHash;
        this.oldAvatarHash = oldAvatarHash;
    }

    @Override
    public Icon getNewAvatar() {
        return getAvatar(newAvatarHash);
    }

    @Override
    public boolean newAvatarIsDefaultAvatar() {
        return newAvatarHash == null;
    }

    @Override
    public Icon getOldAvatar() {
        return getAvatar(oldAvatarHash);
    }

    @Override
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
            return new IconImpl(getApi(), new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
    }

}
