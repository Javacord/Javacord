package org.javacord.api.util.auth;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * This class represents the route to which a request is done that needs to be authenticated.
 */
public interface Route {

    /**
     * The URL of the route.
     *
     * @return The URL of the route.
     */
    URL getUrl();

    /**
     * The proxy that is used to connect to the URL.
     *
     * @return The proxy that is used to connect to the URL.
     */
    Proxy getProxy();

    /**
     * The inet socket address to which the connection is done.
     *
     * @return The inet socket address to which the connection is done.
     */
    InetSocketAddress getInetSocketAddress();

}
