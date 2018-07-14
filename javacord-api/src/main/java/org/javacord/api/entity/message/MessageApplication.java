package org.javacord.api.entity.message;

/**
 * This class represents a message application.
 */
public interface MessageApplication {
    /**
     * Returns the name of the application.
     *
     * @return The name of the application.
     */
    String getName();

    /**
     * Returns the description of the application.
     *
     * @return The description of the application.
     */
    String getDescription();

    /**
     * Returns the ID of the applications image.
     *
     * @return The ID of the applications image.
     */
    String getIcon();

    /**
     * Returns the ID of the applications embed image asset.
     *
     * @return the ID of the applications embed image asset.
     */
    String getCoverImage();

    /**
     * Returns the {@link Message} that the application belongs to.
     *
     * @return the message that the application belongs to.
     */
    Message getMessage();

    /**
     * Returns the ID of the application.
     *
     * @return the ID of the application.
     */
    long getApplicationId();
}
