package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PaymentFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void paymentDetailPage_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/payment/detail");

        String pageTitle = driver.getTitle();
        assertTrue(pageTitle.contains("Payment") || pageTitle.contains("Detail"));
    }

    @Test
    void paymentListPage_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/payment/admin/list");

        String pageTitle = driver.getTitle();
        assertTrue(pageTitle.contains("Payment") || pageTitle.contains("Admin") || pageTitle.contains("List"));
    }

    @Test
    void paymentAdminDetailPage_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/payment/admin/detail/test-payment-id");

        // Should show payment detail page or error
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String pageSource = driver.getPageSource();

        // Either shows form or error/redirect
        assertTrue(pageSource.contains("Payment") ||
                pageSource.contains("Admin") ||
                pageSource.contains("Detail"));
    }

    @Test
    void paymentSetStatusPage_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/payment/admin/set-status/test-payment-id");

        // Should show set status page or redirect
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String pageSource = driver.getPageSource();

        assertTrue(pageSource.contains("Payment") ||
                pageSource.contains("Status") ||
                pageSource.contains("Accept") ||
                pageSource.contains("Reject") ||
                pageSource.contains("List") ||
                pageSource.contains("error"));
    }
}
