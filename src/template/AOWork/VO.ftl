<#include "include/head.ftl">
package ${NamespaceVo};

import java.util.Date;

<#include "include/copyright.ftl">

/**
 * <#-- 《${tableLabel}》 实体模型  -->
 * @author ${copyright.author}
 *
 */
public class ${Po}{
	private static final long serialVersionUID = 1L;
	
	<#list table.columnList as column>
	private ${column.columnSimpleClassName} ${column.columnName?uncap_first}; //${column.columnLabel}
	</#list>
	
	/**
	 *<#-- 实例化一个"${tableLabel}"实体模型对象 -->
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
	 * 取得"${column.columnLabel}"
	 * @return 返回"${column.columnLabel}"
	 */
	public ${column.columnSimpleClassName} get${column.columnName?cap_first}(){
		return this.${column.columnName?uncap_first};
	}
	/**
	 * 设置"${column.columnLabel}"的值
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