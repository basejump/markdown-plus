package nineci.markdownplus;
/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus

 much of this came from markdownj.
 Original Copyright (c) 2005, Martian Software
 Authors: Pete Bevin, John Mutchek
 http://www.martiansoftware.com/markdownj
 */
import java.util.regex.Matcher
import java.util.regex.Pattern

class BlockQuoteParser {
	MarkdownPlus markdownPlus
	
	MarkdownText parse(MarkdownText markup) {
		Pattern p = Pattern.compile("(" +
			"(" +
				"^[ \t]*>[ \t]?" 	+ // > at the start of a line
				".+\\n" 			+ // rest of the first line
				"(.+\\n)*" 			+ // subsequent consecutive lines
				"\\n*" 				+ // blanks
			")+" +
		")", Pattern.MULTILINE) 
		return markup.replaceAll(p) {m->
			MarkdownText blockQuote = new MarkdownText(m.group(1)) 
			blockQuote.replaceAll("^[ \t]*>[ \t]?" , "") 
			blockQuote.replaceAll('^[ \t]+$' , "") 
			blockQuote = markdownPlus.runBlockGamut(blockQuote) 
			blockQuote.replaceAll("^", "  ") 
			
			Pattern p1 = Pattern.compile(/(\s*<pre>.*?<\/pre>)/, Pattern.DOTALL) 
			blockQuote = blockQuote.replaceAll(p1){match->
				String pre = match.group(1) 
				return pre.replaceAll("(?m)^  ","")
			}
			return "<blockquote>\n" + blockQuote + "\n</blockquote>\n\n" 
		}
	}
}
