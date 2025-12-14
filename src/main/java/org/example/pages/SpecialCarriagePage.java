package org.example.pages;

import org.openqa. selenium.By;
import org. openqa.selenium.Keys;
import org.openqa. selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa. selenium.support.ui.ExpectedConditions;
import org.example.models.PassengerData;
import java.util.List;
import java.io.File;

public class SpecialCarriagePage extends BasePage {

    // Локатори
    private final By contactLastNameInput = By. name("enteredLastnameContact");
    private final By contactFirstNameInput = By.name("enteredFirstnameContact");
    private final By phoneInput = By. name("enteredPhone");
    private final By emailInput = By.name("enteredEmail");
    private final By fromStationInput = By.xpath("//label[contains(text(), 'Звідки')]/following-sibling::div//input");
    private final By toStationInput = By.xpath("//label[contains(text(), 'Куди')]/following-sibling:: div//input");
    private final By travelDateInput = By. name("date");
    private final By trainNumberInput = By.name("enteredTrain");
    private final By paymentStationInput = By.xpath("//label[contains(text(), 'Станція оплати')]/../following-sibling::div//input");
    private final By passengerLastNameInput = By.name("enteredLastname1");
    private final By passengerFirstNameInput = By.name("enteredFirstname");
    private final By idCardInput = By.xpath("//label[contains(text(), 'Серія та № посвідчення')]/following-sibling::input");
    private final By issueDateInput = By.xpath("//label[contains(text(), 'Дата видачі')]/ancestor::div[contains(@class,'form-group')]//input");
    private final By issuedByInput = By.xpath("//label[contains(text(), 'Ким видано')]/following-sibling::div//input");
    private final By fileUploadInput = By.id("assetsFieldHandle");
    private final By agreementCheckbox = By.id("submitTerms");
    private final By submitButton = By.xpath("//button[contains(text(), 'Оформити заявку')]");
    private final By calendarConfirmButton = By.cssSelector(".mx-datepicker-btn-confirm");
    private final By prevMonthButton = By.cssSelector("button.mx-btn-icon-left");

    // URL
    private static final String PAGE_URL = "https://services.uz.gov.ua/special-carriage";

    public SpecialCarriagePage(WebDriver driver) {
        super(driver);
    }

    // ==================== ОСНОВНІ МЕТОДИ ====================

    public SpecialCarriagePage open() {
        driver.get(PAGE_URL);
        driver.manage().window().maximize();
        return this;
    }

    // ==================== КОНТАКТНА ІНФОРМАЦІЯ ====================

    public SpecialCarriagePage fillContactLastName(String lastName) {
        WebElement element = waitForElement(contactLastNameInput);
        typeText(element, lastName);
        return this;
    }

    public SpecialCarriagePage fillContactFirstName(String firstName) {
        WebElement element = driver.findElement(contactFirstNameInput);
        typeText(element, firstName);
        return this;
    }

    public SpecialCarriagePage fillPhone(String phone) {
        WebElement element = driver.findElement(phoneInput);
        typeText(element, phone);
        return this;
    }

    public SpecialCarriagePage fillEmail(String email) {
        WebElement element = driver.findElement(emailInput);
        typeText(element, email);
        return this;
    }

    public SpecialCarriagePage selectAccommodationType(String radioId) {
        WebElement radioLabel = driver.findElement(By. cssSelector("label[for='" + radioId + "']"));
        clickElement(radioLabel);
        return this;
    }

    // ==================== ІНФОРМАЦІЯ ПРО ПОЇЗДКУ ====================

    public SpecialCarriagePage selectFromStation(String station) {
        WebElement element = driver.findElement(fromStationInput);
        selectFromDropdown(element, station, 500);
        return this;
    }

    public SpecialCarriagePage selectToStation(String station) {
        WebElement element = driver.findElement(toStationInput);
        selectFromDropdown(element, station, 1500);
        return this;
    }

    public SpecialCarriagePage selectTravelDate(String date) {
        WebElement dateField = waitForElement(travelDateInput);
        scrollToElement(dateField);
        clickElement(dateField);
        sleep(1000);

        debugCalendar();

        try {
            selectDateInCalendar(date, 0);
            closeCalendarIfOpen();
        } catch (Exception e) {
            System.out.println("⚠ Помилка вибору дати: " + e.getMessage());
        }

        return this;
    }

