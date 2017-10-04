package de.btobastian.javacord.events.server.role;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * A user role add event.
 */
public class UserRoleAddEvent extends UserRoleEvent {

    /**
     * Creates a new user role add event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param role The role of the event.
     * @param user The user of the event.
     */
    public UserRoleAddEvent(DiscordApi api, Server server, Role role, User user) {
        super(api, server, role, user);
    }

}
