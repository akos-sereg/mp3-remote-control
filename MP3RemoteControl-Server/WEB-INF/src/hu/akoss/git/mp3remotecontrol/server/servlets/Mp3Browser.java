package hu.akoss.git.mp3remotecontrol.server.servlets;

import hu.akoss.git.mp3remotecontrol.server.auth.Authorizer;
import hu.akoss.git.mp3remotecontrol.server.auth.AuthorizerException;
import hu.akoss.git.mp3remotecontrol.server.utils.FileHelper;
import hu.akoss.git.mp3remotecontrol.server.utils.HtmlEntities;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * http://127.0.0.1:8080/mp3control-srvc/mp3browser
 * 
 * Parameters:
 *  - ForceReload: avoid cache
 *  - Path: folder to be listed (ex.: E:\mp3\) 
 *  
 * @author sigterm
 *
 */
public class Mp3Browser extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static String _cache = null;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		PrintWriter out = res.getWriter();
		
		System.err.println("[Mp3Browser] Request: " + req.toString() + ", From: " + req.getRemoteAddr() + ", " + req.getRemoteHost());
		
		try {
			Authorizer.authorizeRequest(req);
		} catch (AuthorizerException e) {
			out.print(e.getMessage());
			out.close();
			return;
		}
		
		try 
		{
			String structureAsXml = _cache;
			
			if (_cache == null 
					|| (req.getParameter("ForceReload") != null 
							&& req.getParameter("ForceReload").equals("true")))
			{
				structureAsXml = displayIt(new File(req.getParameter("Path")), new StringBuilder());
				_cache = structureAsXml;
				
				System.err.println("[Mp3Browser] Browse: folder structure xml built: " + structureAsXml.length() + " chars.");
			}
			else
			{
				System.err.println("[Mp3Browser] Browse: folder structure from cache: " + structureAsXml.length() + " chars.");	
			}
			
			out.append(structureAsXml);
		}
		catch (Exception err)
		{
			System.err.println("[Mp3Browser] Browse error: " + err.getMessage());
		}
		
		out.close();

	}
	
	public static String displayIt(File node, StringBuilder sb){
		
		if (node.isFile())
		{
			System.err.println(node.getAbsolutePath());
			sb.append(String.format("<file fullpath=\"%s\" name=\"%s\">", 
					HtmlEntities.quote(node.getAbsolutePath().replace("&", "&amp;")),
					HtmlEntities.quote(node.getName().replace("&", "&amp;"))));
		}
		
		if (node.isDirectory())
		{
			sb.append(String.format("<folder fullpath=\"%s\" name=\"%s\">", 
					HtmlEntities.quote(node.getAbsolutePath().replace("&", "&amp;")),
					HtmlEntities.quote(node.getName().replace("&", "&amp;"))));
		}

		if(node.isDirectory()){
			//String[] subNote = node.list();
			String [] orderedSubNote = getOrderedNodes(node.list());
			for(String filename : orderedSubNote){
				displayIt(new File(node, filename), sb);
			}
		}
 
		if (node.isDirectory())
		{
			sb.append("</folder>");
		}
		
		if (node.isFile())
		{
			sb.append("</file>");
		}
		
		return sb.toString();
	}

	private static String [] getOrderedNodes(String [] nodes)
	{
		TreeSet<String> listMp3 = new TreeSet<String>();
		TreeSet<String> listDir = new TreeSet<String>();
		TreeSet<String> listOther = new TreeSet<String>();
		
		for (int i=0; i!=nodes.length; i++)
		{
			File file = new File(nodes[i]);
			if (!file.isDirectory() && !FileHelper.isMp3(nodes[i]))
			{
				listOther.add(nodes[i]);
			}
			else if (!file.isDirectory() && FileHelper.isMp3(nodes[i]))
			{
				listOther.add(nodes[i]);
			}
			else if (file.isDirectory())
			{
				listMp3.add(nodes[i]);
			}
		}
		
		// Prepare return
//		Collections.sort(listDir);
//		Collections.sort(listMp3);
//		Collections.sort(listOther);
		
		ArrayList<String> retval = new ArrayList<String>();
		retval.addAll(listDir);
		retval.addAll(listMp3);
		retval.addAll(listOther);
		
		String [] retvalArr = new String[retval.size()];
		
		Iterator<String> it= retval.iterator();

		int cnt=0;
        while(it.hasNext())
        {
          String value=(String)it.next();

          retvalArr[cnt] = value;
          cnt++;
        }
		
		return retvalArr;
	}
}