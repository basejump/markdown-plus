/*
 * Copyright (c) 2005, Pete Bevin.
 * <http://markdownj.petebevin.com>
 */


package nineci.markdownplus;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

class MarkdownjFileTests {
	static String MARKDOWNJ_TEST_DIR = "/MarkdownJFiles";
	
	@Test
	public void runTest() {
		
		def testList = []
		['/dingus.txt','/paragraphs.txt','/snippets.txt','/lists.txt'].each{
			testList.addAll(newTestResultPairList(it));
		}
		
		testList.each{pair->
			println("ran ${pair.toString()}")
			MarkdownPlus markup = new MarkdownPlus();
			assertEquals("${pair.toString()} should succeed", pair.getResult().trim(), markup.markdown(pair.getTest()).trim());
		}
		//assertTrue(false);
	}
	
	public static List<TestResultPair> newTestResultPairList(String filename) throws IOException {
		List<TestResultPair> list = new ArrayList<TestResultPair>();
		URL fileUrl = MarkdownjFileTests.class.getResource("${MARKDOWNJ_TEST_DIR}${filename}")
		FileReader file = new FileReader(fileUrl.getFile());
		BufferedReader reader = new BufferedReader(file);
		StringBuffer test = null;
		StringBuffer result = null;
		
		Pattern pTest = Pattern.compile("# Test (\\w+) \\((.*)\\)");
		Pattern pResult = Pattern.compile("# Result (\\w+)");
		String line;
		int lineNumber = 0;
		
		String testNumber = null;
		String testName = null;
		StringBuffer curbuf = null;
		while ((line = reader.readLine()) != null) {
			lineNumber++;
			Matcher mTest = pTest.matcher(line);
			Matcher mResult = pResult.matcher(line);
			
			if (mTest.matches()) { // # Test
				addTestResultPair(list, test, result, testNumber, testName);
				testNumber = mTest.group(1);
				testName = mTest.group(2);
				test = new StringBuffer();
				result = new StringBuffer();
				curbuf = test;
			} else if (mResult.matches()) { // # Result
				if (testNumber == null) {
					throw new RuntimeException("Test file has result without a test (line " + lineNumber + ")");
				}
				String resultNumber = mResult.group(1);
				if (!testNumber.equals(resultNumber)) {
					throw new RuntimeException("Result " + resultNumber + " test " + testNumber + " (line " + lineNumber + ")");
				}
				
				curbuf = result;
			} else {
				curbuf.append(line);
				curbuf.append("\n");
			}
		}
		
		addTestResultPair(list, test, result, testNumber, testName);
		
		return list;
	}
	
	private static void addTestResultPair(List list, StringBuffer testbuf, StringBuffer resultbuf, String testNumber, String testName) {
		if (testbuf == null || resultbuf == null) {
			return;
		}
		
		String test = chomp(testbuf.toString());
		String result = chomp(resultbuf.toString());
		
		String id = testNumber + "(" + testName + ")";
		
		list.add(new TestResultPair(id, test, result));
	}
	
	private static String chomp(String s) {
		int lastPos = s.length() - 1;
		while (s.charAt(lastPos) == '\n' || s.charAt(lastPos) == '\r') {
			lastPos--;
		}
		return s.substring(0, lastPos + 1);
	}
	
}
