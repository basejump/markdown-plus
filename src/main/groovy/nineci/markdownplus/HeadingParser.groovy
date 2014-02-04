/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus
 */

package nineci.markdownplus;

import java.util.regex.Matcher
import java.util.regex.Pattern

class HeadingParser {
	
	MarkdownPlus markdownPlus
	
	//needs AnchorParser so it can store the ids so these can be linked back to
	AnchorParser anchorParser
	
	MarkdownText parse(MarkdownText markup) {

		Pattern h1 = Pattern.compile('^(.*)\n====+$', Pattern.MULTILINE);
		//Pattern h1 = Pattern.compile(h1Pattern, Pattern.MULTILINE);
		markup.replaceAll(h1){ m->
			String heading = m.group(1).trim()
			return makeHeading(heading,1)
		}
		
		//do the h2 with ------
		Pattern h2 = Pattern.compile('^(.*)\n----+$', Pattern.MULTILINE);
		markup.replaceAll(h2){ m->
			String heading = m.group(1)
			return makeHeading(heading,2)
		}
		
		//items in this format"#### heading 1 ####"
		Pattern p = Pattern.compile(/^(#{1,6})\s*(.*?)\s*\1?$/, Pattern.MULTILINE);
		markup.replaceAll(p){ m->
			String heading = m.group(2);
			String level = m.group(1).length();
			return makeHeading(heading,level)
		}
		return markup;
	}

	String makeHeading(String headingText,level){
		String theId
		def idMatcher = headingText =~  /.*\[(.*?)\]/
		if(idMatcher.matches()){
			theId = idMatcher.group(1).trim()
			headingText = headingText.replaceAll(/\[(.*?)\]/, "").trim()
		}else{
			theId= MarkdownUtil.makeId(headingText)
		}
		headingText=headingText.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
		//add the id to the map
		anchorParser.linkMap[theId]=[url:"#$theId",title:"${headingText}"]
		return "<h$level id=\"${theId}\">${headingText}</h$level>\n" as String
	}
}
