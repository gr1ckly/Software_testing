package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramNavigationPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OpenReelsPageTest extends BaseUiTest {

    @Test
    void reelsPageCanBeOpenedWithoutCrash() {
        InstagramNavigationPage navigationPage = new InstagramNavigationPage(driver, wait);
        Assertions.assertTrue(navigationPage.openReelsAndCheckPageLoaded());
    }
}
