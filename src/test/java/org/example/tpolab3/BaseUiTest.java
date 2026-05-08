package org.example.tpolab3;

import org.example.tpolab3.core.DriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BaseUiTest {
    protected DriverManager driverManager;
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driverManager = new DriverManager();
        String debuggerAddress = System.getProperty("chrome.debuggerAddress", "").trim();
        try {
            driverManager.start();
        } catch (RuntimeException ex) {
            if (!debuggerAddress.isEmpty()) {
                Assumptions.assumeTrue(false, "Attach к debug Chrome не удался: " + ex.getMessage());
            }
            throw ex;
        }
        driver = driverManager.getDriver();
        wait = driverManager.getWait();
    }

    @AfterEach
    void tearDown() {
        if (driverManager != null) {
            driverManager.stop();
        }
    }
}
