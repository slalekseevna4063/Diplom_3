import com.codeborne.selenide.Selenide;
import fordeleteuser.API;
import fordeleteuser.Login;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pageanduser.MainPage;
import pageanduser.NewUser;
import pageanduser.Url;

import static com.codeborne.selenide.Selenide.open;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ConstructorTest {
    private NewUser newUser;
    private MainPage mainPage;
    private String accessToken;

    @Before
    public void Preconditions() {
        newUser = NewUser.getRandomUser();
        Response responseCreate = API.createUser(newUser);
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(Login.class).getAccessToken();
    }

    @Test
    @DisplayName("Проверка перехода к разделу «Соусы».")
    public void checkSauces() {
        mainPage = open(Url.URL_BASE, MainPage.class);
        // Проверяем, что кнопка не активна и мы не находимся на данном разделе
        assertNotEquals(MainPage.ACTIVE_SAUCES, mainPage.getAttributeSauces());
        mainPage.clickButtonSauces();
        // Проверяем, что после клика, кнопка активна и мы находимся на данном разделе
        assertEquals(MainPage.ACTIVE_SAUCES, mainPage.getAttributeSauces());
    }

    @Test
    @DisplayName("Проверка перехода к разделу «Начинки».")
    public void checkToppings() {
        mainPage = open(Url.URL_BASE, MainPage.class);
        // Проверяем, что кнопка не активна и мы не находимся на данном разделе
        assertNotEquals(MainPage.ACTIVE_TOPPINGS, mainPage.getAttributeToppings());
        mainPage.clickButtonToppings();
        // Проверяем, что после клика, кнопка активна и мы находимся на данном разделе
        assertEquals(MainPage.ACTIVE_TOPPINGS, mainPage.getAttributeToppings());
    }

    @Test
    @DisplayName("Проверка перехода к разделу «Булки».")
    public void checkBuns() {
        mainPage = open(Url.URL_BASE, MainPage.class);
        // Проверяем, что после клика, кнопка активна и мы находимся на данном разделе
        assertEquals(MainPage.ACTIVE_BUNS, mainPage.getAttributeBuns());
    }

    @After
    public void Postconditions() {
        API.deleteUser(accessToken);
        Selenide.clearBrowserCookies();
    }
}
