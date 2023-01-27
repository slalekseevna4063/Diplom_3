package pageanduser;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ResetPasswordPage {
    //локатор кнопки "Войти"
    @FindBy(how = How.CSS, using = ".Auth_link__1fOlj")
    private SelenideElement buttonAuthorization;

    @Step("Клик по кнопке 'Войти'")
    public void clickButtonAuthorization() {
        buttonAuthorization.click();
    }
}
