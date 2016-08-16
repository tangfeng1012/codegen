<#include "include/head.ftl">
package ${NamespaceAction};

<#include "include/copyright.ftl">

import com.hhly.base.action.BaseAction;
import org.apache.log4j.Logger;
import ${NamespaceService};

/**
 * @author ${copyright.author} 
 */
 public class ${entity}Action extends baseAction{
 
 	private final static Logger LOG = Logger.getLogger(${entity}Action.class);
 	
 	@Autowired
	private I${entity}Service ${entity}Service;
	
	public void add${entity}(){
		
	}
	
	public void del${entity}(){
		
	}
	
	public void upt${entity}(){
		
	}
	
	
 }