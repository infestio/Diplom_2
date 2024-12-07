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

public class GetSpecificUserOrderTest {

    private UserClient userClient;
    private String accessToken;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        User user = UserGenerator.getRandom();
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        orderClient = new OrderClient();
    }

    @Test
    @Description("Can get the order details of a specific authorized user")
    public void getSpecificUserOrderListTest() {
        List<Map<String, String>> listIngredients = orderClient.ingredients().extract().path("data");
        String[] ingredients = {listIngredients.get(3).get("_id"), listIngredients.get(5).get("_id")};
        Order order = new Order(ingredients);
        orderClient.createOrder(order, accessToken);
        ValidatableResponse getSpecUserOrderResponse = orderClient.getSpecificOrderList(accessToken);
        int getSpecUserOrderResponseStatusCode = getSpecUserOrderResponse.extract().statusCode();
        assertEquals(200, getSpecUserOrderResponseStatusCode);
        boolean ordered = getSpecUserOrderResponse.extract().path("success");
        assertTrue(ordered);
        assertNotNull(getSpecUserOrderResponse.extract().path("orders"));

    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

}
