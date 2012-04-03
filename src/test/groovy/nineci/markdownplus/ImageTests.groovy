package nineci.markdownplus;

import org.junit.Test;

import static org.junit.Assert.*;

class ImageTests {
	
	@Test
	public void testMarkdown() {
		MarkdownPlus markup = new MarkdownPlus();
		String markdownText = markup.markdown(md);
		println(markdownText)
		assertNotNull(markdownText)
		assertEquals(html,markdownText)
	}

	
def md = '''
Payment  ![](../images/icons/gridView.png "Grid")  or Form  ![](../images/icons/formView.png "Form")  view:
'''
	
def html = '''<p>Payment  <img src="../images/icons/gridView.png" alt="" title="Grid" />  or Form  <img src="../images/icons/formView.png" alt="" title="Form" />  view:</p>
'''
}
