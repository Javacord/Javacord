package de.btobastian.javacord.utils.audio;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.AudioManager;
import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.exceptions.AudioException;
import de.btobastian.javacord.exceptions.PermissionsException;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.payload.VoiceStateUpdate;
import net.tomp2p.opuswrapper.Opus;
import org.slf4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ImplAudioManager implements AudioManager {
	
	private static final Logger logger = LoggerUtil.getLogger(ImplAudioManager.class);
	
	private static final boolean OPUS_LOADED;
	
	private final ImplDiscordAPI api;
	private final ImplServer server;
	private VoiceChannel connecting = null;
	private AudioConnection connection = null;
	
	private AudioProvider provider = null;
	
	private long timeout = 10000;
	
	public ImplAudioManager(ImplDiscordAPI api, ImplServer server) {
		this.api = api;
		this.server = server;
	}
	
	public static boolean audioEnabled() {
		return OPUS_LOADED;
	}
	
	private void checkOpus() throws AudioException {
		if (!OPUS_LOADED) throw new AudioException("Opus is not supported on this system.");
	}
	
	public void setConnection(AudioConnection connection) {
		this.connection = connection;
		if (connection == null) return;
		connecting = null;
		connection.setProvider(provider);
		connection.ready(timeout);
	}
	
	public VoiceChannel getConnectingChannel() {
		return connecting;
	}
	
	public VoiceChannel getConnectedChannel() {
		return connection == null ? null : connection.getChannel();
	}
	
	public boolean isConnecting() {
		return connecting != null;
	}
	
	public boolean isConnected() {
		return connection != null;
	}
	
	public void setConnecting(VoiceChannel channel) {
		this.connecting = channel;
	}
	
	public void closeConnection(int reason) {
		connecting = null;
		if (connection == null) return;
		connection.close(reason);
		connection = null;
	}
	
	public void regionChange() {
		VoiceChannel next = connection.getChannel();
		closeConnection(3);
		this.connecting = next;
	}
	
	@Override
	public Future<Void> joinOrMoveVoice(VoiceChannel channel) {
		return api.getThreadPool().getExecutorService().submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				checkOpus();
				if (!server.getId().equals(channel.getServer().getId())) throw new IllegalArgumentException("Voice channel is from different server.");
				if (connecting != null) throw new AudioException("Already trying to connect to voice channel " + connecting.getId() + ".");
				PermissionState state = channel.getOverwrittenPermissions(api.getYourself()).getState(PermissionType.VOICE_CONNECT);
				if (state == PermissionState.DENIED) throw new PermissionsException("Bot does not have permission to join channel.");
				connecting = channel;
				logger.debug("Sending voice state update (channel: {})", channel.getId());
				api.getSocketAdapter().send(new VoiceStateUpdate(channel, false, false));
				return null;
			}
		});
	}
	
	@Override
	public void setProvider(AudioProvider provider) {
		this.provider = provider;
		if (connection != null) connection.setProvider(provider);
	}
	
	@Override
	public void leaveVoice() {
		closeConnection(0);
	}
	
	static {
		boolean load = false;
		try {
			logger.debug("Loading Opus Library (supported: {})", Opus.INSTANCE == null ? "" : "yes");
			load = true;
		} catch (UnsatisfiedLinkError ignored) {
			logger.debug("Opus not supported. Audio connections disabled.");
		}
		OPUS_LOADED = load;
	}
	
}
