package org.javacord.core.util.rest

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import spock.lang.Specification
import spock.lang.Subject

@Subject(RestRequestResult)
class RestRequestResultTest extends Specification {

    def 'jsonBody is not null, but MissingNode if response body is empty'() {
        given:
            def request = new Request.Builder()
                    .url('http://localhost')
                    .build()
            def response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(0)
                    .message('')
                    .body(ResponseBody.create('', null))
                    .build()

        when:
            def result = new RestRequestResult(Stub(RestRequest), response)

        then:
            result.jsonBody != null
            result.jsonBody.isMissingNode()
    }

}
