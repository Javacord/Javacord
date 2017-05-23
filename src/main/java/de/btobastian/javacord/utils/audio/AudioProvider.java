package de.btobastian.javacord.utils.audio;

import javax.sound.sampled.AudioFormat;

public interface AudioProvider {
	
	AudioFormat FORMAT = new AudioFormat(48000f, 16, 2, true, true);
	
	boolean canProvide();
	
	byte[] provide();
	
	default boolean preEncoded() {
		return false;
	}
	
}
