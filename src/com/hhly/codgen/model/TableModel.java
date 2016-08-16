package com.hhly.codgen.model;

import java.util.ArrayList;
import java.util.List;


public class TableModel {
	
	private String tableName;//����
	
	private String catalog;//�������  һ��Ϊ���ݿ���
	
	private String schema;//������ģʽ  һ��Ϊ�û���
	
	private String tableType;//�����͡����͵������� "TABLE"��"VIEW"��"SYSTEM TABLE"��"GLOBAL TEMPORARY"��"LOCAL TEMPORARY"��"ALIAS" �� "SYNONYM"��
	
    private String tabComment;//��ע��

    private String tableLabel;//���ǩ Ҳ���Ǳ�����ı���
    
    private List<ColumnModel> columnList=new ArrayList<ColumnModel>();//��ģ�ͼ���

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
