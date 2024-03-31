package tests.user.changedata;

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
public class PositiveChangingUserDataTests extends BaseTest {

    private final String email;
    private final String password;

    public PositiveChangingUserDataTests(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Parameterized.Parameters(name = "{index}: change email={0}, password={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"test" + randomEmail, randomPassword},
                {randomEmail, "test" + randomPassword},
        });
    }

    @Test
    @DisplayName("Changing user email with authorization")
    @Step("Positive: Changing user email")
    public void testChangeUserEmail() {
        User user = new User(email, password, null);
        Response response = userClient.changeUserInfo(accessToken, user);
        response.then().assertThat().statusCode(SUCCESS_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(true));
    }
}
