package usertest;

import client.User;
import client.UserClient;
import client.UserGenerator;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UpdateUserNegativeTest {

    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        User user = UserGenerator.getRandom();
        accessToken = userClient.create(user).extract().path("accessToken");
    }

    @Test
    @Description("Unable to update email of unauthorized user, endpoint will return error")
    public void updateEmailNotAuthorizedUserTest() {
        String email = "samson1234@yandex.ru";
        ValidatableResponse updateEmailResponse = userClient.noAuthorizedUpdate(User.updateEmail(email));
        int updateEmailResponseStatusCode = updateEmailResponse.extract().statusCode();
        assertEquals(401, updateEmailResponseStatusCode);
        String updateEmailResponseError = updateEmailResponse.extract().path("message");
        assertEquals("You should be authorised", updateEmailResponseError);
    }

    @Test
    @Description("Unable to update email of unauthorized user, endpoint will return error")
    public void updateNameNotAuthorizedUserTest() {
        String name = "Homer";
        ValidatableResponse updateNameResponse = userClient.noAuthorizedUpdate(User.updateName(name));
        int updateNameResponseStatusCode = updateNameResponse.extract().statusCode();
        assertEquals(401, updateNameResponseStatusCode);
        String updateNameResponseError = updateNameResponse.extract().path("message");
        assertEquals("You should be authorised", updateNameResponseError);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

}
