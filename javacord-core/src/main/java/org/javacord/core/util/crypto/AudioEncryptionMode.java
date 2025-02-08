package org.javacord.core.util.crypto;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.util.crypto.AudioEncryptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents the supported audio encryption modes.
 *
 * <p>Provides functionality to select and create appropriate encryptors based on
 * Discord's supported encryption modes found in the <a href="https://discord.com/developers/docs/topics/voice-connections#transport-encryption-modes">discord documentation</a>.
 */
public enum AudioEncryptionMode {
    AEAD_AES256_GCM_RTPSIZE("aead_aes256_gcm_rtpsize"),
    AEAD_XCHACHA20_POLY1305_RTPSIZE("aead_xchacha20_poly1305_rtpsize");

    private final String mode;

    private static final List<AudioEncryptionMode> priorityOrder = Arrays.asList(
            AEAD_AES256_GCM_RTPSIZE,
            AEAD_XCHACHA20_POLY1305_RTPSIZE
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
     * Creates a new {@link AudioEncryptor} instance for this encryption mode.
     *
     * @return Optional AudioEncryptor containing the configured audio encryptor,
     */
    public Optional<AudioEncryptor> getAudioEncryptor() {
        switch (this) {
            case AEAD_AES256_GCM_RTPSIZE:
                return Optional.of(new Aes256GcmRtpSizeAudioEncryptor(12));
            case AEAD_XCHACHA20_POLY1305_RTPSIZE:
                return Optional.of(new XChaCha20Poly1305RtpSizeAudioEncryptor(24));
            default:
                return Optional.empty();
        }
    }
}