    public SpecialCarriagePage fillTrainNumber(String trainNumber) {
        closeCalendarIfOpen();
        sleep(300);

        WebElement element = driver.findElement(trainNumberInput);
        scrollToElement(element);
        typeText(element, trainNumber);
        sleep(500);
        return this;
    }

    public SpecialCarriagePage selectPaymentStation(String station) {
        closeCalendarIfOpen();
        sleep(300);

        WebElement element = driver.findElement(paymentStationInput);
        scrollToElement(element);

        js.executeScript("arguments[0]. scrollIntoView({block: 'center'});", element);
        sleep(300);
        js.executeScript("arguments[0]. click();", element);

        element. sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys. BACK_SPACE);
        element.sendKeys(station);
        sleep(1000);
        element.sendKeys(Keys.ARROW_DOWN);
        element.sendKeys(Keys.ENTER);
        sleep(500);
        return this;
    }

    // ==================== ДАНІ ПАСАЖИРА ====================

    public SpecialCarriagePage selectPassengerCategory(String categoryId) {
        WebElement categoryLabel = driver.findElement(By.cssSelector("label[for='" + categoryId + "']"));
        scrollToElement(categoryLabel);
        clickElement(categoryLabel);
        sleep(500);
        return this;
    }

    public SpecialCarriagePage fillPassengerLastName(String lastName) {
        WebElement element = driver.findElement(passengerLastNameInput);
        scrollToElement(element);
        replaceText(element, lastName);
        sleep(500);
        return this;
    }

    public SpecialCarriagePage fillPassengerFirstName(String firstName) {
        WebElement element = driver.findElement(passengerFirstNameInput);
        replaceText(element, firstName);
        sleep(500);
        return this;
    }

    public SpecialCarriagePage fillIdCardNumber(String idCard) {
        WebElement element = driver.findElement(idCardInput);
        scrollToElement(element);
        typeText(element, idCard);
        sleep(500);
        return this;
    }

    public SpecialCarriagePage selectIssueDate(String date) {
        System.out.println("\n📅 Встановлення дати видачі:  " + date);

        try {
            WebElement dateField = findIssueDateField();
            scrollToElement(dateField);
            sleep(500);

            int monthsBack = calculateMonthsBack(date);
            System.out.println("  Потрібно:  " + monthsBack + " місяців назад");

            // Якщо менше 2 місяців - спробуємо через календар
            if (monthsBack <= 2) {
                System.out.println("  → Спроба через календар");
                try {
                    dateField.click();
                    sleep(1000);

                    selectDateInCalendar(date, monthsBack);
                    closeCalendarIfOpen();

                    String actualValue = dateField.getAttribute("value");
                    if (actualValue != null && !actualValue.isEmpty()) {
                        System.out.println("✓ Дату встановлено через календар: " + actualValue);
                        return this;
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ Календар не спрацював: " + e. getMessage());
                    closeCalendarIfOpen();
                }
            }

            // Для давніх дат або якщо календар не спрацював
            System.out.println("  → Використовую альтернативні способи");
            setDateDirectly(dateField, date);

            String finalValue = dateField.getAttribute("value");
            if (finalValue == null || finalValue.isEmpty()) {
                System.out.println("  ❌ КРИТИЧНО: Дата НЕ встановлена!");
            } else {
                System.out.println("✓ Дату видачі встановлено: " + finalValue);
            }

        } catch (Exception e) {
            System.out.println("✗ Помилка встановлення дати видачі: " + e.getMessage());
            e.printStackTrace();
        }

        return this;
    }

    public SpecialCarriagePage fillIssuedBy(String issuedBy) {
        WebElement element = driver.findElement(issuedByInput);
        scrollToElement(element);
        typeText(element, issuedBy);
        return this;
    }

    // ==================== ЗАВАНТАЖЕННЯ ДОКУМЕНТА ====================

    public SpecialCarriagePage uploadDocument(String filePath) {
        uploadFile(filePath);
        return this;
    }

    // ==================== ПІДТВЕРДЖЕННЯ ТА ВІДПРАВКА ====================

    public SpecialCarriagePage acceptAgreement() {
        WebElement checkbox = driver.findElement(agreementCheckbox);
        js.executeScript("arguments[0].click();", checkbox);
        return this;
    }

    public void submit() {
        WebElement button = driver.findElement(submitButton);
        scrollToElement(button);
        sleep(1000);
        clickElement(button);
    }

    // ==================== МЕТОД ДЛЯ ЗАПОВНЕННЯ ВСІЄЇ ФОРМИ ====================

    public SpecialCarriagePage fillForm(PassengerData data) {
        fillContactLastName(data.getContactLastName());
        fillContactFirstName(data.getContactFirstName());
        fillPhone(data. getPhone());
        fillEmail(data.getEmail());
        selectAccommodationType(data.getAccommodationType());

        selectFromStation(data. getFromStation());
        selectToStation(data.getToStation());
        selectTravelDate(data.getTravelDate());
        fillTrainNumber(data.getTrainNumber());
        selectPaymentStation(data.getPaymentStation());

        selectPassengerCategory(data.getPassengerCategory());
        fillPassengerLastName(data.getPassengerLastName());
        fillPassengerFirstName(data.getPassengerFirstName());
        fillIdCardNumber(data.getIdCardNumber());
        selectIssueDate(data.getIssueDate());
        fillIssuedBy(data.getIssuedBy());

        if (data.getDocumentPath() != null && !data.getDocumentPath().isEmpty()) {
            uploadDocument(data. getDocumentPath());
        }

        return this;
    }

    // ==================== ДОПОМІЖНІ МЕТОДИ ====================

    private void selectDateInCalendar(String dateToSelect, int monthsBack) throws Exception {
        sleep(500);

        if (monthsBack > 0) {
            for (int i = 0; i < monthsBack; i++) {
                try {
                    WebElement prevButton = waitForClickable(prevMonthButton);
                    js.executeScript("arguments[0]. click();", prevButton);
                    sleep(400);
                } catch (Exception e) {
                    System.out.println("⚠ Не вдалося перейти на попередній місяць:  " + e.getMessage());
                }
            }
        }

        sleep(500);

        try {
            WebElement dayCell = wait. until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//td[@title='" + dateToSelect + "']")));

            String classAttr = dayCell.getAttribute("class");
            boolean isDisabled = classAttr != null && classAttr.contains("disabled");

            if (isDisabled) {
                System.out.println("⚠ УВАГА: Дата " + dateToSelect + " disabled, але спробую вибрати");
            }

            wait.until(ExpectedConditions. visibilityOf(dayCell));
            scrollToElement(dayCell);
            js.executeScript("arguments[0]. click();", dayCell);

            System.out.println("✓ Вибрано дату: " + dateToSelect);
        } catch (Exception e) {
            System.out.println("✗ Помилка при виборі дати " + dateToSelect + ": " + e.getMessage());
            throw e;
        }

        sleep(500);

        try {
            WebElement confirmButton = wait.until(ExpectedConditions.presenceOfElementLocated(calendarConfirmButton));
            wait.until(ExpectedConditions. elementToBeClickable(confirmButton));
            js.executeScript("arguments[0]. click();", confirmButton);
            System.out.println("✓ Дату підтверджено");
            sleep(500);
        } catch (Exception e) {
            System.out. println("⚠ Кнопка підтвердження не знайдена");
        }
    }

    private void closeCalendarIfOpen() {
        try {
            List<WebElement> calendars = driver.findElements(
                    By.cssSelector(".mx-datepicker-main. mx-datepicker-popup"));

            if (!calendars. isEmpty() && calendars.get(0).isDisplayed()) {
                System.out.println("⚠ Календар відкритий, закриваю.. .");

                driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
                sleep(500);

                try {
                    if (calendars.get(0).isDisplayed()) {
                        js.executeScript(
                                "let calendars = document.querySelectorAll('.mx-datepicker-main.mx-datepicker-popup');" +
                                        "calendars. forEach(cal => cal.style.display = 'none');"
                        );
                    }
                } catch (Exception ignored) {}

                System.out.println("✓ Календар закрито");
                sleep(300);
            }
        } catch (Exception e) {
            // Календар вже закритий
        }
    }

    private WebElement findIssueDateField() {
        System.out.println("  🔍 Пошук поля дати видачі.. .");

        try {
            WebElement el = driver.findElement(issueDateInput);
            if (el. isDisplayed()) {
                System.out.println("  ✓ Знайдено через основний локатор");
                return el;
            }
        } catch (Exception ignored) {}

        try {
            WebElement el = driver. findElement(By.xpath(
                    "//label[contains(text(), 'Дата видачі')]/..//input"
            ));
            if (el.isDisplayed()) {
                System.out.println("  ✓ Знайдено через альтернативний XPath");
                return el;
            }
        } catch (Exception ignored) {}

        throw new RuntimeException("❌ Поле дати видачі не знайдено!");
    }

    private int calculateMonthsBack(String targetDate) {
        try {
            String[] parts = targetDate.split("-");
            int targetYear = Integer.parseInt(parts[0]);
            int targetMonth = Integer.parseInt(parts[1]);

            java.time.LocalDate now = java.time.LocalDate. now();
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();

            int monthsBack = (currentYear - targetYear) * 12 + (currentMonth - targetMonth);

            return Math.max(0, monthsBack);

        } catch (Exception e) {
            System.out.println("⚠ Помилка розрахунку: " + e.getMessage());
            return 0;
        }
    }

    private void setDateDirectly(WebElement dateField, String date) {
        try {
            System.out.println("  → Спроба встановити дату: " + date);

            js.executeScript("arguments[0]. scrollIntoView({block: 'center'});", dateField);
            sleep(500);

            // СПОСІБ 1: sendKeys
            System.out.println("  → Спосіб 1: Введення тексту");
            try {
                dateField.click();
                sleep(300);

                dateField.clear();
                sleep(200);

                dateField.sendKeys(date);
                sleep(300);

                dateField.sendKeys(Keys.TAB);
                sleep(500);

                String value1 = dateField.getAttribute("value");
                if (value1 != null && !value1.isEmpty()) {
                    System.out.println("  ✓ Спосіб 1 спрацював:  " + value1);
                    return;
                }
            } catch (Exception e) {
                System.out.println("  ✗ Спосіб 1 помилка: " + e.getMessage());
            }

            // СПОСІБ 2: JavaScript
            System.out.println("  → Спосіб 2: JavaScript");
            try {
                js.executeScript("arguments[0]. value = arguments[1];", dateField, date);
                js.executeScript(
                        "arguments[0]. dispatchEvent(new Event('input', { bubbles: true }));" +
                                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                                "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                        dateField
                );
                sleep(500);

                String value2 = dateField.getAttribute("value");
                if (value2 != null && !value2.isEmpty()) {
                    System.out.println("  ✓ Спосіб 2 спрацював: " + value2);
                    return;
                }
            } catch (Exception e) {
                System.out.println("  ✗ Спосіб 2 помилка: " + e.getMessage());
            }

            // СПОСІБ 3: Формат dd. MM.yyyy
            System.out.println("  → Спосіб 3: Інший формат дати");
            try {
                String[] parts = date.split("-");
                String formattedDate = parts[2] + "." + parts[1] + "." + parts[0];

                System.out.println("     Формат: " + formattedDate);

                dateField.click();
                sleep(300);

                dateField.clear();
                dateField.sendKeys(formattedDate);
                dateField.sendKeys(Keys.TAB);
                sleep(500);

                String value3 = dateField.getAttribute("value");
                if (value3 != null && !value3.isEmpty()) {
                    System.out. println("  ✓ Спосіб 3 спрацював: " + value3);
                    return;
                }
            } catch (Exception e) {
                System.out. println("  ✗ Спосіб 3 помилка: " + e.getMessage());
            }

            System.out.println("  ❌ Жоден спосіб не спрацював!");

        } catch (Exception e) {
            System.out.println("  ✗ Критична помилка: " + e.getMessage());
        }
    }

    private void debugCalendar() {
        try {
            System.out.println("\n=== ДІАГНОСТИКА КАЛЕНДАРЯ ===");

            List<WebElement> calendars = driver.findElements(
                    By.cssSelector(".mx-datepicker-main.mx-datepicker-popup"));

            if (calendars.isEmpty()) {
                System.out.println("❌ Календар НЕ відкритий!");
                return;
            }

            WebElement calendar = calendars.get(0);
            if (!calendar.isDisplayed()) {
                System.out.println("❌ Календар існує, але НЕ видимий!");
                return;
            }

            System.out.println("✓ Календар відкритий");

            try {
                WebElement monthBtn = calendar.findElement(By.cssSelector(".mx-btn-current-month"));
                WebElement yearBtn = calendar.findElement(By.cssSelector(".mx-btn-current-year"));
                System.out.println("📅 Поточний місяць: " + monthBtn.getText());
                System.out.println("📅 Поточний рік: " + yearBtn. getText());
            } catch (Exception e) {
                System.out.println("⚠ Не вдалося прочитати місяць/рік");
            }

            List<WebElement> allDates = calendar.findElements(By.cssSelector("td.cell"));
            System.out.println("\n📋 Доступні дати в календарі:");

            int count = 0;
            for (WebElement dateCell : allDates) {
                String title = dateCell.getAttribute("title");
                String classes = dateCell.getAttribute("class");

                if (title != null && !title.isEmpty() && ! classes.contains("not-current-month")) {
                    boolean isDisabled = classes.contains("disabled");
                    boolean isToday = classes.contains("today");

                    String status = isDisabled ? " [DISABLED]" : " [OK]";
                    if (isToday) status += " [TODAY]";

                    System.out.println("  - " + title + status);
                    count++;

                    if (count >= 10) {
                        System.out.println("  ...  (показано перші 10 дат)");
                        break;
                    }
                }
            }

            System.out.println("=== КІНЕЦЬ ДІАГНОСТИКИ ===\n");

        } catch (Exception e) {
            System.out.println("❌ Помилка діагностики: " + e. getMessage());
        }
    }

    private void uploadFile(String fileName) {
        try {
            File file;

            File tempFile = new File(fileName);
            if (tempFile.isAbsolute()) {
                file = tempFile;
            } else {
                String projectDir = System.getProperty("user.dir");
                file = new File(projectDir, fileName);
            }

            System.out.println("📄 Робота з файлом: " + file.getAbsolutePath());

            if (! file.exists()) {
                System.out.println("⚠ Файл не знайдено.  Створюю тестовий файл...");

                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                    System.out.println("✓ Створено директорії: " + parentDir.getAbsolutePath());
                }

                try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
                    writer.write("Тестовий документ для автоматизації\n");
                    writer.write("Дата створення: " + java.time.LocalDateTime.now() + "\n");
                    writer.write("Файл: " + file.getName() + "\n");
                }

                System.out.println("✓ Файл створено:  " + file.getAbsolutePath());
            } else {
                System.out. println("✓ Файл існує: " + file.getAbsolutePath());
            }

            String absolutePath = file.getAbsolutePath();

            WebElement fileInput = wait.until(ExpectedConditions. presenceOfElementLocated(fileUploadInput));

            js.executeScript(
                    "arguments[0].style.opacity = '1';" +
                            "arguments[0].style.display = 'block';" +
                            "arguments[0].style.visibility = 'visible';" +
                            "arguments[0].classList.remove('opacity-0', 'hide');",
                    fileInput
            );

            fileInput.sendKeys(absolutePath);
            System.out.println("✓ Документ успішно завантажено:  " + file.getName());

            sleep(500);

            try {
                WebElement uploadedFile = driver.findElement(
                        By.xpath("//*[contains(text(), '" + file.getName() + "')]"));
                System.out. println("✓ Файл відображається на сторінці");
            } catch (Exception e) {
                System. out.println("⚠ Файл завантажено, але не відображається");
            }

        } catch (Exception e) {
            System.out.println("✗ Помилка при завантаженні файлу: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== ПЕРЕВІРКИ ====================

    public boolean isSubmitButtonVisible() {
        try {
            WebElement button = driver.findElement(submitButton);
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFormDisplayed() {
        try {
            WebElement firstField = driver.findElement(contactLastNameInput);
            return firstField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver. getCurrentUrl();
    }

    public boolean isSuccessPageDisplayed() {
        sleep(2000);
        return driver.getCurrentUrl().contains("success-special");
    }

    public String getApplicationId() {
        String url = driver.getCurrentUrl();
        return extractParameterFromUrl(url, "id");
    }

    private String extractParameterFromUrl(String url, String parameterName) {
        try {
            String[] parts = url.split("\\?");
            if (parts. length > 1) {
                String[] params = parts[1].split("&");
                for (String param :  params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals(parameterName)) {
                        return keyValue[1];
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Помилка при парсингу URL: " + e.getMessage());
        }
        return null;
    }
}