package tests.user.auth;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tests.BaseTest;

import java.util.Arrays;
import java.util.Collection;

import static helper.Helper.*;

@RunWith(Parameterized.class)
public class NegativeLoginTests extends BaseTest {

    private final String email;
    private final String password;
    private final String expectedMessage;

    public NegativeLoginTests(String email, String password, String expectedMessage) {
        this.email = email;
        this.password = password;
        this.expectedMessage = expectedMessage;
    }

    @Parameterized.Parameters(name = "{index}: Login with email={0}, password={1}, expected message={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {BaseTest.randomEmail, "wrongPassword", CREDENTIALS_IS_NOT_VALID},
                {"wrong@mail.com", BaseTest.randomPassword, CREDENTIALS_IS_NOT_VALID},
                {"wrong@mail.com", "wrongPassword", CREDENTIALS_IS_NOT_VALID},
                {BaseTest.randomEmail, BaseTest.randomPassword, CREDENTIALS_IS_NOT_VALID}
        });
    }

    @Test
    @DisplayName("Log in with incorrect login and password")
    @Step("Negative: Wrong credentials")
    public void testLoginUserWithDifferentCredentials() {
        User user = new User(email, password, null);
        Response response = BaseTest.userClient.loginUser(user);
        response
                .then().assertThat().statusCode(ACCESS_DENIED_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(false))
                .and()
                .body(MESSAGE_API_RESPONSE, Matchers.is(expectedMessage));
    }
}
