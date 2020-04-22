package org.javacord.core.entity.message.mention;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.mention.AllowedMentions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AllowedMentionsImpl implements AllowedMentions {


    private final List<Long> allowedRoleMentions;
    private final List<Long> allowedUserMentions;
    private final List<String> generalMentions;

    private final boolean mentionAllRoles;
    private final boolean mentionAllUsers;
    private final boolean mentionEveryoneAndHere;

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
        this.mentionAllRoles = mentionAllRoles;
        this.mentionAllUsers = mentionAllUsers;
        this.mentionEveryoneAndHere = mentionEveryoneAndHere;


        if (allowedRoleMentions == null || allowedRoleMentions.isEmpty()) {
            this.allowedRoleMentions = null;
        } else {
            this.allowedRoleMentions = allowedRoleMentions;
        }

        if (allowedUserMentions == null || allowedUserMentions.isEmpty()) {
            this.allowedUserMentions = null;
        } else {
            this.allowedUserMentions = allowedUserMentions;
        }

        if (mentionAllRoles || mentionAllUsers || mentionEveryoneAndHere) {
            generalMentions = new ArrayList<>();
            if (mentionAllRoles) {
                generalMentions.add("roles");
            }
            if (mentionAllUsers) {
                generalMentions.add("users");
            }
            if (mentionEveryoneAndHere) {
                generalMentions.add("everyone");
            }
        } else {
            generalMentions = null;
        }
    }

    @Override
    public Optional<List<Long>> getAllowedRoleMentions() {
        return Optional.ofNullable(allowedRoleMentions);
    }

    @Override
    public Optional<List<Long>> getAllowedUserMentions() {
        return Optional.ofNullable(allowedUserMentions);
    }

    @Override
    public Optional<List<String>> getGeneralMentions() {
        return Optional.ofNullable(generalMentions);
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
        ArrayNode users = object.putArray("users");
        ArrayNode roles = object.putArray("roles");

        if (mentionAllRoles) {
            parse.add("roles");
        } else {
            if (allowedRoleMentions.size() > 100) {
                allowedRoleMentions.subList(0, 99).forEach(aLong -> roles.add(aLong.toString()));
            } else {
                allowedRoleMentions.forEach(aLong -> roles.add(aLong.toString()));
            }
        }

        if (mentionAllUsers) {
            parse.add("users");
        } else {
            if (allowedUserMentions.size() > 100) {
                allowedUserMentions.subList(0, 99).forEach(aLong -> users.add(aLong.toString()));
            } else {
                allowedUserMentions.forEach(aLong -> users.add(aLong.toString()));
            }
        }

        if (mentionEveryoneAndHere) {
            parse.add("everyone");
        }
        return object;
    }
}
