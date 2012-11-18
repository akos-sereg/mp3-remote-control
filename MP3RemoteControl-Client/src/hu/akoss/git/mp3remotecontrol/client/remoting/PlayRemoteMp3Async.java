package hu.akoss.git.mp3remotecontrol.client.remoting;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;
import hu.akoss.git.mp3remotecontrol.client.ui.Context;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.Folder;
import hu.akoss.git.mp3remotecontrol.client.ui.utils.NotificationProvider;

import java.util.Hashtable;

import android.os.AsyncTask;


public class PlayRemoteMp3Async extends AsyncTask<Folder, Void, String> {

	private Folder _folder;
	
	@Override
	protected String doInBackground(Folder... params) {
		
		_folder = params.length > 0 ? params[0] : null;
		Proxy proxy = new Proxy();

		Hashtable<String, String> param = new Hashtable<String, String>();
		if (_folder != null)
		{
			param.put("Path", params[0].getFullPath());	
		}
		
		param.put("action", "play");

		String responseText = proxy.request(Settings.SERVLET_PLAYER, param);

		return responseText;
	}
	
	@Override
	protected void onPostExecute(String message_) 
	{
		if (message_ == null)
		{
			return;
		}
		
		if (message_.equals("OK"))
		{
			//Context.getTabHost().setCurrentTab(1);
			new GetNowPlayingInfoAsync().execute();
			
			return;
		}
		
		NotificationProvider.showMessage("Oops, server says: " + message_);
	}

}
