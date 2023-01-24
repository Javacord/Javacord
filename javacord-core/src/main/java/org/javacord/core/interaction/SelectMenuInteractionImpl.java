package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.component.SelectMenuOptionImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.util.logging.LoggerUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SelectMenuInteractionImpl extends MessageComponentInteractionImpl implements SelectMenuInteraction {

    private static final Logger logger = LoggerUtil.getLogger(SelectMenuInteractionImpl.class);
    private final List<SelectMenuOption> selectMenuOptions = new ArrayList<>();
    private final List<SelectMenuOption> chosenSelectMenuOption = new ArrayList<>();
    private final List<ServerChannel> selectMenuChannels = new ArrayList<>();
    private final List<User> selectMenuUsers = new ArrayList<>();
    private final List<Role> selectMenuRoles = new ArrayList<>();
    private final List<Mentionable> selectMenuMentionables = new ArrayList<>();
    private String placeholder;
    private int minimumValues;
    private int maximumValues;
    private final ComponentType componentType;

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public SelectMenuInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);

        this.componentType = ComponentType.fromId(jsonData.get("data").get("component_type").asInt());

        JsonNode messageObject = jsonData.get("message");
        JsonNode componentsObject = messageObject.get("components");

        JsonNode dataObject = jsonData.get("data");
        outerLoop:
        for (JsonNode highLevelComponent : componentsObject) {
            for (JsonNode lowLevelComponent : highLevelComponent.get("components")) {
                if (componentType.isSelectMenuType()
                        && lowLevelComponent.has("custom_id")
                        && lowLevelComponent.get("custom_id").asText().equals(dataObject.get("custom_id").asText())) {
                    placeholder = lowLevelComponent.has("placeholder")
                            ? lowLevelComponent.get("placeholder").asText()
                            : null;
                    maximumValues = lowLevelComponent.has("max_values")
                            ? lowLevelComponent.get("max_values").asInt()
                            : 1;
                    minimumValues = lowLevelComponent.has("min_values")
                            ? lowLevelComponent.get("min_values").asInt()
                            : 1;
                    if (lowLevelComponent.hasNonNull("options")) {
                        for (JsonNode optionObject : lowLevelComponent.get("options")) {
                            selectMenuOptions.add(new SelectMenuOptionImpl(optionObject));
                        }
                    }
                    break outerLoop;
                }
            }
        }

        JsonNode valuesArray = dataObject.get("values");

        switch (componentType) {
            case SELECT_MENU_CHANNEL:
                selectMenuChannels.addAll(getSelectMenuChannels(valuesArray));
                break;
            case SELECT_MENU_ROLE:
                selectMenuRoles.addAll(getSelectMenuRoles(valuesArray));
                break;
            case SELECT_MENU_USER:
                selectMenuUsers.addAll(getSelectMenuUsers(dataObject));
                break;
            case SELECT_MENU_MENTIONABLE:
                selectMenuMentionables.addAll(getSelectMenuRoles(valuesArray));
                selectMenuMentionables.addAll(getSelectMenuUsers(dataObject));
                break;
            case SELECT_MENU_STRING:
                for (JsonNode jsonNode : valuesArray) {
                    chosenSelectMenuOption.addAll(selectMenuOptions.stream()
                            .filter(option -> option.getValue().equals(jsonNode.asText()))
                            .collect(Collectors.toList()));
                }
                break;
            default:
                logger.warn("Creating a SelectMenuInteractionImpl with an unhandled Select Menu component"
                        + " type which is most likely not wanted!");
                break;
        }
    }

    private List<User> getSelectMenuUsers(JsonNode dataObject) {
        List<User> usersList = new ArrayList<>();
        JsonNode valuesArray = dataObject.get("values");
        JsonNode resolved = dataObject.get("resolved");
        JsonNode users = resolved.get("users");
        JsonNode members = resolved.get("members");

        for (JsonNode value : valuesArray) {
            final long id = value.asLong();
            Optional<User> optionalMember = getServer().flatMap(server -> server.getMemberById(id));
            Optional<User> optionalUser = getApi().getCachedUserById(id);

            if (optionalMember.isPresent()) {
                usersList.add(optionalMember.orElseThrow(AssertionError::new));
            } else if (optionalUser.isPresent()) {
                usersList.add(optionalUser.orElseThrow(AssertionError::new));
            } else if (members.has(value.asText())) {
                getServer().ifPresent(server -> {
                    usersList.add(new UserImpl(((DiscordApiImpl) getApi()), users.get(value.asText()),
                            members.get(value.asText()),
                            (ServerImpl) server));
                });
            } else if (users.has(value.asText())) {
                usersList.add(new UserImpl(((DiscordApiImpl) getApi()),
                        users.get(value.asText()), (MemberImpl) null, null));
            }
        }

        return usersList;
    }

    private List<ServerChannel> getSelectMenuChannels(JsonNode valuesArray) {
        List<ServerChannel> channels = new ArrayList<>();
        for (JsonNode jsonNode : valuesArray) {
            getServer().flatMap(server -> server.getChannelById(jsonNode.asLong()))
                    .ifPresent(channels::add);
        }
        return channels;
    }

    private List<Role> getSelectMenuRoles(JsonNode valuesArray) {
        List<Role> roles = new ArrayList<>();
        for (JsonNode jsonNode : valuesArray) {
            getServer().flatMap(server -> server.getRoleById(jsonNode.asLong()))
                    .ifPresent(roles::add);
        }
        return roles;
    }

    @Override
    public ComponentType getComponentType() {
        return componentType;
    }

    @Override
    public List<Role> getSelectedRoles() {
        return Collections.unmodifiableList(selectMenuRoles);
    }

    @Override
    public List<User> getSelectedUsers() {
        return Collections.unmodifiableList(selectMenuUsers);
    }

    @Override
    public List<ServerChannel> getSelectedChannels() {
        return Collections.unmodifiableList(selectMenuChannels);
    }

    @Override
    public List<Mentionable> getSelectedMentionables() {
        return Collections.unmodifiableList(selectMenuMentionables);
    }

    @Override
    public List<SelectMenuOption> getChosenOptions() {
        return chosenSelectMenuOption;
    }

    @Override
    public List<SelectMenuOption> getPossibleOptions() {
        return selectMenuOptions;
    }


    @Override
    public Optional<String> getPlaceholder() {
        return Optional.ofNullable(placeholder);
    }

    @Override
    public int getMinimumValues() {
        return minimumValues;
    }

    @Override
    public int getMaximumValues() {
        return maximumValues;
    }
}
