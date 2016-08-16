<#include "include/head.ftl">
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${NamespaceMapper}.${entity}Mapper">

	<!--类与表的字段映射关系-->
	<resultMap id="${entity}ResultMap" type="${NamespaceEntity}.${entity}Entity">
		<#list table.columnList as column>
			<result property="${column.fieldName}" column="${column.columnName}"/>
		</#list>
	</resultMap>

	<!--新增-->
	<insert id="insert" parameterType="${NamespaceEntity}.${entity}Entity">
		INSERT INTO ${table.tableName} (
		<trim suffixOverrides=",">
		  <#list table.columnList as column>
			<if test="${column.fieldName} != null">
		      ${column.columnName},
		    </if>
		  </#list>
		</trim>   
		)VALUES(
		<trim suffixOverrides=",">
			<#list table.columnList as column>
			<if test="${column.fieldName} != null">
		  		<@mapperEl column.fieldName/>,
			</if>
			</#list>
		</trim>
		)
	</insert>
	
	<!--修改-->
	<update id="update" parameterType="${NamespaceEntity}.${entity}Entity">
		UPDATE ${table.tableName} 
		<set>
		    <#list table.columnList as column>
		    <if test="${column.fieldName} != null">
		      ${column.columnName} = <@mapperEl column.fieldName/>,
		    </if>
		    </#list>
		</set>
		<where>
		  <#list table.columnList as column>
				<if test="${column.fieldName} != null">
			    AND ${column.columnName} = <@mapperEl column.fieldName/>
			  </if>
		  </#list>
		</where>
	</update>

	<!--删除-->
	<delete id="delete" parameterType="${NamespaceEntity}.${entity}Entity">
		DELETE FROM ${table.tableName}
		<where>
		    <#list table.columnList as column>
			<if test="${column.fieldName} != null">
		      AND ${column.columnName} = <@mapperEl column.fieldName/>
		    </if>
		    </#list>
		</where>
	</delete>

	<!--查询-->
	<select id="qry${entity}Record" parameterType="${NamespaceEntity}.${entity}Entity" resultMap="${entity}ResultMap" >
		SELECT  
		t.*
		FROM ${table.tableName} t 
		<where>
		    <#list table.columnList as column>
			<if test="${column.fieldName} != null">
		      AND ${column.columnName} = <@mapperEl column.fieldName/>
		    </if>
		    </#list>
		</where>
	</select>

	<!--查询记录数-->
	<select id="qry${entity}RecordCOUNT" parameterType="${NamespaceEntity}.${entity}Entity"  resultType="int">
		SELECT  
		COUNT(1)
		FROM ${table.tableName} t 
		<where>
		    <#list table.columnList as column>
			<if test="${column.fieldName} != null">
		      AND ${column.columnName} = <@mapperEl column.fieldName/>
		    </if>
		    </#list>
		</where>
	</select>
</mapper>