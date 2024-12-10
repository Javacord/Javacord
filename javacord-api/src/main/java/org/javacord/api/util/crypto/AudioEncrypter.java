package org.javacord.api.util.crypto;

/**
 * A base class to define the minimum requirements for an encrypted audio frame.
 */
public interface AudioEncrypter {
    /**
     * Sets the ssrc identifier for the RTP header.
     *
     * @param ssrc The 32 bit synchronization source identifier
     * @return The current AudioEncrypter instance for method chaining
     */
    AudioEncrypter setSsrc(int ssrc);

    /**
     * Sets the RTP timestamp for the audio packet.
     *
     * @param timestamp The RTP timestamp value
     * @return The current AudioEncrypter instance for method chaining
     */
    AudioEncrypter setTimestamp(int timestamp);

    /**
     * Sets the sequence number for the RTP header.
     *
     * @param sequence The 16 bit sequence number
     * @return The current AudioEncrypter instance for method chaining
     */
    AudioEncrypter setSequence(char sequence);

    /**
     * Encrypts an audio frame using the provided encryption key.
     *
     * <p>Creates an RTP header using the previously set srrc, timestamp, and sequence number.
     * The encryption method and output format depend on the implementing class.
     *
     * @param key The encryption key used to encrypt the audio frame
     * @param audioFrame The raw audio frame data to be encrypted
     * @return An {@link EncryptedAudioFrame} containing both the RTP header and the encrypted audio data
     */
    EncryptedAudioFrame seal(byte[] key, byte[] audioFrame);
}
