package org.javacord.api.util.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * This class represents an RFC 7235 authentication challenge.
 */
public final class Challenge {

    /**
     * The authentication scheme in lowercase.
     */
    private final String scheme;

    /**
     * The auth params with lowercase keys.
     */
    private final Map<String, String> authParams;

    /**
     * Creates a new challenge.
     *
     * @param scheme     The authentication scheme for this challenge.
     * @param authParams The auth params for this challenge.
     */
    public Challenge(String scheme, Map<String, String> authParams) {
        this.scheme = scheme.toLowerCase(Locale.US);
        Map<String, String> newAuthParams = new HashMap<>();
        for (Entry<String, String> authParam : authParams.entrySet()) {
            String key = (authParam.getKey() == null) ? null : authParam.getKey().toLowerCase(Locale.US);
            newAuthParams.put(key, authParam.getValue());
        }
        this.authParams = Collections.unmodifiableMap(newAuthParams);
    }

    /**
     * Gets the authentication scheme in lowercase, like {@code basic}.
     *
     * @return The authentication scheme.
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Gets the auth params, including {@code realm} if present, but as {@code String}s.
     * The keys are all lowercase, as the auth param names are to be treated case insensitively anyway.
     * If a challenge uses the {@code token68} form instead of the auth params form, there is exactly one entry in the
     * result where the key is {@code null} and the the value is the token.
     *
     * @return The auth params.
     */
    public Map<String, String> getAuthParams() {
        return authParams;
    }

    /**
     * Returns the protection space.
     *
     * @return The protection space.
     */
    public Optional<String> getRealm() {
        return Optional.ofNullable(authParams.get("realm"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        Challenge that = (Challenge) o;
        return Objects.equals(scheme, that.scheme)
                && Objects.equals(authParams, that.authParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheme, authParams);
    }

    @Override
    public String toString() {
        return String.format("Challenge (scheme: %s, authParams: %s)", scheme, authParams);
    }

}
