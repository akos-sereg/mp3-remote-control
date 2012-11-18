package hu.akoss.git.mp3remotecontrol.server.servlets;

import hu.akoss.git.mp3remotecontrol.server.auth.Authorizer;
import hu.akoss.git.mp3remotecontrol.server.auth.AuthorizerException;
import hu.akoss.git.mp3remotecontrol.server.mp3player.BasicMp3Player;
import hu.akoss.git.mp3remotecontrol.server.mp3player.IMediaPlayer;
import hu.akoss.git.mp3remotecontrol.server.mp3player.JavaxSampled;
import hu.akoss.git.mp3remotecontrol.server.mp3player.JavazoomPlayer;
import hu.akoss.git.mp3remotecontrol.server.mp3player.JmfPlayer;
import hu.akoss.git.mp3remotecontrol.server.mp3player.MidiPlayer;
import hu.akoss.git.mp3remotecontrol.server.mp3player.SystemPlayer;
import hu.akoss.git.mp3remotecontrol.server.utils.Audio;
import hu.akoss.git.mp3remotecontrol.server.utils.HtmlEntities;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 127.0.0.1:8080/mp3control-srvc/mp3player?action=play
 * 
 * Parameters:
 *  - action: play|stop
 *  - Path: <file path>
 * 
 * @author sigterm
 *
 */
public class Mp3Player extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	//private static IMediaPlayer _player = new JavazoomPlayer();
	//private static IMediaPlayer _player = new JmfPlayer();
	//private static IMediaPlayer _player = new SystemPlayer();
	//private static IMediaPlayer _player = new JavaxSampled();
	private static IMediaPlayer _player = new BasicMp3Player();
	//private static IMediaPlayer _player = new MidiPlayer();
	private static String _currentlyPlaying = null;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		
		PrintWriter out = res.getWriter();
		
		try {
			Authorizer.authorizeRequest(req);
		} catch (AuthorizerException e) {
			out.print(e.getMessage());
			out.close();
			return;
		}
		
		String action = req.getParameter("action");
		
		if (action.equals("play"))
		{
			try {
				_currentlyPlaying = HtmlEntities.toPlain(req.getParameter("Path"));
				_player.play(_currentlyPlaying);
				out.write("OK");
			}
			catch (Exception err)
			{
				err.printStackTrace();
				out.write(err.getMessage());
			}
		}
		else if (action.equals("stop"))
		{
			try {
				_player.stop();
				out.write("OK");
			}
			catch (Exception err)
			{
				err.printStackTrace();
				out.write(err.getMessage());
			}
		}
		else if (action.equals("pause"))
		{
			try {
				_player.pause();
				out.write("OK");
			}
			catch (Exception err)
			{
				err.printStackTrace();
				out.write(err.getMessage());
			}
		}
		else if (action.equals("getNowPlayingInfo"))
		{
			try {
				out.write("OK\n");
				out.write(_currentlyPlaying + "\n");
				out.write("false\n");
				out.write("false\n");
				
				int volume = (int)(Audio.getMasterOutputVolume() * 100);
				out.write(String.valueOf(volume) + "\n");
				
				if (_currentlyPlaying == null)
				{
					out.write("N/A");
				}
				else
				{
					File currentlyPlaying = new File(_currentlyPlaying);
					out.write(currentlyPlaying.getName());	
				}
			}
			catch (Exception err)
			{
				err.printStackTrace();
				out.write(err.getMessage());
			}
		}
		
		out.close();
	}
}