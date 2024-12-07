package usertest;

import client.User;
import client.UserClient;
import client.UserGenerator;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeleteUserTest {

    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
    }

    @Test
    @Description("This test only checks the operation of the Delete endpoint, the test does not apply to the additional task")
    public void deleteUserTest() {
        ValidatableResponse createUserResponse = userClient.create(user);
        String accessToken = createUserResponse.extract().path("accessToken");
        ValidatableResponse deleteUserResponse = userClient.delete(accessToken);
        int deleteUserResponseStatusCode = deleteUserResponse.extract().statusCode();
        assertEquals(202, deleteUserResponseStatusCode);
        boolean deleted = deleteUserResponse.extract().path("success");
        assertTrue(deleted);

    }
}
