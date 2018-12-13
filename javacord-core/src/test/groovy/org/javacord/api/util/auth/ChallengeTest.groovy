package org.javacord.api.util.auth

import spock.lang.Specification
import spock.lang.Subject

@Subject(Challenge)
class ChallengeTest extends Specification {

    def 'scheme is always lowercase'() {
        when:
            def challenge = new Challenge('Basic', [:])

        then:
            challenge.scheme == 'basic'
    }

    def 'auth param keys are always lowercase'() {
        when:
            def challenge = new Challenge('Basic', [Realm: 'foo'])

        then:
            challenge.authParams == [realm: 'foo']
    }


    def 'realm is recognized from auth params'() {
        when:
            def challenge = new Challenge('Basic', [Realm: 'foo'])

        then:
            challenge.realm.orElse(null) == 'foo'
    }

    def 'auth params are immutable'() {
        given:
            def challenge = new Challenge('Basic', [Realm: 'foo'])

        when:
            challenge.authParams.bar = 'baz'

        then:
            thrown UnsupportedOperationException
    }

    def 'auth params are defensively copied'() {
        given:
            def authParams = [Realm: 'foo']
            def challenge = new Challenge('Basic', authParams)

        when:
            authParams.bar = 'baz'

        then:
            authParams == [Realm: 'foo', bar: 'baz']
            challenge.authParams == [realm: 'foo']
    }

}
