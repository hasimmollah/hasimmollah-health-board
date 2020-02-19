
package com.hasim.healthboard.bdd.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class RequestBuilder {

    private static File endpointJSONFile = new File(
            "src/test/integration/endpoints/APIEndpoints.json");

    private File invalidJSONPath = new File(
            "src/test/integration/data/" + System.getProperty("appenv"));

    public static final String END_POINT_CR_IDENTIFY = "cardReaderIdentify";

    public static final String END_POINT_CR_RESPOND = "cardReaderRespond";

    public static final String END_POINT_CR_DEVICE = "cardReaderDevice";

    private RequestSpecification request;

    public final static String cardReaderIdentifyKeyName = "CardReaderIdentifyChallengeInformation";

    public final static String cardReaderRespondKeyName = "CardReaderRespondChallengeInformation";

    private static Random random = new Random();

    private static int min = 1;

    private static int max = 99999999;

    private HashMap<String, Object> requestHeaderJSON = new HashMap<String, Object>();

    private String userFileForRun;

    private static File lengthDetailFile = new File(
            "src/test/integration/data/" + System.getProperty("appenv") + "/actors/LengthDetails.json");

    private File invalidJSONFile;

    private static String cardPIN;
    private static List<String> listOfPINs;

    public RequestBuilder() {
        request = RestAssured.with();

    }

    public void setUserdetails(String userFileName) {
        userFileForRun = userFileName;
        prepareValidHeader();
    }

    public void updateCardReaderToValidWithScript(String scriptHeaders) {
        updateRequestParmeters(null, null, null, scriptHeaders, null, null, null);
    }

    public void updateCardReaderToMissingHeader(String missingHeaders) {
        updateRequestParmeters(missingHeaders, null, null, null, null, null, null);
    }

    public void updateCardReaderToEmptyHeader(String emptyHeaders) {

        updateRequestParmeters(null, emptyHeaders, null, null, null, null, null);
    }

    public void updateCardReaderToInvalidHeader(String invalidHeaders) {

        updateRequestParmeters(null, null, null, null, invalidHeaders, null, null);
    }

    public void updateCardReaderToNewValue(String attribute, String newValue) {

        updateRequestParmeters(null, null, null, null, null, attribute, newValue);
    }

    public void updateCardReaderToInvalidLengthHeader(String invalidlength) {
        updateRequestParmeters(null, null, invalidlength, null, null, null, null);
    }

    public void getCardReaderInvalidHeader(String invalidHeaders, String invalidUser) {
        invalidJSONFile = new File(
                invalidJSONPath + "/actors/" + invalidUser + ".json");
        updateRequestParmeters(null, null, null, null, invalidHeaders, null, null);
    }

    private void updateRequestParmeters(String missingHeaders, String emptyHeaders,
                                        String invalidLengthHeader, String scriptTagHeader, String invalidHeader, String attribute,
                                        String newValue) {

        if (missingHeaders != null) {
            missingHeaders(missingHeaders);
        }

        if (emptyHeaders != null) {
            emptyHeaders(emptyHeaders);

        }

        if (invalidLengthHeader != null) {
            invalidLengthHeaders(invalidLengthHeader);
        }

        if (scriptTagHeader != null) {
            requestHeaderJSON.replace(scriptTagHeader, "<script> Select * <script/>");
        }
        if (invalidHeader != null) {
            invalidHeaders(invalidHeader);
        }
        if (newValue != null) {
            newValue(attribute, newValue);
        }
    }

    public void replaceAttribute(String attribute, String newValue) {
        updateRequestParmeters(null, null, null, null, null, attribute, newValue);
    }

    public Response getHealthBoardResponse(String endpoint) {
        System.out.println("Final header " + requestHeaderJSON);
        request.headers(requestHeaderJSON);
        Response response;
        System.out.println("System.getenv(\"HTTP_PROXY\")  " + System.getenv("HTTP_PROXY"));
        if (System.getProperty("appenv") == "SIT") {
            setCertifcates();
        } else if (System.getenv("HTTP_PROXY") != null) {
            System.out.println(" Proxy not null");
            String[] httpProxy = System.getenv("HTTP_PROXY").split(":");
            request.given().relaxedHTTPSValidation().proxy(httpProxy[0],
                    Integer.parseInt(httpProxy[1]));
        }
        response = request.when().get(JsonPath.from(endpointJSONFile).getString(endpoint+"health"));
        return response.prettyPeek();
    }

    public Response getCardReaderResponse(String endpoint, String jsonFile, String paramName,
                                          String paramValue) {
        HashMap<String, Object> jsonBody = prepareRequestBody(jsonFile);
        if (paramName != null && END_POINT_CR_IDENTIFY.equalsIgnoreCase(endpoint)) {
            HashMap<String, String> obj = (HashMap<String, String>) jsonBody.get(cardReaderIdentifyKeyName);
            splitStringAndInsertInMap(paramName, paramValue, obj);
        }
        if (paramName != null && END_POINT_CR_RESPOND.equalsIgnoreCase(endpoint)) {
            HashMap<String, String> obj = (HashMap<String, String>) jsonBody.get(cardReaderRespondKeyName);
            splitStringAndInsertInMap(paramName, paramValue, obj);
        }
        if (System.getProperty("appenv") == "SIT" && StringUtils.containsIgnoreCase(jsonFile, "AUTOPIN")) {
            updateUserPIN(requestHeaderJSON, jsonBody, endpoint);
        }
        System.out.println("Final header " + requestHeaderJSON);
        System.out.println("Final body " + jsonBody);
        request.headers(requestHeaderJSON).body(jsonBody);
        Response response;
        System.out.println("System.getenv(\"HTTP_PROXY\")  " + System.getenv("HTTP_PROXY"));
        if (System.getProperty("appenv") == "SIT") {
            setCertifcates();
        } else if (System.getenv("HTTP_PROXY") != null) {
            System.out.println(" Proxy not null");
            String[] httpProxy = System.getenv("HTTP_PROXY").split(":");
            request.given().relaxedHTTPSValidation().proxy(httpProxy[0],
                    Integer.parseInt(httpProxy[1]));
        }
        response = request.when().post(JsonPath.from(endpointJSONFile).getString(endpoint));
        return response.prettyPeek();
    }

    private void updateUserPIN(HashMap<String, Object> requestHeader, HashMap<String, Object> requestBody, String endpoint) {
        System.out.println("requestBody.size()==========================" + requestBody.size());
        System.out.println("userId===================" + requestHeader.get("x-lbg-internal-user-id"));
        if (requestHeader.get("x-lbg-internal-user-id") == null || requestBody.size() == 0) return;
        String userId = requestHeader.get("x-lbg-internal-user-id").toString();
        File pinFile = new File("src/test/integration/data/" + System.getProperty("appenv") + "/pin/"+ endpoint + "/" + userId + ".txt");
        HashMap<String, String> obj = (HashMap<String, String>) requestBody.get(requestBody.keySet().toArray()[0]);
        try {
            listOfPINs = Files.readAllLines(pinFile.toPath());
            cardPIN = listOfPINs.remove(0);
            if (StringUtils.equalsIgnoreCase(obj.get("PassCode"), "AUTOPIN")) {
                obj.put("PassCode", cardPIN);
            }
            requestBody.put(requestBody.keySet().toArray()[0].toString(), obj);
            Files.write(pinFile.toPath(), listOfPINs, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void splitStringAndInsertInMap(String paramName, String paramValue,
                                           HashMap<String, String> map) {
        if (paramName.contains(",")) {
            String[] params = paramName.split(",");
            String[] values = paramValue.split(",");
            if (params.length != values.length) {
                throw new RuntimeException(
                        "length mismatch of passed params and params value");
            }
            for (int i = 0; i < params.length; i++) {
                map.put(params[i], values[i]);
            }
        } else {
            map.put(paramName, paramValue);
        }
    }

    private HashMap<String, Object> prepareRequestBody(String jsonFile) {
        File requestBodyJsonFile = new File(
                "src/test/integration/data/" + System.getProperty("appenv") + "/actors/" + jsonFile
                        + ".json");
        return JsonPath.from(requestBodyJsonFile).get();
    }

    private void setCertifcates() {

        /*
         * Create and Load Trust Store
         */
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance("PKCS12");
            FileInputStream certFile = new FileInputStream(
                    new File(
                            "cert/preprod-testing-updated.p12"));
            trustStore.load(certFile, "changeit".toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (trustStore != null) {
            org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = null;
            try {
                clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(
                        trustStore,
                        "changeit");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            }
            request.config(RestAssured.config().sslConfig(new SSLConfig().with()
                    .sslSocketFactory(clientAuthFactory).and().allowAllHostnames()));
        }
        /*
         * Build RequestSpecification with Certificate and Headers
         */
        request.given().trustStore(trustStore).keyStore(new File(
                "cert/preprod-testing-updated.p12"), "changeit");
    }

    private void prepareValidHeader() {

        File cardReaderJSONFile = new File(
                "src/test/integration/data/" + System.getProperty("appenv") + "/actors/"
                        + userFileForRun + ".json");
        HashMap<String, String> cardReaderJSON = JsonPath.from(cardReaderJSONFile).get();
        for (String key : cardReaderJSON.keySet()) {
            if (StringUtils.isNumeric(cardReaderJSON.get(key))) {
                cardReaderJSON.replace("x-lbg-txn-correlation-id",
                        String.valueOf(nextTransactionId()));
                requestHeaderJSON.put(key, Integer.parseInt(cardReaderJSON.get(key)));
            } else {
                requestHeaderJSON.put(key, cardReaderJSON.get(key));
            }
        }
    }

    private void missingHeaders(String headerData) {
        String[] fieldsForOperation = StringUtils.split(headerData, ",");
        for (String fieldName : fieldsForOperation) {
            requestHeaderJSON.remove(fieldName);
        }
    }

    private void emptyHeaders(String headerData) {
        String[] fieldsForOperation = StringUtils.split(headerData, ",");
        for (String fieldName : fieldsForOperation) {
            requestHeaderJSON.put(fieldName, StringUtils.EMPTY);
        }
    }

    private void invalidHeaders(String headerData) {
        String[] attributesForOperation = StringUtils.split(headerData, ",");
        for (String attributeName : attributesForOperation) {
            String valueforInvalidAttribute = getAttributeData(invalidJSONFile, attributeName);
            if (StringUtils.isNumeric(getAttributeData(invalidJSONFile, attributeName))) {
                requestHeaderJSON.put(attributeName, Integer.parseInt(valueforInvalidAttribute));
            } else {
                requestHeaderJSON.put(attributeName, valueforInvalidAttribute);
            }
        }
    }

    private void newValue(String attribute, String value) {
        String[] params = attribute.split(",");
        String[] values = value.split(",");
        if (params.length != values.length) {
            throw new RuntimeException(
                    "length mismatch of passed params and params value");
        }
        for (int i = 0; i < params.length; i++) {
            requestHeaderJSON.replace(params[i], values[i]);
        }
    }

    private void invalidLengthHeaders(String headerData) {
        String attributeMaxLength;
        String newValue;
        String[] attributesForOperation = StringUtils.split(headerData, ",");
        for (String attributeName : attributesForOperation) {
            attributeMaxLength = getAttributeData(lengthDetailFile, attributeName);
            newValue = RandomStringUtils
                    .randomAlphanumeric(Integer.parseInt(attributeMaxLength) + 1).toUpperCase();

            requestHeaderJSON.put(attributeName, newValue);
        }
    }

    private String getAttributeData(File sourceFile, String attributeName) {

        HashMap<String, String> sourceFileData = JsonPath.from(sourceFile).get();
        return sourceFileData.get(attributeName);
    }

    private String getFieldData(File sourceFile, String fieldName) {
        HashMap<String, String> sourceFileData = JsonPath.from(sourceFile).get();
        return sourceFileData.get(fieldName);
    }

    private static int nextTransactionId() {
        return random.nextInt((max - min) + 1) + min;
    }
}
