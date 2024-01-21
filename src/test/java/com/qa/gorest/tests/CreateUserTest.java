package com.qa.gorest.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.gorest.base.BaseTest;
import com.qa.gorest.client.RestClient;
import com.qa.gorest.constants.APIConstants;
import com.qa.gorest.constants.APIHttpStatus;
import com.qa.gorest.pojo.User;
import com.qa.gorest.utils.ExcelUtil;
import com.qa.gorest.utils.StringUtils;

import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateUserTest extends BaseTest{

	//All the testclasses are the child of BaseTest class.

	@BeforeMethod
	public void getUserSetup() {
		restClient = new RestClient(prop, baseURI);
	}
	
	//1. Without DataProvider
	@Test
	public void getAllUsersTest() {
		
		//1. POST
		User user = new User("Kartik", StringUtils.getRandomEmailId(), "male", "active");
		
		Integer userId = restClient.post(GOREST_ENDPOINT, "json", user, true, true)
				.then().log().all()
				.assertThat().statusCode(APIHttpStatus.CREATED_201.getCode())
				.extract().path("id");
		
		System.out.println("User id is:" + userId);
		
		//2. GET
		RestClient clientGet = new RestClient(prop, baseURI); //Reason to create a new object is that in the same TC we have another GET call and we can't use the same restClient object otherwise it will append the URLs and might give incorrect results. Two HTTP Requests so better to have 2 restClient objects.
		clientGet.get(GOREST_ENDPOINT+"/"+userId, true, true) //Without "isAuthorizationHeaderAdded" flag in the addAuthorization() method of the RestClient class, If we keep the (boolean includeAuth) as true for two times, then the Authorization header will be added two times, one at the POST call and other time at GET call and the test result will fail giving 404(Bad Request). Hence, keep it false at second time. 
			.then() //Now, we have used the feature flag of isAuthorizationHeaderAdded in the addAuthorization() method, now we can set true for includeAuth.
			.assertThat().statusCode(APIHttpStatus.OK_200.getCode())
			.and()
			.body("id", equalTo(userId));
	}
	
	//2. With DataProvider
	@DataProvider
	public Object[][] getUserTestData() {
		return new Object[][] {
			{"Subodh", "male", "active"},
			{"Seema", "female", "inactive"},
			{"Madhuri", "female", "active"}
		};
	}
	
	@Test(dataProvider = "getUserTestData")
	public void getAllUsersTest(String name, String gender, String status) {
		
		//1. POST
		User user = new User(name, StringUtils.getRandomEmailId(), gender, status);
		
		Integer userId = restClient.post(GOREST_ENDPOINT, "json", user, true, true)
				.then().log().all()
				.assertThat().statusCode(APIHttpStatus.CREATED_201.getCode())
				.extract().path("id");
		
		System.out.println("User id is:" + userId);
		
		//2. GET
		RestClient clientGet = new RestClient(prop, baseURI); //Reason to create a new object is that in the same TC we have another GET call and we can't use the same restClient object otherwise it will append the URLs and might give incorrect results. Two HTTP Requests so better to have 2 restClient objects.
		clientGet.get(GOREST_ENDPOINT+"/"+userId, true, true) //Without "isAuthorizationHeaderAdded" flag in the addAuthorization() method of the RestClient class, If we keep the (boolean includeAuth) as true for two times, then the Authorization header will be added two times, one at the POST call and other time at GET call and the test result will fail giving 404(Bad Request). Hence, keep it false at second time. 
			.then() //Now, we have used the feature flag of isAuthorizationHeaderAdded in the addAuthorization() method, now we can set true for includeAuth.
			.assertThat().statusCode(APIHttpStatus.OK_200.getCode())
			.and()
			.body("id", equalTo(userId));
	}
	
	//3. Excel Sheet DataProvider //Not used much(try to avoid), earlier approach is more used for Dataprovider.

	@DataProvider
	public Object[][] getUserTestSheetData() {
		return ExcelUtil.getTestData(APIConstants.GOREST_USER_SHEET_NAME);
	}

	@Test(dataProvider = "getUserTestSheetData")
	public void getAllUsersTest_UsingExcel(String name, String gender, String status) {

		//1. POST
		User user = new User(name, StringUtils.getRandomEmailId(), gender, status);

		Integer userId = restClient.post(GOREST_ENDPOINT, "json", user, true, true)
				.then().log().all()
				.assertThat().statusCode(APIHttpStatus.CREATED_201.getCode())
				.extract().path("id");

		System.out.println("User id is:" + userId);

		//2. GET
		RestClient clientGet = new RestClient(prop, baseURI); 
		clientGet.get(GOREST_ENDPOINT+"/"+userId, true, true) 
		.then()
		.assertThat().statusCode(APIHttpStatus.OK_200.getCode())
		.and()
		.body("id", equalTo(userId));			
	}
	
}
	
