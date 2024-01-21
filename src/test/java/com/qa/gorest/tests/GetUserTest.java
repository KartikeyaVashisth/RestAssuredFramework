package com.qa.gorest.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.gorest.base.BaseTest;
import com.qa.gorest.client.RestClient;
import com.qa.gorest.constants.APIHttpStatus;

import static org.hamcrest.Matchers.*;

public class GetUserTest extends BaseTest{

//	RestClient restClient; //Since we have inherited the restClient from the BaseTest(Parent) class, so we can remove this line as it is separate variable of the class, no need to declare it.
	//To run this Test class, we need to run it from the testng_regression.xml file as without it how will it get the baseURI. 
	//Now, the baseURI will be given to the setUp() method which is under @BeforeTest annotation in the BaseTest which will be further given to the constructor of RestClient.
	//Once the constructor is given with the values of prop and baseURI, all the respective class variables of RestClient class will be assigned with the values as written inside the constructor i.e. this.classVariable = local variable
	
	@BeforeMethod   //So as to create the separate object of RestClient for each @Test annotation.
	public void getUserSetup() {
		restClient = new RestClient(prop, baseURI );
	}
	
	@Test
	public void getAllUsersTest() {
//		restClient = new RestClient();//Now, since BaseTest setup() method is ready, we can remove this line and inherit it from the BaseTest(Parent) class. Commenting it as of now for understanding.
		restClient.get(GOREST_ENDPOINT, true, true)//We will get the error here for restClient until we declare the restClient as public or protected bcoz with default access modifier we cannot access beyond package. Do not declare it as public otherwise anyone can create object of BaseTest class and access it. So, make it protected.
					.then().log().all()
					.assertThat().statusCode(APIHttpStatus.OK_200.getCode());
	}

	//Now, prob is that in all the 3 TCs, we are using the same restClient, so it will append the service URLs for the second and third TC and giving us incorrect or failed result. We need to have a separate object of restClient for each TC.
	//So, we will create an object of RestClient in the @BeforeMethod which will be executed before each @Test annotation.
	@Test
	public void getUserTest() {
		restClient.get(GOREST_ENDPOINT+"/"+5970794, true, true) //here, even though we have marked isAuth as true, it won't add the header again as we have added feature flag of isAuthorizationHeaderAdded in addAuthorization() method in RestClient class.
					.then().log().all()
					.assertThat().statusCode(APIHttpStatus.OK_200.getCode())
					.and()
					.body("id", equalTo(5970794));
	}
	
	//url?name&staus
	@Test()
	public void getUserWithQueryParamsTest() {
		
		Map<String,Object> queryParams = new HashMap<String,Object>();
		queryParams.put("name", "Kartik");
		queryParams.put("status", "active");

		restClient.get(GOREST_ENDPOINT, null, queryParams, true, true) //If we don't want to pass the headers, we can pass the 'null' value.
		.then().log().all()
		.assertThat().statusCode(APIHttpStatus.OK_200.getCode());
		
	}
}
