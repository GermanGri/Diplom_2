import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Order;
import site.nomoreparties.stellarburgers.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class CreatingOrderTests {

    List<String> ingredientList;
    private String randomEmail;
    private String randomPassword;
    private String randomName;
    private static String accessToken;
    String wrongIngredientFirst = "61c0c5a71d1f82001bdaaa7d";
    String wrongIngredientSecond = "61c0c5a71d1f82001bdaaa7f";

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
    @Step("Positive: Create order without auth")
    public void createOrderWithoutAuth(){
        Order order = new Order();
        order.setIngredients(ingredientList);
        Response response =given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }
    @Test
    @Step("Positive: Create order with auth user")
    public void createOrderWithAuthUser(){
        Order order = new Order();
        order.setIngredients(ingredientList);
        Response response =given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @Step("Negative: Create order without ingredients without auth")
    public void createOrderWithoutIngredientsWithoutAuth(){
        Order order = new Order();
        Response response =given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is(  "Ingredient ids must be provided"));
    }
    @Test
    @Step("Negative: Create order without ingredients with auth")
    public void createOrderWithoutIngredientsWithAuth(){
        Order order = new Order();
        Response response =given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is(  "Ingredient ids must be provided"));
    }

    @Test
    @Step("Negative: Create order with bad ingredients without auth")
    public void createOrderWithBadIngredientsWithoutAuth(){
        Order order = new Order();
        ingredientList.clear();
        ingredientList.add(wrongIngredientFirst);
        ingredientList.add(wrongIngredientSecond);
        order.setIngredients(ingredientList);
        Response response =given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is(  "One or more ids provided are incorrect"));
    }
    @Test
    @Step("Negative: Create order with bad ingredients with auth")
    public void createOrderWithBadIngredients(){
        Order order = new Order();
        ingredientList.clear();
        ingredientList.add(wrongIngredientFirst);
        ingredientList.add(wrongIngredientSecond);
        order.setIngredients(ingredientList);
        Response response =given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is(  "One or more ids provided are incorrect"));
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





