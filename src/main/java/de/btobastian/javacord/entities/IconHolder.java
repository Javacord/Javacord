package de.btobastian.javacord.entities;

import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.utils.JavacordCompletableFuture;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * All entities which have an icon/avatar.
 */
public interface IconHolder extends DiscordEntity {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(IconHolder.class);

    /**
     * Gets the url of the icon.
     *
     * @return The url of the icon.
     */
    Optional<URL> getIconUrl();

    /**
     * Gets the icon of the entity as byte array.
     *
     * @return The icon of the entity as byte array.
     */
    default CompletableFuture<Optional<byte[]>> getIconAsByteArray() {
        CompletableFuture<Optional<byte[]>> future = new JavacordCompletableFuture<>();
        getApi().getThreadPool().getExecutorService().submit(() -> {
           try {
               logger.debug("Trying to get icon for entity {}", this);
               Optional<URL> url = getIconUrl();
               if (!url.isPresent()) {
                   logger.debug("There's no icon url for entity {}!", this);
                   future.complete(Optional.empty());
                   return;
               }
               HttpsURLConnection conn = (HttpsURLConnection) url.get().openConnection();
               conn.setRequestMethod("GET");
               conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
               conn.setRequestProperty("User-Agent", Javacord.USER_AGENT);

               try (
                   InputStream in = new BufferedInputStream(conn.getInputStream());
                   ByteArrayOutputStream out = new ByteArrayOutputStream()
               ) {
                   byte[] buf = new byte[1024];
                   int n;
                   while (-1 != (n = in.read(buf))) {
                       out.write(buf, 0, n);
                   }
                   byte[] icon = out.toByteArray();
                   logger.debug("Got icon for entity {} (size: {})", this, icon.length);
                   future.complete(Optional.of(icon));
               }
           } catch (Throwable t) {
               future.completeExceptionally(t);
           }
        });
        return future;
    }

    /**
     * Gets the icon of the entity as {@link BufferedImage}.
     *
     * @return The icon of the entity.
     */
    default CompletableFuture<Optional<BufferedImage>> getIcon() {
        CompletableFuture<Optional<BufferedImage>> future = new JavacordCompletableFuture<>();
        getIconAsByteArray().whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
                return;
            }
            if (bytes.isPresent()) {
                try {
                    future.complete(Optional.ofNullable(ImageIO.read(new ByteArrayInputStream(bytes.get()))));
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                }
                return;
            }
            future.complete(Optional.empty());
        });
        return future;
    }

}
