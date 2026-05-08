package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramLoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginFormVisibilityTest extends BaseUiTest {

    @Test
    void loginFormIsVisibleOnLandingPage() {
        InstagramLoginPage loginPage = new InstagramLoginPage(driver, wait);
        loginPage.openHome();
        Assertions.assertTrue(
                loginPage.isLoginFormVisible() || loginPage.isInstagramLandingLoaded(),
                "Должна открываться страница Instagram с логин-формой или login wall"
        );
    }
}
