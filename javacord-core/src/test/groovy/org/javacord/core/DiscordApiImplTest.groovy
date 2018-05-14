package org.javacord.core

import org.javacord.api.entity.server.Server
import spock.lang.Specification
import spock.lang.Subject

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

}
