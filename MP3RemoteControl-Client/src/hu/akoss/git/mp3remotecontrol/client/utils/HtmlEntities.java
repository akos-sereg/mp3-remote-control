package hu.akoss.git.mp3remotecontrol.client.utils;

public class HtmlEntities {

	public static String quote(String text_)
	{
		if (text_ == null) return "";
		
		String quoted = new String(text_);
		quoted = quoted.replace("í", ":iacute:");
		quoted = quoted.replace("é", ":eacute:");
		quoted = quoted.replace("á", ":aacute:");
		quoted = quoted.replace("û", ":utilde:");
		quoted = quoted.replace("õ", ":otilde:");
		quoted = quoted.replace("ú", ":uacute:");
		quoted = quoted.replace("ó", ":oacute:");
		quoted = quoted.replace("ü", ":uuml:");
		quoted = quoted.replace("ö", ":ouml:");
		
		quoted = quoted.replace("Í", ":Iacute:");
		quoted = quoted.replace("É", ":Eacute:");
		quoted = quoted.replace("Á", ":Aacute:");
		quoted = quoted.replace("Û", ":Utilde:");
		quoted = quoted.replace("Õ", ":Otilde:");
		quoted = quoted.replace("Ú", ":Uacute:");
		quoted = quoted.replace("Ó", ":Oacute:");
		quoted = quoted.replace("Ü", ":Uuml:");
		quoted = quoted.replace("Ö", ":Ouml:");
		
		return quoted;
	}
	
	public static String toPlain(String text_)
	{
		if (text_ == null) return "";
		
		String quoted = new String(text_);
		quoted = quoted.replace(":iacute:", "í");
		quoted = quoted.replace(":eacute:", "é");
		quoted = quoted.replace(":aacute:", "á");
		quoted = quoted.replace(":utilde:", "û");
		quoted = quoted.replace(":otilde:", "õ");
		quoted = quoted.replace(":uacute:", "ú");
		quoted = quoted.replace(":oacute:", "ó");
		quoted = quoted.replace(":uuml:", "ü");
		quoted = quoted.replace(":ouml:", "Ö");
		
		quoted = quoted.replace(":Iacute:", "Í");
		quoted = quoted.replace(":Eacute:", "É");
		quoted = quoted.replace(":Aacute:", "Á");
		quoted = quoted.replace(":Utilde:", "Û");
		quoted = quoted.replace(":Otilde:", "Õ");
		quoted = quoted.replace(":Uacute:", "Ú");
		quoted = quoted.replace(":Oacute:", "Ó");
		quoted = quoted.replace(":Uuml:", "Ü");
		quoted = quoted.replace(":Ouml:", "Ö");
		
		return quoted;
	}
	
}
