package nineci.markdownplus;

import java.util.regex.*;

/*
 * Every line must be either something : something followed by either a tab something
 */
class MetaDataParser {
	
	//MarkdownPlus markdownPlus
	Map data = [:]

	MarkdownText parse(MarkdownText text) {
		def firstLinePat=/\A([a-zA-Z0-9][0-9a-zA-Z _-]*?):(.*)\n/+
		/[^\=\-]/  //not followed by a - or = which means its a heading and we will leave it alone
		Matcher m = (text.toString() =~ firstLinePat)
		//if the first line is found to match and has a value then it has a least 1 meta value
		if(m.find() && m.group(2).trim()){
			//make sure its not a url which means they just started typing
			if(m.group(1).find(/(https?|ftp)/)) return text
			
			String curKey
			StringBuffer newText = new StringBuffer()
			def noMoreMeta = false
			//TODO I don't spinning through the entire file like this but I didn't have time to figure out a faster way
			text.toString().eachLine{line->
				//as soon as we run into a blank line stop
				if(noMoreMeta || !line.trim()){
					noMoreMeta = true
					newText<<line<<"\n"
					return
				}
				Matcher lm = (line =~ /([a-zA-Z0-9][0-9a-zA-Z _-]*?):\s*(.*)/)
				if(lm.find()){
					curKey = lm.group(1).trim().toLowerCase()
					data[curKey] = lm.group(2).trim()
					//println "key is : [${lm.group(1)}] and val is:[${lm.group(2)}]"
				}else{
					if(data[curKey].class == ArrayList){
						data[curKey] << line.trim()
					}else{
						data[curKey] = [data[curKey],line.trim()]
					}
				}
			}
			text.text = newText
		}
		return text
	}
}
