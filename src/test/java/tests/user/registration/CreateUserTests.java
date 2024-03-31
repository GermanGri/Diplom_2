package tests.user.registration;

import clients.UserClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static helper.Helper.*;

public class CreateUserTests {
    private String randomEmail;
    private String randomPassword;
    private String randomName;
    private static String accessToken;
    private static UserClient userClient;

    @Before
    @Step("Generate credentials")
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        randomEmail = generateRandomEmail(5);
        randomPassword = generateRandomString(6);
        randomName = generateRandomString(7);
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Create a unique user")
    @Step("Positive: Create User")
    public void testCreateUser() {
        accessToken = userClient.createUser(randomEmail, randomPassword, randomName);
    }

    @Test
    @DisplayName("Create a user who is already registered")
    @Step("Negative: Trying to create already existed user")
    public void testCreateSameUser() {
        User user = new User(randomEmail, randomPassword, randomName);
        Response firstResponse = userClient.createUser(user);
        firstResponse.then().assertThat().statusCode(SUCCESS_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(true));
        Response alreadyCreatedUserResponse = userClient.createUser(user);
        alreadyCreatedUserResponse
                .then().assertThat().statusCode(FORBIDDEN_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(false))
                .and()
                .body(MESSAGE_API_RESPONSE, Matchers.is(USER_ALREADY_EXIST));
    }

    @Test
    @DisplayName("Create a user and don't fill in one of the required fields")
    @Step("Negative: Create user without field")
    public void testCreateUserWithoutField() {
        User user = new User(null, randomPassword, randomName);
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(FORBIDDEN_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(false))
                .and()
                .body(MESSAGE_API_RESPONSE, Matchers.is(ONE_OF_CRED_IS_MISSING));
    }

    @AfterClass
    @Step("Delete credentials")
    static public void DeleteUser() {
        userClient.deleteUser(accessToken);
    }
}
