package com.hhly.codgen.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hhly.codgen.model.InOutType;
import com.hhly.codgen.model.OutputModel;
import com.hhly.codgen.service.BuildConfig;
import com.hhly.codgen.service.Builder;
import com.hhly.codgen.util.StringtemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CodeBuilder implements Builder{
	private Log log = LogFactory.getLog(getClass());
	
	private String encoding = "GBK";
	
	private Locale locale = Locale.CHINA;
	
	private Map<String, Object> dataModelMap = new LinkedHashMap<>();
	
	private Map<String, OutputModel> outputModelMap = new LinkedHashMap<>();
	
	public CodeBuilder(Map<String, Object> dataModelMap, Map<String, OutputModel> outputModelMap) {
		this.dataModelMap = dataModelMap;
		this.outputModelMap = outputModelMap;
	}
	
	public CodeBuilder(BuildConfig buildConfig){
		this.dataModelMap = buildConfig.getDataModelMap();
		this.outputModelMap = buildConfig.getOutputModelMap();
		this.encoding = buildConfig.getOutputEncoding();
	}
	
	@Override
	public Map<String, OutputModel> build() {
		Configuration configuration = new Configuration();
		configuration.setEncoding(locale, encoding);
		
		File templateFile = null, outputFile = null;
		Template template = null;
		PrintWriter pw = null;
		try {
			if(outputModelMap != null){
				for(Map.Entry<String, OutputModel> entry : outputModelMap.entrySet()){
					if(entry.getValue().getDisabled() == true){ //配置文件设置disabled为true不输出
						continue;
					}
					
					if(entry.getValue().getTemplateModel().getType() == InOutType.FILE){
						templateFile = new File(entry.getValue().getTemplateModel().getTemplate());
						if(!templateFile.exists()){
							throw new IOException(String.format("模板%s不存在", templateFile));
						}
						configuration.setDirectoryForTemplateLoading(templateFile.getParentFile());
						template = configuration.getTemplate(templateFile.getName(), locale);
					}else{
						configuration.setTemplateLoader(new StringtemplateLoader(entry.getValue().getTemplateModel().getTemplate()));
						template = configuration.getTemplate("");
					}
					
					template.setEncoding(encoding);
					if(entry.getValue().getType() == InOutType.FILE){
						outputFile = new File(entry.getValue().getOutput());
						if(!outputFile.getParentFile().exists()){
							outputFile.getParentFile().mkdirs();
						}
						pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), encoding));
						template.process(dataModelMap, pw);
						log.debug("构建并输出文件:" + outputFile.getAbsolutePath());
					}else{
						StringWriter sw = new StringWriter();
						template.process(dataModelMap, sw);
						entry.getValue().setOutput(sw.toString());
						sw.close();
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pw != null)
				pw.close();
		}
		return outputModelMap;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
