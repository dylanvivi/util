package cn.bidlink.framework.core.utils.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;
import cn.bidlink.framework.core.utils.pager.Pager;


 
/**
 * {"data":[{"id":1000,"code":"A","codeName":"测试客户_0","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1001,"code":"A","codeName":"测试客户_1","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1002,"code":"A","codeName":"测试客户_2","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1003,"code":"A","codeName":"测试客户_3","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1004,"code":"A","codeName":"测试客户_4","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1005,"code":"A","codeName":"测试客户_5","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1006,"code":"A","codeName":"测试客户_6","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1007,"code":"A","codeName":"测试客户_7","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1008,"code":"A","codeName":"测试客户_8","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1009,"code":"A","codeName":"测试客户_9","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"},{"id":1010,"code":"A","codeName":"测试客户_10","description":"OK","isWarmType":"是","status":"启用","scope":"共享","modifier":"Admin","modifyTime":"2011-11-15 17:22:06"}],"totalCount":11,"currentPage":1,"totalpages":2}
* @ClassName: JsonUtils
* @Description: TODO描述
* @author dj <a>mailto:zuiwoxing@gmail.com</a>
* @date 2011-11-15 下午05:22:35
*
 */
public class JsonUtils {
	
	/**
	 * 
	* @Title: configJson
	* @Description:  JSON解析器具
	* @param  datePattern yyyy-MM-dd HH:mm
	* @param  excludes 排出字段
	* @return JsonConfig    返回类型
	* @throws
	 */
	public static JsonConfig configJson(String datePattern,String [] excludes) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor(datePattern));
		jsonConfig.setPropertySetStrategy(new BidDatePropertySetStrategy(jsonConfig));
		return jsonConfig;
	}
	

    /**
     * 通过bean生成JSON数据 对象
    * @Title: getJsonFromBean
    * @Description: TODO描述
    * @param  bean
    * @param  datePattern 日志格式yyyy-MM-dd HH:mm:ss
    * @param excludes 排出字段
    * @param @return    设定文件
    * @return JSONObject    返回类型
    * @throws
     */
	public static synchronized JSONObject getJsonFromBean(Object bean,String datePattern,String [] excludes) {
			JsonConfig config = configJson(datePattern,excludes);
			return  JSONObject.fromObject(bean,config);
	}
	
	/**
	 * 通过bean生成JSON 数组
	* @Title: getJsonArrayFromObject
	* @Description: TODO描述
	* @param  objectList 集合对象
	* @param  datePattern 日志格式yyyy-MM-dd HH:mm:ss
	* @param excludes 排出字段
	* @param @return    设定文件
	* @return JSONArray    返回类型
	* @throws
	 */
	public static synchronized JSONArray getJsonArrayFromList(Object objectList,String datePattern,String [] excludes) {
		JsonConfig config = configJson(datePattern,excludes);
		return JSONArray.fromObject(objectList,config);
	}
	
	/**
	 * 
	* @Title: getJsonFromList
	* @Description: 通过List生成JSON数据
	* @param  pager 分页组件
	* @param  beanList 集合对象
	* @param  datePattern 时间格式
	* @param  excludes 排除字段
	* @param     设定文件
	* @return JSONObject    返回类型
	* @throws
	 */
	public static synchronized JSONObject getJsonFromList(Pager pager, List<?> beanList,String datePattern,String [] excludes) {
		JSONObject res = new JSONObject();
		JSONArray arr = null;
		if (beanList == null || beanList.size() ==0) {
			arr = new JSONArray();
		} else {
			arr = getJsonArrayFromList(beanList, datePattern,excludes);
		}
		res.put("data",arr);
		if (pager != null) {
			res.put("totalCount", pager.getTotalRows());
			res.put("currentPage", pager.getCurrentPage());
			res.put("totalpages", pager.getTotalPages());
		}
		return res;
	}

	public static synchronized <T> T  toBean(String json,Class<T> cls,String datePattern,String [] excludes) {
		T t = null;
		try {
			JsonConfig config = configJson(datePattern,excludes);
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {datePattern}));
			JSONObject jsonObj = JSONObject.fromObject(json, config);
			t = cls.cast(JSONObject.toBean(jsonObj, cls));
		}finally {
			 JSONUtils.getMorpherRegistry().deregisterMorpher(new DateMorpher(new String[] {datePattern}));
 		}
		return t;
	}
	
	
	public static synchronized <T> List<T> toBeanList(String json,Class<T> cls,String datePattern,String [] excludes) {
		JsonConfig config = configJson(datePattern,excludes);
 		JSONArray jsonObj = JSONArray.fromObject(json, config);
 	    List<T>  lists = new ArrayList<T>();
		for(int i=0; i < jsonObj.size(); i++) {
	    	JSONObject obj = (JSONObject) jsonObj.get(i);
	    	lists.add(toBean(obj.toString(), cls, datePattern, excludes));
	    }
		return lists;
 	}
	

    /**
     * 
     * @param array
     * @return
     */
	public static  String getStringFromArray(Object[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		return JSONArray.fromObject(array).toString();
	}
 
}
