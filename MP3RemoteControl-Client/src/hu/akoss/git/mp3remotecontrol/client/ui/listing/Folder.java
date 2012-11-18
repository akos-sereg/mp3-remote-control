package hu.akoss.git.mp3remotecontrol.client.ui.listing;

import hu.akoss.git.mp3remotecontrol.client.utils.HtmlEntities;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Folder {

	private Folder _parent;
	private String _displayName;
	private boolean _isLevelUp;
	private boolean _isFile;
	private String _name;
	private String _fullPath;
	private List<Folder> _children = new ArrayList<Folder>();
	
	public boolean isFile() {
		return _isFile;
	}
	public void setIsFile(boolean _isFile) {
		this._isFile = _isFile;
	}

	public Folder getParent() {
		return _parent;
	}
	public void setParent(Folder _parent) {
		this._parent = _parent;
	}
	public String getDisplayName() {
		return _displayName;
	}
	public void setDisplayName(String _displayName) {
		this._displayName = _displayName;
	}
	public boolean isLevelUp() {
		return _isLevelUp;
	}
	public void setIsLevelUp(boolean _isLevelUp) {
		this._isLevelUp = _isLevelUp;
	}
	public List<Folder> getChildren() {
		return _children;
	}
	public void setChildren(List<Folder> _children) {
		this._children = _children;
	}
	public String getName() {
		return _name;
	}
	public void setName(String _name) {
		this._name = _name;
	}
	public String getFullPath() {
		return _fullPath;
	}
	public void setFullPath(String _fullPath) {
		this._fullPath = _fullPath;
	}
	
	public Folder(String name_)
	{
		_name = name_;
	}
	
	public Folder(Node node_)
	{
		if (node_ != null
				&& node_.getAttributes() != null 
				&& node_.getAttributes().getNamedItem("name") != null)
		{
			_displayName = HtmlEntities.toPlain(node_.getAttributes().getNamedItem("name").getNodeValue()); 
			_name = node_.getAttributes().getNamedItem("name").getNodeValue();
		}
		else
		{
			_name = "undefined";
		}
		
		if (node_ != null
				&& node_.getAttributes() != null 
				&& node_.getAttributes().getNamedItem("fullpath") != null)
		{
			_fullPath = node_.getAttributes().getNamedItem("fullpath").getNodeValue();	
		}
		else
		{
			_fullPath = "undefined";
		}
	}
	
	public String getExtension()
	{
		String ext = null;
		String s = _name;
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1)
		ext = s.substring(i+1).toLowerCase();

		if(ext == null)	return "";
		return ext;
	}
	
	public void addChildren(NodeList children_, Folder parent_)
	{
		if (children_ == null)
		{
			return;	
		}
	
		Folder folderUp = new Folder("..");
		folderUp.setDisplayName("..");
		folderUp.setIsLevelUp(true);
		folderUp.setParent(parent_);
		_children.add(folderUp);
		
		for (int i=0; i!=children_.getLength(); i++)
		{
			Node node = children_.item(i);
			Folder child = new Folder(node);
			child.setIsFile(node.getNodeName().toLowerCase().equals("file"));
			child.addChildren(node.getChildNodes(), this);
			child.setParent(this);
			_children.add(child);
		}
	}
	
	public Folder [] getChildrenAsArray()
	{
		Folder [] children = new Folder[_children.size()];
		
		int i = 0;
		for (Folder folder : _children)
		{
			children[i] = folder; 
			i++;
		}
		
		return children;
	}
}
