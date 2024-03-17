import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;

import static helper.Helper.*;
import static helper.Helper.CONTENT_TYPE_VALUE;
import static io.restassured.RestAssured.given;

public class ChangingUserDataTests extends BaseTest {

    @Test
    @DisplayName("Changing user email with authorization")
    @Step("Positive: Changing user email")
    public void testChangeUserEmail() {
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
    @DisplayName("Changing user password with authorization")
    @Step("Positive: Changing user password")
    public void testChangeUserPass() {
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
    @DisplayName("Changing user name with authorization")
    @Step("Positive: Changing user name")
    public void testChangeUserName() {
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
    @DisplayName("Changing user data without authorization")
    @Step("Negative: Changing user data without token")
    public void testChangeUserDataWithoutToken() {
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

}
