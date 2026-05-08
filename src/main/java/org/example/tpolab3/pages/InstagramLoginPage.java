package org.example.tpolab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class InstagramLoginPage extends BasePage {
    private final By usernameInput = By.xpath("//input[@name='username' or @autocomplete='username']");
    private final By passwordInput = By.xpath("//input[@name='password']");
    private final By submitButton = By.xpath("//button[@type='submit']");
    private final By signUpLink = By.xpath("//a[contains(@href, '/accounts/emailsignup/')]");
    private final By forgotPasswordLink = By.xpath("//a[contains(@href, '/accounts/password/reset/')]");
    private final By loginError = By.xpath("//p[contains(text(), 'incorrect') or contains(text(), 'невер')]");

    public InstagramLoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void openHome() {
        navigate("https://www.instagram.com/");
    }

    public boolean isLoginFormVisible() {
        return canUseLoginForm();
    }

    public boolean canUseLoginForm() {
        return !driver.findElements(usernameInput).isEmpty()
                && !driver.findElements(passwordInput).isEmpty()
                && !driver.findElements(submitButton).isEmpty();
    }

    public boolean isInstagramLandingLoaded() {
        String url = driver.getCurrentUrl();
        String title = driver.getTitle().toLowerCase();
        return url.contains("instagram.com") && title.contains("instagram");
    }

    public void loginWithInvalidCredentials() {
        type(usernameInput, "invalid_user_automation");
        type(passwordInput, "invalid_password_automation");
        click(submitButton);
    }

    public void login(String username, String password) {
        By appNavigation = By.xpath("//nav");
        navigate("https://www.instagram.com/");
        if (!driver.findElements(appNavigation).isEmpty() && driver.findElements(usernameInput).isEmpty()) {
            return;
        }

        navigate("https://www.instagram.com/accounts/login/");
        if (!driver.findElements(appNavigation).isEmpty() && driver.findElements(usernameInput).isEmpty()) {
            return;
        }

        By alternativeUsername = By.xpath("//input[@type='text' and (@name='username' or contains(@aria-label, 'Phone') or contains(@aria-label, 'Телефон'))]");
        if (driver.findElements(usernameInput).isEmpty() && !driver.findElements(alternativeUsername).isEmpty()) {
            type(alternativeUsername, username);
        } else {
            type(usernameInput, username);
        }
        type(passwordInput, password);
        click(submitButton);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/accounts/onetap/"),
                ExpectedConditions.urlContains("/challenge/"),
                ExpectedConditions.urlContains("/"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//nav"))
        ));
    }

    public void dismissSaveLoginInfoDialogIfPresent() {
        By notNowButton = By.xpath("//button[normalize-space()='Not now' or normalize-space()='Не сейчас']");
        if (!driver.findElements(notNowButton).isEmpty()) {
            click(notNowButton);
        }
    }

    public void dismissTurnOnNotificationsDialogIfPresent() {
        By notNowButton = By.xpath("//button[normalize-space()='Not Now' or normalize-space()='Not now' or normalize-space()='Не сейчас']");
        if (!driver.findElements(notNowButton).isEmpty()) {
            click(notNowButton);
        }
    }

    public boolean isInvalidLoginErrorShown() {
        List<?> matches = driver.findElements(loginError);
        return !matches.isEmpty();
    }

    public void openSignUpPage() {
        if (driver.findElements(signUpLink).isEmpty()) {
            navigate("https://www.instagram.com/accounts/emailsignup/");
        } else {
            click(signUpLink);
        }
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/accounts/emailsignup"),
                ExpectedConditions.urlContains("/accounts/login")
        ));
    }

    public void openForgotPasswordPage() {
        if (driver.findElements(forgotPasswordLink).isEmpty()) {
            navigate("https://www.instagram.com/accounts/password/reset/");
        } else {
            click(forgotPasswordLink);
        }
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/accounts/password/reset"),
                ExpectedConditions.urlContains("/accounts/login")
        ));
    }

    public boolean isSignUpFlowOpened() {
        String url = driver.getCurrentUrl();
        return url.contains("/accounts/emailsignup")
                || (url.contains("/accounts/login") && (url.contains("emailsignup") || url.contains("signup")));
    }

    public boolean isPasswordResetFlowOpened() {
        String url = driver.getCurrentUrl();
        return url.contains("/accounts/password/reset")
                || (url.contains("/accounts/login") && (url.contains("password") || url.contains("reset")));
    }
}
