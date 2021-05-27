package org.javacord.core.entity.message.component;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ComponentType;

public class ActionRowImpl extends ComponentImpl implements ActionRow {
    public ActionRowImpl() {
        super(ComponentType.ActionRow);
    }
}
