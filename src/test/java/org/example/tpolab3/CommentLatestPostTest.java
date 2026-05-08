package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramAccountActionsPage;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommentLatestPostTest extends AuthenticatedManualSessionTestBase {

    @Test
    void commentLatestPostForTargetProfile() {
        requireAuthenticatedManualSessionOrSkip();
        String targetUsername = getTargetUsername();
        String targetPostUrl = getTargetPostUrl();
        String commentText = "класс " + System.currentTimeMillis();

        InstagramAccountActionsPage actionsPage = new InstagramAccountActionsPage(driver, wait);
        if (!targetPostUrl.isBlank()) {
            actionsPage.openPostByUrl(targetPostUrl);
        } else {
            actionsPage.openProfile(targetUsername);
            Assumptions.assumeTrue(
                    actionsPage.hasVisiblePostsOnOpenedProfile(),
                    "У профиля @" + targetUsername + " нет доступных публикаций для комментария."
            );
            actionsPage.openLatestPostFromProfile();
        }

        Assertions.assertTrue(actionsPage.addCommentToCurrentPost(commentText), "Комментарий должен быть опубликован");
    }
}
