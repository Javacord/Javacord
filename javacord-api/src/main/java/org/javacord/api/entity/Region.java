package org.javacord.api.entity;

/**
 * This enum represents a valid region for a server.
 */
public enum Region implements Nameable {

    // "Normal" regions
    AMSTERDAM("amsterdam", "Amsterdam", false),
    BRAZIL("brazil", "Brazil", false),
    EUROPE("europe", "Europe", false),
    EU_WEST("eu-west", "EU West", false),
    EU_CENTRAL("eu-central", "EU Central", false),
    FRANKFURT("frankfurt", "Frankfurt", false),
    HONG_KONG("hongkong", "Hong Kong", false),
    JAPAN("japan", "Japan", false),
    LONDON("london", "London", false),
    RUSSIA("russia", "Russia", false),
    INDIA("india", "India", false),
    SINGAPORE("singapore", "Singapore", false),
    SYDNEY("sydney", "Sydney", false),
    US_EAST("us-east", "US East", false),
    US_WEST("us-west", "US West", false),
    US_CENTRAL("us-central", "US Central", false),
    US_SOUTH("us-south", "US South", false),

    // Vip regions
    VIP_AMSTERDAM("vip-amsterdam", "Amsterdam (VIP)", true),
    VIP_BRAZIL("vip-brazil", "Brazil (VIP)", true),
    VIP_EU_WEST("vip-eu-west", "EU West (VIP)", true),
    VIP_EU_CENTRAL("vip-eu-central", "EU Central (VIP)", true),
    VIP_FRANKFURT("vip-frankfurt", "Frankfurt (VIP)", true),
    VIP_LONDON("vip-london", "London (VIP)", true),
    VIP_SINGAPORE("vip-singapore", "Singapore (VIP)", true),
    VIP_SYDNEY("vip-sydney", "Sydney (VIP)", true),
    VIP_US_EAST("vip-us-east", "US East (VIP)", true),
    VIP_US_WEST("vip-us-west", "US West (VIP)", true),
    VIP_US_CENTRAL("vip-us-central", "US Central (VIP)", true),
    VIP_US_SOUTH("vip-us-south", "US South (VIP)", true),

    /**
     * An unknown region, most likely because discord added new regions.
     */
    UNKNOWN("us-west", "Unknown", false);

    private final String key;
    private final String name;
    private final boolean vip;

    /**
     * Class constructor.
     *
     * @param key The key of the region.
     * @param name The name of the region.
     * @param vip If the region is for VIPs or not.
     */
    Region(String key, String name, boolean vip) {
        this.key = key;
        this.name = name;
        this.vip = vip;
    }

    /**
     * Gets the key of the region which is used for REST-request.
     *
     * @return The key of the region.
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the name of the region (which looks nicer than the key :-) ).
     *
     * @return The name of the region.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Checks if the region of is for VIPs only.
     *
     * @return Whether the region of is for VIPs only or not.
     */
    public boolean isVip() {
        return vip;
    }

    /**
     * Gets a region by its key.
     *
     * @param key The key of the region.
     * @return The region with the given key. {@link Region#UNKNOWN} if no region for the given key was found.
     */
    public static Region getRegionByKey(String key) {
        for (Region region : values()) {
            if (region.getKey().equalsIgnoreCase(key) && region != UNKNOWN) {
                return region;
            }
        }
        return UNKNOWN;
    }

}
