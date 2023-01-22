package DeleteUsersPojos;

import UsersAndPagesObjects.NewUser;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class API {

    private static final String URL = " https://stellarburgers.nomoreparties.site/";
    private static final String HANDLE = "api/auth";

    @Step("Создаём пользователя.")
    public static Response createUser(NewUser user) {
        return given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(user).when().post(URL + HANDLE + "/register");
    }

    @Step("Регистрируем пользователя.")
    public static Response loginUser(NewUser user) {
        return given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(user).when().post(URL + HANDLE + "/login");
    }

    @Step("Удаляем пользователя.")
    public static void deleteUser(String token) {
        given().contentType(ContentType.JSON).header("Authorization", token)
                .delete(URL + HANDLE + "/user").then().statusCode(202).and().body("success", equalTo(true));
    }
}
