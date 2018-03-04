package de.btobastian.javacord.entities.message;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Mentionable;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class can help you to generate messages.
 * It can be helpful, if you work a lot with decorations, etc.
 */
public class MessageBuilder {

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
    private final List<Attachment> attachments = new ArrayList<>();

    /**
     * Creates a new message builder.
     */
    public MessageBuilder() { }

    /**
     * Creates a message builder from a message.
     *
     * @param message The message to copy.
     * @return A message builder which would produce the same text as the given message.
     */
    public static MessageBuilder fromMessage(Message message) {
        MessageBuilder builder = new MessageBuilder();
        builder.getStringBuilder().append(message.getContent());
        if (!message.getEmbeds().isEmpty()) {
            builder.setEmbed(message.getEmbeds().get(0).toBuilder());
        }
        for (MessageAttachment attachment : message.getAttachments()) {
            builder.addAttachment(attachment.getUrl());
        }
        return builder;
    }

    /**
     * Appends a sting with or without decoration to the message.
     *
     * @param message The string to append.
     * @param decorations The decorations of the string.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder append(String message, MessageDecoration... decorations) {
        for (MessageDecoration decoration : decorations) {
            strBuilder.append(decoration.getPrefix());
        }
        strBuilder.append(message);
        for (int i = decorations.length - 1; i >= 0; i--) {
            strBuilder.append(decorations[i].getSuffix());
        }
        return this;
    }

    /**
     * Appends code to the message.
     *
     * @param language The language, e.g. "java".
     * @param code The code.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder appendCode(String language, String code) {
        strBuilder
                .append("\n")
                .append(MessageDecoration.CODE_LONG.getPrefix())
                .append(language)
                .append("\n")
                .append(code)
                .append(MessageDecoration.CODE_LONG.getSuffix());
        return this;
    }

    /**
     * Appends a mentionable entity (usually a user or channel) to the message.
     *
     * @param entity The entity to mention.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder append(Mentionable entity) {
        strBuilder.append(entity.getMentionTag());
        return this;
    }

    /**
     * Appends the string representation of the object (calling {@link String#valueOf(Object)} method) to the message.
     *
     * @param object The object to append.
     * @return The current instance in order to chain call methods.
     * @see StringBuilder#append(Object)
     */
    public MessageBuilder append(Object object) {
        strBuilder.append(object);
        return this;
    }

    /**
     * Appends a new line to the message.
     *
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder appendNewLine() {
        strBuilder.append("\n");
        return this;
    }

    /**
     * Sets the embed of the message.
     *
     * @param embed The embed to set.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder setEmbed(EmbedBuilder embed) {
        this.embed = embed;
        return this;
    }


    /**
     * Sets if the message should be text to speech.
     *
     * @param tts Whether the message should be text to speech or not.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder setTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    /**
     * Adds a file to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(InputStream, String)
     */
    public MessageBuilder addFile(InputStream stream, String fileName) {
        return addAttachment(stream, fileName);
    }

    /**
     * Adds a file to the message.
     *
     * @param file The file.
     * @return The current instance in order to chain call methods.
     * @see #addAttachment(File)
     */
    public MessageBuilder addFile(File file) {
        return addAttachment(file);
    }

    /**
     * Adds an attachment to the message.
     *
     * @param url The url of the attachment.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(URL url) {
        attachments.add(new Attachment(url));
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param stream The stream of the file.
     * @param fileName The name of the file.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(InputStream stream, String fileName) {
        if (stream == null || fileName == null) {
            throw new IllegalArgumentException("stream and fileName cannot be null!");
        }
        attachments.add(new Attachment(fileName, stream));
        return this;
    }

    /**
     * Adds an attachment to the message.
     *
     * @param file The file.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder addAttachment(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null!");
        }
        try {
            return addAttachment(new FileInputStream(file), file.getName());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The provided file couldn't be found!");
        }
    }

    /**
     * Sets the nonce of the message.
     *
     * @param nonce The nonce to set.
     * @return The current instance in order to chain call methods.
     */
    public MessageBuilder setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    public StringBuilder getStringBuilder() {
        return strBuilder;
    }

    /**
     * Sends the message.
     *
     * @param messageable The messageable (text channel or user) to which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(Messageable messageable) {
        if (messageable instanceof TextChannel) {
            return send((TextChannel) messageable);
        }
        if (messageable instanceof User) {
            return send((User) messageable);
        }
        throw new IllegalArgumentException("The provided messageable object is neither a text channel or user!");
    }

    /**
     * Sends the message.
     *
     * @param user The user to which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(User user) {
        return user.openPrivateChannel()
                .thenComposeAsync(this::send, user.getApi().getThreadPool().getExecutorService());
    }

    /**
     * Sends the message.
     *
     * @param channel The channel to which the message should be sent.
     * @return The sent message.
     */
    public CompletableFuture<Message> send(TextChannel channel) {
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
                    List<Attachment> tempAttachments = new ArrayList<>();
                    tempAttachments.addAll(attachments);
                    // Add the attachments required for the embed
                    if (embed != null) {
                        embed.consumeRequiredAttachments((fileName, image) -> tempAttachments.add(
                                new Attachment(fileName, image.asInputStream(channel.getApi()).join())));
                    }
                    for (int i = 0; i < tempAttachments.size(); i++) {
                        byte[] bytes;
                        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                            int nRead;
                            byte[] data = new byte[16384];

                            while ((nRead = tempAttachments.get(i).getStream().read(data, 0, data.length)) != -1) {
                                buffer.write(data, 0, nRead);
                            }
                            buffer.flush();
                            bytes = buffer.toByteArray();
                            tempAttachments.get(i).getStream().close();
                        }

                        String mediaType = URLConnection.guessContentTypeFromName(tempAttachments.get(i).getFileName());
                        if (mediaType == null) {
                            mediaType = "application/octet-stream";
                        }
                        multipartBodyBuilder.addFormDataPart("file" + i, tempAttachments.get(i).getFileName(),
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

    @Override
    public String toString() {
        return strBuilder.toString();
    }

    /**
     * A simple class only used for file upload.
     */
    private final class Attachment {

        private String fileName;
        private InputStream stream;
        private final URL url;

        /**
         * Creates a new attachment.
         *
         * @param fileName The name of the attached file.
         * @param stream The stream which provides the file.
         */
        protected Attachment(String fileName, InputStream stream) {
            this.fileName = fileName;
            this.stream = stream;
            this.url = null;
        }

        /**
         * Creates a new attachment.
         *
         * @param url The url of the attached file.
         */
        protected Attachment(URL url) {
            this.fileName = null;
            this.stream = null;
            this.url = url;
        }

        /**
         * Gets the name of the attached file.
         *
         * @return The name of the attached file.
         */
        protected String getFileName() {
            if (fileName == null) {
                fileName = url.getFile();
                if (fileName.equals("")) {
                    fileName = "index.html";
                }
            }
            return fileName;
        }

        /**
         * Gets the stream which provides the file.
         *
         * @return The stream which provides the file.
         */
        protected InputStream getStream() throws IOException {
            if (stream == null) {
                stream = url.openStream();
            }
            return stream;
        }

    }

}