/*
much of this came from markdownj.

Copyright (c) 2010 9ci Inc
Authors: Joshua Burnett
http://www.9ci.com/markdownplus

Original Copyright (c) 2005, Martian Software
Authors: Pete Bevin, John Mutchek
http://www.martiansoftware.com/markdownj

*/

package nineci.markdownplus;

import java.util.regex.Matcher
import java.util.regex.Pattern

class CodeBlockParser {
	MarkdownPlus markdownPlus
	String LANG_IDENTIFIER = "lang:";
	
	MarkdownText parse(MarkdownText markup) {
		Pattern p = Pattern.compile("" +
		"(?:\\n\\n|\\A)" +
		"((?:" +
		/(?:[ ]{${MarkdownText.TAB_WIDTH}})/ +
		".*\\n+" +
		")+" +
		")" +
		"((?=^[ ]{0,${MarkdownText.TAB_WIDTH}}\\S)|\\Z)", Pattern.MULTILINE);
		return markup.replaceAll(p) {m->
			
			String codeBlock = m.group(1);
			MarkdownText ed = new MarkdownText(codeBlock);
			ed.outdent();
			encodeCode(ed);
			ed.replaceAll("\\A\\n+","").replaceAll("\\s+\\z","");
			String text = ed.toString();
			String out = "";
			String firstLine = firstLine(text);
			if (isLanguageIdentifier(firstLine)) {
				out = languageBlock(firstLine, text);
			} else {
				out = genericCodeBlock(text);
			}
			return out;
		}
		
	}
	
	public MarkdownText parseCodeSpans(MarkdownText markup) {
		def p = Pattern.compile("(?<!\\\\)(`+)(.+?)(?<!`)\\1(?!`)")
		return markup.replaceAll(p){m->
			String code = m.group(2)
			MarkdownText subEditor = new MarkdownText(code)
			subEditor.replaceAll(/^[ \t]+/,"").replaceAll(/[ \t]+$/,"")
			encodeCode(subEditor)
			return "<code>" + subEditor.toString() + "</code>"
		}
	}
	
	public void encodeCode(MarkdownText ed) {
		ed.replaceAll("&", "&amp;");
		ed.replaceAll("<", "&lt;");
		ed.replaceAll(">", "&gt;");
		ed.replaceAll("\\*", markdownPlus.protectedChars.encode("*"));
		ed.replaceAll("_", markdownPlus.protectedChars.encode("_"));
		ed.replaceAll("\\{", markdownPlus.protectedChars.encode("{"));
		ed.replaceAll("\\}", markdownPlus.protectedChars.encode("}"));
		ed.replaceAll("\\[", markdownPlus.protectedChars.encode("["));
		ed.replaceAll("\\]", markdownPlus.protectedChars.encode("]"));
		ed.replaceAll("\\\\", markdownPlus.protectedChars.encode("\\"));
	}
	
	String firstLine(String text){
		if (!text) return "";
		String[] splitted = text.split("\\n");
		return splitted[0];
	}
	
	boolean isLanguageIdentifier(String line){
		if (!line) return false;
		String lang = "";
		if (line.startsWith(LANG_IDENTIFIER)) {
			lang = line.replaceFirst(LANG_IDENTIFIER, "").trim();
		}
		return lang.length() > 0;
	}
	
	String languageBlock(String firstLine, String text){
		// dont'use %n in format string (markdown aspect every new line char as "\n")
		//String codeBlockTemplate = "<pre class=\"brush: %s\">%n%s%n</pre>"; // http://alexgorbatchev.com/wiki/SyntaxHighlighter
		String codeBlockTemplate = "\n\n<pre class=\"%s\">\n%s\n</pre>\n\n"; // http://shjs.sourceforge.net/doc/documentation.html
		String lang = firstLine.replaceFirst(LANG_IDENTIFIER, "").trim();
		String block = text.replaceFirst( firstLine+"\n", "");
		return String.format(codeBlockTemplate, lang, block);
	}
	String genericCodeBlock(String text){
		// dont'use %n in format string (markdown aspect every new line char as "\n")
		String codeBlockTemplate = "\n\n<pre><code>%s\n</code></pre>\n\n";
		return String.format(codeBlockTemplate, text);
	}
}
