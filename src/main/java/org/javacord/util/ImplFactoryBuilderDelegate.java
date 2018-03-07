package org.javacord.util;

import org.javacord.DiscordApiFactory;
import org.javacord.ImplDiscordApiFactory;
import org.javacord.entity.message.MessageFactory;
import org.javacord.entity.message.embed.EmbedFactory;
import org.javacord.entity.message.embed.impl.ImplEmbedFactory;
import org.javacord.entity.message.impl.ImplMessageFactory;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsFactory;
import org.javacord.entity.permission.impl.ImplPermissionsFactory;

/**
 * The implementation of {@code FactoryBuilderDelegate}.
 */
public class ImplFactoryBuilderDelegate implements FactoryBuilderDelegate {

    @Override
    public DiscordApiFactory createDiscordApiFactory() {
        return new ImplDiscordApiFactory();
    }

    @Override
    public EmbedFactory createEmbedFactory() {
        return new ImplEmbedFactory();
    }

    @Override
    public MessageFactory createMessageFactory() {
        return new ImplMessageFactory();
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
