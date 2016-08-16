<#include "include/head.ftl">
package ${NamespaceDaoImpl};

import com.qtone.aow.dao.BaseDaoImpl;
import ${NamespaceModel}.${Po};
import ${NamespaceParam}.${Po}Param;

<#include "include/copyright.ftl">

/**
 * 《${tableLabel}》 数据访问对象实现类
 * @author ${copyright.author}
 *
 */
public class ${Po}DaoImpl extends BaseDaoImpl<${Po}> implements ${Po}Dao {

	@Override
	public long getRecordCount() {
		${Po}Param param = new ${Po}Param();
		return getRecordCount(param);
	}
	<#list table.columnList as column>
	<#if column.primaryKey>
	@Override
	public ${Po} queryForObject(${column.columnSimpleClassName} ${column.columnName?uncap_first}) {
		${Po}Param param = new ${Po}Param();
		param.set${column.columnName?cap_first}(${column.columnName?uncap_first});
		return queryForObject(param);
	}

	@Override
	public int delete(${column.columnSimpleClassName} ${column.columnName?uncap_first}) {
		${Po} model = new ${Po}();
		model.set${column.columnName?cap_first}(${column.columnName?uncap_first});
		return delete(model);
	}
	</#if>	
	</#list>
}
