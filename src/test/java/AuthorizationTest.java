import DeleteUsersPojos.API;
import DeleteUsersPojos.Login;
import UsersAndPagesObjects.*;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.apache.http.HttpStatus.SC_OK;

public class AuthorizationTest {
    NewUser newUser;
    MainPage mainPage;
    AuthorizationPage authorizationPage;
    String accessToken;


    @Before
    public void Preconditions() {
        newUser = NewUser.getRandomUser();
        Response responseCreate = API.createUser(newUser);
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(Login.class).getAccessToken();
    }

    @Test
    @DisplayName("Вход по кнопке «Войти в аккаунт» на главной странице.")
    public void checkLoginFromMainPage() {
        mainPage = open(MainPage.URL, MainPage.class);
        mainPage.clickButtonLogIntoAccount();
        authorizationPage = page(AuthorizationPage.class);
    }

    @Test
    @DisplayName("Вход через кнопку «Личный кабинет».")
    public void checkLoginPersonalCabinet() {
        mainPage = open(MainPage.URL, MainPage.class);
        mainPage.clickButtonPersonal();
        authorizationPage = page(AuthorizationPage.class);
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации.")
    public void checkLoginRegistrationForm() {
        RegistrationPage registrationPage = open(RegistrationPage.URL, RegistrationPage.class);
        registrationPage.clickButtonLogin();
        authorizationPage = page(AuthorizationPage.class);
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля, заполнение первой формы")
    public void checkLoginForgotPassword() {
        authorizationPage = open(AuthorizationPage.URL, AuthorizationPage.class);
        authorizationPage.clickButtonRestorePassword();
        ForgotPasswordPage forgotPasswordPage = page(ForgotPasswordPage.class);
        forgotPasswordPage.clickButtonAuthorization();
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля, заполнение второй формы")
    public void checkLoginResetPassword() {
        authorizationPage = open(AuthorizationPage.URL, AuthorizationPage.class);
        authorizationPage.clickButtonRestorePassword();
        ForgotPasswordPage forgotPasswordPage = page(ForgotPasswordPage.class);
        forgotPasswordPage.restore(newUser.getEmail());
        ResetPasswordPage resetPasswordPage = page(ResetPasswordPage.class);
        resetPasswordPage.clickButtonAuthorization();
    }

    @After
    public void Postconditions() {
        authorizationPage.login(newUser.getEmail(), newUser.getPassword());
        if (mainPage == null) {
            mainPage = page(MainPage.class);
        }
        mainPage.getButtonMakeOrder().shouldBe(Condition.visible);
        API.deleteUser(accessToken);
        Selenide.clearBrowserCookies();
    }
}
