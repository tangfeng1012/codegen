<#include "include/head.ftl">
package ${NamespaceVo};

import java.util.Date;

<#include "include/copyright.ftl">

/**
 * <#-- ��${tableLabel}�� ʵ��ģ��  -->
 * @author ${copyright.author}
 *
 */
public class ${Po}{
	private static final long serialVersionUID = 1L;
	
	<#list table.columnList as column>
	private ${column.columnSimpleClassName} ${column.columnName?uncap_first}; //${column.columnLabel}
	</#list>
	
	/**
	 *<#-- ʵ����һ��"${tableLabel}"ʵ��ģ�Ͷ��� -->
	 */
	public ${Po}() {
		super();
	}
	
	@Override
	public String getTableName() {
		return "${table.tableName}";
	}
	
	<#list table.columnList as column>
	<#if column.columnName?uncap_first!="createUserID"&&column.columnName?uncap_first!="createTime"
		&&column.columnName?uncap_first!="modifyUserID"&&column.columnName?uncap_first!="modifyTime" >
	/**
	 * ȡ��"${column.columnLabel}"
	 * @return ����"${column.columnLabel}"
	 */
	public ${column.columnSimpleClassName} get${column.columnName?cap_first}(){
		return this.${column.columnName?uncap_first};
	}
	/**
	 * ����"${column.columnLabel}"��ֵ
	 * @param ${column.columnName?uncap_first} ${column.columnLabel}
	 */
	public void set${column.columnName?cap_first}(${column.columnSimpleClassName} ${column.columnName?uncap_first}){
		this.${column.columnName?uncap_first} = ${column.columnName?uncap_first};
	}
	</#if>
	</#list>
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("${Po} [")
		<#list table.columnList as column>
		<#if column_index==0>
		.append("${column.columnName}=").append(this.get${column.columnName?cap_first}())
		<#else>
		.append(",${column.columnName}=").append(this.get${column.columnName?cap_first}())
		</#if>
		</#list>
		.append("]");
		return builder.toString();
	}
}