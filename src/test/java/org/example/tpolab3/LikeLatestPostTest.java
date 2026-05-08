package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramAccountActionsPage;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LikeLatestPostTest extends AuthenticatedManualSessionTestBase {

    @Test
    void likeLatestPostForTargetProfile() {
        requireAuthenticatedManualSessionOrSkip();
        String targetUsername = getTargetUsername();
        String targetPostUrl = getTargetPostUrl();

        InstagramAccountActionsPage actionsPage = new InstagramAccountActionsPage(driver, wait);
        if (!targetPostUrl.isBlank()) {
            actionsPage.openPostByUrl(targetPostUrl);
        } else {
            actionsPage.openProfile(targetUsername);
            Assumptions.assumeTrue(
                    actionsPage.hasVisiblePostsOnOpenedProfile(),
                    "У профиля @" + targetUsername + " нет доступных публикаций для лайка."
            );
            actionsPage.openLatestPostFromProfile();
        }

        Assertions.assertTrue(
                actionsPage.toggleLikeAndRestoreInitialState(),
                "Лайк должен переключиться и вернуться в исходное состояние"
        );
    }
}
