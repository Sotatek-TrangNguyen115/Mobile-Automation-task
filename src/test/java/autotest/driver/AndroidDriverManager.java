package autotest.driver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import constants.ConfigProperties;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class AndroidDriverManager extends DriverManager {
	@ Override
	public void createWebDriver() throws IOException {
		DesiredCapabilities capabilities = new DesiredCapabilities();		
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");
		capabilities.setCapability(AndroidMobileCapabilityType .APP_ACTIVITY, ConfigProperties.getInstance().systemProperties().getProperty("APP_ACTIVITY"));
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, ConfigProperties.getInstance().systemProperties().getProperty("DEVICE_NAME"));
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, ConfigProperties.getInstance().systemProperties().getProperty("APP_PACKAGE"));
		driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
}
