package com.dylan.db;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;

import com.dylan.lang.StringUtils;

/**
 * 创建实体类
 * @author dylan
 *
 */
public class BuildDTO {
	/**
	 * 创建javaBean实体类
	 * @param tableName	数据库表名
	 */
	public static void buildWithORM(String tableName) {
		Connection conn = null;
		PreparedStatement pment = null;
		ResultSet rs = null;
		PrintWriter pw = null;

		DataBase db = new DataBase();
		try {
			String pojoName = StringUtils.firstToUpperAndReplace(tableName);

			/***---------------修改生成位置-------------------------------------*/

			pw = new PrintWriter(new File("src/main/java/com/dylan/entity/" + pojoName + ".java"));

			/***---------------修改包名-------------------------------------*/
			pw.println("package com.dylanvivi.pojo;\n\n");
			pw.println("import java.sql.Date;\n\n");

			pw.println("import com.phoenix.dao.support.annotation.Column;");
			pw.println("import com.phoenix.dao.support.annotation.Id;");
			pw.println("import com.phoenix.dao.support.annotation.Table;\n\n");

			pw.println("/**");
			pw.println(" * 通过数据库内表的字段动态生成 javabean");
			pw.println(" * @author dylan");
			pw.println(" **/");
			pw.println("@Table(value = \"" + tableName + "\")");
			pw.println("public class " + pojoName + " {\t");

			//创建连接
			conn = db.getConn();
			//构建预处理器
			String sql = "select * from " + tableName + " where 1 = 2";
			pment = conn.prepareStatement(sql);
			rs = pment.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();

			//获取表单主键
			ResultSet primaryKey = conn.getMetaData().getPrimaryKeys(null, null, tableName);

			//获取表单的列数
			int colum = metaData.getColumnCount();

			for (int i = 1; i <= colum; i++) {
				String pStr = ""; //setXxxx
				String typeStr = ""; //类型

				//获取列名
				String column = metaData.getColumnName(i).toUpperCase();
				String columName = StringUtils.replaceUnderlineAndfirstToUpper(metaData.getColumnName(i).toLowerCase());
				//获取每一列类型
				int type = metaData.getColumnType(i);
				typeStr = sqlType2JavaType(type);

				//判断是否主键
				while (primaryKey.next()) {
					if (column.equals(primaryKey.getString(4))) {//判断是否为主键 3:表名 4:主键名
						pw.println("\t@Id");
					}
				}

				//组装 private 的语句
				pStr += "private " + typeStr + " " + columName + ";";

				//输出 private 的字段

				pw.println("\t@Column(value=\"" + column + "\")");
				pw.println("\t" + pStr + "\n");

			}

			String constructStr = ""; //构造
			//组装空参构造函数
			constructStr += "public " + pojoName + "() {\n\n\t}";
			//输出
			pw.println("\n\t" + constructStr + "\n");

			for (int i = 1; i <= colum; i++) {
				String getStr = "";
				String setStr = "";
				String typeStr = "";
				//获取列名
				String columName = StringUtils.replaceUnderlineAndfirstToUpper(metaData.getColumnName(i).toLowerCase());
				//获取每一列类型
				int type = metaData.getColumnType(i);
				//判断
				typeStr = sqlType2JavaType(type);

				//组装 set 的语句
				setStr += "public void set" + columName.substring(0, 1).toUpperCase() + "" + columName.substring(1)
						+ "(" + typeStr + " " + columName + ") {\n";
				setStr += "\t\tthis." + columName + " = " + columName + ";\n\t}";

				//组装 get 的语句
				getStr += "public " + typeStr + " get" + columName.substring(0, 1).toUpperCase() + ""
						+ columName.substring(1) + "() {\n\t";
				getStr += "\treturn this." + columName + ";\n\t}";
				//输出set
				pw.println("\t" + setStr);
				//输出 get
				pw.println("\t" + getStr);
			}
			pw.println("}");

			//清理缓存
			pw.flush();
			pw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭连接
			db.closeAll();
		}
	}

