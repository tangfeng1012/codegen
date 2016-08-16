package com.hhly.codgen.boot;

import org.junit.Test;

import com.hhly.codgen.model.ColumnModel;
import com.hhly.codgen.model.ProjectConfig;
import com.hhly.codgen.service.BuildConfig;
import com.hhly.codgen.service.Builder;
import com.hhly.codgen.service.ColumnHandler;
import com.hhly.codgen.service.impl.CodeBuilder;
import com.hhly.codgen.service.impl.ProjectBuildConfig;
import com.hhly.codgen.util.JSONUtils;
import com.hhly.codgen.util.ProjectConfigHelper;

public class Main {
	
	@Test
	public void projectStart(){
		try {
			ProjectConfig projectConfig = ProjectConfigHelper.getDefaultProjectConfig(null);
//			System.out.println(JSONUtils.obj2json(projectConfig));
			//对数据库列进行处理
			projectConfig.getDbProvider().getColumnHandlers().add(new ColumnHandler() {
				@Override
				public void handle(ColumnModel columnModel) {
					String columnName = columnModel.getColumnName();
					if(!columnName.contains("_")){
						columnName = columnName.toLowerCase().trim();
					}else{
						String[] splitStr = columnName.split("_");
						StringBuilder builder = new StringBuilder();
						builder.append(splitStr[0].toLowerCase());
						for(int i = 1; i < splitStr.length; i++){
							String s = splitStr[i].substring(0, 1).toUpperCase() + splitStr[i].substring(1);
							builder.append(s);
						}
						columnName = builder.toString();
					}
					columnModel.setColumnName(columnName);
				}
			});
			
			ProjectBuildConfig buildConfig = new ProjectBuildConfig(projectConfig);
			buildConfig.setTableName("T_OMS_ACTIVITY");
			
			Builder builder = new CodeBuilder(buildConfig);
			builder.build();
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
