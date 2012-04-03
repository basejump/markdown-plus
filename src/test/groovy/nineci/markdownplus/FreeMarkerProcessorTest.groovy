package nineci.markdownplus;

import org.junit.Ignore
import org.junit.Test;
import static org.junit.Assert.*;

class FreeMarkerProcessorTest {

	@Test
  @Ignore("Failing: Perhaps incomplete?")
	void testProcessContent() {
		def fmp = new FreeMarkerProcessor()
		def res = fmp.processContent([something:'exists'],simpleFtl)
		println res
		assertNotNull res
		assertEquals simpleFtlResult,res
	}

def simpleFtl='''
This is a basic freemarker test
===============================
<#include "foo.md">

<!-- html comment-->
<#assign x = 1>  <#-- create variable x -->
${x}
${something}
${dummyVar}
${TOC}
<#noparse>${TOC}</#noparse>
<% whatever you want here %>
'''

def simpleFtlResult ='''
This is a basic freemarker test
===============================

1
exists
dummyVal
'''
}
