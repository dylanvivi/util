package com.dylan.lang;

import junit.framework.Assert;

public class SqlUtil {
	/**
	 * 获取分页SQL
	 * @param sql	原始SQL
	 * @param page	页码, 从1开始
	 * @param pageSize	分页大小
	 * @return
	 */
	public static String getPageSql(String sql, int page, int pageSize) {
		Assert.assertTrue((page >= 1));
		Assert.assertTrue(pageSize > 0);
		int start = (page - 1) * pageSize + 1;
		int end = start + pageSize - 1;
		return getLimitSql(sql, start, end);
	}

	/**
	 * 获取分页SQL, 双闭
	 * @param sql	原始SQL
	 * @param start	起始条数, 从1开始
	 * @param end	结束条数
	 * @return
	 */
	public static String getLimitSql(String sql, int start, int end) {
		return "select Page_temp2.* from (select Page_temp1.*, rownum as rownum_id from (" + sql
				+ ") Page_temp1 where rownum <= " + end + " ) Page_temp2 where rownum_id >=" + start;
	}

	/**
	 * 获取合并SQL
	 * @param table	数据表名
	 * @param colnames	列名, 多个逗号分隔
	 * @param keynames	主键列名, 多个逗号分隔
	 * @return
	 */
	public static String getMergeSql(String table, String colnames, String keynames) {
		StringBuilder sb = new StringBuilder();
		sb.append("MERGE INTO " + table + " a ");
		sb.append(" USING (SELECT ");
		String[] colnamelist = colnames.split(",");
		for (int i = 0; i < colnamelist.length; i++) {
			String col = colnamelist[i].trim();
			sb.append(" ? " + col);
			if (i < colnamelist.length - 1)
				sb.append(",");
		}
		sb.append(" FROM dual) b ");
		sb.append("ON (");
		String[] keynamelist = keynames.split(",");
		for (int i = 0; i < keynamelist.length; i++) {
			String keyname = keynamelist[i].trim();
			sb.append(" a." + keyname + "= b." + keyname);
			if (i < keynamelist.length - 1)
				sb.append(" AND ");
		}
		sb.append(") " + "WHEN MATCHED THEN ");

		String updatenames = "";
		for (int i = 0; i < colnamelist.length; i++) {
			String colname = colnamelist[i].trim();
			if (!(keynames + ",").contains(colname + ",")) {
				updatenames += " a." + colname + "= b." + colname;
				updatenames += (" ,");
			}
		}
		if (updatenames.endsWith(","))
			updatenames = updatenames.substring(0, updatenames.length() - 1);
		sb.append(" UPDATE SET " + updatenames);
		sb.append(" WHEN NOT MATCHED THEN INSERT (" + colnames + ") ");
		sb.append(" VALUES (");
		for (int i = 0; i < colnamelist.length; i++) {
			String col = colnamelist[i].trim();
			sb.append(" b." + col);
			if (i < colnamelist.length - 1)
				sb.append(",");
		}
		sb.append(")");
		return sb.toString();
	}
}
