package org.javacord.test

import org.mockserver.configuration.ConfigurationProperties
import org.mockserver.integration.ClientAndServer
import org.spockframework.runtime.extension.AbstractGlobalExtension
import org.spockframework.runtime.model.SpecInfo

class MockProxyManager extends AbstractGlobalExtension {

    private static final Object MOCK_PROXY_LOCK = new Object()

    private static final Object HTTP_PROXY_LOCK = new Object()

    private static final Object SOCKS_PROXY_LOCK = new Object()

    private static volatile ClientAndServer mockProxyField

    private static volatile Proxy httpProxyField

    private static volatile Proxy socksProxyField

    static final ProxySelector proxySelector = new ProxySelector() {
        @Override
        List<Proxy> select(URI uri) {
            [httpProxy]
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

    static getHttpProxy() {
        Proxy httpProxy = httpProxyField
        if (httpProxy == null) {
            synchronized (HTTP_PROXY_LOCK) {
                httpProxy = httpProxyField
                if (httpProxy == null) {
                    httpProxyField = httpProxy = new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(InetAddress.getLoopbackAddress(), mockProxy.localPort))
                }
            }
        }
        httpProxy
    }

    static getSocksProxy() {
        Proxy socksProxy = socksProxyField
        if (socksProxy == null) {
            synchronized (SOCKS_PROXY_LOCK) {
                socksProxy = socksProxyField
                if (socksProxy == null) {
                    socksProxyField = socksProxy = new Proxy(Proxy.Type.SOCKS,
                            new InetSocketAddress(InetAddress.getLoopbackAddress(), mockProxy.localPort))
                }
            }
        }
        socksProxy
    }

    static setHttpSystemProperties() {
        System.properties.'https.proxyHost' = InetAddress.getLoopbackAddress().hostAddress
        System.properties.'https.proxyPort' = mockProxy.localPort as String
    }

    private static setCommonSocksSystemProperties() {
        System.properties.socksProxyHost = InetAddress.getLoopbackAddress().hostAddress
        System.properties.socksProxyPort = mockProxy.localPort as String
    }

    static setSocks4SystemProperties() {
        setCommonSocksSystemProperties()
        System.properties.socksProxyVersion = '4'
        // require authentication to prevent accidental usage of SOCKS5 when SOCKS4 should be used
        ConfigurationProperties.socksProxyServerUsername UUID.randomUUID().toString()
        ConfigurationProperties.socksProxyServerPassword UUID.randomUUID().toString()
    }

    static setSocks5SystemProperties(username, password) {
        setSocks5SystemProperties()
        ConfigurationProperties.socksProxyServerUsername username
        ConfigurationProperties.socksProxyServerPassword password
    }

    static setSocks5SystemProperties() {
        setCommonSocksSystemProperties()
        System.properties.socksProxyVersion = '5'
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
