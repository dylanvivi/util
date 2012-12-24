package com.dylan.lang.json;

import java.sql.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * JSON Date Parse
 * @author dylan
 *
 */
public class JSONUtils {
	
	public static JsonConfig  getJsonConfig(){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());  
		return jsonConfig;
	}
	
	public static JSONObject returnObj(List list,int total){
		JSONObject obj = new JSONObject();
		obj.put("total", total);
		JSONArray arrlist = JSONArray.fromObject(list,getJsonConfig());
		obj.put("results", arrlist);
		return obj;
	}
}
