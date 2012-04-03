package nineci.markdownplus;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.core.Macro;

class FreeMarkerProcessor {

	static TEMPLATE_NAME = "invoice"
	
	//context map is simpley a map of anything you want to use in freemarker and is just like passing them into a gsp page.
	// most often it will be one or more domain objects.
	//example map could be as simple as [book:Book.find(id)]
	//in freemarker template you then access them like ${book.title} or ${book.author.name} etc..
	def getTemplate(templateName,templateContent) {
		Configuration freeconfig = new Configuration()
		freeconfig.setSharedVariable 'dummyVar', 'dummyVal'
		freeconfig.setDateTimeFormat("MMM dd, yyyy")
		freeconfig.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);  
		//Reader templateReader = configuration.getTemplateLoader().getReader(template,"UTF-8")
		Reader templateReader = new StringReader(templateContent)
		Template htmlTemplate = new Template(templateName,templateReader,freeconfig)
		return htmlTemplate
	}
	
	//returns a string with the processed freemarker template
	String processContent(Map dataModel,templateContent) {
		Template htmlTemplate = getTemplate(TEMPLATE_NAME,templateContent)
		return processTemplate(dataModel,htmlTemplate)
	}
	
	//returns a string with the processed freemarker template with the addition of the macros
	def processContent(Map dataModel,templateContent,macroName,macroTemplateSource) {
		Template htmlTemplate = getTemplate(TEMPLATE_NAME,templateContent)
		Template macroTemplate = getTemplate(TEMPLATE_NAME,macroTemplateSource)
		for (Iterator it = macroTemplate.getMacros().values().iterator(); it.hasNext();) {
			Macro add = (Macro) it.next()
			htmlTemplate.addMacro(add)
		}
		return processTemplate(dataModel,htmlTemplate)
	}
	
	String processTemplate(Map dataModel, Template htmlTemplate) {
		StringWriter htmlWriter = new StringWriter()
		htmlTemplate.process(dataModel, htmlWriter)
		return htmlWriter.toString()
	}

}
