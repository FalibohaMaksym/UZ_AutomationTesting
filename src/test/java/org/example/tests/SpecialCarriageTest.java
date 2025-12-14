package org.example.tests;

import org.example.models.PassengerData;
import org.example.pages.SpecialCarriagePage;
import org.example.utils.WebSearchHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome. ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SpecialCarriageTest {

    private WebDriver driver;
    private SpecialCarriagePage specialCarriagePage;
    private WebDriverWait wait;

    @BeforeClass
    public void setupClass() {
        System.out.println("=". repeat(60));
        System.out.println("ПОЧАТОК ТЕСТУВАННЯ:  Special Carriage Form");
        System.out.println("=".repeat(60));
    }

    @BeforeMethod
    public void setup() {
        System.out. println("\n🚀 Ініціалізація WebDriver.. .");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        specialCarriagePage = new SpecialCarriagePage(driver);
        System.out. println("✓ WebDriver ініціалізовано успішно");
    }

    @Test(priority = 1, description = "Перевірка відкриття сторінки форми")
    public void testPageOpens() {
        System.out. println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 1: Перевірка відкриття сторінки");
        System.out.println("=".repeat(60));

        specialCarriagePage.open();

        Assert.assertTrue(specialCarriagePage.isFormDisplayed(),
                "Форма не відображається на сторінці");
        Assert.assertTrue(specialCarriagePage.getCurrentUrl().contains("special-carriage"),
                "URL не містить 'special-carriage'");

        System.out.println("✓ Сторінка відкрита успішно");
        System.out.println("✓ Форма відображається");
        takeScreenshot("page_opened");
    }

    @Test(priority = 2, description = "Заповнення форми з валідними даними")
    public void testFillFormWithValidData() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 2: Заповнення форми з валідними даними");
        System.out. println("=".repeat(60));

        // Підготовка тестових даних
        PassengerData testData = new PassengerData.Builder()
                .contactLastName("Іваненко")
                .contactFirstName("Іван")
                .phone("996022001")
                .email("test.automation@gmail.com")
                .accommodationType("3") // ID радіокнопки
                .fromStation("Київ")
                .toStation("Львів")
                .travelDate("2025-12-18")
                .trainNumber("052")
                .paymentStation("Київ")
                .passengerCategory("4") // ID радіокнопки
                . passengerLastName("Василенко")
                .passengerFirstName("Петро")
                .idCardNumber("ВК14365211")
                .issueDate("2024-12-25")
                .issuedBy("Тестова організація")
                .documentPath("src/main/resources/images/rizhiy_kot-1024. jpg")
                .build();

        // Відкриваємо сторінку та заповнюємо форму
        specialCarriagePage. open();

        System.out.println("\n📝 Заповнення контактної інформації.. .");
        specialCarriagePage
                .fillContactLastName(testData.getContactLastName())
                .fillContactFirstName(testData.getContactFirstName())
                .fillPhone(testData.getPhone())
                .fillEmail(testData.getEmail())
                .selectAccommodationType(testData.getAccommodationType());
        System.out.println("✓ Контактна інформація заповнена");

        System.out.println("\n🚂 Заповнення інформації про поїздку...");
        specialCarriagePage
                .selectFromStation(testData.getFromStation())
                .selectToStation(testData.getToStation())
                .selectTravelDate(testData.getTravelDate())
                .fillTrainNumber(testData.getTrainNumber())
                .selectPaymentStation(testData.getPaymentStation());
        System.out.println("✓ Інформація про поїздку заповнена");

        takeScreenshot("form_trip_info_filled");

        System.out.println("\n👤 Заповнення даних пасажира...");
        specialCarriagePage
                .selectPassengerCategory(testData.getPassengerCategory())
                .fillPassengerLastName(testData.getPassengerLastName())
                .fillPassengerFirstName(testData.getPassengerFirstName())
                .fillIdCardNumber(testData.getIdCardNumber())
                .selectIssueDate(testData.getIssueDate())
                .fillIssuedBy(testData.getIssuedBy());
        System.out.println("✓ Дані пасажира заповнені");

        System.out. println("\n📎 Завантаження документа...");
        specialCarriagePage. uploadDocument(testData.getDocumentPath());
        System.out.println("✓ Документ завантажено");

        takeScreenshot("form_fully_filled");

        Assert.assertTrue(specialCarriagePage.isSubmitButtonVisible(),
                "Кнопка відправки не відображається");

        System.out.println("\n✓ ФОРМА УСПІШНО ЗАПОВНЕНА!");
    }

    @Test(priority = 3, description = "Повний тест:  заповнення та відправка форми")
    public void testCompleteFormSubmission() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 3: Повний цикл - заповнення та відправка");
        System.out.println("=".repeat(60));

        // Підготовка тестових даних
        PassengerData testData = new PassengerData.Builder()
                .contactLastName("Шевченко")
                .contactFirstName("Тарас")
                .phone("501234567")
                .email("shevchenko.test@ukr.net")
                .accommodationType("3")
                .fromStation("Київ")
                .toStation("Одеса")
                .travelDate("2025-12-20")
                .trainNumber("143")
                .paymentStation("Київ")
                .passengerCategory("4")
                .passengerLastName("Шевченко")
                .passengerFirstName("Тарас")
                .idCardNumber("АВ12345678")
                .issueDate("2024-11-22")
                .issuedBy("Міграційна служба")
                .documentPath("src/main/resources/images/rizhiy_kot-1024.jpg")
                .build();

        // Відкриваємо та заповнюємо форму
        System.out.println("\n📋 Заповнення форми...");
        specialCarriagePage.open();
        specialCarriagePage.fillForm(testData);

        takeScreenshot("before_submit");

        // Приймаємо угоду
        System.out. println("\n☑ Прийняття угоди...");
        specialCarriagePage.acceptAgreement();

        // Відправляємо форму
        System.out.println("\n📤 Відправка форми.. .");
        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Оформити заявку')]"));
        WebSearchHelper.scrollToElement(driver, submitButton);
        WebSearchHelper.clickElement(driver, submitButton);

        // Очікуємо редірект на сторінку успіху
        System.out.println("\n⏳ Очікування результату...");
        try {
            Thread.sleep(5000); // Даємо час на обробку
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        takeScreenshot("after_submit");

        // Перевіряємо редірект
        String currentUrl = specialCarriagePage. getCurrentUrl();
        System.out.println("📍 Поточний URL: " + currentUrl);

        if (currentUrl.contains("success-special")) {
            String applicationId = specialCarriagePage.getApplicationId();

            System.out.println("\n" + "=".repeat(60));
            System.out.println("✓ ФОРМУ УСПІШНО ВІДПРАВЛЕНО!");
            System.out.println("=".repeat(60));
            System.out.println("📋 ID заявки: " + applicationId);
            System.out.println("📧 Email: " + testData.getEmail());
            System.out.println("=".repeat(60));

            Assert.assertTrue(specialCarriagePage.isSuccessPageDisplayed(),
                    "Сторінка успіху не відображається");
            Assert.assertNotNull(applicationId, "ID заявки не отримано");

        } else {
            System.out.println("\n⚠ УВАГА: Редірект на сторінку успіху не відбувся");
            System.out.println("Можливі причини:");
            System.out.println("  - Помилки валідації");
            System.out.println("  - Проблеми з мережею");
            System.out. println("  - Сервер не відповідає");

            takeScreenshot("submission_failed");

            // Не провалюємо тест, якщо це тестове середовище
            Assert.fail("Форма не була відправлена.  URL:  " + currentUrl);
        }
    }

    @Test(priority = 4, description = "Тест з використанням Builder pattern")
    public void testFormFillingWithBuilder() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 4: Заповнення форми через Builder");
        System.out.println("=".repeat(60));

        PassengerData data = createTestPassengerData("Коваленко", "Олександр", "test4@example.com");

        specialCarriagePage.open()
                .fillForm(data)
                .acceptAgreement();

        Assert.assertTrue(specialCarriagePage.isSubmitButtonVisible(),
                "Кнопка відправки не видима після заповнення форми");

        System.out. println("✓ Форма заповнена через Builder pattern");
        takeScreenshot("builder_test_completed");
    }

    @Test(priority = 5, description = "Тест заповнення мінімальних обов'язкових полів")
    public void testMinimalRequiredFields() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ 5: Мінімальний набір обов'язкових полів");
        System.out. println("=".repeat(60));

        specialCarriagePage.open()
                .fillContactLastName("Тест")
                .fillContactFirstName("Користувач")
                .fillPhone("501111111")
                .fillEmail("minimal@test.com")
                .selectAccommodationType("3")
                .selectFromStation("Київ")
                .selectToStation("Харків")
                .selectTravelDate("2025-12-19")
                .fillTrainNumber("100")
                .selectPaymentStation("Київ")
                .selectPassengerCategory("4")
                .fillPassengerLastName("Тест")
                .fillPassengerFirstName("Користувач")
                .fillIdCardNumber("ТС11111111")
                .selectIssueDate("2023-01-01")
                .fillIssuedBy("Тестова служба");

        takeScreenshot("minimal_fields_filled");

        Assert.assertTrue(specialCarriagePage.isSubmitButtonVisible(),
                "Кнопка відправки не видима");

        System.out.println("✓ Мінімальні поля заповнені");
    }

    // ==================== ДОПОМІЖНІ МЕТОДИ ====================

    private PassengerData createTestPassengerData(String lastName, String firstName, String email) {
        return new PassengerData.Builder()
                .contactLastName(lastName)
                .contactFirstName(firstName)
                .phone("500000000")
                .email(email)
                .accommodationType("3")
                .fromStation("Київ")
                .toStation("Дніпро")
                .travelDate("2025-12-21")
                .trainNumber("077")
                .paymentStation("Київ")
                .passengerCategory("4")
                .passengerLastName(lastName)
                .passengerFirstName(firstName)
                .idCardNumber("ТТ00000000")
                .issueDate("2022-01-01")
                .issuedBy("Test Authority")
                .documentPath("src/main/resources/images/rizhiy_kot-1024.jpg")
                .build();
    }

    private void takeScreenshot(String fileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            File destination = new File("test-screenshots/" + fileName + "_" + timestamp + ".png");

            destination.getParentFile().mkdirs();

            Files.copy(screenshot.toPath(), destination.toPath(),
                    StandardCopyOption. REPLACE_EXISTING);

            System.out.println("📸 Скріншот:  " + destination.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("⚠ Не вдалося зробити скріншот: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            System.out.println("\n🛑 Закриття браузера...");
            try {
                Thread.sleep(2000); // Пауза для перегляду результату
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
            System.out.println("✓ Браузер закрито");
        }
    }

    @AfterClass
    public void tearDownClass() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТУВАННЯ ЗАВЕРШЕНО");
        System.out.println("=".repeat(60));
    }
}