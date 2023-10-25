package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.UserContextMenuInteraction;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.member.MemberImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.UserImpl;
import java.util.Optional;

public class UserContextMenuInteractionImpl extends ApplicationCommandInteractionImpl
        implements UserContextMenuInteraction {

    private final Member targetMember;
    private final UserImpl targetUser;
    private final long targetId;

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public UserContextMenuInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);

        JsonNode data = jsonData.get("data");
        targetId = data.get("target_id").asLong();

        JsonNode userData = data.get("resolved").get("users").get(String.valueOf(targetId));
        targetUser = new UserImpl(api, userData);
        if (data.get("resolved").get("members").has(String.valueOf(targetId))) {
            JsonNode memberData = data.get("resolved").get("members").get(String.valueOf(targetId));
            ServerImpl server = (ServerImpl) getServer().orElseThrow(AssertionError::new);
            targetMember = server.getMemberById(targetId)
                    .orElseGet(() -> new MemberImpl(api, server, memberData, targetUser));
        } else {
            targetMember = null;
        }

    }

    @Override
    public long getTargetId() {
        return targetId;
    }

    @Override
    public User getTarget() {
        return targetUser;
    }

    @Override
    public Optional<Member> getTargetMember() {
        return Optional.ofNullable(targetMember);
    }
}
