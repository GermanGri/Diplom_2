import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Order;



import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class CreatingOrderTests extends BaseTest{
    String wrongIngredientFirst = "61c0c5a71d1f82001bdaaa7d";
    String wrongIngredientSecond = "61c0c5a71d1f82001bdaaa7f";


    @Test
    @DisplayName("Creating an order without authorization")
    @Step("Positive: Create order without auth")
    public void createOrderWithoutAuth() {
        Order order = new Order();
        BaseTest.getListIngredient();
        order.setIngredients(ingredientList);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Creating an order with authorization")
    @Step("Positive: Create order with auth user")
    public void createOrderWithAuthUser() {
        Order order = new Order();
        BaseTest.getListIngredient();
        order.setIngredients(ingredientList);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Creating an order without ingredients and without auth")
    @Step("Negative: Create order without ingredients without auth")
    public void createOrderWithoutIngredientsWithoutAuth() {
        Order order = new Order();
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Creating an order without ingredients and with auth")
    @Step("Negative: Create order without ingredients with auth")
    public void createOrderWithoutIngredientsWithAuth() {
        Order order = new Order();
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Creating an order with an incorrect ingredient hash and without auth")
    @Step("Negative: Create order with bad ingredients without auth")
    public void createOrderWithBadIngredientsWithoutAuth() {
        Order order = new Order();
        BaseTest.getListIngredient();
        ingredientList.clear();
        ingredientList.add(wrongIngredientFirst);
        ingredientList.add(wrongIngredientSecond);
        order.setIngredients(ingredientList);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("One or more ids provided are incorrect"));
    }

    @Test
    @DisplayName("Creating an order with an incorrect ingredient hash and with auth")
    @Step("Negative: Create order with bad ingredients with auth")
    public void createOrderWithBadIngredients() {
        Order order = new Order();
        BaseTest.getListIngredient();
        ingredientList.clear();
        ingredientList.add(wrongIngredientFirst);
        ingredientList.add(wrongIngredientSecond);
        order.setIngredients(ingredientList);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDERS);
        response.then().assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("One or more ids provided are incorrect"));
    }

}





