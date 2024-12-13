package org.javacord.core.util.gateway;

import org.apache.logging.log4j.Logger;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.audio.AudioSourceBase;
import org.javacord.api.util.crypto.AudioEncryptor;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.audio.AudioConnectionImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.audio.AudioSourceFinishedEventImpl;
import org.javacord.core.util.logging.LoggerUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class AudioUdpSocket {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(AudioUdpSocket.class);

    private final DatagramSocket socket;
    private final String threadName;

    private final AudioConnectionImpl connection;
    private final InetSocketAddress address;
    private final AudioEncryptor audioEncryptor;
    private final int ssrc;

    private volatile boolean shouldSend = false;

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
     * @param address    The address to connect to.
     * @param ssrc       The ssrc.
     * @param audioEncryptor The AudioEncryptor to seal the audio frames.
     * @throws SocketException If the socket could not be opened,
     *                         or the socket could not bind to the specified local port.
     */
    public AudioUdpSocket(AudioConnectionImpl connection,
                          InetSocketAddress address,
                          int ssrc,
                          AudioEncryptor audioEncryptor) throws SocketException {
        this.connection = connection;
        this.address = address;
        this.ssrc = ssrc;
        this.audioEncryptor = audioEncryptor;

        socket = new DatagramSocket();
        threadName = String.format("Javacord Audio Send Thread (%#s)", connection.getServer());
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
     * @see <a href="https://discord.com/developers/docs/topics/voice-connections#ip-discovery">Discord Docs</a>
     */
    public InetSocketAddress discoverIp() throws IOException {
        byte[] buffer = new byte[74];
        ByteBuffer.wrap(buffer)
                .putShort((short) 1)
                .putShort((short) 70)
                .putInt(ssrc);

        // send the byte array which contains the ssrc
        socket.send(new DatagramPacket(buffer, buffer.length, address));
        // create a new buffer which is used to receive data from discord
        buffer = new byte[74];
        socket.receive(new DatagramPacket(buffer, buffer.length));
        // gets the ip of the packet
        final String ip = new String(buffer, 8, buffer.length - 10).trim();
        // gets the port (last two bytes) which is a little endian unsigned short
        final int port =
                ByteBuffer.wrap(new byte[] {buffer[buffer.length - 1], buffer[buffer.length - 2]}).getShort() & 0xffff;

        return new InetSocketAddress(ip, port);
    }

    /**
     * Starts polling frames from the audio connection and sending them through the socket.
     */
    public void startSending() {
        if (shouldSend) {
            return;
        }
        shouldSend = true;

        DiscordApiImpl api = (DiscordApiImpl) connection.getChannel().getApi();
        api.getThreadPool().getSingleThreadExecutorService(threadName).submit(() -> {
            try {
                long nextFrameTimestamp = System.nanoTime();
                boolean dontSleep = true;
                boolean speaking = false;
                long framesOfSilenceToPlay = 5;
                while (shouldSend) {
                    // Get the current audio source. If none is available, it will block the thread
                    AudioSource source = connection.getCurrentAudioSourceBlocking();
                    if (source == null) {
                        logger.error("Got null audio source without being interrupted ({})", connection);
                        return;
                    }

                    if (source.hasFinished()) {
                        connection.removeAudioSource();
                        dontSleep = true;

                        // Dispatch AudioSourceFinishedEvent AFTER removing the source.
                        // Otherwise, AudioSourceFinishedEvent#getNextSource() won't work
                        api.getEventDispatcher().dispatchAudioSourceFinishedEvent(
                                (ServerImpl) connection.getServer(),
                                connection,
                                ((AudioSourceBase) source).getDelegate(),
                                new AudioSourceFinishedEventImpl(source, connection));
                        continue;
                    }

                    AudioPacket packet = null;
                    byte[] frame = source.hasNextFrame() ? source.getNextFrame() : null;

                    // If the source is muted, replace the frame with a muted frame
                    if (source.isMuted()) {
                        frame = null;
                    }

                    if (frame != null || framesOfSilenceToPlay > 0) {
                        if (!speaking && frame != null) {
                            speaking = true;
                            connection.setSpeaking(true);
                        }

                        packet = new AudioPacket(
                                frame,
                                sequence,
                                ((int) sequence * 960),
                                ssrc,
                                this.audioEncryptor
                        );

                        // We can stop sending frames of silence after 5 frames
                        if (frame == null) {
                            framesOfSilenceToPlay--;
                            if (framesOfSilenceToPlay == 0) {
                                speaking = false;
                                connection.setSpeaking(false);
                            }
                        } else {
                            framesOfSilenceToPlay = 5;
                        }
                    }

                    nextFrameTimestamp = nextFrameTimestamp + 20_000_000;

                    sequence++;

                    if (packet != null) {
                        try {
                            packet.encrypt(secretKey);
                        } catch (RuntimeException e) {
                            logger.error("Could not encrypt audio packet. Disconnecting: ", e);
                            connection.close(); // Is this correct? What do we want to do when a packet can't encrypt?
                            return;
                        }
                    }
                    try {
                        if (dontSleep) {
                            nextFrameTimestamp = System.nanoTime() + 20_000_000;
                            dontSleep = false;
                        } else {
                            Thread.sleep(Math.max(0, nextFrameTimestamp - System.nanoTime()) / 1_000_000);
                        }
                        if (packet != null) {
                            socket.send(packet.asUdpPacket(address));
                        }
                    } catch (IOException e) {
                        logger.error("Failed to send audio packet for {}", connection);
                    }
                }
            } catch (InterruptedException e) {
                if (shouldSend) {
                    logger.debug("Got interrupted unexpectedly while waiting for next audio source packet");
                }
            }
        });
    }

    /**
     * Stops polling frames from the audio connection.
     */
    public void stopSending() {
        shouldSend = false;
        connection.getChannel()
                .getApi()
                .getThreadPool()
                .removeAndShutdownSingleThreadExecutorService(threadName);
    }

}
