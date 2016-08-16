<#include "include/head.ftl">
package ${NamespaceDao};

import com.qtone.aow.dao.BaseDao;
import ${NamespaceModel}.${Po};

<#include "include/copyright.ftl">

/**
 * 《${tableLabel}》 数据访问接口类
 * @author ${copyright.author}
 *
 */
public interface ${Po}Dao extends BaseDao<${Po}> {
	/**
	 * 取得符合默认条件的所有记录数
	 * @return
	 */
	long getRecordCount();  
	<#list table.columnList as column>
	<#if column.primaryKey>
	/**
	 * 根据主键取得一个实体模型
	 * @param ${column.columnName?uncap_first} 主键
	 * @return
	 */
	${Po} queryForObject(${column.columnSimpleClassName} ${column.columnName?uncap_first});
	/**
	 * 根据主键删除一个实体模型
	 * @param ${column.columnName?uncap_first} 主键
	 * @return 影响记录数，正常返回1，否则返回0
	 */
	int delete(${column.columnSimpleClassName} ${column.columnName?uncap_first});
	</#if>	
	</#list>
}