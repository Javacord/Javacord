package org.javacord.core.entity.emoji

import org.javacord.api.entity.emoji.CustomEmoji
import spock.lang.Specification

class CustomEmojiImplTest extends Specification {
    def "equalsEmoji returns false for different custom emojis"() {
        setup:
            CustomEmoji one = new CustomEmojiImpl(null, 1, "one", false)
            CustomEmoji two = new CustomEmojiImpl(null, 2, "two", false)

        expect:
            !one.equalsEmoji(two)
    }
}
