/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus
 
 Parts of the Regex adapted from from PHP Markdown & Extra
 Copyright (c) 2004-2009 Michel Fortin  
 <http://michelf.com/projects/php-markdown/>
 */
package nineci.markdownplus;
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Parses the following type of syntax into an html table
 * 
 *  Header 1  | Header 2
 *  ----------|----------
 *  row 1 val | row 2 val
 * 
 */
class TableParser {
	
	MarkdownPlus markdownPlus
	
	String tablePat = "(?m)" 	+ //?m is Pattern.MULTILINE 
		"^"							+
		"[ ]{0,${MarkdownText.TAB_WIDTH - 1}}" 		+ //allow whitespace
		"(.*[|].+)\n" 				+ //#1 header Row at least 1 pipe
		//"[|](.+)\n" 				+ //#1 header Row at least 1 pipe
		"[ ]{0,${MarkdownText.TAB_WIDTH - 1}}" 		+ //allow whitespace
		/[|]?([ ]*[-:]+[ ]*[|][-| :]+)\n/ 	+ //#2: Header underline
		"(" 						+ //#3 group: Body
			/(?>.*[|].*\n)*/ 		+ //Row content ?> means don't include it in a group
		")" 						+
		/(?=\n+|\Z)/ 				//Stop at final double newline or (\Z) end of string it self	
		
	MarkdownText parse(MarkdownText text) {
		int lessThanTab = MarkdownText.TAB_WIDTH - 1;

		text.replaceAll(tablePat){m->
			//Remove leading pipe for each row on body, the head and cols don't need it since the regex group doesn't have it
			def body = m.group(3).replaceAll(/(?m)^ *[|]/,'').replaceAll(/(?m)[|] *$/,'')
			
			//Remove tailing pipes
			def header = m.group(1).replaceAll(/^ *[|]/,'').replaceAll(/[|] *$/,'')
			def cols =  m.group(2).replaceAll(/^ *[|]/,'').replaceAll(/[|] *$/,'')
			
			//Reading alignment from header underline.
			def colAlign = cols.split("[|] *");
			colAlign.eachWithIndex{ column, i->
				if (column.matches('^ *-+: *$')) 		colAlign[i] = " align='right'"
				else if (column.matches('^ *:-+: *$')) 	colAlign[i] = " align='center'"
				else if (column.matches('^ *:-+ *$')) 	colAlign[i] = " align='left'"
				else colAlign[i]=""
			}

			// Write column headers.
			def tableHtml= new StringBuffer("<table>\n<thead>\n<tr>\n")
			
			//do the span stuff on the header and write the TH header
			header = markdownPlus.runSpanGamut(new MarkdownText(header)).toString();
			
			header.split("[|] *").eachWithIndex{ headval, i->
				tableHtml << "   <th${colAlign[i]}>${headval.trim()}</th>\n"
			}
			tableHtml << "</tr>\n</thead>\n"
			//now do the rows and body
			tableHtml << "<tbody>\n"
			String[] rows = body.split("\n");
			
			for (String row : rows){
				row = markdownPlus.runSpanGamut(new MarkdownText(row)).toString();
				tableHtml << "<tr>\n"
				for (String td : row.split("[|] *")){
					tableHtml<< "   <td>${td.trim()}</td>\n"
				}
				tableHtml << "</tr>\n"
			}
			tableHtml << "</tbody>\n</table>\n"
			return tableHtml
		}
		
	}
		
}

