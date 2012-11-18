package hu.akoss.git.mp3remotecontrol.client.utils;

public class HtmlEntities {

	public static String quote(String text_)
	{
		if (text_ == null) return "";
		
		String quoted = new String(text_);
		quoted = quoted.replace("�", ":iacute:");
		quoted = quoted.replace("�", ":eacute:");
		quoted = quoted.replace("�", ":aacute:");
		quoted = quoted.replace("�", ":utilde:");
		quoted = quoted.replace("�", ":otilde:");
		quoted = quoted.replace("�", ":uacute:");
		quoted = quoted.replace("�", ":oacute:");
		quoted = quoted.replace("�", ":uuml:");
		quoted = quoted.replace("�", ":ouml:");
		
		quoted = quoted.replace("�", ":Iacute:");
		quoted = quoted.replace("�", ":Eacute:");
		quoted = quoted.replace("�", ":Aacute:");
		quoted = quoted.replace("�", ":Utilde:");
		quoted = quoted.replace("�", ":Otilde:");
		quoted = quoted.replace("�", ":Uacute:");
		quoted = quoted.replace("�", ":Oacute:");
		quoted = quoted.replace("�", ":Uuml:");
		quoted = quoted.replace("�", ":Ouml:");
		
		return quoted;
	}
	
	public static String toPlain(String text_)
	{
		if (text_ == null) return "";
		
		String quoted = new String(text_);
		quoted = quoted.replace(":iacute:", "�");
		quoted = quoted.replace(":eacute:", "�");
		quoted = quoted.replace(":aacute:", "�");
		quoted = quoted.replace(":utilde:", "�");
		quoted = quoted.replace(":otilde:", "�");
		quoted = quoted.replace(":uacute:", "�");
		quoted = quoted.replace(":oacute:", "�");
		quoted = quoted.replace(":uuml:", "�");
		quoted = quoted.replace(":ouml:", "�");
		
		quoted = quoted.replace(":Iacute:", "�");
		quoted = quoted.replace(":Eacute:", "�");
		quoted = quoted.replace(":Aacute:", "�");
		quoted = quoted.replace(":Utilde:", "�");
		quoted = quoted.replace(":Otilde:", "�");
		quoted = quoted.replace(":Uacute:", "�");
		quoted = quoted.replace(":Oacute:", "�");
		quoted = quoted.replace(":Uuml:", "�");
		quoted = quoted.replace(":Ouml:", "�");
		
		return quoted;
	}
	
}
