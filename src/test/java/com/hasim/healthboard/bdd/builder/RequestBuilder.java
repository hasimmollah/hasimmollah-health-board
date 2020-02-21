
package com.hasim.healthboard.bdd.builder;

import java.io.File;
import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestBuilder {

	private static File endpointJSONFile = new File("src/test/integration/endpoints/APIEndpoints.json");

	private RequestSpecification request;

	private HashMap<String, Object> requestHeaderJSON = new HashMap<String, Object>();

	public RequestBuilder() {
		request = RestAssured.with();

	}

	public Response getHealthBoardResponse(String endpoint) {
		System.out.println("Final header " + requestHeaderJSON);
		requestHeaderJSON.put("origin", "localhost");
		request.headers(requestHeaderJSON);
		Response response;

		response = request.when().get(JsonPath.from(endpointJSONFile).getString(endpoint) + "health");
		return response.prettyPeek();
	}

}
