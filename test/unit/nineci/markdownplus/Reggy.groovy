package nineci.markdownplus;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.regex.*;

//just a scratch pad for regex tests
class Reggy {
	
	@Test
	public void testPat() {
		def metaPat =/([a-zA-Z0-9][0-9a-zA-Z _-]*?):\s*(.*)/
		def pat2=/(?m)\A/ +
		/(^$metaPat\n)+/+
		/(\s*.*\n)+/	+ // at least followed by something and not blank lines
		/(?=\n+)/	 // stop at the first 1 or more blank  lines
		
		//def pat=/(?m)\A([a-zA-Z0-9][0-9a-zA-Z _-]*?):(\s*.*\n)(^.*\n)+(?=\s*\n+)/ 
		def pat=/\A([a-zA-Z0-9][0-9a-zA-Z _-]*?):(.*)\n/
		Matcher m = (stuff =~ pat)
		//if the first line has a property then make it happen
		if(m.find() && m.group(2).trim()){
			println "group ${m.group()}"
			def endProc = false
			def lines = stuff.tokenize('\n')
			int endOfMeta = 0
			for(line in lines){
				endOfMeta = endOfMeta + line.length() +1 //add 1 for the \n we tokenized
				if(!line.trim()) break
				Matcher lm = (line =~ metaPat)
				if(lm.find()){
					println "key is : [${lm.group(1)}] and val is:[${lm.group(2)}]"
				}else{
					println "goes to prior array ${line.trim()}"
				}
				//println "$line"
			}
			StringBuffer sb = new StringBuffer(stuff)
			println "stuff is "+stuff
			sb.delete(0, endOfMeta)
			println "buffer is "+sb
		}
//		assertNotNull m
//		while(m.find()){
//			println "group ${m.group()}"
//			Matcher m2 = (m.group() =~ metaPat)
//			while(m2.find()){
//				println "key is : [${m2.group(1)}] and val is:[${m2.group(2)}]"
//			}
//		}
	}
//	def metaPat =/([a-zA-Z0-9][0-9a-zA-Z _-]*?):\s*(.*)/
//	def pat=/(?m)\A/ 	+ // \A means start at the beggining of string (?m) = multiline match
//			/(^$metaPat\n)/	+ //
//			/(\s*.*\n)+/	+ // at least followed by something and not blank lines
//			/(?=\n+)/	 // stop at the first 1 or more blank  lines
//	//grabs everything from beginning of file until 2 or more linefeeds
//	//def until_2_line_Feeds=/(?m)\A(^.*\n)+(?=\s*\n+)/ 
//	Matcher m = (text.toString() =~ pat)	
	
//	@Test
//	public void testPat() {
//		def metaPat =/([a-zA-Z0-9][0-9a-zA-Z _-]*?):\s*(.*)/
//		def pat=/(?m)\A(^.*\n)+(?=\s*\n+)/ 
//		Matcher m = (stuff =~ pat)
//		assertNotNull m
//		while(m.find()){
//			println "got this ${m.group()}"
//			Matcher m2 = (m.group() =~ metaPat)
//			while(m2.find()){
//				println "key is : [${m2.group(1)}] and val is:[${m2.group(2)}]"
//			}
//		}
//	}
	
def stuff='''
dummy : adsfasdf
title:				MultiMar:kdown User's Guide    
Subtitle:			Version 2.0.b6
Author:				Fletcher T. Penney
Web:				http://fletcherpenney.net/
Keywords:			Markdown  
					LaTeX  
					TeX  
Copyright:			2005-2009 Fletcher T. Penney.	
	   
this should not be picked up : because its not a def
'''.trim()
}
