package org.javacord.core.entity.message.component.internal;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.message.component.internal.SelectMenuBuilderDelegate;
import org.javacord.core.entity.message.component.SelectMenuImpl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class SelectMenuBuilderDelegateImpl implements SelectMenuBuilderDelegate {
    private ComponentType type = null;

    private List<SelectMenuOption> options = new ArrayList<>();

    private EnumSet<ChannelType> channelTypes = EnumSet.noneOf(ChannelType.class);

    private String placeholder = null;

    private int minimumValues = 1;

    private int maximumValues = 1;

    private String customId = null;

    private boolean isDisabled = false;

    @Override
    public ComponentType getType() {
        return type;
    }

    @Override
    public void setType(ComponentType type) {
        this.type = type;
    }

    @Override
    public void copy(SelectMenu selectMenu) {
        Optional<String> placeholder = selectMenu.getPlaceholder();
        this.customId = selectMenu.getCustomId();
        this.minimumValues = selectMenu.getMinimumValues();
        this.maximumValues = selectMenu.getMaximumValues();
        this.isDisabled = selectMenu.isDisabled();
        this.options = selectMenu.getOptions();
        this.channelTypes = selectMenu.getChannelTypes();
        this.type = selectMenu.getType();

        placeholder.ifPresent(this::setPlaceholder);
    }

    @Override
    public void addChannelType(ChannelType channelType) {
        channelTypes.add(channelType);
    }

    @Override
    public void addOption(SelectMenuOption selectMenuOption) {
        options.add(selectMenuOption);
    }

    @Override
    public void removeOption(SelectMenuOption selectMenuOption) {
        options.remove(selectMenuOption);
    }

    @Override
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public void setMinimumValues(int minimumValues) {
        this.minimumValues = minimumValues;
    }

    @Override
    public void setMaximumValues(int maximumValues) {
        this.maximumValues = maximumValues;
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @Override
    public SelectMenu build() {
        return new SelectMenuImpl(type, options, placeholder, customId, minimumValues, maximumValues, isDisabled,
                channelTypes);
    }

    @Override
    public void removeAllOptions() {
        this.options.clear();
    }

    @Override
    public String getCustomId() {
        return customId;
    }
}
