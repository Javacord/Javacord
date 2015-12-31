package de.btobastian.javacord.api;

/**
 * An enum with message decorations.
 */
public enum MessageDecoration {
    
    ITALICS("*"),
    BOLD("**"),
    BOLD_ITALICS("***"),
    STRIKEOUT("~~"),
    CODE_SIMPLE("`"),
    CODE_LONG("```"),
    UNDERLINE("__"),
    UNDERLINE_ITALICS("__*"),
    UNDERLINE_BOLD("__**"),
    UNDERLINE_BOLD_ITALICS("__***");
    
    private String prefix;
    private String suffix;
    
    private MessageDecoration(String prefix) {
        this.prefix = prefix;
        this.suffix = new StringBuilder(prefix).reverse().toString();
    }

    protected String getPrefix() {
        return prefix;
    }

    protected String getSuffix() {
        return suffix;
    }

}
