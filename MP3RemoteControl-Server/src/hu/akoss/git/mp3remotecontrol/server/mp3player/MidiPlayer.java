package hu.akoss.git.mp3remotecontrol.server.mp3player;

import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiPlayer implements IMediaPlayer {

	@Override
	public void play(String path_) throws Exception {
		File inputFile = new File(path_);
		Sequence seq;
		Sequencer sequencer;
		try {
			sequencer = MidiSystem.getSequencer();
			seq = MidiSystem.getSequence(inputFile);
			sequencer.setSequence(seq);
			sequencer.open();
			sequencer.start();
			System.err.println("[MidiPlayer] playing started");
		} catch (Exception exception) {
			// fail
			System.out.println(exception.getMessage());
		}
	}

	@Override
	public void pause() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub

	}

}
