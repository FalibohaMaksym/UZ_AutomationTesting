package org.example.pages;

import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure. Allure;


import java.io.ByteArrayInputStream;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
    }

    // Методи-помічники
    protected void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            js.executeScript("arguments[0]. click();", element);
        }
    }

    protected void scrollToElement(WebElement element) {
        js.executeScript("arguments[0]. scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        sleep(300);
    }

    protected void typeText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    protected void replaceText(WebElement element, String text) {
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(text);
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] takeScreenshotBytes() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType. BYTES);
    }

    public void attachScreenshot(String name) {
        Allure.addAttachment(name, new ByteArrayInputStream(takeScreenshotBytes()));
    }

    protected void selectFromDropdown(WebElement input, String value, int waitMs) {
        scrollToElement(input);
        input.clear();
        input.sendKeys(value);
        sleep(waitMs);
        input.sendKeys(Keys. ARROW_DOWN);
        input.sendKeys(Keys.ENTER);
    }

    protected void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}
