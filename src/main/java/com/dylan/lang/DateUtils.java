package com.dylan.lang;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

	private static final String DEFAULT_FULL_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final Calendar DEFAULT_CALENDAR = Calendar.getInstance();

	/**
	 * 当前时间
	 * @return
	 */
	public static String getNow(String pattern) {
		return DateUtils.format(new Date(), pattern);
	}

	/**
	 * 当前时间:格式:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getNow() {
		return DateUtils.format(new Date(), DEFAULT_FULL_PATTERN);
	}

	/**
	 * 当前日期:格式yyyy-MM-dd
	 * @return
	 */
	public static String getToday() {
		return DateUtils.format(new Date(), DEFAULT_PATTERN);
	}

	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	/**
	 * 当前年份
	 * @return
	 */
	public static int getYear() {
		return DEFAULT_CALENDAR.get(Calendar.YEAR);
	}

	/**
	 * 当前月份
	 * @return
	 */
	public static int getMonth() {
		return DEFAULT_CALENDAR.get(Calendar.MONTH) + 1;
	}

	/**
	 * 当前日期
	 * @return
	 */
	public static int getDay() {
		return DEFAULT_CALENDAR.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 当前小时
	 * @return
	 */
	public static int getHours() {
		return DEFAULT_CALENDAR.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinutes() {
		return DEFAULT_CALENDAR.get(Calendar.MINUTE);
	}

	public static int getSeconds() {
		return DEFAULT_CALENDAR.get(Calendar.SECOND);
	}

	/**
	 * String --> Date
	 * @param strDate
	 * @param dateFormat
	 * @return
	 */
	public static Date parse(String strDate, String dateFormat) {
		if (strDate == null || strDate.length() == 0) {
			return null;
		}
		Date date = null;
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 默认格式:yyyy-MM-dd
	 * @param strDate
	 * @return
	 */
	public static Date parse(String strDate) {
		return parse(strDate, DEFAULT_PATTERN);
	}

	/**
	 * 格式:yyyy-MM-dd HH:mm:ss
	 * @param strDate
	 * @return
	 */
	public static Date parseFull(String strDate) {
		return parse(strDate, DEFAULT_FULL_PATTERN);
	}

	/**
	 * 比较日期大小
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 */
	public static boolean compare(String strStartDate, String strEndDate) {
		Date startDate = parse(strStartDate, DEFAULT_PATTERN);
		Date endDate = parse(strEndDate, DEFAULT_PATTERN);
		long startTime = getMillisOfDate(startDate);
		long endTime = getMillisOfDate(endDate);
		return endTime - startTime > 0;
	}

	/**
	 * 返回两个日期相差毫秒
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 */
	public static long compareDateStr(String strStartDate, String strEndDate) {
		Date startDate = parse(strStartDate, DEFAULT_PATTERN);
		Date endDate = parse(strEndDate, DEFAULT_PATTERN);
		long startTime = getMillisOfDate(startDate);
		long endTime = getMillisOfDate(endDate);
		return endTime - startTime;
	}

	public static long getMillisOfDate(Date date) {
		DEFAULT_CALENDAR.setTime(date);
		return DEFAULT_CALENDAR.getTimeInMillis();
	}

	public static String addDay(Date date, int count, int field, String format) {
		DEFAULT_CALENDAR.setTime(date);
		int year = getYear();
		int month = getMonth() - 1;
		int day = getDay();
		int hours = getHours();
		int minutes = getMinutes();
		int seconds = getSeconds();
		Calendar calendar = new GregorianCalendar(year, month, day, hours, minutes, seconds);
		calendar.add(field, count);
		String tmpDate = format(calendar.getTime(), format);
		DEFAULT_CALENDAR.setTime(new Date());
		return tmpDate;
	}

	/**
	 * 得到本月最后一天
	 * @return
	 */
	public static String getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getYear());
		cal.set(Calendar.MONTH, getMonth());
		cal.set(Calendar.DATE, 0);
		return format(cal.getTime(), DEFAULT_PATTERN);
	}

	/**
	 * 本周第一天
	 * @return
	 */
	public static String getFirstDayOfWeek() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return format(c.getTime(), DEFAULT_PATTERN);
	}

	public static String getYesterday() {
		return addDay(new Date(), -1, Calendar.DATE, DEFAULT_PATTERN);
	}

	public static boolean isDate(String date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean isDate(String date) {
		return isDate(date, DEFAULT_PATTERN);
	}

	/**
	 * 一天的开始
	 * @param day:yyyy-MM-dd
	 * @return
	 */
	public static String getBeginOfDay(String day) {
		if (StringUtils.isEmpty(day) || !isDate(day)) {
			return null;
		}
		return day + " 00:00:00";
	}

	/**
	 * 一天的结束
	 * @param  day:yyyy-MM-dd
	 * @return
	 */
	public static String getEndOfDay(String day) {
		if (StringUtils.isEmpty(day) || !isDate(day)) {
			return null;
		}
		return day + " 23:59:59";
	}

	public static String getBeginOfDay() {
		return getBeginOfDay(getToday());
	}

	public static String getEndOfDay() {
		return getEndOfDay(getToday());
	}

}
