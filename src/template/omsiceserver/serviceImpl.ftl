<#include "include/head.ftl">
package ${NamespaceServiceImpl};

<#include "include/copyright.ftl">

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${NamespaceMapper}.${entity}Mapper;
import com.hhly.oms.mapper.BaseMapper;
import ${NamespaceService}.${entity}Service;
import ${NamespaceEntity}.${entity}Entity;

/**
 * ¡¶${table.tableLabel}¡· serviceImpl
 * @author ${copyright.author}
 *
 */
@Service("${entity?uncap_first}Service")
public class ${entity}ServiceImpl extends BaseServiceImpl<${entity}Entity> implements ${entity}Service {

	@Autowired
	private ${entity}Mapper ${entity?uncap_first}Mapper;
	
	@Override
	@Resource(name = "${entity?uncap_first}Mapper")
	protected void setBaseMapper(BaseMapper<${entity}Entity> baseMapper) {
		super.baseMapper = baseMapper;
	}
	
	
}