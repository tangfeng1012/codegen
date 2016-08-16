package com.hhly.codgen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hhly.codgen.model.JdbcConfig;

public class JdbcUtil {
	
	/**
	 * 
	 * @param jdbcConfig
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static Connection getConn(JdbcConfig jdbcConfig){
		if(jdbcConfig == null)
			return null;
		
		try {
			Class.forName(jdbcConfig.getDriver());//数据库驱动
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcConfig.getUrl(), jdbcConfig.getUsername(), jdbcConfig.getPassword());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++数据库成功建立连接++++++++++++++++++++++++++++++++++");
		return connection;
	}
	
	public static void safelyClose(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void safelyClose(PreparedStatement pstmt) {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void safelyClose(Statement stmt) {
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void safelyClose(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void safelyClose(ResultSet rs,Statement stmt){
		safelyClose(rs);
		safelyClose(stmt);
	}
	public static void safelyClose(ResultSet rs,Statement stmt,Connection conn) {
		safelyClose(rs);
		safelyClose(stmt);
		safelyClose(conn);
	}
	public static void safelyClose(ResultSet rs,PreparedStatement pstmt,Connection conn) {
		safelyClose(rs);
		safelyClose(pstmt);
		safelyClose(conn);
	}
	public static void safelyClose(PreparedStatement pstmt, Connection conn) {
		safelyClose(pstmt);
		safelyClose(conn);
	}

	public static void safelyClose(Statement stmt, Connection conn) {
		safelyClose(stmt);
		safelyClose(conn);
	}

}
