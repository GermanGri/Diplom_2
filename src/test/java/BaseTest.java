import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import site.nomoreparties.stellarburgers.User;

import java.util.List;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class BaseTest {
    public String randomEmail;
    public String randomPassword;
    public String randomName;
    public static String accessToken;
    public static List<String> ingredientList;
    @Before
    @Step("Generate credentials")
    public void setUp() {
        createUser();
    }

    @AfterClass
    @Step("Delete credentials")
    static public void DeleteUser() {
        given()
                .auth().oauth2(accessToken)
                .delete(USER_URL)
                .then().assertThat().statusCode(202).and().body("success", Matchers.is(true));
    }
    public static void getListIngredient() {
        RestAssured.baseURI = BASE_URI;
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .get(GET_INGREDIENTS_URL);
        response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
        JsonPath jsonPathEvaluator = response.jsonPath();
        ingredientList = jsonPathEvaluator.getList("data._id");
    }
    public void createUser() {
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
}
