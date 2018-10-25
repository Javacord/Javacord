package org.javacord.core.util.gateway;

import org.javacord.core.audio.AudioConnectionImpl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioUdpSocket {

    private final DatagramSocket socket;

    private final AudioConnectionImpl audioConnection;
    private final InetSocketAddress address;
    private final int ssrc;

    /**
     * Creates a new audio udp socket.
     *
     * @param audioConnection The audio connection that uses the socket.
     * @param address The address to connect to.
     * @param ssrc The ssrc.
     * @throws SocketException If the socket could not be opened,
     *                         or the socket could not bind to the specified local port.
     */
    public AudioUdpSocket(
            AudioConnectionImpl audioConnection, InetSocketAddress address, int ssrc) throws SocketException {
        this.audioConnection = audioConnection;
        this.address = address;
        this.ssrc = ssrc;
        socket = new DatagramSocket();
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

}
