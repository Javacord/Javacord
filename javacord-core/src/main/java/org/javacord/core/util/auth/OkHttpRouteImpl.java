package org.javacord.core.util.auth;

import org.javacord.api.util.auth.Route;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Objects;

/**
 * The implementation of {@link Route} for OkHttp.
 */
public class OkHttpRouteImpl implements Route {

    /**
     * The real route from OkHttp.
     */
    private final okhttp3.Route route;

    /**
     * Creates a new OkHttpRouteImpl.
     *
     * @param route The real route from OkHttp.
     */
    public OkHttpRouteImpl(okhttp3.Route route) {
        this.route = route;
    }

    @Override
    public URL getUrl() {
        return route.address().url().url();
    }

    @Override
    public Proxy getProxy() {
        return route.proxy();
    }

    @Override
    public InetSocketAddress getInetSocketAddress() {
        return route.socketAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        OkHttpRouteImpl that = (OkHttpRouteImpl) o;
        return Objects.equals(getUrl(), that.getUrl())
                && Objects.equals(getProxy(), that.getProxy())
                && Objects.equals(getInetSocketAddress(), that.getInetSocketAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getProxy(), getInetSocketAddress());
    }

    @Override
    public String toString() {
        return String.format(
                "Route (url: %s, proxy: %s, inetSocketAddress: %s)", getUrl(), getProxy(), getInetSocketAddress());
    }

}
