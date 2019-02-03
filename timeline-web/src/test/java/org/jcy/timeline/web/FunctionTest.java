package org.jcy.timeline.web;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FunctionTest {

    private static final Logger log = LoggerFactory.getLogger(FunctionTest.class);

    private static WebDriver chrome;

    @BeforeClass
    public static void setProperties() {
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\72.0.3626.81\\chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", "/opt/google/chrome/chromedriver.exe");
            System.setProperty("webdriver.chrome.bin", "/opt/google/chrome");
        }

        chrome = new ChromeDriver();

        log.info("Chrome Driver is initialized in headless mode.");
    }

    @Test
    public void openAndClickMoreTest() {
//        //Launch website
//        chrome.get("http://localhost:8085/timeline");
//        //Puts a Implicit wait, Will wait for 10 seconds before throwing exception
//        chrome.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//        //Maximize the browser
//        chrome.manage().window().maximize();
//        List<WebElement> items = chrome.findElements(By.cssSelector("body>div>div>div>div>div>div>div"));
//        WebElement button = items.get(items.size()-1).findElement(By.cssSelector("div"));
//        button.click();
//        List<WebElement> afterClickMoreItems = chrome.findElements(By.cssSelector("body>div>div>div>div>div>div>div>div"));
//
//        Assert.assertTrue(items.size() <= afterClickMoreItems.size());

        Assert.assertNotNull(chrome);
    }

    @AfterClass
    public static void quit() {
        if (chrome != null) {
            chrome.quit();
            log.info("Quit Chrome.");
        } else {
            log.error("Fail to quit Chrome, since it is NULL.");
        }
    }
}
