package com.qascript;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

public class StepDefs {
    WebDriver driver;
    String username = System.getenv("BROWSERSTACK_USERNAME");
    String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
    String URL = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";

    @Given("Launch website")
    public void launchQAScript() {
        System.setProperty("webdriver.chrome.driver", "D:\\Selenium Xoonit\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/");

    }

    @Then("Verify webpage is displayed to user")
    public void qascriptPageIsDisplayedToUser() {

        String strTitle = driver.getTitle();
        Assert.assertEquals(strTitle, "SeleniumHQ Browser Automation");
        driver.quit();
    }

    public static void markTestStatus(String status, String reason, WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+status+"\", \"reason\": \""+reason+"\"}}");
      }

    @Given("Launch website in BS")
    public void launchWebsiteInBS() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os", "Windows");
        caps.setCapability("os_version", "10");
        caps.setCapability("resolution", "1920x1080");
        caps.setCapability("browser", "Chrome");
        caps.setCapability("browser_version", "latest");
        caps.setCapability("build", "1.0");
        caps.setCapability("browserstack.debug", "true");
        caps.setCapability("project", "Browserstack Demo");
        caps.setCapability("name", "BS Test");
        try {
            WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
            driver.get("https://www.goo1gle.com");
            WebElement element = driver.findElement(By.name("q"));
            element.sendKeys("BrowserStack");
            element.submit();
            // Setting the status of test as 'passed' or 'failed' based on the condition; if
            // title of the web page contains 'BrowserStack'
            WebDriverWait wait = new WebDriverWait(driver, 5);
            try {
                wait.until(ExpectedConditions.titleContains("BrowserStack"));
                markTestStatus("passed", "Yaay title contains 'BrowserStack'!", driver);
            } catch (Exception e) {
                markTestStatus("failed", "Naay title does not contain 'BrowserStack'!", driver);
            }
            System.out.println(driver.getTitle());
            driver.quit();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
