package org.example.tpolab3.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class DriverManager {
    private static final String BASE_URL = "https://www.instagram.com/";

    private WebDriver driver;
    private WebDriverWait wait;
    private boolean attachedToExistingChrome;

    public void start() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        attachedToExistingChrome = false;

        driver = switch (browser) {
            case "firefox" -> createFirefox(headless);
            case "chrome" -> createChrome(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        if (attachedToExistingChrome) {
            Set<String> handles = driver.getWindowHandles();
            if (handles.isEmpty()) {
                throw new IllegalStateException("No browser windows found in attached Chrome session.");
            }
            driver.switchTo().window(handles.iterator().next());
        }

        if (!attachedToExistingChrome) {
            driver.manage().window().setSize(new Dimension(1280, 900));
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        if (!attachedToExistingChrome) {
            driver.get(BASE_URL);
        }
    }

    private WebDriver createChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        String debuggerAddress = System.getProperty("chrome.debuggerAddress", "").trim();
        if (!debuggerAddress.isEmpty()) {
            options.setExperimentalOption("debuggerAddress", debuggerAddress);
            attachedToExistingChrome = true;
            WebDriverException lastException = null;
            for (int attempt = 1; attempt <= 3; attempt++) {
                try {
                    return new ChromeDriver(options);
                } catch (WebDriverException ex) {
                    lastException = ex;
                    try {
                        Thread.sleep(1200);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted while attaching to Chrome debugger.", interruptedException);
                    }
                }
            }
            throw new IllegalStateException(
                    "Cannot attach to Chrome debugger at " + debuggerAddress
                            + ". Ensure Chrome is started with --remote-debugging-port and has an open tab.",
                    lastException
            );
        }

        options.addArguments("--disable-gpu");
        options.addArguments("--lang=en-US");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        if (headless) {
            options.addArguments("--headless=new");
        }
        ChromeDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
        return chromeDriver;
    }

    private WebDriver createFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-private");
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public void stop() {
        if (driver != null) {
            if (attachedToExistingChrome) {
                driver = null;
                return;
            }
            driver.quit();
            driver = null;
        }
    }

    public boolean isAttachedToExistingChrome() {
        return attachedToExistingChrome;
    }
}
