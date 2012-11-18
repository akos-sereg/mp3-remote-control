package hu.akoss.git.mp3remotecontrol.server.mp3player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class SystemPlayer implements IMediaPlayer {

	@Override
	public void play(final String path_) throws Exception {
		Thread playerThread = new Thread() {
			public void run() {
				try {
					System.err.println("[SystemPlayer] Play: " + path_);

					ProcessBuilder pb = new ProcessBuilder("/home/akoss/bin/mplayer/start.sh", path_);
					pb.start();
					System.err.println("[SystemPlayer] Play: stopped playing "
							+ path_);

				} catch (Exception e) {
					System.err.println("[SystemPlayer] Play: error "
							+ e.getMessage());
				}
			}
		};

		playerThread.start();
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
