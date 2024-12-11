package org.javacord.api.util.crypto;

/**
 * An encrypted audio frame is intended to hold the RTP header, and the encrypted audioFrame.
 */
public class EncryptedAudioFrame {
    private final byte[] header;
    private final byte[] audioFrame;

    /**
     * Constructs the AudioFrame given an RTP header and encrypted audio frame.
     * @param header RTP byte[] header
     * @param encryptedFrame Encrypted audio frame byte[]
     */
    public EncryptedAudioFrame(byte[] header, byte[] encryptedFrame) {
        this.header = header;
        this.audioFrame = encryptedFrame;
    }

    /**
     * Get the RTP header.
     * @return byte[]
     */
    public byte[] getHeader() {
        return this.header;
    }

    /**
     * Get the encrypted audio frame. The exact format depends on what encryption mode constructed this.
     * @return byte[]
     */
    public byte[] getAudioFrame() {
        return this.audioFrame;
    }
}
