package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerLeaveEvent;

/**
 * This listener listens to server leaves.
 * TODO: find a better way to formulate this
 */
@FunctionalInterface
public interface ServerLeaveListener {

        /**
         * This method is called every time you leave a server.
         * Do not confuse it with the ServerMemberRemoveListener:
         * ServerMemberRemoveListener is for other users and ServerLeaveListener is for you!
         *
         * @param event the event.
         */
        void onServerLeave(ServerLeaveEvent event);
        }
