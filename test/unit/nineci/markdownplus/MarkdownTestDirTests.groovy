package nineci.markdownplus;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
/**
 * these run John Gruber's tests taken from the markdownj directory and modified for the heading id default chagnes to markdownplus
 * 
 */
class MarkdownTestDirTests {
	private final static String MARKDOWN_TEST_DIR = "/MarkdownTestFiles";
	
	@Test
	public void runTest() throws IOException {
		URL fileUrl = MarkdownTestDirTests.class.getResource(MARKDOWN_TEST_DIR);

		def flist = new File(fileUrl.getFile()).listFiles({dir, file-> file ==~ /.*?\.text/ } as FilenameFilter).toList()*.name 

		flist.each{
			String testName = it.substring(0, it.lastIndexOf('.'));
			String testText = slurp(MARKDOWN_TEST_DIR + File.separator + it);
			String htmlText = slurp(MARKDOWN_TEST_DIR + File.separator + testName + ".html");
			MarkdownPlus markup = new MarkdownPlus();
			String markdownText = markup.markdown(testText);
			assertEquals(it, htmlText.trim(), markdownText.trim());
		}
	}
	
	private String slurp(String fileName) throws IOException {
		URL fileUrl = this.getClass().getResource(fileName);
		File file = new File(URLDecoder.decode(fileUrl.getFile(), "UTF-8"));
		FileReader reader = new FileReader(file);
		StringBuffer sb = new StringBuffer();
		int ch;
		while ((ch = reader.read()) != -1) {
			sb.append((char) ch);
		}
		return sb.toString();
	}
}
