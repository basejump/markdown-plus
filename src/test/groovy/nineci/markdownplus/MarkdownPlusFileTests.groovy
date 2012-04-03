package nineci.markdownplus;

import org.junit.Test;

import static org.junit.Assert.*;

class MarkdownPlusFileTests {
	private final static String MARKDOWN_TEST_DIR = "/MarkdownPlusFiles";

	@Test
	public void sandbox() throws IOException {
		URL templateUrl = MarkdownTestDirTests.class.getResource("/nineci/markdownplus/templates/basic.html");
		URL mdfile = MarkdownTestDirTests.class.getResource("/MarkdownPlusFiles/basictest.md");
		MarkdownFile mdoc = new MarkdownFile(mdfile)
		mdoc.process()
		//String html = mdoc.toHtml(url)
		
		
//		//get the basic.html template
//		
//		def basicTemplate = new File(templateUrl.getFile()).text
//		basicTemplate = basicTemplate.replaceAll(/\$\{head\}/, "Test Head")
//		basicTemplate = basicTemplate.replaceAll(/\$\{body\}/, "Test body")
//		
//		println basicTemplate
		
	}

		
	//@Test
	public void runTest() throws IOException {
		URL fileUrl = MarkdownTestDirTests.class.getResource(MARKDOWN_TEST_DIR);
		
		def flist = new File(fileUrl.getFile()).listFiles({dir, file-> file ==~ /.*?\.md/ } as FilenameFilter).toList()*.name 
		assert flist.size() > 0
		flist.each{
			String testName = it.substring(0, it.lastIndexOf('.'));
			String testText = slurp(MARKDOWN_TEST_DIR + File.separator + it);
			String htmlText = slurp(MARKDOWN_TEST_DIR + File.separator + testName + ".html");
			MarkdownPlus markup = new MarkdownPlus();
			String markdownText = markup.markdown(testText);
			println markdownText
			assertEquals(it, htmlText.trim(), markdownText.trim());
		}
	}
	
	private String slurp(String fileName) throws IOException {
		URL fileUrl = this.getClass().getResource(fileName);
		File file = new File(URLDecoder.decode(fileUrl.getFile(), "UTF-8"));
		println file.text
		return file.text
//		FileReader reader = new FileReader(file);
//		StringBuffer sb = new StringBuffer();
//		int ch;
//		while ((ch = reader.read()) != -1) {
//			sb.append((char) ch);
//		}
//		return sb.toString();
	}
}
