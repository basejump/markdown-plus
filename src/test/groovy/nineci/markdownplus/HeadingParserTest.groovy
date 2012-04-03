package nineci.markdownplus;

import org.junit.Test;

import static org.junit.Assert.*;

class HeadingParserTest {
	
	@Test
	public void testHeaders(){
		MarkdownPlus markup = new MarkdownPlus();
		String markdownText = markup.markdown(headerMd);
		println(markdownText)
		assertNotNull(markdownText)
		assertEquals(headerHtml,markdownText)
	}
	
	@Test
	public void testMakeID(){
		assertEquals("abc__89.5_123_45-67",MarkdownUtil.makeId("abc_ 89.5 1@#\$%23 45-67"))
		assertEquals("markdown_basics",MarkdownUtil.makeId("Markdown: Basics"))
	}
	
	
//	@Test
//	public void testMakeHeading(){
//		assertEquals("<h1 id=\"test_h1\">Test H1</h1>\n",new HeadingParser().makeHeading("Test H1",1))
//		assertEquals("<h3 id=\"some_id\">Test H3 &amp;&lt;&gt;</h3>\n", new HeadingParser().makeHeading("Test H3 &<> [some_id]",3))
//	}
		
	def headerMd = '''
Test H1
=======

Test H1 with id   [test_manual_id]
=======

Test H2
-------

### Test H3 & id & <>       [some_id]
	
'''
	
	def headerHtml = '''<h1 id="test_h1">Test H1</h1>

<h1 id="test_manual_id">Test H1 with id</h1>

<h2 id="test_h2">Test H2</h2>

<h3 id="some_id">Test H3 &amp; id &amp; &lt;&gt;</h3>
'''
	
	def imageMd = '''
inline ![image][] and a [link][] with attributes.

[image]: http://path.to/image "Image title" 
[link]:  http://path.to/link.html "Some Link" class="wtf" 
	
'''
	
	def imageHtml = '''<h1 id="test_h1">Test H1</h1>

<h1 id="test_manual_id">Test H1 with id</h1>

<h2 id="test_h2">Test H2</h2>

<h3 id="some_id">Test H3 &amp; id &amp; &lt;&gt;</h3>
'''
}
