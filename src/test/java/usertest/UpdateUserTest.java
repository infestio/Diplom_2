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


public class UpdateUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
    }

    @Test
    @Description("You can update the email of an authorized user, the field in the endpoint response body will be updated")
    public void updateEmailAuthorizedUserTest() {
        String email = "samson1234@yandex.ru";
        ValidatableResponse updateEmailResponse = userClient.update(User.updateEmail(email), accessToken);
        int updateEmailResponseStatusCode = updateEmailResponse.extract().statusCode();
        assertEquals(updateEmailResponseStatusCode, 200);
        assertEquals(email, updateEmailResponse.extract().path("user.email"));
        assertEquals(user.getName(), updateEmailResponse.extract().path("user.name"));
    }

    @Test
    @Description("You can update the name of an authorized user, the field in the endpoint response body will be updated")
    public void updateNameAuthorizedUserTest() {
        String name = "Ярославль";
        ValidatableResponse updateNameResponse = userClient.update(User.updateName(name), accessToken);
        int updateNameResponseStatusCode = updateNameResponse.extract().statusCode();
        assertEquals(updateNameResponseStatusCode, 200);
        assertEquals(name, updateNameResponse.extract().path("user.name"));
        assertEquals(user.getEmail(), updateNameResponse.extract().path("user.email"));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }


}
