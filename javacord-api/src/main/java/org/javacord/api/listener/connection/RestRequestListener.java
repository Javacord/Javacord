package org.javacord.api.listener.connection;

import org.javacord.api.event.connection.RestRequestEvent;
import org.javacord.api.listener.GloballyAttachableListener;

/**
 * This listener listens to rest request.
 */
@FunctionalInterface
public interface RestRequestListener extends GloballyAttachableListener {

    /**
     * This method is called every time a rest request is made.
     *
     * @param event The event.
     */
    void onRestRequest(RestRequestEvent event);

}
