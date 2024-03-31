package tests.user.changedata;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import models.User;
import org.hamcrest.Matchers;
import org.junit.Test;
import tests.BaseTest;

import static helper.Helper.*;

public class NegativeChangingUserData extends BaseTest {

    @Test
    @DisplayName("Changing user data without authorization")
    @Step("Negative: Changing user data without token")
    public void testChangeUserDataWithoutToken() {
        User user = new User("test" + randomEmail, randomPassword, "randomName");
        userClient.requestSpec
                .body(user)
                .when()
                .patch(userClient.getEndpointUrl(USER_URL))
                .then().assertThat().statusCode(ACCESS_DENIED_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(false))
                .and()
                .body(MESSAGE_API_RESPONSE, Matchers.is(USER_SHOULD_BE_AUTHORIZE));
    }
}
