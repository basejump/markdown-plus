/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus

 */

package nineci.markdownplus;


class HrParser {
	
	MarkdownText parse(MarkdownText markup) {
        String[] hrDelimiters = [/\*/, '-', '_']
		[/\*/, '-', '_'].each{
			markup.replaceAll(/^[ ]{0,2}([ ]?$it[ ]?){3,}[ ]*$/, "<hr />");
        }
		return markup
	}
}
