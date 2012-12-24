package com.dylan.lang;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

/**
 * 读取配置文件工具类
 */
public class PropertyUtils {
	private static String propUrl = "develop.properties";
	private static Properties prop = new Properties();

	static {
		try {
			InputStream in = new BufferedInputStream(new ClassPathResource(
					propUrl).getInputStream());
			prop.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据name获得配置文件中的值
	 * 
	 * @param name
	 * @return
	 */
	public static String getString(String name) {
		return prop.getProperty(name);
	}

	public static void main(String[] args) {
		System.out.println(PropertyUtils.getString("permissionURL"));
	}
}
