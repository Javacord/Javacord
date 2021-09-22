package org.javacord.core.interaction;

import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.internal.ApplicationCommandUpdaterDelegate;

public abstract class ApplicationCommandUpdaterDelegateImpl<T extends ApplicationCommand>
        implements ApplicationCommandUpdaterDelegate<T> {

    protected long commandId;

    protected String name = null;

    protected boolean defaultPermission = true;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDefaultPermission(boolean defaultPermission) {
        this.defaultPermission = defaultPermission;
    }
}
