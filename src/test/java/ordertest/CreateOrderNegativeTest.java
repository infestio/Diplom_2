package ordertest;

import client.*;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CreateOrderNegativeTest {

    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        accessToken = userClient.create(user).extract().path("accessToken");
        orderClient = new OrderClient();
    }

    @Test
    @Description("Can't create an order without ingredients, the endpoint will return an error")
    public void noIngredientsOrderTest() {
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken").toString().substring(7, 178);
        String[] noIngredients = {};
        Order order = new Order(noIngredients);
        ValidatableResponse noIngredientsOrderResponse = orderClient.createOrder(order, accessToken);
        int noIngredientOrderResponseStatusCode = noIngredientsOrderResponse.extract().statusCode();
        assertEquals(400, noIngredientOrderResponseStatusCode);
        String noIngredientOrderResponseError = noIngredientsOrderResponse.extract().path("message");
        assertEquals("Ingredient ids must be provided", noIngredientOrderResponseError);
    }

    @Test
    @Description("Can't create an order with invalid hash ingredients, the endpoint will return status code 500")
    public void invalidHashOrderTest() {
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken").toString().substring(7, 178);
        String[] invalidHashIngredients = {"ржаной хлеб", "чеснок"};
        Order order = new Order(invalidHashIngredients);
        ValidatableResponse noIngredientsOrderResponse = orderClient.createOrder(order, accessToken);
        int noIngredientOrderResponseStatusCode = noIngredientsOrderResponse.extract().statusCode();
        assertEquals(500, noIngredientOrderResponseStatusCode);
    }

    @Test
    @Description("It is impossible to create an order without user authorization, the endpoint will return an error")
    @Issue("BUG_001")
    public void noAuthorizedUserOrderTest() {
        List<Map<String, String>> listIngredients = orderClient.ingredients().extract().path("data");
        String[] ingredients = {listIngredients.get(3).get("_id"), listIngredients.get(5).get("_id")};
        Order order = new Order(ingredients);
        ValidatableResponse noAuthorizedUserOrderResponse = orderClient.noAuthorizedOrder(order);
        int noAuthorizedUserOrderResponseStatusCode = noAuthorizedUserOrderResponse.extract().statusCode();
        assertEquals(401, noAuthorizedUserOrderResponseStatusCode);
        String noAuthorizedUserOrderResponseError = noAuthorizedUserOrderResponse.extract().path("message");
        assertEquals("You should be authorised", noAuthorizedUserOrderResponseError);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