	public static void build(String tableName) {
		Connection conn = null;
		PreparedStatement pment = null;
		ResultSet rs = null;
		PrintWriter pw = null;

		DataBase db = new DataBase();
		try {
			String pojoName = StringUtils.firstToUpperAndReplace(tableName);

			/***---------------修改生成位置-------------------------------------*/

			pw = new PrintWriter(new File("src/main/java/com/dylan/entity/" + pojoName + ".java"));

			/***---------------修改包名-------------------------------------*/
			pw.println("package com.dylanvivi.pojo;\n\n");
			pw.println("import java.sql.Date;\n\n");

			pw.println("/**");
			pw.println(" * 通过数据库内表的字段动态生成 javabean");
			pw.println(" * @author dylan");
			pw.println(" **/");

			pw.println("public class " + pojoName + " {\t");

			//创建连接
			conn = db.getConn();
			//构建预处理器
			String sql = "select * from " + tableName + " where 1 = 2";
			pment = conn.prepareStatement(sql);
			rs = pment.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();

			//获取表单主键
			ResultSet primaryKey = conn.getMetaData().getPrimaryKeys(null, null, tableName);

			//获取表单的列数
			int colum = metaData.getColumnCount();

			for (int i = 1; i <= colum; i++) {
				String pStr = ""; //setXxxx
				String typeStr = ""; //类型

				//获取列名
				String column = metaData.getColumnName(i).toUpperCase();
				String columName = StringUtils.replaceUnderlineAndfirstToUpper(metaData.getColumnName(i).toLowerCase());
				//获取每一列类型
				int type = metaData.getColumnType(i);
				//判断列类型
				typeStr = sqlType2JavaType(type);

				//组装 private 的语句
				pStr += "private " + typeStr + " " + columName + ";";

				//输出 private 的字段
				pw.println("\t" + pStr + "\n");

			}

			String constructStr = ""; //构造
			//组装空参构造函数
			constructStr += "public " + pojoName + "() {\n\n\t}";
			//输出
			pw.println("\n\t" + constructStr + "\n");

			for (int i = 1; i <= colum; i++) {
				String getStr = "";
				String setStr = "";
				String typeStr = "";
				//获取列名
				String columName = StringUtils.replaceUnderlineAndfirstToUpper(metaData.getColumnName(i).toLowerCase());
				//获取每一列类型
				int type = metaData.getColumnType(i);
				//判断
				typeStr = sqlType2JavaType(type);

				//组装 set 的语句
				setStr += "public void set" + columName.substring(0, 1).toUpperCase() + "" + columName.substring(1)
						+ "(" + typeStr + " " + columName + ") {\n";
				setStr += "\t\tthis." + columName + " = " + columName + ";\n\t}";

				//组装 get 的语句
				getStr += "public " + typeStr + " get" + columName.substring(0, 1).toUpperCase() + ""
						+ columName.substring(1) + "() {\n\t";
				getStr += "\treturn this." + columName + ";\n\t}";
				//输出set
				pw.println("\t" + setStr);
				//输出 get
				pw.println("\t" + getStr);
			}
			pw.println("}");
			//清理缓存
			pw.flush();
			pw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭连接
			db.closeAll();
		}
	}

	/**
	  * 功能：获得列的数据类型 -- mysql
	  * @param sqlType
	  * @return
	  */
	private static String sqlType2JavaType(int type) {
		String typeStr = "";
		if (Types.INTEGER == type || Types.NUMERIC == type) {
			typeStr = "int";
		} else if (Types.VARCHAR == type || Types.CHAR == type || -1 == type) {
			typeStr = "String";
		} else if (Types.DECIMAL == type) {
			typeStr = "double";
		} else if (Types.TIMESTAMP == type || Types.DATE == type) {
			typeStr = "Date";
		} else if (Types.CLOB == type) {
			typeStr = "long";
		}
		return typeStr;
	}
}
