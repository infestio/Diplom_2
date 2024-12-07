package ordertest;

import client.*;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GetSpecificUserOrderNegativeTest {

    private UserClient userClient;
    private String accessToken;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        User user = UserGenerator.getRandom();
        accessToken = userClient.create(user).extract().path("accessToken");
        orderClient = new OrderClient();
    }

    @Test
    @Description("Cannot get order details without authorization, endpoint will return an error")
    public void getSpecificNoAuthorizedUserOrderTest() {
        List<Map<String, String>> listIngredients = orderClient.ingredients().extract().path("data");
        String[] ingredients = {listIngredients.get(3).get("_id"), listIngredients.get(5).get("_id")};
        Order order = new Order(ingredients);
        orderClient.createOrder(order, accessToken);
        ValidatableResponse getSpecNoAuthorizedUserOrderResponse = orderClient.getSpecificNoAuthorizedOrderList();
        int getSpecUserOrderResponseStatusCode = getSpecNoAuthorizedUserOrderResponse.extract().statusCode();
        assertEquals(401, getSpecUserOrderResponseStatusCode);
        assertEquals("You should be authorised", getSpecNoAuthorizedUserOrderResponse.extract().path("message"));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
