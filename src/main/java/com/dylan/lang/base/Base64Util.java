package com.dylan.lang.base;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
	// 把普通字符编码成64bit
	public static String StrToBase64(String str) {
		if (str == null){
			return null;
		}
		return Base64.encodeBase64String(str.getBytes());
	}

	// 把64bit编码转化成普通字符串
	public static String Base64ToStr(String str, String charset) throws UnsupportedEncodingException {
		if (str == null){
			return null;
		}
		return new String(Base64.decodeBase64(str), charset);
	}
	
	// 把64bit编码转化成二进制字节数组
	public static byte[] Base64ToBinary(String str) {
		if (str == null){
			return null;
		}
		return Base64.decodeBase64(str);
	}
}
