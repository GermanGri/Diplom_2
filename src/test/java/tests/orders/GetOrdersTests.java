package tests.orders;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Order;
import org.hamcrest.Matchers;
import org.junit.Assert;

import org.junit.Test;
import tests.BaseTest;


import java.util.List;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class GetOrdersTests extends BaseTest {

    @Test
    @DisplayName("Receiving orders from an authorized user")
    @Step("Positive: Get user orders")
    public void testGetUserOrders() {
        Order order = new Order();
        BaseTest.getListIngredient();
        order.setIngredients(ingredientList);
        Response response = given()
                .header(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(ORDERS_URL);
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
    @DisplayName("Receiving orders from an unauthorized user")
    @Step("Negative: Get orders without auth")
    public void testGetOrdersWithoutAuth() {
        given()
                .get(ORDERS_URL)
                .then().assertThat().statusCode(401).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

}
