package autotest.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverManager extends DriverManager {
	@ Override
	public void createWebDriver() {
		ChromeOptions chromeOptions = new ChromeOptions();
		this.driver = new ChromeDriver(chromeOptions);
	}
}
