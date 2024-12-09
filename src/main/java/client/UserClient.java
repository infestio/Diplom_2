package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {

    private static final String USER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String DELETE_UPDATE_INFO_PATH = "/api/auth/user";

    @Step("Create a user using endpoint /api/auth/register")
    public ValidatableResponse create(User user) {
        return  given()
                .log()
                .all()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Login the user using endpoint /api/auth/login")
    public ValidatableResponse login(UserCredentials userCredentials) {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(LOGIN_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Removing a user using accessToken")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE_UPDATE_INFO_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Update the user using endpoint /api/auth/user")
    public ValidatableResponse update(User user, String accessToken) {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(DELETE_UPDATE_INFO_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Update unauthorized user using endpoint /api/auth/user (without accessToken)")
    public ValidatableResponse noAuthorizedUpdate(User user) {
        return given()
                .log()
                .all()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(DELETE_UPDATE_INFO_PATH)
                .then()
                .log()
                .all();
    }

    @Step("Get information about an authorized user using endpoint /api/auth/user")
    public ValidatableResponse info(String accessToken) {
        return  given()
                .log()
                .all()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(DELETE_UPDATE_INFO_PATH)
                .then()
                .log()
                .all();
    }

}
