package hu.akoss.git.mp3remotecontrol.server.servlets;

import hu.akoss.git.mp3remotecontrol.server.auth.Authorizer;
import hu.akoss.git.mp3remotecontrol.server.auth.AuthorizerException;
import hu.akoss.git.mp3remotecontrol.server.utils.Audio;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Mp3VolumeControl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		PrintWriter out = res.getWriter();

		try {
			Authorizer.authorizeRequest(req);
		} catch (AuthorizerException e) {
			out.print(e.getMessage());
			return;
		}
		
		String action = req.getParameter("action");
		if (action != null && action.equals("setVolume") && req.getParameter("volume") != null)
		{
			try 
			{
				float volume = Float.valueOf(req.getParameter("volume"));
				Audio.setMasterOutputVolume(volume);
				out.write("OK");
				System.err.println("[Mp3VolumeControl] SetVolume: " + volume);
			}
			catch (Exception err)
			{
				out.write(err.getMessage());
			}
			
		}
		else if (action != null && action.equals("getVolume"))
		{
			try 
			{
				out.println(String.format("%f", Audio.getMasterOutputVolume()));
				out.write("OK");
				System.err.println("[Mp3VolumeControl] GetVolume: " + Audio.getMasterOutputVolume());
			}
			catch (Exception err)
			{
				out.write(err.getMessage());
			}
		}
		
		out.close();
	}

}
