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

public class CreateOrderTest {

    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private List<Map<String, String>> listIngredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        orderClient = new OrderClient();
        listIngredients = orderClient.ingredients().extract().path("data");
    }

    @Test
    @Description("Can create an order for an authorized user, the endpoint will return the correct status code, and the response body will contain the correct user name.")
    public void createPositiveOrderTest() {
        String [] ingredients = {listIngredients.get(0).get("_id"), listIngredients.get(2).get("_id")};
        Order order = new Order(ingredients);
        ValidatableResponse createOrderResponse = orderClient.createOrder(order, accessToken);
        int createOrderResponseStatusCode = createOrderResponse.extract().statusCode();
        assertEquals(200, createOrderResponseStatusCode);
        boolean orderCreated = createOrderResponse.extract().path("success");
        assertTrue(orderCreated);
        String createOrderOwnerName = createOrderResponse.extract().path("order.owner.name");
        assertEquals(user.getName(), createOrderOwnerName);

    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
