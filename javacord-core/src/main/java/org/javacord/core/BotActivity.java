package org.javacord.core;

import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.core.entity.activity.ActivityImpl;

/**
 * Used to set the activity of the bot.
 */
public class BotActivity {
    /**
     * Sets the activity of the bot to "Watching".
     *
     * @param name The name of the activity.
     * @param url The url of the activity.
     * @param state The state of the activity.
     * @return The bots activity.
     */
    public static Activity watching(String name, String url, String state) {
        return new ActivityImpl(ActivityType.WATCHING, name, url, state);
    }

    /**
     * Sets the activity of the bot to "Watching".
     *
     * @param name The name of the activity.
     * @param url The url of the activity.
     * @return The bots activity.
     */
    public static Activity watching(String name, String url) {
        return new ActivityImpl(ActivityType.WATCHING, name, url, null);
    }

    /**
     * Sets the activity of the bot to "Playing".
     *
     * @param name The name of the activity.
     * @param state The state of the activity.
     * @return The bots activity.
     */
    public static Activity playing(String name, String state) {
        return new ActivityImpl(ActivityType.PLAYING, name, null, state);
    }

    /**
     * Sets the activity of the bot to "Playing".
     *
     * @param name The name of the activity.
     * @return The bots activity.
     */
    public static Activity playing(String name) {
        return new ActivityImpl(ActivityType.PLAYING, name, null, null);
    }

    /**
     * Sets the activity of the bot to "Listening".
     *
     * @param name The name of the activity.
     * @param state The state of the activity.
     * @return The bots activity.
     */
    public static Activity listening(String name, String state) {
        return new ActivityImpl(ActivityType.LISTENING, name, null, state);
    }

    /**
     * Sets the activity of the bot to "Listening".
     *
     * @param name The name of the activity.
     * @return The bots activity.
     */
    public static Activity listening(String name) {
        return new ActivityImpl(ActivityType.LISTENING, name, null, null);
    }

    /**
     * Sets the activity of the bot to "Streaming".
     *
     * @param name The name of the activity.
     * @param url The url of the activity.
     * @param state The state of the activity.
     * @return The bots activity.
     */
    public static Activity streaming(String name, String url, String state) {
        return new ActivityImpl(ActivityType.LISTENING, name, url, state);
    }

    /**
     * Sets the activity of the bot to "Streaming".
     *
     * @param name The name of the activity.
     * @param url The url of the activity.
     * @return The bots activity.
     */
    public static Activity streaming(String name, String url) {
        return new ActivityImpl(ActivityType.LISTENING, name, url, null);
    }

    /**
     * Sets a custom activity.
     *
     * @param state The state of the activity.
     * @return The bots activity.
     */
    public static Activity custom(String state) {
        return new ActivityImpl(ActivityType.CUSTOM, state, null, null);
    }

    /**
     * Sets a chosen type of activity.
     *
     * @param type The type of activity.
     * @param name The name of the activity.
     * @param state The state of the activity.
     * @return The bots activity.
     */
    public static Activity of(ActivityType type, String name, String state) {
        return new ActivityImpl(type, name, null, state);
    }

    /**
     * Sets a chosen type of activity.
     *
     * @param type The type of activity.
     * @param name The name of the activity.
     * @return The bots activity.
     */
    public static Activity of(ActivityType type, String name) {
        return new ActivityImpl(type, name, null, null);
    }
}
