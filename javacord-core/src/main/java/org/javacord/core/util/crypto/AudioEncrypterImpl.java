package org.javacord.core.util.crypto;

import org.javacord.api.util.crypto.AudioEncrypter;

import java.nio.ByteBuffer;

/**
 * Base implementation for audio encryption functionality.
 *
 * <p>Provides common RTP header generation and state management
 * for audio encryption implementations.
 */
public abstract class AudioEncrypterImpl implements  AudioEncrypter {
    protected static final byte RTP_TYPE = (byte) 0x80;
    protected static final byte RTP_VERSION = (byte) 0x78;
    protected static final int RTP_HEADER_LENGTH = 12;

    protected int ssrc;
    protected int timestamp;
    protected char sequence;

    /**
     * {@inheritDoc}
     */
    @Override
    public org.javacord.api.util.crypto.AudioEncrypter setSsrc(int ssrc) {
        this.ssrc = ssrc;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public org.javacord.api.util.crypto.AudioEncrypter setTimestamp(int timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AudioEncrypter setSequence(char sequence) {
        this.sequence = sequence;
        return this;
    }

    /**
     * Generates the next RTP header using the current state.
     *
     * <p>Creates a 12 byte header containing the RTP type, version, sequence number,
     * timestamp, and ssrc identifier.
     *
     * @return byte[] containing complete RTP header
     */
    protected byte[] nextHeader() {
        ByteBuffer buffer = ByteBuffer.allocate(RTP_HEADER_LENGTH)
                .put(0, RTP_TYPE)
                .put(1, RTP_VERSION)
                .putChar(2, this.sequence)
                .putInt(4, this.timestamp)
                .putInt(8, this.ssrc);
        return buffer.array();
    }
}
