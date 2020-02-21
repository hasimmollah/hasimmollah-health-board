
package com.hasim.healthboard.bdd.stepdefs;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import com.hasim.healthboard.bdd.builder.RequestBuilder;
import com.hasim.healthboard.bdd.builder.ResponseBuilder;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.JsonParser;
import io.restassured.response.Response;

public class HealthBoardStepdef {
	private RequestBuilder requestBuilder;

	private ResponseBuilder responseBuilder;

	private Response actualResponse;

	private String expectedResponse;

	JsonParser parser;

	@Before
	public void before() throws SQLException {
		/*
		 * if (System.getProperty("appenv") == null) { System.setProperty("appenv",
		 * "Sandbox"); }
		 */
		requestBuilder = new RequestBuilder();
		responseBuilder = new ResponseBuilder();
		parser = new JsonParser();
	}

	@After
	public void after() {
		// DBConn.closeConnection();
	}

	@When("^I make a request for health check to Health Board Api")
	public void iMakeARequestToHealthCheck() {
		actualResponse = requestBuilder.getHealthBoardResponse("healthCheck");
	}

	@Then("^I should get the response with StatusCode as (.*) and health check message as (.*)$")
	public void shouldGetTheResponseStatusCodeAndHealthCheckMessage(int statusCode, String message) throws Throwable {
		actualResponse.then().statusCode(statusCode);
		assertEquals(message, actualResponse.body().asString());
	}

}
