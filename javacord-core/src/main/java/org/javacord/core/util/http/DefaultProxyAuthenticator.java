package org.javacord.core.util.http;

import okhttp3.Authenticator;
import okhttp3.Challenge;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * This class bridges the system default {@link java.net.Authenticator} to the OkHttp proxy authenticator mechanism.
 */
public class DefaultProxyAuthenticator implements Authenticator {

    // regexes according to RFC 7235
    private static final String TOKEN_PATTERN_PART = "[!#$%&'*+.^_`|~\\p{Alnum}-]+";
    private static final String TOKEN68_PATTERN_PART = "[\\p{Alnum}._~+/-]+=*";
    private static final String OWS_PATTERN_PART = "[ \\t]*";
    private static final String QUOTED_PAIR_PATTERN_PART = "\\\\([\\t \\p{Graph}\\x80-\\xFF])";
    private static final String QUOTED_STRING_PATTERN_PART =
            "\"(?:[\\t \\x21\\x23-\\x5B\\x5D-\\x7E\\x80-\\xFF]|" + QUOTED_PAIR_PATTERN_PART + ")*\"";
    private static final String AUTH_PARAM_PATTERN_PART = TOKEN_PATTERN_PART + OWS_PATTERN_PART + '='
            + OWS_PATTERN_PART + "(?:" + TOKEN_PATTERN_PART + '|' + QUOTED_STRING_PATTERN_PART + ')';
    private static final String CHALLENGE_PATTERN_PART = TOKEN_PATTERN_PART + "(?: +(?:" + TOKEN68_PATTERN_PART
            + "|(?:,|" + AUTH_PARAM_PATTERN_PART + ")(?:" + OWS_PATTERN_PART
            + ",(?:" + OWS_PATTERN_PART + AUTH_PARAM_PATTERN_PART + ")?)*)?)?";
    private static final String PROXY_AUTHENTICATE_VALUE_SPLIT_PATTERN_PART =
            "(?:" + OWS_PATTERN_PART + ',' + OWS_PATTERN_PART + ")+";

    private static final Pattern PROXY_AUTHENTICATE_VALUE_PATTERN = Pattern.compile("^(?:," + OWS_PATTERN_PART + ")*"
            + CHALLENGE_PATTERN_PART
            + "(?:" + OWS_PATTERN_PART + ",(?:" + OWS_PATTERN_PART + CHALLENGE_PATTERN_PART + ")?)*$");
    private static final Pattern AUTH_SCHEME_PATTERN = Pattern.compile('^' + TOKEN_PATTERN_PART + '$');
    private static final Pattern AUTH_SCHEME_AND_TOKEN68_PATTERN =
            Pattern.compile('^' + TOKEN_PATTERN_PART + " +" + TOKEN68_PATTERN_PART + '$');
    private static final Pattern AUTH_SCHEME_AND_PARAM_PATTERN =
            Pattern.compile('^' + TOKEN_PATTERN_PART + " +" + AUTH_PARAM_PATTERN_PART + '$');
    private static final Pattern AUTH_PARAM_PATTERN = Pattern.compile('^' + AUTH_PARAM_PATTERN_PART + '$');
    private static final Pattern TOKEN_PATTERN = Pattern.compile('^' + TOKEN_PATTERN_PART + '$');
    private static final Pattern QUOTED_PAIR_PATTERN = Pattern.compile(QUOTED_PAIR_PATTERN_PART);

    private static final Pattern PROXY_AUTHENTICATE_VALUE_SPLIT_PATTERN =
            Pattern.compile(PROXY_AUTHENTICATE_VALUE_SPLIT_PATTERN_PART);
    private static final Pattern WHITESPACE_SPLIT_PATTERN = Pattern.compile(" +");
    private static final Pattern AUTH_PARAM_SPLIT_PATTERN = Pattern.compile(OWS_PATTERN_PART + '=' + OWS_PATTERN_PART);
    private static final Pattern QUOTED_STRING_AUTH_PARAM_AT_END_PATTERN = Pattern.compile(
            TOKEN_PATTERN_PART + OWS_PATTERN_PART + '=' + OWS_PATTERN_PART + QUOTED_STRING_PATTERN_PART + '$');

    private static final Map<String, List<Challenge>> BASIC_CHALLENGES_CACHE = new ConcurrentHashMap<>();

    @Override
    public Request authenticate(Route route, Response response) {
        Request request = response.request();
        HttpUrl requestUrl = request.url();
        InetSocketAddress proxyAddress = (InetSocketAddress) route.proxy().address();
        String host = proxyAddress.getHostString();
        InetAddress addr = proxyAddress.getAddress();
        int port = proxyAddress.getPort();
        String protocol = requestUrl.scheme();
        URL url = requestUrl.url();

        return getBasicChallenges(response).map(challenge -> {
            PasswordAuthentication passwordAuthentication = java.net.Authenticator.requestPasswordAuthentication(
                    host, addr, port, protocol, challenge.realm(), challenge.scheme(), url,
                    java.net.Authenticator.RequestorType.PROXY);
            if (passwordAuthentication != null) {
                return Credentials.basic(passwordAuthentication.getUserName(),
                        String.valueOf(passwordAuthentication.getPassword()), challenge.charset());
            }
            return null;
        })
                .filter(Objects::nonNull)
                .filter(credentials -> request.headers("Proxy-Authorization").stream().noneMatch(credentials::equals))
                .findAny()
                .map(credentials -> request.newBuilder().header("Proxy-Authorization", credentials).build())
                .orElse(null);
    }

