package com.qa.gorest.base;

import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.qa.gorest.client.RestClient;
import com.qa.gorest.configuration.ConfigurationManager;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

public class BaseTest {

	//Service URLs:
	public static final String GOREST_ENDPOINT = "/public/v2/users";
	public static final String REQRES_ENDPOINT = "/api/users";
	public static final String CIRCUIT_ENDPOINT = "/api/f1";
	public static final String AMADEUS_TOKEN_ENDPOINT = "/v1/security/oauth2/token";
	public static final String AMADEUS_FLIGHTBOKKING_ENDPOINT = "/v1/shopping/flight-destinations";


	protected ConfigurationManager config;
	protected Properties prop;//Not making the variables as public, otherwise anyone can create object of Base class and access it.
	protected RestClient restClient;//Making these as protected, so that anytime we need these in Testclass(Child) class, we can inherit and use them. Keeping them default, won't let the user to inherit and access them as default cannot be accessed outside of the current package.
	protected String baseURI; //Incase anyone wants to use the BaseURI in the child class, they can easily inherit.
	
	//We are not writing the read from Properties file in this class[initProp() method] bcoz this is a TestNG class and we should not write or have the logic to read from the Properties file here. So, we can write it in the ConfgurationManager.java class.
	@Parameters({"baseURI"}) //This will fetch the baseURI from testng.xml and give it to the setUp method. Hence, we need to pass baseURI as argument to the setUp() method as well. 
	@BeforeTest
	public void setUp(String baseURI) {
		
		RestAssured.filters(new AllureRestAssured()); //Added this one line of code to generate the Allure test report. Added here bcoz this setUp() method will be executed before every @Test method.
													//To Open the Allure report> Go to Terminal> Into the Project directory(right click on Project and Properties to get the path) 
													//Type "allure serve allure-results" //For ExtentReport, we add dependency and add one ExtentReportListener class in src/main/java
													//And need to add this ExtentReportListener class to the testng_regression.xml file at the suite level. So whenever we run the .xml file, it will get activated in Background and fetch all test results and give us a nice report.
		config = new ConfigurationManager();
		prop = config.initProp(); //Since initProp() method will return Properties class reference, so we can store it in prop.
//		String baseUri = prop.getProperty("baseURI"); //Now, instead we will get the baseURI from the runner file(testng_regression.xml) file where we have <parameter name="" value="" /> tag and we will pass the key in name= instead of getting it from the Properties file.
		this.baseURI = baseURI; //This will assign the baseURI which we have got from the runner file to the baseURI of the class variable.
//		restClient = new RestClient(prop, baseURI ); //Added in the @BeforeMethod annotation so that an object of restClient is created before each Test is executed.
		
	}
}
