package org.javacord.api.util.auth;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * This class represents the response that demands authentication.
 */
public interface Response {

    // regexes according to RFC 7235
    String TOKEN_PATTERN_PART = "[!#$%&'*+.^_`|~\\p{Alnum}-]+";
    String TOKEN68_PATTERN_PART = "[\\p{Alnum}._~+/-]+=*";
    String OWS_PATTERN_PART = "[ \\t]*";
    String QUOTED_PAIR_PATTERN_PART = "\\\\([\\t \\p{Graph}\\x80-\\xFF])";
    String QUOTED_STRING_PATTERN_PART =
            "\"(?:[\\t \\x21\\x23-\\x5B\\x5D-\\x7E\\x80-\\xFF]|" + QUOTED_PAIR_PATTERN_PART + ")*\"";
    String AUTH_PARAM_PATTERN_PART = TOKEN_PATTERN_PART + OWS_PATTERN_PART + '='
            + OWS_PATTERN_PART + "(?:" + TOKEN_PATTERN_PART + '|' + QUOTED_STRING_PATTERN_PART + ')';
    String CHALLENGE_PATTERN_PART = TOKEN_PATTERN_PART + "(?: +(?:" + TOKEN68_PATTERN_PART
            + "|(?:,|" + AUTH_PARAM_PATTERN_PART + ")(?:" + OWS_PATTERN_PART
            + ",(?:" + OWS_PATTERN_PART + AUTH_PARAM_PATTERN_PART + ")?)*)?)?";
    String AUTHENTICATION_HEADER_VALUE_SPLIT_PATTERN_PART =
            "(?:" + OWS_PATTERN_PART + ',' + OWS_PATTERN_PART + ")+";

    Pattern AUTHENTICATION_HEADER_VALUE_PATTERN = Pattern.compile("^(?:," + OWS_PATTERN_PART + ")*"
            + CHALLENGE_PATTERN_PART
            + "(?:" + OWS_PATTERN_PART + ",(?:" + OWS_PATTERN_PART + CHALLENGE_PATTERN_PART + ")?)*$");
    Pattern AUTH_SCHEME_PATTERN = Pattern.compile('^' + TOKEN_PATTERN_PART + '$');
    Pattern AUTH_SCHEME_AND_TOKEN68_PATTERN =
            Pattern.compile('^' + TOKEN_PATTERN_PART + " +" + TOKEN68_PATTERN_PART + '$');
    Pattern AUTH_SCHEME_AND_PARAM_PATTERN =
            Pattern.compile('^' + TOKEN_PATTERN_PART + " +" + AUTH_PARAM_PATTERN_PART + '$');
    Pattern AUTH_PARAM_PATTERN = Pattern.compile('^' + AUTH_PARAM_PATTERN_PART + '$');
    Pattern TOKEN_PATTERN = Pattern.compile('^' + TOKEN_PATTERN_PART + '$');
    Pattern QUOTED_PAIR_PATTERN = Pattern.compile(QUOTED_PAIR_PATTERN_PART);

    Pattern AUTHENTICATION_HEADER_VALUE_SPLIT_PATTERN =
            Pattern.compile(AUTHENTICATION_HEADER_VALUE_SPLIT_PATTERN_PART);
    Pattern WHITESPACE_SPLIT_PATTERN = Pattern.compile(" +");
    Pattern AUTH_PARAM_SPLIT_PATTERN = Pattern.compile(OWS_PATTERN_PART + '=' + OWS_PATTERN_PART);
    Pattern QUOTED_STRING_AUTH_PARAM_AT_END_PATTERN = Pattern.compile(
            TOKEN_PATTERN_PART + OWS_PATTERN_PART + '=' + OWS_PATTERN_PART + QUOTED_STRING_PATTERN_PART + '$');

    Map<Map.Entry<String, String>, List<Challenge>> CHALLENGES_CACHE = new ConcurrentHashMap<>();

    /**
     * The response code.
     *
     * @return The response code.
     */
    int getCode();

    /**
     * The response message.
     *
     * @return The response message.
     */
    String getMessage();

    /**
     * The headers of the response.
     *
     * @return The headers of the response.
     */
    Map<String, List<String>> getHeaders();

    /**
     * The headers of the response with the given name.
     *
     * @param headerName The name of the header for which to return the values.
     * @return The headers of the response with the given name.
     */
    default List<String> getHeaders(String headerName) {
        return getHeaders().get(headerName);
    }

