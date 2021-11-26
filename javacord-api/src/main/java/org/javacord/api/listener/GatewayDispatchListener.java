package org.javacord.api.listener;

import org.javacord.api.event.GatewayDispatchEvent;

@FunctionalInterface
public interface GatewayDispatchListener extends GloballyAttachableListener {
    /**
     * Called if a new gateway event is received.
     *
     * @param event The gateway event.
     */
    void onGatewayDispatch(GatewayDispatchEvent event);
}
