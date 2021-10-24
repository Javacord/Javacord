package org.javacord.core.entity.sticker;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.logging.log4j.Logger;
import org.javacord.api.Javacord;
import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.sticker.internal.StickerBuilderDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class StickerBuilderDelegateImpl implements StickerBuilderDelegate {

    private final Logger logger = LoggerUtil.getLogger(StickerBuilderDelegateImpl.class);
    private final DiscordApiImpl api;
    private final ServerImpl server;

    private String name;
    private String description;
    private String tags;
    private File file;

    /**
     * Creates a new implementation of the sticker builder delegate.
     *
     * @param server The server that owns the sticker.
     */
    public StickerBuilderDelegateImpl(ServerImpl server) {
        this.api = (DiscordApiImpl) server.getApi();
        this.server = server;
    }

    @Override
    public void copy(Sticker sticker) {
        String fileUrlString = "https://" + Javacord.DISCORD_CDN_DOMAIN
                + "stickers/" + sticker.getId()
                + sticker.getFormatType().toString().toLowerCase();

        this.name = sticker.getName();
        this.description = sticker.getDescription();
        this.tags = sticker.getTags();

        try {
            Optional.of(new File(new URL(fileUrlString).toURI())).ifPresent(this::setFile);
        } catch (MalformedURLException | URISyntaxException exception) {
            logger.warn("Seems like the url of the sticker is malformed! Please contact the developer!", exception);
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public CompletableFuture<Sticker> create() {
        return create(null);
    }

    @Override
    public CompletableFuture<Sticker> create(String reason) {
        if (name == null) {
            throw new IllegalStateException("The name is no optional parameter.");
        }
        if (tags == null) {
            throw new IllegalStateException("The tags are no optional parameter.");
        }
        if (file == null) {
            throw new IllegalStateException("The file content is no optional parameter.");
        }
        if (file.length() > 512000) {
            throw new IllegalStateException("The file is too large (must be smaller than 500 KB).");
        }

        FileContainer container = new FileContainer(file);

        if (!container.getFileTypeOrName().endsWith("png") && !container.getFileTypeOrName().endsWith("apng")
                && !container.getFileTypeOrName().endsWith("json")) {
            throw new IllegalStateException("The file must be an image.");
        }

        String mediaType = URLConnection
                .guessContentTypeFromName(container.getFileTypeOrName());
        if (mediaType == null) {
            mediaType = "application/octet-stream";
        }

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("description", description)
                .addFormDataPart("tags", tags)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse(mediaType), container.asByteArray(api).join()))
                .build();

        return new RestRequest<Sticker>(api, RestMethod.POST, RestEndpoint.SERVER_STICKER)
                .setUrlParameters(server.getIdAsString())
                .setMultipartBody(multipartBody)
                .setAuditLogReason(reason)
                .execute(result ->  new StickerImpl(api, result.getJsonBody()));
    }


}
