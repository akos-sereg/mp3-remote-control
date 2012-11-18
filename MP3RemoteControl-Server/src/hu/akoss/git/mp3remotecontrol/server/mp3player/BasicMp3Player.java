package hu.akoss.git.mp3remotecontrol.server.mp3player;

import hu.akoss.git.mp3remotecontrol.server.utils.FileHelper;

import java.io.File;
import java.util.Map;


import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class BasicMp3Player implements IMediaPlayer, BasicPlayerListener {

	private String _filename;
	private String lastOperation = "null";
	private BasicPlayer _player = new BasicPlayer();
	
	public BasicMp3Player()
	{
		_player.addBasicPlayerListener(this);
	}
	
	public void play(final String filename) throws Exception
	{
		lastOperation = "play";
		_filename = filename;
		
		Thread playerThread = 
	        new Thread() {
	            public void run() {
	                try 
	                { 
	                	System.err.println("[BasicMp3Player] Play: " + filename);
	                	
	                	if (_player != null)
	                	{
	                		_player.stop();
	                		System.err.println("[BasicMp3Player] Play: stopped playing previous song ");
	                	}
	                	
	                	_player.open(new File(filename));
	                	_player.play();
	                }
	                catch (Exception e) 
	                { 
	                	e.printStackTrace();
	                	System.err.println("[BasicMp3Player] Play: error " + e.getMessage()); 
	                }
	            }
	        };
	    
        playerThread.start();
	}

	@Override
	public void pause() throws Exception {
		lastOperation = "pause";
	}

	@Override
	public void resume() throws Exception {
		lastOperation = "resume";
	}

	@Override
	public void stop() throws Exception {
		lastOperation = "stop";
		synchronized (_player) {
			_player.stop();
		}
	}
	
	private void playNext() throws Exception
	{
		if (lastOperation.equals("stop"))
    	{
    		return;
    	}
    	
    	// Playing has stopped normally (end of song), playing next song ...
    	String nextSongFilename = getNextSongFilename(_filename);
    	if (nextSongFilename != null)
    	{
    		play(nextSongFilename);
    	}
    	else
    	{
    		System.err.println("[BasicMp3Player] No more songs in folder, playing stopped.");
    	}
	}
	
	private String getNextSongFilename(String filename_)
	{
		File file = new File(filename_);
		File folder = null;
		
		try {
			folder = file.getParentFile().getCanonicalFile();
		}
		catch (Exception err_)
		{
			err_.printStackTrace();
			return null;
		}
		
		File[] files = folder.listFiles();
		String nextFile = null;
		System.err.println("[BasicMp3Player] Looking for next file...");
		boolean next = false;
		for (File f : files)
		{
			if (f.isDirectory())
			{
				continue;
			}
			
			System.err.println("[BasicMp3Player] Iteration: " + f.toString());
			String fCanonicalPath = null;
			try 
			{
				fCanonicalPath = f.getCanonicalPath();
			}
			catch (Exception err_)
			{
				err_.printStackTrace();
				continue;
			}
			
			if (!FileHelper.isMp3(filename_))
			{
				continue;
			}
			
			if (next)
			{
				nextFile = fCanonicalPath;
				System.err.println("[BasicMp3Player] Next file found: " + f.toString());
				break;
			}
			
			if (fCanonicalPath.equals(filename_))
			{
				next = true;
			}
		}
		
		return nextFile;
	}

	@Override
	public void opened(Object arg0, Map arg1) {
		
	}

	@Override
	public void progress(int arg0, long arg1, byte[] arg2, Map arg3) {
		
	}

	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent arg0) {
		
		if (_player.getStatus() == BasicPlayer.STOPPED)
		{
			try 
			{
				playNext();	
			}
			catch (Exception err)
			{
				System.err.println("[BasicMp3Player] State Updated, could not play next song - error caught:");
				err.printStackTrace();
			}
		}
	}
}
