package org.javacord.core.entity;

import org.javacord.api.entity.VanityUrlCode;

import java.net.MalformedURLException;
import java.net.URL;

public class VanityUrlCodeImpl implements VanityUrlCode {

    private final String code;

    /**
     * Creates a new VanityUrlCode.
     *
     * @param vanityUrlCode The vanity code
     */
    public VanityUrlCodeImpl(String vanityUrlCode) {
        code = vanityUrlCode;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public URL getUrl() {
        try {
            return new URL("https://discord.com/invite/" + code);
        } catch (MalformedURLException e) {
            throw new AssertionError("Unexpected malformed vanity url", e);
        }
    }

}
