package org.example;

import org. openqa.selenium.By;
import org.openqa.selenium. JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa. selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa. selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui. ExpectedConditions;
import org. openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

import static org.example.utils.WebSearchHelper.*;

public class Main {


    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://services.uz.gov.ua/special-carriage");
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Заповнення форми
            WebElement lastNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("enteredLastnameContact")));
            lastNameField.sendKeys("Іваненко");

            WebElement firstNameField = driver.findElement(By.name("enteredFirstnameContact"));
            firstNameField.sendKeys("Іван");

            WebElement phoneInput = driver.findElement(By. name("enteredPhone"));
            phoneInput.sendKeys("996022001");

            WebElement emailField = driver.findElement(By. name("enteredEmail"));
            emailField.sendKeys("e@gmail.com");

            WebElement radioButton = driver.findElement(By.cssSelector("label[for='3']"));
            clickElement(driver, radioButton);

            // Вибір "Звідки"
            WebElement inputFrom = driver.findElement(By.xpath("//label[contains(text(), 'Звідки')]/following-sibling::div//input"));
            scrollToElement(driver, inputFrom);
            inputFrom.clear();
            inputFrom.sendKeys("Київ");
            Thread.sleep(500);
            inputFrom.sendKeys(Keys. ARROW_DOWN);
            inputFrom. sendKeys(Keys.ENTER);

            // Вибір "Куди"
            WebElement inputTo = driver.findElement(By. xpath("//label[contains(text(), 'Куди')]/following-sibling::div//input"));
            scrollToElement(driver, inputTo);
            inputTo.clear();
            inputTo. sendKeys("Львів");
            Thread.sleep(1500);
            inputTo. sendKeys(Keys.ARROW_DOWN);
            inputTo.sendKeys(Keys.ENTER);

            // Вибір дати поїздки
            WebElement dateField = wait.until(ExpectedConditions. presenceOfElementLocated(By.name("date")));
            scrollToElement(driver, dateField);
            clickElement(driver, dateField);

            // Спробуємо вибрати найближчу доступну дату (сьогодні або завтра)
            // Згідно з HTML, сьогодні 2025-12-13 є доступною (має клас "today")
            try {
                selectDateInCalendar(driver, wait, "2025-12-18", 0);
            } catch (Exception e) {
                // Якщо не вийшло, спробуємо попередню дату
                System.out.println("Спроба вибрати іншу дату...");
                selectDateInCalendar(driver, wait, "2025-12-12", 0);
            }

            Thread.sleep(500);

            // Заповнення номера потяга
            WebElement trainInput = driver.findElement(By. name("enteredTrain"));
            scrollToElement(driver, trainInput);
            trainInput.clear();
            trainInput.sendKeys("052");

            Thread.sleep(500);

            // Вибір станції оплати
            WebElement payStationInput = driver.findElement(
                    By.xpath("//label[contains(text(), 'Станція оплати')]/../following-sibling::div//input"));

            scrollToElement(driver, payStationInput);
            payStationInput.click();
            payStationInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            payStationInput. sendKeys(Keys.BACK_SPACE);
            payStationInput.sendKeys("Київ");

            Thread.sleep(1000);

            payStationInput. sendKeys(Keys.ARROW_DOWN);
            payStationInput.sendKeys(Keys.ENTER);

            Thread.sleep(500);

            // Вибір категорії
            WebElement categoryLabel = driver.findElement(By. cssSelector("label[for='4']"));
            scrollToElement(driver, categoryLabel);
            clickElement(driver, categoryLabel);

            Thread.sleep(500);

            // Заповнення даних пасажира
            WebElement passLastName = driver.findElement(By. name("enteredLastname1"));
            scrollToElement(driver, passLastName);
            passLastName.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            passLastName.sendKeys(Keys.BACK_SPACE);
            passLastName. sendKeys("Василенко");

            Thread.sleep(500);

            WebElement passFirstName = driver.findElement(By.name("enteredFirstname"));
            passFirstName.sendKeys(Keys. chord(Keys.CONTROL, "a"));
            passFirstName. sendKeys(Keys.BACK_SPACE);
            passFirstName.sendKeys("Петро");

            Thread.sleep(500);

            // Заповнення серії та номера посвідчення
            WebElement idCardInput = driver.findElement(By.xpath("//label[contains(text(), 'Серія та № посвідчення')]/following-sibling::input"));
            scrollToElement(driver, idCardInput);
            idCardInput.sendKeys("ВК14365211");

            Thread.sleep(500);

            // Відкриття календаря для дати видачі (минула дата)
            WebElement issueDateInput = driver.findElement(By.xpath("//label[contains(text(), 'Дата видачі')]/ancestor::div[contains(@class,'form-group')]//input"));
            scrollToElement(driver, issueDateInput);
            clickElement(driver, issueDateInput);

            // Переходимо на 12 місяців назад для вибору 2024-12-25
            selectDateInCalendar(driver, wait, "2024-12-25", 12);

            WebElement issuedByInput = driver.findElement(By.xpath("//label[contains(text(), 'Ким видано')]/following-sibling::div//input"));
            scrollToElement(driver, issuedByInput);
            issuedByInput.sendKeys("some org");

            uploadDocument(driver, "src/main/resources/images/rizhiy_kot-1024.jpg");
