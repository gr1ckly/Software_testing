package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramLoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OpenPasswordResetPageTest extends BaseUiTest {

    @Test
    void passwordResetPageCanBeOpened() {
        InstagramLoginPage loginPage = new InstagramLoginPage(driver, wait);
        loginPage.openHome();
        loginPage.openForgotPasswordPage();
        Assertions.assertTrue(loginPage.isPasswordResetFlowOpened());
    }
}
