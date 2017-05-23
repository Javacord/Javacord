package de.btobastian.javacord.utils.audio.player;

import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.javacord.utils.audio.AudioPlayer;
import org.slf4j.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class FilePlayer extends AudioPlayer {
	
	private static final Logger logger = LoggerUtil.getLogger(FilePlayer.class);
	
	private Runnable runnable = null;
	
	private File file = null;
	private boolean started = false;
	private boolean playing = false;
	private boolean paused = false;
	private boolean stopped = true;
	
	public FilePlayer() {
	}
	
	public FilePlayer(File file) throws IOException, UnsupportedAudioFileException {
		setFile(file);
	}
	
	public void setFile(File file) throws IOException, UnsupportedAudioFileException {
		if (file == null) throw new IllegalArgumentException("Audio file is null");
		else if (!file.exists()) throw new IllegalArgumentException("Audio file doesn't exist.");
		else {
			resetState();
			this.file = file;
			setInput(AudioSystem.getAudioInputStream(file));
		}
	}
	
	private void resetState() {
		file = null;
		started = false;
		playing = false;
		paused = false;
		stopped = true;
	}
	
	@Override
	public void play() {
		if (started && stopped) {
			throw new IllegalStateException("Cannot play after being stopped. Restart the player to play.");
		}
		else {
			started = true;
			playing = true;
			paused = false;
			stopped = false;
		}
	}
	
	@Override
	public void pause() {
		playing = false;
		paused = true;
	}
	
	@Override
	public void stop() {
		playing = false;
		paused = false;
		stopped = true;
	}
	
	@Override
	public void reset() {
		try {
			File f = file;
			resetState();
			setFile(f);
		} catch (IOException | UnsupportedAudioFileException e) {
			logger.warn("Error while resetting file player.", e);
		}
	}
	
	@Override
	public boolean hasStarted() {
		return started;
	}
	
	@Override
	public boolean isPlaying() {
		return playing;
	}
	
	@Override
	public boolean isPaused() {
		return paused;
	}
	
	@Override
	public boolean isStopped() {
		return stopped;
	}
	
	public void onAudioEnd(Runnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	protected void onAudioEnd() {
		if (runnable != null) runnable.run();
	}
}
