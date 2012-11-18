package hu.akoss.git.mp3remotecontrol.client.model;

public class NowPlayingInfo {

	private String _fullPath;
	private boolean _isStopped;
	private boolean _isPaused;
	private float _volume;
	private String _filename;

	public String getFilename() {
		return _filename;
	}

	public void setFilename(String _filename) {
		this._filename = _filename;
	}

	public float getVolume() {
		return _volume;
	}

	public void setVolume(float _volume) {
		this._volume = _volume;
	}

	public String getFullPath() {
		return _fullPath;
	}

	public void setFullPath(String _fullPath) {
		this._fullPath = _fullPath;
	}
	
	public boolean isStopped() {
		return _isStopped;
	}

	public void setIsStopped(boolean _isStopped) {
		this._isStopped = _isStopped;
	}

	public boolean isPaused() {
		return _isPaused;
	}

	public void setIsPaused(boolean _isPaused) {
		this._isPaused = _isPaused;
	}

	public NowPlayingInfo(String fullPath_)
	{
		_fullPath = fullPath_;
	}
}
