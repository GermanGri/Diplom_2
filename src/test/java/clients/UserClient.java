package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.User;
import org.hamcrest.Matchers;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    public final RequestSpecification requestSpec;

    public UserClient() {
        this.requestSpec = given()
                .baseUri(BASE_URI)
                .contentType(CONTENT_TYPE_VALUE);
    }

    @Step("Positive: Create User")
    public String createUser(String email, String password, String name) {
        User user = new User(email, password, name);
        try {
            Response response = requestSpec.body(user)
                    .post(getEndpointUrl(USER_REGISTER_URL));
            assertStatusCode(response, SUCCESS_RESPONSE_CODE);
            String accessToken = extractAccessToken(response);
            System.out.println("User created successfully. Access Token: " + accessToken);
            return accessToken;
        } catch (Exception e) {
            System.err.println(ERROR_USER + e.getMessage());
            return null;
        }
    }

    @Step("Positive: Create User")
    public Response createUser(User user) {
        try {
            return requestSpec.body(user)
                    .post(getEndpointUrl(USER_REGISTER_URL));
        } catch (Exception e) {
            System.err.println(ERROR_USER + e.getMessage());
            return null;
        }
    }

    @Step("Positive: Get User Info")
    public Response getUserInfo(String accessToken) {
        try {
            Response response = requestSpec.header(AUTHORIZATION, BEARER + accessToken)
                    .get(getEndpointUrl(USER_URL));
            System.out.println("User Info: " + response.body().asString());
            return response;
        } catch (Exception e) {
            System.err.println(ERROR_USER + e.getMessage());
            return null;
        }
    }

    @Step("Positive: Change User Info")
    public Response changeUserInfo(String accessToken, User user) {
        try {
            Response response = requestSpec.header(AUTHORIZATION, BEARER + accessToken)
                    .body(user)
                    .when()
                    .patch(getEndpointUrl(USER_URL));

            System.out.println("User Info: " + response.body().asString());
            return response;
        } catch (Exception e) {
            System.err.println(ERROR_USER + e.getMessage());
            return null;
        }
    }

    @Step("Positive: Auth User")
    public Response loginUser(User user) {
        try {
            return requestSpec.body(user).post(getEndpointUrl(USER_LOGIN_URL));
        } catch (Exception e) {
            System.err.println(SOME_ERROR + e.getMessage());
            return null;
        }
    }

    @Step("Positive: Delete User")
    public void deleteUser(String accessToken) {
        try {
            given()
                    .auth().oauth2(accessToken)
                    .delete(getEndpointUrl(USER_URL))
                    .then()
                    .assertThat()
                    .statusCode(202)
                    .and()
                    .body(SUCCESS_API_RESPONSE, Matchers.is(true));

            System.out.println("User deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }
}


