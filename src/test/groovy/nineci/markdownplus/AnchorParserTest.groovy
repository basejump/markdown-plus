package nineci.markdownplus;

import static org.junit.Assert.*;

import org.junit.Test;

class AnchorParserTest {

	@Test
	public void testMarkdown() {
		MarkdownPlus markup = new MarkdownPlus();
		String markdownText = markup.markdown(md);
		println(markdownText)
		assertNotNull(markdownText)
		assertEquals(html,markdownText)
	}

	@Test
	public void testParseAutoLinks() {
		//fail("Not yet implemented");
	}

	@Test
	public void testStripLinkDefinitions() {
		//fail("Not yet implemented");
	}
	
def md = '''
test this
---------

test link [test this] and [sampleid] or use setup link text [link text][heading 3]

this should also work with a blank id [test this][]

and inside another set of brackets [[test this][]]

> ## test h2  [sampleid]
> some other text

### heading 3
'''

def html = '''<h2 id="test_this">test this</h2>

<p>test link <a href="#test_this" title="test this">test this</a> and <a href="#sampleid" title="test h2">sampleid</a> or use setup link text <a href="#heading_3" title="heading 3">link text</a></p>

<p>this should also work with a blank id <a href="#test_this" title="test this">test this</a></p>

<p>and inside another set of brackets [<a href="#test_this" title="test this">test this</a>]</p>

<blockquote>
  <h2 id="sampleid">test h2</h2>
  
  <p>some other text</p>
</blockquote>

<h3 id="heading_3">heading 3</h3>
'''
}
