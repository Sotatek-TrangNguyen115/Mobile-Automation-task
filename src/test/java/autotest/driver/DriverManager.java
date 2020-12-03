package autotest.driver;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;

public abstract class DriverManager {
	protected WebDriver driver;
	protected abstract void createWebDriver() throws Exception;
	public void quitWebDriver() {
		if(null != driver) {
			driver.quit();
			driver = null;
		}
	}
	public WebDriver getWebDriver() throws Exception {
		if(null == driver) {
			createWebDriver();
		}
		return driver;
	}
}
