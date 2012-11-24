package hu.akoss.git.mp3remotecontrol.client.remoting;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class Proxy {

	public String request(String service_, Hashtable<String, String> params_) 
	{
		if (params_ == null)
		{
			params_ = new Hashtable<String, String>();
		}
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		
		String url = String.format("http://%s/MP3ControlService/%s", 
				Settings.getConnection().getServiceUrl(), 
				service_);
		
		params_.put("Username", Settings.getConnection().getUsername());
		params_.put("Password", Settings.getConnection().getPassword());
		
		try {
			//HttpPost httpGet = new HttpPost(url);
			HttpPost httppost = new HttpPost(url);
			
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params_.size());
		 
		    // Add two POST request variables
		    for (Entry<String, String> entry : params_.entrySet())
		    {
		    	nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));	
		    }
		    
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		 
		    // Execute HTTP Post Request
		    HttpResponse response = httpclient.execute(httppost);
		    HttpEntity entity = response.getEntity();
		 
		    return EntityUtils.toString(entity);
		} 
		catch (UnsupportedEncodingException err)
		{
			return null;
		}
		catch (ClientProtocolException e) 
		{
			return null;
		} 
		catch (IOException e) 
		{
			return null;
		}
	}
}
