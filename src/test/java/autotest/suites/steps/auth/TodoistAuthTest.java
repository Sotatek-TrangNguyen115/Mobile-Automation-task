package autotest.suites.steps.auth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

import autotest.base.BaseTest;
import constants.ConfigProperties;
import constants.DriverType;
import io.appium.java_client.MobileElement;

public class TodoistAuthTest extends BaseTest<MobileElement> {
	public TodoistAuthTest(DriverType type) {
		super(type);
	}
	
	protected void signInWithGmail() throws IOException {
		MobileElement signInWithGoogleBtn = tryFindClickableElementById("com.todoist:id/btn_welcome_continue_with_email");
		signInWithGoogleBtn.click();
		MobileElement inputEmail = tryFindClickableElementById("com.todoist:id/email_exists_input");
		inputEmail.sendKeys(ConfigProperties.getInstance().systemProperties().getProperty("email"));
		MobileElement continueEmailBtn = tryFindClickableElementById("com.todoist:id/btn_continue_with_email");
		continueEmailBtn.click();
		MobileElement inputPassword = tryFindClickableElementById("com.todoist:id/log_in_password");
		inputPassword.sendKeys(ConfigProperties.getInstance().systemProperties().getProperty("password"));
		MobileElement loginBtn = tryFindClickableElementById("com.todoist:id/btn_log_in");
		loginBtn.click();
	}
}
