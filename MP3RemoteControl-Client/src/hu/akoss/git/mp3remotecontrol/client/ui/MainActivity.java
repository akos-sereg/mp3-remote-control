package hu.akoss.git.mp3remotecontrol.client.ui;

import hu.akoss.git.mp3remotecontrol.client.sqlite.LocalSQLite;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        TabHost tabHost = getTabHost();
        
        LocalSQLite.Instance = new LocalSQLite(this);
 
        // Tab for Browsing
        TabSpec browse = tabHost.newTabSpec("Browse");
        browse.setIndicator("Browse", getResources().getDrawable(R.drawable.folder_explore));
        Intent browseIntent = new Intent(this, BrowserActivity.class);
        browse.setContent(browseIntent);
 
        // Tab for "Now Playing"
        TabSpec nowPlaying = tabHost.newTabSpec("Now Playing");
        nowPlaying.setIndicator("Now Playing", getResources().getDrawable(R.drawable.sound));
        Intent nowPlayingIntent = new Intent(this, NowPlayingActivity.class);
        nowPlaying.setContent(nowPlayingIntent);
        
        // Tab for "Settings"
        TabSpec settings = tabHost.newTabSpec("Connection");
        settings.setIndicator("Connection", getResources().getDrawable(R.drawable.pc));
        Intent settingsIntent = new Intent(this, ConnectionActivity.class);
        settings.setContent(settingsIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(browse); // Adding photos tab
        tabHost.addTab(nowPlaying); // Adding videos tab
        tabHost.addTab(settings); // Adding videos tab
        Context.setTabHost(tabHost);
    }
}