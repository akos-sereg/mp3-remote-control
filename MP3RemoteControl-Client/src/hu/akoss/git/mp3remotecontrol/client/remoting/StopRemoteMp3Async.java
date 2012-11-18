package hu.akoss.git.mp3remotecontrol.client.remoting;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;
import hu.akoss.git.mp3remotecontrol.client.ui.Context;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.Folder;
import hu.akoss.git.mp3remotecontrol.client.ui.utils.NotificationProvider;

import java.util.Hashtable;

import android.os.AsyncTask;


public class StopRemoteMp3Async extends AsyncTask<Folder, Void, String> {

	@Override
	protected String doInBackground(Folder... arg0) {
		Proxy proxy = new Proxy();

		Hashtable<String, String> param = new Hashtable<String, String>();
		param.put("action", "stop");

		String responseText = proxy.request(Settings.SERVLET_PLAYER, param);

		return responseText;
	}
	
	@Override
	protected void onPostExecute(String message_)
	{
		if (message_ == null || message_.equals("OK"))
		{
			if (Context.getNowPlaying() != null) 
			{
				Context.getNowPlaying().setIsStopped(true);
				Context.getNowPlaying().setIsPaused(false);
			}
			
			return;
		}
		
		NotificationProvider.showMessage(message_);
	}

}
