package autotest.driver;

import constants.DriverType;

public class DriverManagerFactory {
	public static DriverManager getDriverManager(DriverType type) {
		DriverManager driverManager = null;
		switch(type) {
		case CHROME:
			driverManager = new ChromeDriverManager();
			break;
		case ANDROID:
			driverManager = new AndroidDriverManager();
			break;
		default:
			break;
		}
		return driverManager;
	}
}
