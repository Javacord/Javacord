package org.javacord.core.util.logging

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.test.appender.ListAppender
import spock.lang.Specification
import spock.lang.Subject

class PrivacyProtectionLoggerTest extends Specification {
    @Subject
    def privacyProtectionLogger = new PrivacyProtectionLogger(LogManager.logger)

    def 'logging with null private data does not throw exception'() {
        given:
            def random = UUID.randomUUID().toString()
            PrivacyProtectionLogger.addPrivateData(null)

        when:
            privacyProtectionLogger.fatal(random)

        then:
            noExceptionThrown()

        and:
            !new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
                    .any { it.thrown }

        and:
            new ArrayList<>(ListAppender.getListAppender('Test Appender').events)
                    .findAll { it.level == Level.FATAL }
                    .any { it.message.formattedMessage == random }

        cleanup:
            PrivacyProtectionLogger.privateDataSet.remove(null)
    }
}
