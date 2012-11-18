package hu.akoss.git.mp3remotecontrol.client.remoting;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;
import hu.akoss.git.mp3remotecontrol.client.model.NowPlayingInfo;
import hu.akoss.git.mp3remotecontrol.client.ui.utils.NotificationProvider;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.os.AsyncTask;


public class GetNowPlayingInfoAsync extends AsyncTask<Object, Void, NowPlayingInfo> {

	private static List<IGetNowPlayingInfoAsyncResult> _handlers = new ArrayList<IGetNowPlayingInfoAsyncResult>();
	
	public static void subscribe(IGetNowPlayingInfoAsyncResult handler)
	{
		if (_handlers.contains(handler))
		{
			return;
		}
		
		_handlers.add(handler);
	}
	
	@Override
	protected NowPlayingInfo doInBackground(Object... args) {

		Proxy proxy = new Proxy();

		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("action", "getNowPlayingInfo");
		
		String responseText = proxy.request(Settings.SERVLET_PLAYER, params);
		
		if (responseText == null) {
			return null;
		}

		return parseResponse(responseText);
	}
	
	private NowPlayingInfo parseResponse(String response_)
	{
		if (!response_.substring(0, 2).equals("OK"))
		{
			NotificationProvider.showMessage(response_);
			return null;
		}
		
		String [] args = response_.split("\n");
		
		System.err.println(response_);
		System.err.println("Volume: " + args[4]);
		
		NowPlayingInfo info = new NowPlayingInfo(args[1]);
		info.setIsPaused(Boolean.valueOf(args[2]));
		info.setIsStopped(Boolean.valueOf(args[3]));
		info.setVolume(Float.valueOf(args[4]) / 100);
		info.setFilename(args[5]);
		
		return info;
	}
	
	@Override
	protected void onPostExecute(NowPlayingInfo nowPlayingInfo_) {
		if (nowPlayingInfo_ == null)
		{
			return;
		}
		
		for (IGetNowPlayingInfoAsyncResult handler : _handlers)
		{
			try 
			{
				handler.nowPlayingInfoReturned(nowPlayingInfo_);	
			}
			catch (Exception err)
			{
				
			}
		}
	}

}
