import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import fordeleteuser.API;
import fordeleteuser.Login;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pageanduser.AuthorizationPage;
import pageanduser.NewUser;
import pageanduser.RegistrationPage;
import pageanduser.Url;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.apache.http.HttpStatus.SC_OK;

public class RegistrationTest {
    private NewUser newUser;
    private RegistrationPage registrationPage;

    @Before
    public void Preconditions() {
        newUser = NewUser.getRandomUser();
        registrationPage = open(Url.URL_REGISTRATION, RegistrationPage.class);
    }


    @Test
    @DisplayName("Проверяем успешность регистрации")
    public void checkRegistration() {
        registrationPage.registration(newUser.getName(), newUser.getEmail(), newUser.getPassword());
        AuthorizationPage authorizationPage = page(AuthorizationPage.class);
        //проверяем, что кнопку "Войти" появилась на экране
        authorizationPage.getButtonAuthorization().shouldBe(Condition.visible);
        //удаление пользователя
        NewUser oldUser = new NewUser(newUser.getEmail(), newUser.getPassword());
        Response response = API.loginUser(oldUser);
        String accessToken = response.then().statusCode(SC_OK).extract().body().as(Login.class).getAccessToken();
        API.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Проверяем ошибки если пароль невалиден.")
    public void checkDefectRegistration() {
        registrationPage.registration(newUser.getName(), newUser.getEmail(), RandomStringUtils.randomAlphabetic(5));
        registrationPage.getTextError().shouldBe(Condition.visible);
    }

    @After
    public void Postconditions() {
        Selenide.clearBrowserCookies();
    }
}
