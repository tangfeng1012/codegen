package com.hhly.codgen.model;

public class ColumnModel {
	
	private String columnName;//����
	
	private String fieldName;//�ֶ��� ��Ӧ���ݿ����ʵ�ֶ����ƣ�һ�㲻�ɸ���
	
	private String colComment;//��ע��
	
	private String columnLabel;//�б�ǩ����ע�͵ı�ǩ���֡����ڴ�ӡ�������ʾ��ָ���еĽ�����⣨���ģ�
	
	private String colRemark;//��ע�͵ı�ע����
	
	private int columnType;//�е� SQL ���͡�
	
	private String columnTypeName;//�е� SQL������������
	
    private String columnSimpleClassName;//�����������������ļ���������ȫ�޶�������Ĭ�ϵı������ΪJava����String
    
	private String columnClassName;//sql����������java�ж�Ӧ�ľ�����������
	
    private String columnClassPackage;//���������������������ڵİ�(�����ռ�)��Ĭ�ϵı������ΪJava���磺java.lang
	
	private int columnDisplaySize;//ָ���е�����׼���
	
	private int scale;//��ȡָ���е�С�����ұߵ�λ����
	
	private int precision;//ָ���е�ָ���п�
	
	private boolean autoIncrement;//ָʾ�Ƿ��Զ�Ϊָ���н��б�š�
	
	private boolean readOnly;//ָʾָ�������Ƿ���ȷ����д�롣
	
	private boolean searchable;//ָʾ�Ƿ������ where �Ӿ���ʹ��ָ�����С�
	
	private boolean currency;//ָʾָ�������Ƿ���һ����ϣ����ֵ�� 
	
	private boolean nullable;//ָʾָ�����е�ֵ�Ƿ����Ϊ null�� 
	
	private boolean primaryKey;//�Ƿ�Ϊ����
	

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getColComment() {
		return colComment;
	}

	public void setColComment(String colComment) {
		this.colComment = colComment;
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	public String getColRemark() {
		return colRemark;
	}

	public void setColRemark(String colRemark) {
		this.colRemark = colRemark;
	}

	public int getColumnType() {
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public String getColumnClassName() {
		return columnClassName;
	}

	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}

	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}

	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean isCurrency() {
		return currency;
	}

	public void setCurrency(boolean currency) {
		this.currency = currency;
	}

	public String getColumnSimpleClassName() {
		return columnSimpleClassName;
	}

	public void setColumnSimpleClassName(String columnSimpleClassName) {
		this.columnSimpleClassName = columnSimpleClassName;
	}

	public String getColumnClassPackage() {
		return columnClassPackage;
	}

	public void setColumnClassPackage(String columnClassPackage) {
		this.columnClassPackage = columnClassPackage;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	

}
