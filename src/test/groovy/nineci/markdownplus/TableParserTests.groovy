package nineci.markdownplus;

import static org.junit.Assert.*;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;


import org.junit.Test;

import static org.junit.Assert.*;
import java.util.regex.Matcher
import java.util.regex.Pattern


class TableParserTests {
	
	@Test
	public void testTable(){
		MarkdownPlus markup = new MarkdownPlus();
		String markdownText = markup.markdown(tableMarkup);
		//println(markdownText)
		assertNotNull(markdownText)
		assertEquals(tableResults,markdownText)
	}
	
	@Test
	public void testTableNoBeginEndPipes(){
		String markdownText = new MarkdownPlus().markdown(tableMarkupBeginEndPipes);
		//println(markdownText)
		assertNotNull(markdownText)
		assertEquals(tableResults,markdownText)
	}
	@Test
	public void test2Tables(){
		String markdownText = new MarkdownPlus().markdown(tables2Markup);
		println(markdownText)
		assertNotNull(markdownText)
		assertEquals(tables2Results,markdownText)
	}

	
	def tableMarkup = '''
| Head1 | _Italic_		 | **Bold** |
|:------|---------------:|----------|
|a2     | _Italic_ 		 |   $100.00   |
|a3     | [Test](/test/) | **$200.00** |
'''
	
	def tableMarkupBeginEndPipes = '''
  Head1  | _Italic_		 | **Bold** 
 | :-|---------------:|-
 |a2     | _Italic_ 		 |   $100.00   
 a3     | [Test](/test/) | **$200.00** |  
'''
	
	def tableResults = '''<table>
<thead>
<tr>
   <th align='left'>Head1</th>
   <th align='right'><em>Italic</em></th>
   <th><strong>Bold</strong></th>
</tr>
</thead>
<tbody>
<tr>
   <td>a2</td>
   <td><em>Italic</em></td>
   <td>$100.00</td>
</tr>
<tr>
   <td>a3</td>
   <td><a href="/test/">Test</a></td>
   <td><strong>$200.00</strong></td>
</tr>
</tbody>
</table>
'''
	
def tables2Markup = '''
| Head1 | _Italic_		 | **Bold** |
|:------|---------------:|----------|
|a2     | _Italic_ 		 |   $100.00   |
|a3     | [Test](/test/) | **$200.00** |

| table2 | table2 | table2 |
|------|----------|----------|
|a2     | b2	|   		c2  |
'''
def tables2Results = '''<table>
<thead>
<tr>
   <th align='left'>Head1</th>
   <th align='right'><em>Italic</em></th>
   <th><strong>Bold</strong></th>
</tr>
</thead>
<tbody>
<tr>
   <td>a2</td>
   <td><em>Italic</em></td>
   <td>$100.00</td>
</tr>
<tr>
   <td>a3</td>
   <td><a href="/test/">Test</a></td>
   <td><strong>$200.00</strong></td>
</tr>
</tbody>
</table>

<table>
<thead>
<tr>
   <th>table2</th>
   <th>table2</th>
   <th>table2</th>
</tr>
</thead>
<tbody>
<tr>
   <td>a2</td>
   <td>b2</td>
   <td>c2</td>
</tr>
</tbody>
</table>
'''	
}