package hu.akoss.git.mp3remotecontrol.server.auth;

import hu.akoss.git.mp3remotecontrol.server.config.Settings;

import javax.servlet.http.HttpServletRequest;


public class Authorizer {

	public static void authorizeRequest(HttpServletRequest request_) throws AuthorizerException
	{
		if (request_.getParameter("Username") == null ||
				request_.getParameter("Password") == null)
		{
			throw new AuthorizerException("User not authorized.");
		}
		
		if (!request_.getParameter("Username").equals(Settings.SERVICE_USERNAME) ||
				!request_.getParameter("Password").equals(Settings.SERVICE_PASSWORD))
		{
			throw new AuthorizerException("User not authorized.");
		}
	}
}
