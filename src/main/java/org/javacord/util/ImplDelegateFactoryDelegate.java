package org.javacord.util;

import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.ImplDiscordApiBuilderDelegate;
import org.javacord.entity.channel.ChannelCategoryBuilderDelegate;
import org.javacord.entity.channel.ServerTextChannelBuilderDelegate;
import org.javacord.entity.channel.ServerVoiceChannelBuilderDelegate;
import org.javacord.entity.channel.impl.ImplChannelCategoryBuilderDelegate;
import org.javacord.entity.channel.impl.ImplServerTextChannelBuilderDelegate;
import org.javacord.entity.channel.impl.ImplServerVoiceChannelBuilderDelegate;
import org.javacord.entity.message.MessageBuilderDelegate;
import org.javacord.entity.message.embed.EmbedBuilderDelegate;
import org.javacord.entity.message.embed.impl.ImplEmbedBuilderDelegate;
import org.javacord.entity.message.impl.ImplMessageBuilderDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsBuilderDelegate;
import org.javacord.entity.permission.impl.ImplPermissionsBuilderDelegate;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.impl.ImplServer;

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

    @Override
    public ChannelCategoryBuilderDelegate createChannelCategoryBuilderDelegate(Server server) {
        return new ImplChannelCategoryBuilderDelegate((ImplServer) server);
    }

    @Override
    public ServerTextChannelBuilderDelegate createServerTextChannelBuilderDelegate(Server server) {
        return new ImplServerTextChannelBuilderDelegate((ImplServer) server);
    }

    @Override
    public ServerVoiceChannelBuilderDelegate createServerVoiceChannelBuilderDelegate(Server server) {
        return new ImplServerVoiceChannelBuilderDelegate((ImplServer) server);
    }

}
