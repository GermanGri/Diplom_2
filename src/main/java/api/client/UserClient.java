package api.client;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import site.nomoreparties.stellarburgers.User;

import static io.restassured.RestAssured.given;



public class UserClient {
    public static final String CONTENT_TYPE_LABEL = "Content-type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String AUTH_URL = "/api/auth";
    public static String accessToken;


    public User getUser(String randomEmail, String randomPassword, String randomName, String url, int statusCode){
        User user = new User(randomEmail, randomPassword, randomName);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(user)
                .when()
                .post(AUTH_URL + url);
        response.then().assertThat().statusCode(statusCode).and().body("success", Matchers.is(true));
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        return user;
    }
}


