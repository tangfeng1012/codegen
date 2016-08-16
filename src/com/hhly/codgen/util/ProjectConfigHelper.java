package com.hhly.codgen.util;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hhly.codgen.exception.CodgenException;
import com.hhly.codgen.model.Copyright;
import com.hhly.codgen.model.InOutType;
import com.hhly.codgen.model.JdbcConfig;
import com.hhly.codgen.model.OutputModel;
import com.hhly.codgen.model.ProjectConfig;
import com.hhly.codgen.model.TemplateModel;
import com.hhly.codgen.service.ColumnHandler;
import com.hhly.codgen.service.DbProvider;

public class ProjectConfigHelper {
	
	private static final String codgenDefault = "com/hhly/codgen/resources/codgen-default.xml";
	
	private static final String codgenConfig = "codgen-config.xml";
			
	private static Log log = LogFactory.getLog(ProjectConfigHelper.class);
	
	
	private static Map<String, ProjectConfig> projectConfigMap = null;
	
	private static ProjectConfig firstProjectConfig = null;
	
	/**
	 * 
	 * @param relativePath
	 * @return
	 */
	public static ProjectConfig getDefaultProjectConfig(String relativePath){
		if(projectConfigMap == null)
			loadConfigFromFile(relativePath);
		
		for(Map.Entry<String, ProjectConfig> entry : projectConfigMap.entrySet()){
			if(entry.getValue().isDefaultProject()){
				firstProjectConfig = entry.getValue();
			}
		}
		return firstProjectConfig;
	}
	
