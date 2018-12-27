package org.javacord.core.util.ratelimit

import org.apache.logging.log4j.test.appender.ListAppender
import org.javacord.api.exception.DiscordException
import org.javacord.core.DiscordApiImpl
import org.javacord.core.util.concurrent.ThreadPoolImpl
import org.javacord.core.util.rest.RestRequest
import spock.lang.Specification
import spock.lang.Subject

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import java.util.concurrent.TimeUnit

@Subject(RatelimitManager)
class RatelimitManagerTest extends Specification {

    def 'executeBlocking() throwing a DiscordException without result does not cause an Exception'() {
        given:
            RestRequest request = Stub {
                executeBlocking() >> { throw new DiscordException(null, null, null, null) }
                getResult() >> new CompletableFuture<>()
            }
            def threadPool = new ThreadPoolImpl()
            DiscordApiImpl api = Stub {
                getThreadPool() >> threadPool
                getTimeOffset() >> null
            }
            new RatelimitManager(api).queueRequest request

        when:
            request.result.join()

        then:
            CompletionException ce = thrown()
            ce.cause instanceof DiscordException
            !ce.cause.response.present

        and:
            // wait until the thread pool is shut down, so that the request processing
            // is finished and the message got logged if the exception happened
            threadPool.shutdown()
            threadPool.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS)
            !new ArrayList<>(ListAppender.getListAppender('Test Appender').events).any { it.thrown }

        cleanup:
            threadPool?.shutdown()
    }

}
