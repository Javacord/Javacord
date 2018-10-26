package org.javacord.core.util.gateway

import com.neovisionaries.ws.client.OpeningHandshakeException
import com.neovisionaries.ws.client.WebSocketException
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.test.appender.ListAppender
import org.javacord.core.DiscordApiImpl
import org.javacord.core.util.concurrent.ThreadPoolImpl
import org.javacord.test.MockProxyManager
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.verify.VerificationTimes
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.util.environment.RestoreSystemProperties

import javax.net.ssl.SSLHandshakeException

@Subject(DiscordWebSocketAdapter)
class DiscordWebSocketAdapterTest extends Specification {

    @Shared
    def originalGateway

    @Shared
    @AutoCleanup("shutdown")
    def threadPool = new ThreadPoolImpl()

    def setupSpec() {
        originalGateway = DiscordWebSocketAdapter.gateway
        DiscordWebSocketAdapter.gateway = 'wss://gateway-url'
    }

    def cleanupSpec() {
        DiscordWebSocketAdapter.gateway = originalGateway
    }

    @RestoreSystemProperties
    def 'WebSocket calls with a man-in-the-middle attack fail'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            MockProxyManager.setHttpSystemProperties()
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            ListAppender.getListAppender('Test Appender').events
                    .findAll { it.level == Level.WARN }
                    .findAll { it.thrown }
                    .each { throw it.thrown }

        then:
            WebSocketException wse = thrown()
            wse.cause instanceof SSLHandshakeException
    }

    @RestoreSystemProperties
    def 'WebSocket calls with man-in-the-middle attack allowed do not fail with handshake exception'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            MockProxyManager.setHttpSystemProperties()
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            ListAppender.getListAppender('Test Appender').events
                    .findAll { it.level == Level.WARN }
                    .findAll { it.thrown }
                    .each { throw it.thrown }

        then:
            OpeningHandshakeException ohe = thrown()
            ohe.message == 'The status code of the opening handshake response is not \'101 Switching Protocols\'. ' +
                    'The status line is: HTTP/1.1 404 Not Found'

        and:
            MockProxyManager.mockProxy.verify HttpRequest.request(), VerificationTimes.atLeast(1)
    }

    @RestoreSystemProperties
    def 'WebSocket calls are done via system properties configured HTTP proxy'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            MockProxyManager.setHttpSystemProperties()
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            ListAppender.getListAppender('Test Appender').events
                    .findAll { it.level == Level.WARN }
                    .findAll { it.thrown }
                    .each { throw it.thrown }

        then:
            OpeningHandshakeException ohe = thrown()
            ohe.message == 'The status code of the opening handshake response is not \'101 Switching Protocols\'. ' +
                    'The status line is: HTTP/1.1 404 Not Found'

        and:
            MockProxyManager.mockProxy.verify HttpRequest.request(), VerificationTimes.atLeast(1)
    }

    def 'WebSocket calls are done via system default proxy selector configured proxy'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            def defaultProxySelector = ProxySelector.default
            ProxySelector.default = MockProxyManager.proxySelector
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            ListAppender.getListAppender('Test Appender').events
                    .findAll { it.level == Level.WARN }
                    .findAll { it.thrown }
                    .each { throw it.thrown }

        then:
            OpeningHandshakeException ohe = thrown()
            ohe.message == 'The status code of the opening handshake response is not \'101 Switching Protocols\'. ' +
                    'The status line is: HTTP/1.1 404 Not Found'

        and:
            MockProxyManager.mockProxy.verify HttpRequest.request(), VerificationTimes.atLeast(1)

        cleanup:
            ProxySelector.default = defaultProxySelector
    }

}
