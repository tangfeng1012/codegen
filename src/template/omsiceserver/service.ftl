<#include "include/head.ftl">
package ${NamespaceService};

<#include "include/copyright.ftl">

import java.util.List;
import java.util.Map;
import ${NamespaceEntity}.${entity}Entity;

/**
 * ¡¶${table.tableLabel}¡· service
 * @author ${copyright.author}
 *
 */
public interface ${entity}Service extends BaseService<${entity}Entity> {

	//public List<ActivityProblemEntity> queryActitityProblemByobj(Map<String,Object> paramMap);
}
