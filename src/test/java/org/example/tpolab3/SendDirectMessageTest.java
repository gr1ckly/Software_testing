package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramAccountActionsPage;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SendDirectMessageTest extends AuthenticatedManualSessionTestBase {

    @Test
    void sendDirectMessageToTargetProfile() {
        requireAuthenticatedManualSessionOrSkip();
        String targetUsername = getTargetUsername();
        String messageText = "класс " + System.currentTimeMillis();

        InstagramAccountActionsPage actionsPage = new InstagramAccountActionsPage(driver, wait);
        actionsPage.openProfile(targetUsername);
        Assumptions.assumeTrue(
                actionsPage.canMessageOpenedProfile(),
                "Для профиля @" + targetUsername + " недоступна кнопка Message."
        );
        Assertions.assertTrue(
                actionsPage.sendDirectMessageFromProfile(targetUsername, messageText),
                "Сообщение в direct должно быть отправлено"
        );
    }
}
