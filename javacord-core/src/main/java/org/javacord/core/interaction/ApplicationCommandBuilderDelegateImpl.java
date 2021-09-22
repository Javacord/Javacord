package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.internal.ApplicationCommandBuilderDelegate;

public abstract class ApplicationCommandBuilderDelegateImpl<T extends ApplicationCommand>
        implements ApplicationCommandBuilderDelegate<T> {

    protected String name;

    protected Boolean defaultPermission;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDefaultPermission(Boolean defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    /**
     * Gets the JSON body for this application command.
     *
     * @return The JSON body for this application command.
     */
    public abstract ObjectNode getJsonBodyForApplicationCommand();
}
