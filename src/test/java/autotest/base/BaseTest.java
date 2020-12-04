package autotest.base;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import autotest.driver.DriverManager;
import autotest.driver.DriverManagerFactory;
import constants.DriverType;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import static io.restassured.RestAssured.given;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BaseTest<T extends WebElement> {
	protected WebDriver driver;
	protected WebDriverWait webDriverWait;
	private DriverManager driverManager;
	private DriverType driverType;
	
	public BaseTest(DriverType type) {
		driverType = type;
	}
	
	protected T tryFindClickableElementById(String id) {
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(id))); 
		return findElementById(id);
	}
	
	protected T tryFindVisibilityOfElementById(String id) {
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
		return findElementById(id);
	}
	
	protected List<T> tryFindVisibilityOfAllElementsById(String id) {
		webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(id)));
		return findElementsById(id);
	}
	
	protected T tryFindClickableElementByXPath(String xPath) {
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath))); 
		return findElementByXPath(xPath);
	}
	
	protected T findElementById(String id) {
		switch(driverType) {
			case ANDROID:
				return ((AndroidDriver<T>)driver).findElementById(id);
			case IOS:
				return ((IOSDriver<T>)driver).findElementById(id);
			default:
				return null;
		}
	}
	
	protected T findElementByXPath(String xPath) {
		switch(driverType) {
			case ANDROID:
				return ((AndroidDriver<T>)driver).findElementByXPath(xPath);
			case IOS:
				return ((IOSDriver<T>)driver).findElementByXPath(xPath);
			default:
				return null;
		}
	}
	
	protected List<T> findElementsById(String id) {
		switch(driverType) {
			case ANDROID:
				return ((AndroidDriver<T>)driver).findElementsById(id);
			case IOS:
				return ((IOSDriver<T>)driver).findElementsById(id);
			default:
				return null;
		}
	}
	
	@BeforeSuite
	public void StartSession() throws Exception {
		driverManager = DriverManagerFactory.getDriverManager(driverType);
		driver = driverManager.getWebDriver();
		webDriverWait = new WebDriverWait(driver, 10);
	}
	
	@AfterSuite
	public void Dispose() {
		driverManager.quitWebDriver();
	}
}
