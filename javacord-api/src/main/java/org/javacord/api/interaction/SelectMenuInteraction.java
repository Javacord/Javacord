package org.javacord.api.interaction;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface SelectMenuInteraction extends MessageComponentInteraction {

    /**
     * Gets the selected roles.
     * Only available if the select menu is of type {@link ComponentType#SELECT_MENU_ROLE}.
     *
     * @return The selected roles.
     */
    List<Role> getSelectedRoles();

    /**
     * Gets the selected users.
     * Only available if the select menu is of type {@link ComponentType#SELECT_MENU_USER}.
     *
     * @return The selected users.
     */
    List<User> getSelectedUsers();

    /**
     * Gets the selected channels.
     * Only available if the select menu is of type {@link ComponentType#SELECT_MENU_CHANNEL}.
     * If sent in a DM channel, this will always be empty.
     *
     * @return The selected channels.
     */
    List<ServerChannel> getSelectedChannels();

    /**
     * Gets the selected mentionables.
     * Only available if the select menu is of type {@link ComponentType#SELECT_MENU_MENTIONABLE}.
     *
     * @return The selected mentionables.
     */
    List<Mentionable> getSelectedMentionables();

    /**
     * Get the options the user was chosen.
     * Only available when using a select menu of type {@link ComponentType#SELECT_MENU_STRING}.
     *
     * @return The options.
     */
    List<SelectMenuOption> getChosenOptions();

    /**
     * Get all options from the select menu.
     * Only available when using a select menu of type {@link ComponentType#SELECT_MENU_STRING}.
     *
     * @return All options.
     */
    List<SelectMenuOption> getPossibleOptions();

    /**
     * Get the custom id of the select menu.
     *
     * @return The custom ID.
     */
    String getCustomId();

    /**
     * Get the placeholder of the select menu.
     *
     * @return The placeholder.
     */
    Optional<String> getPlaceholder();

    /**
     * Gets the minimum amount of options which must be selected.
     *
     * @return The min values.
     */
    int getMinimumValues();

    /**
     * Gets the maximum amount of options which can be selected.
     *
     * @return The max values.
     */
    int getMaximumValues();

}
