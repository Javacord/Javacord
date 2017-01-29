package de.btobastian.javacord.utils.audio;

import com.sun.jna.ptr.PointerByReference;
import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.payload.VoiceSpeaking;
import net.tomp2p.opuswrapper.Opus;
import org.slf4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class AudioConnection {
	
	private static final Logger logger = LoggerUtil.getLogger(AudioConnection.class);
	
	// CONSTANTS
	public static final int SAMPLE_RATE = 48000;
	public static final int FRAME_SIZE = 960; // 960
	public static final int FRAME_TIME = 20; // 20
	public static final int CHANNELS = 2;
	
	private final String tID;
	private final AudioSocket socket;
	private DatagramSocket udpSocket;
	private VoiceChannel channel;
	private AudioProvider provider;
	private PointerByReference opusEncoder;
	
	private AudioSender sender;
	
	private boolean speaking = false;
	
	private volatile int silenceCount = 0;
	private boolean silenceSent = false;
	private final byte[] silence = new byte[] {(byte) 0xF8, (byte) 0xFF, (byte) 0xFE};
	
	public AudioConnection(AudioSocket socket, VoiceChannel channel) {
		this.socket = socket;
		this.channel = channel;
		this.socket.connection = this;
		
		tID = "Audio Connection for Guild: " + channel.getServer().getId();
		
		IntBuffer error = IntBuffer.allocate(4);
		opusEncoder = Opus.INSTANCE.opus_encoder_create(SAMPLE_RATE, CHANNELS, Opus.OPUS_APPLICATION_AUDIO, error);
	}
	
	public void ready(long timeout) {
		Thread thread = new Thread(tID + " Ready Thread") {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				boolean connTimeout = false;
				while (!socket.isReady() && !connTimeout) {
					if (timeout > 0 && System.currentTimeMillis() - start > timeout) {
						connTimeout = true;
						break;
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						logger.warn("Thread interrupted.", e);
						Thread.currentThread().interrupt();
					}
				}
				if (!connTimeout) {
					AudioConnection.this.udpSocket = socket.getUdpSocket();
					setupSender();
				}
				else {
					socket.close(6);
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	public void close(int reason) {
		if (sender != null) sender.stop();
		socket.close(reason);
	}
	
	public void setProvider(AudioProvider provider) {
		this.provider = provider;
		setupSender();
	}
	
	public VoiceChannel getChannel() {
		return channel;
	}
	
	private synchronized void setupSender() {
		if (udpSocket != null && !udpSocket.isClosed() && provider != null && sender == null) {
			sender = new DefaultAudioSender(new AudioPacketProvider());
			sender.start();
		}
		else if (provider == null && sender != null) {
			sender.stop();
			sender = null;
		}
	}
	
	private class AudioPacketProvider implements PacketProvider {
		
		char s = 0;
		int time = 0;
		
		@Override
		public String getIdentifier() {
			return tID;
		}
		
		@Override
		public VoiceChannel getConnectedChannel() {
			return AudioConnection.this.channel;
		}
		
		@Override
		public DatagramSocket getUDPSocket() {
			return AudioConnection.this.udpSocket;
		}
		
		@Override
		public DatagramPacket getNextPacket(boolean changeTalking) {
			DatagramPacket nextPacket = null;
			
			try {
				if (silenceSent && provider != null && provider.canProvide()) {
					silenceCount = -1;
					byte[] raw = provider.provide();
					if (raw == null || raw.length == 0) {
						if (speaking && changeTalking) setSpeaking(false);
					}
					if (!provider.preEncoded()) {
						raw = encodeOpus(raw);
					}
					AudioPacket packet = new AudioPacket(s, time, socket.getSSRC(), raw);
					if (!speaking) setSpeaking(true);
					nextPacket = packet.toUDPPacketEncrypted(socket.getAddress(), socket.getKey());
					if (s + 1 > Character.MAX_VALUE) s = 0;
					else s++;
				}
				else if (silenceCount > -1) {
					AudioPacket packet = new AudioPacket(s, time, socket.getSSRC(), silence);
					nextPacket = packet.toUDPPacketEncrypted(socket.getAddress(), socket.getKey());
					if (s + 1 > Character.MAX_VALUE) s = 0;
					else s++;
					if (++silenceCount > 10) {
						silenceCount = -1;
						silenceSent = true;
					}
				}
				else if (speaking && changeTalking) setSpeaking(false);
			} catch (Exception e) {
				logger.warn("An error occured while getting audio packet.", e);
			}
			
			if (nextPacket != null) time += FRAME_SIZE;
			return nextPacket;
		}
		
		@Override
		public void onConnectionError(int status) {
			logger.warn("Audio connection error: " + status);
			socket.close(status);
		}
		
		@Override
		public void onConnectionLost() {
			logger.warn("Unable to send packets. Closing connection.");
			socket.close(1);
		}
	}
	
	private byte[] encodeOpus(byte[] raw) {
		ShortBuffer shortBuffer = ShortBuffer.allocate(raw.length / 2);
		ByteBuffer encoded = ByteBuffer.allocate(4096); // 4096
		for (int i = 0; i < raw.length; i += 2) {
			int first = 0x000000FF & raw[i];
			int second = 0x000000FF & raw[i + 1];
			short s = (short) ((first << 8) | second);
			shortBuffer.put(s);
		}
		shortBuffer.flip();
		int r = Opus.INSTANCE.opus_encode(opusEncoder, shortBuffer, FRAME_SIZE, encoded, encoded.capacity());
		
		byte[] audio = new byte[r];
		encoded.get(audio);
		return audio;
	}
	
	private void setSpeaking(boolean speaking) {
		this.speaking = speaking;
		socket.send(new VoiceSpeaking(speaking));
		if (!speaking) sendSilence();
	}
	
	private void sendSilence() {
		silenceCount = 0;
	}
}
