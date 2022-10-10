package org.javacord.api.event.server;

/**
 * A server becomes unavailable event.
 * Unavailability means, that a Discord server is down due to a temporary outage.
 *
 * @see <a href="https://discord.com/developers/docs/topics/gateway#guild-availability">Discord docs</a>
 */
public interface ServerBecomesUnavailableEvent extends ServerEvent {
}
