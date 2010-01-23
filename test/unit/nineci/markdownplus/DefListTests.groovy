package nineci.markdownplus;


import org.junit.Test;

import static org.junit.Assert.*;


class DefListTests {
	
	@Test
	void testDef(){
		MarkdownPlus markup = new MarkdownPlus();
		String markdownText = markup.markdown(defListMd);
		println(markdownText)
		assertNotNull(markdownText)
		assertEquals(defListHtml,markdownText)
	}
	
//	@Test
//	void testRegEx(){
//		String tval = "asfd\n	\nasdfads\n"
//		//def numberMatcher = tval =~ /\n[ ]*/ 
//		//def numberMatcher = tval =~ /.*\n[ ]*\n.*/  
//		println bob
//		def matcher = (tval =~ /.*\n[\s\t]*\n.*/)  
//		assert (bob =~ /\n\s*\n/).find() 
//		//assert (tval ==~ /.*\n[\s\t]*\n.*/)   // TRUE
//		
//	}



def defListMd = '''
Apple 
 :	Pomaceous fruit of **plants** of the genus 
	Malus in the *family* Rosaceae.

 :	its red and tasty

Orange

:   This definition has a code block, a blockquote and a list.

        code block.

    > block quote
    > on two lines.

    1.  first list item
    2.  second list item


'''
	
def defListHtml = '''<dl>
<dt>Apple</dt>
<dd>Pomaceous fruit of <strong>plants</strong> of the genus 
    Malus in the <em>family</em> Rosaceae.</dd>
<dt>Orange</dt>
<dd><p>This definition has a code block, a blockquote and a list.</p>

<pre><code>code block.
</code></pre>

<blockquote>
  <p>block quote
  on two lines.</p>
</blockquote>

<ol>
<li>first list item</li>
<li>second list item</li>
</ol></dd>
</dl>
'''
}
