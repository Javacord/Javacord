package org.javacord.core.util.crypto;

import org.javacord.api.util.crypto.EncryptedAudioFrame;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implements audio encryption using AEAD-AES-256-GCM with RTP size.
 */
public class Aes256GcmRtpSizeAudioEncrypter extends AeadAudioEncrypter {
    private static final int TAG_LENGTH_BITS = 128;

    /**
     * Creates an encrypter for the XChaCha20Poly1305-RTPSIZE mode provided a nonce length.
     * @param nonceLength in bytes
     */
    public Aes256GcmRtpSizeAudioEncrypter(int nonceLength) {
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
    public EncryptedAudioFrame seal(byte[] key, byte[] audioFrame) throws RuntimeException {
        byte[] header = this.nextHeader();
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

        return new EncryptedAudioFrame(header, this.getCombined());
    }
}