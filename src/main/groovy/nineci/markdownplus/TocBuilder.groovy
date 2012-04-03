/*
 Copyright (c) 2010 9ci Inc
 Authors: Joshua Burnett
 http://www.9ci.com/markdownplus
 */
package nineci.markdownplus;

import java.util.List;
import groovy.xml.StreamingMarkupBuilder
import org.jdom.output.XMLOutputter
import org.jdom.*
import org.jdom.filter.*
import org.jdom.input.SAXBuilder

/**
 * Builds a table of contents from the html headers (h1,h2, etc...)
 * Spits out a ul/li html list that can be styled with css. Will also optionally prepend the numbers to the beginning of the h2 values
 *
 * @author Joshua Burnett
 *
 */
class TocBuilder {
	//base header level to start with. if its 2 then it ignores the h1 and drills through h2, h3 etc..
	// would mostly be used if there is only 1 H1 for the title and not normally a best practice
	int topLevel = 1

	//the header(h) level to go down to (3 = drill to the h3 header)
	int depth = 3

	//prepend toc index number to Header, default is true
	boolean prependTocNumber = true

	//what html element to look in.default is nothing which means it looks directly under the root element. body would be a normal overide
	def parentTag

	//pass in the html and out comes an list or list of lists for the table of contents
	String build(StringBuffer html){

		def hsearch = []
		for(i in topLevel..depth){
			hsearch.add("h$i")
		}

		def builder = new SAXBuilder(false)
		builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
		def document  = builder.build(new StringReader(html.toString()))
		def rootElement = (parentTag ? document.rootElement.getChild(parentTag) : document.rootElement)

		def headerList = rootElement.children.findAll{el->
			hsearch.find{it==el.name}
		}

		//return blank string if its empty
		if (headerList.isEmpty()){
			return ""
		}

		//turn the headers into a heirarchical list
		List toc = []
		headerList.each{el->
			int curlevel= el.name.charAt(1).toString().toInteger()
			def baseList = []
			if(curlevel==topLevel){ //add a new base level
				baseList = toc
				//toc.add(["text":el.text(),id:el.@id,children:[]])
			}else{
				baseList = toc.last()
				for (int idex = topLevel; idex < curlevel-1; ++idex){
					baseList = (baseList.children.isEmpty()) ? baseList.children : baseList.children.last()
				}
				baseList = baseList.children
			}
			baseList.add([element:el,children:[]])
			//println baseList
		}

		def tocHtml = makeTocList(toc,"").trim()
		if(prependTocNumber){
			def writer = new StringWriter()
			//if root element was dummy then it was just wrapped for this so output just the children
			//def elToOutput =
			new XMLOutputter().output(document.rootElement, writer)
			html.length =0
			html.append writer.buffer
			//println html.toString()
		}
		//println writer.toString()
		return tocHtml
	}


	/**
	 * takes a list of maps and returns the html with the ul->li table fo contents list
	 *
	 * @param els a list of maps in the form
	 * 		[text:"the header text",id:"the id of the header", children
	 * @param tocNum - blank if this is the top, recursive calls with the current toc number
	 * @return turns the html with the ul->li table fo contents list
	 */
	String makeTocList(List elements,String tocNum){
		StringBuilder tocHtml = new StringBuilder("\n<ul class=\"toc\">\n")
		elements.eachWithIndex{el,i->
			String tocLevelNum
			tocHtml << "<li>"
			if(!tocNum){
				tocLevelNum="${i+1}"
			}else{
				tocLevelNum="${tocNum}.${i+1}"
			}
			tocHtml << '<a href="#' << (el.element.getAttributeValue("id") ?: '') << '">'
			tocHtml << "<span class='toc-number'>$tocLevelNum${(tocNum?'':'.')} </span>"
			def txt = getAllText(el.element).trim()
			tocHtml << "<span class='toc-text'>$txt</span></a>"

			//replace
			if(prependTocNumber){
				def textEl = getFirstTextNode(el.element)
				textEl.text =  "$tocLevelNum${(tocNum?'':'.')} $textEl.text"
			}

			if(!el.children.isEmpty()){
				tocHtml << makeTocList(el.children,tocLevelNum)
			}

			tocHtml << "</li>\n"
		}
		tocHtml << "</ul>"
		return tocHtml
	}


