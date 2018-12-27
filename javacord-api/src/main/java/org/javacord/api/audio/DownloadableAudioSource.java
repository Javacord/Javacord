package org.javacord.api.audio;

import java.util.concurrent.CompletableFuture;

/**
 * A downloadable audio source.
 */
public interface DownloadableAudioSource extends AudioSource {

    /**
     * Downloads the audio source.
     *
     * <p>Allows you to fully download the audio source before queuing it.
     *
     * @return A future with this audio source. Finishes when the download is completed.
     */
    CompletableFuture<? extends DownloadableAudioSource> download();

    /**
     * Checks if the audio source is fully downloaded.
     *
     * @return Whether the audio source is fully downloaded or not.
     */
    boolean isFullyDownloaded();

}
