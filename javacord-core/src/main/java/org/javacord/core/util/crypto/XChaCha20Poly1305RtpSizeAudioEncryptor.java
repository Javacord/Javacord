package org.javacord.core.util.crypto;

import com.google.crypto.tink.aead.internal.InsecureNonceXChaCha20Poly1305;

/**
 * Implements audio encryption using AEAD-XChaCha20-Poly1305 with RTP size.
 */
public class XChaCha20Poly1305RtpSizeAudioEncryptor extends AeadAudioEncryptorImpl {

    /**
     * Creates an encryptor for the XChaCha20Poly1305-RTPSIZE mode provided a nonce length.
     * @param nonceLength in bytes
     */
    public XChaCha20Poly1305RtpSizeAudioEncryptor(int nonceLength) {
        super(nonceLength);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Encrypts the audio frame using AEAD-XChaCha20-Poly1305 with the following process:
     * <ul>
     *     <li>Generates an RTP header and nonce</li>
     *     <li>Initializes XChaCha20-Poly1305 cipher with the provided key</li>
     *     <li>Uses the RTP header as Additional Authenticated Data</li>
     * </ul>
     *
     * @throws RuntimeException if encryption fails due to encryption failure.
     */
    @Override
    public byte[] seal(byte[] header, byte[] key, byte[] audioFrame) throws RuntimeException {
        byte[] nonce = this.nextNonce();

        try {
            InsecureNonceXChaCha20Poly1305 cipher = new InsecureNonceXChaCha20Poly1305(key);
            encrypted = cipher.encrypt(nonce, audioFrame, header);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt audio frame: ", e);
        }

        return this.getCombined();
    }
}
