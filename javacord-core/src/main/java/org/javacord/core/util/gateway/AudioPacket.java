package org.javacord.core.util.gateway;

import com.codahale.xsalsa20poly1305.SecretBox;
import okio.ByteString;
import org.javacord.api.audio.SilentAudioSource;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class AudioPacket {

    private static final byte RTP_TYPE = (byte) 0x80;
    private static final byte RTP_VERSION = (byte) 0x78;
    private static final int RTP_HEADER_LENGTH = 12;
    private static final int NONCE_LENGTH = 24;

    private boolean encrypted;
    private byte[] header;
    private byte[] audioFrame;

    /**
     * Creates a new silent audio packet.
     *
     * @param ssrc The ssrc.
     * @param sequence The sequence.
     * @param timestamp The timestamp.
     */
    public AudioPacket(int ssrc, char sequence, int timestamp) {
        this(null, ssrc, sequence, timestamp);
    }

    /**
     * Creates a new audio packet.
     *
     * @param audioFrame A byte array containing 20ms of audio
     * @param ssrc The ssrc.
     * @param sequence The sequence.
     * @param timestamp The timestamp.
     */
    public AudioPacket(byte[] audioFrame, int ssrc, char sequence, int timestamp) {
        if (audioFrame == null) {
            audioFrame = SilentAudioSource.SILENCE_FRAME;
        }
        this.audioFrame = audioFrame;
        // See https://discordapp.com/developers/docs/topics/voice-connections#encrypting-and-sending-voice
        ByteBuffer buffer = ByteBuffer.allocate(RTP_HEADER_LENGTH)
                .put(0, RTP_TYPE)
                .put(1, RTP_VERSION)
                .putChar(2, sequence)
                .putInt(4, timestamp)
                .putInt(8, ssrc);
        header = buffer.array();
    }

    /**
     * Encrypts the audio packet with the given key.
     *
     * @param key The key used to encrypt the packet.
     */
    public void encrypt(byte[] key) {
        byte[] nonce = new byte[NONCE_LENGTH];
        System.arraycopy(header, 0, nonce, 0, RTP_HEADER_LENGTH);
        audioFrame = new SecretBox(ByteString.of(key)).seal(ByteString.of(nonce), ByteString.of(audioFrame))
                .toByteArray();
        encrypted = true;
    }

    /**
     * Creates a {@code DatagramPacket}, ready to be sent.
     *
     * @param address The destination address.
     * @return The created datagram packet.
     */
    public DatagramPacket asUdpPacket(InetSocketAddress address) {
        byte[] packet = new byte[header.length + audioFrame.length];
        System.arraycopy(header, 0, packet, 0, header.length);
        System.arraycopy(audioFrame, 0, packet, header.length, audioFrame.length);
        return new DatagramPacket(packet, packet.length, address);
    }

    /**
     * Checks if the audio packet has been encrypted using the {@link #encrypt(byte[])} method.
     *
     * @return Whether the audio packet is encrypted or not.
     */
    public boolean isEncrypted() {
        return encrypted;
    }

}
