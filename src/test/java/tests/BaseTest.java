package tests;

import clients.UserClient;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;

import java.util.List;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class BaseTest {
    public static String randomEmail;
    public static String randomPassword;
    public String randomName;
    public static String accessToken;
    public static List<String> ingredientList;
    public static UserClient userClient = new UserClient();

    @Before
    @Step("Generate credentials")
    public void setUp() {
        randomEmail = generateRandomEmail(5);
        randomPassword = generateRandomString(6);
        randomName = generateRandomString(7);
        accessToken = userClient.createUser(randomEmail, randomPassword, randomName);
    }

    @AfterClass
    @Step("Delete credentials")
    static public void deleteUser() {
        userClient.deleteUser(accessToken);
    }

    @Step("Get List with Ingredients")
    public static void getListIngredient() {
        RestAssured.baseURI = BASE_URI;
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .get(GET_INGREDIENTS_URL);
        response.then().assertThat().statusCode(200).and().body(SUCCESS_API_RESPONSE, Matchers.is(true));
        JsonPath jsonPathEvaluator = response.jsonPath();
        ingredientList = jsonPathEvaluator.getList("data._id");
    }
}
