package org.javacord.core.util.http;

import okhttp3.Credentials;
import org.javacord.api.util.auth.Authenticator;
import org.javacord.api.util.auth.Request;
import org.javacord.api.util.auth.Response;
import org.javacord.api.util.auth.Route;
import org.javacord.core.util.auth.OkHttpRequestImpl;
import org.javacord.core.util.auth.OkHttpResponseImpl;
import org.javacord.core.util.auth.OkHttpRouteImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class bridges the system default {@link java.net.Authenticator} for {@code Basic} auth and the
 * {@link Authenticator} for any auth to the OkHttp proxy authenticator mechanism.
 */
public class ProxyAuthenticator implements okhttp3.Authenticator {

    /**
     * The authenticator to delegate work to.
     */
    private final Authenticator authenticator;

    /**
     * Creates a new proxy authenticator that is capable of doing {@code Basic} auth with credentials from the system
     * default authenticator.
     */
    public ProxyAuthenticator() {
        this(null);
    }

    /**
     * Creates a new proxy authenticator that requests the authentication headers to set from the given authenticator.
     *
     * @param authenticator The authenticator to delegate work to.
     */
    public ProxyAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public okhttp3.Request authenticate(okhttp3.Route route, okhttp3.Response response) throws IOException {
        okhttp3.Request request = response.request();

        Map<String, List<String>> requestHeaders = authenticator == null
                ? systemDefaultAuthentication(
                        new OkHttpRouteImpl(route), new OkHttpRequestImpl(request), new OkHttpResponseImpl(response))
                : authenticator.authenticate(
                        new OkHttpRouteImpl(route), new OkHttpRequestImpl(request), new OkHttpResponseImpl(response));

        if ((requestHeaders == null) || requestHeaders.isEmpty()) {
            return null;
        }

        okhttp3.Request.Builder resultBuilder = request.newBuilder();
        requestHeaders.forEach((headerName, headerValues) -> {
            if (headerValues == null) {
                resultBuilder.removeHeader(headerName);
                return;
            }
            if (headerValues.isEmpty()) {
                return;
            }
            String firstHeaderValue = headerValues.get(0);
            if (firstHeaderValue == null) {
                resultBuilder.removeHeader(headerName);
            } else {
                resultBuilder.addHeader(headerName, firstHeaderValue);
            }
            headerValues.stream().skip(1).forEach(headerValue -> resultBuilder.addHeader(headerName, headerValue));
        });

        return resultBuilder.build();
    }

    /**
     * Generates a {@code Basic} auth header with credentials from the system default authenticator.
     *
     * @param route    The route to which a request is done that needs to be authenticated.
     * @param request  The originating request that led to the authentication attempt.
     * @param response The response that demands authentication.
     * @return The {@code Basic} auth header.
     */
    private Map<String, List<String>> systemDefaultAuthentication(Route route, Request request, Response response) {
        InetSocketAddress proxyAddress = (InetSocketAddress) route.getProxy().address();
        String host = proxyAddress.getHostString();
        InetAddress addr = proxyAddress.getAddress();
        int port = proxyAddress.getPort();
        URL url = route.getUrl();
        String protocol = url.getProtocol();

        return response.getChallenges("basic")
                .filter(challenge -> challenge.getRealm().isPresent())
                .filter(challenge -> {
                    String charset = challenge.getAuthParams().get("charset");
                    return charset == null || charset.equalsIgnoreCase("UTF-8");
                })
                .map(challenge -> {
                    String realm = challenge.getRealm().orElseThrow(AssertionError::new);
                    PasswordAuthentication passwordAuthentication =
                            java.net.Authenticator.requestPasswordAuthentication(host, addr, port, protocol,
                                    realm, challenge.getScheme(), url, java.net.Authenticator.RequestorType.PROXY);
                    if (passwordAuthentication != null) {
                        Charset charset = challenge.getAuthParams().containsKey("charset")
                                ? StandardCharsets.UTF_8
                                : StandardCharsets.ISO_8859_1;
                        return Credentials.basic(passwordAuthentication.getUserName(),
                                String.valueOf(passwordAuthentication.getPassword()), charset);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .filter(credentials ->
                        request.getHeaders("Proxy-Authorization").stream().noneMatch(credentials::equals))
                .findAny()
                .map(credentials -> Collections.singletonMap("Proxy-Authorization", Arrays.asList(null, credentials)))
                .orElse(null);
    }

}
