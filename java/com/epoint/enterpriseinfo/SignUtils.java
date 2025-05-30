package com.epoint.enterpriseinfo;


import java.security.MessageDigest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SignUtils {
	
	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
		'9', 'A', 'B', 'C', 'D', 'E', 'F' };

	protected static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	protected static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	protected static String md5(String content) {
		if (isEmpty(content) || isBlank(content)) {
			return "";
		}
		try {
			byte[] btInput = content.getBytes("UTF-8");
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param source
	 * @return
	 */
	protected static Map<String, Object> sortMapByKey(Map<String, Object> source) {
		if (source == null || source.isEmpty()) {
			return new HashMap<String, Object>();
		}
		Map<String, Object> dest = new TreeMap<String, Object>(
				new Comparator<String>() {
					@Override
					public int compare(String key1, String key2) {
						return key1.compareTo(key2);
					}
				});
		dest.putAll(source);
		return dest;
	}

	/**
	 * @param values
	 * @param key
	 * @return
	 */
	public static String getSign(Map<String, Object> values, String key) {
		Map<String, Object> sorts = sortMapByKey(values);
		StringBuilder content = new StringBuilder();
		for (String k : sorts.keySet()) {
			String value = sorts.get(k).toString();
			if (value != null && !"".equals(value)) {
				content.append(k).append("=").append(value).append("&");
			}
		}
		content.append("key=").append(key);
		return md5(content.toString()).toUpperCase();
	}

}
