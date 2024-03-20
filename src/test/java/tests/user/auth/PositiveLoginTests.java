package tests.user.auth;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.hamcrest.Matchers;
import org.junit.Test;
import tests.BaseTest;

import static helper.Helper.SUCCESS_API_RESPONSE;
import static helper.Helper.SUCCESS_RESPONSE_CODE;

public class PositiveLoginTests {

    @Test
    @DisplayName("Log in under an existing user")
    @Step("Positive: Log in user")
    public void testLoginUser() {
        User user = new User(BaseTest.randomEmail, BaseTest.randomPassword, null);
        Response response = BaseTest.userClient.loginUser(user);
        response.then().assertThat().statusCode(SUCCESS_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(true));
    }
}
