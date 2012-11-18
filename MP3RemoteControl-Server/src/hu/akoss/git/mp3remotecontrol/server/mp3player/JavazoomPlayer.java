package hu.akoss.git.mp3remotecontrol.server.mp3player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class JavazoomPlayer implements IMediaPlayer {

	private String _filename;
    private Player player; 
    private Thread playerThread;
    private String lastOperation = "null";
    
	@Override
	public void play(final String filename) throws Exception {
		
		_filename = filename;
		
		if (player != null)
		{
			synchronized (player) {
				player.close();
			}
		}
		
        FileInputStream fis     = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis);
        player = new Player(bis);

        // run in new thread to play in background
        playerThread = 
	        new Thread() {
	            public void run() {
	                try 
	                { 
	                	lastOperation = "play";
	                	
	                	System.err.println("[JavazoomPlayer] Play: " + filename);
	                	player.play();	
	                	
	                	System.err.println("[JavazoomPlayer] Play: stopped playing " + filename);
	                	
	                	playNext();
	                }
	                catch (Exception e) 
	                { 
	                	e.printStackTrace();
	                	System.err.println("[JavazoomPlayer] Play: error " + e.getMessage());
	                	try {
							playNext();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
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
		
		if (player != null)
		{
			synchronized (player) {
				player.close();
				System.err.println("[JavazoomPlayer] Stop: " + _filename);
			}
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
    		System.err.println("[JavazoomPlayer] No more songs in folder, playing stopped.");
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
		System.err.println("[JavazoomPlayer] Looking for next file...");
		boolean next = false;
		for (File f : files)
		{
			System.err.println("[JavazoomPlayer] Iteration: " + f.toString());
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
			
			if (next)
			{
				nextFile = fCanonicalPath;
				System.err.println("[JavazoomPlayer] Next file found: " + f.toString());
				break;
			}
			
			if (fCanonicalPath.equals(filename_))
			{
				next = true;
			}
		}
		
		return nextFile;
	}

}