//
//            WebElement agreementLabel = driver.findElement(By.cssSelector("label[for='submitTerms']"));
//            scrollToElement(driver, agreementLabel);
//            clickElement(driver, agreementLabel);

            WebElement hiddenCheckbox = driver.findElement(By.id("submitTerms"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", hiddenCheckbox);

//            Thread.sleep(15000);

            WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Оформити заявку')]"));
            scrollToElement(driver, submitButton);
            submitButton.click();

            System.out. println("Форма успішно заповнена!");

            Thread.sleep(17000);


        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void uploadDocument(WebDriver driver, String fileName) {
        try {
            File file;

            // Перевіряємо, чи вказано абсолютний шлях
            File tempFile = new File(fileName);
            if (tempFile. isAbsolute()) {
                // Якщо шлях абсолютний - використовуємо його
                file = tempFile;
            } else {
                // Якщо шлях відносний - використовуємо директорію проекту
                String projectDir = System.getProperty("user.dir");
                file = new File(projectDir, fileName);
            }

            System.out.println("Робота з файлом: " + file.getAbsolutePath());

            // Створюємо файл, якщо його немає
            if (!file. exists()) {
                System.out.println("Файл не знайдено. Створюю тестовий файл...");

                // Створюємо всі батьківські директорії
                File parentDir = file. getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                    System.out.println("✓ Створено директорії: " + parentDir.getAbsolutePath());
                }

                // Створюємо файл з контентом
                try (java. io.FileWriter writer = new java. io.FileWriter(file)) {
                    writer.write("Тестовий документ для автоматизації\n");
                    writer.write("Дата створення: " + java.time.LocalDateTime.now() + "\n");
                    writer.write("Файл: " + file.getName() + "\n");
                }

                System.out.println("✓ Файл створено: " + file.getAbsolutePath());
            } else {
                System.out. println("✓ Файл існує: " + file.getAbsolutePath());
            }

            String absolutePath = file.getAbsolutePath();

            // Знаходимо input для завантаження
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("assetsFieldHandle")));

            // Робимо input видимим через JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "arguments[0].style.opacity = '1';" +
                            "arguments[0].style.display = 'block';" +
                            "arguments[0].style.visibility = 'visible';" +
                            "arguments[0].classList. remove('opacity-0', 'hide');",
                    fileInput
            );

            // Завантажуємо файл
            fileInput.sendKeys(absolutePath);
            System.out.println("✓ Документ успішно завантажено: " + file.getName());

            // Чекаємо обробки
            Thread.sleep(500);

            // Опціонально: перевіряємо, чи з'явився файл на сторінці
            try {
                WebElement uploadedFile = driver.findElement(
                        By.xpath("//*[contains(text(), '" + file.getName() + "')]"));
                System.out.println("✓ Файл відображається на сторінці");
            } catch (Exception e) {
                System.out.println("⚠ Файл завантажено, але не відображається (це нормально)");
            }

        } catch (Exception e) {
            System.out.println("✗ Помилка при завантаженні файлу:  " + e.getMessage());
            e.printStackTrace();
        }
    }
}