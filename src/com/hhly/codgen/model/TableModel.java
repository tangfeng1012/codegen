package com.hhly.codgen.model;

import java.util.ArrayList;
import java.util.List;


public class TableModel {
	
	private String tableName;//表名
	
	private String catalog;//所属类别  一般为数据库名
	
	private String schema;//表所属模式  一般为用户名
	
	private String tableType;//表类型。典型的类型是 "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"。
	
    private String tabComment;//表注释

    private String tableLabel;//表标签 也就是表的中文别名
    
    private List<ColumnModel> columnList=new ArrayList<ColumnModel>();//列模型集合

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getTabComment() {
		return tabComment;
	}

	public void setTabComment(String tabComment) {
		this.tabComment = tabComment;
	}

	public String getTableLabel() {
		return tableLabel;
	}

	public void setTableLabel(String tableLabel) {
		this.tableLabel = tableLabel;
	}

	public List<ColumnModel> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnModel> columnList) {
		this.columnList = columnList;
	}

	@Override
	public String toString() {
		return "TableModel [tableName=" + tableName + ", catalog=" + catalog + ", schema=" + schema + ", tableType="
				+ tableType + ", tabComment=" + tabComment + ", tableLabel=" + tableLabel + ", columnList=" + columnList
				+ "]";
	}
    

}
