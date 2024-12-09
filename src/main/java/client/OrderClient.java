package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    public static final String INGREDIENTS_PATH = "/api/ingredients";
    public static final String ORDER_PATH = "/api/orders";
    public static final String SPECIFIC_ORDER_PATH = "/api/orders";

    @Step("Get information about available ingredients using endpoint /api/ingredients")
    public ValidatableResponse ingredients() {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Create an order using endpoint /api/orders")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then()
                .log()
                .all();

    }

    @Step("Create an order for an unauthorized user using endpoint /api/orders (without accessToken")
    public ValidatableResponse noAuthorizedOrder(Order order) {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Get order details of a specific user using endpoint /api/orders")
    public ValidatableResponse getSpecificOrderList(String accessToken) {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(SPECIFIC_ORDER_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Get order data of a specific user without authorization")
    public ValidatableResponse getSpecificNoAuthorizedOrderList() {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .when()
                .get(SPECIFIC_ORDER_PATH)
                .then()
                .log()
                .all();
    }

}
