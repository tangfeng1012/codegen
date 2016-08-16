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
 * ��Ŀ����������Ϣ  ��xml����ȡ�������ݽ��д���(
 * 	ģ��·�������·�����Բ�������${}���д���
 * )
 * 
 * @author Administrator
 *
 */
public class ProjectBuildConfig implements BuildConfig{
	
	private ProjectConfig projectConfig; 
	
	private Map<String, Object> dataModelMap = new LinkedHashMap<>();//����ģ��
	
	private Map<String, OutputModel> outputModelMap = new LinkedHashMap<>();//���ģ��
	
	private String tableName;//����
	
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
//			throw new CodgenException("û�����ñ���...");
		}else{
			dataModelMap.put(tableName, getTableName());
			//���ñ�ģ�ͣ�������ģ�ͣ�
			TableModel tm = projectConfig.getDbProvider().createTableModel(getTableName());
			dataModelMap.put(MessageConstant.PROJECT_TABLE, tm);
			
			//ģ��������Ӧ��bean����---���ݱ����Զ����ɣ�
			if(tableName.contains("_")){
				String[] splitStr = tableName.split("_");
				StringBuilder builder = new StringBuilder();
				for(int i = 0; i < splitStr.length; i++){
					String str = splitStr[i].substring(0, 1).toUpperCase() + splitStr[i].substring(1).toLowerCase();
					builder.append(str);
				}
				dataModelMap.put(MessageConstant.PROJECT_MOUDLE, builder.toString());
				
				//����entityName(��ӦtableName ����"_"�и�õ������һ���ַ��� �������Сд)
				String splitLastStr = StringUtils.substringAfterLast(tableName, "_");
				String entityName = splitLastStr.substring(0, 1).toUpperCase() + splitLastStr.substring(1).toLowerCase();
				dataModelMap.put(MessageConstant.PROJECT_ENTITYNAME, entityName);
			}else{
				String entityName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1).toLowerCase();
				dataModelMap.put(MessageConstant.PROJECT_MOUDLE, entityName);
				dataModelMap.put(MessageConstant.PROJECT_ENTITYNAME, entityName);
			}
		}
		
		//�������ļ��е�<dataModel>��ǩ���ݼ��뵽����ģ����
		Map<String, String> dataModel = projectConfig.getDataModelMap();
		if(dataModel != null && !dataModel.isEmpty()){
			for(Map.Entry<String, String> entry : dataModel.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				if(StringUtils.isNotBlank(value) && value.contains("${")){ //�������й����������ַ���
					value = BuilderHelper.parseBuilderParam(value, dataModelMap);
				}
				dataModelMap.put(key, value);
			}
		}
		
		return dataModelMap;
	}

	
	@Override
	public Map<String, OutputModel> getOutputModelMap() {
		
		getDataModelMap();//ȷ���ѻ������ģ��
		
		Map<String, OutputModel> outputMap = projectConfig.getOutputModelMap();
		if(outputMap != null && !outputMap.isEmpty()){
			for(Map.Entry<String, OutputModel> entry : outputMap.entrySet()){
				OutputModel outputModel = entry.getValue();
				
				String templatePath = outputModel.getTemplateModel().getTemplate();
				if(StringUtils.isNotBlank(templatePath) && templatePath.contains("${")){
					templatePath = BuilderHelper.parseBuilderParam(templatePath, dataModelMap);
				}
				templatePath = formatTemplateFilename(templatePath);//��ʽ���ļ���
				
				String outputPath = outputModel.getOutput();
				if(StringUtils.isNotBlank(outputPath) && outputPath.contains("${")){
					outputPath = BuilderHelper.parseBuilderParam(outputPath, dataModelMap);
				}
				//������Ǿ���·����·��ǰ����Ĭ��·��System.getProperties("user.dir")
				if(!outputPath.contains(":")){
					String defalutRealPath = dataModelMap.get(MessageConstant.PROJECT_OUTPUTDIECTORY).toString().replace("\\", "/");
					defalutRealPath = defalutRealPath.endsWith("/") ? defalutRealPath : defalutRealPath + "/";
					outputPath = defalutRealPath + outputPath;
					outputPath = formatTemplateFilename(outputPath);//��ʽ���ļ���
				}
				
				outputModel.getTemplateModel().setTemplate(templatePath);
				outputModel.setOutput(outputPath);
				
				outputMap.put(outputModel.getName(), outputModel);
			}
		}
		
		return outputMap;
	}
	
	/**
	 * ��ʽ��ģ���ļ���Ϊ�������ļ�URL���������δ����·������Ĭ��Ϊ���·���µ� ��template/��+��Ŀ����
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
					throw new IOException(String.format("ģ���ļ�%s�����ڣ�",filename));
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
