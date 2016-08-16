package com.hhly.codgen.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.cache.TemplateLoader;

/**
 * freemarker ×Ö·û´®Ä£°åÀà
 * @author Administrator
 *
 */
public class StringtemplateLoader implements TemplateLoader{
	
	private String defaultTemplateName = "_defaultTemplateName";
	private Map<String, Object> templates = new HashMap<>();
	
	public StringtemplateLoader(String paramStr) {
		if(StringUtils.isNotBlank(paramStr)){
			templates.put(defaultTemplateName, paramStr);
		}
	}

	public void AddTemplate(String name, String value){
		if(StringUtils.isBlank(name)){
			name = defaultTemplateName;
		}
		if(!templates.containsKey(name)){			
			templates.put(name, value);
		}
	}
	
	@Override
	public Object findTemplateSource(String s) throws IOException {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(s)){
			s = defaultTemplateName;
		}
		return templates.get(s);
	}

	@Override
	public Reader getReader(Object obj, String s) throws IOException {
		// TODO Auto-generated method stub
		StringReader reader = new StringReader((String)obj);
		return reader;
	}
	@Override
	public long getLastModified(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void closeTemplateSource(Object obj) throws IOException {
		// TODO Auto-generated method stub
	}
	
}
