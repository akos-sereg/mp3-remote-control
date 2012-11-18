package hu.akoss.git.mp3remotecontrol.client.remoting;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;
import hu.akoss.git.mp3remotecontrol.client.ui.utils.NotificationProvider;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.os.AsyncTask;


public class SetVolumeAsync extends AsyncTask<Float, Void, String> {
	
	private Float _requestedVolume;
	private static List<ISetVolumeAsyncResult> _handlers = new ArrayList<ISetVolumeAsyncResult>();
	
	public static void subscribe(ISetVolumeAsyncResult handler)
	{
		if (_handlers.contains(handler))
		{
			return;
		}
		
		_handlers.add(handler);
	}
	
	@Override
	protected String doInBackground(Float... args) {
		
		if (args.length == 0)
		{
			return null;
		}
		
		_requestedVolume = args[0];
		Proxy proxy = new Proxy();

		Hashtable<String, String> param = new Hashtable<String, String>();
		param.put("action", "setVolume");
		param.put("volume", String.valueOf(args[0]));

		String responseText = proxy.request(Settings.SERVLET_VOLUME_CONTROL, param);

		return responseText;
	}
	
	@Override
	protected void onPostExecute(String message_)
	{
		if (message_ == null || message_.equals("OK"))
		{
			RemoteConfig.Volume = _requestedVolume;
			
			for (ISetVolumeAsyncResult handler : _handlers)
			{
				try 
				{
					handler.volumeSet(RemoteConfig.Volume);
				}
				catch (Exception err_)
				{
					
				}
			}
			
			return;
		}
		
		NotificationProvider.showMessage(message_);
	}


}
