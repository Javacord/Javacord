package de.btobastian.javacord.entities;

import de.btobastian.javacord.utils.audio.AudioProvider;

import java.util.concurrent.Future;

public interface AudioManager {
	
	public Future<Void> joinOrMoveVoice(VoiceChannel channel);
	
	public void leaveVoice();
	
	public void setProvider(AudioProvider provider);
	
}
