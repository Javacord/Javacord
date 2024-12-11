package org.javacord.core.util.crypto;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.util.crypto.AudioEncrypter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the supported audio encryption modes.
 *
 * <p>Provides functionality to select and create appropriate encrypters based on
 * Discord's supported encryption modes found in the <a href="https://discord.com/developers/docs/topics/voice-connections#transport-encryption-modes">discord documentation</a>.
 */
public enum AudioEncryptionMode {
    AEAD_AES256_GCM_RTPSIZE("aead_aes256_gcm_rtpsize"),
    AEAD_XCHACHA20_POLY1305_RTPSIZE("aead_xchacha20_poly1305_rtpsize"),
    XSALSA20_POLY1305("xsalsa20_poly1305");

    private final String mode;

    private static final List<AudioEncryptionMode> priorityOrder = List.of(
            AEAD_AES256_GCM_RTPSIZE,
            AEAD_XCHACHA20_POLY1305_RTPSIZE,
            XSALSA20_POLY1305
    );

    AudioEncryptionMode(String mode) {
        this.mode = mode;
    }

    /**
     * Picks the "best" mode based on order found in the <a href="https://discord.com/developers/docs/topics/voice-connections#transport-encryption-modes">discord documentation</a>.
     * @param modes provided directly from the socket data
     * @return Optional AudioEncryptionMode
     */
    public static Optional<AudioEncryptionMode> getBestMode(JsonNode modes) {
        List<String> strModes = new ArrayList<>();

        for (JsonNode child : modes) {
            strModes.add(child.asText());
        }

        return priorityOrder.stream()
                .filter(mode -> strModes.contains(mode.toString()))
                .findFirst();
    }

    /**
     * The String identifier that discord uses for the encryption mode.
     *
     * @return String identifier for the mode.
     */
    @Override
    public String toString() {
        return this.mode;
    }

    /**
     * Creates a new {@link AudioEncrypter} instance for this encryption mode.
     *
     * @return Optional AudioEncrypter containing the configured audio encrypter,
     */
    public Optional<AudioEncrypter> getAudioEncrypter() {
        switch (this) {
            case XSALSA20_POLY1305:
                return Optional.of(new XSalsa20Poly1305AudioEncrypter());
            case AEAD_AES256_GCM_RTPSIZE:
                return Optional.of(new Aes256GcmRtpSizeAudioEncrypter(12));
            case AEAD_XCHACHA20_POLY1305_RTPSIZE:
                return Optional.of(new XChaCha20Poly1305RtpSizeAudioEncrypter(24));
            default:
                return Optional.empty();
        }
    }
}
