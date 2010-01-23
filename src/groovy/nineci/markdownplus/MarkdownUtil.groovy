/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus
 */
package nineci.markdownplus;

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * a bunch of statics to help with processing.
 * Some of the replace and delete may not be needed since its groovy but they are copied from markdownj
 * 
 * @author Joshua Burnett
 *
 */
class MarkdownUtil {
	static String replaceAll(String text, String regex, String replacement) {
		MarkdownText ed = new MarkdownText(text);
		ed.replaceAll(regex, replacement);
		return ed.toString();
	}


	/**
	 * lowercase, replace all spaces with _, remove all non alpha-numeric except -,. and _
	 * @param the text to process into an id
	 * @return the string id
	 */
	static String makeId(String text){
		//replace all spaces with underscores
		//and keep any alphanumerics as well as -,.,_
		def s = text.toLowerCase().trim().replaceAll("[ ]", "_").toLowerCase()
		return s.replaceAll(/[^a-zA-Z0-9\.\_\-]/, "")
	}
	
	public static String decodeHTML(String html) {

		html = html.replaceAll(/&#(\d+);/){all,charDecimal->
			char ch = (char) Integer.parseInt(charDecimal)
			return Character.toString(ch)
		}
		
		html = html.replaceAll(/&#x([0-9a-fA-F]+);/){all,charHex->
			char ch = (char) Integer.parseInt(charHex, 16)
			return Character.toString(ch)
		}

		return html
	}

}