	/**
	 * Pass in the HTML and it will modify the
	 * @return
	 */
	String addTocNumToHeader(){

	}

	/**
	 * Return the first child node of type Text, searching recursively
	 * from the given node.  Search is depth-first.
	 */
	Text getFirstTextNode(node){
		def filter = new ContentFilter(ContentFilter.TEXT | ContentFilter.ELEMENT);
		if (node instanceof Text){
			return node
		}

		// node instanceof Element
		def content = node.getContent(filter);

		for (contentNode in content){
			def result = getFirstTextNode(contentNode);
			if (result != null){
				return result;
			}
		}
		return null;
	}

	/**
	 * Return the text of all nodes at and below the given node, searching
	 * recursively.  Search is depth-first.
	 */
	String getAllText(node){
		def textList = []
		getAllText(node, textList)

		def buf = new StringBuffer()

		for (text in textList){
			buf << text << " "
		}
		return buf.toString();
	}

	/**
	 * Find the text of all nodes at and below the given node, searching
	 * recursively.  Search is depth-first.  Append the text found to
	 * the specified textList.
	 */
	void getAllText(node, textList){
		if (node instanceof Text){
			textList.add(node.getTextTrim());
		}
		else{
			def contentNodes = node.getContent(new ContentFilter(ContentFilter.TEXT | ContentFilter.ELEMENT));

			for (contentNode in contentNodes){
				getAllText(contentNode, textList);
			}
		}
	}









	//pass in the html and out comes an list or list of lists for the table of contents
	String buildParserOld(String html){
		def parser = new XmlParser(false,false)
		parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
		def root= parser.parseText(html)

		def hsearch = []
		for(i in topLevel..depth){
			hsearch.add("h$i")
		}
		def rootNode = (parentTag ? root."$parentTag"[0].children() : root)
		def headerList = rootNode.findAll{el->
			hsearch.find{it==el.name()}
		}

		//return blank string if its empty
		if (headerList.isEmpty()){
			return ""
		}

		List toc = []
		headerList.each{el->
			int curlevel= el.name().charAt(1).toString().toInteger()
			//println "${el.text()} ${el.@id}  $curlevel"
			def baseList = []
			if(curlevel==topLevel){ //add a new base level
				baseList = toc
				//toc.add(["text":el.text(),id:el.@id,children:[]])
			}else{
				baseList = toc.last()
				for (int idex = topLevel; idex < curlevel-1; ++idex){
					baseList = (baseList.children.isEmpty()) ? baseList.children : baseList.children.last()
				}
				baseList = baseList.children
			}
			baseList.add(["text":el.text(),id:el.@id,children:[]])
		}
		//
		//		def writer = new StringWriter()
		//		def nodePrinter = new XmlNodePrinter(new PrintWriter(writer),"")
		//		nodePrinter.preserveWhitespace=true
		//		nodePrinter.namespaceAware=true
		//		nodePrinter.print(root)
		//		def result = writer.toString()
		//		println result

		def builder = new SAXBuilder()
		builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
		def document  = builder.build(new StringReader(html))
		Namespace xhtml = Namespace.getNamespace( "xhtml", "http://www.w3.org/1999/xhtml");
		document.rootElement.getChild("body",xhtml).children.each{
			println it.text = "test $it.text"
		}
		def writer = new StringWriter()
		new XMLOutputter().output(document, writer)
		println writer.toString()



		def tocHtml = '<div id="toc" class="toc">\n' << makeTocList(toc,"").trim() << '\n</div>'
		return tocHtml
	}


}
