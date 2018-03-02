package de.btobastian.javacord.entity.message.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.Mentionable;
import de.btobastian.javacord.entity.channel.TextChannel;
import de.btobastian.javacord.entity.message.Message;
import de.btobastian.javacord.entity.message.MessageBuilder;
import de.btobastian.javacord.entity.message.MessageDecoration;
import de.btobastian.javacord.entity.message.Messageable;
import de.btobastian.javacord.entity.message.embed.EmbedBuilder;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.util.FileContainer;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link MessageBuilder}.
 */
public class ImplMessageBuilder implements MessageBuilder {

    /**
     * The receiver of the message.
     */
    private Messageable messageable = null;

    /**
     * The string builder used to create the message.
     */
    private final StringBuilder strBuilder = new StringBuilder();

    /**
     * The embed of the message. Might be <code>null</code>.
     */
    private EmbedBuilder embed = null;

    /**
     * If the message should be text to speech or not.
     */
    private boolean tts = false;

    /**
     * The nonce of the message.
     */
    private String nonce = null;

    /**
     * A list with all attachments which should be added to the message.
     */
    private final List<FileContainer> attachments = new ArrayList<>();

    /**
     * Creates a new message builder.
     */
    public ImplMessageBuilder() { }

    @Override
    public MessageBuilder setReceiver(Messageable messageable) {
        this.messageable = messageable;
        return this;
    }

    @Override
    public ImplMessageBuilder append(String message, MessageDecoration... decorations) {
        for (MessageDecoration decoration : decorations) {
            strBuilder.append(decoration.getPrefix());
        }
        strBuilder.append(message);
        for (int i = decorations.length - 1; i >= 0; i--) {
            strBuilder.append(decorations[i].getSuffix());
        }
        return this;
    }

    @Override
    public ImplMessageBuilder appendCode(String language, String code) {
        strBuilder
                .append("\n")
                .append(MessageDecoration.CODE_LONG.getPrefix())
                .append(language)
                .append("\n")
                .append(code)
                .append(MessageDecoration.CODE_LONG.getSuffix());
        return this;
    }

    @Override
    public ImplMessageBuilder append(Mentionable entity) {
        strBuilder.append(entity.getMentionTag());
        return this;
    }

    @Override
    public ImplMessageBuilder append(Object object) {
        strBuilder.append(object);
        return this;
    }

    @Override
    public ImplMessageBuilder appendNewLine() {
        strBuilder.append("\n");
        return this;
    }

    @Override
    public MessageBuilder setContent(String content) {
        strBuilder.setLength(0);
        strBuilder.append(content);
        return this;
    }

    @Override
    public ImplMessageBuilder setEmbed(EmbedBuilder embed) {
        this.embed = embed;
        return this;
    }

    @Override
    public ImplMessageBuilder setTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    @Override
    public ImplMessageBuilder addFile(BufferedImage image, String fileName) {
        return addAttachment(image, fileName);
    }

    @Override
    public ImplMessageBuilder addFile(File file) {
        return addAttachment(file);
    }

    @Override
    public ImplMessageBuilder addFile(Icon icon) {
        return addAttachment(icon);
    }

    @Override
    public ImplMessageBuilder addFile(URL url) {
        return addAttachment(url);
    }

    @Override
    public ImplMessageBuilder addFile(byte[] bytes, String fileName) {
        return addAttachment(bytes, fileName);
    }

    @Override
    public ImplMessageBuilder addFile(InputStream stream, String fileName) {
        return addAttachment(stream, fileName);
    }

    @Override
    public ImplMessageBuilder addAttachment(BufferedImage image, String fileName) {
        if (image == null || fileName == null) {
            throw new IllegalArgumentException("image and fileName cannot be null!");
        }
        attachments.add(new FileContainer(image, fileName));
        return this;
    }

