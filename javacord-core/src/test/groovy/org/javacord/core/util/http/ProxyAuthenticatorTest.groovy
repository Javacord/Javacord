package org.javacord.core.util.http

import io.netty.handler.codec.http.HttpHeaderNames
import okhttp3.Address
import okhttp3.Credentials
import okhttp3.Dns
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.net.SocketFactory
import java.net.Authenticator as JNAuthenticator

@Subject(ProxyAuthenticator)
class ProxyAuthenticatorTest extends Specification {

    @Subject
    def proxyAuthenticator = new ProxyAuthenticator()

    @Unroll
    def 'correct authentication header is set from default authenticator (realmToAnswer: "#realmToAnswer")'(
            realmToAnswer) {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            def credentials = Credentials.basic username, password

        and:
            def defaultAuthenticator = JNAuthenticator.theAuthenticator
            JNAuthenticator.default = new JNAuthenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    if (requestingPrompt == realmToAnswer) {
                        new PasswordAuthentication(username, password as char[])
                    }
                }
            }

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                            80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def response = new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .addHeader(HttpHeaderNames.PROXY_AUTHENTICATE as String, 'Basic realm=foo')
                    .addHeader(HttpHeaderNames.PROXY_AUTHENTICATE as String, 'Basic realm=bar')
                    .build()

        when:
            def returnedRequest = proxyAuthenticator.authenticate route, response

        then:
            returnedRequest
            returnedRequest.headers(HttpHeaderNames.PROXY_AUTHORIZATION as String) == [credentials]

        cleanup:
            JNAuthenticator.default = defaultAuthenticator

        where:
            realmToAnswer << ['foo', 'bar']
    }

    @Unroll
    def 'credentials are not retried twice in a row with default authenticator (realmToAnswer: "#realmToAnswer")'(
            realmToAnswer) {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            def credentials = Credentials.basic username, password

        and:
            def defaultAuthenticator = JNAuthenticator.theAuthenticator
            JNAuthenticator.default = new JNAuthenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    if (requestingPrompt == realmToAnswer) {
                        new PasswordAuthentication(username, password as char[])
                    }
                }
            }

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                            80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def request = new Request.Builder()
                    .url('http://localhost')
                    .header(HttpHeaderNames.PROXY_AUTHORIZATION as String, credentials)
                    .build()
            def response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .addHeader(HttpHeaderNames.PROXY_AUTHENTICATE as String, 'Basic realm=foo')
                    .addHeader(HttpHeaderNames.PROXY_AUTHENTICATE as String, 'Basic realm=bar')
                    .build()

        expect:
            !proxyAuthenticator.authenticate(route, response)

        cleanup:
            JNAuthenticator.default = defaultAuthenticator

        where:
            realmToAnswer << ['foo', 'bar']
    }

    @Unroll
    def 'new credentials are tried if others did not work with default authenticator (realmToAnswer: "#realmToAnswer")'(
            realmToAnswer) {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            def credentials = Credentials.basic username, password

        and:
            def defaultAuthenticator = JNAuthenticator.theAuthenticator
            JNAuthenticator.default = new JNAuthenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    if (requestingPrompt == realmToAnswer) {
                        new PasswordAuthentication(username, password as char[])
                    }
                }
            }

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                            80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def request = new Request.Builder()
                    .url('http://localhost')
                    .header(HttpHeaderNames.PROXY_AUTHORIZATION as String, 'wrong credentials')
                    .build()
            def response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .addHeader(HttpHeaderNames.PROXY_AUTHENTICATE as String, 'Basic realm=foo')
                    .addHeader(HttpHeaderNames.PROXY_AUTHENTICATE as String, 'Basic realm=bar')
                    .build()

        when:
            def returnedRequest = proxyAuthenticator.authenticate route, response

        then:
            returnedRequest
            returnedRequest.headers(HttpHeaderNames.PROXY_AUTHORIZATION as String) == [credentials]

        cleanup:
            JNAuthenticator.default = defaultAuthenticator

        where:
            realmToAnswer << ['foo', 'bar']
    }

    @Unroll
    def 'correct authentication header is set from explicit authenticator'() {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            def credentials = Credentials.basic username, password

        and:
            def proxyAuthenticator = new ProxyAuthenticator({ route, request, response ->
                [(HttpHeaderNames.PROXY_AUTHORIZATION as String): [credentials]]
            })

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                             80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def response = new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .addHeader(HttpHeaderNames.PROXY_AUTHENTICATE as String, 'Basic realm=foo')
                    .build()

        when:
            def returnedRequest = proxyAuthenticator.authenticate route, response

        then:
            returnedRequest
            returnedRequest.headers(HttpHeaderNames.PROXY_AUTHORIZATION as String) == [credentials]
    }

    @Unroll
    def 'header is removed from request when value list is null'() {
        given:
            def proxyAuthenticator = new ProxyAuthenticator({ route, request, response ->
                [(HttpHeaderNames.PROXY_AUTHORIZATION as String): null]
            })

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                             80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def request = new Request.Builder()
                    .url('http://localhost')
                    .header(HttpHeaderNames.PROXY_AUTHORIZATION as String, 'wrong credentials')
                    .build()
            def response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .build()

        when:
            def returnedRequest = proxyAuthenticator.authenticate route, response

        then:
            returnedRequest
            returnedRequest.headers(HttpHeaderNames.PROXY_AUTHORIZATION as String) == []
    }

    @Unroll
    def 'header is removed from request when first entry in value list is null'() {
        given:
            def proxyAuthenticator = new ProxyAuthenticator({ route, request, response ->
                [(HttpHeaderNames.PROXY_AUTHORIZATION as String): [null]]
            })

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                             80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def request = new Request.Builder()
                    .url('http://localhost')
                    .header(HttpHeaderNames.PROXY_AUTHORIZATION as String, 'wrong credentials')
                    .build()
            def response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .build()

        when:
            def returnedRequest = proxyAuthenticator.authenticate route, response

        then:
            returnedRequest
            returnedRequest.headers(HttpHeaderNames.PROXY_AUTHORIZATION as String) == []
    }

    @Unroll
    def 'no request is prepared if authenticator did not provide a response'() {
        given:
            def proxyAuthenticator = new ProxyAuthenticator({ route, request, response -> null })

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                             80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def request = new Request.Builder()
                    .url('http://localhost')
                    .header(HttpHeaderNames.PROXY_AUTHORIZATION as String, 'wrong credentials')
                    .build()
            def response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .build()

        expect:
            !proxyAuthenticator.authenticate(route, response)
    }

    @Unroll
    def 'no request is prepared if authenticator responds with empty map'() {
        given:
            def proxyAuthenticator = new ProxyAuthenticator({ route, request, response -> [:] })

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                             80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def request = new Request.Builder()
                    .url('http://localhost')
                    .header(HttpHeaderNames.PROXY_AUTHORIZATION as String, 'wrong credentials')
                    .build()
            def response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .build()

        expect:
            !proxyAuthenticator.authenticate(route, response)
    }

    @Unroll
    def 'headers are not changed if value list is empty'() {
        given:
            def proxyAuthenticator = new ProxyAuthenticator({ route, request, response ->
                [(HttpHeaderNames.PROXY_AUTHORIZATION as String): []]
            })

        and:
            def proxy = new Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved('localhost', 80))
            def route = new Route(
                    new Address('localhost',
                            80,
                            Stub(Dns),
                            Stub(SocketFactory),
                            null,
                            null,
                            null,
                            Stub(okhttp3.Authenticator),
                            null,
                            [],
                            [],
                            Stub(ProxySelector)),
                    proxy,
                    Stub(InetSocketAddress))
            def request = new Request.Builder()
                    .url('http://localhost')
                    .header(HttpHeaderNames.PROXY_AUTHORIZATION as String, 'wrong credentials')
                    .build()
            def response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
                    .build()

        when:
            def returnedRequest = proxyAuthenticator.authenticate route, response

        then:
            returnedRequest
            returnedRequest.headers(HttpHeaderNames.PROXY_AUTHORIZATION as String) == ['wrong credentials']
    }

}
