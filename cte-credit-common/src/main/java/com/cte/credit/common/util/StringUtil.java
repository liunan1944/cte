package com.cte.credit.common.util;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import sun.misc.BASE64Encoder;
/**
* @Title: 字符串工具类
* @Package com.bill99.ifs.crs.util 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xiaocl chenglin.xiao@99bill.com   
* @date 2015年5月20日 下午4:18:50 
* @version V1.0
 */
public class StringUtil {

	public static final char[] ALPHEBIC = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	public static final char[] ALPHEBIC_AND_NUMBER_UPPER = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			// 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			// 'n', 'o', 'p', 'q', 'r', 's',
			// 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	public static final char[] ALPHEBIC_AND_NUMBER_LOWER = new char[] {
			// 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			// 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			// 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	public static final char[] ALPHEBIC_AND_NUMBER = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	public static final char[] NUMBER = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	public static final char[] SQL2K5_RESERVE_CHARS = new char[] { '%', '_', '[' };

	public static final int LIKETYPE_ALL = 0;
	public static final int LIKETYPE_PRE = 1;
	public static final int LIKETYPE_SUF = 2;

	public static final int FILE_NAME_MAX_LENGTH_BYTE = 150;

	public static String getAlphaOrder(int order) {
		StringBuffer sb = new StringBuffer();
		int size = ALPHEBIC.length;
		int i = order - 1;
		if (i < 0)
			throw new IllegalArgumentException();
		while (i >= size) {
			int j = i % size;
			i = i / size - 1;
			sb.insert(0, ALPHEBIC[j]);
		}
		sb.insert(0, ALPHEBIC[i]);
		return sb.toString();
	}

	public static String upcaseFirstLetter(String str) {
		if (str == null) {
			return null;
		}
		String firstChar = str.substring(0, 1);
		return firstChar.toUpperCase() + str.substring(1, str.length());
	}

	public static String lowerFirstLetter(String str) {
		if (str == null) {
			return null;
		}
		String firstChar = str.substring(0, 1);
		return firstChar.toLowerCase() + str.substring(1, str.length());
	}

	public static String[] getFormatedIncreasedString(String firstString, int stepNum, int count) {
		if (firstString == null) {
			return null;
		}
		Pattern p = Pattern.compile("([0-9]+)$");
		Matcher m = p.matcher(firstString);

		if (m.find()) {

			int dIndex = m.start();
			String prefix = firstString.substring(0, dIndex);
			String suffix = firstString.substring(dIndex);
			/*
			 * int index = suffix.length(); if (suffix.endsWith("0")) { index =
			 * suffix.length() - 1; if (suffix.lastIndexOf("0", index) > 0) {
			 * prefix += suffix.substring(0, suffix.lastIndexOf("0", index));
			 * suffix = suffix.substring(suffix.lastIndexOf("0", index)); } }
			 * else { prefix += suffix.substring(0, suffix.lastIndexOf("0",
			 * index) + 1); suffix = suffix.substring(suffix.lastIndexOf("0",
			 * index) + 1); }
			 */

			int suffixLenth = suffix.length();

			long in = Long.parseLong(suffix);
			String[] value = new String[count];
			value[0] = firstString;
			for (int i = 1; i < count; i++) {
				long lastNum = in + stepNum * i;
				String last = Long.toString(lastNum);
				if (last.length() < suffixLenth) {
					last = formatLong(lastNum, suffixLenth);
				}
				value[i] = prefix + last;

			}
			return value;
		}
		return null;
	}

