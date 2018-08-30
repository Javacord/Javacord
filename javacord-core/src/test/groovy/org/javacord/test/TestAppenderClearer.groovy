package org.javacord.test

import org.apache.logging.log4j.test.appender.ListAppender
import org.spockframework.runtime.extension.AbstractGlobalExtension
import org.spockframework.runtime.model.SpecInfo

class TestAppenderClearer extends AbstractGlobalExtension {

    @Override
    void visitSpec(SpecInfo spec) {
        spec.allFeatures*.addIterationInterceptor {
            ListAppender.getListAppender('Test Appender').clear()
            it.proceed()
        }
    }

}
