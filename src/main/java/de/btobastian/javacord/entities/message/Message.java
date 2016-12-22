/*
 * Copyright (C) 2016 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.entities.message;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

/**
 * This interface represents a message.
 */
public interface Message extends Comparable<Message> {

    /**
     * Gets the id of the message.
     *
     * @return The id of the message.
     */
    public String getId();

    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    public String getContent();

    /**
     * Gets the {@link Channel channel} of the message.
     * <code>Null</code> if the message is a private message.
     *
     * @return The channel of the message.
     */
    public Channel getChannelReceiver();

    /**
     * Gets the user the message was sent to.
     * <code>Null</code> if the message is no private message.
     *
     * @return The user who received the message.
     */
    public User getUserReceiver();

    /**
     * Gets the {@link MessageReceiver receiver} of the message.
     * Could be a channel or a user. If you are the receiver the author of the message will be returned.
     *
     * @return The receiver of the message. If you are the receiver, the author of the message will be returned.
     */
    public MessageReceiver getReceiver();

    /**
     * Gets the author of the message.
     *
     * @return The author of the message.
     */
    public User getAuthor();

    /**
     * Checks if the message is a private message.
     *
     * @return Whether th message is private or not.
     */
    public boolean isPrivateMessage();

    /**
     * Gets all mentioned users.
     *
     * @return A list with all mentioned users.
     */
    public List<User> getMentions();

    /**
     * Checks if the message is tts.
     *
     * @return Whether the message is tts or not.
     */
    public boolean isTts();

    /**
     * Deletes the message.
     *
     * @return A future which tells us if the deletion was successful or not.
     */
    public Future<Void> delete();

    /**
     * Gets the attachments of a message.
     *
     * @return The attachments of the message.
     */
    public Collection<MessageAttachment> getAttachments();

    /**
     * Replies to the message with the given content.
     *
     * @param content The content of the message.
     * @return The sent message.
     */
    public Future<Message> reply(String content);

    /**
     * Replies to the message with the given content.
     *
     * @param content The content of the message.
     * @param tts Whether the message should be tts or not.
     * @return The sent message.
     */
    public Future<Message> reply(String content, boolean tts);

    /**
     * Replies to the message with the given content.
     *
     * @param content The content of the message.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message.
     */
    public Future<Message> reply(String content, FutureCallback<Message> callback);

    /**
     * Replies to the message with the given content.
     *
     * @param content The content of the message.
     * @param tts Whether the message should be tts or not.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message.
     */
    public Future<Message> reply(String content, boolean tts, FutureCallback<Message> callback);

    /**
     * Replies with a file.
     *
     * @param file The file to upload.
     * @return The sent message containing the file.
     */
    public Future<Message> replyFile(File file);

    /**
     * Replies with a file.
     *
     * @param file The file to upload.
     * @param callback The callback which will be informed when the file was uploaded or upload failed.
     * @return The sent message containing the file.
     */
    public Future<Message> replyFile(File file, FutureCallback<Message> callback);

    /**
     * Replies with a file.
     *
     * @param inputStream An input stream.
     * @param filename The name of the file.
     * @return The sent message containing the file.
     */
    public Future<Message> replyFile(InputStream inputStream, String filename);

    /**
     * Replies with a file.
     *
     * @param inputStream An input stream.
     * @param filename The name of the file.
     * @param callback The callback which will be informed when the file was uploaded or upload failed.
     * @return The sent message containing the file.
     */
    public Future<Message> replyFile(InputStream inputStream, String filename, FutureCallback<Message> callback);

    /**
     * Replies with a file and comment.
     *
     * @param file The file to upload.
     * @param comment An additional comment to your file.
     * @return The sent message containing the file.
     */
    public Future<Message> replyFile(File file, String comment);

    /**
     * Replies with a file and comment.
     *
     * @param file The file to upload.
     * @param comment An additional comment to your file.
     * @param callback The callback which will be informed when the file was uploaded or upload failed.
     * @return The sent message containing the file.
     */
    public Future<Message> replyFile(File file, String comment, FutureCallback<Message> callback);

    /**
     * Replies with a file and comment.
     *
     * @param inputStream An input stream.
     * @param filename The name of the file.
     * @param comment An additional comment to your file.
     * @return The sent message containing the file.
     */
    public Future<Message> replyFile(InputStream inputStream, String filename, String comment);

    /**
     * Replies with a file and comment.
     *
     * @param inputStream An input stream.
     * @param filename The name of the file.
     * @param comment An additional comment to your file.
     * @param callback The callback which will be informed when the file was uploaded or upload failed.
     * @return The sent message containing the file.
     */
    public Future<Message> replyFile(InputStream inputStream, String filename, String comment,
                                     FutureCallback<Message> callback);

    /**
     * Gets the date of creation.
     *
     * @return The date of creation.
     */
    public Calendar getCreationDate();

    /**
     * Edits the message.
     *
     * @param content The new content of the message.
     * @return A future which tells us if the edit was successful or not.
     */
    public Future<Void> edit(String content);

}
