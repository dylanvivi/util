package com.dylan.db;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBase {

	Logger logger = LoggerFactory.getLogger(DataBase.class);

	private static String DRIVER;
	private static String URL;
	private static String USER;
	private static String PASS;

	public Connection conn;
	public PreparedStatement pstmt;
	public ResultSet rs;
	public CallableStatement cstmt;

	public Connection getConn() throws ClassNotFoundException {
		Connection conn = null;
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream("/db.properties"));
			DRIVER = properties.getProperty("driver");
			URL = properties.getProperty("url");
			USER = properties.getProperty("user");
			PASS = properties.getProperty("password");
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASS);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void closeAll() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int execulQ(String sql, String[] param) {
		int num = 0;
		try {
			conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < param.length; i++) {
				pstmt.setString(i + 1, param[i]);
			}
			num = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
}
