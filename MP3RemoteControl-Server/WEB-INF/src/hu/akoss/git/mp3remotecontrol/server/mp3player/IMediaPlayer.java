package hu.akoss.git.mp3remotecontrol.server.mp3player;

public interface IMediaPlayer {

	void play(String path_) throws Exception;
	void pause() throws Exception;
	void resume() throws Exception;
	void stop() throws Exception;
}
