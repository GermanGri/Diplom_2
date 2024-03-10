import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;

import static helper.Helper.*;
import static helper.Helper.CONTENT_TYPE_VALUE;
import static io.restassured.RestAssured.given;

public class ChangingUserDataTests {

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
    @Step("Positive: Changing user email")
    public void testChangeUserEmail(){
        User user = new User("test" + randomEmail, randomPassword, randomName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .and()
                .body(user)
                .when()
                .patch(USER_URL)
                .then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }
    @Test
    @Step("Positive: Changing user password")
    public void testChangeUserPass(){
        User user = new User(randomEmail, "test" + randomPassword, randomName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .and()
                .body(user)
                .when()
                .patch(USER_URL)
                .then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @Step("Positive: Changing user name")
    public void testChangeUserName(){
        User user = new User(randomEmail, "test" + randomPassword, randomName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .and()
                .body(user)
                .when()
                .patch(USER_URL)
                .then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @Step("Negative: Changing user data without token")
    public void testChangeUserDataWithoutToken(){
        User user = new User("test" + randomEmail, randomPassword, randomName);
        given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .and()
                .body(user)
                .when()
                .patch(USER_URL)
                .then().assertThat().statusCode(401).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
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
