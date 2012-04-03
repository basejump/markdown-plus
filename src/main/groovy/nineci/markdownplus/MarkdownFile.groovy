package nineci.markdownplus;

import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;

class MarkdownFile {
	File file
	
	public MarkdownFile() {}
	
	public MarkdownFile(URL url) {
		def file = new File(url.getFile())
	}
	public MarkdownFile(File file) {
		this.file = file
	}
	
	def process(){
		//read file and process markdown
		MarkdownPlus mdp = new MarkdownPlus()
		String markdown = mdp.markdown(file.text)
		//println markdown

		//get template and fill
		def templateName = mdp.metaDataParser.data.template?:"doc.html" //default is doc
		def templateHome = System.getProperty("markdownplus.home") + "/templates"
		def templateFile = new File(templateHome,templateName)
		//TODO add checks to current directory for the template name too.
		//URL templateUrl = MarkdownFile.class.getResource("/nineci/markdownplus/templates/doc.html");
		String basicTemplate = templateFile.text
		
		//TODO do the header metadata and any css or scripts
		basicTemplate = basicTemplate.replaceAll(/\$\{head\}/, "")
		basicTemplate =replaceMeta(mdp.metaDataParser.data,basicTemplate,markdown)
		
		//build and insert the table of contents
		def mdpBuffer = new StringBuffer("<div id='content' class='container'>\n" << markdown << "\n</div><!--end Content-->")
		def toc = new TocBuilder().build(mdpBuffer)
		basicTemplate = basicTemplate.replace('${toc}', toc)
		
		basicTemplate = basicTemplate.replace('${body}', mdpBuffer.toString())
		

		//write fill out
		def baseName = file.name.substring(0, file.name.lastIndexOf('.'))
		def htmlFile= new File("${baseName}.html")
		htmlFile.write(basicTemplate) //can only write strings
		
		createPdf(htmlFile)
	}
	
	def createPdf(File htmlFile){

		def baseName = file.name.substring(0, file.name.lastIndexOf('.'))
		def pdfFile= new File("${baseName}.pdf")
		
		def os = new FileOutputStream(pdfFile)
	
		ITextRenderer renderer = new ITextRenderer()
		renderer.setDocument(htmlFile)
		renderer.layout()
		renderer.createPDF(os)
		
		os.close()
	}
	
	def replaceMeta(Map data,String template, String markdown){
		def markdownString = markdown.toString()
		data.each{key,val->
			def repKey = ('${'+key+'}').toLowerCase()
			//println "replacing $repKey with $val"
			if(val instanceof ArrayList){
				val = val.join('<br/>')
			}
			template = template.replace(repKey, val)
		}
		return template
	}
	
	public static void main(String[] args) {
		println System.getProperty("markdownplus.home")
//		System.getProperties().each{
//			println it
//		}
//		println MarkdownFile.class.getResource("/")
		for (arg in args){
			def f = new File(arg)
			def mdf = new MarkdownFile(f)
			mdf.process()
		}
	}
}