	/**
	 * 从配置文件中加载项目配置
	 * @param relativePath
	 */
	private static void loadConfigFromFile(String relativePath){
		projectConfigMap = new LinkedHashMap<>();
		
		InputStream inputStream = null;
		try {
			inputStream = ClassLoaderUtils.getStream(codgenDefault); //加载默认配置
			loadConfigFromInputStream(inputStream);
			
			if(StringUtils.isBlank(relativePath)){
				inputStream = ClassLoaderUtils.getStream(codgenConfig);//用户自定义配置
				loadConfigFromInputStream(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		
	}
	
	/**
	 * 从流中读取项目配置
	 * @param inputStream
	 */
	private static void loadConfigFromInputStream(InputStream inputStream){
		ProjectConfig projectConfig = null;
		OutputModel outputModel = null;
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(inputStream);
			Element root = document.getDocumentElement();
			NodeList projectNodeList = root.getElementsByTagName("project");
			
			for(int i = 0; i < projectNodeList.getLength(); i++){
				Element projectNode = (Element)projectNodeList.item(i);
				String projectName = projectNode.getAttribute("name");
				String isEnabled = projectNode.getAttribute("isEnabled");
				if(projectConfigMap.containsKey(projectName)){
					throw new CodgenException("配置文件中存在相同的项目名称:" + projectName);
				}
				if(StringUtils.isNotBlank(isEnabled) && isEnabled.equalsIgnoreCase("false")){ 
					continue;//不加载已禁用的project
				}
				if(projectNode.hasAttribute("extend")){
					String extendProject = projectNode.getAttribute("extend");
					if(projectConfigMap.containsKey(extendProject) == false){
						throw new CodgenException("配置文件中项目【" + projectName + "】所继承的项目不存在或已禁用");
					}else{ 
						projectConfig = projectConfigMap.get(extendProject).deepClone(); //序列化方式克隆
					}
				}else{
					projectConfig = new ProjectConfig();
				}
				
				projectConfig.setProjectName(projectName);
				projectConfig.setProjectLable(projectNode.getAttribute("lable"));
				projectConfig.setOutputEncoding(projectNode.getAttribute("outputEncoding"));
				String isDefault = projectNode.getAttribute("isDefault");
				if(StringUtils.isNotBlank("isDefault")){
					projectConfig.setDefaultProject(isDefault.equalsIgnoreCase("true"));
				}
				
				//project子节点
				NodeList childNodes = projectNode.getChildNodes();
				for(int j = 0; j < childNodes.getLength(); j++){
					Node node = childNodes.item(j);
					if(node instanceof Element){
						Element childElement = (Element)node;
						String tagName = childElement.getTagName();
						if(tagName.equals("dbProvider")){
							DbProvider dbprovider = parseForDbProvider(childElement);
							projectConfig.setDbProvider(dbprovider);
						}else if(tagName.equals("copyright")){
							Copyright copyright = parseForCopyright(childElement);
							projectConfig.setCopyright(copyright);
						}else if(tagName.equals("dataModel")){
							String name = childElement.getAttribute("name");
							String value = childElement.getTextContent().trim();
							projectConfig.getDataModelMap().put(name, value);
						}else if(tagName.equals("outputModel")){
							String key = childElement.getAttribute("name");// output name
							outputModel = new OutputModel(key);
							String content = childElement.getTextContent().trim();
							String type = childElement.getAttribute("type").toUpperCase();
							if(!StringUtils.isBlank(content)){
								outputModel.setType(InOutType.valueOf(type));
								outputModel.setOutput(content);
							}else{
								outputModel.setType(InOutType.FILE);
							}
							
							TemplateModel templateModel = null;
							if(childElement.hasAttribute("templateFile")){ //templateFile属性指定ftl文件位置(/template/AOWork/bill.ftl)
								templateModel = new TemplateModel();
								templateModel.setName(key);
								templateModel.setType(InOutType.FILE);
								templateModel.setTemplate(childElement.getAttribute("templateFile"));
							}else{ //如果不指定，则默认模板文件名称合成规则：当前应用类路径+"template/" + 项目名称 + "/" + 输出名称+".ftl"
								templateModel = new TemplateModel();
								templateModel.setName(key);
								templateModel.setType(InOutType.FILE);
								templateModel.setTemplate(key);
							}
							outputModel.setTemplateModel(templateModel);
							
							if(childElement.hasAttribute("disabled")){
								outputModel.setDisabled(Boolean.parseBoolean(childElement.getAttribute("disabled")));
							}else{
								outputModel.setDisabled(false);
							}
							projectConfig.getOutputModelMap().put(key, outputModel);
						}
					 }
				 }
				
				log.debug(projectConfig);
				projectConfigMap.put(projectConfig.getProjectName(), projectConfig);
				if(firstProjectConfig == null)
					firstProjectConfig = projectConfig;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析DOM <DbProvider>节点 
	 * @param dbProviderElement
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static DbProvider parseForDbProvider(Element dbProviderElement) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		DbProvider dbProvider = null;
		String driverClazz = dbProviderElement.getAttribute("class");
		NodeList jdbcNodeList = dbProviderElement.getElementsByTagName("jdbc");
		if(jdbcNodeList != null && jdbcNodeList.getLength() > 0){
			Node jdbcNode = jdbcNodeList.item(0);
			if(jdbcNode instanceof Element){
				Element jdbcElement = (Element)jdbcNode;
				System.out.println(jdbcElement.getTagName());
				JdbcConfig jdbcConfig = parseForJdbcConfig(jdbcElement);
				Constructor constructor = Class.forName(driverClazz).getConstructor(JdbcConfig.class);
				dbProvider = (DbProvider)constructor.newInstance(jdbcConfig);
			}
		}else{
			dbProvider = (DbProvider) Class.forName(driverClazz).newInstance(); 
		}
			
		NodeList columnHandlerNodeList = dbProviderElement.getElementsByTagName("columnHandler");
		List<ColumnHandler> columnhandlers = parseForColumnHandler(columnHandlerNodeList);
		dbProvider.setColumnHandlers(columnhandlers);
		
		return dbProvider;
	}
	
	/**
	 * 解析<copyright> DOM节点
	 * @param copyrightElement
	 * @return
	 */
	public static Copyright parseForCopyright(Element copyrightElement){
		Copyright copyright = null;
		if(copyrightElement.hasChildNodes()){
			copyright = new Copyright();
			NodeList crNodeList = copyrightElement.getChildNodes();
			if(crNodeList != null && crNodeList.getLength() > 0){
				for(int i = 0; i < crNodeList.getLength(); i++){
//					Element crEle = (Element)crNodeList.item(i);
					Node crNode = crNodeList.item(i);
					if(crNode instanceof Element){
						Element crEle = (Element)crNode;
						switch(crEle.getTagName()){
						case "version":
							copyright.setVersion(crEle.getTextContent().trim());
							break;
						case "author":
							copyright.setAuthor(crEle.getTextContent().trim());
							break;
						case "authorEmail":
							copyright.setAuthorEmail(crEle.getTextContent().trim());
							break;
						case "company" : 	
							copyright.setCompany(crEle.getTextContent().trim());
							break;
						case "description":
							copyright.setDescription(crEle.getTextContent().trim());
							break;
						case "createDate":
							copyright.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							break;
						case "modifyDate":	
							copyright.setModifyDate(crEle.getTextContent().trim());
							break;
						default:
							break;
						}
					}
				}
			}
		}
		return copyright;
	}
	
	/**
	 * 解析DOM <jdbc></jdbc>节点
	 * @param jdbcElement
	 * @return
	 */
	public static JdbcConfig parseForJdbcConfig(Element jdbcElement){
		JdbcConfig jdbcConfig = new JdbcConfig();
		if(jdbcElement.hasChildNodes()){
			NodeList jdbcItems = jdbcElement.getChildNodes();
			for(int i = 0; i < jdbcItems.getLength(); i++){
				Node jdbcNode = jdbcItems.item(i);
				if(jdbcNode instanceof Element){
					Element jdbcItem = (Element)jdbcNode;
					String tagName = jdbcItem.getTagName();
					String text = jdbcItem.getTextContent();
					if("driver".equals(tagName)){
						jdbcConfig.setDriver(text);
					}else if("url".equals(tagName)){
						jdbcConfig.setUrl(text);
					}else if("username".equals(tagName)){
						jdbcConfig.setUsername(text);
					}else if("password".equals(tagName)){
						jdbcConfig.setPassword(text);
					}
				}
			}
		}
		log.debug("解析DOM<jdbc>节点:" + jdbcConfig);
		return jdbcConfig;
	}
	
	/**
	 * 解析DOM <ColumnHandler>节点  此节点class实现ColumnHandler接口，
	 * 用于对查询数据库表中的列进行操作
	 * @param columnHandlerNodeList
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static List<ColumnHandler> parseForColumnHandler(NodeList columnHandlerNodeList) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		List<ColumnHandler> colHandlers = new ArrayList<>();
		if(columnHandlerNodeList == null)
			return colHandlers;
		
		ColumnHandler columnHandler = null;
		for(int i = 0; i < columnHandlerNodeList.getLength(); i++){
			Node columnhandlerNode = columnHandlerNodeList.item(i);
			if(columnhandlerNode instanceof Element){
				Element columnhandlerElement = (Element)columnhandlerNode;
				String columnClass = columnhandlerElement.getAttribute("class");
				columnHandler = (ColumnHandler)Class.forName(columnClass).newInstance();
				colHandlers.add(columnHandler);
			}
		}
		return colHandlers;
	}
}
