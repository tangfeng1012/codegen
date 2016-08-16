<#include "include/head.ftl">
package ${NamespaceDao};

import com.qtone.aow.dao.BaseDao;
import ${NamespaceModel}.${Po};

<#include "include/copyright.ftl">

/**
 * ��${tableLabel}�� ���ݷ��ʽӿ���
 * @author ${copyright.author}
 *
 */
public interface ${Po}Dao extends BaseDao<${Po}> {
	/**
	 * ȡ�÷���Ĭ�����������м�¼��
	 * @return
	 */
	long getRecordCount();  
	<#list table.columnList as column>
	<#if column.primaryKey>
	/**
	 * ��������ȡ��һ��ʵ��ģ��
	 * @param ${column.columnName?uncap_first} ����
	 * @return
	 */
	${Po} queryForObject(${column.columnSimpleClassName} ${column.columnName?uncap_first});
	/**
	 * ��������ɾ��һ��ʵ��ģ��
	 * @param ${column.columnName?uncap_first} ����
	 * @return Ӱ���¼������������1�����򷵻�0
	 */
	int delete(${column.columnSimpleClassName} ${column.columnName?uncap_first});
	</#if>	
	</#list>
}