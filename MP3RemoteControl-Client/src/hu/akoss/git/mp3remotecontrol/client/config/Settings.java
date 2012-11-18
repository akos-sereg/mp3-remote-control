package hu.akoss.git.mp3remotecontrol.client.config;

import hu.akoss.git.mp3remotecontrol.client.model.ConnectionDetails;
import hu.akoss.git.mp3remotecontrol.client.remoting.FolderStructureAsync;

public class Settings {

	private static ConnectionDetails _connection;
	static {
		_connection = new ConnectionDetails();
		_connection.setServiceUrl("192.168.2.100:8080");
		_connection.setFolder("E:\\mp3\\");
		_connection.setUsername("akoss");
		_connection.setPassword("sajt");
	}
	
	public static ConnectionDetails getConnection()
	{
		return _connection;
	}
	
	public static void setConnection(ConnectionDetails connection_)
	{
		_connection = connection_;
		new FolderStructureAsync().execute(_connection);
	}
	
//	public static String SERVICE_URL = "192.168.2.100:8080";
//	public static String SERVICE_FOLDER = "E:\\mp3\\";
//	public static String SERVICE_USERNAME = "akos";
//	public static String SERVICE_PASSWORD = "sajt";
	
//	public static String SERVICE_URL = "192.168.2.78:8085";
//	public static String SERVICE_FOLDER = "/media/samsung/mp3";
//	public static String SERVICE_USERNAME = "akos";
//	public static String SERVICE_PASSWORD = "sajt";
	
	public static final String SERVLET_PLAYER = "mp3player";
	public static final String SERVLET_BROWSER = "mp3browser";
	public static final String SERVLET_VOLUME_CONTROL = "mp3volumecontrol";
	public static final float VOLUME_SMALLEST_UNIT = 0.02f;
}
