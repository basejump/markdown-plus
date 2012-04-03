/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus

 Regex adapted from markdownj
 Copyright (c) 2005, Martian Software
 Authors: Pete Bevin, John Mutchek
 http://www.martiansoftware.com/markdownj

 Regex adapted from from PHP Markdown & Extra
 Copyright (c) 2004-2009 Michel Fortin
 <http://michelf.com/projects/php-markdown/>
 */

package nineci.markdownplus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ListParser {

	MarkdownPlus markdownPlus

	int listLevel=0

    MarkdownText parse(MarkdownText text) {
        int lessThanTab = MarkdownText.TAB_WIDTH - 1

        String wholeList = "(" 					+ // #1 whole list
        	"(" 								+ // #2
				"[ ]{0,$lessThanTab}" 	+ //up to 3 spaces
                /((?:[-+*]|\d+[.]))/ 			+ // #4 first list marker
                "[ ]+" 							+ // #
            ")" 								+
            "(?s:.+?)" +
            "(" +
                "\\z" + // End of input is OK
                "|" +
                "\\n{2,}" +
                "(?=\\S)" + // If not end of input, then a new para
                "(?![ ]*" +
                "(?:[-+*]|\\d+[.])" +
                "[ ]+" +
             ")" + // negative lookahead for another list marker
            ")" +
         ")";

		def replaceClosure = { m->
			String list = m.group(1)
			String listStart = m.group(3)
			String listType = listStart.matches("[*+-]") ? 'ul':'ol'

			//turn 2 line feeds to 3 so that last items will be turned to a paragraph
			list = list.replaceAll(/\n{2,}/, "\n\n\n");

			String result = processListItems(list);

			def isUL
			String linefeed = ""

			if(listLevel>0){
				result = result.replaceAll(/\s+$/, "");
				isUL = "ul" == listType
			}else{
				isUL = listStart.matches("[*+-]")
				linefeed ="\n"
			}

			return "<$listType>${linefeed}${result}</$listType>\n".toString()
		}
		def pat = (listLevel>0) ? "^" : /(?:(?<=\n\n)|\A\n?)/
        Pattern matchStartOfLine = Pattern.compile(pat + wholeList, Pattern.MULTILINE);
        text.replaceAll(matchStartOfLine, replaceClosure);
        return text;
    }

    String processListItems(String list) {
    	listLevel++;

		// Trim trailing blank lines:
		list = list.replaceAll(/\n{2,}\z/, '\n');

		String listMarker = /[-+\*]|\d+[.]/
		String pat = /(?m)(\n)?/ 		+
			/^([ \t]*)/				+ //indentSpace
			/(${listMarker})[ ]+/ 	+ //list marker and space
			/((?s:.+?)/				+ //text = group[4]
			/(\n{1,2}))/ 			+ //tailing blank lines
			/(?=\n*(\z|\2(${listMarker})[ \t]+))/

		list = list.replaceAll(pat){ List group-> //Groovy 1.8.6 now passes a List<String> rather than a String[]
			String text = group[4];
			MarkdownText item = new MarkdownText(text);
			String indentSpace = group[1];
			if (indentSpace || text.find("\n\n")) {
				item = markdownPlus.runBlockGamut(item.outdent());
			} else {
				item = parse(item.outdent());
				item = markdownPlus.runSpanGamut(item);
			}
			return "<li>" + item.trim().toString() + "</li>\n";
		}
        listLevel--;
        return list;
    }

}
