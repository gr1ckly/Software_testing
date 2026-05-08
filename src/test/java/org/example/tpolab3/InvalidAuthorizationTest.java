package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramLoginPage;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvalidAuthorizationTest extends BaseUiTest {

    @Test
    void invalidCredentialsShowError() {
        InstagramLoginPage loginPage = new InstagramLoginPage(driver, wait);
        loginPage.openHome();
        Assumptions.assumeTrue(
                loginPage.canUseLoginForm(),
                "Login-форма недоступна из-за anti-bot/login wall, тест пропускается"
        );
        loginPage.loginWithInvalidCredentials();
        Assertions.assertTrue(loginPage.isInvalidLoginErrorShown(), "Ожидалось сообщение об ошибке авторизации");
    }
}
