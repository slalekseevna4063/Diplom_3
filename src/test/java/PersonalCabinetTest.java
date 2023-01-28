import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import fordeleteuser.API;
import fordeleteuser.Login;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pageanduser.*;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.apache.http.HttpStatus.SC_OK;

public class PersonalCabinetTest {
    private NewUser newUser;
    private MainPage mainPage;
    private AuthorizationPage authorizationPage;
    private String accessToken;
    private boolean yandex = false; // Если надо тесты запустить в Яндекс браузере, то переменная - true, для Хрома - false

    @Before
    public void Preconditions() {
        newUser = NewUser.getRandomUser();
        Response responseCreate = API.createUser(newUser);
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(Login.class).getAccessToken();
    }

    @Test
    @DisplayName("Проверка перехода по клику на «Личный кабинет». Неавторизованный пользователь.")
    public void checkPersonalCabinetLogout() {
        mainPage = open(Url.URL_BASE, MainPage.class);
        mainPage.clickButtonPersonal();
        authorizationPage = page(AuthorizationPage.class);
        //проверяем, что кнопка "Войти" появилась на экране
        authorizationPage.getButtonAuthorization().shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Проверка перехода по клику на «Личный кабинет». Авторизованный пользователь.")
    public void checkPersonalCabinetLogin() {
        authorizationPage = open(Url.URL_LOGIN, AuthorizationPage.class);
        authorizationPage.login(newUser.getEmail(), newUser.getPassword());
        mainPage = page(MainPage.class);
        mainPage.clickButtonPersonal();
        ProfilePage profilePage = page(ProfilePage.class);
        //проверяем, что кнопку "Выйти" появилась на экране
        profilePage.getButtonExit().shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Проверка перехода по клику на «Конструктор»")
    public void checkGoToConstructor() {
        authorizationPage = open(Url.URL_LOGIN, AuthorizationPage.class);
        authorizationPage.login(newUser.getEmail(), newUser.getPassword());
        mainPage = page(MainPage.class);
        mainPage.clickButtonPersonal();
        ProfilePage profilePage = page(ProfilePage.class);
        profilePage.clickConstructor();
        //проверяем, что кнопку "Оформить заказ" появилась на экране
        mainPage.getButtonMakeOrder().shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Проверка перехода по клику на логотип Stellar Burgers.")
    public void checkGoToLogo() {
        authorizationPage = open(Url.URL_LOGIN, AuthorizationPage.class);
        authorizationPage.login(newUser.getEmail(), newUser.getPassword());
        mainPage = page(MainPage.class);
        mainPage.clickButtonPersonal();
        ProfilePage profilePage = page(ProfilePage.class);
        profilePage.clickLogo();
        //проверяем, что кнопку "Оформить заказ" появилась на экране
        mainPage.getButtonMakeOrder().shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Проверка выхода по кнопке «Выйти» в личном кабинете.")
    public void check() {
        authorizationPage = open(Url.URL_LOGIN, AuthorizationPage.class);
        authorizationPage.login(newUser.getEmail(), newUser.getPassword());
        mainPage = page(MainPage.class);
        mainPage.clickButtonPersonal();
        ProfilePage profilePage = page(ProfilePage.class);
        profilePage.clickButtonExit();
        //проверяем, что кнопку "Войти" появилась на экране
        authorizationPage.getButtonAuthorization().shouldBe(Condition.visible);
    }

    @After
    public void Postconditions() {
        API.deleteUser(accessToken);
        Selenide.clearBrowserCookies();
    }
}
