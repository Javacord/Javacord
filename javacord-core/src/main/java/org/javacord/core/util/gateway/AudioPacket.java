package org.javacord.core.util.gateway;

import org.javacord.api.audio.SilentAudioSource;
import org.javacord.api.util.crypto.AudioEncrypter;
import org.javacord.api.util.crypto.EncryptedAudioFrame;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class AudioPacket {
    private final AudioEncrypter audioEncrypter;
    private final byte[] audioFrame;

    private EncryptedAudioFrame encryptedFrame;

    /**
     * Creates a new audio packet.
     *
     * @param audioFrame A byte array containing 20ms of audio
     */
    public AudioPacket(byte[] audioFrame, AudioEncrypter audioEncrypter) {
        if (audioFrame == null) {
            audioFrame = SilentAudioSource.SILENCE_FRAME;
        }
        this.audioFrame = audioFrame;
        this.audioEncrypter = audioEncrypter;
    }

    /**
     * Encrypts the audio packet with the given key.
     *
     * @param key The key used to encrypt the packet.
     */
    public void encrypt(byte[] key) {
        this.encryptedFrame = this.audioEncrypter.seal(key, this.audioFrame);
    }

    /**
     * Creates a {@code DatagramPacket}, ready to be sent.
     *
     * @param address The destination address.
     * @return The created datagram packet.
     */
    public DatagramPacket asUdpPacket(InetSocketAddress address) {
        byte[] packet = new byte[this.encryptedFrame.getHeader().length + this.encryptedFrame.getAudioFrame().length];
        System.arraycopy(this.encryptedFrame.getHeader(), 0, packet, 0, this.encryptedFrame.getHeader().length);
        System.arraycopy(this.encryptedFrame.getAudioFrame(), 0, packet, this.encryptedFrame.getHeader().length, this.encryptedFrame.getAudioFrame().length);
        return new DatagramPacket(packet, packet.length, address);
    }
}
