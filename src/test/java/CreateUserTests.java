import api.client.UserClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class CreateUserTests {
    private String randomEmail;
    private String randomPassword;
    private String randomName;
    private static String accessToken;

    @Before
    @Step("Generate credentials")
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        randomEmail = generateRandomEmail(5);
        randomPassword = generateRandomString(6);
        randomName = generateRandomString(7);

    }

    @Test
    @DisplayName("Create a unique user")
    @Step("Positive: Create User")
    public void testCreateUser() {
        getUser(randomEmail, randomPassword, randomName);
    }

    @Test
    @DisplayName("Create a user who is already registered")
    @Step("Negative: Create same user")
    public void testCreateSameUser(){
        User user = getUser(randomEmail, randomPassword, randomPassword);
        given()
                .header(CONTENT_TYPE_LABEL,CONTENT_TYPE_VALUE)
                .body(user)
                .when()
                .post(USER_REGISTER_URL)
                .then().assertThat().statusCode(403).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }

    @Test
    @DisplayName("Create a user and don't fill in one of the required fields")
    @Step("Negative: Create user without field")
    public void testCreateUserWithoutField() {
        User tempUser = getUser(randomEmail, randomPassword, randomName);
        User user = new User(null, randomPassword, randomName);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(user)
                .when()
                .post(USER_REGISTER_URL);
        response.then().assertThat().statusCode(403).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Step("Positive: Create User")
    private User getUser(String randomEmail, String randomPassword, String randomName) {
        User user = new User(randomEmail, randomPassword, randomName);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(user)
                .when()
                .post(USER_REGISTER_URL);
                response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        return user;
    }

    @AfterClass
    @Step("Delete credentials")
    static public void DeleteUser() {
        given()
                .auth().oauth2(accessToken)
                .delete(USER_URL)
                .then().assertThat().statusCode(202).and().body("success", Matchers.is(true));
    }
}
