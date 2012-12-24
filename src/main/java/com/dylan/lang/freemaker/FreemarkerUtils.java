package com.dylan.lang.freemaker;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtils {
	private static final Logger logger = Logger.getLogger(FreemarkerUtils.class);
	private static Configuration cfg;

	static {
		try {
			cfg = new Configuration();
			cfg.setDefaultEncoding("utf-8");
			cfg.setDirectoryForTemplateLoading(new File(getClassPath()));
		} catch (Exception e) {
			logger.error("init freemarker config error!", e);
		}
	}

	/**
	 * classpath根目录
	 * @return
	 */
	private static String getClassPath() {
		return FreemarkerUtils.class.getClassLoader().getResource("").getPath();
	}

	/**
	 * 
	 * @param ftlName	ftl模板路径
	 * @param contents	ftl模板内容
	 * @return
	 */
	public static String getTemplateOutput(String ftlName, Map<String, Object> contents) {
		try {
			Template template = cfg.getTemplate(ftlName);
			Writer writer = new StringWriter();
			template.process(contents, writer);

			return writer.toString();
		} catch (Exception e) {
			logger.error("FreemarkerUtils getTemplateOutput() error!", e);
		}

		return "";
	}

	public static void main(String[] args) {
		String ftlName = "ftl/refuse_message.ftl";
		Map<String, Object> contents = new HashMap<String, Object>();
		contents.put("msgId", 1);
		contents.put("user", "test");
		contents.put("prdType", "test");

		String text = getTemplateOutput(ftlName, contents);
		System.out.println(text);
	}
}
