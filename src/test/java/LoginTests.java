import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class LoginTests extends BaseTest {

    @Test
    @DisplayName("Log in under an existing user")
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
    @DisplayName("Log in with incorrect login and password")
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

}
