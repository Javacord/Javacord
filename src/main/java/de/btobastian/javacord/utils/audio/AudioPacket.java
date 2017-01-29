package de.btobastian.javacord.utils.audio;

import de.btobastian.javacord.utils.TweetNaclFast;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioPacket {
	
	public static final int RTP_HEADER_LENGTH = 12;
	public static final int XSALSA20_LENGTH = 24;
	public static final byte RTP_VERSION_PAD = (byte) 0x80;
	public static final byte RTP_TYPE = (byte) 0x78;
	
	public static final int RTP_VERSION_PAD_INDEX = 0;
	public static final int RTP_PAYLOAD_INDEX = 1;
	public static final int S_INDEX = 2;
	public static final int TIMESTAMP_INDEX = 4;
	public static final int SSRC_INDEX = 8;
	
	private final char s;
	private final int time;
	private final int ssrc;
	private final byte[] audio;
	private final byte[] raw;
	
	public AudioPacket(DatagramPacket packet) {
		this(Arrays.copyOf(packet.getData(), packet.getLength()));
	}
	
	public AudioPacket(byte[] raw) {
		this.raw = raw;
		
		ByteBuffer buffer = ByteBuffer.wrap(raw);
		s = buffer.getChar(S_INDEX);
		time = buffer.getInt(TIMESTAMP_INDEX);
		ssrc = buffer.getInt(SSRC_INDEX);
		
		byte[] audio = new byte[buffer.array().length - RTP_HEADER_LENGTH];
		System.arraycopy(buffer.array(), RTP_HEADER_LENGTH, audio, 0, audio.length);
		this.audio = audio;
	}
	
	public AudioPacket(char s, int time, int ssrc, byte[] audio) {
		this.s = s;
		this.time = time;
		this.ssrc = ssrc;
		this.audio = audio;
		
		ByteBuffer buffer = ByteBuffer.allocate(RTP_HEADER_LENGTH + audio.length);
		buffer.put(RTP_VERSION_PAD_INDEX, RTP_VERSION_PAD);
		buffer.put(RTP_PAYLOAD_INDEX, RTP_TYPE);
		buffer.putChar(S_INDEX, s);
		buffer.putInt(TIMESTAMP_INDEX, time);
		buffer.putInt(SSRC_INDEX, ssrc);
		System.arraycopy(audio, 0, buffer.array(), RTP_HEADER_LENGTH, audio.length);
		raw = buffer.array();
	}
	
	public byte[] nonce() {
		return Arrays.copyOf(raw, RTP_HEADER_LENGTH);
	}
	
	public byte[] rawPacket() {
		return Arrays.copyOf(raw, raw.length);
	}
	
	public byte[] getAudio() {
		return Arrays.copyOf(audio, audio.length);
	}
	
	public char getSequence() {
		return s;
	}
	
	public int getSSRC() {
		return ssrc;
	}
	
	public int getTime() {
		return time;
	}
	
	public DatagramPacket toUDPPacket(InetSocketAddress address) {
		return new DatagramPacket(rawPacket(), raw.length, address);
	}
	
	public DatagramPacket toUDPPacketEncrypted(InetSocketAddress address, byte[] key) {
		byte[] eNonce = new byte[XSALSA20_LENGTH];
		System.arraycopy(nonce(), 0, eNonce, 0, RTP_HEADER_LENGTH);
		TweetNaclFast.SecretBox boxer = new TweetNaclFast.SecretBox(key);
		byte[] encrypted = boxer.box(audio, eNonce);
		return new AudioPacket(s, time, ssrc, encrypted).toUDPPacket(address);
	}
	
	public static AudioPacket echoPacket(DatagramPacket packet, int ssrc) {
		ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOf(packet.getData(), packet.getLength()));
		buffer.put(RTP_VERSION_PAD_INDEX, RTP_VERSION_PAD);
		buffer.put(RTP_PAYLOAD_INDEX, RTP_TYPE);
		buffer.putInt(SSRC_INDEX, ssrc);
		return new AudioPacket(buffer.array());
	}
	
	public static AudioPacket decryptPacket(DatagramPacket packet, byte[] key) {
		TweetNaclFast.SecretBox boxer = new TweetNaclFast.SecretBox(key);
		AudioPacket encrypted = new AudioPacket(packet);
		byte[] eNonce = new byte[XSALSA20_LENGTH];
		System.arraycopy(encrypted.nonce(), 0, eNonce, 0, RTP_HEADER_LENGTH);
		byte[] decrypted = boxer.open(encrypted.getAudio(), eNonce);
		byte[] decryptedRaw = new byte[RTP_HEADER_LENGTH + decrypted.length];
		System.arraycopy(encrypted.nonce(), 0, decryptedRaw, 0, RTP_HEADER_LENGTH);
		System.arraycopy(decrypted, 0, decryptedRaw, RTP_HEADER_LENGTH, decrypted.length);
		return new AudioPacket(decryptedRaw);
	}
	
}
