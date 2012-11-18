package hu.akoss.git.mp3remotecontrol.client.ui;

import hu.akoss.git.mp3remotecontrol.client.model.NowPlayingInfo;

import android.widget.TabHost;

public class Context {

	private static TabHost _tabHost;
	private static NowPlayingInfo _nowPlaying;
	
	public static NowPlayingInfo getNowPlaying() {
		return _nowPlaying;
	}

	public static void setNowPlaying(NowPlayingInfo _nowPlaying) {
		Context._nowPlaying = _nowPlaying;
	}

	public static void setTabHost(TabHost tabHost_)
	{
		_tabHost = tabHost_;
	}
	
	public static TabHost getTabHost()
	{
		return _tabHost;
	}
}
