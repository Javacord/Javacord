package org.javacord.api.entity.message.component;

import org.javacord.api.entity.emoji.Emoji;

import java.util.Optional;

public interface Button extends Component {
    ButtonStyle getStyle();

    Optional<String> getCustomID();

    Optional<String> getLabel();

    Optional<String> getUrl();

    Optional<Boolean> isDisabled();

    Optional<Emoji> getEmoji();
}
