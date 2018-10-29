package org.javacord.core.util.auth;

import org.javacord.api.util.auth.Route;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * The implementation of {@link Route} for NV web socket.
 */
public class NvWebSocketRouteImpl implements Route {

    private final URL url;
    private final Proxy proxy;
    private final InetSocketAddress inetSocketAddress;

    /**
     * Creates a new NvWebSocketRouteImpl.
     *
     * @param url               The URL of the route.
     * @param proxy             The proxy that is used to connect to the URL.
     * @param inetSocketAddress The inet socket address to which the connection is done.
     */
    public NvWebSocketRouteImpl(URL url, Proxy proxy, InetSocketAddress inetSocketAddress) {
        this.url = url;
        this.proxy = proxy;
        this.inetSocketAddress = inetSocketAddress;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public Proxy getProxy() {
        return proxy;
    }

    @Override
    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

}
