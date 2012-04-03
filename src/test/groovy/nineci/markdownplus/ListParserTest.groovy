package nineci.markdownplus;

import static org.junit.Assert.*;

import org.junit.Test

class ListParserTest {

	@Test
	public void testMarkdown() {
		MarkdownPlus markup = new MarkdownPlus();
		String markdownText = markup.markdown(listMd);
		println(markdownText)
		assertNotNull(markdownText)
		assertEquals(listHtml,markdownText)
	}
	
	@Test
	public void testMarkdownParagraph() {
		MarkdownPlus markup = new MarkdownPlus();
		String markdownText = markup.markdown(listParaMd);
		println(markdownText)
		assertNotNull(markdownText)
		assertEquals(listParaHtml,markdownText)
	}
//	@Test
//	public void testMarkdownSimp() {
//		MarkdownPlus markup = new MarkdownPlus();
//		String markdownText = markup.markdown(listSimpMd);
//		println(markdownText)
//		assertNotNull(markdownText)
//		assertEquals(listSimpHtml,markdownText)
//	}
////	
//	@Test
//	public void testMarkdownParagraphOld() {
//		MarkdownProcessor markup = new MarkdownProcessor();
//		String markdownText = markup.markdown(listParaMd);
//		println(markdownText)
//		assertNotNull(markdownText)
//		assertEquals(listParaHtml,markdownText)
//	}
	

def listParaMd = '''
1. First

2. Second:
	- Fee
	- Fie
	- Foe

3. Third
'''

def listParaHtml = '''<ol>
<li><p>First</p></li>
<li><p>Second:</p>

<ul><li>Fee</li>
<li>Fie</li>
<li>Foe</li></ul></li>
<li><p>Third</p></li>
</ol>
'''
def listMd = '''
1. First
2. Second:
	* Fee
	* Fie
	* Foe
3. Third
'''

def listHtml = '''<ol>
<li>First</li>
<li>Second:
<ul><li>Fee</li>
<li>Fie</li>
<li>Foe</li></ul></li>
<li>Third</li>
</ol>
'''
}
