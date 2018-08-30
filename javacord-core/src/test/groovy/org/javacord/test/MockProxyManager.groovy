package org.javacord.test

import org.mockserver.integration.ClientAndServer
import org.spockframework.runtime.extension.AbstractGlobalExtension
import org.spockframework.runtime.model.SpecInfo

class MockProxyManager extends AbstractGlobalExtension {

    private static final Object MOCK_PROXY_LOCK = new Object()

    private static final Object PROXY_LOCK = new Object()

    private static volatile ClientAndServer mockProxyField

    private static volatile Proxy proxyField

    static final ProxySelector proxySelector = new ProxySelector() {
        @Override
        List<Proxy> select(URI uri) {
            [proxy]
        }

        @Override
        void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        }
    }

    static getMockProxy() {
        ClientAndServer mockProxy = mockProxyField
        if (mockProxy == null) {
            synchronized (MOCK_PROXY_LOCK) {
                mockProxy = mockProxyField
                if (mockProxy == null) {
                    mockProxyField = mockProxy = ClientAndServer.startClientAndServer()
                }
            }
        }
        mockProxy
    }

    static getProxy() {
        Proxy proxy = proxyField
        if (proxy == null) {
            synchronized (PROXY_LOCK) {
                proxy = proxyField
                if (proxy == null) {
                    proxyField = proxy = new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(InetAddress.getLoopbackAddress(), mockProxy.localPort))
                }
            }
        }
        proxy
    }

    static setHttpSystemProperties() {
        System.properties.'https.proxyHost' = InetAddress.getLoopbackAddress().hostAddress
        System.properties.'https.proxyPort' = mockProxy.localPort as String
    }

    @Override
    void visitSpec(SpecInfo spec) {
        spec.allFeatures*.addIterationInterceptor {
            mockProxyField?.reset()
            it.proceed()
        }
    }

    @Override
    void stop() {
        mockProxyField?.close()
    }

}
