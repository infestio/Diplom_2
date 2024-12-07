package usertest;

import client.User;
import client.UserClient;
import client.UserGenerator;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @Description("The user can be created, the request returns the correct response code, in the response body: success: true")
    public void createUserPositiveTest() {
        ValidatableResponse createResponse = userClient.create(user);
        int responseStatusCode = createResponse.extract().statusCode();
        assertEquals(200, responseStatusCode );
        boolean created = createResponse.extract().path("success");
        assertTrue(created);

        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse infoResponse = userClient.info(accessToken);
        assertEquals(user.getEmail(), infoResponse.extract().path("user.email"));
        assertEquals(user.getName(), infoResponse.extract().path("user.name"));

    }

    @Test
    @Description("You can't create two identical users, creating a user with a login that already exists returns an error")
    public void canNotCreateTwoIdenticalUserTest() {
        accessToken = userClient.create(user).extract().path("accessToken");
        ValidatableResponse createIdenticalUserResponse = userClient.create(user);
        int createIdenticalStatusCode = createIdenticalUserResponse.extract().statusCode();
        assertEquals(createIdenticalStatusCode, 403);
        String createIdenticalUserError = createIdenticalUserResponse.extract().path("message");
        assertEquals("User already exists", createIdenticalUserError );

    }

}
