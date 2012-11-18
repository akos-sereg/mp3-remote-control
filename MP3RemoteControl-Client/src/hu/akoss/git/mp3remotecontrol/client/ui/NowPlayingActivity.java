package hu.akoss.git.mp3remotecontrol.client.ui;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;
import hu.akoss.git.mp3remotecontrol.client.model.NowPlayingInfo;
import hu.akoss.git.mp3remotecontrol.client.remoting.GetNowPlayingInfoAsync;
import hu.akoss.git.mp3remotecontrol.client.remoting.IGetNowPlayingInfoAsyncResult;
import hu.akoss.git.mp3remotecontrol.client.remoting.ISetVolumeAsyncResult;
import hu.akoss.git.mp3remotecontrol.client.remoting.PauseRemoteMp3Async;
import hu.akoss.git.mp3remotecontrol.client.remoting.PlayRemoteMp3Async;
import hu.akoss.git.mp3remotecontrol.client.remoting.RemoteConfig;
import hu.akoss.git.mp3remotecontrol.client.remoting.SetVolumeAsync;
import hu.akoss.git.mp3remotecontrol.client.remoting.StopRemoteMp3Async;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.Folder;
import hu.akoss.git.mp3remotecontrol.client.utils.HtmlEntities;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import hu.akoss.git.mp3control.ui.R;

public class NowPlayingActivity extends Activity implements ISetVolumeAsyncResult, IGetNowPlayingInfoAsyncResult {
	
	private Folder _songFolder;
	
	public static NowPlayingActivity Instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Instance = this;
		setContentView(R.layout.now_playing);
		SetVolumeAsync.subscribe(this);
		GetNowPlayingInfoAsync.subscribe(this);
		
		Button pauseBtn = (Button)findViewById(R.id.pauseBtn);
		pauseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Context.getNowPlaying() != null && Context.getNowPlaying().isPaused())
				{
					new PlayRemoteMp3Async().execute();
				}
				else
				{
					new PauseRemoteMp3Async().execute();	
				}
			}
		});
		
		Button stopBtn = (Button)findViewById(R.id.stopBtn);
		stopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new StopRemoteMp3Async().execute();
			}
		});
		
		Button goToFolderBtn = (Button)findViewById(R.id.goToFolderBtn);
		goToFolderBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BrowserActivity.getInstance().displayFolder(_songFolder.getParent());
				Context.getTabHost().setCurrentTab(0);
			}
		});

		
		Button volumePlusBtn = (Button)findViewById(R.id.volumePlus);
		volumePlusBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Float newColume = RemoteConfig.Volume + Settings.VOLUME_SMALLEST_UNIT; 
				
				if (newColume > 1.0f)
				{
					newColume = 1.0f;
				}
				
				new SetVolumeAsync().execute(newColume);
			}
		});
		
		Button volumeMinusBtn = (Button)findViewById(R.id.volumeMinus);
		volumeMinusBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Float newColume = RemoteConfig.Volume - Settings.VOLUME_SMALLEST_UNIT; 
				
				if (newColume < 0.0f)
				{
					newColume = 0.0f;
				}
				
				new SetVolumeAsync().execute(newColume);
			}
		});
		
//		Button playBtn = (Button)findViewById(R.id.playBtn);
//		playBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (Context.getNowPlaying() != null && Context.getNowPlaying().isPaused())
//				{
//					new PlayRemoteMp3Async().execute();	
//				}
//				else
//				{
//					new PlayRemoteMp3Async().execute(new Folder(Context.getNowPlaying().getFullPath()));
//				}
//			}
//		});
		
		new GetNowPlayingInfoAsync().execute();
	}

	public void setNowPlaying(NowPlayingInfo info_)
	{
		TextView nowPlayingMp3 = (TextView)findViewById(R.id.nowPlayingMp3);
		if (info_.getFullPath() == null)
		{
			nowPlayingMp3.setText("N/A");
		}
		else
		{
			nowPlayingMp3.setText(HtmlEntities.toPlain(info_.getFilename()));
			_songFolder = BrowserActivity.getCurrentFolder();
		}
	}

	@Override
	public void volumeSet(float volume_) {
		ProgressBar progressBar = (ProgressBar)findViewById(R.id.volumeProgressBar);
		progressBar.setMax(100);
		
		int progress = (int)(volume_ * 100);
		progressBar.setProgress(progress);
	}

	@Override
	public void nowPlayingInfoReturned(NowPlayingInfo info_) {
		RemoteConfig.Volume = info_.getVolume();
		volumeSet(info_.getVolume());
		setNowPlaying(info_);
		
		if (info_.getFilename() != null)
		{
			Context.getTabHost().setCurrentTab(1);	
		}
	}
}