    @Override
    public ImplMessageBuilder addAttachment(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null!");
        }
        attachments.add(new FileContainer(file));
        return this;
    }

    @Override
    public ImplMessageBuilder addAttachment(Icon icon) {
        if (icon == null) {
            throw new IllegalArgumentException("icon cannot be null!");
        }
        attachments.add(new FileContainer(icon));
        return this;
    }

    @Override
    public ImplMessageBuilder addAttachment(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null!");
        }
        attachments.add(new FileContainer(url));
        return this;
    }

    @Override
    public ImplMessageBuilder addAttachment(byte[] bytes, String fileName) {
        if (bytes == null || fileName == null) {
            throw new IllegalArgumentException("bytes and fileName cannot be null!");
        }
        attachments.add(new FileContainer(bytes, fileName));
        return this;
    }

    @Override
    public ImplMessageBuilder addAttachment(InputStream stream, String fileName) {
        if (stream == null || fileName == null) {
            throw new IllegalArgumentException("stream and fileName cannot be null!");
        }
        attachments.add(new FileContainer(stream, fileName));
        return this;
    }

    @Override
    public ImplMessageBuilder setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    @Override
    public StringBuilder getStringBuilder() {
        return strBuilder;
    }

    @Override
    public CompletableFuture<Message> send() {
        if (messageable == null) {
            throw new IllegalStateException("Cannot send message without knowing the receiver");
        }
        if (messageable instanceof TextChannel) {
            return send((TextChannel) messageable);
        } else if (messageable instanceof User) {
            return ((User) messageable).openPrivateChannel()
                    .thenCompose(this::send);
        }
        throw new IllegalStateException("Messageable of unknown type");
    }

    @Override
    public String toString() {
        return strBuilder.toString();
    }

    /**
     * Sends the message to the given text channel.
     *
     * @param channel The channel to which the message should be sent.
     * @return The sent message.
     */
    private CompletableFuture<Message> send(TextChannel channel) {
        ObjectNode body = JsonNodeFactory.instance.objectNode()
                .put("content", toString() == null ? "" : toString() )
                .put("tts", tts);
        body.putArray("mentions");
        if (embed != null) {
            embed.toJsonNode(body.putObject("embed"));
        }
        if (nonce != null) {
            body.put("nonce", nonce);
        }

        RestRequest<Message> request = new RestRequest<Message>(channel.getApi(), RestMethod.POST, RestEndpoint.MESSAGE)
                .setUrlParameters(channel.getIdAsString());
        if (!attachments.isEmpty() || (embed != null && embed.requiresAttachments())) {
            CompletableFuture<Message> future = new CompletableFuture<>();
            // We access files etc. so this should be async
            channel.getApi().getThreadPool().getExecutorService().submit(() -> {
                try {
                    MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("payload_json", body.toString());
                    List<FileContainer> tempAttachments = new ArrayList<>(attachments);
                    // Add the attachments required for the embed
                    if (embed != null) {
                        tempAttachments.addAll(embed.getRequiredAttachments());
                    }
                    for (int i = 0; i < tempAttachments.size(); i++) {
                        byte[] bytes = tempAttachments.get(i).asByteArray(channel.getApi()).join();

                        String mediaType = URLConnection
                                .guessContentTypeFromName(tempAttachments.get(i).getFileTypeOrName());
                        if (mediaType == null) {
                            mediaType = "application/octet-stream";
                        }
                        multipartBodyBuilder.addFormDataPart("file" + i, tempAttachments.get(i).getFileTypeOrName(),
                                RequestBody.create(MediaType.parse(mediaType), bytes));
                    }

                    request.setMultipartBody(multipartBodyBuilder.build());
                    request.execute(result -> ((ImplDiscordApi) channel.getApi())
                            .getOrCreateMessage(channel, result.getJsonBody()))
                            .whenComplete((message, throwable) -> {
                                if (throwable != null) {
                                    future.completeExceptionally(throwable);
                                } else {
                                    future.complete(message);
                                }
                            });
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                }
            });
            return future;
        } else {
            request.setBody(body);
            return request.execute(result -> ((ImplDiscordApi) channel.getApi())
                    .getOrCreateMessage(channel, result.getJsonBody()));
        }
    }

}
