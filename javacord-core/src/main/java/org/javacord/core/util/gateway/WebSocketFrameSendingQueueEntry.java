package org.javacord.core.util.gateway;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.Comparator;
import java.util.Optional;

/**
 * This class represents a prioritized entry in the web socket frame sending queue.
 */
public class WebSocketFrameSendingQueueEntry implements Comparable<WebSocketFrameSendingQueueEntry> {

    /**
     * The comparator for determining send order.
     */
    private static final Comparator<WebSocketFrameSendingQueueEntry> ENTRY_COMPARATOR = Comparator
            .<WebSocketFrameSendingQueueEntry, Boolean>comparing(entry -> !entry.lifecycle)
            .thenComparing(entry -> !entry.priority)
            .thenComparing(entry -> entry.timestamp);

    /**
     * The web socket on which to send the frame.
     */
    private final WebSocket webSocket;

    /**
     * The web socket frame to be sent.
     */
    private final WebSocketFrame webSocketFrame;

    /**
     * Whether this entry is a priority entry.
     */
    private final boolean priority;

    /**
     * Whether this entry is a lifecycle entry.
     */
    private final boolean lifecycle;

    /**
     * The timestamp when this entry was created.
     */
    private final long timestamp = System.nanoTime();

    /**
     * Creates a new web socket frame sending queue entry.
     *
     * @param webSocket The web socket on which to send the frame.
     * @param webSocketFrame The web socket frame to be sent.
     * @param priority Whether this entry is a priority entry.
     * @param lifecycle Whether this entry is a lifecycle entry.
     */
    public WebSocketFrameSendingQueueEntry(
            WebSocket webSocket, WebSocketFrame webSocketFrame, boolean priority, boolean lifecycle) {
        if (lifecycle && (webSocket == null)) {
            throw new IllegalArgumentException("lifecycle frame sending requests must specify the web socket");
        }
        this.webSocket = webSocket;
        this.webSocketFrame = webSocketFrame;
        this.priority = priority;
        this.lifecycle = lifecycle;
    }

    /**
     * Gets the web socket to send to.
     *
     * @return The web socket to send to.
     */
    public Optional<WebSocket> getWebSocket() {
        return Optional.ofNullable(webSocket);
    }

    /**
     * Gets the web socket frame to send.
     *
     * @return The web socket frame to send.
     */
    public WebSocketFrame getFrame() {
        return webSocketFrame;
    }

    /**
     * Gets whether this entry is a priority lifecyle one.
     *
     * @return Whether this entry is a priority lifecyle one.
     */
    public boolean isPriorityLifecyle() {
        return priority && lifecycle;
    }

    @Override
    public int compareTo(WebSocketFrameSendingQueueEntry other) {
        return ENTRY_COMPARATOR.compare(this, other);
    }

}
