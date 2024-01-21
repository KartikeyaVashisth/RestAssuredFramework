package com.qa.gorest.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qa.gorest.base.BaseTest;
import com.qa.gorest.client.RestClient;
import com.qa.gorest.constants.APIHttpStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AmadeusAPITest extends BaseTest {

	//In the interview always say yes we have migrated to OAuth 2.0, devs give us all necessary information to generate the token like clientID, clientSecret and then we use the access token for our tests.
	private String accessToken;
	
	@Parameters({"baseURI", "grantType", "clientId", "clientSecret"}) //These values we will get from the xml file.
	@BeforeMethod    //For this class, we are creating separate @BeforeMethod bcoz we need to pass "grantType", "clientId", "clientSecret" specifically which are not needed for other Tests except for this OAuth2.0
	public void flightAPISetup(String baseURI, String grantType, String clientId, String clientSecret) {
		restClient = new RestClient(prop, baseURI);
		accessToken = restClient.getAccessToken(AMADEUS_TOKEN_ENDPOINT, grantType, clientId, clientSecret);
	}
	
	@Test
	public void getFlightInfoTest() {
		
		RestClient restClientFlight = new RestClient(prop, baseURI); //New Object since this is a new HTTP GET call.
		
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("origin", "PAR");
		queryParams.put("maxPrice", 200);

		Map<String, String> headersMap = new HashMap<String, String>();
		headersMap.put("Authorization", "Bearer "+ accessToken);
		
		Response flightDataResponse = restClientFlight.get(AMADEUS_FLIGHTBOKKING_ENDPOINT, headersMap, queryParams, false, true)
							.then().log().all()
							.assertThat()
								.statusCode(APIHttpStatus.OK_200.getCode())
									.and()
										.extract()
											.response();
		
		JsonPath js = flightDataResponse.jsonPath();
		String type = js.get("data[0].type");
		System.out.println(type);//flight-destination
		
	}
}
