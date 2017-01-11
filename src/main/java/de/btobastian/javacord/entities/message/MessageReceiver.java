/*
 * Copyright (C) 2017 Bastian Oppermann
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
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Future;

/**
 * This interface represents a message receiver.
 * The most common message receivers are {@link de.btobastian.javacord.entities.Channel}
 * and {@link de.btobastian.javacord.entities.User}.
 */
public interface MessageReceiver {

    /**
     * Gets the of of the message receiver.
     *
     * @return The id of the message receiver.
     */
    public String getId();

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param nonce The nonce can be used for validating a message was sent.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, String nonce);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param tts Whether the message should be tts or not.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, boolean tts);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param tts Whether the message should be tts or not.
     * @param nonce The nonce can be used for validating a message was sent.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, boolean tts, String nonce);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param embed An embed that should be added to the message.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, EmbedBuilder embed);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param embed An embed that should be added to the message.
     * @param nonce The nonce can be used for validating a message was sent.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, EmbedBuilder embed, String nonce);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param embed An embed that should be added to the message.
     * @param tts Whether the message should be tts or not.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, EmbedBuilder embed, boolean tts);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param embed An embed that should be added to the message.
     * @param tts Whether the message should be tts or not.
     * @param nonce The nonce can be used for validating a message was sent.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, EmbedBuilder embed, boolean tts, String nonce);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, FutureCallback<Message> callback);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param nonce The nonce can be used for validating a message was sent.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, String nonce, FutureCallback<Message> callback);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param tts Whether the message should be tts or not.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, boolean tts, FutureCallback<Message> callback);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param tts Whether the message should be tts or not.
     * @param nonce The nonce can be used for validating a message was sent.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, boolean tts, String nonce, FutureCallback<Message> callback);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param embed An embed that should be added to the message.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, EmbedBuilder embed, FutureCallback<Message> callback);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param embed An embed that should be added to the message.
     * @param nonce The nonce can be used for validating a message was sent.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, EmbedBuilder embed, String nonce, FutureCallback<Message> callback);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param embed An embed that should be added to the message.
     * @param tts Whether the message should be tts or not.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, EmbedBuilder embed, boolean tts, FutureCallback<Message> callback);

    /**
     * Sends a message with the given content.
     *
     * @param content The content of the message.
     * @param embed An embed that should be added to the message.
     * @param tts Whether the message should be tts or not.
     * @param nonce The nonce can be used for validating a message was sent.
     * @param callback The callback which will be informed when the message was sent or sending failed.
     * @return The sent message. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendMessage(String content, EmbedBuilder embed, boolean tts, String nonce, FutureCallback<Message> callback);

    /**
     * Sendss a file.
     *
     * @param file The file to upload.
     * @return The sent message containing the file. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendFile(File file);

    /**
     * Sends a file.
     *
     * @param file The file to upload.
     * @param callback The callback which will be informed when the file was uploaded or upload failed.
     * @return The sent message containing the file. Canceled if something didn't work (e.g. missing permissions).
     */
    public Future<Message> sendFile(File file, FutureCallback<Message> callback);

    /**
     * Sends a file.
     *
     * @param inputStream An input stream.
     * @param filename The name of the file.
     * @return The sent message containing the file.
     */
    public Future<Message> sendFile(InputStream inputStream, String filename);

    /**
     * Sends a file.
     *
     * @param inputStream An input stream.
     * @param filename The name of the file.
     * @param callback The callback which will be informed when the file was uploaded or upload failed.
     * @return The sent message containing the file.
     */
    public Future<Message> sendFile(InputStream inputStream, String filename, FutureCallback<Message> callback);

    /**
     * Sends a file with a comment.
     *
     * @param file The file to upload.
     * @param comment An additional comment to your file.
     * @return The sent message containing the file.
     */
    public Future<Message> sendFile(File file, String comment);

    /**
     * Sends a file with a comment.
     *
     * @param file The file to upload.
     * @param comment An additional comment to your file.
     * @param callback The callback which will be informed when the file was uploaded or upload failed.
     * @return The sent message containing the file.
     */
    public Future<Message> sendFile(File file, String comment, FutureCallback<Message> callback);

    /**
     * Sends a file with a comment.
     *
     * @param inputStream An input stream.
     * @param filename The name of the file.
     * @param comment An additional comment to your file.
     * @return The sent message containing the file.
     */
    public Future<Message> sendFile(InputStream inputStream, String filename, String comment);

    /**
     * Sends a file with a comment.
     *
     * @param inputStream An input stream.
     * @param filename The name of the file.
     * @param comment An additional comment to your file.
     * @param callback The callback which will be informed when the file was uploaded or upload failed.
     * @return The sent message containing the file.
     */
    public Future<Message> sendFile(InputStream inputStream, String filename, String comment,
                                    FutureCallback<Message> callback);

    /**
     * Gets the message history of this channel.
     *
     * @param limit The maximum amount of messages.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistory(int limit);

    /**
     * Gets the message history of this channel.
     *
     * @param limit The maximum amount of messages.
     * @param callback The callback which will be informed when the message history is available.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistory(int limit, FutureCallback<MessageHistory> callback);

    /**
     * Gets the message history of this channel.
     *
     * @param before Only gets the messages before the given message.
     * @param limit The maximum amount of messages.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistoryBefore(Message before, int limit);

    /**
     * Gets the message history of this channel.
     *
     * @param before Only gets the messages before the given message.
     * @param limit The maximum amount of messages.
     * @param callback The callback which will be informed when the message history is available.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistoryBefore(
            Message before, int limit, FutureCallback<MessageHistory> callback);

    /**
     * Gets the message history of this channel.
     *
     * @param beforeId Only gets the messages before the message with the given id.
     * @param limit The maximum amount of messages.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistoryBefore(String beforeId, int limit);

    /**
     * Gets the message history of this channel.
     *
     * @param beforeId Only gets the messages before the message with the given id.
     * @param limit The maximum amount of messages.
     * @param callback The callback which will be informed when the message history is available.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistoryBefore(
            String beforeId, int limit, FutureCallback<MessageHistory> callback);

    /**
     * Gets the message history of this channel.
     *
     * @param after Only gets the messages after the given message.
     * @param limit The maximum amount of messages.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistoryAfter(Message after, int limit);

    /**
     * Gets the message history of this channel.
     *
     * @param after Only gets the messages after the given message.
     * @param limit The maximum amount of messages.
     * @param callback The callback which will be informed when the message history is available.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistoryAfter(
            Message after, int limit, FutureCallback<MessageHistory> callback);

    /**
     * Gets the message history of this channel.
     *
     * @param afterId Only gets the messages after the message with the given id.
     * @param limit The maximum amount of messages.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistoryAfter(String afterId, int limit);

    /**
     * Gets the message history of this channel.
     *
     * @param afterId Only gets the messages after the message with the given id.
     * @param limit The maximum amount of messages.
     * @param callback The callback which will be informed when the message history is available.
     * @return A message history.
     */
    public Future<MessageHistory> getMessageHistoryAfter(
            String afterId, int limit, FutureCallback<MessageHistory> callback);

    /**
     * Shows the "is typing.." status for 5 seconds.
     */
    public void type();

}
