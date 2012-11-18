package hu.akoss.git.mp3remotecontrol.server.mp3player;

import hu.akoss.git.mp3remotecontrol.server.utils.HtmlEntities;

import java.io.File;

import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.format.AudioFormat;
import javax.media.pim.PlugInManager;


public class JmfPlayer implements IMediaPlayer {

	private Player _player;
	private String _currentlyPlaying;

	@Override
	public void play(String file_) throws Exception {
		if (file_ == null && _player != null) {
			System.err.println("[Mp3Player] Play: Resume " + _currentlyPlaying);
			_player.start();
			return;
		}

		_currentlyPlaying = file_;

		Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		Format input2 = new AudioFormat(AudioFormat.MPEG);
		Format output = new AudioFormat(AudioFormat.LINEAR);

		PlugInManager.addPlugIn("com.sun.media.codec.audio.mp3.JavaDecoder",
				new Format[] { input1, input2 }, new Format[] { output },
				PlugInManager.CODEC);

		if (_player != null && _player.getState() == Player.Started) {
			_player.stop();
			_player.close();
			System.err.println("[Mp3Player] Play: Exsting player stopped.");
		}

		System.err.println("[Mp3Player] Play: requested (html): "
				+ _currentlyPlaying);
		System.err.println("[Mp3Player] Play: requested (raw): "
				+ HtmlEntities.toPlain(_currentlyPlaying));
		_player = Manager.createPlayer(new MediaLocator(new File(HtmlEntities
				.toPlain(_currentlyPlaying)).toURI().toURL()));
		_player.start();
		System.err.println("[Mp3Player] Play: Started " + _currentlyPlaying);

	}

	@Override
	public void resume() {

	}

	@Override
	public void stop() {
		if (_player == null) {
			System.err.println("[Mp3Player] Stop: Nothing to be stopped.");
			return;
		}

		try {
			_player.stop();
			_player.close();

			System.err
					.println("[Mp3Player] Stop: Stopped " + _currentlyPlaying);
			_currentlyPlaying = null;
		} catch (Exception err) {
			System.err.println("[Mp3Player] Stop: Error: " + err.getMessage());
		}
	}

	@Override
	public void pause() throws Exception {
		if (_player == null) {
			System.err.println("[Mp3Player] Pause: Nothing to be paused.");
			return;
		}

		try {
			_player.stop();

			System.err
					.println("[Mp3Player] Pause: Paused " + _currentlyPlaying);
		} catch (Exception err) {
			System.err.println("[Mp3Player] Pause: Error: " + err.getMessage());
		}

	}

}
