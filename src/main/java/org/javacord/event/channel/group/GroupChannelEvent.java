package org.javacord.event.channel.group;

import org.javacord.entity.channel.GroupChannel;
import org.javacord.event.channel.TextChannelEvent;
import org.javacord.event.channel.VoiceChannelEvent;

/**
 * A group channel event.
 */
public interface GroupChannelEvent extends TextChannelEvent, VoiceChannelEvent {

    @Override
    GroupChannel getChannel();

}
