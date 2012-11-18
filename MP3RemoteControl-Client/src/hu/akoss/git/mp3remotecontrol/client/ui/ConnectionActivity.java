package hu.akoss.git.mp3remotecontrol.client.ui;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;
import hu.akoss.git.mp3remotecontrol.client.model.ConnectionDetails;
import hu.akoss.git.mp3remotecontrol.client.sqlite.LocalSQLite;
import hu.akoss.git.mp3remotecontrol.client.ui.utils.NotificationProvider;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import hu.akoss.git.mp3control.ui.R;

public class ConnectionActivity extends Activity {

	/**
	 * Ignore connection list selection when drop down control gets populated
	 */
	private boolean ignoreSelection = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.connection);
		populateForm();
	
		Spinner connectionList = (Spinner) findViewById(R.id.connectionList);
		connectionList.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				
				if (ignoreSelection)
				{
					// Drop down is being populated
					return;
				}
				
				String selectedUrl = parent.getItemAtPosition(pos).toString();
				ConnectionDetails connection = readConnectionDetails(selectedUrl);
				if (connection == null)
				{
					return;
				}
				
				populateConnection(connection);
				Settings.setConnection(connection);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		Button clearConnectionListBtn = (Button)findViewById(R.id.clearConnectionListBtn);
		clearConnectionListBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try 
				{
					LocalSQLite.setValue("ConnectionCount", "0");
					populateForm();
				}
				catch (Exception err)
				{
					NotificationProvider.showMessage(err.getMessage());
				}
			}
		});
		
		Button connectBtn = (Button)findViewById(R.id.setConnectionBtn);
		connectBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Fetch connection details from screen
				ConnectionDetails connection = getDisplayedConnection();
				if (connection == null)
				{
					return;
				}
				
				// Store connection in local SQLite
				boolean stored = storeConnectionDetails(connection);
				
				// Call setConnection: invoke fetch from remote machine
				Settings.setConnection(connection);
				
				if (stored)
				{
					// New connection should be populated in list
					populateForm();
				}
			}
		});
	}	
	
	private ConnectionDetails readConnectionDetails(String serviceUrl)
	{
		ConnectionDetails conn = null;
		
		int storedConnectionCount = getConnectionCount();
		for (int i=0; i!=storedConnectionCount; i++)
		{
			String currentServiceUrl = LocalSQLite.getValue(String.format("conn[%d].url", i));
			if (currentServiceUrl != null && currentServiceUrl.equals(serviceUrl))
			{
				conn = new ConnectionDetails();
				conn.setServiceUrl(LocalSQLite.getValue(String.format("conn[%d].url", i)));
				conn.setFolder(LocalSQLite.getValue(String.format("conn[%d].folder", i)));
				conn.setUsername(LocalSQLite.getValue(String.format("conn[%d].user", i)));
				conn.setPassword(LocalSQLite.getValue(String.format("conn[%d].passwd", i)));
				
				break;
			}
		}
		
		return conn;
	}
	
	private boolean storeConnectionDetails(ConnectionDetails connection_)
	{
		if (isConnectionStored(connection_))
		{
			System.err.println("[ConnectionActivity] storeConnectionDetails: already stored.");
			return false;
		}
		
		// Save connection
		int newConnectionIndex = getConnectionCount();
		LocalSQLite.setValue(String.format("conn[%d].url", newConnectionIndex), connection_.getServiceUrl());
		LocalSQLite.setValue(String.format("conn[%d].folder", newConnectionIndex), connection_.getFolder());
		LocalSQLite.setValue(String.format("conn[%d].user", newConnectionIndex), connection_.getUsername());
		LocalSQLite.setValue(String.format("conn[%d].passwd", newConnectionIndex), connection_.getPassword());
		
		newConnectionIndex++;
		LocalSQLite.setValue("ConnectionCount", String.valueOf(newConnectionIndex));
		
		System.err.println("[ConnectionActivity] storeConnectionDetails: new connection stored.");
		
		return true;
	}
	
	private boolean isConnectionStored(ConnectionDetails connection_)
	{
		int connCount = getConnectionCount();
		
		for (int i=0; i!=connCount; i++)
		{
			String urlKey = String.format("conn[%d].url", i);
			if (LocalSQLite.getValue(urlKey) != null 
					&& LocalSQLite.getValue(urlKey).equals(connection_.getServiceUrl()))
			{
				System.err.println("[ConnectionActivity] isConnectionStored: yes.");
				return true;
			}
		}
		
		System.err.println("[ConnectionActivity] isConnectionStored: no.");
		
		return false;
	}
	
	private int getConnectionCount()
	{
		String connCount = LocalSQLite.getValue("ConnectionCount");
		if (connCount == null)
		{
			return 0;
		}
		
		System.err.println("[ConnectionActivity] getConnectionCount: "+Integer.valueOf(connCount)+".");
		
		return Integer.valueOf(connCount);
	}
	
	private void populateConnection(ConnectionDetails connection_)
	{
		((EditText)findViewById(R.id.connHost)).setText(connection_.getServiceUrl());
		((EditText)findViewById(R.id.connFolder)).setText(connection_.getFolder());
		((EditText)findViewById(R.id.connUser)).setText(connection_.getUsername());
		((EditText)findViewById(R.id.connPassword)).setText(connection_.getPassword());
		
		System.err.println("[ConnectionActivity] populateConnection: "+connection_.getServiceUrl()+".");
	}
	
	private void populateForm()
	{
		// Populate ConnectionList drop down
		int connCount = getConnectionCount();
		Spinner connectionList = (Spinner) findViewById(R.id.connectionList);
		List<String> list = new ArrayList<String>();
		
		int selectedIndex = -1;
		for (int i=0; i!=connCount; i++)
		{
			String url = LocalSQLite.getValue(String.format("conn[%d].url", i));
			list.add(url);
			
			if (url.equals(Settings.getConnection().getServiceUrl()))
			{
				selectedIndex = i;
			}
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		ignoreSelection = true;
		connectionList.setAdapter(dataAdapter);
		
		if (selectedIndex != -1)
		{
			connectionList.setSelection(selectedIndex);	
		}
		ignoreSelection = false;
		
		connectionList.setEnabled(connCount > 0);
		Button clearConnectionListBtn = (Button)findViewById(R.id.clearConnectionListBtn);
		clearConnectionListBtn.setEnabled(connCount > 0);
	}
	
	private ConnectionDetails getDisplayedConnection()
	{
		ConnectionDetails connection = new ConnectionDetails();
		connection.setServiceUrl(((EditText)findViewById(R.id.connHost)).getText().toString());
		connection.setFolder(((EditText)findViewById(R.id.connFolder)).getText().toString());
		connection.setUsername(((EditText)findViewById(R.id.connUser)).getText().toString());
		connection.setPassword(((EditText)findViewById(R.id.connPassword)).getText().toString());
		
		return connection;
	}
}
