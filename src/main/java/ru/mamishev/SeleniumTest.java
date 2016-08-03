package ru.mamishev;

import junit.textui.TestRunner;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * @author MamishevDA
 *         02.08.2016
 */
public class SeleniumTest extends TestRunner {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("http://wrike.com/wa");
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
    }

   /* @After
    public void shutDown() {
        driver.quit();
    }*/

    @Test
    public void navigateByClick() {
        WebElement navigateElement = driver.findElement(By.cssSelector(".wg-header__desktop-primary-nav-item.wg-header__desktop-primary-nav-item--solutions.wg-header__desktop-primary-nav-item--no-link.wg-header__desktop-primary-nav-item--parent"));
        navigateElement.click();
        WebElement webElement = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".wg-header__sticky-submenu-item.wg-header__sticky-submenu-item--dev")));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", webElement);
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-id-36")));
        //driver.getPageSource();
        System.out.println(driver.getTitle());
        assertEquals("https://www.wrike.com/product-management-software/", driver.getCurrentUrl());
    }

    @Test
    public void checkEmail() {
        WebElement inputElement = driver.findElement(By.cssSelector(".wg-input.global-form__input"));
        inputElement.sendKeys("someadress");
        WebElement button = driver.findElement(By.cssSelector(".wg-btn.wg-btn--green.global-form__btn"));
        button.click();
        WebElement errorLabel = driver.findElement(By.cssSelector(".wg-field-error.wg-tooltip.wg-tooltip--error.wg-field-error--visible"));
        System.out.println(errorLabel.getText());
        assertEquals("Enter your email", errorLabel.getText());
        driver.navigate().refresh();
        Boolean isPresent = driver.findElements(By.cssSelector(".wg-field-error.wg-tooltip.wg-tooltip--error.wg-field-error--visible")).size() > 0;
        // List list  = driver.findElements(By.cssSelector(".wg-field-error.wg-tooltip.wg-tooltip--error.wg-field-error--visible"));
        // System.out.println(errorLabel.getText());
        // assertFalse(errorLabel.getText());
        assertFalse(isPresent);
    }

    @Test
    public void checkText() {
        driver.get("https://www.wrike.com/product-management-software/");
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        WebElement textField = driver.findElement(By.className("section-features")).findElement(By.tagName("h2"));
        assertTrue(textField.isDisplayed());
    }

    @Test
    public void checkViewFullProduct() {
        driver.get("https://www.wrike.com/product-management-software/");
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        WebElement element = driver.findElement(By.cssSelector(".view-all-features-link.wg-btn.wg-btn--hollow.wg-btn--white.wg-btn--arrow"));
        System.out.println(element.isDisplayed() + " is displayed");
        assertTrue(element.isDisplayed());
        element.click();
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0,500)", "");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".wg-header__desktop .wg-header__free-trial-input.wg-input")));

        WebElement inputElement = driver.findElement(By.cssSelector(".wg-header__desktop .wg-header__free-trial-input.wg-input"));
        inputElement.sendKeys("akldfjalsdjflsd");
        WebElement buttonElement = driver.findElement(By.cssSelector(".wg-header__desktop .wg-header__free-trial-button.wg-btn.wg-btn--green"));
        buttonElement.click();

        WebElement errorElement = driver.findElement(By.cssSelector(".wg-field-error.wg-tooltip.wg-tooltip--error.wg-tooltip--input.wg-tooltip--bottom.wg-field-error--visible"));
        assertTrue(errorElement.isDisplayed());

        inputElement.sendKeys("allstar@mail.ru");
        buttonElement.click();

        WebDriverWait waitAgain = new WebDriverWait(driver, 15);
        waitAgain.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-id-24")));
        assertEquals("https://www.wrike.com/resend/", driver.getCurrentUrl());
    }
}
