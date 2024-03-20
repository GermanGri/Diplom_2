package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Order;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class OrdersClient extends Client {

    public final RequestSpecification requestSpec;

    public OrdersClient() {
        this.requestSpec = given()
                .baseUri(BASE_URI)
                .contentType(CONTENT_TYPE_VALUE);
    }

    @Step("Positive: Create Order")
    public Response createOrder(Order order) {
        try {
            return requestSpec.body(order).post(ORDERS_URL);
        } catch (Exception e) {
            System.err.println(SOME_ERROR + e.getMessage());
            return null;
        }
    }

    @Step("Positive: Create Order with Auth")
    public Response createOrder(Order order, String accessToken) {
        try {
            return requestSpec.header(AUTHORIZATION, BEARER + accessToken).body(order).post(ORDERS_URL);
        } catch (Exception e) {
            System.err.println(SOME_ERROR + e.getMessage());
            return null;
        }
    }
}
