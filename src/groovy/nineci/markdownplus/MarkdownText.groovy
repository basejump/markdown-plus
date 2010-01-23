/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus

 adapted and converted from markdownj
 Copyright (c) 2005, Martian Software
 Authors: Pete Bevin, John Mutchek
 http://www.martiansoftware.com/markdownj
 
 Some of the Regex and logic snagged from from PHP Markdown & Extra
 Copyright (c) 2004-2009 Michel Fortin  
 <http://michelf.com/projects/php-markdown/>
 */

package nineci.markdownplus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;


/**
 * A text document for markdown. contains a mutable StringBuilder(like a StringBuffer) to hole the markdown before and after
 * 
 */
class MarkdownText {
	StringBuffer text;
    static final int TAB_WIDTH = 4;

    public MarkdownText(CharSequence text) {
        this.text = new StringBuffer(text.toString());
    }

    public String toString() {
        return text.toString();
    }
	
	
	public MarkdownText parseItalicsAndBold(MarkdownText markup) {
		replaceAll(/(\*\*|__)(?=\S)(.+?[*_]*)(?<=\S)\1/, '<strong>$2</strong>') 
		replaceAll(/(\*|_)(?=\S)(.+?)(?<=\S)\1/, '<em>$2</em>') 
		return this 
	}
	
	public MarkdownText parseImages(MarkdownText text) {
		replaceAll(/!\[(.*)\]\((.*) "(.*)"\)/, '<img src=\"$2\" alt=\"$1\" title=\"$3\" />') 
		replaceAll(/!\[(.*)\]\((.*)\)/, '<img src=\"$2\" alt=\"$1\" />') 
		return this
	}
	
	/*
	 * compiles the regex into a mutiline pattern and calls groovy's string replace
	 * and replaces the internal stringbuilder
	 */
    public MarkdownText replaceAll(String regex, String replacement) {
        if (text.length() > 0) {
            Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
			text = new StringBuffer(text.toString().replaceAll(p, replacement))
        }
        return this;
    }
	
	/*
	 * accepts a closure that gets passed the matcher object for each find. the closure should return 
	 * the replacement text for each matcher.find that gets called. compiles the regex into a mutiline pattern 
	 * and calls  replaceAll(Pattern pattern, Closure closure)
	 */
	public MarkdownText replaceAll(String regex, Closure closure) {
		return replaceAll(Pattern.compile(regex, Pattern.MULTILINE),closure)
	}
	
