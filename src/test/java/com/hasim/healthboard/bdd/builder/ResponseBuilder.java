
package com.hasim.healthboard.bdd.builder;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.bazaarvoice.jolt.JsonUtils;


public class ResponseBuilder {

    public String getHealthCheckResponse(String message) {

        return JsonUtils.toPrettyJsonString(message);
    }

    public String getExpectedCardReaderDevice(String expectedResponse) {

        if (StringUtils.contains(expectedResponse, "Empty")) {
            return JsonUtils.toPrettyJsonString(StringUtils.EMPTY);

        }

        // List<Object> billersSpec = JsonUtils.filepathToList("src/test/integration/data/" +
        // System.getProperty("appenv") + "/billers/BillersTemplate.json");
        // Chainr chainr = Chainr.fromSpec(billersSpec);
        Object inputJSON = JsonUtils.filepathToObject("src/test/integration/data/"
            + System.getProperty("appenv") + "/response/" + expectedResponse + ".json");
        // Object transformedOutput = chainr.transform(inputJSON);
        return JsonUtils.toPrettyJsonString(inputJSON);
    }

    public String getExpectedErrorResponse(String errorCode, String errorMsg) {
        String expectedErrorJSON = StringUtils.EMPTY;
        HashMap<String, HashMap> responseHeaderMapJSON = new HashMap<String, HashMap>();
        HashMap<String, String> responseHeaderJSON = new HashMap<String, String>();
        responseHeaderJSON.put("code", errorCode);
        responseHeaderJSON.put("message", errorMsg);
        responseHeaderMapJSON.put("error", responseHeaderJSON);

        try {
            expectedErrorJSON = new ObjectMapper().writeValueAsString(responseHeaderMapJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return expectedErrorJSON;
    }

}
