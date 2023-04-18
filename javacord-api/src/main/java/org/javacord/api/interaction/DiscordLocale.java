package org.javacord.api.interaction;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DiscordLocale {

    INDONESIAN("id"),
    DANISH("da"),
    GERMAN("de"),
    ENGLISH_UK("en-GB"),
    ENGLISH_US("en-US"),
    SPANISH("es-ES"),
    FRENCH("fr"),
    CROATIAN("hr"),
    ITALIAN("it"),
    LITHUANIAN("lt"),
    HUNGARIAN("hu"),
    DUTCH("nl"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE_BRAZILIAN("pt-BR"),
    ROMANIAN_ROMANIA("ro"),
    FINNISH("fi"),
    SWEDISH("sv-SE"),
    VIETNAMESE("vi"),
    TURKISH("tr"),
    CZECH("cs"),
    GREEK("el"),
    BULGARIAN("bg"),
    RUSSIAN("ru"),
    UKRAINIAN("uk"),
    HINDI("hi"),
    THAI("th"),
    CHINESE_CHINA("zh-CN"),
    JAPANESE("ja"),
    CHINESE_TAIWAN("zh-TW"),
    KOREAN("ko"),
    UNKNOWN("");

    /**
     * A map for retrieving the enum instances by code.
     */
    private static final Map<String, DiscordLocale> instanceByCode;

    static {
        instanceByCode = Collections.unmodifiableMap(
                Arrays.stream(values())
                        .collect(Collectors.toMap(DiscordLocale::getLocaleCode, Function.identity())));
    }

    private final String localeCode;

    /**
     * Constructs a new DiscordLocale.
     *
     * @param localeCode The short form locale code (e.g. en-US, fr).
     */
    DiscordLocale(String localeCode) {
        this.localeCode = localeCode;
    }

    /**
     * Gets the short form locale code (e.g. en-US, fr)
     * @return The locale code.
     */
    public String getLocaleCode() {
        return localeCode;
    }

    /**
     * Gets a DiscordLocale from its locale code.
     *
     * @param localeCode The locale code.
     * @return A DiscordLocale or UNKNOWN if this code is unknown.
     */
    public static DiscordLocale fromLocaleCode(String localeCode) {
        return instanceByCode.getOrDefault(localeCode, DiscordLocale.UNKNOWN);
    }

}
