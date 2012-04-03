package nineci.markdownplus;

import static org.junit.Assert.*;

import org.junit.Test;

class HrParserTest {

	@Test
	public void testMarkdown() {
		MarkdownPlus markup = new MarkdownPlus();
		String markdownText = markup.markdown(hrMd);
		assertNotNull(markdownText)
		assertEquals(hrHtml,markdownText)
	}

	
//	@Test
//	public void testMakeHeading(){
//		assertEquals("<h1 id=\"test_h1\">Test H1</h1>\n",HeadingParser.makeHeading("Test H1",1))
//		assertEquals("<h3 id=\"some_id\">Test H3 &amp;&lt;&gt;</h3>\n",HeadingParser.makeHeading("Test H3 &<> [some_id]",3))
//	}
	
def hrMd ='''----

***
	
* * *

- - - - - - -

'''
	
def hrHtml='''<hr />

<hr />

<hr />

<hr />
'''

}