	public static String formatLong(long num, int length) {
		String number = Long.toString(num);
		while (number.length() < length) {
			number = "0" + number;
		}
		return number;
	}

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat dateTimeFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
		dateTimeFormat.applyPattern(pattern);
		return dateTimeFormat.format(date);
	}

	public static String getRandomUpperStr(int strLength) {
		StringBuffer sb = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < strLength; i++) {
			int j = (sr.nextInt() * ALPHEBIC_AND_NUMBER_UPPER.length);
			sb.append(ALPHEBIC_AND_NUMBER_UPPER[j]);
		}
		return sb.toString();
	}

	public static String getRandomLowerStr(int strLength) {
		StringBuffer sb = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < strLength; i++) {
			int j = (sr.nextInt() * ALPHEBIC_AND_NUMBER_LOWER.length);
			sb.append(ALPHEBIC_AND_NUMBER_LOWER[j]);
		}
		return sb.toString();
	}

	public static String getRandomStrAll(int strLength) {
		StringBuffer sb = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < strLength; i++) {
			int j = (sr.nextInt() * ALPHEBIC_AND_NUMBER.length);
			sb.append(ALPHEBIC_AND_NUMBER[j]);
		}
		return sb.toString();
	}

	public static String getRandomStrNum(int strLength) {
		StringBuffer sb = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < strLength; i++) {
			int j = (sr.nextInt() * NUMBER.length);
			sb.append(NUMBER[j]);
		}
		return sb.toString();
	}
	public static String getRandomNumStr(int strLength) {
		StringBuffer sb = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < strLength; i++) {
			int j = Math.abs((sr.nextInt() * NUMBER.length)%(strLength+1));
			sb.append(NUMBER[j]);
		}
		return sb.toString();
	}
	

	public static String encodeStrForSql2k5(String str) {
		if (str == null)
			return null;
		Set<Character> cs = new HashSet<Character>();
		for (char c : SQL2K5_RESERVE_CHARS)
			cs.add(c);
		StringBuffer sb = new StringBuffer();
		for (Character c : str.toCharArray())
			if (cs.contains(c)) {
				sb.append("[");
				sb.append(c);
				sb.append("]");
			} else
				sb.append(c);
		return sb.toString();
	}

	public static String getLickStrForSql2k5(String param) {
		return getLikeStr(encodeStrForSql2k5(param), LIKETYPE_ALL);
	}

	public static String getLikeStr(String param) {
		return getLikeStr(param, LIKETYPE_ALL);
	}

	public static String getLikeStr(String param, int type) {
		if (param == null)
			throw new IllegalArgumentException("Param can not be Null");
		if (type == LIKETYPE_ALL)
			return "%" + param + "%";
		else if (type == LIKETYPE_PRE)
			return "%" + param;
		else if (type == LIKETYPE_SUF)
			return param + "%";
		else
			throw new IllegalArgumentException("Like Type Error");
	}

	public static String getSuffix(String value, String delim) {
		int index = value.lastIndexOf(delim);
		if (index == -1)
			return "";
		return value.substring(index);
	}

	public static String getPrefix(String value, String delim) {
		int index = value.lastIndexOf(delim);
		if (index == -1)
			return value;
		return value.substring(0, index);
	}

	public static String convertArrayToString(String[] arr, String seperator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(seperator + arr[i]);
		}
		if (sb.length() > 0)
			sb.deleteCharAt(0);
		return sb.toString();
	}

	public static String displayArMonth(Integer arMonth) {
		StringBuffer sb = new StringBuffer();
		int year = arMonth / 12;
		int month = arMonth % 12 + 1;
		sb.append(year + "." + StringUtil.formatLong(month, 2));
		return sb.toString();
	}

	public static String nullToStr(String str) {
		return str == null ? "" : str;
	}

	public static String toStringExcludeNull(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}

	public static boolean isEmpty(Object str) {
		return str == null || str.toString().trim().equals("");
	}

	/**
	 * Repeat a string n times to form a new string.
	 * 
	 * @param str
	 *            String to repeat
	 * @param repeat
	 *            int number of times to repeat
	 * @return String with repeated string
	 * @throws NegativeArraySizeException
	 *             if repeat < 0
	 * @throws NullPointerException
	 *             if str is null
	 */
	public static String repeat(String str, int repeat) {
		StringBuffer buffer = new StringBuffer(repeat * str.length());
		for (int i = 0; i < repeat; i++) {
			buffer.append(str);
		}
		return buffer.toString();
	}

	/**
	 * Right pad a String with spaces. Pad to a size of n.
	 * 
	 * @param str
	 *            String to repeat
	 * @param size
	 *            int number of times to repeat
	 * @return right padded String
	 * @throws NullPointerException
	 *             if str is null
	 */
	public static String rightPad(String str, int size) {
		return rightPad(str, size, " ");
	}

	/**
	 * Right pad a String with a specified string. Pad to a size of n.
	 * 
	 * @param str
	 *            String to pad out
	 * @param size
	 *            int size to pad to
	 * @param delim
	 *            String to pad with
	 * @return right padded String
	 * @throws NullPointerException
	 *             if str or delim is null
	 * @throws ArithmeticException
	 *             if delim is the empty string
	 */
	public static String rightPad(String str, int size, String delim) {
		size = (size - str.length()) / delim.length();
		if (size > 0) {
			str += repeat(delim, size);
		}
		return str;
	}

	/**
	 * Left pad a String with spaces. Pad to a size of n.
	 * 
	 * @param str
	 *            String to pad out
	 * @param size
	 *            int size to pad to
	 * @return left padded String
	 * @throws NullPointerException
	 *             if str or delim is null
	 */
	public static String leftPad(String str, int size) {
		return leftPad(str, size, " ");
	}

	/**
	 * Left pad a String with a specified string. Pad to a size of n.
	 * 
	 * @param str
	 *            String to pad out
	 * @param size
	 *            int size to pad to
	 * @param delim
	 *            String to pad with
	 * @return left padded String
	 * @throws NullPointerException
	 *             if str or delim is null
	 * @throws ArithmeticException
	 *             if delim is the empty string
	 */
	public static String leftPad(String str, int size, String delim) {
		size = (size - str.length()) / delim.length();
		if (size > 0) {
			str = repeat(delim, size) + str;
		}
		return str;
	}

	/**
	 * Remove whitespace from the front and back of a String.
	 * 
	 * @param str
	 *            the string to remove whitespace from
	 * @return the stripped string
	 */
	public static String strip(String str) {
		return strip(str, null);
	}

	/**
	 * Remove a specified String from the front and back of a String. If
	 * Whitespace is wanted to be removed, used the strip(String) method.
	 * 
	 * @param str
	 *            the string to remove a string from
	 * @param delim
	 *            the string to remove at start and end
	 * @return the stripped string
	 */
	public static String strip(String str, String delim) {
		str = stripStart(str, delim);
		return stripEnd(str, delim);
	}

	/**
	 * Strip whitespace from the front and back of every string in the array.
	 * 
	 * @param strs
	 *            the strings to remove whitespace from
	 * @return the stripped strings
	 */
	public static String[] stripAll(String[] strs) {
		return stripAll(strs, null);
	}

	/**
	 * Strip the specified delimiter from the front and back of every String in
	 * the array.
	 * 
	 * @param strs
	 *            the strings to remove a string from
	 * @param delimiter
	 *            the string to remove at start and end
	 * @return the stripped strings
	 */
	public static String[] stripAll(String[] strs, String delimiter) {
		if ((strs == null) || (strs.length == 0)) {
			return strs;
		}
		int sz = strs.length;
		String[] newArr = new String[sz];
		for (int i = 0; i < sz; i++) {
			newArr[i] = strip(strs[i], delimiter);
		}
		return newArr;
	}

	/**
	 * Strip any of a supplied string from the end of a String.. If the strip
	 * string is null, whitespace is stripped.
	 * 
	 * @param str
	 *            the string to remove characters from
	 * @param strip
	 *            the string to remove
	 * @return the stripped string
	 */
	public static String stripEnd(String str, String strip) {
		if (str == null) {
			return null;
		}
		int end = str.length();

		if (strip == null) {
			while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else {
			while ((end != 0) && (strip.indexOf(str.charAt(end - 1)) != -1)) {
				end--;
			}
		}
		return str.substring(0, end);
	}

	/**
	 * Strip any of a supplied string from the start of a String. If the strip
	 * string is null, whitespace is stripped.
	 * 
	 * @param str
	 *            the string to remove characters from
	 * @param strip
	 *            the string to remove
	 * @return the stripped string
	 */
	public static String stripStart(String str, String strip) {
		if (str == null) {
			return null;
		}

		int start = 0;

		int sz = str.length();

		if (strip == null) {
			while ((start != sz) && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		} else {
			while ((start != sz) && (strip.indexOf(str.charAt(start)) != -1)) {
				start++;
			}
		}
		return str.substring(start);
	}

	/**
	 * Splits the provided text into a list, using whitespace as the separator.
	 * The separator is not included in the returned String array.
	 * 
	 * @param str
	 *            the string to parse
	 * @return an array of parsed Strings
	 */
	public static String[] split(String str) {
		return split(str, null, -1);
	}

	/**
	 * @see #split(String, String, int)
	 */
	public static String[] split(String text, String separator) {
		return split(text, separator, -1);
	}

	/**
	 * Splits the provided text into a list, based on a given separator. The
	 * separator is not included in the returned String array. The maximum
	 * number of splits to perfom can be controlled. A null separator will cause
	 * parsing to be on whitespace.
	 * 
	 * <p>
	 * This is useful for quickly splitting a string directly into an array of
	 * tokens, instead of an enumeration of tokens (as
	 * <code>StringTokenizer</code> does).
	 * 
	 * @param str
	 *            The string to parse.
	 * @param separator
	 *            Characters used as the delimiters. If <code>null</code>,
	 *            splits on whitespace.
	 * @param max
	 *            The maximum number of elements to include in the list. A zero
	 *            or negative value implies no limit.
	 * @return an array of parsed Strings
	 */
	public static String[] split(String str, String separator, int max) {
		StringTokenizer tok = null;
		if (separator == null) {
			// Null separator means we're using StringTokenizer's default
			// delimiter, which comprises all whitespace characters.
			tok = new StringTokenizer(str);
		} else {
			tok = new StringTokenizer(str, separator);
		}

		int listSize = tok.countTokens();
		if (max > 0 && listSize > max) {
			listSize = max;
		}

		String[] list = new String[listSize];
		int i = 0;
		int lastTokenBegin = 0;
		int lastTokenEnd = 0;
		while (tok.hasMoreTokens()) {
			if (max > 0 && i == listSize - 1) {
				// In the situation where we hit the max yet have
				// tokens left over in our input, the last list
				// element gets all remaining text.
				String endToken = tok.nextToken();
				lastTokenBegin = str.indexOf(endToken, lastTokenEnd);
				list[i] = str.substring(lastTokenBegin);
				break;
			} else {
				list[i] = tok.nextToken();
				lastTokenBegin = str.indexOf(list[i], lastTokenEnd);
				lastTokenEnd = lastTokenBegin + list[i].length();
			}
			i++;
		}
		return list;
	}

	/**
	 * 比较两个字符串是否相等。
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isEquals(String str1, String str2) {
		if (isEmpty(str1)) {
			return isEmpty(str2);
		} else {
			return str1.equals(str2);
		}
	}

	/**
	 * 检查指定的字符串列表是否不为空。
	 * 
	 * @param values
	 *            字符串列表
	 * @return true/false
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	public static String removeLastSplit(String oldstr, String split, int n) {
		int indx = oldstr.length();

		for (int i = 0; i < n; i++) {
			indx = StringUtils.lastIndexOf(oldstr, "/", indx - 1);
			if (indx == 0) {
				return StringUtils.EMPTY;
			}
		}
		String tars = StringUtils.substring(oldstr, 0, indx);
		return tars;
	}
	
	/**
	 * 产出随机交易序列号
	 * @return 交易序列号
	 */
	public synchronized static String getRandomNo() {
		String currentTime = DateUtil.getSimpleDate(new Date(),
				"yyyyMMddHHmmssSSS");
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			sb.append(chars.charAt(RandomUtils.nextInt(26)));
		}
		return currentTime + sb.toString();
	}
	/**
	 * 生成指定长度的字符串
	 * 建议获取30位以上长度
	 * @param length
	 * @return
	 */
	public static String getRequstId(int length){
		
		if (length < 1) {
			return null;
		}
		
		StringBuffer bf = new StringBuffer();		
		Random strGen = new Random();
		char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
				.toCharArray();		
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[strGen.nextInt(61)];
		}
		
		bf.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())).append(new String(randBuffer));
		String enResu = new BASE64Encoder().encode(bf.toString().getBytes());
		
		if(enResu != null && enResu.length()>length){
			return enResu.substring(0, length);
		}else{
			return new String(randBuffer);
		}
	}
	
	/**
	 * 判断一个字符串是不是正数（包括小数）
	 * @param str
	 * @return
	 */
	public static boolean isPositiveNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]+.*[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一个字符串是不是数字（包含小数）
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]+.*[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一个字符串是不是数字（整数）
	 * @param str
	 * @return
	 */
	public static boolean isIntNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断一个字符串是不是正整数
	 * @param str
	 * @return
	 */
	public static boolean isPositiveInt(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一组字符串数组是否都符合最大长度，都符合返回true
	 * @param strs
	 * @param lens
	 * @return
	 */
	public static boolean isMaxLenVaild(String[] strs,int[] lens){
		if(strs.length!=lens.length) return false;
		for (int i = 0; i < lens.length; i++) {
			if(strs[i].length()>lens[i]){
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断某个字符串是否和可变字符串数组中其中一个相等
	 * @param str
	 * @param strs
	 * @return
	 */
	public static boolean isStrInStrs(String str,String... strs){
		for (String item : strs) {
			if(item.equals(str))
				return true;
		}
		return false;
	}
	/**
	 * 判断某个字符串是否为身份证号码
	 * @param str
	 * @return boolean
	 */
    public static boolean isCardNo(String str)
    {
    	String resx = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
    	return Pattern.matches(resx, str);
    }
    /**
     * 校验是否id格式(数字+字母+_任意组合)
     * @param param
     * @return
     */
    public static boolean isIdReg(String param){
    	String regEx="^[0-9a-zA-Z_-]+$";
    	return Pattern.matches(regEx, param);
    }
    /**
     * 校验是否包含中文和全角字符
     * @param param
     * @return
     */
    public static boolean isContainsChinese(String str) {
        Pattern p = Pattern.compile("[^\\x00-\\xff]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    /**
     * 校验是否email格式
     * @param param
     * @return
     */
    public static boolean isEmailReg(String param){
    	try{
    		String regEx="[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        	return Pattern.matches(regEx, param);
    	}catch(Exception e){
    		return false;
    	}
    	
    }
    /** 
     * 日期格式校验
     * @param str 日期字符串,dateFormat 待校验的日期格式
     * @return 校验码 
     */ 
    public static boolean isValidDate(String str,String dateFormat) {
        boolean convertSuccess=true;
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            format.setLenient(false);
            format.parse(str);
         } catch (ParseException e) {
            convertSuccess=false;
         } 
        return convertSuccess;
    }
    
    /** 
     * 转换对象为字符串，为null转换为空字符串
     * @param obj
     * @return String 
     */ 
	public static String stringNoNull(Object obj) {
		if (obj == null)
			return "";
		return obj.toString();
	}
}
