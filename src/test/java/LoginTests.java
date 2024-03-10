import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;

import static helper.Helper.*;
import static helper.Helper.generateRandomString;
import static io.restassured.RestAssured.given;

public class LoginTests {

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
        User user = new User(randomEmail, randomPassword, randomName);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(user)
                .when()
                .post(USER_REGISTER_URL);
        response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
    }

    @Test
    @Step("Positive: Log in user")
    public void testLoginUser() {
        User user = new User(randomEmail, randomPassword, null);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(user)
                .when()
                .post(USER_LOGIN_URL)
                .then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @Step("Negative: Wrong credentials")
    public void testLoginUserWrongCreds() {
        User user = new User("wrong@mail.ru", "wrongPassword", null);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(user)
                .when()
                .post(USER_LOGIN_URL)
                .then().assertThat().statusCode(401).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
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
