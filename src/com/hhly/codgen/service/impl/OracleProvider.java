package com.hhly.codgen.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hhly.codgen.model.JdbcConfig;
import com.hhly.codgen.service.DbProvider;
import com.hhly.codgen.util.JdbcUtil;

public class OracleProvider extends DbProvider{
	
	public OracleProvider(JdbcConfig jdbcConfig) {
		// TODO Auto-generated constructor stub
		super(jdbcConfig);
	}

	@Override
	public Map<String, String> doGetColumnComments(String tableName) {
		Map<String, String> colComment = new LinkedHashMap<String, String>();
		String columnName = null, comment = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "select * from USER_COL_COMMENTS where TABLE_NAME='" + tableName.toUpperCase() + "'";
		try{
			stmt = getConn().createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				columnName = rs.getString("COLUMN_NAME").toLowerCase();
				comment = StringUtils.trim(rs.getString("COMMENTS"));
				colComment.put(columnName, comment);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			JdbcUtil.safelyClose(rs, stmt);
		}
		return colComment;
	}

	@Override
	public Map<String, String> doGetTableComment(String tableName) {
		Map<String, String> tableCommentMap = new LinkedHashMap<>();
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = getConn().createStatement();
			String sql = "select * from USER_TAB_COMMENTS where TABLE_NAME = '" + tableName.toUpperCase() + "'";
			rs = statement.executeQuery(sql);
			while(rs.next()){
				String comment = StringUtils.trim(rs.getString("COMMENTS"));
				tableCommentMap.put(tableName, comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.safelyClose(rs, statement);
		}
		
		return tableCommentMap;
	}
}
