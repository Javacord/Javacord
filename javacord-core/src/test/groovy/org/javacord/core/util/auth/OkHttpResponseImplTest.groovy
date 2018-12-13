package org.javacord.core.util.auth

import io.netty.handler.codec.http.HttpHeaderNames
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.javacord.api.util.auth.Challenge
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Subject(OkHttpResponseImpl)
class OkHttpResponseImplTest extends Specification {

    @Unroll
    def 'parsing all challenges works correctly (challenges: #challenges)'() {
        given:
            def responseBuilder = new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
            challenges.each { responseBuilder.addHeader HttpHeaderNames.PROXY_AUTHENTICATE as String, it }
            def response = new OkHttpResponseImpl(responseBuilder.build())

        expect:
            response.challenges.collect() == expectedChallenges

        where:
            challenges                                    | expectedChallenges
            [' ,  , Basic realm=foo']                     | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm=foo']                           | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm="foo"']                         | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm = "foo"']                       | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm = "foo",Digest']                | [new Challenge('Basic', [realm: 'foo']),
                                                             new Challenge('Digest', [:])]
            ['Basic realm = "foo",Basic realm=bar']       | [new Challenge('Basic', [realm: 'foo']),
                                                             new Challenge('Basic', [realm: 'bar'])]
            ['Digest,Basic realm="foo"']                  | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'foo'])]
            ['Digest,Basic ,,realm="foo"']                | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'foo'])]
            ['Digest, Basic realm="foo"']                 | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'foo'])]
            ['Digest, Basic ,,realm="foo"']               | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'foo'])]
            ['Digest,,,, Basic ,,realm="foo"']            | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'foo'])]
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']    | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'foo', foo: 'bar'])]
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']   | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'f,oo', foo: 'bar'])]
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'f,oo', foo: 'bar'])]
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo,"'] | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'f,oo,', foo: 'bar'])]
            [/Digest,,,, Basic ,,,realm="f\\\"o\o"/]      | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: /f\"oo/])]
            [/Digest,,,, Basic ,,,realm="f\\\\"o\o"/]     | []
            [/Digest,,,, Basic ,,,realm="f"o\o"/]         | []
            [/Digest,,,, Basic ,,,realm=f"oo/]            | []
            ['Digest', 'Basic realm=foo']                 | [new Challenge('Digest', [:]),
                                                             new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm=foo', 'Digest']                 | [new Challenge('Basic', [realm: 'foo']),
                                                             new Challenge('Digest', [:])]
            ['Basic realm=foo', 'Basic realm=bar']        | [new Challenge('Basic', [realm: 'foo']),
                                                             new Challenge('Basic', [realm: 'bar'])]
            ['Other abc==']                               | [new Challenge('Other', [(null): 'abc=='])]
            ['Other abc==, realm=foo']                    | []
            ['Other realm=foo, realm=bar']                | []
    }

    @Unroll
    def 'parsing challenges for specific scheme works correctly (challenges: #challenges, scheme: #scheme)'() {
        given:
            def responseBuilder = new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
            challenges.each { responseBuilder.addHeader HttpHeaderNames.PROXY_AUTHENTICATE as String, it }
            def response = new OkHttpResponseImpl(responseBuilder.build())

        expect:
            response.getChallenges(scheme).collect() == expectedChallenges

        where:
            challenges                                    | scheme   | expectedChallenges
            [' ,  , Basic realm=foo']                     | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            [' ,  , Basic realm=foo']                     | 'Other'  | []
            ['Basic realm=foo']                           | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm=foo']                           | 'Other'  | []
            ['Basic realm="foo"']                         | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm="foo"']                         | 'Other'  | []
            ['Basic realm = "foo"']                       | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm = "foo"']                       | 'Other'  | []
            ['Basic realm = "foo",Digest']                | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm = "foo",Digest']                | 'Digest' | [new Challenge('Digest', [:])]
            ['Basic realm = "foo",Digest']                | 'Other'  | []
            ['Basic realm = "foo",Basic realm=bar']       | 'Basic'  | [new Challenge('Basic', [realm: 'foo']),
                                                                        new Challenge('Basic', [realm: 'bar'])]
            ['Basic realm = "foo",Basic realm=bar']       | 'Other'  | []
            ['Digest,Basic realm="foo"']                  | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Digest,Basic realm="foo"']                  | 'Digest' | [new Challenge('Digest', [:])]
            ['Digest,Basic realm="foo"']                  | 'Other'  | []
            ['Digest,Basic ,,realm="foo"']                | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Digest,Basic ,,realm="foo"']                | 'Digest' | [new Challenge('Digest', [:])]
            ['Digest,Basic ,,realm="foo"']                | 'Other'  | []
            ['Digest, Basic realm="foo"']                 | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Digest, Basic realm="foo"']                 | 'Digest' | [new Challenge('Digest', [:])]
            ['Digest, Basic realm="foo"']                 | 'Other'  | []
            ['Digest, Basic ,,realm="foo"']               | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Digest, Basic ,,realm="foo"']               | 'Digest' | [new Challenge('Digest', [:])]
            ['Digest, Basic ,,realm="foo"']               | 'Other'  | []
            ['Digest,,,, Basic ,,realm="foo"']            | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Digest,,,, Basic ,,realm="foo"']            | 'Digest' | [new Challenge('Digest', [:])]
            ['Digest,,,, Basic ,,realm="foo"']            | 'Other'  | []
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']    | 'Basic'  | [
                    new Challenge('Basic', [realm: 'foo', foo: 'bar'])]
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']    | 'Digest' | [new Challenge('Digest', [:])]
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']    | 'Other'  | []
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']   | 'Basic'  | [
                    new Challenge('Basic', [realm: 'f,oo', foo: 'bar'])]
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']   | 'Digest' | [new Challenge('Digest', [:])]
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']   | 'Other'  | []
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | 'Basic'  | [
                    new Challenge('Basic', [realm: 'f,oo', foo: 'bar'])]
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | 'Digest' | [new Challenge('Digest', [:])]
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | 'Other'  | []
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo,"'] | 'Basic'  | [
                    new Challenge('Basic', [realm: 'f,oo,', foo: 'bar'])]
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo,"'] | 'Digest' | [new Challenge('Digest', [:])]
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo,"'] | 'Other'  | []
            [/Digest,,,, Basic ,,,realm="f\\\"o\o"/]      | 'Basic'  | [new Challenge('Basic', [realm: /f\"oo/])]
            [/Digest,,,, Basic ,,,realm="f\\\"o\o"/]      | 'Digest' | [new Challenge('Digest', [:])]
            [/Digest,,,, Basic ,,,realm="f\\\"o\o"/]      | 'Other'  | []
            [/Digest,,,, Basic ,,,realm="f\\\\"o\o"/]     | 'Basic'  | []
            [/Digest,,,, Basic ,,,realm="f\\\\"o\o"/]     | 'Digest' | []
            [/Digest,,,, Basic ,,,realm="f\\\\"o\o"/]     | 'Other'  | []
            [/Digest,,,, Basic ,,,realm="f"o\o"/]         | 'Basic'  | []
            [/Digest,,,, Basic ,,,realm="f"o\o"/]         | 'Digest' | []
            [/Digest,,,, Basic ,,,realm="f"o\o"/]         | 'Other'  | []
            [/Digest,,,, Basic ,,,realm=f"oo/]            | 'Basic'  | []
            [/Digest,,,, Basic ,,,realm=f"oo/]            | 'Digest' | []
            [/Digest,,,, Basic ,,,realm=f"oo/]            | 'Other'  | []
            ['Digest', 'Basic realm=foo']                 | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Digest', 'Basic realm=foo']                 | 'Digest' | [new Challenge('Digest', [:])]
            ['Digest', 'Basic realm=foo']                 | 'Other'  | []
            ['Basic realm=foo', 'Digest']                 | 'Basic'  | [new Challenge('Basic', [realm: 'foo'])]
            ['Basic realm=foo', 'Digest']                 | 'Digest' | [new Challenge('Digest', [:])]
            ['Basic realm=foo', 'Digest']                 | 'Other'  | []
            ['Basic realm=foo', 'Basic realm=bar']        | 'Basic'  | [new Challenge('Basic', [realm: 'foo']),
                                                                        new Challenge('Basic', [realm: 'bar'])]
            ['Basic realm=foo', 'Basic realm=bar']        | 'Other'  | []
            ['Other abc==']                               | 'Basic'  | []
            ['Other abc==']                               | 'Other'  | [new Challenge('Other', [(null): 'abc=='])]
            ['Other abc==, realm=foo']                    | 'Basic'  | []
            ['Other abc==, realm=foo']                    | 'Other'  | []
            ['Other realm=foo, realm=bar']                | 'Basic'  | []
            ['Other realm=foo, realm=bar']                | 'Other'  | []
    }

    @Unroll
    def 'the correct headers according to response code are parsed (responseCode: #responseCode)'() {
        given:
            def response = new OkHttpResponseImpl(new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(responseCode)
                    .message('')
                    .addHeader(HttpHeaderNames.WWW_AUTHENTICATE as String, 'www')
                    .addHeader(HttpHeaderNames.PROXY_AUTHENTICATE as String, 'proxy')
                    .build())

        expect:
            response.getChallenges().collect() == challenges

        where:
            responseCode                        | challenges
            HttpURLConnection.HTTP_OK           | []
            HttpURLConnection.HTTP_UNAUTHORIZED | [new Challenge('www', [:])]
            HttpURLConnection.HTTP_PROXY_AUTH   | [new Challenge('proxy', [:])]
    }

    @Unroll
    def 'parse results are reused (challenges: #challenges)'() {
        given:
            def responseBuilder = new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
            challenges.each { responseBuilder.addHeader HttpHeaderNames.PROXY_AUTHENTICATE as String, it }
            def firstResponse = new OkHttpResponseImpl(responseBuilder.build())

        and:
            responseBuilder = new Response.Builder()
                    .request(new Request.Builder().url('http://localhost').build())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HttpURLConnection.HTTP_PROXY_AUTH)
                    .message('')
            challenges.each { responseBuilder.addHeader HttpHeaderNames.PROXY_AUTHENTICATE as String, it }
            def secondResponse = new OkHttpResponseImpl(responseBuilder.build())

        when:
            def firstChallenges = firstResponse.challenges.toArray()
            def secondChallenges = secondResponse.challenges.toArray()

        then:
            (0..(firstChallenges.size() - 1))
                    .collectEntries { [firstChallenges[it], secondChallenges[it]] }
                    .every { first, second -> first.is second }

        where:
            challenges                                    | _
            [' ,  , Basic realm=foo']                     | _
            ['Basic realm=foo']                           | _
            ['Basic realm="foo"']                         | _
            ['Basic realm = "foo"']                       | _
            ['Basic realm = "foo",Digest']                | _
            ['Basic realm = "foo",Basic realm=bar']       | _
            ['Digest,Basic realm="foo"']                  | _
            ['Digest,Basic ,,realm="foo"']                | _
            ['Digest, Basic realm="foo"']                 | _
            ['Digest, Basic ,,realm="foo"']               | _
            ['Digest,,,, Basic ,,realm="foo"']            | _
            ['Digest,,,, Basic ,,foo=bar,realm="foo"']    | _
            ['Digest,,,, Basic ,,foo=bar,realm="f,oo"']   | _
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo"']  | _
            [',Digest,,,, Basic ,,foo=bar,realm="f,oo,"'] | _
            [/Digest,,,, Basic ,,,realm="f\\\"o\o"/]      | _
            ['Digest', 'Basic realm=foo']                 | _
            ['Basic realm=foo', 'Digest']                 | _
            ['Basic realm=foo', 'Basic realm=bar']        | _
            ['Other abc==']                               | _
    }

}
