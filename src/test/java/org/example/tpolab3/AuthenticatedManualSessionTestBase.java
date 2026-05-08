package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramNavigationPage;
import org.junit.jupiter.api.Assumptions;

public abstract class AuthenticatedManualSessionTestBase extends BaseUiTest {

    protected static final String DEFAULT_TARGET_USERNAME = "instagram";

    protected void requireAuthenticatedManualSessionOrSkip() {
        String debuggerAddress = System.getProperty("chrome.debuggerAddress", "").trim();
        Assumptions.assumeTrue(
                !debuggerAddress.isEmpty(),
                "Для этих тестов нужен attach к ручной сессии Chrome: -Dchrome.debuggerAddress=127.0.0.1:9222"
        );

        InstagramNavigationPage navigationPage = new InstagramNavigationPage(driver, wait);
        Assumptions.assumeTrue(
                navigationPage.isAuthenticatedSession(),
                "Сессия не авторизована. Войдите вручную в Instagram в debug-окне Chrome и перезапустите тест."
        );
    }

    protected String getTargetUsername() {
        return System.getProperty("target.username", DEFAULT_TARGET_USERNAME).trim();
    }

    protected String getTargetPostUrl() {
        return System.getProperty("target.postUrl", "").trim();
    }
}
