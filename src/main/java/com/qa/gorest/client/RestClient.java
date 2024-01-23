package com.qa.gorest.client;

import static io.restassured.RestAssured.*;

import java.util.Map;
import java.util.Properties;

import com.qa.gorest.constants.APIHttpStatus;
import com.qa.gorest.frameworkexceptions.APIFrameworkException;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestClient {

//	private static final String BASE_URI = "https://gorest.co.in"; //Note: All constants variable names should be in capital letters.
//	private static final String BEARER_TOKEN = "e4b8e1f593dc4a731a153c5ec8cc9b8bbb583ae964ce650a741113091b4e2ac6";
	//Commenting the above two lines as they are hardcoded. Instead now passing the class variable baseURI in the methods created below in the class.
	
	private static RequestSpecBuilder specBuilder; 
	
	private Properties prop;
	private String baseURI;
	
	private boolean isAuthorizationHeaderAdded = false;
	
//	static { //static block will be executed even before the object of this class is created. Before the main() method in Java, static block is executed.
//		specBuilder = new RequestSpecBuilder();
//	} //commenting the static block and creating a RestClient constructor. We can remove this static block actually. Keeping it commented as of now.
	
	public RestClient(Properties prop, String baseURI) { //This constructor will be called each time the object of this class will be created. Passing Properties file reference so that we can read the properties. Also, need BaseURI
		specBuilder = new RequestSpecBuilder();
		this.prop = prop; //this.classVariable = local variable
		this.baseURI = baseURI;	
	}
	
	public void addAuthorization() {
		if(!isAuthorizationHeaderAdded) { //This feature flag is necessary to avoid two times authorization header getting added issue while running TCs.
		specBuilder.addHeader("Authorization", "Bearer " + prop.getProperty("token"));
		isAuthorizationHeaderAdded = true;
		}
	}
	
	private void setRequestContentType(String contentType) {
		switch (contentType.toLowerCase()) { //json-JSON-Json, because user can supply any of the string, hence converting it to lower case to avoid any issue.
		case "json":
			specBuilder.setContentType(ContentType.JSON);
			break;
		case "xml":
			specBuilder.setContentType(ContentType.XML);
			break;
		case "text":
			specBuilder.setContentType(ContentType.TEXT);
			break;	
		case "multipart":
			specBuilder.setContentType(ContentType.MULTIPART);
			break;	

		default:
			System.out.println("plz pass the right content type....");
			throw new APIFrameworkException("INVALIDCONTENTTYPE");
		}
	}
	 
	//Below are the different RequestSpecification's that we have created like if we just want to set baseURI, next if we want headers also , next if we want with queryParams as well, next for the request body which will be used in various GET/POST/PUT/PATCH/DELETE calls.
	private RequestSpecification createRequestSpec(boolean includeAuth) { //Basic request with only Base URI and authentication header
		
		specBuilder.setBaseUri(baseURI);
		if(includeAuth) {
			addAuthorization();
		}
		return specBuilder.build();
	}
	
	private RequestSpecification createRequestSpec(Map<String,String> headersMap,  boolean includeAuth) { //Overloaded method: If we want to pass multiple headers. We can supply in form of Hashmap.
		
		specBuilder.setBaseUri(baseURI);
		if(includeAuth) {
			addAuthorization();
		}
		if(headersMap!=null) {
			specBuilder.addHeaders(headersMap);
		}
		return specBuilder.build();
	}
	
	private RequestSpecification createRequestSpec(Map<String,String> headersMap, Map<String,Object> queryParams, boolean includeAuth) { //Overloaded method: If we want to add query parameters in GET request call.
		
		specBuilder.setBaseUri(baseURI);
		if(includeAuth) {
			addAuthorization();
		}
		if(headersMap!=null) {
			specBuilder.addHeaders(headersMap);
		}
		if(queryParams!=null) {
			specBuilder.addQueryParams(queryParams);
		}
		return specBuilder.build();
	}
	
	//Below is let's say we are writing for POST call where we don't need query Parameters but we need to supply body() and for body() we will supply in form of POJO.
	//So therefore, we will pass Object type so that it can hold any kind of java class object and also we need to mention what kind of POJO, so we need to pass ContentTypes also.
	private RequestSpecification createRequestSpec(Object requestBody, String contentType, boolean includeAuth) { //Overloaded method: 
		
		specBuilder.setBaseUri(baseURI);
		if(includeAuth) {
			addAuthorization();
		}
		setRequestContentType(contentType); //ContentType is needed for POST/PUT/PATCH calls as we need to pass the body and so we need to mention the content type for it whether json/xml/text etc.
		
		if(requestBody!=null) {
			specBuilder.setBody(requestBody);
		}
		return specBuilder.build();
	}
	
	private RequestSpecification createRequestSpec(Object requestBody, String contentType, Map<String,String> headersMap, boolean includeAuth) { //Overloaded method: 
		
		specBuilder.setBaseUri(baseURI);
		if(includeAuth) {
			addAuthorization();
		}
		setRequestContentType(contentType);
		if(requestBody!=null) {
			specBuilder.setBody(requestBody);
		}
		
		if(headersMap!=null) {
			specBuilder.addHeaders(headersMap);
		}
		
		return specBuilder.build();
	}
	
	//Http method utils
	//Adding a boolean log in method argument is a standard practice if we want logs[log().all()] or not. Also, we are not adding log=true/false to the config.properties file because then it will be applicable to all the Testcases. So better to maintain it at the method level.
	public Response get(String serviceURL, boolean includeAuth, boolean log) {
		if(log) {
			return RestAssured.given().spec(createRequestSpec(includeAuth)).log().all().when().get(serviceURL); //Another way can be to directly pass the spec in the given() like given(createRequestSpec())
		}
		return RestAssured.given().spec(createRequestSpec(includeAuth)).when().get(serviceURL); //We will not write then() here because Assertion is testng responsibility in Testclass and not the utility class responsibility.
	}
	//Now, we don't want to give access of createRequestSpec(includeAuth) method to the user, so we can make them PRIVATE in nature and we'll use them internally in this class.
	
	public Response get(String serviceURL, Map<String,String> headersMap, boolean includeAuth, boolean log) {
		if(log) {
			return RestAssured.given().spec(createRequestSpec(headersMap, includeAuth)).log().all().when().get(serviceURL);
		}
		return RestAssured.given().spec(createRequestSpec(headersMap, includeAuth)).when().get(serviceURL);
	}	

	public Response get(String serviceUrl, Map<String, String> headersMap, Map<String, Object> queryParams, boolean includeAuth, boolean log) {
		
		if(log) {
			return RestAssured.given().spec(createRequestSpec(headersMap, queryParams, includeAuth)).log().all().when().get(serviceUrl);
		}
		return RestAssured.given(createRequestSpec(headersMap, queryParams, includeAuth)).when().get(serviceUrl);
	}
	
	//POST
	public Response post(String serviceUrl, String ContentType, Object requestBody, boolean includeAuth, boolean log ) { //POST call without headers included
		if(log) {
			return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, includeAuth)).log().all().when().post(serviceUrl);
		}
		return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, includeAuth)).when().post(serviceUrl);	
	}
	
	public Response post(String serviceUrl, String ContentType, Object requestBody, Map<String, String> headersMap, boolean includeAuth, boolean log ) { //POST call with headers included
		if(log) {
			return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, headersMap, includeAuth)).log().all().when().post(serviceUrl);
		}
		return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, headersMap, includeAuth)).when().post(serviceUrl);	
	}
	
	//PUT : Same two methods as POST, only difference is we have .put() method
	public Response put(String serviceUrl, String ContentType, Object requestBody, boolean includeAuth, boolean log ) { //PUT call without headers included
		if(log) {
			return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, includeAuth)).log().all().when().put(serviceUrl);
		}
		return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, includeAuth)).when().put(serviceUrl);	
	}
	
	public Response put(String serviceUrl, String ContentType, Object requestBody, Map<String, String> headersMap, boolean includeAuth, boolean log ) { //PUT call with headers included
		if(log) {
			return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, headersMap, includeAuth)).log().all().when().put(serviceUrl);
		}
		return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, headersMap, includeAuth)).when().put(serviceUrl);	
	}
	
	//PATCH: Same two methods as POST and PUT, only difference is we have .patch() method
	public Response patch(String serviceUrl, String ContentType, Object requestBody, boolean includeAuth, boolean log ) { //PATCH call without headers included
		if(log) {
			return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, includeAuth)).log().all().when().patch(serviceUrl);
		}
		return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, includeAuth)).when().patch(serviceUrl);	
	}
	
	public Response patch(String serviceUrl, String ContentType, Object requestBody, Map<String, String> headersMap, boolean includeAuth, boolean log ) { //PATCH call with headers included
		if(log) {
			return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, headersMap, includeAuth)).log().all().when().patch(serviceUrl);
		}
		return RestAssured.given().spec(createRequestSpec(requestBody, ContentType, headersMap, includeAuth)).when().patch(serviceUrl);	
	}
	
	//DELETE
	public Response delete(String serviceUrl, boolean includeAuth, boolean log) {
		if(log) {
			return RestAssured.given().spec(createRequestSpec(includeAuth)).log().all().when().delete(serviceUrl);
		}
		return RestAssured.given().spec(createRequestSpec(includeAuth)).when().delete(serviceUrl);
	}
	 
	//For OAuth 2.0	
	public String getAccessToken(String serviceURL, String grantType, String clientId, String clientSecret  ) {
		//1. POST - get the access token
				RestAssured.baseURI = "https://test.api.amadeus.com";
				
				String accessToken = given().log().all()
					.contentType(ContentType.URLENC)
					.formParam("grant_type", grantType)
					.formParam("client_id", clientId)
					.formParam("client_secret", clientSecret)
				.when()
					.post(serviceURL)
				.then().log().all()
					.assertThat()
						.statusCode(APIHttpStatus.OK_200.getCode())
						.extract().path("access_token");
					
				System.out.println("access token: " + accessToken);
			return 	accessToken;
				
	}
}
