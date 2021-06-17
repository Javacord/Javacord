package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeNsfwLevelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

public interface ServerChangeNsfwLevelListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server changes its NSFW level.
     *
     * @param event The event.
     */
    void onServerChangeNsfwLevel(ServerChangeNsfwLevelEvent event);

}
