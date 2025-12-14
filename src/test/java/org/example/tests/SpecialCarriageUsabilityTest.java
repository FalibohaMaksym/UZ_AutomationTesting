package org.example.tests;

import jdk.jfr.Description;
import org.example.pages.BasePage;
import org.example.pages.SpecialCarriagePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa. selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng. annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;
import java.time. format.DateTimeFormatter;
import java.util.List;

public class SpecialCarriageUsabilityTest {

    private WebDriver driver;
    private SpecialCarriagePage specialCarriagePage;
    private SoftAssert softAssert;

    @BeforeClass
    public void setupClass() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ПОЧАТОК ТЕСТУВАННЯ:  Usability форми Special Carriage");
        System.out.println("=".repeat(60));
    }

    @BeforeMethod
    public void setup() {
        System.out.println("\n🚀 Ініціалізація WebDriver.. .");
        driver = new ChromeDriver();
        specialCarriagePage = new SpecialCarriagePage(driver);
        softAssert = new SoftAssert();
        System.out.println("✓ WebDriver ініціалізовано успішно");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            System.out.println("\n🛑 Закриття браузера...");
            driver. quit();
            System.out. println("✓ Браузер закрито");
        }
    }

    @AfterClass
    public void tearDownClass() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТУВАННЯ ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));
    }

    // ==================== ТЕСТИ НА ВАЛІДАЦІЮ EMAIL ====================

    @Test(priority = 1)
    @Description("Перевірка валідації некоректного email")
    public void testInvalidEmailValidation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 1: Валідація некоректної електронної пошти");
        System.out.println("=". repeat(60));

        specialCarriagePage.open();

        String[] invalidEmails = {
                "test",                    // без @
                "test@",                   // без домену
                "@domain. com",             // без локальної частини
                "test@domain",             // без домену верхнього рівня
                "test@@domain.com",        // подвійна @
                "test@domain.. com",        // подвійна крапка
                "test space@domain.com",   // пробіл
                "test@domain.com.",        // крапка в кінці
                ". test@domain.com",        // крапка на початку
                "тест@domain.com"          // кирилиця
        };

        int testNumber = 1;
        for (String email : invalidEmails) {
            System.out.println("\n🧪 Тест 1." + testNumber + ": Email = '" + email + "'");

            // Очищуємо поле
            WebElement emailField = driver.findElement(By.name("enteredEmail"));
            emailField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            emailField.sendKeys(Keys.BACK_SPACE);

            // Вводимо некоректний email
            emailField.sendKeys(email);
            emailField.sendKeys(org.openqa.selenium.Keys.TAB); // Втрата фокусу

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Шукаємо повідомлення про помилку
            boolean hasError = checkEmailError();

            if (hasError) {
                System.out. println("  ✓ Помилка відображається (правильно)");
            } else {
                System.out.println("  ❌ Помилка НЕ відображається (некоректна валідація)");
            }

            softAssert.assertTrue(hasError,
                    "Має відображатись помилка для email: " + email);

            testNumber++;
        }

        takeScreenshot("invalid_email_validation");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ ТЕСТ 1 ЗАВЕРШЕНО");
        System.out. println("=".repeat(60));

        softAssert.assertAll();
    }

    @Test(priority = 2)
    @Description("Перевірка валідації коректного email")
    public void testValidEmailAcceptance() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 2: Прийняття коректного email");
        System.out.println("=".repeat(60));

        specialCarriagePage.open();

        String[] validEmails = {
                "test@example.com",
                "user.name@example.com",
                "user+tag@example.co.uk",
                "test123@test-domain.com",
                "a@b.co"
        };

        int testNumber = 1;
        for (String email :  validEmails) {
            System.out.println("\n🧪 Тест 2." + testNumber + ": Email = '" + email + "'");

            WebElement emailField = driver.findElement(By.name("enteredEmail"));
            emailField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            emailField.sendKeys(Keys.BACK_SPACE);
            emailField.sendKeys(email);
            emailField.sendKeys(org.openqa.selenium.Keys.TAB);

            try {
                Thread. sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean hasError = checkEmailError();

            if (! hasError) {
                System. out.println("  ✓ Помилка НЕ відображається (правильно)");
            } else {
                System.out.println("  ❌ Помилка відображається для валідного email");
            }

            softAssert.assertFalse(hasError,
                    "НЕ має відображатись помилка для валідного email: " + email);

            testNumber++;
        }

        takeScreenshot("valid_email_acceptance");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ ТЕСТ 2 ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));

        softAssert. assertAll();
    }

    // ==================== ТЕСТИ НА ВАЛІДАЦІЮ ТЕЛЕФОНУ ====================

    @Test(priority = 3)
    @Description("Перевірка валідації некоректного номера телефону")
    public void testInvalidPhoneValidation() {
        System.out. println("\n" + "=". repeat(60));
        System.out.println("ТЕСТ 3: Валідація некоректного номера телефону");
        System.out.println("=".repeat(60));

        specialCarriagePage.open();

        String[][] invalidPhones = {
                {"12345", "Занадто короткий"},
                {"abc123456", "Містить літери"},
                {"501234567890", "Занадто довгий"},
                {"50 123 45 67", "Містить пробіли"},
                {"+380501234567", "Містить +"},
                {"(050)1234567", "Містить дужки"},
                {"", "Порожній"}
        };

        int testNumber = 1;
        for (String[] testCase : invalidPhones) {
            String phone = testCase[0];
            String description = testCase[1];

            System.out.println("\n🧪 Тест 3." + testNumber + ": " + description);
            System.out.println("  Телефон:  '" + phone + "'");

            WebElement phoneField = driver.findElement(By.name("enteredPhone"));
            phoneField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            phoneField.sendKeys(Keys.BACK_SPACE);

            if (! phone.isEmpty()) {
                phoneField.sendKeys(phone);
            }

            phoneField.sendKeys(org.openqa.selenium.Keys.TAB);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean hasError = checkPhoneError();

            if (hasError) {
                System.out.println("  ✓ Помилка відображається (правильно)");
            } else {
                System.out.println("  ❌ Помилка НЕ відображається");
            }

            softAssert.assertTrue(hasError,
                    "Має відображатись помилка для телефону: " + description);

            testNumber++;
        }

        takeScreenshot("invalid_phone_validation");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ ТЕСТ 3 ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));

        softAssert.assertAll();
    }

    @Test(priority = 4)
    @Description("Перевірка валідації коректного номера телефону")
    public void testValidPhoneAcceptance() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 4: Прийняття коректного номера телефону");
        System.out. println("=".repeat(60));

        specialCarriagePage. open();

        String[] validPhones = {
                "501234567",
                "671234567",
                "931234567",
                "631234567"
        };

        int testNumber = 1;
        for (String phone : validPhones) {
            System.out.println("\n🧪 Тест 4." + testNumber + ":  Телефон = '" + phone + "'");

            WebElement phoneField = driver.findElement(By.name("enteredPhone"));
            phoneField.clear();
            phoneField.sendKeys(phone);
            phoneField.sendKeys(org.openqa.selenium.Keys.TAB);

            try {
                Thread. sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean hasError = checkPhoneError();

            if (!hasError) {
                System.out.println("  ✓ Помилка НЕ відображається (правильно)");
            } else {
                System.out.println("  ❌ Помилка відображається для валідного телефону");
            }

            softAssert.assertFalse(hasError,
                    "НЕ має відображатись помилка для валідного телефону: " + phone);

            testNumber++;
        }

        takeScreenshot("valid_phone_acceptance");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ ТЕСТ 4 ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));

        softAssert.assertAll();
    }

    // ==================== ТЕСТИ НА ПОРОЖНІ ІМ'Я ====================

    @Test(priority = 5)
    @Description("Перевірка валідації порожнього імені")
    public void testEmptyNameValidation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 5: Валідація порожнього імені");
        System.out.println("=".repeat(60));

        specialCarriagePage.open();

        String[][] nameFields = {
                {"enteredLastnameContact", "Прізвище контакту"},
                {"enteredFirstnameContact", "Ім'я контакту"},
                {"enteredLastname1", "Прізвище пасажира"},
                {"enteredFirstname", "Ім'я пасажира"}
        };

        int testNumber = 1;
        for (String[] field : nameFields) {
            System.out.println("\n🧪 Тест 5." + testNumber + ":  Поле '" + field[1] + "'");

            // Заповнюємо всі поля окрім поточного
            fillFormExceptField(field[0]);

            // Прокручуємо до кнопки Submit
            WebElement submitButton = driver.findElement(
                    By.xpath("//button[contains(text(), 'Оформити заявку')]"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", submitButton);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e. printStackTrace();
            }

            // Натискаємо Submit
            submitButton.click();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Перевіряємо що форма не відправилась
            String currentUrl = driver.getCurrentUrl();
            boolean stayedOnPage = currentUrl.contains("special-carriage") &&
                    ! currentUrl.contains("success");

            // Перевіряємо наявність помилки біля поля
            boolean hasError = checkFieldError(field[0]);

            if (stayedOnPage) {
                System.out.println("  ✓ Форма не відправилась (правильно)");
            } else {
                System.out.println("  ❌ Форма відправилась з порожнім полем");
            }

            if (hasError) {
                System.out.println("  ✓ Помилка відображається біля поля");
            } else {
                System.out.println("  ⚠ Помилка не відображається біля поля");
            }

            softAssert.assertTrue(stayedOnPage,
                    "Форма не повинна відправлятись без поля: " + field[1]);

            // Відкриваємо сторінку заново для наступного тесту
            if (testNumber < nameFields.length) {
                specialCarriagePage.open();
            }

            testNumber++;
        }

        takeScreenshot("empty_name_validation");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ ТЕСТ 5 ЗАВЕРШЕНО");
        System.out.println("=". repeat(60));

        softAssert.assertAll();
    }

    // ==================== ТЕСТ НА CHECKBOX УГОДИ ====================

    private boolean checkPrivacyPolicyError() {
        try {
            // Шукаємо повідомлення про помилку біля checkbox
            WebElement checkbox = driver. findElement(By.id("submitTerms"));
            WebElement parent = checkbox.findElement(By. xpath("./ancestor::div[contains(@class,'form-check') or contains(@class,'form-group')]"));

            List<WebElement> errors = parent.findElements(By. cssSelector("span. error, . invalid-feedback, .text-danger"));

            for (WebElement error : errors) {
                if (error.isDisplayed() && ! error.getText().trim().isEmpty()) {
                    System.out.println("    Повідомлення:  " + error.getText());
                    return true;
                }
            }

            // Альтернативний пошук - шукаємо будь-яке повідомлення на сторінці
            List<WebElement> allErrors = driver.findElements(By. cssSelector("span.error"));
            for (WebElement error :  allErrors) {
                if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                    String text = error.getText().toLowerCase();
                    if (text.contains("угод") || text.contains("політик") || text.contains("згод")) {
                        System.out.println("    Повідомлення: " + error.getText());
                        return true;
                    }
                }
            }

            return false;

        } catch (Exception e) {
            System.out.println("    ⚠ Помилка при пошуку:  " + e.getMessage());
            return false;
        }
    }

    @Test(priority = 6)
    @Description("Перевірка що форма не відправляється без згоди на політику")
    public void testPrivacyPolicyCheckboxRequired() {
        System.out.println("\n" + "=". repeat(60));
        System.out.println("ТЕСТ 6: Обов'язковість згоди на політику конфіденційності");
        System.out.println("=". repeat(60));

        specialCarriagePage.open();

        System.out.println("\n📝 Заповнення всіх полів без прийняття угоди.. .");
        fillAllFieldsWithoutAgreement();

        System.out.println("\n🧪 Спроба відправити форму без checkbox...");

        // Перевіряємо стан checkbox
        WebElement checkbox = driver.findElement(By. id("submitTerms"));
        boolean isChecked = checkbox.isSelected();

        System.out.println("  Checkbox стан: " + (isChecked ? "✓ Відмічено" : "❌ НЕ відмічено"));

        softAssert.assertFalse(isChecked, "Checkbox має бути НЕ відмічений");

        // Натискаємо Submit
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
            Thread. sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Перевіряємо що форма не відправилась
        String currentUrl = driver.getCurrentUrl();
        boolean stayedOnPage = currentUrl.contains("special-carriage") &&
                !currentUrl. contains("success");

        if (stayedOnPage) {
            System.out.println("\n  ✓ Форма НЕ відправилась без згоди (правильно)");
        } else {
            System. out.println("\n  ❌ Форма відправилась БЕЗ згоди на політику (КРИТИЧНА ПОМИЛКА!)");
        }

        softAssert.assertTrue(stayedOnPage,
                "Форма НЕ повинна відправлятись без згоди на політику конфіденційності");

        // Перевіряємо наявність повідомлення про помилку
        System.out.println("\n🔍 Пошук повідомлення про необхідність згоди...");
        boolean hasError = checkPrivacyPolicyError();

        if (hasError) {
            System.out.println("  ✓ Повідомлення про помилку відображається");
        } else {
            System.out.println("  ⚠ Повідомлення про помилку не знайдено (можлива пасивна валідація)");
        }

        takeScreenshot("no_privacy_consent");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ ТЕСТ 6 ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));

        softAssert.assertAll();
    }
    // ==================== ТЕСТ НА ОДНОТИПНІСТЬ КАЛЕНДАРІВ ====================

    @Test(priority = 7)
    @Description("Перевірка що всі календарі мають кнопку підтвердження")
    public void testCalendarsConsistency() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 7: Однотипність календарів (наявність кнопки підтвердження)");
        System.out.println("=".repeat(60));

        specialCarriagePage.open();

        // Заповнюємо поля до календарів
        fillFieldsBeforeCalendars();

        String[][] calendarFields = {
                {"date", "Дата поїздки", "name"},
                {"//label[contains(text(), 'Дата видачі')]/ancestor::div[contains(@class,'form-group')]//input", "Дата видачі", "xpath"}
        };

        int testNumber = 1;
        for (String[] field : calendarFields) {
            System.out.println("\n🧪 Тест 7." + testNumber + ":  Календар '" + field[1] + "'");

            try {
                // Відкриваємо календар
                WebElement dateField;
                if (field[2].equals("name")) {
                    dateField = driver.findElement(By.name(field[0]));
                } else {
                    dateField = driver.findElement(By.xpath(field[0]));
                }

                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", dateField);

                Thread.sleep(500);
                dateField.click();
                Thread.sleep(1500);

                // Перевіряємо що календар відкрився
                List<WebElement> calendars = driver.findElements(
                        By.cssSelector(".mx-datepicker-main. mx-datepicker-popup"));

                boolean calendarOpened = ! calendars.isEmpty() && calendars.get(0).isDisplayed();

                if (calendarOpened) {
                    System.out.println("  ✓ Календар відкрився");

                    // Перевіряємо наявність кнопки підтвердження
                    boolean hasConfirmButton = checkCalendarHasConfirmButton();

                    if (hasConfirmButton) {
                        System. out.println("  ✓ Кнопка підтвердження присутня");
                    } else {
                        System.out. println("  ❌ Кнопка підтвердження ВІДСУТНЯ");
                    }

                    softAssert.assertTrue(hasConfirmButton,
                            "Календар '" + field[1] + "' має містити кнопку підтвердження");

                    // Перевіряємо наявність кнопок навігації
                    boolean hasPrevButton = checkCalendarHasPrevButton();
                    boolean hasNextButton = checkCalendarHasNextButton();

                    if (hasPrevButton) {
                        System.out. println("  ✓ Кнопка 'попередній місяць' присутня");
                    } else {
                        System.out.println("  ⚠ Кнопка 'попередній місяць' відсутня");
                    }

                    if (hasNextButton) {
                        System.out.println("  ✓ Кнопка 'наступний місяць' присутня");
                    } else {
                        System. out.println("  ⚠ Кнопка 'наступний місяць' відсутня");
                    }

                    softAssert.assertTrue(hasPrevButton,
                            "Календар має містити кнопку навігації 'назад'");
                    softAssert.assertTrue(hasNextButton,
                            "Календар має містити кнопку навігації 'вперед'");

                } else {
                    System.out.println("  ❌ Календар НЕ відкрився");
                    softAssert.fail("Календар '" + field[1] + "' не відкрився");
                }

                // Закриваємо календар
                driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys. ESCAPE);
                Thread.sleep(500);

            } catch (Exception e) {
                System.out.println("  ❌ Помилка при перевірці календаря:  " + e.getMessage());
                softAssert.fail("Помилка при перевірці календаря '" + field[1] + "': " + e.getMessage());
            }

            testNumber++;
        }

        takeScreenshot("calendars_consistency");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ ТЕСТ 7 ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));

        softAssert.assertAll();
    }

    // ==================== ДОПОМІЖНІ МЕТОДИ ====================

    private boolean checkEmailError() {
        try {
            // Шукаємо помилку біля поля email
            WebElement emailField = driver.findElement(By.name("enteredEmail"));
            WebElement parent = emailField.findElement(By.xpath("./ancestor::div[contains(@class,'form-group')]"));

            List<WebElement> errors = parent.findElements(By. cssSelector("span.error"));

            for (WebElement error : errors) {
                if (error.isDisplayed() && ! error.getText().trim().isEmpty()) {
                    System.out.println("  Повідомлення:  " + error.getText());
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkPhoneError() {
        try {
            WebElement phoneField = driver.findElement(By.name("enteredPhone"));
            WebElement parent = phoneField.findElement(By.xpath("./ancestor::div[contains(@class,'form-group')]"));

            List<WebElement> errors = parent.findElements(By.cssSelector("span.error"));

            for (WebElement error : errors) {
                if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                    System.out.println("  Повідомлення: " + error.getText());
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkFieldError(String fieldName) {
        try {
            WebElement field = driver.findElement(By. name(fieldName));
            WebElement parent = field.findElement(By.xpath("./ancestor::div[contains(@class,'form-group')]"));

            List<WebElement> errors = parent.findElements(By.cssSelector("span.error"));

            for (WebElement error : errors) {
                if (error.isDisplayed() && !error.getText().trim().isEmpty()) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkCalendarHasConfirmButton() {
        try {
            List<WebElement> confirmButtons = driver.findElements(
                    By.cssSelector(".mx-datepicker-btn-confirm, button.mx-btn-confirm"));

            for (WebElement btn : confirmButtons) {
                if (btn.isDisplayed()) {
                    System.out.println("    Текст кнопки: '" + btn.getText() + "'");
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkCalendarHasPrevButton() {
        try {
            List<WebElement> prevButtons = driver. findElements(
                    By.cssSelector("button.mx-btn-icon-left"));

            return !prevButtons.isEmpty() && prevButtons.get(0).isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkCalendarHasNextButton() {
        try {
            List<WebElement> nextButtons = driver.findElements(
                    By.cssSelector("button.mx-btn-icon-right"));

            return !nextButtons.isEmpty() && nextButtons.get(0).isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }

    private void fillFormExceptField(String fieldToSkip) {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        String travelDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (! fieldToSkip.equals("enteredLastnameContact")) {
            specialCarriagePage.fillContactLastName("Тест");
        }
        if (!fieldToSkip.equals("enteredFirstnameContact")) {
            specialCarriagePage. fillContactFirstName("Користувач");
        }
        if (!fieldToSkip.equals("enteredPhone")) {
            specialCarriagePage.fillPhone("501234567");
        }
        if (!fieldToSkip.equals("enteredEmail")) {
            specialCarriagePage.fillEmail("test@example.com");
        }

        specialCarriagePage.selectAccommodationType("3");
        specialCarriagePage.selectFromStation("Київ");
        specialCarriagePage.selectToStation("Львів");
        specialCarriagePage.selectTravelDate(travelDate);
        specialCarriagePage.fillTrainNumber("100");
        specialCarriagePage.selectPaymentStation("Київ");
        specialCarriagePage.selectPassengerCategory("4");

        if (!fieldToSkip. equals("enteredLastname1")) {
            specialCarriagePage.fillPassengerLastName("Тест");
        }
        if (! fieldToSkip.equals("enteredFirstname")) {
            specialCarriagePage.fillPassengerFirstName("Користувач");
        }

        specialCarriagePage.fillIdCardNumber("ТС12345678");
        specialCarriagePage.fillIssuedBy("Тестова служба");

        // Дата видачі
        try {
            WebElement issueDateField = driver.findElement(
                    By.xpath("//label[contains(text(), 'Дата видачі')]/ancestor::div[contains(@class,'form-group')]//input"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = '01.01.2024';", issueDateField);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", issueDateField);
        } catch (Exception ignored) {}

        specialCarriagePage.acceptAgreement();
    }

    private void fillAllFieldsWithoutAgreement() {
        LocalDate futureDate = LocalDate. now().plusDays(10);
        String travelDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        specialCarriagePage
                .fillContactLastName("Шевченко")
                .fillContactFirstName("Тарас")
                .fillPhone("501234567")
                .fillEmail("test@example.com")
                .selectAccommodationType("3")
                .selectFromStation("Київ")
                .selectToStation("Одеса")
                .selectTravelDate(travelDate)
                .fillTrainNumber("143")
                .selectPaymentStation("Київ")
                .selectPassengerCategory("4")
                .fillPassengerLastName("Шевченко")
                .fillPassengerFirstName("Тарас")
                .fillIdCardNumber("АВ12345678")
                .fillIssuedBy("Міграційна служба");

        // Дата видачі
        try {
            WebElement issueDateField = driver.findElement(
                    By.xpath("//label[contains(text(), 'Дата видачі')]/ancestor::div[contains(@class,'form-group')]//input"));
            ((org.openqa.selenium. JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = '22.11.2024';", issueDateField);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new Event('change', { bubbles:  true }));", issueDateField);
        } catch (Exception ignored) {}
    }

    private void fillFieldsBeforeCalendars() {
        specialCarriagePage
                .fillContactLastName("Тест")
                .fillContactFirstName("Користувач")
                .fillPhone("501234567")
                .fillEmail("test@example.com")
                .selectAccommodationType("3")
                .selectFromStation("Київ")
                .selectToStation("Львів");
    }

    private void takeScreenshot(String name) {
        try {
            java.io.File screenshot = ((org.openqa.selenium.TakesScreenshot) driver)
                    .getScreenshotAs(org.openqa.selenium.OutputType.FILE);

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
            System.out.println("⚠ Не вдалося зробити скріншот: " + e. getMessage());
        }
    }
}