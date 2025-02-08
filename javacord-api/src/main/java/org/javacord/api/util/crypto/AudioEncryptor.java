package org.javacord.api.util.crypto;

/**
 * A base class to define the minimum requirements for an encrypted audio frame.
 */
public interface AudioEncryptor {
    /**
     * Encrypts an audio frame using the provided encryption key.
     *
     * <p>The encryption mode depends on the implementing class.
     *
     * @param header byte[] containing the RTP formatted header
     * @param key The encryption key used to encrypt the audio frame
     * @param audioFrame The raw audio frame data to be encrypted
     * @return byte[] containing the encrypted audio data
     */
    byte[] seal(byte[] header, byte[] key, byte[] audioFrame);
}
