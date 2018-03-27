package org.javacord.api.event.channel.group;

import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.event.channel.TextChannelEvent;
import org.javacord.api.event.channel.VoiceChannelEvent;

/**
 * A group channel event.
 */
public interface GroupChannelEvent extends TextChannelEvent, VoiceChannelEvent {

    @Override
    GroupChannel getChannel();

}
