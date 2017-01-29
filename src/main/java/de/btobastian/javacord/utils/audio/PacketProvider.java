package de.btobastian.javacord.utils.audio;

import de.btobastian.javacord.entities.VoiceChannel;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface PacketProvider {
	
	public String getIdentifier();
	
	public VoiceChannel getConnectedChannel();
	
	public DatagramSocket getUDPSocket();
	
	public DatagramPacket getNextPacket(boolean changeTalking);
	
	public void onConnectionError(int status);
	
	public void onConnectionLost();
	
}
