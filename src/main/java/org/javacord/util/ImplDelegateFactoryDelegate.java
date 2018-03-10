package org.javacord.util;

import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.ImplDiscordApiBuilderDelegate;
import org.javacord.entity.message.MessageBuilderDelegate;
import org.javacord.entity.message.embed.EmbedBuilderDelegate;
import org.javacord.entity.message.embed.impl.ImplEmbedBuilderDelegate;
import org.javacord.entity.message.impl.ImplMessageBuilderDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsBuilderDelegate;
import org.javacord.entity.permission.impl.ImplPermissionsBuilderDelegate;

/**
 * The implementation of {@link DelegateFactoryDelegate}.
 */
public class ImplDelegateFactoryDelegate implements DelegateFactoryDelegate {

    @Override
    public DiscordApiBuilderDelegate createDiscordApiBuilderDelegate() {
        return new ImplDiscordApiBuilderDelegate();
    }

    @Override
    public EmbedBuilderDelegate createEmbedBuilderDelegate() {
        return new ImplEmbedBuilderDelegate();
    }

    @Override
    public MessageBuilderDelegate createMessageBuilderDelegate() {
        return new ImplMessageBuilderDelegate();
    }

    @Override
    public PermissionsBuilderDelegate createPermissionsBuilderDelegate() {
        return new ImplPermissionsBuilderDelegate();
    }

    @Override
    public PermissionsBuilderDelegate createPermissionsBuilderDelegate(Permissions permissions) {
        return new ImplPermissionsBuilderDelegate(permissions);
    }

}
