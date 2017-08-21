package com.m104.utils;

public class Conv {

	public static String toStr(Object sour, String init) {
		String dest;
		try {
			dest =  sour.toString();
			if (dest == null)
				dest = init;
		} catch (Exception e) {
			dest = init;
		}
		return dest;
	}

	public static int toInt(Object sour, int init) {
		int dest;
		try {
			dest = Integer.parseInt((sour.toString()));
		} catch (Exception e) {
			dest = init;
		}
		return dest;
	}
 	

}
