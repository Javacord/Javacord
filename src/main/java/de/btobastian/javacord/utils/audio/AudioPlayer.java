package de.btobastian.javacord.utils.audio;

import de.btobastian.javacord.utils.LoggerUtil;
import org.slf4j.Logger;
import org.tritonus.dsp.ais.AmplitudeAudioInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;

public abstract class AudioPlayer implements AudioProvider {
	
	private static final Logger logger = LoggerUtil.getLogger(AudioPlayer.class);
	
	private AudioInputStream input = null;
	private AudioFormat format = null;
	private AmplitudeAudioInputStream amplitudeStream = null;
	private float volume = 1.0f;
	
	public abstract void play();
	
	public abstract void pause();
	
	public abstract void stop();
	
	public abstract void reset();
	
	public abstract boolean hasStarted();
	
	public abstract boolean isPlaying();
	
	public abstract boolean isPaused();
	
	public abstract boolean isStopped();
	
	protected void onAudioEnd() {}
	
	public final void setInput(AudioInputStream input) {
		if (input == null) throw new IllegalArgumentException("Audio source is null.");
		if (this.input != null) {
			try {
				this.input.close();
			} catch (IOException e) {
				logger.warn("Error closing the audio stream.", e);
			}
		}
		AudioFormat inputFormat = input.getFormat();
		AudioFormat pcm = new AudioFormat(
				Encoding.PCM_SIGNED,
				inputFormat.getSampleRate(),
				inputFormat.getSampleSizeInBits() != -1 ? inputFormat.getSampleSizeInBits() : 16,
				inputFormat.getChannels(),
				inputFormat.getFrameSize() != -1 ? inputFormat.getFrameSize() : 2 * inputFormat.getChannels(),
				inputFormat.getFrameRate() != -1.0f ? inputFormat.getFrameRate() : inputFormat.getSampleRate(),
				inputFormat.isBigEndian());
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(pcm, input);
		this.format = new AudioFormat(pcm.getEncoding(), 48000.0f, pcm.getSampleSizeInBits(), pcm.getChannels(), pcm.getFrameSize(), 48000.0f, true);
		amplitudeStream = new AmplitudeAudioInputStream(inputStream);
		this.input = AudioSystem.getAudioInputStream(format, amplitudeStream);
	}
	
	public final void setVolume(float volume) {
		this.volume = volume;
		if (amplitudeStream != null) amplitudeStream.setAmplitudeLinear(volume);
	}
	
	public final boolean canProvide() {
		return !isPaused() && !isStopped();
	}
	
	@Override
	public byte[] provide() {
		if (input == null || format == null) throw new IllegalArgumentException("Audio input must be set before audio can be played.");
		try {
			byte[] audio = new byte[960 * format.getFrameSize()];
			int i = input.read(audio, 0, audio.length);
			if (i > 0) {
				return audio;
			}
			else {
				stop();
				input.close();
				onAudioEnd();
				return null;
			}
		} catch (IOException e) {
			logger.warn("An error occured while providing audio.", e);
			return new byte[0];
		}
	}
}
