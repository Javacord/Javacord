package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.entity.server.ArchivedThreads;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.channel.ThreadMemberImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArchivedThreadsImpl implements ArchivedThreads {

    final List<ServerThreadChannel> serverThreadChannels;
    final List<ThreadMember> threadMembers;
    final boolean hasMoreThreads;

    /**
     * Creates a new ActiveThread.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public ArchivedThreadsImpl(final DiscordApiImpl api, final ServerImpl server, final JsonNode data) {
        serverThreadChannels = new ArrayList<>();
        for (final JsonNode jsonNode : data.get("threads")) {
            serverThreadChannels.add(server.getOrCreateServerThreadChannel(jsonNode));
        }

        threadMembers = new ArrayList<>();
        for (final JsonNode jsonNode : data.get("members")) {
            threadMembers.add(new ThreadMemberImpl(api, server, jsonNode));
        }

        hasMoreThreads = data.get("has_more").asBoolean();
    }

    @Override
    public List<ServerThreadChannel> getServerThreadChannels() {
        return Collections.unmodifiableList(serverThreadChannels);
    }

    @Override
    public List<ThreadMember> getThreadMembers() {
        return Collections.unmodifiableList(threadMembers);
    }

    @Override
    public boolean hasMoreThreads() {
        return hasMoreThreads;
    }
}
