package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.ApplicationCommandPermissionType;
import org.javacord.api.interaction.ApplicationCommandPermissions;

public class ApplicationCommandPermissionsImpl implements ApplicationCommandPermissions {

    private final long id;
    private final ApplicationCommandPermissionType type;
    private final boolean permission;

    /**
     * Class constructor.
     *
     * @param data The json data of the application command permissions.
     */
    public ApplicationCommandPermissionsImpl(JsonNode data) {
        id = data.get("id").asLong();
        type = ApplicationCommandPermissionType.fromValue(data.get("type").asInt());
        permission = data.get("permission").asBoolean();
    }

    /**
     * Class constructor.
     *
     * @param id The id.
     * @param type The type.
     * @param permission The permission.
     *
     */
    public ApplicationCommandPermissionsImpl(long id, ApplicationCommandPermissionType type, boolean permission) {
        this.id = id;
        this.type = type;
        this.permission = permission;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public ApplicationCommandPermissionType getType() {
        return type;
    }

    @Override
    public boolean getPermission() {
        return permission;
    }

    /**
     * Adds the json data to the given object node.
     *
     * @return The provided object with the data of the embed.
     */
    public ObjectNode toJsonNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("id", id);
        node.put("type", type.getValue());
        node.put("permission", permission);

        return node;
    }
}
