package hu.akoss.git.mp3remotecontrol.server.utils;

public class FileHelper {

	public static boolean isMp3(String filename_)
	{
		if(filename_ == null)
		{
			return false;
		}
		
		return filename_.toLowerCase().contains(".mp3");
		
	}
}
