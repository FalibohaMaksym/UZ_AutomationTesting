package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebSearchHelper {

    public static void clickElement(WebDriver driver, WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        }
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block:  'center'});", element);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread. currentThread().interrupt();
        }
    }

    public static void selectDateInCalendar(WebDriver driver, WebDriverWait wait, String dateToSelect, int monthsBack) throws InterruptedException {
        Thread.sleep(500);

        // 1. Логіка перегортання місяців назад
        if (monthsBack > 0) {
            for (int i = 0; i < monthsBack; i++) {
                try {
                    // Використовуємо короткий wait тут, щоб не чекати довго, якщо кнопка не клікабельна
                    WebElement prevMonthButton = driver.findElement(By.cssSelector("button.mx-btn-icon-left"));
                    clickElement(driver, prevMonthButton);
                    Thread.sleep(200); // Трохи пришвидшимо
                } catch (Exception e) {
                    System.out.println("Не вдалося перейти на попередній місяць: " + e.getMessage());
                    break; // Якщо не можемо клікнути назад - виходимо з циклу
                }
            }
        }

        Thread.sleep(500);

        // 2. Вибір дня
        try {
            // Шукаємо комірку з датою
            WebElement dayCell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//td[@title='" + dateToSelect + "']")));

            // Перевірка на disabled (як у вас було)
            String classAttr = dayCell.getAttribute("class");
            if (classAttr != null && classAttr.contains("disabled")) {
                System.out.println("УВАГА: Дата " + dateToSelect + " недоступна (disabled)");
                // Тут можна не кидати Exception, а спробувати клікнути все одно, або просто залогувати
            }

            scrollToElement(driver, dayCell);
            clickElement(driver, dayCell);
            System.out.println("Клікнули на дату: " + dateToSelect);

        } catch (Exception e) {
            System.out.println("Помилка при кліку на день " + dateToSelect + ": " + e.getMessage());
        }

        Thread.sleep(1000);

        try {
            var confirmButtons = driver.findElements(By.cssSelector(".mx-datepicker-btn-confirm"));

            // Якщо список не пустий і кнопка видима -> клікаємо
            if (!confirmButtons.isEmpty() && confirmButtons.get(0).isDisplayed()) {
                clickElement(driver, confirmButtons.get(0));
                System.out.println("Кнопка підтвердження натиснута.");
            } else {
                System.out.println("Кнопка підтвердження відсутня (календар закрився сам).");
            }
        } catch (Exception e) {
            System.out.println("Помилка при спробі підтвердження (ігноруємо): " + e.getMessage());
        }
    }

}
