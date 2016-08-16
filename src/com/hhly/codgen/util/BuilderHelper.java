package com.hhly.codgen.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class BuilderHelper {
	
	/**
	 * 解析freeMarker构建参数
	 * @param param
	 * @param dataMap
	 * @return
	 */
	public static String parseBuilderParam(String param, Map<String, Object> dataMap){
		Configuration configuration = new Configuration();
		configuration.setTemplateLoader(new StringtemplateLoader(param));
		configuration.setDefaultEncoding("utf-8");
		StringWriter writer = new StringWriter(); 
		try {
			Template template = configuration.getTemplate("");
			template.process(dataMap, writer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	public static void main(String[] args) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("name", "ttff");
		System.out.println(parseBuilderParam("welcome ${name}!!!", dataMap));
	}

}
