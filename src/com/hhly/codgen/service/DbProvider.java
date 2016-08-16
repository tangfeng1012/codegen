package com.hhly.codgen.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hhly.codgen.exception.CodgenException;
import com.hhly.codgen.model.ColumnModel;
import com.hhly.codgen.model.JdbcConfig;
import com.hhly.codgen.model.TableModel;
import com.hhly.codgen.service.impl.DataTypeConverterForJava;
import com.hhly.codgen.util.IgnoreCaseMap;
import com.hhly.codgen.util.JdbcUtil;

public abstract class DbProvider {
	
	private Connection connection;
	
	private JdbcConfig jdbcConfig;
	
	private List<ColumnHandler> columnHandlers = new ArrayList<>();
	
	private Map<String, Map<String, String>> columnComments = null;
	
	private Map<String, String> tableComments = null;
	
	private String columnCommentSeperator = " ";//列注释分隔符
	
	private Log log = LogFactory.getLog(DbProvider.class);
	
	protected Connection getConn() {
		if(connection == null){
			if(jdbcConfig == null){
				try {
					throw new CodgenException("connection and jdbcConfig 不能同时为null");
				} catch (CodgenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return JdbcUtil.getConn(jdbcConfig);
		}
		return connection;
	}
	
	//获取指定表的所有列注释   返回的集合的元素格式为：列名(COLUMN_NAME)和注释(COMMENTS)。
	//不同数据库有不同实现，交给子类实现
	public abstract Map<String, String> doGetColumnComments(String tableName);
	
	public Map<String, String> getColumnComments(String tableName){
		if(columnComments == null){
			columnComments = new IgnoreCaseMap<>();
		}
		Map<String, String> resultMap = columnComments.get(tableName);
		if(resultMap == null){
			resultMap = new IgnoreCaseMap<>(doGetColumnComments(tableName));
			columnComments.put(tableName, resultMap);
		}
		return resultMap;
	}
	
	/**
	 * 获取列注释  注：列注释 = 列标签 + 分隔符 + 列备注信息（跳转类型 0:无跳转,1:跳页面,2:跳app界面）
	 * @param tableName
	 * @param colName
	 * @return
	 */
	public String getColumnComment(String tableName, String colName){
		String comment = "";
		Map<String, String> colMap = getColumnComments(tableName);
		if(colMap != null){
			comment = StringUtils.defaultString(colMap.get(colName), colName)
					.replace("\r", " ").replace("\n", " ");
		}
		return comment;
	}
	
	/**
	 * 从列注释中提取列标签 
	 * @param colComment
	 * @return
	 */
	public String getColumnLableFromComment(String colComment){
		if(StringUtils.isNotBlank(colComment)){
			colComment = StringUtils.substringBefore(colComment, columnCommentSeperator);
		}
		return StringUtils.defaultString(colComment);
	}
	
	/**
	 * 从列注释中提取列备注信息
	 * @param colComment
	 * @return
	 */
	public String getColumnRemarkFromComment(String colComment){
		if(StringUtils.isNotBlank(colComment)){
			colComment = StringUtils.substringAfter(colComment, columnCommentSeperator);
		}
		return StringUtils.defaultString(colComment);
	}
	
	public abstract Map<String, String> doGetTableComment(String tableName);
	
	public Map<String, String> getTableComments(String tableName){
		if(tableComments == null){
			tableComments = new IgnoreCaseMap<>(doGetTableComment(tableName));
		}
		return tableComments;
	}
	
	/**
	 * 获取表注释
	 * @param tableName
	 * @return
	 */
	public String getTableComment(String tableName, String defaultStr){
		String tableComent = getTableComments(tableName).get(tableName);
		return StringUtils.defaultString(tableComent, defaultStr);
	}
	
	/**
	 * 从注释中提取表标签  表注释 = 表标签 + 分隔符 + 表备注信息
	 * @param comment
	 * @param defaultStr
	 * @return
	 */
	public String getTableLabelFromComment(String comment){
		if(StringUtils.isNotBlank(comment)){
			comment = StringUtils.substringBefore(comment, columnCommentSeperator);
		}
		return StringUtils.defaultString(comment);
	}
	/**
	 * 根据表名，连接数据库，得到tableModel
	 * @param tableName
	 * @return
	 */
	public TableModel createTableModel(String tableName){
		TableModel tableModel = new TableModel();
		//设置表相关的元数据
		tableModel.setTableName(tableName);
		tableModel.setTabComment(getTableComment(tableName,tableName));
		tableModel.setTableLabel(getTableLabelFromComment(tableModel.getTabComment()));
		
		Statement statement = null; 
		ResultSet rs = null;
		try {
			String sql = "select * from " + tableName;
			statement = getConn().createStatement();
			rs = statement.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			ColumnModel columnModel = null;
			for(int i = 1; i < rsmd.getColumnCount(); i++){
				columnModel = new ColumnModel();
				tableModel.getColumnList().add(columnModel);
				//设置列相关的元数据
				columnModel.setColumnName(rsmd.getColumnName(i));
				columnModel.setFieldName(rsmd.getColumnName(i));
				columnModel.setColComment(getColumnComment(tableName, columnModel.getColumnName()));
				columnModel.setColumnLabel(getColumnLableFromComment(columnModel.getColComment()));
				columnModel.setColRemark(getColumnRemarkFromComment(columnModel.getColComment()));
				//设置sql类型
				columnModel.setColumnType(rsmd.getColumnType(i));
				//设置sql数据类型名称
				columnModel.setColumnTypeName(rsmd.getColumnTypeName(i));
				//设置sql数据类型在java中对应的数据类型
				columnModel.setColumnClassName(rsmd.getColumnClassName(i));
				//指定列的最大标准宽度
				columnModel.setColumnDisplaySize(rsmd.getColumnDisplaySize(i));
				//获取指定列的小数点右边的位数。
				columnModel.setScale(rsmd.getScale(i));
				//指定列的指定列宽
				columnModel.setPrecision(rsmd.getPrecision(i));
				//指示是否自动为指定列
				columnModel.setAutoIncrement(rsmd.isAutoIncrement(i));
				//指定的列是否明确不可写入。
				columnModel.setReadOnly(rsmd.isReadOnly(i));
				//判断该字段是否可以作为搜索条件，并出现在where语句中
				columnModel.setSearchable(rsmd.isSearchable(i));
				////判断是否为货币类型字段
				columnModel.setCurrency(rsmd.isCurrency(i));
				
				//判断该列允许空否
				if(rsmd.isNullable(i)==ResultSetMetaData.columnNoNulls){
					columnModel.setNullable(false);
				}else{
					columnModel.setNullable(true);
				}
				
				//如果没有设置任何的列模型处理器，则默认使用一个java数据类型转换器来处理列模型
				if(columnHandlers == null || columnHandlers.isEmpty()){
					 new DataTypeConverterForJava().handle(columnModel);
				}else{
					for(ColumnHandler handler : columnHandlers){
						handler.handle(columnModel);
					}
				}
			}
			log.debug("table模型:" + tableModel);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return tableModel;
	}
	
	public DbProvider(JdbcConfig jdbcConfig) {
		// TODO Auto-generated constructor stub
		this.jdbcConfig = jdbcConfig;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public JdbcConfig getJdbcConfig() {
		return jdbcConfig;
	}

	public void setJdbcConfig(JdbcConfig jdbcConfig) {
		this.jdbcConfig = jdbcConfig;
	}

	public List<ColumnHandler> getColumnHandlers() {
		return columnHandlers;
	}

	public void setColumnHandlers(List<ColumnHandler> columnHandlers) {
		this.columnHandlers = columnHandlers;
	}
	
	
	
}
