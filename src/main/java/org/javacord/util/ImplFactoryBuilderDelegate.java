package org.javacord.util;

import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.ImplDiscordApiBuilderDelegate;
import org.javacord.entity.message.MessageBuilderDelegate;
import org.javacord.entity.message.embed.EmbedBuilderDelegate;
import org.javacord.entity.message.embed.impl.ImplEmbedBuilderDelegate;
import org.javacord.entity.message.impl.ImplMessageBuilderDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsFactory;
import org.javacord.entity.permission.impl.ImplPermissionsFactory;

/**
 * The implementation of {@code FactoryBuilderDelegate}.
 */
public class ImplFactoryBuilderDelegate implements FactoryBuilderDelegate {

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
    public PermissionsFactory createPermissionsFactory() {
        return new ImplPermissionsFactory();
    }

    @Override
    public PermissionsFactory createPermissionsFactory(Permissions permissions) {
        return new ImplPermissionsFactory(permissions);
    }

}
