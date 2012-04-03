package nineci.markdownplus;

import static org.junit.Assert.*;

import org.junit.Test;

class MetaDataParserTest {
	
	
	@Test
	public void testParse() {
		def metaparser = new MetaDataParser()
		def result = metaparser.parse(new MarkdownText(metaMd))
		assertEquals "Will not get read: because its not meta\n\n# yet to : be processed heading #",result.toString().trim()
		assertEquals "A Lofty Title : Markdown Rocks",metaparser.data.title
		assertEquals "test2",metaparser.data.subtitle
		assertEquals "Joshua Burnett",metaparser.data.author
		assertEquals "2010 9ci Inc.",metaparser.data.copyright
		assertEquals(['blah','blah2','blah3'],metaparser.data.keywords)
	}
	
	
def metaMd='''
Title:		A Lofty Title : Markdown Rocks  
subtitle:	test2   
author:		Joshua Burnett
web:		http://greenbill.com/
Copyright:	2010 9ci Inc.
Keywords:	blah
			blah2
			blah3
Base Level:	2

Will not get read: because its not meta

# yet to : be processed heading #
'''.trim()

}
