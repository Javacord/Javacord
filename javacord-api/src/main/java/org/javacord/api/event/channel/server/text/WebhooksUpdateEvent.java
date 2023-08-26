package org.javacord.api.event.channel.server.text;

import org.javacord.api.event.channel.server.textable.TextableRegularServerChannelEvent;

/**
 * A webhooks update event.
 * It gets fired when a server channel's webhook is created, updated, or deleted.
 */
public interface WebhooksUpdateEvent extends TextableRegularServerChannelEvent {
}
