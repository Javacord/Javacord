package org.javacord.core.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implements audio encryption using AEAD-AES-256-GCM with RTP size.
 */
public class Aes256GcmRtpSizeAudioEncryptor extends AeadAudioEncryptorImpl {
    private static final int TAG_LENGTH_BITS = 128;

    /**
     * Creates an encryptor for the Aes256Gcm-RTPSIZE mode provided a nonce length.
     * @param nonceLength in bytes
     */
    public Aes256GcmRtpSizeAudioEncryptor(int nonceLength) {
        super(nonceLength);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Encrypts the audio frame using AEAD-AES-256-GCM with the following process:
     * <ul>
     *     <li>Generates an RTP header and nonce</li>
     *     <li>Initializes AES cipher in GCM mode</li>
     *     <li>Uses the RTP header as Additional Authenticated Data</li>
     *     <li>Encrypts the audio frame with the provided key</li>
     * </ul>
     *
     * @throws RuntimeException if encryption fails due to any encryption failure.
     */
    @Override
    public byte[] seal(byte[] header, byte[] key, byte[] audioFrame) throws RuntimeException {
        byte[] nonce = this.nextNonce();

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(key, "AES"),
                    new GCMParameterSpec(TAG_LENGTH_BITS, nonce));

            cipher.updateAAD(header);

            encrypted = cipher.doFinal(audioFrame);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt audio frame: ", e);
        }

        return this.getCombined();
    }
}