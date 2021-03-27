package org.javacord.core.entity.message.mention;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.mention.AllowedMentionType;
import org.javacord.api.entity.message.mention.AllowedMentions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class AllowedMentionsImpl implements AllowedMentions {


    private final List<Long> allowedRoleMentions;
    private final List<Long> allowedUserMentions;

    private final EnumSet<AllowedMentionType> allowedMentionTypes = EnumSet.noneOf(AllowedMentionType.class);

    /**
     * Creates a new mention.
     *
     * @param mentionAllRoles        Whether it mentions all roles.
     * @param mentionAllUsers        Whether it mentions all users.
     * @param mentionEveryoneAndHere Whether it mentions @everyone and @here.
     * @param allowedRoleMentions    Mentions added role ids.
     * @param allowedUserMentions    Mentions added user ids.
     */
    public AllowedMentionsImpl(boolean mentionAllRoles, boolean mentionAllUsers, boolean mentionEveryoneAndHere,
                               ArrayList<Long> allowedRoleMentions, ArrayList<Long> allowedUserMentions) {
        this.allowedRoleMentions = allowedRoleMentions;
        this.allowedUserMentions = allowedUserMentions;
        if (mentionAllRoles) {
            allowedMentionTypes.add(AllowedMentionType.ROLES);
        }
        if (mentionAllUsers) {
            allowedMentionTypes.add(AllowedMentionType.USERS);
        }
        if (mentionEveryoneAndHere) {
            allowedMentionTypes.add(AllowedMentionType.EVERYONE);
        }
    }

    @Override
    public List<Long> getAllowedRoleMentions() {
        return allowedRoleMentions;
    }

    @Override
    public List<Long> getAllowedUserMentions() {
        return allowedUserMentions;
    }

    @Override
    public EnumSet<AllowedMentionType> getMentionTypes() {
        return allowedMentionTypes;
    }

    /**
     * Gets the embed as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @return The embed as a ObjectNode.
     */
    public ObjectNode toJsonNode() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();
        return toJsonNode(object);
    }

    /**
     * Adds the json data to the given object node.
     *
     * @param object The object, the data should be added to.
     * @return The provided object with the data of the embed.
     */
    public ObjectNode toJsonNode(ObjectNode object) {
        ArrayNode parse = object.putArray("parse");

        if (allowedMentionTypes.contains(AllowedMentionType.ROLES)) {
            parse.add("roles");
        } else {
            ArrayNode roles = object.putArray("roles");
            allowedRoleMentions
                    .forEach(id -> roles.add(id.toString()));
        }

        if (allowedMentionTypes.contains(AllowedMentionType.USERS)) {
            parse.add("users");
        } else {
            ArrayNode users = object.putArray("users");
            allowedUserMentions
                    .forEach(id -> users.add(id.toString()));
        }

        if (allowedMentionTypes.contains(AllowedMentionType.EVERYONE)) {
            parse.add("everyone");
        }

        return object;
    }
}
