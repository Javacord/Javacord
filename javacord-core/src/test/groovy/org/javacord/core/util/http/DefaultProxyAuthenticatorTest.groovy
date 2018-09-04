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

@Subject(DefaultProxyAuthenticator)
class DefaultProxyAuthenticatorTest extends Specification {

    @Subject
    def defaultProxyAuthenticator = new DefaultProxyAuthenticator()

    @Unroll
    def 'correct authentication header is set (challenges: "#challenges", realmToAnswer: "#realmToAnswer")'(
            challenges, realmToAnswer) {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            String credentials = Credentials.basic username, password

        and:
            def defaultAuthenticator = Authenticator.theAuthenticator
            Authenticator.default = new Authenticator() {
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
            def responseBuilder = new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
            challenges.each { responseBuilder.addHeader HttpHeaderNames.PROXY_AUTHENTICATE as String, it }
            def response = responseBuilder.build()

        when:
            def returnedRequest = defaultProxyAuthenticator.authenticate route, response

        then:
            returnedRequest
            returnedRequest.header(HttpHeaderNames.PROXY_AUTHORIZATION as String) == credentials

        cleanup:
            Authenticator.default = defaultAuthenticator

        where:
            challenges                                   | realmToAnswer
            [' ,  , Basic realm=foo']                    | 'foo'
            ['Basic realm=foo']                          | 'foo'
            ['Basic realm="foo"']                        | 'foo'
            ['Basic realm = "foo"']                      | 'foo'
            ['Basic realm = "foo",Digest']               | 'foo'
            ['Basic realm = "foo",Basic realm=bar']      | 'foo'
            ['Basic realm = "foo",Basic realm=bar']      | 'bar'
            ['Digest,Basic realm="foo"']                 | 'foo'
            ['Digest,Basic ,,realm="foo"']               | 'foo'
            ['Digest, Basic realm="foo"']                | 'foo'
            ['Digest, Basic ,,realm="foo"']              | 'foo'
            ['Digest,,,, Basic ,,realm="foo"']           | 'foo'
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']   | 'foo'
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | 'f,oo'
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"'] | 'f,oo'
            ['Digest,,,, Basic ,,,realm="f\\\\\\"o\\o"'] | 'f\\"oo'
            ['Digest', 'Basic realm=foo']                | 'foo'
            ['Basic realm=foo', 'Digest']                | 'foo'
            ['Basic realm=foo', 'Basic realm=bar']       | 'foo'
            ['Basic realm=foo', 'Basic realm=bar']       | 'bar'
    }

    @Unroll
    def 'challenge parse results are cached and reused (challenges: "#challenges", realmToAnswer: "#realmToAnswer")'(
            challenges, realmToAnswer) {
        given:
            def responseBuilder = new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
            challenges.each { responseBuilder.addHeader HttpHeaderNames.PROXY_AUTHENTICATE as String, it }
            def response = responseBuilder.build()

        when:
            def firstChallenges = defaultProxyAuthenticator.getBasicChallenges(response).toArray()
            def secondChallenges = defaultProxyAuthenticator.getBasicChallenges(response).toArray()

        then:
            (0 .. (firstChallenges.size() - 1))
                    .collectEntries { [firstChallenges[it], secondChallenges[it]] }
                    .every { first, second -> first.is second }

        where:
            challenges                                   | realmToAnswer
            [' ,  , Basic realm=foo']                    | 'foo'
            ['Basic realm=foo']                          | 'foo'
            ['Basic realm="foo"']                        | 'foo'
            ['Basic realm = "foo"']                      | 'foo'
            ['Basic realm = "foo",Digest']               | 'foo'
            ['Basic realm = "foo",Basic realm=bar']      | 'foo'
            ['Basic realm = "foo",Basic realm=bar']      | 'bar'
            ['Digest,Basic realm="foo"']                 | 'foo'
            ['Digest,Basic ,,realm="foo"']               | 'foo'
            ['Digest, Basic realm="foo"']                | 'foo'
            ['Digest, Basic ,,realm="foo"']              | 'foo'
            ['Digest,,,, Basic ,,realm="foo"']           | 'foo'
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']   | 'foo'
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | 'f,oo'
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"'] | 'f,oo'
            ['Digest,,,, Basic ,,,realm="f\\\\\\"o\\o"'] | 'f\\"oo'
            ['Digest', 'Basic realm=foo']                | 'foo'
            ['Basic realm=foo', 'Digest']                | 'foo'
            ['Basic realm=foo', 'Basic realm=bar']       | 'foo'
            ['Basic realm=foo', 'Basic realm=bar']       | 'bar'
    }

    @Unroll
    def 'credentials are not retried twice in a row (challenges: "#challenges", realmToAnswer: "#realmToAnswer")'(
            challenges, realmToAnswer) {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            String credentials = Credentials.basic username, password

        and:
            def defaultAuthenticator = Authenticator.theAuthenticator
            Authenticator.default = new Authenticator() {
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
            def responseBuilder = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
            challenges.each { responseBuilder.addHeader HttpHeaderNames.PROXY_AUTHENTICATE as String, it }
            def response = responseBuilder.build()

        expect:
            !defaultProxyAuthenticator.authenticate(route, response)

        cleanup:
            Authenticator.default = defaultAuthenticator

        where:
            challenges                                   | realmToAnswer
            [' ,  , Basic realm=foo']                    | 'foo'
            ['Basic realm=foo']                          | 'foo'
            ['Basic realm="foo"']                        | 'foo'
            ['Basic realm = "foo"']                      | 'foo'
            ['Basic realm = "foo",Digest']               | 'foo'
            ['Basic realm = "foo",Basic realm=bar']      | 'foo'
            ['Basic realm = "foo",Basic realm=bar']      | 'bar'
            ['Digest,Basic realm="foo"']                 | 'foo'
            ['Digest,Basic ,,realm="foo"']               | 'foo'
            ['Digest, Basic realm="foo"']                | 'foo'
            ['Digest, Basic ,,realm="foo"']              | 'foo'
            ['Digest,,,, Basic ,,realm="foo"']           | 'foo'
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']   | 'foo'
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | 'f,oo'
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"'] | 'f,oo'
            ['Digest,,,, Basic ,,,realm="f\\\\\\"o\\o"'] | 'f\\"oo'
            ['Digest', 'Basic realm=foo']                | 'foo'
            ['Basic realm=foo', 'Digest']                | 'foo'
            ['Basic realm=foo', 'Basic realm=bar']       | 'foo'
            ['Basic realm=foo', 'Basic realm=bar']       | 'bar'
    }

    @Unroll
    def 'new credentials are tried if others did not work (challenges: "#challenges", realmToAnswer: "#realmToAnswer")'(
            challenges, realmToAnswer) {
        given:
            def username = UUID.randomUUID().toString()
            def password = UUID.randomUUID().toString()
            String credentials = Credentials.basic username, password

        and:
            def defaultAuthenticator = Authenticator.theAuthenticator
            Authenticator.default = new Authenticator() {
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
            def responseBuilder = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
            challenges.each { responseBuilder.addHeader HttpHeaderNames.PROXY_AUTHENTICATE as String, it }
            def response = responseBuilder.build()

        when:
            def returnedRequest = defaultProxyAuthenticator.authenticate route, response

        then:
            returnedRequest
            returnedRequest.header(HttpHeaderNames.PROXY_AUTHORIZATION as String) == credentials

        cleanup:
            Authenticator.default = defaultAuthenticator

        where:
            challenges                                   | realmToAnswer
            [' ,  , Basic realm=foo']                    | 'foo'
            ['Basic realm=foo']                          | 'foo'
            ['Basic realm="foo"']                        | 'foo'
            ['Basic realm = "foo"']                      | 'foo'
            ['Basic realm = "foo",Digest']               | 'foo'
            ['Basic realm = "foo",Basic realm=bar']      | 'foo'
            ['Basic realm = "foo",Basic realm=bar']      | 'bar'
            ['Digest,Basic realm="foo"']                 | 'foo'
            ['Digest,Basic ,,realm="foo"']               | 'foo'
            ['Digest, Basic realm="foo"']                | 'foo'
            ['Digest, Basic ,,realm="foo"']              | 'foo'
            ['Digest,,,, Basic ,,realm="foo"']           | 'foo'
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']   | 'foo'
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | 'f,oo'
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"'] | 'f,oo'
            ['Digest,,,, Basic ,,,realm="f\\\\\\"o\\o"'] | 'f\\"oo'
            ['Digest', 'Basic realm=foo']                | 'foo'
            ['Basic realm=foo', 'Digest']                | 'foo'
            ['Basic realm=foo', 'Basic realm=bar']       | 'foo'
            ['Basic realm=foo', 'Basic realm=bar']       | 'bar'
    }

}
