package com.hhly.codgen.model;

public class ColumnModel {
	
	private String columnName;//列名
	
	private String fieldName;//字段名 对应数据库的真实字段名称，一般不可更改
	
	private String colComment;//列注释
	
	private String columnLabel;//列标签，列注释的标签部分。用于打印输出和显示的指定列的建议标题（中文）
	
	private String colRemark;//列注释的备注部分
	
	private int columnType;//列的 SQL 类型。
	
	private String columnTypeName;//列的 SQL数据类型名称
	
    private String columnSimpleClassName;//面向具体编程语言中类的简单类名，非全限定名，，默认的编程语言为Java，如String
    
	private String columnClassName;//sql数据类型在java中对应的具体数据类型
	
    private String columnClassPackage;//面向具体编程语言中类的所在的包(命名空间)，默认的编程语言为Java，如：java.lang
	
	private int columnDisplaySize;//指定列的最大标准宽度
	
	private int scale;//获取指定列的小数点右边的位数。
	
	private int precision;//指定列的指定列宽
	
	private boolean autoIncrement;//指示是否自动为指定列进行编号。
	
	private boolean readOnly;//指示指定的列是否明确不可写入。
	
	private boolean searchable;//指示是否可以在 where 子句中使用指定的列。
	
	private boolean currency;//指示指定的列是否是一个哈希代码值。 
	
	private boolean nullable;//指示指定列中的值是否可以为 null。 
	
	private boolean primaryKey;//是否为主键
	

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
