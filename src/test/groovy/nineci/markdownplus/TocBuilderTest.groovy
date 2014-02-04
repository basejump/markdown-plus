package nineci.markdownplus;

import org.junit.Test;

import static org.junit.Assert.*;

class TocBuilderTest {

	@Test
	void testBuild(){
		def tocBuilder = new TocBuilder(parentTag:'body')
		def toc = tocBuilder.build(new StringBuffer(html))
		println toc
		assertEquals tocResult,toc
	}

	//test with h2 as the top level
	@Test
	void testBuild_H2TopLevel(){
		def tocBuilder = new TocBuilder(parentTag:'body',topLevel:2)
		tocBuilder.topLevel=2
		def toc = tocBuilder.build(new StringBuffer(html))
		println toc
		assertEquals tocResultH2,toc
	}

	//test with h2 as the top level
	@Test
	void testBuild_H2TopLevel_Depth4(){
		def tocBuilder = new TocBuilder(topLevel:2,depth:4)
		def toc = tocBuilder.build(("<dummy>\n" << tocHtml_H2_depth4 << "\n</dummy>"))
		println toc
		assertEquals tocResult_H2_depth4,toc
	}

def html='''<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<!--comment-->
</head>
<body>
<h1 id="toc1">toc 1 <em>blah</em></h1>
<h1>toc 2</h1>
<h2 id="toc2-1">toc 2-1</h2>
<div><h3>this is inside a div and will be skipped</h3></div>
<h2 id="toc2-2">toc 2-2</h2>
<h3 id="toc2-2-1">toc 2-2-1</h3>
<h4 id="toc2-2-1">h4 2-2-1-1</h4>
<h1 id="toc3">toc 3</h1>
<p>asdf <span class="asdf"> adsf asdf </span>
	asdf <h2>this is inside a pararagraph and will be skipped</h2> asdfasdf </p>
<h4 id="toc2-2-1">this will get used if topLeve is 2 since the parent h1 is ignored</h4>
<div><h3>this is inside a div and will be skipped</h3></div>
</body>
</html>
'''
def tocResult='''
<ul class="toc">
<li><a href="#toc1"><span class='toc-number'>1. </span><span class='toc-text'>toc 1 blah</span></a></li>
<li><a href="#"><span class='toc-number'>2. </span><span class='toc-text'>toc 2</span></a>
<ul class="toc">
<li><a href="#toc2-1"><span class='toc-number'>2.1 </span><span class='toc-text'>toc 2-1</span></a></li>
<li><a href="#toc2-2"><span class='toc-number'>2.2 </span><span class='toc-text'>toc 2-2</span></a>
<ul class="toc">
<li><a href="#toc2-2-1"><span class='toc-number'>2.2.1 </span><span class='toc-text'>toc 2-2-1</span></a></li>
</ul></li>
</ul></li>
<li><a href="#toc3"><span class='toc-number'>3. </span><span class='toc-text'>toc 3</span></a></li>
</ul>
'''.trim()

def tocResultH2='''
<ul class="toc">
<li><a href="#toc2-1"><span class='toc-number'>1. </span><span class='toc-text'>toc 2-1</span></a></li>
<li><a href="#toc2-2"><span class='toc-number'>2. </span><span class='toc-text'>toc 2-2</span></a>
<ul class="toc">
<li><a href="#toc2-2-1"><span class='toc-number'>2.1 </span><span class='toc-text'>toc 2-2-1</span></a></li>
</ul></li>
</ul>
'''.trim()

def tocHtml_H2_depth4='''
<h1>Some Title</h1>
<h2 id="toc2-1">toc 2-1</h2>
<h2 id="toc2-2">toc 2-2</h2>
<h3 id="toc2-2-1">toc 2-2-1</h3>
<h4 id="toc2-2-1">h4 2-2-1-1</h4>
<h4 id="toc2-2-1">this will get used if topLeve is 2 since the parent h1 is ignored</h4>
'''
def tocResult_H2_depth4='''
<ul class="toc">
<li><a href="#toc2-1"><span class='toc-number'>1. </span><span class='toc-text'>toc 2-1</span></a></li>
<li><a href="#toc2-2"><span class='toc-number'>2. </span><span class='toc-text'>toc 2-2</span></a>
<ul class="toc">
<li><a href="#toc2-2-1"><span class='toc-number'>2.1 </span><span class='toc-text'>toc 2-2-1</span></a>
<ul class="toc">
<li><a href="#toc2-2-1"><span class='toc-number'>2.1.1 </span><span class='toc-text'>h4 2-2-1-1</span></a></li>
<li><a href="#toc2-2-1"><span class='toc-number'>2.1.2 </span><span class='toc-text'>this will get used if topLeve is 2 since the parent h1 is ignored</span></a></li>
</ul></li>
</ul></li>
</ul>
'''.trim()
}

