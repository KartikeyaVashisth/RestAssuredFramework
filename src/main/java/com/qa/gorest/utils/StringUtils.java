package com.qa.gorest.utils;

public class StringUtils {

	public static String getRandomEmailId() {

		return "apiautomation"+System.currentTimeMillis()+"@yahoo.com";
		//return "apiautomation"+ UUID.randomUUID()+"@mail.com"; Another way to create random numbers but above one is better actually as this will be generated with -(hyphens)
	}
}
