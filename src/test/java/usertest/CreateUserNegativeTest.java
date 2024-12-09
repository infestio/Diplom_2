package usertest;

import client.User;
import client.UserClient;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateUserNegativeTest {

    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    private final String email;
    private final String password;
    private final String name;

    public CreateUserNegativeTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getUserParam() {
        return new Object[][] {
                {"", "Qwerty1234", "Вася"},
                {"infestberserk01@yandex.ru", "", "Вася"},
                {"infestberserk01@yandex.ru", "Qwerty1234", ""},
                {null, "Qwerty1234", "Вася"},
                {"infestberserk01@yandex.ru", null, "Вася"},
                {"infestberserk01@yandex.ru", "Qwerty1234", null},
        };
    }

    @Test
    @Description("If one of the fields (login/password/name) is missing or empty, the endpoint returns an error")
    public void createUserWithoutOneOfParamTest() {
        User user = new User(email, password, name);
        ValidatableResponse createNegativeResponse = userClient.create(user);
        int createNegativeResponseStatusCode = createNegativeResponse.extract().statusCode();
        assertEquals(createNegativeResponseStatusCode,403);
        String createNegativeResponseError = createNegativeResponse.extract().path("message");
        assertEquals("Email, password and name are required fields", createNegativeResponseError);

        if (createNegativeResponseStatusCode == 200) {
            String accessToken = createNegativeResponse.extract().path("accessToken");
            userClient.delete(accessToken);
        }
    }


}
