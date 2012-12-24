package com.dylan.lang;

/**
 * String常用工具类..
 * 持续更新ing..
 * @author dylan
 *
 */
public class StringUtils {

	/**
	 * 判断字符串是否为空 or is NULL
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim()) || str.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return str == null;
	}

	/**
	* 首字母大写
	* @param srcStr
	* @return
	*/
	public static String firstCharacterToUpper(String srcStr) {
		return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
	}

	/**
	* 替换字符串并让它的下一个字母为大写
	* @param srcStr
	* @param org
	* @param ob
	* @return
	*/
	public static String replaceUnderlineAndfirstToUpper(String srcStr, String org, String ob) {
		String newString = "";
		int first = 0;
		while (srcStr.indexOf(org) != -1) {
			first = srcStr.indexOf(org);
			if (first != srcStr.length()) {
				newString = newString + srcStr.substring(0, first) + ob;
				srcStr = srcStr.substring(first + org.length(), srcStr.length());
				srcStr = firstCharacterToUpper(srcStr);
			}
		}
		newString = newString + srcStr;
		return newString;
	}

	/**
	 * 替换"_"并让它的下一个字母为大写
	 * @param str
	 * @return
	 */
	public static String replaceUnderlineAndfirstToUpper(String str) {
		return replaceUnderlineAndfirstToUpper(str, "_", "");
	}

	/**
	 * 生成表名，首字母大写
	 * @param str
	 * @return
	 */
	public static String firstToUpperAndReplace(String str) {
		String data = firstCharacterToUpper(str.toLowerCase());
		return replaceUnderlineAndfirstToUpper(data, "_", "");
	}

	public static String trimFloat(float str, int loc) {
		return String.format("%." + loc + "f", str);
	}

	public static String trimFloat(String str, int loc) {
		return String.format("%." + loc + "f", Float.parseFloat(str));
	}

}
