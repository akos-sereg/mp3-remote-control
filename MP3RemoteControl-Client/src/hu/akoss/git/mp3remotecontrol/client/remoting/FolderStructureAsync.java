package hu.akoss.git.mp3remotecontrol.client.remoting;

import hu.akoss.git.mp3remotecontrol.client.config.Settings;
import hu.akoss.git.mp3remotecontrol.client.model.ConnectionDetails;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.Folder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.os.AsyncTask;


public class FolderStructureAsync extends AsyncTask<ConnectionDetails, Void, Folder> {

	private static String _lastFolder;
	private static List<IFolderStructureAsyncResult> _handlers = new ArrayList<IFolderStructureAsyncResult>();

	public static void subscribe(IFolderStructureAsyncResult handler_) {
		if (_handlers.contains(handler_)) {
			return;
		}

		_handlers.add(handler_);
		_lastFolder = Settings.getConnection().getFolder();
	}

	@Override
	protected Folder doInBackground(ConnectionDetails... args) {

		Proxy proxy = new Proxy();

		if (args.length == 0)
		{
			return null;
		}
		
		ConnectionDetails conn = args[0];
		
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("Path", conn.getFolder());
		if (_lastFolder != null && !_lastFolder.equals(conn.getFolder()))
		{
			params.put("ForceReload", "true");	
		}

		String responseText = proxy.request(Settings.SERVLET_BROWSER, params);
		if (responseText == null) {
			return null;
		}

		try {
			return getRootFolder(responseText);
		} catch (Exception err) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Folder rootFolder_) {
		if (_handlers == null) {
			return;
		}

		for (IFolderStructureAsyncResult handler : _handlers) {
			try {
				handler.onFolderStructureReturned(rootFolder_);
			} catch (Exception err) {
				// Error in handler user code
				System.err.println(err.getMessage());
			}
		}
	}

	private Folder getRootFolder(String folderStructureAsXml_) throws Exception {
		Folder rootFolder = new Folder("RootFolder");
		Document structure = null;

		try {
			structure = getFolderStructureFromXml(folderStructureAsXml_);
			rootFolder.addChildren(structure.getChildNodes(), rootFolder);
		} catch (Exception err) {
			throw new Exception(
					"Folder Structure: Invalid folder structure received from remote service.");
		}

		return rootFolder;
	}

	private Document getFolderStructureFromXml(String folderStructureAsXml_)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(folderStructureAsXml_));

		return db.parse(is);
	}

	private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}
}
