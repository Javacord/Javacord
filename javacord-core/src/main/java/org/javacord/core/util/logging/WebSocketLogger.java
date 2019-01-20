package org.javacord.core.util.logging;

import com.neovisionaries.ws.client.ThreadType;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class WebSocketLogger implements WebSocketListener {
    private static final Logger logger = LoggerUtil.getLogger(WebSocketLogger.class);

    @Override
    public void onStateChanged(WebSocket websocket, WebSocketState newState) {
        logger.trace("onStateChanged: newState='{}'", newState);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException cause) {
        logger.trace("onConnectError", cause);
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                               boolean closedByServer) {
        logger.trace("onDisconnected: closedByServer='{}' serverCloseFrame='{}' clientCloseFrame='{}'",
                closedByServer, serverCloseFrame, clientCloseFrame);
    }

    @Override
    public void onFrame(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onFrame: frame='{}'", frame);
    }

    @Override
    public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onContinuationFrame: frame='{}'", frame);
    }

    @Override
    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onTextFrame: frame='{}'", frame);
    }

    @Override
    public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onBinaryFrame: frame='{}'", frame);
    }

    @Override
    public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onCloseFrame: frame='{}'", frame);
    }

    @Override
    public void onPingFrame(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onPingFrame: frame='{}'", frame);
    }

    @Override
    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onPongFrame: frame='{}'", frame);
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) {
        logger.trace("onTextMessage: text='{}'", text);
    }

    @Override
    public void onTextMessage(WebSocket websocket, byte[] data) {
        logger.trace("onTextFrame: data='{}'", data);
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) {
        logger.trace("onBinaryMessage: binary='{}'", binary);
    }

    @Override
    public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onSendingFrame: frame='{}'", frame);
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onFrameSent: frame='{}'", frame);
    }

    @Override
    public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) {
        logger.trace("onFrameUnsent: frame='{}'", frame);
    }

    @Override
    public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) {
        logger.trace("onThreadCreated: threadType='{}' thread='{}'", threadType, thread);
    }

    @Override
    public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) {
        logger.trace("onThreadStarted: threadType='{}' thread='{}'", threadType, thread);
    }

    @Override
    public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) {
        logger.trace("onThreadStopping: threadType='{}' thread='{}'", threadType, thread);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
        logger.trace("onConnected: headers='{}'", headers);
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) {
        logger.trace("onError", cause);
    }

    @Override
    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
        logger.trace("onFrameError: frame='{}'", frame, cause);
    }

    @Override
    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) {
        logger.trace("onMessageError: frames='{}'", frames, cause);
    }

    @Override
    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) {
        logger.trace("onMessageDecompressionError: compressed='{}'", compressed, cause);
    }

    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) {
        logger.trace("onTextMessageError: data='{}'", data, cause);
    }

    @Override
    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
        logger.trace("onSendError: frame='{}'", frame, cause);
    }

    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
        logger.trace("onUnexpectedError", cause);
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) {
        logger.trace("handleCallbackError", cause);
    }

    @Override
    public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) {
        logger.trace("onSendingHandshake: requestLine='{}' headers='{}'", requestLine, headers);
    }
}