	/*
	 * accepts a closure that gets passed the matcher object for each find. the closure should return 
	 * the replacement text for each matcher.find that gets called 
	 */
	public MarkdownText replaceAll(Pattern pattern, Closure closure) {
		Matcher m = pattern.matcher(text);
		int lastIndex = 0;
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			sb << text.subSequence(lastIndex, m.start())
			sb << closure(m)
			
			lastIndex = m.end()
		}
		//println "finshed while with $sb"
		sb << text.subSequence(lastIndex, text.length())
//		StringBuffer sb = new StringBuffer();
//		while (m.find()) {
//			m.appendReplacement(sb, closure(m).toString());
//		}
//		m.appendTail(sb);
		text = sb
		return this;
	}
	
	/*
	 * converts all the tabs into the appropriate amount of spaces so we 
	 * can make our regex calls easier and not have to worry about using \t every where
	 */
	public MarkdownText tabsToSpaces(int tabWidth) {
		def newText = text.toString().replaceAll(/(.*?)\t/) {all,lineSoFar-> 
			int width = lineSoFar.length();
			lineSoFar = lineSoFar << ' ';
			width++
			while (width % tabWidth != 0){
				lineSoFar = lineSoFar << ' '
				width++
			}			
			return lineSoFar.toString()
		}
		text = new StringBuffer(newText)
		return this
	}


    /**
     *opposite of indent, remove 1 level of indent spaces or tabs
     */
    public MarkdownText outdent(int spaces) {
        return replaceAll(/^(\t|[ ]{1,$spaces})/, "");
    }

	/**
	 *opposite of indent, remove 1 level of indent spaces or tabs
	 *based on static TAB_WIDTH
	 */
    public MarkdownText outdent() {
        return outdent(MarkdownText.TAB_WIDTH);
    }

    /**
     * just calls the trim on the toString().trim() of the internal StringBuilder
     */
    public MarkdownText trim() {
        text = new StringBuffer(text.toString().trim());
        return this;
    }

    /**
     * Introduce a number of spaces at the start of each line.
     */
    public MarkdownText indent(int spaces) {
		String indent
        for (int i = 0; i < spaces; i++) {
			indent<<' '
        }
		//replaces each line with this
        return replaceAll("^", indent)
    }

    /**
     * Add a string to the end of the buffer.
     * @param s
     */
    public void append(CharSequence s) {
        text.append(s)
    }

    /*
     * put string at beggining of this text
     */
    public void prepend(CharSequence s) {
		StringBuffer newText = new StringBuffer();
		text = newText << s << text
    }

    int length() {
        return text.length()
    }
	
	/*
	 * clens up the linefeeds, converts tabs to spaces and blanks lines with only spaces or tabs
	 */
	def cleanUp(){
		// Normalize line endings
		replaceAll(/\r\n/, "\n") 	// DOS to Unix
		replaceAll(/\r/, "\n")    // Mac to Unix
		
		//Strip any lines consisting only of spaces and tabs as this makes subsequent regexen easier to write, because we can
		//match consecutive blank lines with /\n+/ instead of /[ \t]*\n+/ .
		replaceAll(/^[ \t]+$/, "")

		//add 2 blank lines to the end
		append("\n\n");
		
		//convert all tabs to spaces
		tabsToSpaces(MarkdownText.TAB_WIDTH);
	}
	
	MarkdownText encodeEscapeChars(CharacterProtector charpro) {
		// Two backslashes in a row
		replaceAll(/\\{2,}/, charpro.encode("\\"));
		
		"`_>!*{}[]()#+-.".toCharArray().each{
			replaceAll(/\\{1,}\$it/, charpro.encode(String.valueOf(it)));
		}
		return this
	}
	
	public MarkdownText encodeAmpsAndAngles() {
		// Ampersand-encoding based entirely on Nat Irons's Amputator MT plugin:
		// http://bumppo.net/projects/amputator/
		replaceAll("&(?!#?[xX]?(?:[0-9a-fA-F]+|\\w+);)", "&amp;") 
		replaceAll('<(?![a-z/?\\$!])', "&lt;") 
		return this 
	}
	
	public MarkdownText encodeSpecialCharsInTags(CharacterProtector charpro) {
		def theText = text.toString()
		Pattern p = Pattern.compile("" +
				"(?s:<!(--.*?--\\s*)+>)" +
				"|" +
				"(?s:<\\?.*?\\?>)" +
				"|" +
				'(?:<[a-z/!$](?:[^<>]|5)*>)' + //nested tags 6 levels
				"", Pattern.CASE_INSENSITIVE);
		
		Matcher m = p.matcher(theText);
		int last = 0;
		StringBuffer sb = new StringBuffer()
		while (m.find()) {
			if (last < m.start()) {
				sb << theText.substring(last, m.start());
			}
			String value = m.group()
			["`","_","*","\\"].each{
				value = value.replaceAll(/\$it/, charpro.encode(it));
			}
			sb << value
			last = m.end();
		}
		//basically if there is no match or there is stuff hanging on the end then 
		if (last < theText.length()) {
			sb << theText.substring(last, theText.length())
		}
		
		text = sb
		return this
	}
	
	
	MarkdownText hashHTMLBlocks(CharacterProtector protectedHtml) {
		// Hashify HTML blocks:
		// We only want to do this for block-level HTML tags, such as headers,
		// lists, and tables. That's because we still want to wrap <p>s around
		// "paragraphs" that are wrapped in non-block-level tags, such as anchors,
		// phrase emphasis, and spans. The list of tags we're looking for is
		// hard-coded:
		
		String[] tagsA = [
		"p", "div", "h1", "h2", "h3", "h4", "h5", "h6", "blockquote", "pre", "table",
		"dl", "ol", "ul", "script", "noscript", "form", "fieldset", "iframe", "math"
		]
		String[] tagsB = ["ins", "del"]
		
		String alternationA = tagsA.join("|")
		String alternationB = alternationA + "|" + tagsB.join("|") 
		
		int less_than_tab = MarkdownText.TAB_WIDTH - 1 
		
		// 
		// First, look for nested blocks, e.g.:
		//   <div>
		//       <div>
		//       tags for inner block must be indented.
		//       </div>
		//   </div>
		//
		// The outermost tags must start at the left margin for this to match, and
		// the inner nested divs must be indented.
		// We need to do this before the next, more liberal match, because the next
		// match will start at the first `<div>` and stop at the first `</div>`.
		Pattern p1 = Pattern.compile("(" +
				"^<(" + alternationA + ")" +
				"\\b" +
				"(?>.*\\n)*?" +
				"</\\2>" +
				"[ ]*" +
				"(?=\\n+|\\Z))", Pattern.MULTILINE) 
		
		def htmlClos = { m->
			String literal = m.group() 
			return "\n\n" + protectedHtml.encode(literal) + "\n\n" 
		}
		
		replaceAll(p1, htmlClos)	
		
		// Now match more liberally, simply from `\n<tag>` to `</tag>\n`
		def p2 ="(" +
				"^" +
				"<(" + alternationB + ")" +
				"\\b" +
				"(?>.*\\n)*?" +
				".*</\\2>" +
				"[ ]*" +
				"(?=\\n+|\\Z))" 
		replaceAll(p2, htmlClos) 
		
		// Special case for <hr>
		def p3 ="(?:" +
				"(?<=\\n\\n)" +
				"|" +
				"\\A\\n?" +
				")" +
				"(" +
				"[ ]{0," + less_than_tab + "}" +
				"<(hr)" +
				"\\b" +
				"([^<>])*?" +
				"/?>" +
				"[ ]*" +
				"(?=\\n{2,}|\\Z))"
		replaceAll(p3, htmlClos) 
		
		// Special case for standalone HTML comments:
		def p4 = "(?:" +
				"(?<=\\n\\n)" +
				"|" +
				"\\A\\n?" +
				")" +
				"(" +
				"[ ]{0," + less_than_tab + "}" +
				"(?s:" +
				"<!" +
				"(--.*?--\\s*)+" +
				">" +
				")" +
				"[ ]*" +
				"(?=\\n{2,}|\\Z)" +
				")"
		replaceAll(p4, htmlClos) 
		return this
	}
}