    /**
     * Retrieve a stream of basic challenges from the {@code Proxy-Authenticate} HTTP headers of the given response.
     *
     * @param response The response to extract the challenges from.
     * @return A stream of {@code Challenge}s, extracted from the given response.
     */
    private Stream<Challenge> getBasicChallenges(Response response) {
        return response.headers("Proxy-Authenticate").stream()
                .flatMap(proxyAuthenticateValue -> BASIC_CHALLENGES_CACHE.computeIfAbsent(proxyAuthenticateValue, s -> {
                    if (!PROXY_AUTHENTICATE_VALUE_PATTERN.matcher(proxyAuthenticateValue).matches()) {
                        return Collections.emptyList();
                    }

                    List<Challenge> result = new ArrayList<>();

                    String[] challengeParts = PROXY_AUTHENTICATE_VALUE_SPLIT_PATTERN.split(proxyAuthenticateValue);
                    boolean authSchemeIsBasic = false;
                    String realm = null;
                    boolean charsetIsUtf8 = false;
                    for (int i = 0, j = challengeParts.length; i < j; i++) {
                        String challengePart = challengeParts[i];

                        // skip empty parts that can occur as first and last element
                        if (challengePart.isEmpty()) {
                            continue;
                        }

                        String newAuthScheme = null;
                        String authParam = null;
                        if (AUTH_SCHEME_PATTERN.matcher(challengePart).matches()) {
                            newAuthScheme = challengePart;
                        } else if (AUTH_SCHEME_AND_TOKEN68_PATTERN.matcher(challengePart).matches()) {
                            newAuthScheme = WHITESPACE_SPLIT_PATTERN.split(challengePart, 2)[0];
                        } else if (AUTH_SCHEME_AND_PARAM_PATTERN.matcher(challengePart).matches()) {
                            String[] authSchemeAndParam = WHITESPACE_SPLIT_PATTERN.split(challengePart, 2);
                            newAuthScheme = authSchemeAndParam[0];
                            authParam = authSchemeAndParam[1];
                        } else if (AUTH_PARAM_PATTERN.matcher(challengePart).matches()) {
                            authParam = challengePart;
                        } else {
                            // comma in quoted string part got split wrongly
                            StringBuilder patternBuilder = new StringBuilder();
                            patternBuilder.append('^').append(Pattern.quote(challengeParts[0]));
                            for (int i2 = 1; i2 < i; i2++) {
                                patternBuilder
                                        .append(PROXY_AUTHENTICATE_VALUE_SPLIT_PATTERN_PART)
                                        .append(Pattern.quote(challengeParts[i2]));
                            }
                            // if the algorithm has a flaw, the loop will crash with an ArrayIndexOutOfBoundsException
                            // if this happens, the algorithm or overall regex has to be fixed, not the array access
                            Matcher quotedStringAuthParamAtEndMatcher;
                            do {
                                patternBuilder
                                        .append(PROXY_AUTHENTICATE_VALUE_SPLIT_PATTERN_PART)
                                        .append(Pattern.quote(challengeParts[i++]));
                                Matcher matcher =
                                        Pattern.compile(patternBuilder.toString()).matcher(proxyAuthenticateValue);
                                if (!matcher.find()) {
                                    // if the algorithm is flawless, this must not happen
                                    throw new AssertionError();
                                }
                                quotedStringAuthParamAtEndMatcher =
                                        QUOTED_STRING_AUTH_PARAM_AT_END_PATTERN.matcher(matcher.group());
                            } while (!quotedStringAuthParamAtEndMatcher.find());
                            authParam = quotedStringAuthParamAtEndMatcher.group();
                        }

                        if (newAuthScheme != null) {
                            addBasicChallengeToResult(authSchemeIsBasic, realm, charsetIsUtf8, result);
                            authSchemeIsBasic = newAuthScheme.equalsIgnoreCase("Basic");
                            realm = null;
                            charsetIsUtf8 = false;
                        }

                        if (authParam != null) {
                            String[] authParamPair = AUTH_PARAM_SPLIT_PATTERN.split(authParam, 2);
                            String authParamValue = authParamPair[1];
                            if (!TOKEN_PATTERN.matcher(authParamValue).matches()) {
                                authParamValue = authParamValue.substring(1, authParamValue.length() - 1);
                                authParamValue = QUOTED_PAIR_PATTERN.matcher(authParamValue).replaceAll("$1");
                            }
                            switch (authParamPair[0].toLowerCase()) {
                                case "realm":
                                    realm = authParamValue;
                                    break;

                                case "charset":
                                    charsetIsUtf8 = authParamValue.equalsIgnoreCase("UTF-8");
                                    break;

                                default:
                                    // ignore unknown auth params
                                    break;
                            }
                        }
                    }
                    addBasicChallengeToResult(authSchemeIsBasic, realm, charsetIsUtf8, result);
                    return result;
                }).stream());
    }

    /**
     * Create a {@code Challenge} object from the given values and add it to the {@code result} list if the challenge
     * is a valid {@code Basic} auth challenge.
     *
     * @param authSchemeIsBasic Whether the auth scheme is {@code Basic}.
     * @param realm             The realm for the authentication. This is mandatory for {@code Basic} auth.
     * @param charsetUtf8       Whether the requested charset is {@code UTF-8}.
     * @param result            The list of challenges to eventually add the created challenge to.
     */
    private void addBasicChallengeToResult(
            boolean authSchemeIsBasic, String realm, boolean charsetUtf8, List<Challenge> result) {
        if (authSchemeIsBasic && (realm != null)) {
            Challenge challenge = new Challenge("Basic", realm);
            if (charsetUtf8) {
                challenge = challenge.withCharset(StandardCharsets.UTF_8);
            }
            result.add(challenge);
        }
    }

}
