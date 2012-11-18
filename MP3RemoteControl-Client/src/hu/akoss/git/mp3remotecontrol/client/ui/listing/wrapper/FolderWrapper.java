package hu.akoss.git.mp3remotecontrol.client.ui.listing.wrapper;

import hu.akoss.git.mp3remotecontrol.client.ui.R;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.Folder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderWrapper {

	private View _row;
	
	public FolderWrapper(View view_)
	{
		_row = view_;
	}
	
	public void populateFrom(Folder folder_)
	{
		getFolderNameLabel().setText(
				String.format("%s", folder_.getDisplayName()));
		
		ImageView icon = (ImageView)_row.findViewById(R.id.folderImg);
		int iconId = R.drawable.folder;
		
		if (folder_.isLevelUp())
		{
			iconId = R.drawable.back_folder;
		}
		
		if (folder_.isFile())
		{
			if (folder_.getExtension().toLowerCase().equals("mp3"))
			{
				iconId = R.drawable.winamp_icon;	
			}
			else
			{
				iconId = R.drawable.social_empty;
			}
		}
		
		icon.setBackgroundResource(iconId);
	}

	private TextView getFolderNameLabel()
	{
		return (TextView)_row.findViewById(R.id.folderNameLabel);
	}
	
}