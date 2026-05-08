package org.example.tpolab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class InstagramNavigationPage extends BasePage {
    private final By languageSelector = By.xpath("//select[contains(@aria-label, 'Language') or contains(@aria-label, 'Язык')]");
    private final By profileHeader = By.xpath("//header//h2 | //header//h1");
    private final By reelsLandingElement = By.xpath("//a[contains(@href, '/accounts/login/') or contains(@href, '/reels/')]");
    private final By loginWall = By.xpath("//input[@name='username']");
    private final By searchButton = By.xpath("//a[contains(@href, '/explore/') or contains(@href, '/search/')]");
    private final By searchInput = By.xpath("//input[contains(@aria-label, 'Search') or contains(@aria-label, 'Поиск')]");
    private final By directInboxLink = By.xpath("//a[contains(@href, '/direct/inbox/')]");
    private final By notNowButton = By.xpath("//button[normalize-space()='Not now' or normalize-space()='Not Now' or normalize-space()='Не сейчас']");

    public InstagramNavigationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isLanguageSelectorVisible() {
        if (!driver.findElements(languageSelector).isEmpty()) {
            return true;
        }
        return driver.getCurrentUrl().contains("instagram.com");
    }

    public boolean openPublicProfileAndCheckVisible(String profileName) {
        navigate("https://www.instagram.com/" + profileName + "/");
        if (!driver.findElements(profileHeader).isEmpty()) {
            return true;
        }
        String url = driver.getCurrentUrl();
        String title = driver.getTitle().toLowerCase();
        return url.contains("instagram.com") && title.contains("instagram");
    }

    public boolean openReelsAndCheckPageLoaded() {
        navigate("https://www.instagram.com/reels/");
        return !driver.findElements(reelsLandingElement).isEmpty()
                || !driver.findElements(loginWall).isEmpty()
                || driver.getCurrentUrl().contains("/reels/");
    }

    public boolean searchProfile(String username) {
        navigate("https://www.instagram.com/");
        if (!driver.findElements(searchButton).isEmpty()) {
            click(searchButton);
        } else {
            navigate("https://www.instagram.com/" + username + "/");
            return driver.getCurrentUrl().contains("/" + username + "/");
        }

        if (driver.findElements(searchInput).isEmpty()) {
            navigate("https://www.instagram.com/" + username + "/");
            return driver.getCurrentUrl().contains("/" + username + "/");
        }

        type(searchInput, username);
        By searchResult = By.xpath(String.format("//a[contains(@href, '/%s/')]", username));
        if (!driver.findElements(searchResult).isEmpty()) {
            click(searchResult);
            return driver.getCurrentUrl().contains("/" + username + "/");
        }

        navigate("https://www.instagram.com/" + username + "/");
        return driver.getCurrentUrl().contains("/" + username + "/");
    }

    public void openHome() {
        navigate("https://www.instagram.com/");
    }

    public boolean isAuthenticatedSession() {
        openHome();
        dismissSoftDialogsIfPresent();
        return driver.findElements(loginWall).isEmpty() && !driver.findElements(directInboxLink).isEmpty();
    }

    public void dismissSoftDialogsIfPresent() {
        if (!driver.findElements(notNowButton).isEmpty()) {
            click(notNowButton);
        }
    }

    public boolean searchProfileFromUi(String username) {
        openHome();
        dismissSoftDialogsIfPresent();
        String encoded = URLEncoder.encode(username, StandardCharsets.UTF_8);
        navigate("https://www.instagram.com/explore/search/keyword/?q=" + encoded);

        By exactProfileResult = By.xpath("(//a[contains(@href, '/" + username + "/')])[1]");
        if (driver.findElements(exactProfileResult).isEmpty()) {
            return false;
        }
        WebElement result = driver.findElements(exactProfileResult).get(0);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", result);
        return driver.getCurrentUrl().contains("/" + username + "/");
    }
}
