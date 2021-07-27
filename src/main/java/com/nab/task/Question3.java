package com.nab.task;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Hassan Hanif
 * @Date 2021-07-26
 * JDK: 11
 */

public class Question3 {
		
	private Properties prop;
	private Pattern regex = Pattern.compile("\\$\\{(.*?)\\}");
	private String templateText; 
	
	/**
	 * 
	 * @param propFileName
	 * @param templateFileName
	 * @throws Exception
	 * 
	 * Construct Class by loading properties file and template file
	 */
	public Question3(String propFileName, String templateFileName) throws IOException {	
		this.prop = new Properties();
		this.templateText = "";
		
		try {
			this.prop.load(getClass().getClassLoader().getResourceAsStream(propFileName));
			this.templateText = new String(getClass().getClassLoader().getResourceAsStream(templateFileName).readAllBytes());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		
		
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 * 
	 * Reads and returns given property value OR if property refers to another propety
	 * then pass it to parseStringForProperties method.
	 */
	public String getProperty(String propertyName) {
		
		String propertyValue = prop.getProperty(propertyName);		
		String populatedString = "";
		
		if (propertyValue != null && !propertyValue.trim().equalsIgnoreCase("")) {
			populatedString = parseStringForProperties(propertyValue);
		}
		return populatedString.equalsIgnoreCase("")?propertyValue:populatedString;		
	}
	
	/**
	 * 
	 * @param parseString
	 * @return
	 * 
	 * Parses and Renders template text or properties referring to other properties.
	 * NOTE: This method is used for both template parsing/rendering and properties 
	 * referring to other properties. 
	 */
	public String parseStringForProperties(String parseString) {
				
		Matcher regexMatcher = regex.matcher(parseString);
		StringBuffer sb = new StringBuffer("");
		
		int lastIndex = 0;
		
		while (regexMatcher.find()) {
			sb.append(parseString, lastIndex, regexMatcher.start());
			
			if (getProperty(regexMatcher.group(1)) == null) {
				sb.append("${"+regexMatcher.group(1)+"}");
			} else {
				sb.append(getProperty(regexMatcher.group(1)));
			}
			lastIndex = regexMatcher.end();
		}
		
		if (lastIndex < parseString.length()) {
		    sb.append(parseString, lastIndex, parseString.length());
		}
		return sb.toString();
		
	}
	
	/**
	 * 
	 * @return
	 * 
	 * This method passes template text to parseStringForProperties method
	 * for rendering/parsing.
	 */
	public String renderText() {
		return this.parseStringForProperties(this.templateText);
	}

	public static void main(String[] args) {
		
		Question3 q3a = null;
		Question3 q3b = null;
		try {
			q3a = new Question3("input_basic.properties", "template_basic.btl");		
			q3b = new Question3("input_adv.properties", "template_adv.btl");
		} catch (IOException e) {
			System.out.println("Resource File(s) Not Found! "+e.getMessage());
		}
		
		System.out.println(q3a.renderText());	
		System.out.println(q3b.renderText());	
	}

}
