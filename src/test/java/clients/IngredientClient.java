package clients;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;

import java.util.List;

import static helper.Helper.*;
import static io.restassured.RestAssured.given;

public class IngredientClient extends Client {

    public static List<String> ingredientList;
    public final RequestSpecification requestSpec;

    public IngredientClient() {
        this.requestSpec = given()
                .baseUri(BASE_URI)
                .contentType(CONTENT_TYPE_VALUE);
    }

    @Step("Get List with Ingredients")
    public List<String> getListIngredient() {
        Response response = requestSpec.get(GET_INGREDIENTS_URL);

        response.then()
                .assertThat().statusCode(SUCCESS_RESPONSE_CODE)
                .and()
                .body(SUCCESS_API_RESPONSE, Matchers.is(true));

        JsonPath jsonPathEvaluator = response.jsonPath();
        ingredientList = jsonPathEvaluator.getList("data._id");

        return ingredientList;
    }
}
