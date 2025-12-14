package org.example. tests;

import io.qameta.allure.Description;
import org.example.pages.SpecialCarriagePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time. LocalDate;
import java.time.format.DateTimeFormatter;

public class SpecialCarriageSecurityTest {

    private WebDriver driver;
    private SpecialCarriagePage specialCarriagePage;
    private SoftAssert softAssert;

    // SQL ін'єкції для тестування
    private static final String[] SQL_INJECTIONS = {
            "' OR '1'='1",
            "' OR 1=1--",
            "admin'--",
            "' OR 'a'='a",
            "1' OR '1' = '1",
            "'; DROP TABLE users--",
            "' UNION SELECT NULL--",
            "1' AND '1'='1",
            "' OR 1=1#",
            "' OR 'x'='x",
            "1'; DROP TABLE users; --",
            "' UNION SELECT * FROM users--",
            "admin' OR '1'='1'--",
            "' OR ''='",
            "1' UNION SELECT NULL, NULL--",
            "<script>alert('XSS')</script>",
            "'; EXEC sp_executesql--",
            "' OR EXISTS(SELECT * FROM users)--"
    };

    @BeforeClass
    public void setupClass() {
        System.out.println("\n" + "=".repeat(60));
        System.out. println("ПОЧАТОК ТЕСТУВАННЯ:  Security - SQL Injection Tests");
        System.out.println("=".repeat(60));
    }

