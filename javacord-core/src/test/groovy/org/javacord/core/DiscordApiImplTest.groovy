package org.javacord.core

import org.javacord.api.entity.server.Server
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class DiscordApiImplTest extends Specification {

    @Subject
    def api = new DiscordApiImpl(null)

    def 'getAllServers returns all servers'() {
        given:
            def readyServer = Mock(Server)
            def nonReadyServer = Mock(Server)
            api.@servers << [0: readyServer]
            api.@nonReadyServers << [1: nonReadyServer]

        expect:
            with(api.allServers) {
                it.size() == 2
                it.containsAll readyServer, nonReadyServer
            }
    }

    @Unroll
    def '#collectionGetter returns unmodifiable collection'() {
        when:
            api."$collectionGetter"(*arguments).clear()

        then:
            thrown UnsupportedOperationException

        where:
            collectionGetter                         | arguments
            'getUnavailableServers'                  | []
            'getCachedUsers'                         | []
            'getCachedUsersByName'                   | [null]
            'getCachedUsersByNameIgnoreCase'         | [null]
            'getCachedUsersByNickname'               | [null, null]
            'getCachedUsersByNicknameIgnoreCase'     | [null, null]
            'getCachedUsersByDisplayName'            | [null, null]
            'getCachedUsersByDisplayNameIgnoreCase'  | [null, null]
            'getServers'                             | []
            'getServersByName'                       | [null]
            'getServersByNameIgnoreCase'             | [null]
            'getCustomEmojis'                        | []
            'getCustomEmojisByName'                  | [null]
            'getCustomEmojisByNameIgnoreCase'        | [null]
            'getRoles'                               | []
            'getRolesByName'                         | [null]
            'getRolesByNameIgnoreCase'               | [null]
            'getChannels'                            | []
            'getGroupChannels'                       | []
            'getPrivateChannels'                     | []
            'getServerChannels'                      | []
            'getChannelCategories'                   | []
            'getServerTextChannels'                  | []
            'getServerVoiceChannels'                 | []
            'getTextChannels'                        | []
            'getVoiceChannels'                       | []
            'getChannelsByName'                      | [null]
            'getChannelsByNameIgnoreCase'            | [null]
            'getTextChannelsByName'                  | [null]
            'getTextChannelsByNameIgnoreCase'        | [null]
            'getVoiceChannelsByName'                 | [null]
            'getVoiceChannelsByNameIgnoreCase'       | [null]
            'getServerChannelsByName'                | [null]
            'getServerChannelsByNameIgnoreCase'      | [null]
            'getChannelCategoriesByName'             | [null]
            'getChannelCategoriesByNameIgnoreCase'   | [null]
            'getServerTextChannelsByName'            | [null]
            'getServerTextChannelsByNameIgnoreCase'  | [null]
            'getServerVoiceChannelsByName'           | [null]
            'getServerVoiceChannelsByNameIgnoreCase' | [null]
            'getGroupChannelsByName'                 | [null]
            'getGroupChannelsByNameIgnoreCase'       | [null]
            'getAllServers'                          | []
    }

}
