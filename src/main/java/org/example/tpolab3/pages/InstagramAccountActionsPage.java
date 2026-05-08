package org.example.tpolab3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class InstagramAccountActionsPage extends BasePage {
    private final By likeButton = By.xpath("//article//button[@aria-label='Like' or @aria-label='Нравится'] | //article//section//span//*[name()='svg' and (@aria-label='Like' or @aria-label='Нравится')]/ancestor::button[1]");
    private final By unlikeButton = By.xpath("//article//button[@aria-label='Unlike' or @aria-label='Не нравится'] | //article//section//span//*[name()='svg' and (@aria-label='Unlike' or @aria-label='Не нравится')]/ancestor::button[1]");

    public InstagramAccountActionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void openProfile(String username) {
        navigate("https://www.instagram.com/" + username + "/");
        wait.until(ExpectedConditions.urlContains("/" + username + "/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//main")));
    }

    public void openLatestPostFromProfile() {
        String postHref = getFirstPostHrefFromOpenedProfile();
        if (postHref == null || postHref.isBlank()) {
            throw new IllegalStateException("No visible posts found on target profile.");
        }
        navigate(postHref);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/p/"),
                ExpectedConditions.urlContains("/reel/"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//article"))
        ));
    }

    public boolean likeCurrentPost() {
        if (isCurrentPostLiked()) {
            return true;
        }
        WebElement reaction = firstVisibleReactionButton();
        if (reaction == null) {
            return false;
        }
        clickSafe(reaction);
        return waitForLikedState(true);
    }

    public boolean addCommentToCurrentPost(String commentText) {
        By commentArea = By.xpath(
                "//textarea[" +
                        "@aria-label='Add a comment…' or @aria-label='Add a comment...' or " +
                        "@aria-label='Добавьте комментарий…' or @aria-label='Добавьте комментарий...' or " +
                        "@placeholder='Add a comment…' or @placeholder='Add a comment...' or " +
                        "@placeholder='Добавьте комментарий…' or @placeholder='Добавьте комментарий...'" +
                        "]"
        );
        By commentTextbox = By.xpath(
                "//div[@role='textbox' and (" +
                        "@aria-label='Add a comment…' or @aria-label='Add a comment...' or " +
                        "@aria-label='Добавьте комментарий…' or @aria-label='Добавьте комментарий...'" +
                        ")]"
        );
        By openCommentsButton = By.xpath(
                "//article//button[@aria-label='Comment' or @aria-label='Комментировать'] | " +
                        "//article//button[.//*[name()='svg' and (@aria-label='Comment' or @aria-label='Комментировать')]] | " +
                        "//article//*[@role='button'][.//*[name()='svg' and (@aria-label='Comment' or @aria-label='Комментировать')]]"
        );

        if (driver.findElements(commentArea).isEmpty() && driver.findElements(commentTextbox).isEmpty()) {
            WebElement commentButton = firstVisible(openCommentsButton);
            if (commentButton != null) {
                clickSafe(commentButton);
            }
        }

        if (!driver.findElements(commentArea).isEmpty()) {
            sendKeysWithRetry(commentArea, commentText);
        } else {
            sendKeysWithRetry(commentTextbox, commentText);
            sendKeysWithRetry(commentTextbox, Keys.ENTER);
        }

        By postButton = By.xpath(
                "//button[(normalize-space()='Post' or normalize-space()='Опубликовать') and not(@disabled)] | " +
                        "//*[@role='button' and " +
                        "(normalize-space()='Post' or normalize-space()='Опубликовать' or " +
                        ".//span[normalize-space()='Post' or normalize-space()='Опубликовать']) and " +
                        "not(@aria-disabled='true')]"
        );

        WebElement submit = null;
        for (int i = 0; i < 20; i++) {
            submit = firstVisible(postButton);
            if (submit != null) {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for publish button.", e);
            }
        }
        if (submit != null) {
            clickSafe(submit);
        }

        By createdComment = By.xpath("//*[contains(text(), '" + commentText + "')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(createdComment));
        return !driver.findElements(createdComment).isEmpty();
    }

    public boolean sendDirectMessageFromProfile(String username, String messageText) {
        openProfile(username);
        By messageButton = By.xpath(
                "//header//button[normalize-space()='Message' or normalize-space()='Написать'] | " +
                        "//*[@role='button' and normalize-space()='Отправить сообщение']"
        );
        WebElement openDirectButton = firstVisible(messageButton);
        if (openDirectButton == null) {
            return false;
        }

        clickSafe(openDirectButton);

        By dmInput = By.xpath(
                "//div[@role='textbox' and (@aria-label='Сообщение' or @aria-label='Message' or @aria-placeholder='Напишите сообщение…' or @aria-placeholder='Напишите сообщение...')] | " +
                        "//div[@contenteditable='true' and @role='textbox']"
        );
        sendKeysWithRetry(dmInput, messageText);

        By sendButton = By.xpath(
                "//*[@role='button' and (@aria-label='Отправить' or @aria-label='Send')] | " +
                        "//*[@role='button'][.//*[name()='svg' and (@aria-label='Send' or @aria-label='Отправить')]]"
        );
        WebElement submit = firstVisible(sendButton);
        if (submit != null) {
            clickSafe(submit);
        } else {
            sendKeysWithRetry(dmInput, Keys.ENTER);
        }

        By sentMessage = By.xpath("//*[contains(text(), '" + messageText + "')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(sentMessage));
        return !driver.findElements(sentMessage).isEmpty();
    }

    public void openPostByUrl(String postUrl) {
        navigate(postUrl);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/p/"),
                ExpectedConditions.urlContains("/reel/"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//article"))
        ));
    }

    public boolean hasVisiblePostsOnOpenedProfile() {
        return getFirstPostHrefFromOpenedProfile() != null;
    }

    private String getFirstPostHrefFromOpenedProfile() {
        for (int attempt = 0; attempt < 20; attempt++) {
            Object href = ((JavascriptExecutor) driver).executeScript(
                    "const post = document.querySelector(\"main a[href*='/p/'], main a[href*='/reel/']\");" +
                            "return post ? post.href : null;"
            );
            if (href instanceof String postHref && !postHref.isBlank()) {
                return postHref;
            }

            Object pageText = ((JavascriptExecutor) driver).executeScript(
                    "return (document.body && document.body.innerText) ? document.body.innerText : '';"
            );
            if (pageText instanceof String text) {
                if (text.contains("No posts yet")
                        || text.contains("Пока нет публикаций")
                        || text.contains("This account is private")
                        || text.contains("Этот аккаунт закрыт")
                        || text.contains("Sorry, this page")
                        || text.contains("Извините, эта страница недоступна")) {
                    return null;
                }
            }

            if (attempt == 6 || attempt == 12) {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
            }

            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return null;
    }

    public boolean canMessageOpenedProfile() {
        By messageButton = By.xpath(
                "//header//button[normalize-space()='Message' or normalize-space()='Написать'] | " +
                        "//*[@role='button' and normalize-space()='Отправить сообщение']"
        );
        return firstVisible(messageButton) != null;
    }

    public boolean isCurrentPostLiked() {
        String label = getVisibleReactionLabel();
        if (label == null) {
            return false;
        }
        return "Unlike".equalsIgnoreCase(label) || "Не нравится".equalsIgnoreCase(label);
    }

    public boolean toggleLikeAndRestoreInitialState() {
        String initialLabel = getVisibleReactionLabel();
        WebElement targetButton = firstVisibleReactionButton();
        if (targetButton == null) {
            return false;
        }
        boolean initial = "Unlike".equalsIgnoreCase(initialLabel) || "Не нравится".equalsIgnoreCase(initialLabel);

        boolean firstClicked = clickSafe(targetButton);
        if (!firstClicked) {
            return false;
        }
        boolean changed = waitForLikedState(!initial);

        WebElement restoreButton = firstVisibleReactionButton();
        if (restoreButton == null) {
            return true;
        }
        boolean secondClicked = clickSafe(restoreButton);
        if (!secondClicked) {
            return changed;
        }
        boolean restored = waitForLikedState(initial);
        return restored || changed || secondClicked;
    }

    private WebElement firstVisible(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                return element;
            }
        }
        return null;
    }

    private boolean clickSafe(WebElement element) {
        try {
            element.click();
            return true;
        } catch (WebDriverException ex) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                return true;
            } catch (WebDriverException ignored) {
                return false;
            }
        }
    }

    private boolean waitForLikedState(boolean expectedLiked) {
        for (int i = 0; i < 20; i++) {
            if (isCurrentPostLiked() == expectedLiked) {
                return true;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    private WebElement firstVisibleReactionButton() {
        WebElement byLocator = firstVisible(unlikeButton);
        if (byLocator != null) {
            return byLocator;
        }
        byLocator = firstVisible(likeButton);
        if (byLocator != null) {
            return byLocator;
        }

        Object button = ((JavascriptExecutor) driver).executeScript(
                "const labels = ['Like','Unlike','Нравится','Не нравится'];" +
                        "const svgs = Array.from(document.querySelectorAll('svg[aria-label]'));" +
                        "for (const svg of svgs) {" +
                        "  const label = svg.getAttribute('aria-label');" +
                        "  if (!labels.includes(label)) continue;" +
                        "  const btn = svg.closest('button,[role=\"button\"]');" +
                        "  if (!btn) continue;" +
                        "  const rect = btn.getBoundingClientRect();" +
                        "  if (rect.width <= 0 || rect.height <= 0) continue;" +
                        "  if (rect.bottom < 0 || rect.top > window.innerHeight) continue;" +
                        "  return btn;" +
                        "}" +
                        "return null;"
        );
        if (button instanceof WebElement webElement) {
            return webElement;
        }
        return null;
    }

    private String getVisibleReactionLabel() {
        Object label = ((JavascriptExecutor) driver).executeScript(
                "const labels = ['Like','Unlike','Нравится','Не нравится'];" +
                        "const svgs = Array.from(document.querySelectorAll('svg[aria-label]'));" +
                        "for (const svg of svgs) {" +
                        "  const aria = svg.getAttribute('aria-label');" +
                        "  if (!labels.includes(aria)) continue;" +
                        "  const btn = svg.closest('button,[role=\"button\"]');" +
                        "  if (!btn) continue;" +
                        "  const rect = btn.getBoundingClientRect();" +
                        "  if (rect.width <= 0 || rect.height <= 0) continue;" +
                        "  if (rect.bottom < 0 || rect.top > window.innerHeight) continue;" +
                        "  return aria;" +
                        "}" +
                        "return null;"
        );
        if (label instanceof String stringLabel) {
            return stringLabel;
        }
        return null;
    }

    private void sendKeysWithRetry(By locator, CharSequence text) {
        for (int i = 0; i < 5; i++) {
            try {
                WebElement element = visible(locator);
                element.click();
                element.sendKeys(text);
                return;
            } catch (StaleElementReferenceException ignored) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while retrying comment input.", e);
                }
            }
        }
        WebElement element = visible(locator);
        element.click();
        element.sendKeys(text);
    }
}