    @BeforeMethod
    public void setup() {
        System.out.println("\n🚀 Ініціалізація WebDriver.. .");
        driver = new EdgeDriver();
        specialCarriagePage = new SpecialCarriagePage(driver);
        softAssert = new SoftAssert();
        System.out.println("✓ WebDriver ініціалізовано успішно");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            System.out.println("\n🛑 Закриття браузера...");
            driver. quit();
            System.out.println("✓ Браузер закрито");
        }
    }

    @AfterClass
    public void tearDownClass() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТУВАННЯ ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));

        // Очищення тестових файлів
        cleanupTestDocuments();
    }

    // ==================== ТЕСТИ НА SQL ІН'ЄКЦІЇ В ТЕКСТОВІ ПОЛЯ ====================

    @Test(priority = 1)
    @Description("SQL ін'єкції в поле 'Прізвище контакту'")
    public void testSQLInjectionInContactLastName() {
        System.out. println("\n" + "=". repeat(60));
        System.out.println("ТЕСТ 1: SQL ін'єкції в поле 'Прізвище контакту'");
        System.out.println("=". repeat(60));

        testSQLInjectionInField(
                "enteredLastnameContact",
                "Прізвище контакту",
                "name"
        );
    }

    @Test(priority = 2)
    @Description("SQL ін'єкції в поле 'Ім'я контакту'")
    public void testSQLInjectionInContactFirstName() {
        System.out.println("\n" + "=".repeat(60));
        System.out. println("ТЕСТ 2: SQL ін'єкції в поле 'Ім'я контакту'");
        System.out.println("=". repeat(60));

        testSQLInjectionInField(
                "enteredFirstnameContact",
                "Ім'я контакту",
                "name"
        );
    }

    @Test(priority = 3)
    @Description("SQL ін'єкції в поле 'Телефон'")
    public void testSQLInjectionInPhone() {
        System.out. println("\n" + "=". repeat(60));
        System.out.println("ТЕСТ 3: SQL ін'єкції в поле 'Телефон'");
        System.out.println("=".repeat(60));

        testSQLInjectionInField(
                "enteredPhone",
                "Телефон",
                "name"
        );
    }

    @Test(priority = 4)
    @Description("SQL ін'єкції в поле 'Email'")
    public void testSQLInjectionInEmail() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 4: SQL ін'єкції в поле 'Email'");
        System.out. println("=".repeat(60));

        testSQLInjectionInField(
                "enteredEmail",
                "Email",
                "name"
        );
    }

    @Test(priority = 5)
    @Description("SQL ін'єкції в поле 'Номер потяга'")
    public void testSQLInjectionInTrainNumber() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 5: SQL ін'єкції в поле 'Номер потяга'");
        System.out.println("=".repeat(60));

        testSQLInjectionInField(
                "enteredTrain",
                "Номер потяга",
                "name"
        );
    }

    @Test(priority = 6)
    @Description("SQL ін'єкції в поле 'Прізвище пасажира'")
    public void testSQLInjectionInPassengerLastName() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 6: SQL ін'єкції в поле 'Прізвище пасажира'");
        System.out.println("=". repeat(60));

        testSQLInjectionInField(
                "enteredLastname1",
                "Прізвище пасажира",
                "name"
        );
    }

    @Test(priority = 7)
    @Description("SQL ін'єкції в поле 'Ім'я пасажира'")
    public void testSQLInjectionInPassengerFirstName() {
        System.out.println("\n" + "=".repeat(60));
        System.out. println("ТЕСТ 7: SQL ін'єкції в поле 'Ім'я пасажира'");
        System.out.println("=".repeat(60));

        testSQLInjectionInField(
                "enteredFirstname",
                "Ім'я пасажира",
                "name"
        );
    }

    @Test(priority = 8)
    @Description("SQL ін'єкції в поле 'Серія та номер посвідчення'")
    public void testSQLInjectionInIdCard() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 8: SQL ін'єкції в поле 'Серія та номер посвідчення'");
        System.out.println("=".repeat(60));

        testSQLInjectionInField(
                "//label[contains(text(), 'Серія та № посвідчення')]/following-sibling::input",
                "Серія та № посвідчення",
                "xpath"
        );
    }

    @Test(priority = 9)
    @Description("SQL ін'єкції в поле 'Ким видано'")
    public void testSQLInjectionInIssuedBy() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 9: SQL ін'єкції в поле 'Ким видано'");
        System.out.println("=".repeat(60));

        testSQLInjectionInField(
                "//label[contains(text(), 'Ким видано')]/following-sibling::div//input",
                "Ким видано",
                "xpath"
        );
    }

    // ==================== ОСНОВНИЙ МЕТОД ТЕСТУВАННЯ ====================

    private void testSQLInjectionInField(String fieldLocator, String fieldName, String locatorType) {
        int testNumber = 1;
        int blocked = 0;
        int accepted = 0;
        int sanitized = 0;
        int errors = 0;

        for (String injection : SQL_INJECTIONS) {
            System.out.println("\n🧪 Тест " + testNumber + "/" + SQL_INJECTIONS.length +
                    ": " + truncateString(injection, 40));

            // Відкриваємо сторінку заново для кожної ін'єкції
            specialCarriagePage.open();

            // Заповнюємо всі поля окрім тестового
            fillAllFieldsExceptOne(fieldLocator, locatorType);

            try {
                WebElement field;
                if (locatorType. equals("name")) {
                    field = driver.findElement(By.name(fieldLocator));
                } else {
                    field = driver.findElement(By.xpath(fieldLocator));
                }

                // Прокручуємо до поля
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", field);

                Thread.sleep(300);

                // РОЗУМНЕ ОЧИЩЕННЯ через Ctrl+A
                field.click();
                Thread.sleep(200);
                field.sendKeys(org.openqa.selenium.Keys. chord(org.openqa.selenium.Keys.CONTROL, "a"));
                field.sendKeys(org.openqa.selenium.Keys. BACK_SPACE);
                Thread.sleep(200);

                // Вводимо SQL ін'єкцію
                field.sendKeys(injection);
                Thread.sleep(300);

                // Втрата фокусу
                field.sendKeys(org.openqa.selenium.Keys.TAB);
                Thread.sleep(500);

                // Перевіряємо чи є помилка ДО submit
                boolean hasValidationError = checkFieldHasError(field);
                String valueBeforeSubmit = field.getAttribute("value");

                if (hasValidationError) {
                    System.out.println("  ✓ Блоковано валідацією (до submit)");
                    blocked++;
                    testNumber++;
                    continue;
                }

                // Приймаємо угоду
                try {
                    WebElement checkbox = driver.findElement(By. id("submitTerms"));
                    if (! checkbox.isSelected()) {
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                                "arguments[0].click();", checkbox);
                    }
                    Thread.sleep(300);
                } catch (Exception e) {
                    System.out.println("  ⚠ Не вдалося відмітити checkbox: " + e.getMessage());
                }

                // SUBMIT ФОРМИ
                System.out.println("  → Відправка форми...");
                WebElement submitButton = driver.findElement(
                        By.xpath("//button[contains(text(), 'Оформити заявку')]"));

                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", submitButton);
                Thread.sleep(500);

                submitButton.click();
                Thread. sleep(3000);

                // Аналіз результату ПІСЛЯ submit
                String currentUrl = driver.getCurrentUrl();
                boolean submittedSuccessfully = currentUrl.contains("success");
                boolean stayedOnPage = currentUrl.contains("special-carriage") && ! submittedSuccessfully;
                boolean hasServerError = checkForServerErrors();

                if (hasServerError) {
                    System.out.println("  ❌ КРИТИЧНО:  Помилка сервера (можлива вразливість!)");
                    errors++;
                } else if (submittedSuccessfully) {
                    System.out. println("  ⚠ Форма відправлена успішно (дані санітизовані на сервері)");
                    sanitized++;
                } else if (stayedOnPage) {
                    boolean hasErrorAfterSubmit = checkForErrors();
                    if (hasErrorAfterSubmit) {
                        System.out.println("  ✓ Відхилено сервером з помилкою валідації");
                        blocked++;
                    } else {
                        System.out.println("  ✓ Форма не відправилась (тиха блокація)");
                        blocked++;
                    }
                } else {
                    System.out.println("  ⚠ Неочікуваний результат.  URL: " + currentUrl);
                    errors++;
                }

                // Логуємо значення поля
                if (valueBeforeSubmit != null && !valueBeforeSubmit.isEmpty()) {
                    if (! valueBeforeSubmit.equals(injection)) {
                        System.out.println("    Санітизація:  '" + truncateString(injection, 20) +
                                "' → '" + truncateString(valueBeforeSubmit, 20) + "'");
                    } else {
                        System.out.println("    Значення:  '" + truncateString(valueBeforeSubmit, 30) + "'");
                    }
                }

            } catch (Exception e) {
                System.out.println("  ✗ Помилка виконання: " + e.getMessage());
                errors++;
            }

            testNumber++;
        }

        // Підсумкова статистика
        System.out. println("\n" + "=".repeat(60));
        System.out.println("📊 СТАТИСТИКА для поля '" + fieldName + "':");
        System.out.println("  ✓ Заблоковано: " + blocked + " (" +
                String.format("%.1f", (blocked * 100.0 / SQL_INJECTIONS.length)) + "%)");
        System.out. println("  ⚠ Санітизовано на сервері: " + sanitized + " (" +
                String.format("%.1f", (sanitized * 100.0 / SQL_INJECTIONS.length)) + "%)");
        System.out.println("  ❌ Помилок: " + errors + " (" +
                String.format("%.1f", (errors * 100.0 / SQL_INJECTIONS.length)) + "%)");
        System.out. println("  📊 Загальна безпека: " +
                String.format("%.1f", ((blocked + sanitized) * 100.0 / SQL_INJECTIONS.length)) + "%");
        System.out.println("=".repeat(60));

        takeScreenshot("sql_injection_summary_" + fieldName. replaceAll(" ", "_"));

        // Assert - критично якщо є помилки сервера
        softAssert.assertEquals(errors, 0,
                "Не повинно бути помилок сервера для поля:  " + fieldName);

        softAssert.assertAll();
    }

    // ==================== КОМПЛЕКСНИЙ ТЕСТ ====================

    @Test(priority = 10)
    @Description("Спроба відправити форму з SQL ін'єкціями у всіх полях")
    public void testFullFormWithSQLInjections() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 10: Повна форма з SQL ін'єкціями");
        System.out.println("=".repeat(60));

        specialCarriagePage.open();

        String sqlInjection = "' OR '1'='1";

        System.out.println("\n📝 Заповнення форми SQL ін'єкцією:  " + sqlInjection);

        // Заповнюємо всі текстові поля SQL ін'єкцією
        try {
            // Контактна інформація
            WebElement lastNameContact = driver.findElement(By. name("enteredLastnameContact"));
            replaceTextWithCtrlA(lastNameContact, sqlInjection);

            WebElement firstNameContact = driver.findElement(By.name("enteredFirstnameContact"));
            replaceTextWithCtrlA(firstNameContact, sqlInjection);

            driver.findElement(By.name("enteredPhone")).sendKeys("501234567");
            driver.findElement(By.name("enteredEmail")).sendKeys("test@example. com");

            Thread.sleep(500);

            specialCarriagePage.selectAccommodationType("3");
            specialCarriagePage.selectFromStation("Київ");
            specialCarriagePage.selectToStation("Львів");

            LocalDate futureDate = LocalDate.now().plusDays(10);
            String travelDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            specialCarriagePage.selectTravelDate(travelDate);

            WebElement trainNumber = driver.findElement(By. name("enteredTrain"));
            replaceTextWithCtrlA(trainNumber, sqlInjection);

            specialCarriagePage.selectPaymentStation("Київ");
            specialCarriagePage.selectPassengerCategory("4");

            WebElement passengerLastName = driver.findElement(By.name("enteredLastname1"));
            replaceTextWithCtrlA(passengerLastName, sqlInjection);

            WebElement passengerFirstName = driver.findElement(By.name("enteredFirstname"));
            replaceTextWithCtrlA(passengerFirstName, sqlInjection);

            WebElement idCard = driver.findElement(
                    By.xpath("//label[contains(text(), 'Серія та № посвідчення')]/following-sibling::input"));
            replaceTextWithCtrlA(idCard, sqlInjection);

            WebElement issueDate = driver.findElement(
                    By.xpath("//label[contains(text(), 'Дата видачі')]/ancestor::div[contains(@class,'form-group')]//input"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = '01. 01.2024';", issueDate);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", issueDate);

            WebElement issuedBy = driver.findElement(
                    By. xpath("//label[contains(text(), 'Ким видано')]/following-sibling::div//input"));
            replaceTextWithCtrlA(issuedBy, sqlInjection);

            // ========== ЗАВАНТАЖЕННЯ ДОКУМЕНТА ==========
            System.out.println("\n📎 Завантаження документа...");
            uploadTestDocument();

            Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("⚠ Помилка при заповненні форми: " + e.getMessage());
        }

        takeScreenshot("form_with_sql_injections");

        System.out.println("\n📤 Спроба відправити форму.. .");

        specialCarriagePage.acceptAgreement();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement submitButton = driver.findElement(
                By.xpath("//button[contains(text(), 'Оформити заявку')]"));

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", submitButton);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        submitButton.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Перевіряємо результат
        String currentUrl = driver.getCurrentUrl();
        System.out.println("\n📍 URL після відправки: " + currentUrl);

        boolean hasError = checkForErrors();
        boolean hasServerError = checkForServerErrors();
        boolean stayedOnPage = currentUrl. contains("special-carriage") &&
                !currentUrl.contains("success");

        if (hasServerError) {
            System.out.println("  ❌ КРИТИЧНО: Знайдено помилку сервера!");
        } else if (stayedOnPage) {
            System.out.println("  ✓ Форма не відправилась (правильний захист)");
        } else if (currentUrl.contains("success")) {
            System.out.println("  ⚠ Форма відправилась (дані санітизовані на сервері)");
        } else {
            System.out. println("  ⚠ Неочікуваний результат");
        }

        if (hasError) {
            System.out.println("  ✓ Відображаються помилки валідації");
        }

        takeScreenshot("sql_injection_result");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ ТЕСТ 10 ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));

        softAssert.assertFalse(hasServerError, "Не повинно бути помилок сервера");
        softAssert.assertAll();
    }

    // ==================== ДОПОМІЖНІ МЕТОДИ ====================

    private void replaceTextWithCtrlA(WebElement element, String text) {
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", element);
            Thread.sleep(200);

            element.click();
            Thread.sleep(100);
            element.sendKeys(org.openqa.selenium.Keys. chord(org.openqa.selenium.Keys.CONTROL, "a"));
            element.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
            Thread.sleep(100);
            element.sendKeys(text);
            Thread.sleep(200);
        } catch (Exception e) {
            System.out.println("  ⚠ Помилка при заміні тексту: " + e.getMessage());
        }
    }

    private void fillAllFieldsExceptOne(String fieldToTest, String locatorType) {
        try {
            LocalDate futureDate = LocalDate.now().plusDays(10);
            String travelDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Контактна інформація
            if (!(locatorType.equals("name") && fieldToTest.equals("enteredLastnameContact"))) {
                specialCarriagePage.fillContactLastName("Тест");
            }
            if (!(locatorType.equals("name") && fieldToTest.equals("enteredFirstnameContact"))) {
                specialCarriagePage.fillContactFirstName("Користувач");
            }
            if (!(locatorType.equals("name") && fieldToTest.equals("enteredPhone"))) {
                specialCarriagePage. fillPhone("501234567");
            }
            if (!(locatorType.equals("name") && fieldToTest.equals("enteredEmail"))) {
                specialCarriagePage.fillEmail("test@example.com");
            }

            specialCarriagePage.selectAccommodationType("3");

            // Станції
            specialCarriagePage. selectFromStation("Київ");
            specialCarriagePage.selectToStation("Львів");

            // Дата поїздки
            specialCarriagePage.selectTravelDate(travelDate);

            // Номер потяга
            if (!(locatorType.equals("name") && fieldToTest.equals("enteredTrain"))) {
                specialCarriagePage. fillTrainNumber("100");
            }

            specialCarriagePage.selectPaymentStation("Київ");
            specialCarriagePage.selectPassengerCategory("4");

            // Дані пасажира
            if (!(locatorType.equals("name") && fieldToTest.equals("enteredLastname1"))) {
                specialCarriagePage.fillPassengerLastName("Тест");
            }
            if (!(locatorType. equals("name") && fieldToTest.equals("enteredFirstname"))) {
                specialCarriagePage.fillPassengerFirstName("Користувач");
            }

            // Серія посвідчення
            boolean isIdCardField = locatorType.equals("xpath") &&
                    fieldToTest.contains("Серія та № посвідчення");
            if (! isIdCardField) {
                specialCarriagePage.fillIdCardNumber("ТС12345678");
            }

            // Дата видачі
            try {
                WebElement issueDateField = driver.findElement(
                        By.xpath("//label[contains(text(), 'Дата видачі')]/ancestor::div[contains(@class,'form-group')]//input"));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].value = '01.01.2024';", issueDateField);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", issueDateField);
                Thread.sleep(300);
            } catch (Exception e) {
                System.out. println("  ⚠ Не вдалося встановити дату видачі");
            }

            // Ким видано
            boolean isIssuedByField = locatorType.equals("xpath") &&
                    fieldToTest.contains("Ким видано");
            if (!isIssuedByField) {
                specialCarriagePage. fillIssuedBy("Тестова служба");
            }

            // ========== ЗАВАНТАЖЕННЯ ДОКУМЕНТА ==========
            System. out.println("  📎 Завантаження документа...");
            uploadTestDocument();

            Thread. sleep(500);

        } catch (Exception e) {
            System. out.println("⚠ Помилка при заповненні форми: " + e. getMessage());
        }
    }

    private void uploadTestDocument() {
        try {
            java.io.File testFile = createTestDocument();

            if (testFile != null) {
                WebElement fileInput = driver.findElement(By.id("assetsFieldHandle"));

                // Робимо input видимим
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].style.opacity = '1';" +
                                "arguments[0].style.display = 'block';" +
                                "arguments[0].style.visibility = 'visible';",
                        fileInput
                );

                fileInput.sendKeys(testFile. getAbsolutePath());
                Thread.sleep(500);

                System.out.println("  ✓ Документ завантажено:  " + testFile.getName());
            }
        } catch (Exception e) {
            System.out.println("  ⚠ Помилка завантаження документа: " + e.getMessage());
        }
    }

    private java.io.File createTestDocument() {
        try {
            // Створюємо директорію якщо не існує
            java.io.File tempDir = new java.io.File("test-documents");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // Створюємо тестовий текстовий файл
            java.io.File testFile = new java.io. File(tempDir, "test_security_document.txt");

            if (! testFile.exists()) {
                try (java.io.FileWriter writer = new java.io. FileWriter(testFile)) {
                    writer.write("Тестовий документ для перевірки безпеки\n");
                    writer.write("Дата створення: " + java.time. LocalDateTime.now() + "\n");
                    writer.write("Призначення: SQL Injection Security Test\n");
                    writer.write("=" . repeat(50) + "\n");
                    writer.write("\n");
                    writer.write("Цей файл створено автоматично для тестування.\n");
                }
            }

            return testFile;

        } catch (Exception e) {
            System.out.println("⚠ Помилка створення тестового файлу: " + e.getMessage());
            return null;
        }
    }

    private void cleanupTestDocuments() {
        try {
            java.io.File tempDir = new java.io.File("test-documents");
            if (tempDir.exists()) {
                java.io.File[] files = tempDir.listFiles();
                if (files != null) {
                    for (java.io. File file : files) {
                        if (file.delete()) {
                            System.out.println("  ✓ Видалено: " + file.getName());
                        }
                    }
                }
                if (tempDir.delete()) {
                    System.out.println("✓ Директорію test-documents видалено");
                }
            }
        } catch (Exception e) {
            System.out. println("⚠ Не вдалося видалити тестові документи: " + e.getMessage());
        }
    }

    private boolean checkFieldHasError(WebElement field) {
        try {
            WebElement parent = field.findElement(By.xpath("./ancestor::div[contains(@class,'form-group')]"));
            java.util.List<WebElement> errors = parent.findElements(By.cssSelector("span.error"));

            for (WebElement error : errors) {
                if (error.isDisplayed() && ! error.getText().trim().isEmpty()) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkForErrors() {
        try {
            java.util.List<WebElement> errors = driver.findElements(By.cssSelector(
                    "span.error, . alert-danger, .text-danger, .invalid-feedback"
            ));

            for (WebElement error : errors) {
                if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                    System.out.println("  Помилка: " + error. getText());
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkForServerErrors() {
        try {
            String pageSource = driver.getPageSource().toLowerCase();

            // Перевіряємо наявність SQL помилок
            String[] sqlErrorPatterns = {
                    "sql syntax",
                    "mysql",
                    "postgresql",
                    "ora-",
                    "sql server",
                    "syntax error",
                    "sqlexception",
                    "database error",
                    "query failed",
                    "unexpected end of sql",
                    "error in your sql",
                    "warning:  mysql"
            };

            for (String pattern : sqlErrorPatterns) {
                if (pageSource.contains(pattern)) {
                    System.out.println("    ⚠ Знайдено SQL помилку: " + pattern);
                    return true;
                }
            }

            // Перевіряємо HTTP помилки
            if (pageSource.contains("500 internal server error") ||
                    pageSource.contains("error 500")) {
                System.out.println("    ⚠ Знайдено помилку сервера 500");
                return true;
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private String truncateString(String str, int maxLength) {
        if (str. length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    private void takeScreenshot(String name) {
        try {
            java.io.File screenshot = ((org.openqa.selenium.TakesScreenshot) driver)
                    .getScreenshotAs(org.openqa.selenium. OutputType.FILE);

            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            java.io.File destFile = new java.io.File(
                    "test-screenshots/" + name + "_" + timestamp + ".png");

            destFile.getParentFile().mkdirs();

            java.nio.file.Files.copy(
                    screenshot.toPath(),
                    destFile.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            System.out. println("📸 Скріншот:  " + destFile.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("⚠ Не вдалося зробити скріншот: " + e.getMessage());
        }
    }
}