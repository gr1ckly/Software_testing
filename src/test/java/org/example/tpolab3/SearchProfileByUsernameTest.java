package org.example.tpolab3;

import org.example.tpolab3.pages.InstagramNavigationPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchProfileByUsernameTest extends BaseUiTest {

    @Test
    void searchConcreteProfileByUsername() {
        String targetUsername = "instagram";
        InstagramNavigationPage navigationPage = new InstagramNavigationPage(driver, wait);
        Assertions.assertTrue(
                navigationPage.searchProfile(targetUsername),
                "Профиль @" + targetUsername + " должен открываться после поиска"
        );
    }
}
