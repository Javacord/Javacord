package org.javacord.core.util.gateway

import com.neovisionaries.ws.client.OpeningHandshakeException
import com.neovisionaries.ws.client.WebSocketException
import io.netty.handler.codec.http.HttpHeaderNames
import okhttp3.Credentials
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.test.appender.ListAppender
import org.javacord.core.DiscordApiImpl
import org.javacord.core.util.concurrent.ThreadPoolImpl
import org.javacord.test.MockProxyManager
import org.mockserver.configuration.ConfigurationProperties
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.verify.VerificationTimes
import spock.lang.AutoCleanup
import spock.lang.IgnoreIf
import spock.lang.PendingFeature
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
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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

    def 'WebSocket calls are done via explicitly configured proxy'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                getProxy() >> Optional.of(MockProxyManager.httpProxy)
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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

    def 'WebSocket calls are done via explicitly configured proxy selector'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                getProxySelector() >> Optional.of(MockProxyManager.proxySelector)
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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
    def 'WebSocket calls through authenticated HTTP proxy use the system default authenticator'() {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            ConfigurationProperties.proxyAuthenticationUsername username
            ConfigurationProperties.proxyAuthenticationPassword password

        and:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)

        and:
            def defaultAuthenticator = Authenticator.theAuthenticator
            Authenticator.default = Mock(Authenticator) {
                (1.._) * getPasswordAuthentication() >> new PasswordAuthentication(username, password as char[])
            }
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                getProxy() >> Optional.of(MockProxyManager.httpProxy)
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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
            Authenticator.default = defaultAuthenticator
    }

    @RestoreSystemProperties
    def 'WebSocket calls through authenticated HTTP proxy use an explicit authenticator'() {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            String credentials = Credentials.basic username, password
            ConfigurationProperties.proxyAuthenticationUsername username
            ConfigurationProperties.proxyAuthenticationPassword password

        and:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)

        and:
            org.javacord.api.util.auth.Authenticator authenticator = Mock {
                (1.._) * authenticate(_, _, _) >> [(HttpHeaderNames.PROXY_AUTHORIZATION as String): [null, credentials]]
            }
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                getProxy() >> Optional.of(MockProxyManager.httpProxy)
                getProxyAuthenticator() >> Optional.of(authenticator)
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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

    @IgnoreIf({
        // SOCKS4 implementation in Java 8 is broken and never used
        // but always SOCKS5, so do not execute this test on Java 8
        def javaVersion = System.properties.'java.version'
        javaVersion.startsWith('1.8.') || (javaVersion == '1.8') ||
                javaVersion.startsWith('8.') || (javaVersion == '8')
    })
    @PendingFeature(exceptions = MissingMethodException, reason = 'NV WebSocket does not yet support SOCKS proxies')
    @RestoreSystemProperties
    def 'WebSocket calls are done via system properties configured SOCKS4 proxy'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            MockProxyManager.setSocks4SystemProperties()

        and:
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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

    @IgnoreIf({
        // SOCKS5 implementation in Java 8 is broken when the answer
        // contains a domain type address which MockProxy supplies,
        // so do not execute this test on Java 8
        def javaVersion = System.properties.'java.version'
        javaVersion.startsWith('1.8.') || (javaVersion == '1.8') ||
                javaVersion.startsWith('8.') || (javaVersion == '8')
    })
    @PendingFeature(exceptions = [], reason = 'NV WebSocket does not yet support SOCKS proxies')
    @RestoreSystemProperties
    def 'WebSocket calls are done via system properties configured SOCKS5 proxy'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            MockProxyManager.setSocks5SystemProperties()

        and:
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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

    @IgnoreIf({
        // SOCKS5 implementation in Java 8 is broken when the answer
        // contains a domain type address which MockProxy supplies,
        // so do not execute this test on Java 8
        def javaVersion = System.properties.'java.version'
        javaVersion.startsWith('1.8.') || (javaVersion == '1.8') ||
                javaVersion.startsWith('8.') || (javaVersion == '8')
    })
    @PendingFeature(exceptions = MissingMethodException, reason = 'NV WebSocket does not yet support SOCKS proxies')
    @RestoreSystemProperties
    def 'WebSocket calls through authenticated SOCKS5 proxy use the system default authenticator'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)

        and:
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                getProxy() >> Optional.of(MockProxyManager.socksProxy)
                isTrustAllCertificates() >> true
            }

        and:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            ConfigurationProperties.socksProxyServerUsername username
            ConfigurationProperties.socksProxyServerPassword password

        and:
            def defaultAuthenticator = Authenticator.theAuthenticator
            Authenticator.default = Mock(Authenticator) {
                (1.._) * getPasswordAuthentication() >> new PasswordAuthentication(username, password as char[])
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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
            Authenticator.default = defaultAuthenticator
    }

    @RestoreSystemProperties
    def 'Explicitly configured proxy for WebSocket calls takes precedence'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)

        and:
            System.properties.'https.proxyHost' = '0.0.0.1'
            System.properties.'https.proxyPort' = '1'
            def defaultProxySelector = ProxySelector.default
            ProxySelector.default = new ProxySelector() {
                @Override
                List<Proxy> select(URI uri) {
                    [new Proxy(
                            Proxy.Type.HTTP,
                            new InetSocketAddress(InetAddress.getByAddress([0, 0, 0, 1] as byte[]), 1))]
                }

                @Override
                void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                }
            }

        and:
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                getProxy() >> Optional.of(MockProxyManager.httpProxy)
                getProxySelector() >> Optional.of(ProxySelector.default)
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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

    @RestoreSystemProperties
    def 'Explicitly configured proxy selector for WebSocket calls takes precedence'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)

        and:
            System.properties.'https.proxyHost' = '0.0.0.1'
            System.properties.'https.proxyPort' = '1'
            def defaultProxySelector = ProxySelector.default
            ProxySelector.default = new ProxySelector() {
                @Override
                List<Proxy> select(URI uri) {
                    [new Proxy(
                            Proxy.Type.HTTP,
                            new InetSocketAddress(InetAddress.getByAddress([0, 0, 0, 1] as byte[]), 1))]
                }

                @Override
                void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                }
            }

        and:
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                getProxySelector() >> Optional.of(MockProxyManager.proxySelector)
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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

    @RestoreSystemProperties
    def 'System default proxy selector for WebSocket calls takes precedence over system properties'() {
        given:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)

        and:
            System.properties.'https.proxyHost' = '0.0.0.1'
            System.properties.'https.proxyPort' = '1'
            def defaultProxySelector = ProxySelector.default
            ProxySelector.default = MockProxyManager.proxySelector

        and:
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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

    @RestoreSystemProperties
    def 'Explicitly configured authenticator for WebSocket calls takes precedence'() {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            String credentials = Credentials.basic username, password
            ConfigurationProperties.proxyAuthenticationUsername username
            ConfigurationProperties.proxyAuthenticationPassword password

        and:
            MockProxyManager.mockProxy.when(
                    HttpRequest.request()
            ) respond HttpResponse.response().withStatusCode(HttpURLConnection.HTTP_NOT_FOUND)

        and:
            def defaultAuthenticator = Authenticator.theAuthenticator
            Authenticator.default = Mock(Authenticator) {
                0 * getPasswordAuthentication()
            }
            org.javacord.api.util.auth.Authenticator authenticator = Mock {
                (1.._) * authenticate(_, _, _) >> [(HttpHeaderNames.PROXY_AUTHORIZATION as String): [null, credentials]]
            }
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                // do not wait for identify rate limit by using a different token each time
                getPrefixedToken() >> UUID.randomUUID().toString()
                getProxy() >> Optional.of(MockProxyManager.httpProxy)
                getProxyAuthenticator() >> Optional.of(authenticator)
                isTrustAllCertificates() >> true
            }

        when:
            new DiscordWebSocketAdapter(api, false)
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
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
            Authenticator.default = defaultAuthenticator
    }

}
