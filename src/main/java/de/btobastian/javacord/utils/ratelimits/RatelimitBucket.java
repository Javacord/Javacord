package de.btobastian.javacord.utils.ratelimits;

import de.btobastian.javacord.utils.rest.RestEndpoint;

import java.util.Optional;

public class RatelimitBucket {

    private final RestEndpoint endpoint;
    private final String majorUrlParameter;

    private int rateLimitResetTimestamp = 0;
    private int rateLimitRemaining = 1;

    private boolean hasActiveScheduler = false;

    public RatelimitBucket(RestEndpoint endpoint) {
        this(endpoint, null);
    }

    public RatelimitBucket(RestEndpoint endpoint, String majorUrlParameter) {
        if (endpoint.isGlobal()) {
            endpoint = null;
        }
        this.endpoint = endpoint;
        this.majorUrlParameter = majorUrlParameter;
    }

    /**
     * Checks if a bucket created with the given parameters would equal this bucket.
     *
     * @param endpoint The endpoint.
     * @param majorUrlParameter The major url parameter.
     * @return Whether a bucket created with the given parameters would equal this bucket or not.
     */
    public boolean equals(RestEndpoint endpoint, String majorUrlParameter) {
        if (endpoint.isGlobal()) {
            endpoint = null;
        }
        boolean endpointSame = this.endpoint == endpoint;
        boolean majorUrlParameterBothNull = this.majorUrlParameter == null && majorUrlParameter == null;
        boolean majorUrlParameterEqual = this.majorUrlParameter != null && this.majorUrlParameter.equals(majorUrlParameter);

        return endpointSame && (majorUrlParameterBothNull || majorUrlParameterEqual);
    }

    /**
     * Gets the rest endpoint of the bucket.
     *
     * @return The endpoint of the bucket. If it's a global limit, the endpoint will be not be present.
     */
    public Optional<RestEndpoint> getEndpoint() {
        return Optional.ofNullable(endpoint);
    }

    /**
     * Checks if this bucket has an active scheduler.
     *
     * @return Whether this bucket has an active scheduler or not.
     */
    public synchronized boolean hasActiveScheduler() {
        return hasActiveScheduler;
    }

    /**
     * Sets if this bucket has an active scheduler.
     *
     * @param hasActiveScheduler Whether this bucket has an active scheduler or not.
     */
    public synchronized void setHasActiveScheduler(boolean hasActiveScheduler) {
        this.hasActiveScheduler = hasActiveScheduler;
    }

    /**
     * Checks if there is still "space" in this bucket, which means that you can still send requests without being
     * ratelimited.
     *
     * @return Whether you can send requests without being ratelimited or not.
     */
    public boolean hasSpace() {
        if (rateLimitResetTimestamp < System.currentTimeMillis() / 1000) {
            return true;
        }
        return rateLimitRemaining > 0;
    }

    /**
     * Sets the remaining requests till ratelimit.
     *
     * @param rateLimitRemaining The remaining requests till ratelimit.
     */
    public void setRateLimitRemaining(int rateLimitRemaining) {
        this.rateLimitRemaining = rateLimitRemaining;
    }

    /**
     * Sets the ratelimit reset timestamp.
     *
     * @param rateLimitResetTimestamp The rateLimit reset timestamp.
     */
    public void setRateLimitResetTimestamp(int rateLimitResetTimestamp) {
        this.rateLimitResetTimestamp = rateLimitResetTimestamp;
    }

    /**
     * Gets the time in seconds how long you have to wait till there's space in the bucket again.
     *
     * @return The time in seconds how long you have to wait till there's space in the bucket again.
     */
    public int getTimeTillSpaceGetsAvailable() {
        if (rateLimitRemaining > 0) {
            return 0;
        }
        return rateLimitResetTimestamp - ((int) (System.currentTimeMillis() / 1000));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RatelimitBucket)) {
            return false;
        }
        RatelimitBucket otherBucket = (RatelimitBucket) obj;
        return equals(otherBucket.endpoint, otherBucket.majorUrlParameter);
    }

    @Override
    public int hashCode() {
        int hash = 42;
        int urlParamHash = majorUrlParameter == null ? 0 : majorUrlParameter.hashCode();
        int endpointHash = endpoint == null ? 0 : endpoint.hashCode();

        hash = hash * 11 + urlParamHash;
        hash = hash * 17 + endpointHash;
        return hash;
    }

    @Override
    public String toString() {
        String str = "Endpoint: " + (endpoint == null ? "global" : endpoint.getEndpointUrl());
        str += ", Major url parameter:" + (majorUrlParameter == null ? "none" : majorUrlParameter);
        return str;
    }
}
