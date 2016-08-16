package com.hhly.codgen.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hhly.codgen.service.DbProvider;

/**
 * 项目配置bean
 * @author Administrator
 *
 */
public class ProjectConfig {
	
	private String projectName;
	
	private String projectLable;
	
	private String outputEncoding;
	
	private DbProvider dbProvider;
	
	private Copyright copyright;
	
	private Map<String, String> dataModelMap = new LinkedHashMap<>();
	
	private Map<String, OutputModel> outputModelMap = new LinkedHashMap<>();
	
	private boolean defaultProject;//多个Project选项时，加载配置为true的Project

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectLable() {
		return projectLable;
	}

	public void setProjectLable(String projectLable) {
		this.projectLable = projectLable;
	}

	public String getOutputEncoding() {
		return outputEncoding;
	}

	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}

	public DbProvider getDbProvider() {
		return dbProvider;
	}

	public void setDbProvider(DbProvider dbProvider) {
		this.dbProvider = dbProvider;
	}

	public Map<String, String> getDataModelMap() {
		return dataModelMap;
	}

	public void setDataModelMap(Map<String, String> dataModelMap) {
		this.dataModelMap = dataModelMap;
	}

	public Map<String, OutputModel> getOutputModelMap() {
		return outputModelMap;
	}

	public void setOutputModelMap(Map<String, OutputModel> outputModelMap) {
		this.outputModelMap = outputModelMap;
	}

	public boolean isDefaultProject() {
		return defaultProject;
	}

	public void setDefaultProject(boolean defaultProject) {
		this.defaultProject = defaultProject;
	}
	
	public Copyright getCopyright() {
		return copyright;
	}

	public void setCopyright(Copyright copyright) {
		this.copyright = copyright;
	}

	/**
	 * 使用序列化的方式进行深度克隆
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ProjectConfig deepClone() throws IOException, ClassNotFoundException{

		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(bao);
		objectOutputStream.writeObject(this);
		
		ByteArrayInputStream baInput = new ByteArrayInputStream(bao.toByteArray());
		ObjectInputStream objectInputStream = new ObjectInputStream(baInput);
		ProjectConfig projectConfig = (ProjectConfig)objectInputStream.readObject();

		return projectConfig;
		
	}

	@Override
	public String toString() {
		return "ProjectConfig [projectName=" + projectName + ", projectLable=" + projectLable + ", outputEncoding="
				+ outputEncoding + ", dbProvider=" + dbProvider + ", copyright=" + copyright + ", dataModelMap="
				+ dataModelMap + ", outputModelMap=" + outputModelMap + ", defaultProject=" + defaultProject + "]";
	}

}
