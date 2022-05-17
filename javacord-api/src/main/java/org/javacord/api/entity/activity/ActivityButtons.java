package org.javacord.api.entity.activity;

public interface ActivityButtons {
    /**
     * Gets the text shown on the button.
     *
     * @return The text shown on the button.
     */
    String getLabel();

    /**
     * The url which is opened when the button is clicked.
     *
     * @return The url which is opened when the button is clicked.
     */
    String getUrl();
}
