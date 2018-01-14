package de.btobastian.javacord.entities.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.entities.ActivityAssets;
import de.btobastian.javacord.entities.Icon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionException;

/**
 * The implementation of {@link ActivityAssets}.
 */
public class ImplActivityAssets implements ActivityAssets {

    private final ImplActivity activity;

    private final String largeImage;
    private final String largeText;
    private final String smallImage;
    private final String smallText;

    /**
     * Creates a new activity assets object.
     *
     * @param data The json data of the assets party.
     */
    public ImplActivityAssets(ImplActivity activity, JsonNode data) {
        this.activity = activity;

        this.largeImage = data.has("large_image") ? data.get("large_image").asText(null) : null;
        this.largeText = data.has("large_text") ? data.get("large_text").asText(null) : null;
        this.smallImage = data.has("small_image") ? data.get("small_image").asText(null) : null;
        this.smallText = data.has("small_text") ? data.get("small_text").asText(null) : null;
    }

    @Override
    public Optional<Icon> getLargeImage() {
        return Optional.ofNullable(largeImage)
                .flatMap(imageId -> activity.getApplicationId()
                        .map(applicationId -> String
                                .format("https://cdn.discordapp.com/app-assets/%s/%s.png", applicationId, imageId)))
                .map(url -> {
                    try {
                        return new URL(url);
                    } catch (MalformedURLException e) {
                        throw new CompletionException(e);
                    }
                })
                .map(url -> new ImplIcon(null, url));
    }

    @Override
    public Optional<String> getLargeText() {
        return Optional.ofNullable(largeText);
    }

    @Override
    public Optional<Icon> getSmallImage() {
        return Optional.ofNullable(smallImage)
                .flatMap(imageId -> activity.getApplicationId()
                        .map(applicationId -> String
                                .format("https://cdn.discordapp.com/app-assets/%s/%s.png", applicationId, imageId)))
                .map(url -> {
                    try {
                        return new URL(url);
                    } catch (MalformedURLException e) {
                        throw new CompletionException(e);
                    }
                })
                .map(url -> new ImplIcon(null, url));
    }

    @Override
    public Optional<String> getSmallText() {
        return Optional.ofNullable(smallText);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImplActivityAssets)) {
            return false;
        }
        ImplActivityAssets otherAssets = (ImplActivityAssets) obj;
        return Objects.deepEquals(largeImage, otherAssets.largeImage)
                && Objects.deepEquals(largeText, otherAssets.largeText)
                && Objects.deepEquals(smallImage, otherAssets.smallImage)
                && Objects.deepEquals(smallText, otherAssets.smallText);
    }

    @Override
    public int hashCode() {
        int hash = 42;
        int largeImageHash = largeImage == null ? 0 : largeImage.hashCode();
        int largeTextHash = largeText == null ? 0 : largeText.hashCode();
        int smallImageHash = smallImage == null ? 0 : smallImage.hashCode();
        int smallTextHash = smallText == null ? 0 : smallText.hashCode();

        hash = hash * 11 + largeImageHash;
        hash = hash * 13 + largeTextHash;
        hash = hash * 17 + smallImageHash;
        hash = hash * 19 + smallTextHash;
        return hash;
    }
}
