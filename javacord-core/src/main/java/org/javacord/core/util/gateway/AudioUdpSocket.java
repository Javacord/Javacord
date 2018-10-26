package org.javacord.core.util.gateway;

import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.core.audio.AudioConnectionImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class AudioUdpSocket {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(AudioUdpSocket.class);

    private final DatagramSocket socket;

    private final AudioConnectionImpl connection;
    private final InetSocketAddress address;
    private final int ssrc;

    /**
     * The secret key used to encrypt audio packets.
     */
    private byte[] secretKey;

    /**
     * Gets incremented for every packet sent.
     */
    private char sequence = (char) 0;

    /**
     * Creates a new audio udp socket.
     *
     * @param connection The audio connection that uses the socket.
     * @param address The address to connect to.
     * @param ssrc The ssrc.
     * @throws SocketException If the socket could not be opened,
     *                         or the socket could not bind to the specified local port.
     */
    public AudioUdpSocket(AudioConnectionImpl connection, InetSocketAddress address, int ssrc) throws SocketException {
        this.connection = connection;
        this.address = address;
        this.ssrc = ssrc;
        socket = new DatagramSocket();
    }

    /**
     * Sets the secret key which is used to encrypt audio packets.
     *
     * @param secretKey The secret key.
     */
    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Finds the bot's "real" external address (in case it's behind a NAT) using a technique called
     * <a href="https://en.wikipedia.org/wiki/UDP_hole_punching">UDP hole punching</a>.
     *
     * @return You real external address.
     * @throws IOException If an I/O error occurs.
     * @see <a href="https://discordapp.com/developers/docs/topics/voice-connections#ip-discovery">Discord Docs</a>
     */
    public InetSocketAddress discoverIp() throws IOException {
        byte[] buffer = new byte[70];
        ByteBuffer.wrap(buffer).putInt(0, ssrc);
        // send the byte array which contains the ssrc
        socket.send(new DatagramPacket(buffer, buffer.length, address));
        // create a new buffer which is used to receive data from discord
        buffer = new byte[70];
        socket.receive(new DatagramPacket(buffer, buffer.length));
        // gets the ip of the packet
        String ip = new String(Arrays.copyOfRange(buffer, 3, buffer.length - 2)).trim();
        // gets the port (last two bytes) which is a little endian unsigned short
        int port = ByteBuffer.wrap(new byte[]{buffer[69], buffer[68]}).getShort() & 0xffff;

        return new InetSocketAddress(ip, port);
    }

    /**
     * Starts polling frames from the audio connection and sending them through the socket.
     */
    public void startSending() {
        DiscordApi api = connection.getChannel().getApi();
        ExecutorService executorService = api.getThreadPool().getSingleThreadExecutorService(
                String.format("Javacord Audio Send Thread (%#s)", connection.getServer()));

        executorService.submit(() -> {
            try {
                long nextFrameTimestamp = System.nanoTime();
                while (true) {
                    AudioSource source = connection.getCurrentAudioSourceBlocking(Long.MAX_VALUE, TimeUnit.DAYS);
                    if (source == null) {
                        logger.error("Got null audio source without being interrupted ({})", connection);
                        return;
                    }
                    AudioPacket packet;
                    if (!source.hasNextFrame()) {
                        packet = new AudioPacket(ssrc, sequence, sequence * 960);
                        nextFrameTimestamp = nextFrameTimestamp + 20_000_000;
                    } else {
                        packet = new AudioPacket(source.getNextFrame(), ssrc, sequence, ((int) sequence) * 960);
                        nextFrameTimestamp = nextFrameTimestamp + 20_000_000;
                    }

                    sequence++;

                    packet.encrypt(secretKey);
                    try {
                        Thread.sleep(Math.max(0, nextFrameTimestamp - System.nanoTime()) / 1_000_000);
                        socket.send(packet.asUdpPacket(address));
                    } catch (IOException e) {
                        logger.error("Failed to send audio packet for {}", connection);
                    }
                }
            } catch (InterruptedException e) {
                logger.debug("Got interrupted while waiting for next audio source packet");
            }
        });
    }

}
