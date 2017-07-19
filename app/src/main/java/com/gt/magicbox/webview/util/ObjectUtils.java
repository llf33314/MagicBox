package com.gt.magicbox.webview.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ObjectUtils {

	/**
	 * 批量判断是否有空值
	 * @param objs 多个参数
	 * @return
	 */
	public static boolean isEmpty(Object... objs){
		for(Object obj : objs){
			if(isEmpty(obj)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 批量判断是否都不为空
	 * @param objs 多个参数
	 * @return
	 */
	public static boolean isNotEmpty(Object... objs){
		for(Object obj : objs){
			if(isEmpty(obj)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 批量判断是否有其中一个不为空
	 * @param objs 多个参数
	 * @return
	 */
	public static boolean isNotEmptyOne(Object... objs){
		for(Object obj : objs){
			if(isNotEmpty(obj)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		boolean b = false;
		try {
			if (obj == null || "".equals(obj)) {
				b = true;
			} else {
				b = false;
			}
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 判断对象是否不为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		boolean b = false;
		try {
			if (obj == null || "".equals(obj)) {
				b = false;
			} else {
				b = true;
			}
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 转Integer
	 * 
	 * @param obj
	 */
	public static Integer toInteger(Object obj) {
		try {
			if (!isEmpty(obj)) {
				return Integer.parseInt(obj.toString());
			} else {
				throw new Exception("对象为空，转换失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 转String
	 * 
	 * @param obj
	 */
	public static String toString(Object obj) {
		try {
			if (!isEmpty(obj)) {
				return obj.toString();
			} else {
				throw new Exception("对象为空，转换失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 转Double
	 * 
	 * @param obj
	 */
	public static Double toDouble(Object obj) {
		try {
			if (!isEmpty(obj)) {
				return Double.parseDouble(obj.toString());
			} else {
				throw new Exception("对象为空，转换失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 校验是否是double数据
	 * 
	 */
	public static boolean isDouble(Object obj) {
		try {
			Double.parseDouble(obj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 移除map中为空的项
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> removeEmptyKey(Map<String, Object> map) {
		List<String> keyLs = new ArrayList<>();
		for (String key : map.keySet()) {
			if (isEmpty(map.get(key))) {
				keyLs.add(key);
			}
		}
		for (String string : keyLs) {
			map.remove(string);
		}
		return map;
	}

	/**
	 * 是否为正整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断手机营业商
	 * 
	 * @param phone_number
	 * @return flag 运营商 3：电信 1 移动 2 联通 0 未知号码
	 */
	public static int matchesPhoneNumber(String phone_number) {
		String cm = "^((13[4-9])|(147)|(15[0-2,7-9])|(18[2-3,7-8]))\\d{8}$";
		String cu = "^((13[0-2])|(145)|(15[5-6])|(186))\\d{8}$";
		String ct = "^((133)|(153)|(18[0,1,9]))\\d{8}$";

		int flag = 0;
		if (phone_number.matches(cm)) {
			flag = 1;
		} else if (phone_number.matches(cu)) {
			flag = 2;
		} else if (phone_number.matches(ct)) {
			flag = 3;
		} else {
			flag = 4;
		}
		return flag;
	}

	/**
	 * Object转成String
	 * 
	 * @param obj
	 * @return
	 */
	public static String getStr(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 判断是否是日期格式
	 * @param str
	 * @return
	 */
	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			format.setLenient(false);
			format.parse(str);
		} catch (Exception e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}
	
	/**
	 * 格式化字符串
	 * @param format
	 * @param args
	 * @return
	 */
	public static String format(String format, Object... args){
		String str=null;
		str=String.format(format, args);
		return str;
	}
	/**
	 * 两数相加 
	 * @return
	 */
	public static Double add(Double number1,Object number2){
		Double number = toDouble(number2);
		if(isNotEmpty(number2)){
			return number1+number;
		}else{
			return number1;
		}
	}

	/**
	 * map转String
	 * @param map
	 * @return
     */
	public static String map2String(Map<?, ?> map){
		StringBuilder result = new StringBuilder();
		for (Object key : map.keySet()){
			result.append(key + " : " + map.get(key) + "\r\n");
		}
		return result.toString();
	}
}
