package de.btobastian.javacord.entities.message;

import de.btobastian.javacord.entities.CustomEmoji;
import de.btobastian.javacord.entities.User;
import org.apache.http.concurrent.FutureCallback;

import java.util.List;
import java.util.concurrent.Future;

public interface Reaction {

    /**
     * Gets the amount of people reacted with this emoji.
     *
     * @return The amount of people reacted with this emoji.
     */
    public int getCount();

    /**
     * Gets whether you used this reaction or not.
     *
     * @return Whether you used this reaction or not.
     */
    public boolean isUsedByYou();

    /**
     * Gets whether the reaction is a custom emoji or an unicode emoji.
     *
     * @return Whether the reaction is a custom emoji or an unicode emoji.
     */
    public boolean isCustomEmoji();

    /**
     * Gets whether the reaction is an unicode reaction or a custom emoji.
     *
     * @return Whether the reaction is a custom emoji or an unicode reaction.
     */
    public boolean isUnicodeEmoji();

    /**
     * Gets the custom emoji of this reaction.
     *
     * @return The custom emoji of this reaction or <code>null</code> if an unicode reaction was used.
     */
    public CustomEmoji getCustomEmoji();

    /**
     * Gets the unicode emoji of this reaction.
     *
     * @return The unicode emoji of this reaction or <code>null</code> of a custom emoji was used.
     */
    public String getUnicodeEmoji();

    /**
     * Gets a list of users who used this reaction.
     *
     * @return A list of users who used this reaction.
     */
    public Future<List<User>> getReactors();

    /**
     * Gets a list of users who used this reaction.
     *
     * @param callback The callback which will be informed when the reactors were fetched or fetching failed.
     * @return A list of users who used this reaction.
     */
    public Future<List<User>> getReactors(FutureCallback<List<User>> callback);

    /**
     * Removes an user of the reactors list.
     *
     * @param user The user to remove.
     * @return A future which tells us if the removal was successful.
     */
    public Future<Void> removeReactor(User user);

}
