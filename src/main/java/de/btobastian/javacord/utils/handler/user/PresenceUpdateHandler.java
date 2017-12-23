package de.btobastian.javacord.utils.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.GameType;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.impl.ImplGame;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.events.user.UserChangeGameEvent;
import de.btobastian.javacord.events.user.UserChangeNameEvent;
import de.btobastian.javacord.events.user.UserChangeStatusEvent;
import de.btobastian.javacord.listeners.user.UserChangeGameListener;
import de.btobastian.javacord.listeners.user.UserChangeNameListener;
import de.btobastian.javacord.listeners.user.UserChangeStatusListener;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles the presence update packet.
 */
public class PresenceUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public PresenceUpdateHandler(DiscordApi api) {
        super(api, true, "PRESENCE_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        long userId = packet.get("user").get("id").asLong();
        api.getUserById(userId).map(user -> ((ImplUser) user)).ifPresent(user -> {
            if (packet.has("game")) {
                Game newGame = null;
                if (!packet.get("game").isNull()) {
                    int gameType = packet.get("game").get("type").asInt();
                    String name = packet.get("game").get("name").asText();
                    String streamingUrl =
                            packet.get("game").has("url") && !packet.get("game").get("url").isNull() ?
                            packet.get("game").get("url").asText() : null;
                    newGame = new ImplGame(GameType.getGameTypeById(gameType), name, streamingUrl);
                }
                Game oldGame = user.getGame().orElse(null);
                user.setGame(newGame);
                if (!Objects.deepEquals(newGame, oldGame)) {
                    dispatchUserGameChangeEvent(user, newGame, oldGame);
                }
            }
            if (packet.has("status")) {
                UserStatus newStatus = UserStatus.fromString(
                        packet.has("status") ? packet.get("status").asText(null) : null);
                UserStatus oldStatus = user.getStatus();
                user.setStatus(newStatus);
                if (newStatus != oldStatus) {
                    dispatchUserStatusChangeEvent(user, newStatus, oldStatus);
                }
            }
            if (packet.get("user").has("username")) {
                String newName = packet.get("user").get("username").asText();
                String oldName = user.getName();
                if (!oldName.equals(newName)) {
                    user.setName(newName);
                    dispatchUserChangeNameEvent(user, newName, oldName);
                }
            }
        });
    }

    private void dispatchUserGameChangeEvent(User user, Game newGame, Game oldGame) {
        UserChangeGameEvent event = new UserChangeGameEvent(api, user, newGame, oldGame);

        List<UserChangeGameListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeGameListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeGameListeners()));
        listeners.addAll(api.getUserChangeGameListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeGame(event));
    }

    private void dispatchUserStatusChangeEvent(User user, UserStatus newStatus, UserStatus oldStatus) {
        UserChangeStatusEvent event = new UserChangeStatusEvent(api, user, newStatus, oldStatus);

        List<UserChangeStatusListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeStatusListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeStatusListeners()));
        listeners.addAll(api.getUserChangeStatusListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeStatus(event));
    }

    private void dispatchUserChangeNameEvent(User user, String newName, String oldName) {
        UserChangeNameEvent event = new UserChangeNameEvent(api, user, newName, oldName);

        List<UserChangeNameListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeNameListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeNameListeners()));
        listeners.addAll(api.getUserChangeNameListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeName(event));
    }

}