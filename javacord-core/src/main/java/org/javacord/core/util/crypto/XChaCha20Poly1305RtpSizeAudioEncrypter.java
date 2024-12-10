package org.javacord.core.util.crypto;

import com.google.crypto.tink.aead.internal.InsecureNonceXChaCha20Poly1305;
import org.javacord.api.util.crypto.EncryptedAudioFrame;

/**
 * Implements audio encryption using AEAD-XChaCha20-Poly1305 with RTP size.
 */
public class XChaCha20Poly1305RtpSizeAudioEncrypter extends AeadAudioEncrypter {

    public XChaCha20Poly1305RtpSizeAudioEncrypter(int nonceLength) {
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
    public EncryptedAudioFrame seal(byte[] key, byte[] audioFrame) throws RuntimeException {
        byte[] header = this.nextHeader();
        byte[] nonce = this.nextNonce();

        try {
            InsecureNonceXChaCha20Poly1305 cipher = new InsecureNonceXChaCha20Poly1305(key);
            encrypted = cipher.encrypt(nonce, audioFrame, header);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt audio frame: ", e);
        }

        return new EncryptedAudioFrame(header, this.getCombined());
    }
}
