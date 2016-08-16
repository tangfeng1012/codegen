package com.hhly.codgen.service.impl;

import org.apache.commons.lang.StringUtils;

import com.hhly.codgen.model.ColumnModel;
import com.hhly.codgen.service.ColumnHandler;


/**
 * 针对java编程环境的数据类型转换器
 * @author 黄天�? *
 */
public class DataTypeConverterForJava implements ColumnHandler {

	public void handle(ColumnModel columnModel) {
		String javaType = columnModel.getColumnClassName();
		if("java.math.BigDecimal".equals(javaType) ){
			if(columnModel.getScale()>0){
				javaType = "java.lang.Double";
			}else{
				if(columnModel.getPrecision()>=1 && columnModel.getPrecision()<=9){
					javaType="java.lang.Integer";
				}else{
					javaType="java.lang.Long";
				}
			}
        }else if("java.sql.TIMESTAMP".equalsIgnoreCase(javaType) || "oracle.sql.TIMESTAMP".equalsIgnoreCase(javaType)){
        	javaType="java.util.Date";
        }
//        else{
//        	type="java.lang.String";
//        }
		
		//根据具体数据库方�?��的数据类型确定java编程语言中的数据类型
		String typeName = columnModel.getColumnTypeName();
		if("decimal".equalsIgnoreCase(typeName)
			||"money".equalsIgnoreCase(typeName)
			||"numeric".equalsIgnoreCase(typeName)
			||"float".equalsIgnoreCase(typeName)
			||"smallmoney".equalsIgnoreCase(typeName)){
			if(columnModel.getScale()>0){
				javaType = "java.lang.Double";
			}else{
				if(columnModel.getPrecision()>=1 && columnModel.getPrecision()<=9){
					javaType="java.lang.Integer";
				}else{
					javaType="java.lang.Long";
				}
			}
		}
				
		columnModel.setColumnClassName(javaType);
		columnModel.setColumnSimpleClassName(StringUtils.substringAfterLast(javaType, "."));//从全限定名中截取�?��类名
		//设置具体编程语言的数据类型所在的包，如java.lang.String的命名空间为java.lang
		columnModel.setColumnClassPackage(StringUtils.substringBeforeLast(javaType, "."));
	}

}
