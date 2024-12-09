package usertest;

import client.User;
import client.UserClient;
import client.UserCredentials;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LoginUserNegativeTest {

    private UserClient userClient;
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        User user = new User("infestberserk01@yandex.ru", "Qwerty1234", "Вася");
        accessToken = userClient.create(user).extract().path("accessToken");
    }

    private final String email;
    private final String password;



    public LoginUserNegativeTest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getLoginParam() {
        return new Object[][] {
                {"infest@mail.ru", "Qwerty1234"},
                {"infestberserk01@yandex.ru", "Silver"},
                {"infest@mail.ru", "Silver"},
                {null, "Qwerty1234"},
                {"infestberserk01@yandex.ru", null},
        };
    }

    @Test
    @Description("The test implements all negative scenarios for the login endpoint on the task")
    public void loginUserIncorrectParamTest() {
        ValidatableResponse incorrectLoginResponse = userClient.login(new UserCredentials(email, password));
        int incorrectLoginResponseStatusCode = incorrectLoginResponse.extract().statusCode();
        assertEquals(401, incorrectLoginResponseStatusCode);
        String incorrectLoginResponseError = incorrectLoginResponse.extract().path("message");
        assertEquals("email or password are incorrect", incorrectLoginResponseError);

        if (incorrectLoginResponseStatusCode == 200) {
            String accessToken = incorrectLoginResponse.extract().path("accessToken");
            userClient.delete(accessToken);
        }

    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

}
