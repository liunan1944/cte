package com.cte.credit.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
* @Title: 日期类工具类
* @Package com.bill99.ifs.crs.util 
* @author xiaocl chenglin.xiao@99bill.com   
* @date 2015年5月20日 下午4:18:23 
* @version V1.0
 */
public class DateUtil {
	/** */
	public static final String DATESHOWFORMAT = "yyyy-MM-dd";

	/** */
	public static final String TIMESHOWFORMAT = "HH:mm:ss";
	/** */
	public static final String DATETIMESHOWFORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 时间格式（yyyy-MM-dd 00:00:00），如：2015-07-16 ：00:00:00
	 */
	public static final String BEGIN_OF_THE_DAY = "yyyy-MM-dd 00:00:00";
	public static final String END_OF_THE_DAY = "yyyy-MM-dd 23:59:59";
	
	public static String getSimpleDate(Date date) {
		if(date == null)return null;
		SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd");
		return s.format(date);
	}
	public static String getSimpleDate(Date date,String format) {
		if(date == null)return null;
		SimpleDateFormat s = new SimpleDateFormat(format);
		return s.format(date);
	}
	public static String getSimpleDateBefore(String format,int months) {
	      Calendar calendar = Calendar.getInstance();
	      calendar.add(Calendar.MONTH,-months);
	      Date date = calendar.getTime();
		return new SimpleDateFormat(format).format(date);
	}
	/**
	 * 距今年份差
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Float getYearsBetweenNow(Date date,int f) throws ParseException {
		if (date==null) {
			return null;
		}
		long days = (f > 0 ? new Date().getTime() - date.getTime() : date
				.getTime() - new Date().getTime())
				/ (24 * 60 * 60 * 1000) + 1;
		return days / 365f;
	}
	/**
	 * 距今DAY差
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Float getDaysBetweenNow(Date date,int f) throws ParseException {
		if (date==null) {
			return null;
		}
		long days = (f > 0 ? new Date().getTime() - date.getTime() : date
				.getTime() - new Date().getTime())
				/ (24 * 60 * 60 * 1000) + 1;
		return days / 1f -1f;
	}
	/**
	 * 获取N个月最后一天
	 * @return
	 */
	public static String getMaxDayOfMonth(int months) {
		// 获取当前时间
		Calendar cal = Calendar.getInstance();
		// 调到上个月
		cal.add(Calendar.MONTH, -months);
		// 得到一个月最最后一天日期(31/30/29/28)
		int MaxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 按你的要求设置时间
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), MaxDay, 23,
				59, 59);
		// 按格式输出
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	/**
	 * 获取N个月前第一天
	 * @return
	 */
	public static String getMinDayOfMonth(int months) {
		// 获取当前时间
		Calendar cal = Calendar.getInstance();
		// 调到上个月
		cal.add(Calendar.MONTH, - months);
		// 得到一个月最最后一天日期(31/30/29/28)
		int MaxDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		// 按你的要求设置时间
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), MaxDay, 00,
				00, 00);
		// 按格式输出
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	/**
	 * 获取几天后日期
	 * @return
	 * @throws ParseException 
	 */
	public static Date addHours(int hours) throws ParseException {
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
        cd.add(Calendar.HOUR, hours);
        Date date=cd.getTime(); 
        return date;
	}
	/**
	 * 获取指定时间几天后的日期
	 * @return
	 * @throws ParseException 
	 */
	public static Date addHours(Date startDate ,int hours) throws ParseException {
		Calendar cd = Calendar.getInstance();
		cd.setTime(startDate);
		cd.add(Calendar.HOUR, hours);
        Date date=cd.getTime(); 
        return date;
	}
	/**
	 * 获取几天后日期
	 * @return
	 * @throws ParseException 
	 */
	public static Date addDays(int days) throws ParseException {
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
        cd.add(Calendar.DATE, days);
        Date date=cd.getTime(); 
        return date;
	}
	/**
	 * 获取指定时间几天后的日期
	 * @return
	 * @throws ParseException 
	 */
	public static Date addDays(Date startDate ,int days) throws ParseException {
		Calendar cd = Calendar.getInstance();
		cd.setTime(startDate);
        cd.add(Calendar.DATE, days);
        Date date=cd.getTime(); 
        return date;
	}
	/**
	 * 获取几月后日期
	 * @return
	 * @throws ParseException 
	 */
	public static Date addMonths(int months) throws ParseException {
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
        cd.add(Calendar.MONTH, months);
        Date date=cd.getTime(); 
        return date;
	}
	/**
	 * 获取指定时间几月后的日期
	 * @return
	 * @throws ParseException 
	 */
	public static Date addMonths(Date startDate ,int months) throws ParseException {
		Calendar cd = Calendar.getInstance();
		cd.setTime(startDate);
        cd.add(Calendar.MONTH, months);
        Date date=cd.getTime(); 
        return date;
	}
	/**
	 * 获取几年后日期
	 * @return
	 * @throws ParseException 
	 */
	public static Date addYears(int years) throws ParseException {
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
        cd.add(Calendar.YEAR, years);
        Date date=cd.getTime(); 
        return date;
	}
	/**
	 * 获取指定时间几年后的日期
	 * @return
	 * @throws ParseException 
	 */
	public static Date addYears(Date startDate ,int years) throws ParseException {
		Calendar cd = Calendar.getInstance();
		cd.setTime(startDate);
        cd.add(Calendar.YEAR, years);
        Date date=cd.getTime(); 
        return date;
	}
	/**
	 * 获取当前时间
	 * @return
	 * @throws ParseException 
	 */
	public static String getCurrDate(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd"); 
		String currDate = sf.format(new Date());
		return currDate;
	 }
	
	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return
	 * @throws ParseException 
	 */
	public static String getCurrTime(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String currDate = sf.format(new Date());
		return currDate;
	 }
	/**
	 * Description：例：今天是2016年5月3日。调用beginOfTheDay(new Date(),1),得到2016-5-04 00:00:00;days可以为负数，结果为相应的前某一天。
	 */
	public static String beginOfTheDay(Date date,int days) {
		String beginTimeStr =getAfterDate(date,days,BEGIN_OF_THE_DAY);
		return beginTimeStr;
	}

	/**
	 * Description：例：今天是2016年5月3日。调用endOfTheDay(new Date(),1),得到2016-5-04 23:59:59;days可以为负数，结果为相应的前某一天。
	 */
	public static String endOfTheDay(Date date,int days) {
		String endTimeStr = getAfterDate(date,days,END_OF_THE_DAY);
		return endTimeStr;
	}
	/**
	 * 日期格式化(Date转换为String)
	 * 
	 * @param _date
	 *            日期
	 * @param patternString
	 *            处理结果日期的显示格式，如："YYYY-MM-DD"
	 * @return
	 */
	public static String getDateString(Date _date, String patternString) {
		String dateString = "";
		if (_date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(patternString);
			dateString = formatter.format(_date);
		}
		return dateString;
	}
	/**
	 * 获取与指定日期相差指定 天数 的日期
	 * 
	 * @param baseDate
	 *            时间字符串，如：2008-12-03 11:00:00
	 * @param dayCount
	 *            向前或向后的天数，向后为正数，向前为负数
	 * @param patternString
	 *            处理结果日期的显示格式，如："YYYY-MM-DD"
	 * @return String 处理后的日期字符
	 */
	public static String getAfterDate(String baseDate, int dayCount, String patternString) {
		int year = Integer.parseInt(baseDate.substring(0, 4));
		int month = Integer.parseInt(baseDate.substring(5, 7));
		int date = Integer.parseInt(baseDate.substring(8, 10));
		Calendar calendar = Calendar.getInstance();
		if (DATETIMESHOWFORMAT.equals(patternString)) {
			int hour = Integer.parseInt(baseDate.substring(11, 13));
			int minute = Integer.parseInt(baseDate.substring(14, 16));
			int second = Integer.parseInt(baseDate.substring(17, 19));
			calendar.set(year, month - 1, date, hour, minute, second);
		} else {
			calendar.set(year, month - 1, date);
		}
		calendar.set(year, month - 1, date);
		calendar.add(Calendar.DATE, dayCount);
		Date _date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(patternString);
		String dateString = formatter.format(_date);
		return dateString;
	}
	/**
	 * 获取与指定日期相差指定 天数 的日期
	 * 
	 * @param baseDate
	 *            时间字符串，如：2008-12-03 11:00:00
	 * @param dayCount
	 *            向前或向后的天数，向后为正数，向前为负数
	 * @param patternString
	 *            处理结果日期的显示格式，如："YYYY-MM-DD"
	 * @return String 处理后的日期字符
	 */
	public static String getAfterDate(Date baseDate, int dayCount, String patternString) {
		String _baseDate = getDateString(baseDate, DATETIMESHOWFORMAT);
		return getAfterDate(_baseDate, dayCount, patternString);
	}
	
	/**
	 * 获取指定时间前后指定分钟的时间
	 * @param startDate
	 * @param minute
	 * @return
	 */
	public static Date addMinute(Date startDate,int minute){
		Calendar cd = Calendar.getInstance();
		cd.setTime(startDate);
        cd.add(Calendar.MINUTE, minute);
        Date date = cd.getTime(); 
        return date;
	}
	/**
     * 校验是否yyyy-MM-dd HH:mm:ss格式
     * @param str
     * @return
     */
    public static boolean isDateTimeReg(String str){
    	String regEx="^\\d{4}[-]([0][1-9]|(1[0-2]))[-]([1-9]|([012]\\d)|(3[01]))([ \\t\\n\\x0B\\f\\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-4]{1}))([:])(([0-5]{1}[0-9]{1}|[6]{1}[0]{1}))([:])((([0-5]{1}[0-9]{1}|[6]{1}[0]{1})))$";
    	return str.matches(regEx);
    }
    
    /**
     * 获取2个日期的月份差
     * @param String
     * @return
     */
    public static int getMonthSpace(String date1, String date2)
    		throws ParseException {

    	int result = 0;

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    	Calendar c1 = Calendar.getInstance();
    	Calendar c2 = Calendar.getInstance();

    	c1.setTime(sdf.parse(date1));
    	c2.setTime(sdf.parse(date2));

    	result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

    	return result == 0 ? 1 : Math.abs(result);

    }
}
