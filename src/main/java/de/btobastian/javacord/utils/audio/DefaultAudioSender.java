package de.btobastian.javacord.utils.audio;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.utils.LoggerUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.NoRouteToHostException;
import java.net.SocketException;

import static de.btobastian.javacord.utils.audio.AudioConnection.FRAME_TIME;

public class DefaultAudioSender implements AudioSender {
	
	private static final Logger logger = LoggerUtil.getLogger(DefaultAudioSender.class);
	
	private final PacketProvider packetProvider;
	private Thread thread;
	
	public DefaultAudioSender(PacketProvider packetProvider) {
		this.packetProvider = packetProvider;
	}
	
	@Override
	public void start() {
		final Server server = packetProvider.getConnectedChannel().getServer();
		final DatagramSocket socket = packetProvider.getUDPSocket();
		
		thread = new Thread(packetProvider.getIdentifier() + " Sending Thread") {
			@Override
			public void run() {
				long lastFrame = System.currentTimeMillis();
				while (!socket.isClosed() && !thread.isInterrupted()) {
					try {
						boolean changeTalking = (System.currentTimeMillis() - lastFrame) > FRAME_TIME;
						DatagramPacket packet = packetProvider.getNextPacket(changeTalking);
						if (packet != null) socket.send(packet);
					} catch (NoRouteToHostException e) {
						packetProvider.onConnectionLost();
					} catch (SocketException ignored) {
					} catch (IOException e) {
						logger.warn("An error occured while sending audio packet.", e);
					} finally {
						long sleep = FRAME_TIME - (System.currentTimeMillis() - lastFrame);
						if (sleep > 0) {
							try {
								Thread.sleep(sleep);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}
						if (System.currentTimeMillis() < lastFrame + 60) {
							lastFrame += FRAME_TIME;
						}
						else {
							lastFrame = System.currentTimeMillis();
						}
					}
				}
			}
		};
		thread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		thread.setDaemon(true);
		thread.start();
	}
	
	@Override
	public void stop() {
		if (thread != null) thread.interrupt();
	}
}
