package com.hhly.codgen.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hhly.codgen.exception.CodgenException;
import com.hhly.codgen.model.OutputModel;
import com.hhly.codgen.model.ProjectConfig;
import com.hhly.codgen.model.TableModel;
import com.hhly.codgen.service.BuildConfig;
import com.hhly.codgen.util.BuilderHelper;
import com.hhly.codgen.util.ClassLoaderUtils;
import com.hhly.codgen.util.FilenameUtil;
import com.hhly.codgen.util.MessageConstant;

import freemarker.template.utility.StringUtil;

/**
 * 项目构建配置信息  对xml配置取到的数据进行处理(
 * 	模板路径、输出路径、对参数含有${}进行处理
 * )
 * 
 * @author Administrator
 *
 */
public class ProjectBuildConfig implements BuildConfig{
	
	private ProjectConfig projectConfig; 
	
	private Map<String, Object> dataModelMap = new LinkedHashMap<>();//数据模型
	
	private Map<String, OutputModel> outputModelMap = new LinkedHashMap<>();//输出模型
	
	private String tableName;//表名
	
	public ProjectBuildConfig(ProjectConfig projectConfig) {
		this.projectConfig = projectConfig;
	}
		
	@Override
	public String getOutputEncoding() {
		return projectConfig.getOutputEncoding();
	}

	@Override
	public Map<String, Object> getDataModelMap() {
		
		if(dataModelMap.size() > 0){
			return dataModelMap;
		}
		
		dataModelMap.put(MessageConstant.PROJECT_NAME, projectConfig.getProjectName());
		dataModelMap.put(MessageConstant.PROJECT_LABLE, projectConfig.getProjectLable());
		dataModelMap.put(MessageConstant.PROJECT_ENCODING, projectConfig.getOutputEncoding());
		dataModelMap.put(MessageConstant.PROJECT_OUTPUTDIECTORY, defaultOutputDirectory());
		dataModelMap.put(MessageConstant.Project_COPYRIGHT, projectConfig.getCopyright());
		
		if(StringUtils.isBlank(getTableName())){
//			throw new CodgenException("没有设置表名...");
		}else{
			dataModelMap.put(tableName, getTableName());
			//设置表模型（包含列模型）
			TableModel tm = projectConfig.getDbProvider().createTableModel(getTableName());
			dataModelMap.put(MessageConstant.PROJECT_TABLE, tm);
			
			//模块名（对应的bean类名---根据表名自动生成）
			if(tableName.contains("_")){
				String[] splitStr = tableName.split("_");
				StringBuilder builder = new StringBuilder();
				for(int i = 0; i < splitStr.length; i++){
					String str = splitStr[i].substring(0, 1).toUpperCase() + splitStr[i].substring(1).toLowerCase();
					builder.append(str);
				}
				dataModelMap.put(MessageConstant.PROJECT_MOUDLE, builder.toString());
				
				//设置entityName(对应tableName 根据"_"切割得到的最后一个字符串 并处理大小写)
				String splitLastStr = StringUtils.substringAfterLast(tableName, "_");
				String entityName = splitLastStr.substring(0, 1).toUpperCase() + splitLastStr.substring(1).toLowerCase();
				dataModelMap.put(MessageConstant.PROJECT_ENTITYNAME, entityName);
			}else{
				String entityName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1).toLowerCase();
				dataModelMap.put(MessageConstant.PROJECT_MOUDLE, entityName);
				dataModelMap.put(MessageConstant.PROJECT_ENTITYNAME, entityName);
			}
		}
		
		//将配置文件中的<dataModel>标签数据加入到数据模板中
		Map<String, String> dataModel = projectConfig.getDataModelMap();
		if(dataModel != null && !dataModel.isEmpty()){
			for(Map.Entry<String, String> entry : dataModel.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				if(StringUtils.isNotBlank(value) && value.contains("${")){ //解析带有构建参数的字符串
					value = BuilderHelper.parseBuilderParam(value, dataModelMap);
				}
				dataModelMap.put(key, value);
			}
		}
		
		return dataModelMap;
	}

	
	@Override
	public Map<String, OutputModel> getOutputModelMap() {
		
		getDataModelMap();//确保已获得数据模型
		
		Map<String, OutputModel> outputMap = projectConfig.getOutputModelMap();
		if(outputMap != null && !outputMap.isEmpty()){
			for(Map.Entry<String, OutputModel> entry : outputMap.entrySet()){
				OutputModel outputModel = entry.getValue();
				
				String templatePath = outputModel.getTemplateModel().getTemplate();
				if(StringUtils.isNotBlank(templatePath) && templatePath.contains("${")){
					templatePath = BuilderHelper.parseBuilderParam(templatePath, dataModelMap);
				}
				templatePath = formatTemplateFilename(templatePath);//格式化文件名
				
				String outputPath = outputModel.getOutput();
				if(StringUtils.isNotBlank(outputPath) && outputPath.contains("${")){
					outputPath = BuilderHelper.parseBuilderParam(outputPath, dataModelMap);
				}
				//如果不是绝对路径，路径前加上默认路径System.getProperties("user.dir")
				if(!outputPath.contains(":")){
					String defalutRealPath = dataModelMap.get(MessageConstant.PROJECT_OUTPUTDIECTORY).toString().replace("\\", "/");
					defalutRealPath = defalutRealPath.endsWith("/") ? defalutRealPath : defalutRealPath + "/";
					outputPath = defalutRealPath + outputPath;
					outputPath = formatTemplateFilename(outputPath);//格式化文件名
				}
				
				outputModel.getTemplateModel().setTemplate(templatePath);
				outputModel.setOutput(outputPath);
				
				outputMap.put(outputModel.getName(), outputModel);
			}
		}
		
		return outputMap;
	}
	
	/**
	 * 格式化模板文件名为完整的文件URL，如果名称未包含路径，则默认为类根路径下的 “template/”+项目名称
	 * @param filename
	 * @return
	 */
	private String formatTemplateFilename(String filename){
		filename = filename.replace("\\", "/");
		if(!filename.contains("/")){
			if(dataModelMap.containsKey(MessageConstant.PROJECT_TEMPLATE)){
				filename = dataModelMap.get(MessageConstant.PROJECT_TEMPLATE)+"/"+filename;
			}else{
				filename = "template/" + projectConfig.getProjectName()+"/"+filename;
			}
		}
		
		if(filename.contains(".")==false 
				|| StringUtils.substringAfterLast(filename, ".").contains("/")){
			filename += ".ftl";
		}
		
		if(filename.contains(":")==false ){
			URL url = ClassLoaderUtils.getResource(filename);
			if(url==null){
				try {
					throw new IOException(String.format("模板文件%s不存在！",filename));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				filename = url.getFile();
			}
		}
		
		filename = FilenameUtil.normalize(filename);
		return filename;
	}

	private String defaultOutputDirectory(){
		String defalutOutputDirectory = System.getProperty("user.dir");
		return defalutOutputDirectory;
	}
	
	public ProjectConfig getProjectConfig() {
		return projectConfig;
	}

	public void setProjectConfig(ProjectConfig projectConfig) {
		this.projectConfig = projectConfig;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setDataModelMap(Map<String, Object> dataModelMap) {
		this.dataModelMap = dataModelMap;
	}

	public void setOutputModelMap(Map<String, OutputModel> outputModelMap) {
		this.outputModelMap = outputModelMap;
	}

	
}
