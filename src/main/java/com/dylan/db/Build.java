package com.dylan.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自动生成javaBean实体类
 * Oracle版本
 * @author dylan
 * 2012.03.07  
 */
public class Build extends DataBase {

	private final Logger log = LoggerFactory.getLogger(Build.class);

	/**
	 * 自动生成JAVABEAN实体类
	 * 
	 */
	public void build(String... tnames) {
		for (String table : tnames) {
			log.debug("processing " + table + "...");
			BuildDTO.build(table.toUpperCase());
		}

	}

	public void buildLike(String prix) {
		String sql = "select table_name from user_tables where table_name Like '%" + prix + "%'";
		List<String> tables = queryTnames(sql);
		process(tables);

	}

	/**
	 * oracle
	 */
	public void buildAll() {
		String sql = "select table_name from user_tables ";
		List<String> tables = queryTnames(sql);
		process(tables);

	}

	/**
	 * mysql
	 */
	public void buildAllMySql() {
		String sql = "select name from sysobjects where type='U'";
		List<String> tables = queryTnames(sql);
		process(tables);
	}

	private List<String> queryTnames(String sql) {
		List<String> list = new ArrayList<String>();
		try {
			this.conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String tname = rs.getString(1);
				if (!tname.equals("dtproperties")) {
					list.add(tname);
				}
			}
			this.closeAll();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	private void process(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			String tname = (String) list.get(i);
			log.debug("processing " + tname + "...");
			BuildDTO.build(tname.toUpperCase());
		}
	}

}
