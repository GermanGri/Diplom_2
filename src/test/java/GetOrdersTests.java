import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Order;
import site.nomoreparties.stellarburgers.User;

import java.util.List;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class GetOrdersTests {
    List<String> ingredientList;
    private String randomEmail;
    private String randomPassword;
    private String randomName;
    private static String accessToken;


    @Before
    @Step("Generate credentials")
    public void setUp() {
        getListIngredient();
        createUser();
    }

    private void createUser() {
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


    private void getListIngredient() {
        RestAssured.baseURI = BASE_URI;
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .get(GET_INGREDIENTS_URL);
        response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> data = jsonPathEvaluator.getList("data._id");
        ingredientList = data;
    }




    @Test
    @Step("Positive: Get user orders")
    public void testGetUserOrders(){
        Order order = new Order();
        order.setIngredients(ingredientList);
        Response response =given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
        given()
                .auth().oauth2(accessToken)
                .get(ORDERS_URL)
                .then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
                JsonPath jsonPathEvaluator = response.jsonPath();
                List<String> orderList = jsonPathEvaluator.getList("order.ingredients");
                Assert.assertFalse(orderList.isEmpty());

    }

    @Test
    @Step("Negative: Get orders without auth")
    public void testGetOrdersWithoutAuth(){
        given()
                .get(ORDERS_URL)
                .then().assertThat().statusCode(401).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @AfterClass
    @Step("Delete credentials")
    static public void DeleteUser() {
        given()
                .auth().oauth2(accessToken)
                .delete(USER_URL)
                .then().assertThat().statusCode(202).and().body("success", Matchers.is(true));
    }
}
