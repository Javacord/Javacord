package org.javacord.core.util.crypto;

import com.codahale.xsalsa20poly1305.SecretBox;
import org.javacord.api.util.crypto.EncryptedAudioFrame;

/**
 * Implements audio encryption using XSalsa20-Poly1305.
 */
public class XSalsa20Poly1305AudioEncrypter extends AudioEncrypterImpl {

    /**
     * {@inheritDoc}
     *
     * <p>Encrypts the audio frame using XSalsa20-Poly1305 with the following process:
     * <ul>
     *     <li>Generates an RTP header</li>
     *     <li>Creates a 24 byte nonce by padding the 12-byte RTP header</li>
     *     <li>Encrypts the audio frame using the provided key and derived nonce</li>
     * </ul>
     */
    @Override
    public EncryptedAudioFrame seal(byte[] key, byte[] audioFrame) {
        byte[] header = this.nextHeader();
        byte[] nonce = new byte[24];
        System.arraycopy(header, 0, nonce, 0, RTP_HEADER_LENGTH);

        return new EncryptedAudioFrame(header, new SecretBox(key).seal(nonce, audioFrame));
    }
}
