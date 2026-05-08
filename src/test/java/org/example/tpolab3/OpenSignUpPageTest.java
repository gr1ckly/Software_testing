package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramLoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OpenSignUpPageTest extends BaseUiTest {

    @Test
    void signUpPageCanBeOpenedFromLoginPage() {
        InstagramLoginPage loginPage = new InstagramLoginPage(driver, wait);
        loginPage.openHome();
        loginPage.openSignUpPage();
        Assertions.assertTrue(loginPage.isSignUpFlowOpened());
    }
}
