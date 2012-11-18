package hu.akoss.git.mp3remotecontrol.client.ui;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;
import hu.akoss.git.mp3remotecontrol.client.remoting.FolderStructureAsync;
import hu.akoss.git.mp3remotecontrol.client.remoting.IFolderStructureAsyncResult;
import hu.akoss.git.mp3remotecontrol.client.remoting.PlayRemoteMp3Async;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.Folder;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.adapter.FolderAdapter;
import hu.akoss.git.mp3remotecontrol.client.ui.utils.NotificationProvider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BrowserActivity extends Activity implements
		IFolderStructureAsyncResult {
	
	private static BrowserActivity _instance;
	private static Folder _currentFolder;
	public static Folder getCurrentFolder()
	{
		return _currentFolder;
	}
	
	public static BrowserActivity getInstance()
	{
		return _instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_instance = this;
		
		NotificationProvider.context = this;
		setContentView(R.layout.browser);
		
		// Remote event subscriptions
		FolderStructureAsync.subscribe(this);

		// Begin fetching remote folder structure
		new FolderStructureAsync().execute(Settings.getConnection());
	}

	@Override
	public void onFolderStructureReturned(Folder rootFolder_) {

		if (rootFolder_ == null) {
			handleError("Mp3ControlService is currently down, please try again later.");
			return;
		}

		displayFolder(rootFolder_.getChildren().get(1));
		Context.getTabHost().setCurrentTab(0);
	}

	public void displayFolder(Folder folder_) {
		
		if (folder_ == null)
		{
			return;
		}
		
		final ListView list = (ListView) this.findViewById(R.id.folderList);
		list.setAdapter(new FolderAdapter(this, android.R.id.text1, folder_
				.getChildrenAsArray()));
		
		list.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				_currentFolder = (Folder) (list.getItemAtPosition(myItemInt));
				if (_currentFolder.isLevelUp()) {
					_currentFolder = _currentFolder.getParent();
				}

				if (_currentFolder.isFile())
				{
					playSelectedMp3();
				}
				else
				{
					displayFolder(_currentFolder);
				}
			}
		});
	}
	
	private void playSelectedMp3()
	{
		new PlayRemoteMp3Async().execute(_currentFolder);
		NotificationProvider.showMessage("Playing song (req. sent) ...");
	}

	private void handleError(String message_) {
		NotificationProvider.showMessage(message_);
	}
}
