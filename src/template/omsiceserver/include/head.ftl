<#-- variants  declaration begin -->
<#assign po=(moduleName)>
<#assign Po=(moduleName?cap_first)>
<#assign entity=(entityName)>
<#--  variants  declaration end -->
<#-- user-defined macro begin-->
<#macro mapperEl value>${r"#{"}${value}}</#macro>
<#macro jspEl value>${r"${"}${value}}</#macro> 
<#-- user-defined macro end-->
<#assign primaryKey="">
<#assign primaryColumn="">
<#list table.columnList as column>
<#if column.primaryKey>
<#assign primaryKey=column.columnName>
<#assign primaryColum=column>
</#if>
</#list>