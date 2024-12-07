package usertest;

import client.User;
import client.UserClient;
import client.UserCredentials;
import client.UserGenerator;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userClient.create(user);
    }

    @Test
    @Description("The user can log in, a successful endpoint returns true")
    public void loginUserTest() {
            ValidatableResponse loginUserResponse = userClient.login(UserCredentials.from(user));
            int loginUserResponseStatusCode = loginUserResponse.extract().statusCode();
            assertEquals(200, loginUserResponseStatusCode);
            boolean login = loginUserResponse.extract().path("success");
            assertTrue(login);

            accessToken = loginUserResponse.extract().path("accessToken");

    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
