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
	
	private String columnCommentSeperator = " ";//��ע�ͷָ���
	
	private Log log = LogFactory.getLog(DbProvider.class);
	
	protected Connection getConn() {
		if(connection == null){
			if(jdbcConfig == null){
				try {
					throw new CodgenException("connection and jdbcConfig ����ͬʱΪnull");
				} catch (CodgenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return JdbcUtil.getConn(jdbcConfig);
		}
		return connection;
	}
	
	//��ȡָ�����������ע��   ���صļ��ϵ�Ԫ�ظ�ʽΪ������(COLUMN_NAME)��ע��(COMMENTS)��
	//��ͬ���ݿ��в�ͬʵ�֣���������ʵ��
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
	 * ��ȡ��ע��  ע����ע�� = �б�ǩ + �ָ��� + �б�ע��Ϣ����ת���� 0:����ת,1:��ҳ��,2:��app���棩
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
	 * ����ע������ȡ�б�ǩ 
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
	 * ����ע������ȡ�б�ע��Ϣ
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
	 * ��ȡ��ע��
	 * @param tableName
	 * @return
	 */
	public String getTableComment(String tableName, String defaultStr){
		String tableComent = getTableComments(tableName).get(tableName);
		return StringUtils.defaultString(tableComent, defaultStr);
	}
	
	/**
	 * ��ע������ȡ���ǩ  ��ע�� = ���ǩ + �ָ��� + ��ע��Ϣ
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
	 * ���ݱ������������ݿ⣬�õ�tableModel
	 * @param tableName
	 * @return
	 */
	public TableModel createTableModel(String tableName){
		TableModel tableModel = new TableModel();
		//���ñ���ص�Ԫ����
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
				//��������ص�Ԫ����
				columnModel.setColumnName(rsmd.getColumnName(i));
				columnModel.setFieldName(rsmd.getColumnName(i));
				columnModel.setColComment(getColumnComment(tableName, columnModel.getColumnName()));
				columnModel.setColumnLabel(getColumnLableFromComment(columnModel.getColComment()));
				columnModel.setColRemark(getColumnRemarkFromComment(columnModel.getColComment()));
				//����sql����
				columnModel.setColumnType(rsmd.getColumnType(i));
				//����sql������������
				columnModel.setColumnTypeName(rsmd.getColumnTypeName(i));
				//����sql����������java�ж�Ӧ����������
				columnModel.setColumnClassName(rsmd.getColumnClassName(i));
				//ָ���е�����׼���
				columnModel.setColumnDisplaySize(rsmd.getColumnDisplaySize(i));
				//��ȡָ���е�С�����ұߵ�λ����
				columnModel.setScale(rsmd.getScale(i));
				//ָ���е�ָ���п�
				columnModel.setPrecision(rsmd.getPrecision(i));
				//ָʾ�Ƿ��Զ�Ϊָ����
				columnModel.setAutoIncrement(rsmd.isAutoIncrement(i));
				//ָ�������Ƿ���ȷ����д�롣
				columnModel.setReadOnly(rsmd.isReadOnly(i));
				//�жϸ��ֶ��Ƿ������Ϊ������������������where�����
				columnModel.setSearchable(rsmd.isSearchable(i));
				////�ж��Ƿ�Ϊ���������ֶ�
				columnModel.setCurrency(rsmd.isCurrency(i));
				
				//�жϸ�������շ�
				if(rsmd.isNullable(i)==ResultSetMetaData.columnNoNulls){
					columnModel.setNullable(false);
				}else{
					columnModel.setNullable(true);
				}
				
				//���û�������κε���ģ�ʹ���������Ĭ��ʹ��һ��java��������ת������������ģ��
				if(columnHandlers == null || columnHandlers.isEmpty()){
					 new DataTypeConverterForJava().handle(columnModel);
				}else{
					for(ColumnHandler handler : columnHandlers){
						handler.handle(columnModel);
					}
				}
			}
			log.debug("tableģ��:" + tableModel);
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
