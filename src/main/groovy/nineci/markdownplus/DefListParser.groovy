package nineci.markdownplus;
/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus

Regex adapted from from PHP Markdown & Extra
Copyright (c) 2004-2009 Michel Fortin  
<http://michelf.com/projects/php-markdown/>

 */
import java.util.regex.Matcher
import java.util.regex.Pattern

class DefListParser {
	
	MarkdownPlus markdownPlus
	
	MarkdownText parse(MarkdownText text) {
		String less_than_tab = MarkdownText.TAB_WIDTH;
		String p = /(?>\A\n?|(?<=\n\n))(?>(/ +  // $1 = whole list
			'(' + // $2
				/[ ]{0,$less_than_tab}/ +
				/((?>.*\S.*\n)+)/ +	// $3 = defined term
				/\n?/ +
				/[ ]{0,$less_than_tab}:[ ]+/ + // # colon starting definition
			')' +
			'(?s:.+?)' +
			'(' +			//					# $4
				'\\z' +
				'|' +
				'\\n{2,}' +
				'(?=\\S)' +
				'(?!' + //					# Negative lookahead for another term
					'[ ]{0,' + less_than_tab + '}' + //
					'(?: \\S.*\\n )+?' +//			# defined term
					'\\n?' +
					'[ ]{0,'+ less_than_tab +'}:[ ]+' + // # colon starting definition
				')' +
				'(?!' +	//					# Negative lookahead for another definition
					'[ ]{0,'+ less_than_tab +'}:[ ]+'+ //# colon starting definition
				')' +
			')' +
		'))' 
		
		//println "try match on $text for $p"
		StringBuffer defHtml = new StringBuffer()
		Pattern pat = Pattern.compile(p, Pattern.MULTILINE);
		Matcher m = pat.matcher(text.toString());
		def wasFound
		
		while (m.find()) {
			wasFound = true
			defHtml << processTerm(m.group(1))
			defHtml << processDef(m.group(1))
		}
		if(wasFound){
			def defList = '<dl>\n' << defHtml <<'</dl>\n'
			String p1 = /(?>(/ +  // $1 = whole list
					'(' + // $2
					/[ ]{0,$less_than_tab}/ +
					/((?>.*\S.*\n)+)/ +	// $3 = defined term
					/\n?/ +
					/[ ]{0,$less_than_tab}:[ ]+/ + // # colon starting definition
					')' +
					'(?s:.+)' +
					'))' 
			
			text.replaceAll(p1){
				defList.toString()
			}
		}
		
	}

	def processTerm(String dltext){
		//trim trailing blanks lines
		dltext = dltext.replaceAll('\\n{2,}\\z/', "\n")

		String p = /(?>\A\n?|\n\n+)/ + 		// # leading line
		'('+						  		// # definition terms = $1
			/[ ]{0,$MarkdownText.TAB_WIDTH}/ + // # leading whitespace
			/(?![:][ ]|[ ])/ +				// # negative lookahead for a definition mark (colon) or more whitespace.
			/(?>\S.*\n)+?/ +				// # actual term (not whitespace).	
		')' +			
		/(?=\n?[ ]{0,3}:[ ])/ 				// # lookahead for following line feed #   with a definition mark.
		//println "try match on $dltext for $p"
		Pattern pat = Pattern.compile(p, Pattern.MULTILINE);
		Matcher m = pat.matcher(dltext.toString());
		StringBuffer defHtml = new StringBuffer()
		defHtml << '<dt>'
		if (m.find()) {
			def theTerm = m.group(1).trim().replaceAll('\\n', '')
			theTerm = markdownPlus.runSpanGamut(new MarkdownText(theTerm)).toString();
			defHtml << theTerm
			//println "term ${m.group(2)}"
		}
		defHtml << '</dt>\n'
		return defHtml
	}
	
	def processDef(String dltext){
		//trim trailing blanks lines
		//do terms
		String p = /\n(\n+)?/ +						// # leading line = $1
			'('	+									// # marker space = $2
				/[ ]{0,$MarkdownText.TAB_WIDTH}/ + 	// # whitespace before colon
				/[:][ ]+/ +							// # definition mark (colon)
			')' +
			/((?s:.+?))/ +							// # definition text = $3
			/(?=\n+/ + 							// # stop at next definition mark,
				'(?:'+									// # next term or end of text
					/[ ]{0,$MarkdownText.TAB_WIDTH}[:][ ]|/ +
					/<dt>|\z/ +
				')' +						
			')'

		//println "def match on $dltext for $p"
		StringBuffer defHtml = new StringBuffer()
		
		Pattern pat = Pattern.compile(p, Pattern.MULTILINE);
		Matcher m = pat.matcher(dltext.toString());
		if (m.find()) {
			defHtml << '<dd>'
			String theDef = m.group(3)
			if(m.group(1) || (theDef =~ /\n\s*\n/).find()  ){
				MarkdownText ed = new MarkdownText(theDef + '\n' );
				ed.outdent();
				theDef=markdownPlus.runBlockGamut(ed);
				theDef = theDef.replaceAll('\\n{2,}\\z/', "\n")
			}else{
				theDef = markdownPlus.runSpanGamut(new MarkdownText(theDef))
				theDef = theDef.replaceAll('\\n{2,}\\z/', "\n")
			}
			defHtml <<  theDef
			defHtml << '</dd>\n'
			
		}
		return defHtml
		
	}


}
