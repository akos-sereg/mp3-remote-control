package hu.akoss.git.mp3remotecontrol.client.ui.utils;

import android.content.Context;
import android.widget.Toast;

public class NotificationProvider {

	public static Context context;
	
	public static void showMessage(String message_)
	{
		if (context == null)
		{
			return;
		}
		
		try 
		{
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, message_, duration);
			toast.show();	
		}
		catch (Exception err)
		{
			
		}
	}
	
}
