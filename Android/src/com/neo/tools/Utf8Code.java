package com.neo.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.net.Uri;

public class Utf8Code {
	public static String utf8Encode(String string) {
		String result = "";
		try {
			result = URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String utf8Decode(String string) {
		String result = "";
		try {
			result = URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}