    /**
     * The body of the response.
     *
     * @return The body of the response.
     * @throws IOException If an IO error occurs.
     */
    default Optional<String> getBody() throws IOException {
        return Optional.empty();
    }

    /**
     * Gets the RFC 7235 authentication challenges of this response.
     *
     * @return The authentication challenges of this response.
     */
    default Stream<Challenge> getChallenges() {
        return getChallenges(null);
    }

    /**
     * Gets the RFC 7235 authentication challenges for the given scheme of this response.
     *
     * @param authenticationScheme The authentication scheme to get the challenges for.
     * @return The authentication challenges of this response for the given scheme.
     */
    default Stream<Challenge> getChallenges(String authenticationScheme) {
        String authenticationHeader;
        switch (getCode()) {
            case HttpURLConnection.HTTP_PROXY_AUTH:
                authenticationHeader = "Proxy-Authenticate";
                break;

            case HttpURLConnection.HTTP_UNAUTHORIZED:
                authenticationHeader = "WWW-Authenticate";
                break;

            default:
                return Stream.empty();
        }

        return getHeaders(authenticationHeader).stream()
                .flatMap(authenticationHeaderValue -> {
                    Map.Entry<String, String> cacheKey =
                            new AbstractMap.SimpleEntry<>(authenticationHeaderValue,
                                    authenticationScheme == null ? null : authenticationScheme.toLowerCase(Locale.US));
                    return CHALLENGES_CACHE.computeIfAbsent(cacheKey, s -> {
                        if (!AUTHENTICATION_HEADER_VALUE_PATTERN.matcher(authenticationHeaderValue).matches()) {
                            return Collections.emptyList();
                        }

                        // needed to properly abort if a header is invalid due to repeated auth param names
                        List<Challenge> result = new ArrayList<>();

                        String[] challengeParts =
                                AUTHENTICATION_HEADER_VALUE_SPLIT_PATTERN.split(authenticationHeaderValue);
                        String authScheme = null;
                        Map<String, String> authParams = new HashMap<>();
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
                                String[] authSchemeAndToken68 = WHITESPACE_SPLIT_PATTERN.split(challengePart, 2);
                                newAuthScheme = authSchemeAndToken68[0];
                                if (authParams.put(null, authSchemeAndToken68[1]) != null) {
                                    // if the regex is correct, this must not happen
                                    throw new AssertionError();
                                }
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
                                            .append(AUTHENTICATION_HEADER_VALUE_SPLIT_PATTERN_PART)
                                            .append(Pattern.quote(challengeParts[i2]));
                                }
                                // if the algorithm has a flaw,
                                // the loop will crash with an ArrayIndexOutOfBoundsException
                                // if this happens, the algorithm or overall regex has to be fixed, not the array access
                                Matcher quotedStringAuthParamAtEndMatcher;
                                do {
                                    patternBuilder
                                            .append(AUTHENTICATION_HEADER_VALUE_SPLIT_PATTERN_PART)
                                            .append(Pattern.quote(challengeParts[i++]));
                                    Matcher matcher = Pattern.compile(
                                            patternBuilder.toString()).matcher(authenticationHeaderValue);
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
                                if (authScheme != null) {
                                    if ((authenticationScheme == null)
                                            || authScheme.equalsIgnoreCase(authenticationScheme)) {
                                        result.add(new Challenge(authScheme, authParams));
                                    }
                                    authParams.clear();
                                }
                                authScheme = newAuthScheme;
                            }

                            if (authParam != null) {
                                String[] authParamPair = AUTH_PARAM_SPLIT_PATTERN.split(authParam, 2);
                                // lower-case to easily check for multiple occurrences
                                String authParamKey = authParamPair[0].toLowerCase(Locale.US);
                                String authParamValue = authParamPair[1];
                                if (!TOKEN_PATTERN.matcher(authParamValue).matches()) {
                                    authParamValue = authParamValue.substring(1, authParamValue.length() - 1);
                                    authParamValue = QUOTED_PAIR_PATTERN.matcher(authParamValue).replaceAll("$1");
                                }
                                if (authParams.put(authParamKey, authParamValue) != null) {
                                    // ignore invalid header value
                                    // auth param keys must not occur multiple times within one challenge
                                    return Collections.emptyList();
                                }
                            }
                        }
                        if ((authenticationScheme == null)
                                || ((authScheme != null) && authScheme.equalsIgnoreCase(authenticationScheme))) {
                            result.add(new Challenge(authScheme, authParams));
                        }
                        return result;
                    }).stream();
                });
    }

}
