package nineci.markdownplus;

import org.junit.runner.RunWith;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runners.Suite;

@RunWith(Suite.class) 
@Suite.SuiteClasses({
	AnchorParserTest.class,
	DefListTests.class,
	HeadingParserTest.class,
	HrParserTest.class,
	ListParserTest.class,
	TableParserTests.class,
	MetaDataParserTest.class,
	TocBuilderTest.class,
	MarkdownjFileTests.class,
	MarkdownTestDirTests.class
})

public class AllTests {


}
