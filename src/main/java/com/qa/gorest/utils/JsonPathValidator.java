package com.qa.gorest.utils;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.qa.gorest.frameworkexceptions.APIFrameworkException;

import io.restassured.response.Response;

public class JsonPathValidator {

	private String getJsonResponseAsString(Response response) {
        return response.getBody().asString();
	}
	
	//Creating various methods based on the different return types.
	//1.
	public <T> T read(Response response, String jsonPath) { //Either we can write "Object" instead of void as the return type can be anything or we can write like "<T> T" which means it can give you any type of return. T means Type. and inside diamond bracket means generics.
		String jsonResponse = getJsonResponseAsString(response);
		try {
        	return JsonPath.read(jsonResponse, jsonPath);
        }
        catch(PathNotFoundException e) {
        	e.printStackTrace();
        	throw new APIFrameworkException(jsonPath + "is not found...");
        }
	}
	
	//2.
	public <T> List<T> readList(Response response, String jsonPath) {
		String jsonResponse =  getJsonResponseAsString(response);
        try {
        	return JsonPath.read(jsonResponse, jsonPath);
        }
        catch(PathNotFoundException e) {
        	e.printStackTrace();
        	throw new APIFrameworkException(jsonPath + "is not found...");
        }
	}
	
	//3.
	public <T> List<Map<String, T>> readListOfMaps(Response response, String jsonPath) {
		String jsonResponse =  getJsonResponseAsString(response);
        try {
        	return JsonPath.read(jsonResponse, jsonPath);
        }
        catch(PathNotFoundException e) {
        	e.printStackTrace();
        	throw new APIFrameworkException(jsonPath + "is not found...");
        }
	}
}
