package com.dylan.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 对org.json进行简单封装
 * @author dylan
 * @version 2012-12-19
 */
public class JSONUtils {

	public static <T> JSONObject fromObject(T obj, String pattern) throws JSONException {
		if (obj instanceof Map) {
			return fromMap((Map) obj, pattern);
		}
		JSONObject data = new JSONObject();
		Class tCls = obj.getClass();
		Method[] methods = tCls.getMethods();
		for (Method method : methods) {
			if (!method.getName().startsWith("get") || method.getName().equalsIgnoreCase("getClass")) { //get开头的
				continue;
			}
			String fieldName = method.getName().substring(3, 4).toLowerCase()
					+ method.getName().substring(4).toLowerCase();
			try {
				Object value = method.invoke(obj, new Object[] {});
				data.put(fieldName, covernToJson(value, pattern));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public static <T> JSONObject fromObject(T obj) throws JSONException {
		return fromObject(obj, "yyyy-MM-dd");
	}

	public static <T> JSONArray fromArray(Collection<T> dataset) throws JSONException {
		JSONArray arr = new JSONArray();
		for (T obj : dataset) {
			JSONObject data = null;
			data = fromObject(obj);
			arr.put(data);
		}
		return arr;
	}

	private static Object covernToJson(Object value, String pattern) throws JSONException {
		if (value == null) {
			return null;
		}
		if (value instanceof Date) {
			Date date = (Date) value;
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		if (value instanceof String) {
			return (String) value;
		}
		if (value instanceof Boolean) {
			return value.toString();
		}
		if (value instanceof Number) {
			return value.toString();
		}
		return fromObject(value, pattern);
	}

	private static JSONObject fromMap(Map<String, Object> map, String pattern) throws JSONException {
		JSONObject obj = new JSONObject();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object data = map.get(key);
			obj.put(key, covernToJson(data, pattern));
		}
		return obj;
	}

}
