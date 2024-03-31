package clients;

import io.restassured.response.Response;

import static helper.Helper.*;

public class Client {

    public String getEndpointUrl(String endpoint) {
        return AUTH_URL + endpoint;
    }

    public void assertStatusCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    public String extractAccessToken(Response response) {
        return response.jsonPath().getString(ACCESS_TOKEN).replace(BEARER, EMPTY_STRING);
    }
}
