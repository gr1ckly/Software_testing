package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramNavigationPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LanguageSelectorVisibilityTest extends BaseUiTest {

    @Test
    void languageSelectorIsVisibleOnLandingPage() {
        InstagramNavigationPage navigationPage = new InstagramNavigationPage(driver, wait);
        Assertions.assertTrue(navigationPage.isLanguageSelectorVisible(), "Селектор языка должен быть видим");
    }
}
