package org.javacord.test

import spock.lang.Specification

class Slf4jBindingTest extends Specification {

    def 'ensure log4j is used as slf4j binding'() {
        given:
            Class staticLoggerBinder = Class.forName('org.slf4j.impl.StaticLoggerBinder')

        expect:
            staticLoggerBinder.singleton.loggerFactoryClassStr == 'org.apache.logging.slf4j.Log4jLoggerFactory'
    }

}
