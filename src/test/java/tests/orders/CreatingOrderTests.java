package tests.orders;

import clients.IngredientClient;
import clients.OrdersClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import org.hamcrest.Matchers;
import org.junit.Test;
import tests.BaseTest;

import java.util.ArrayList;

import static helper.Helper.*;

public class CreatingOrderTests extends BaseTest {
    String wrongIngredientFirst = "61c0c5a71d1f82001bdaaa7d";
    String wrongIngredientSecond = "61c0c5a71d1f82001bdaaa7f";


    @Test
    @DisplayName("Creating an order without authorization")
    @Step("Positive: Create order without auth")
    public void createOrderWithoutAuth() {
        Order order = new Order();
        IngredientClient ingredientClient = new IngredientClient();
        order.setIngredients(ingredientClient.getListIngredient());
        OrdersClient ordersClient = new OrdersClient();
        ordersClient.createOrder(order)
                .then()
                .assertThat().statusCode(SUCCESS_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(true));
    }

    @Test
    @DisplayName("Creating an order with authorization")
    @Step("Positive: Create order with auth user")
    public void createOrderWithAuthUser() {
        Order order = new Order();
        IngredientClient ingredientClient = new IngredientClient();
        order.setIngredients(ingredientClient.getListIngredient());
        OrdersClient ordersClient = new OrdersClient();
        ordersClient.createOrder(order, accessToken)
                .then()
                .assertThat().statusCode(SUCCESS_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(true));
    }

    @Test
    @DisplayName("Creating an order without ingredients and without auth")
    @Step("Negative: Create order without ingredients without auth")
    public void createOrderWithoutIngredientsWithoutAuth() {
        Order order = new Order();
        OrdersClient ordersClient = new OrdersClient();
        Response response = ordersClient.createOrder(order);
        response.then().assertThat().statusCode(BAD_REQUEST).and().body(SUCCESS_API_RESPONSE, Matchers.is(false))
                .and().body(MESSAGE_API_RESPONSE, Matchers.is(SHOULD_BE_PROVIDED));
    }

    @Test
    @DisplayName("Creating an order without ingredients and with auth")
    @Step("Negative: Create order without ingredients with auth")
    public void createOrderWithoutIngredientsWithAuth() {
        Order order = new Order();
        OrdersClient ordersClient = new OrdersClient();
        Response response = ordersClient.createOrder(order, accessToken);
        response.then().assertThat().statusCode(BAD_REQUEST).and().body(SUCCESS_API_RESPONSE, Matchers.is(false))
                .and().body(MESSAGE_API_RESPONSE, Matchers.is(SHOULD_BE_PROVIDED));
    }

    @Test
    @DisplayName("Creating an order with an incorrect ingredient hash and without auth")
    @Step("Negative: Create order with bad ingredients without auth")
    public void createOrderWithBadIngredientsWithoutAuth() {
        Order order = new Order();
        ingredientList = new ArrayList<>();
        ingredientList.add(wrongIngredientFirst);
        ingredientList.add(wrongIngredientSecond);
        OrdersClient ordersClient = new OrdersClient();
        order.setIngredients(ingredientList);
        Response response = ordersClient.createOrder(order);
        response.then().assertThat().statusCode(BAD_REQUEST).and().body(SUCCESS_API_RESPONSE, Matchers.is(false))
                .and().body(MESSAGE_API_RESPONSE, Matchers.is(INCORRECT_VALUE));
    }

    @Test
    @DisplayName("Creating an order with an incorrect ingredient hash and with auth")
    @Step("Negative: Create order with bad ingredients with auth")
    public void createOrderWithBadIngredients() {
        Order order = new Order();
        ingredientList = new ArrayList<>();
        ingredientList.add(wrongIngredientFirst);
        ingredientList.add(wrongIngredientSecond);
        OrdersClient ordersClient = new OrdersClient();
        order.setIngredients(ingredientList);
        Response response = ordersClient.createOrder(order, accessToken);
        response.then().assertThat().statusCode(BAD_REQUEST).and().body(SUCCESS_API_RESPONSE, Matchers.is(false))
                .and().body(MESSAGE_API_RESPONSE, Matchers.is(INCORRECT_VALUE));
    }

}





