package org.javacord.core.util.gateway;

import org.javacord.api.audio.SilentAudioSource;
import org.javacord.api.util.crypto.AudioEncryptor;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class AudioPacket {
    private static final byte RTP_TYPE = (byte) 0x80;
    private static final byte RTP_VERSION = (byte) 0x78;
    private static final int RTP_HEADER_LENGTH = 12;

    private final AudioEncryptor audioEncryptor;
    private final byte[] header;

    private byte[] audioFrame;

    /**
     * Creates a new audio packet.
     *
     * @param audioFrame A byte array containing 20ms of audio
     * @param sequence The 16 bit sequence number
     * @param timestamp The RTP timestamp value
     * @param ssrc The 32 bit synchronization source identifier
     * @param audioEncryptor The encryptor that will seal the frame.
     */
    public AudioPacket(byte[] audioFrame,
                       char sequence,
                       int timestamp,
                       int ssrc,
                       AudioEncryptor audioEncryptor) {
        if (audioFrame == null) {
            audioFrame = SilentAudioSource.SILENCE_FRAME;
        }

        ByteBuffer buffer = ByteBuffer.allocate(RTP_HEADER_LENGTH)
                .put(0, RTP_TYPE)
                .put(1, RTP_VERSION)
                .putChar(2, sequence)
                .putInt(4, timestamp)
                .putInt(8, ssrc);
        this.header = buffer.array();

        this.audioFrame = audioFrame;
        this.audioEncryptor = audioEncryptor;
    }

    /**
     * Encrypts the audio packet with the given key.
     *
     * @param key The key used to encrypt the packet.
     */
    public void encrypt(byte[] key) {
        this.audioFrame = this.audioEncryptor.seal(this.header, key, this.audioFrame);
    }

    /**
     * Creates a {@code DatagramPacket}, ready to be sent.
     *
     * @param address The destination address.
     * @return The created datagram packet.
     */
    public DatagramPacket asUdpPacket(InetSocketAddress address) {
        byte[] packet = new byte[this.header.length + this.audioFrame.length];
        System.arraycopy(
                this.header,
                0,
                packet,
                0,
                this.header.length
        );
        System.arraycopy(
                this.audioFrame,
                0,
                packet,
                this.header.length,
                this.audioFrame.length
        );
        return new DatagramPacket(packet, packet.length, address);
    }
}
