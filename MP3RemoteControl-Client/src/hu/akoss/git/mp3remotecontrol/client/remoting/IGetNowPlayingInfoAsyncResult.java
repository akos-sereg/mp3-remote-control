package hu.akoss.git.mp3remotecontrol.client.remoting;

import hu.akoss.git.mp3remotecontrol.client.model.NowPlayingInfo;

public interface IGetNowPlayingInfoAsyncResult {

	void nowPlayingInfoReturned(NowPlayingInfo info_);
}
