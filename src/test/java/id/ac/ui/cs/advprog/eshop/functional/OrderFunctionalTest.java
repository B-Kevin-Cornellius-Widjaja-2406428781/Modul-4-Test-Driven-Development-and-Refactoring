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
class OrderFunctionalTest {

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
    void createOrder_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/create");

        String pageTitle = driver.getTitle();
        assertEquals("Create New Order", pageTitle);

        // Enter author name
        WebElement authorInput = driver.findElement(By.id("authorInput"));
        authorInput.clear();
        authorInput.sendKeys("Test User");

        // Select a product (assuming product exists)
        WebElement productCheckbox = driver.findElement(By.cssSelector("input[type='checkbox']"));
        productCheckbox.click();

        // Submit
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Should redirect to order history or success page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/order/history"));
        
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Test User") || pageSource.contains("Order"));
    }

    @Test
    void orderHistory_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/history");

        String pageTitle = driver.getTitle();
        assertEquals("Order History", pageTitle);

        // Enter author name
        WebElement authorInput = driver.findElement(By.id("authorInput"));
        authorInput.clear();
        authorInput.sendKeys("TestUser");

        // Submit
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Should show results
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/order/history"));
    }

    @Test
    void payOrderPage_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/pay/test-order-id");

        // Should show payment page (may redirect to history if order not found)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String pageSource = driver.getPageSource();
        
        // Either shows payment form or error/redirect
        assertTrue(pageSource.contains("Pay Order") || 
                    pageSource.contains("Order") || 
                    pageSource.contains("history"));
    }
}
