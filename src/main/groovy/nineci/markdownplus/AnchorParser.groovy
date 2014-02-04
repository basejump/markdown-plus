/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus
 */
package nineci.markdownplus;

import java.util.Map;
import java.util.regex.Matcher
import java.util.regex.Pattern

class AnchorParser {
	
	MarkdownPlus markdownPlus
	Map linkMap = new TreeMap()
	//static int nested_brackets_depth = 6;
	
	MarkdownText parse(MarkdownText markup) {
		
		// Internal references: [link text] [id]
		Pattern refLinks = Pattern.compile("(?x)(" + //$1 whole match
	    /	\[(							/ + // $2 Link text
		/		(?>[^\[\]]+|\[			/ + //nested brackets 1 levels
		/			(?>[^\[\]]+|\[\])*	/ + 
		/		\])*					/ + 
		/	)\]							/ + //end of group 2 linked text 
		/	\s?(?:\n\s*)?				/ + // spaces & linefeed allowed between link and Id
		/	\[(.*?)\]					/ + // ID = $3
		")");
		markup.replaceAll(refLinks){m->
			//println "found match ${m.group(1)}"
			return refLinkCallback(m.group(1), m.group(2), m.group(3).toLowerCase())
		}
		
		// Inline-style links: [link text](url "optional title")
		def inline = "(" 		+ // Whole match = $1
			/\[(.*?)\]/ 		+ // Link text = $2
			/\(/ 				+ // literal opening paren
			/[ ]*/ 				+ // 1 or more spaces
			/<?(.*?)>?/ 		+ // href url = $3
			/[ ]*/ 				+ // 1 or more spaces
			'(' 				+ // $4 the whole title text
				/(['"])/ 		+ // Quote character = $5
				/(.*?)/ 		+ // Title = $6
				/\5/ 			+ // matching quote fro $5
			')?' 				+ //title is optional
			/\)/ 				+ //closing literal paren	
		")"
		
		markup.replaceAll(Pattern.compile(inline,Pattern.DOTALL)) { m ->
			String linkText = m.group(2);
			String url = protectChars(m.group(3))
			String title = m.group(6)?:""

			StringBuffer result = new StringBuffer();
			result.append("<a href=\"").append(url).append("\"");
			if (title) {
				title = protectChars(m.group(6)).replaceAll('"', "&quot;")
				title = " title=\"${title}\""
			}
			return "<a href=\"$url\"$title>$linkText</a>"
		}
		
		// Last, handle reference-style shortcuts: [link text]
		// These must come last in case you've also got [link test][1]
		// or [link test](/foo)
		Pattern referenceShortcut = Pattern.compile("(" + // wrap whole match in $1
			/\[([^\[\]]+)\]/ 	+ // link text = $2; link text it self can't contain '[' or ']'
		")");
		
		markup.replaceAll(referenceShortcut){ m->
			return refLinkCallback(m.group(1),m.group(2),m.group(2).toLowerCase())
		}
		
		return markup;
	}
	
	def refLinkCallback(wholeMatch,linkText,id){
		String anchor
		id = id ?: linkText.toLowerCase()
		id = id.replaceAll(/[ ]?\n/, " ")
		
		def link = linkMap[id] ?: linkMap[MarkdownUtil.makeId(id)]
		if (link) {
			String url = protectChars( link.url)
			String title = link.title
			if (title) {
				title = " title=\"${protectChars(title)}\""
			}
			anchor = "<a href=\"$url\"$title>$linkText</a>"
		}
		return anchor ?: wholeMatch
	}
		
	/*
	 * taken from markdownj. will need to relook at this as I don't think its totally working
	 */
	MarkdownText parseAutoLinks(MarkdownText markup) {
		markup.replaceAll("<((https?|ftp):[^'\">\\s]+)>", '<a href="$1">$1</a>');
		Pattern email = Pattern.compile(/<([-.\w]+\@[-a-z0-9]+(\.[-a-z0-9]+)*\.[a-z]+)>/);
		//FIXME there are no test that fails if I scramble this, we need to add one.
		markup.replaceAll(email){m->
			String address = m.group(1);
			MarkdownText ed = new MarkdownText(address);
			markdownPlus.unEscapeSpecialChars(ed);
			String addr = encodeEmail(ed.toString());
			String url = encodeEmail("mailto:" + ed.toString());
			return "<a href=\"" + url + "\">" + addr + "</a>";
		}
		return markup;
	}
	
	void stripLinkDefinitions(MarkdownText text) {
		def p = "^[ ]{0,3}" 		+ 
			/\[(.+)\][ ]?:/ 		+ // ID = $1
			/[ ]*\n?[ ]*/ 			+ // Space and maybe 1 new line
			/<?(\S+?)>?/ 			+ // URL = $2
			/[ ]*\n?[ ]*/ 			+ // Space
			'(?:["(]' 				+
			     /(.+?)/			+ // Optional title = $3
			'[")][ ]*)?' 			+ // title is optional
			/(?:\n+|\Z)/
		
		text.replaceAll(p){m->
			String id = m.group(1).toLowerCase()
			String url = new MarkdownText(m.group(2)).encodeAmpsAndAngles().toString()
			String title = m.group(3) ?: ""
			title = title.replaceAll('"', "&quot;");
			linkMap[id]=[url:url,title:title]
			return "";
		}
	}
	
	String protectChars(String text){
		text = text.replaceAll("\\*", markdownPlus.protectedChars.encode("*"));
		text = text.replaceAll("_", markdownPlus.protectedChars.encode("_"));
		return text
	}
	
	//FIXME this has to be broken
	String encodeEmail(String s) {
		StringBuffer sb = new StringBuffer();
		char[] email = s.toCharArray();
		for (char ch : email) {
			double r = rnd.nextDouble();
			if (r < 0.45) {      // Decimal
				sb.append("&#");
				sb.append((int) ch);
				sb.append(';');
			} else if (r < 0.9) {  // Hex
				sb.append("&#x");
				sb.append(Integer.toString((int) ch, 16));
				sb.append(';');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
}
