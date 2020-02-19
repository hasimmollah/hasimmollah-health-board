package com.hasim.healthboard.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hasim.healthboard.api.model.ErrorResponse;
import com.hasim.healthboard.api.util.DashboardCommonUtil;

@RunWith(MockitoJUnitRunner.class)
public class DashboardCommonUtilTest {
    @Test
    public void testConvertStringToObject() {

        String expectedCode = "test";
        String expectedMessage = "test message";

        ErrorResponse actualErrorResponse =
            DashboardCommonUtil.convertStringToObject("{\"Error\":{\"Code\":\"" + expectedCode
                + "\",\"Message\":\"" + expectedMessage + "\"}}", ErrorResponse.class);
        assertNotNull(actualErrorResponse);
        assertEquals(expectedCode, actualErrorResponse.getCode());
        assertEquals(expectedMessage, actualErrorResponse.getMessage());
    }
    
    
    @Test
    public void testConvertStringToObjectNull() {

       

        ErrorResponse actualErrorResponse =
            DashboardCommonUtil.convertStringToObject("sdsd", ErrorResponse.class);
        assertNull(actualErrorResponse);
       
    }
    
    @Test
    public void testConvertObjectToJson() {

        String expectedCode = "test";
        String expectedMessage = "test message";
        String expectedResponse = "{\"Error\":{\"Message\":\"" +expectedMessage  + "\",\"Code\":\""
            + expectedCode + "\"}}";
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(expectedCode);
        errorResponse.setMessage(expectedMessage);
        String actualErrorResponse = null;
        try {
            actualErrorResponse = DashboardCommonUtil.convertObjectToJson(errorResponse);
        } catch (JsonProcessingException e) {

        }
        assertNotNull(actualErrorResponse);
        assertEquals(expectedResponse, actualErrorResponse);
    }
    @Test
    public void testConvertObjectToJsonNull() {

 
        String actualErrorResponse = null;
        try {
            actualErrorResponse = DashboardCommonUtil.convertObjectToJson(null);
        } catch (JsonProcessingException e) {

        }
        assertNull(actualErrorResponse);
    }
}
