package autotest.suites;

import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import autotest.models.CreateNewProject;
import autotest.suites.steps.project.ProjectTest;
import constants.DriverType;

public class TestTodoistFactory {
	@Factory(dataProvider = "dp")
	public Object[] getTestProjectClasses(DriverType type) throws IOException {
		Object[] tests = new Object[1];
		CreateNewProject createNewProject = new CreateNewProject();
		tests[0] = new ProjectTest(type, createNewProject);
		return tests;
	}
	
	@DataProvider
	public Object[][] dp() {
		return new Object[][] { { DriverType.ANDROID } };
	}
}
