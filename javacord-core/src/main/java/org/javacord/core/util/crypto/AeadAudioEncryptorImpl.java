package org.javacord.core.util.crypto;

import org.javacord.api.util.crypto.AudioEncryptor;

import java.nio.ByteBuffer;

/**
 * Base implementation for any given AEAD encryption.
 */
public abstract class AeadAudioEncryptorImpl implements AudioEncryptor {
    private final int nonceLength;

    protected byte[] encrypted = new byte[0];
    protected int nonceCounter = Integer.MIN_VALUE; // would take 2.7 years to overflow given 20ms intervals

    /**
     * Generic AEAD encryptor.
     * @param nonceLength in bytes
     */
    public AeadAudioEncryptorImpl(int nonceLength) {
        this.nonceLength = nonceLength;
    }

    /**
     * Generates the next nonce for encryption.
     *
     * <p>Creates a nonce by combining an incrementing counter with zero padding
     * to reach the required nonce length. The counter value is stored in the first
     * 4 bytes of the nonce.
     *
     * @return byte[] containing the generated nonce
     */
    protected byte[] nextNonce() {
        int counter = this.nonceCounter++;
        ByteBuffer nonceBuffer = ByteBuffer.allocate(nonceLength);
        nonceBuffer.putInt(counter);
        nonceBuffer.put(new byte[nonceLength - 4]);
        return nonceBuffer.array();
    }

    /**
     * Combines the encrypted audio data with the nonce counter.
     *
     * <p>Appends the nonce counter used for encryption to the end of the encrypted data.
     *
     * @return byte[] containing the encrypted data followed by the nonce counter
     */
    protected byte[] getCombined() {
        ByteBuffer combined = ByteBuffer.allocate(this.encrypted.length + 4);
        combined.put(this.encrypted);
        combined.putInt(this.nonceCounter - 1);
        return combined.array();
    }
}
