package nineci.markdownplus
/*
much of this logic flow and regex came from  either PHP Markdown Extra and/or markdownj.
Since this is a groovy port I leaned on markdownj heavily for this port. Kudo's to them!!!
I assume they may have a lot from the original John Gruber's regex in perl Markdown too but I have not compared the code

Copyright (c) 2010 9ci Inc
Authors: Joshua Burnett
http://www.9ci.com/markdownplus

Markdownj
Copyright (c) 2005, Martian Software
Authors: Pete Bevin, John Mutchek
http://www.martiansoftware.com/markdownj

PHP Markdown & Extra
Copyright (c) 2004-2009 Michel Fortin  
<http://michelf.com/projects/php-markdown/>

Original Markdown
Copyright (c) 2004-2006 John Gruber  
<http://daringfireball.net/projects/markdown/>

*/


import java.util.Collection
import java.util.regex.Matcher
import java.util.regex.Pattern


class MarkdownPlus {
	
	ListParser 		listParser
	CodeBlockParser codeBlockParser
	AnchorParser 	anchorParser
	HeadingParser	headingParser
	HrParser 		hrParser 
	TableParser 	tableParser
	DefListParser 	defListParser
	BlockQuoteParser blockQuoteParser
	MetaDataParser 	metaDataParser
	
    CharacterProtector protectedHtml = new CharacterProtector()
    CharacterProtector protectedChars = new CharacterProtector()
	
	String markdown(String txt) {
		//init
        MarkdownText text = new MarkdownText(txt)
		hrParser 		= new HrParser()
		anchorParser 	= new AnchorParser(markdownPlus:this)
		headingParser 	= new HeadingParser(markdownPlus:this,anchorParser:anchorParser)
		listParser 		= new ListParser(markdownPlus:this)
		codeBlockParser = new CodeBlockParser(markdownPlus:this)
		tableParser 	= new TableParser(markdownPlus:this)
		defListParser 	= new DefListParser(markdownPlus:this)
		blockQuoteParser = new BlockQuoteParser(markdownPlus:this)
		metaDataParser = new MetaDataParser()
		
        text.cleanUp()

        //parse out the metadata at beginning of line
        println "grabbing meta data"
        metaDataParser.parse(text)
		//run freemarker on it
        //FreeMarkerProcessor fmp = new FreeMarkerProcessor()
        //def res = fmp.processContent([:],text.toString())
        //text.text = new StringBuffer(res)
		//Turn block-level HTML blocks into hash entries
        println "hashing html"
        text.hashHTMLBlocks(protectedHtml)
		
		anchorParser.stripLinkDefinitions(text)
		
		text = runBlockGamut(text)
        unEscapeSpecialChars(text)

        text.append "\n"
        return text.toString()
    }
	
	@Override
	MarkdownText runBlockGamut(MarkdownText text) {
		println "process headings"
		//headings
		headingParser.parse(text)

		//hr breaks
		hrParser.parse(text)

		println "process lists"
		//lists
		listParser.parse(text)
		println "process tables"
		//tables
		tableParser.parse text
		
		println "process definition lists"
		//definition lists
		defListParser.parse text

		println "process code blocks"
		//code blocks
		codeBlockParser.parse text
		
		println "process blockquotes"
		blockQuoteParser.parse text
		
		println "hashing html again"
		text.hashHTMLBlocks(protectedHtml) 
		
		return formParagraphs(text) 
	}
	
	public MarkdownText runSpanGamut(MarkdownText text) {
		text.encodeSpecialCharsInTags(protectedChars) 

		codeBlockParser.parseCodeSpans(text) 

		text.encodeEscapeChars(protectedChars)
		
		text.parseImages()
		
		anchorParser.parse(text)
		anchorParser.parseAutoLinks(text)
		
		//got to call is a second time to escape the stuff done above
		text.encodeSpecialCharsInTags(protectedChars) 
		
		text.encodeAmpsAndAngles()
		
		text.parseItalicsAndBold()
		
		// Manual line breaks
		text.replaceAll(" {2,}\n", " <br />\n") 
		text.replaceAll("//\n", " <br />\n") 
		return text 
	}
	
	public MarkdownText formParagraphs(MarkdownText markup) {
		markup.replaceAll("\\A\\n+","") 
		markup.replaceAll("\\n+\\z","") 
		
		String[] paragraphs 
		if (markup.length()==0) {
			paragraphs = new String[0] 
		} else {
			paragraphs = Pattern.compile("\\n{2,}").split(markup.toString()) 
		}
		for (int i = 0; i < paragraphs.length; i++) {
			String paragraph = paragraphs[i] 
			String decoded = protectedHtml.decode(paragraph) 
			if (decoded) {
				paragraphs[i] = decoded 
			} else {
				paragraph = runSpanGamut(new MarkdownText(paragraph)).toString() 
				paragraphs[i] = "<p>" + paragraph + "</p>" 
			}
		}
		return new MarkdownText(paragraphs.join("\n\n")) 
	}
		
	public void unEscapeSpecialChars(MarkdownText ed) {
		for (String hash : protectedChars.getAllEncodedTokens()) {
			String plaintext = protectedChars.decode(hash) 
			ed.replaceAll(hash){
				return plaintext 
			}
		}
	}
	
}
