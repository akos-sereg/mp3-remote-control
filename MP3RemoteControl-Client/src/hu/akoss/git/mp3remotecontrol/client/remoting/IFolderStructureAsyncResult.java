package hu.akoss.git.mp3remotecontrol.client.remoting;

import hu.akoss.git.mp3remotecontrol.client.ui.listing.Folder;

public interface IFolderStructureAsyncResult {

	public void onFolderStructureReturned(Folder rootFolder_); 
}
