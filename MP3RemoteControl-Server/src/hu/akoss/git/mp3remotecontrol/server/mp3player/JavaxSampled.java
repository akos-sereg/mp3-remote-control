package hu.akoss.git.mp3remotecontrol.server.mp3player;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

// http://www.jsresources.org/examples/SimpleAudioPlayer.java.html
public class JavaxSampled implements IMediaPlayer {
	// private String fileName;

	private final int EXTERNAL_BUFFER_SIZE = 128; // 128000;
	private SourceDataLine line;

	public void init(Object parent, String fileName) throws Exception {
		// Not Implemented cached audio file, we just store filename
		// this.fileName = fileName;
	}

	public void play() throws Exception {
		// play(null, fileName);
	}

	public void play(Object parent, String fileName) throws Exception {

		long start = System.currentTimeMillis();
		// load soundfile
		File soundFile = new File(fileName);
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception ex) {
			throw ex;
		}

		// audioinformat of soundfile
		// audioline
		AudioFormat audioFormat = audioInputStream.getFormat();

		AudioFormat decodedFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(),
				16, audioFormat.getChannels(), audioFormat.getChannels() * 2,
				audioFormat.getSampleRate(), false);
		AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat,
				audioInputStream);

		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				decodedFormat);

		try {
			// open audiodata line
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(decodedFormat);
		} catch (LineUnavailableException ex) {
			throw ex;
		}

		// line can now receive data
		line.start();

		// write data to line
		long start1 = 0;
		int nBytesRead = 0;
		byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
		while (nBytesRead != -1) {
			try {
				nBytesRead = din.read(abData, 0, abData.length);
			} catch (IOException ex) {
				throw ex;
			}
			if (nBytesRead >= 0) {
				int nBytesWritten = line.write(abData, 0, nBytesRead);
				if (start1 == 0)
					start1 = System.currentTimeMillis();
			}
		}

		// wait until all data is played
		line.drain();

		// close line
		line.close();
		din.close();

		long end2 = System.currentTimeMillis();
		System.out.println("duration: " + (start1 - start));
		System.out.println("duration1: " + (end2 - start1));
	}

	public void stop() {
		line.close();
	}

	public void dispose() {
		if (line != null)
			line.close();
	}

	@Override
	public void play(String path_) throws Exception {
		System.setProperty("com.sun.speech.freetts.audio.AudioPlayer.openFailDelayMs", "100");
		System.setProperty("com.sun.speech.freetts.audio.AudioPlayer.totalOpenFailDelayMs", "30000");
		play(null, path_);
	}

	@Override
	public void pause() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() throws Exception {
		// TODO Auto-generated method stub

	}

}
