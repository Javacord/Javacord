package org.javacord.api;

/**
 * This enum contains all different account types.
 */
public enum AccountType {

    /**
     * A client account.
     * Please notice, that public client bots are not allowed by Discord!
     */
    CLIENT(""),

    /**
     * A bot account.
     */
    BOT("Bot ");

    private final String tokenPrefix;

    /**
     * Class constructor.
     *
     * @param tokenPrefix The prefix, which is added in front of the normal token.
     */
    AccountType(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    /**
     * Gets the prefix, which is added in front of the normal token.
     *
     * @return The prefix, which is added in front of the normal token.
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

}
