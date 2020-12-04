package autotest.suites.steps.project;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import autotest.base.BaseTest;
import autotest.models.CreateNewProject;
import autotest.models.TaskModel;
import autotest.suites.steps.auth.TodoistAuthTest;
import constants.ConfigProperties;
import constants.DriverType;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ProjectTest extends TodoistAuthTest {
	private CreateNewProject createNewProject;
	private long projectId;
	private long taskId;
	private String newTaskName;
	private String api_token;
	public ProjectTest(DriverType type, CreateNewProject newProject) throws IOException {
		super(type);
		createNewProject = newProject;
		newTaskName = "TaskName" + RandomStringUtils.random(5, true, false);
		api_token = ConfigProperties.getInstance().systemProperties().getProperty("token");
	}
	
	@Test(priority=1)
	public void createNewProject() throws InterruptedException, IOException{
		String CREATE_NEW_PROJECT_URI = "https://api.todoist.com/rest/v1/projects";
		Response response = RestAssured
				.given()
				.header("Authorization", "Bearer "+api_token)
				.contentType(ContentType.JSON)
				.body(createNewProject)
				.post(CREATE_NEW_PROJECT_URI);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Can not create new project.");
		JsonPath newProjectResponse = response.body().jsonPath();
		projectId = newProjectResponse.get("id");
		verifyCreateProject();
	}
	
	@Test(priority=2)
	public void createTask() throws InterruptedException, MalformedURLException {
		List<MobileElement> listProject = findElementsById("android:id/content");
		for(MobileElement mobileEle : listProject) {
			String name = mobileEle.findElementById("com.todoist:id/name").getText();
			if(createNewProject.getName().equals(name)) {
				mobileEle.click();
				MobileElement createTaskButton = tryFindClickableElementById("com.todoist:id/fab");
				createTaskButton.click();
				MobileElement inputTaskNameTxt = tryFindVisibilityOfElementById("android:id/message");
				inputTaskNameTxt.sendKeys(newTaskName);
				((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
				((AndroidDriver) driver).hideKeyboard();
				verifyCreateTask();
				break;
			}
		}
	}
	
	@Test(priority=3)
	public void ReopenTask() throws MalformedURLException, InterruptedException {
		//Mobile: get name of task and complete task
		List<MobileElement> listTaskEle = findElementsById("com.todoist:id/item");
		for(MobileElement taskEle : listTaskEle) {
			String taskName = taskEle.findElementById("com.todoist:id/text").getText();
			if(taskName.equals(newTaskName)) {
				MobileElement checkMark = taskEle.findElementById("com.todoist:id/checkmark");
				checkMark.click();
				break;
			}
		}
		Thread.sleep(2000);
		String REOPEN_TASK_URI = "https://api.todoist.com/rest/v1/tasks/" + taskId + "/reopen";
		Response response = RestAssured
				.given()
				.header("Authorization", "Bearer "+api_token)
				.post(REOPEN_TASK_URI);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 204, "Can not reopen task");
		Thread.sleep(5000);
		showProjects();
		List<MobileElement> listProject = findElementsById("android:id/content");
		for(MobileElement mobileEle : listProject) {
			String name = mobileEle.findElementById("com.todoist:id/name").getText();
			if(createNewProject.getName().equals(name)) {
				mobileEle.click();
				Thread.sleep(2000);
				verifyReopenedTask();
				break;
			}
		}
	}
	
	private void showProjects() throws InterruptedException {
		MobileElement toolBar = tryFindVisibilityOfElementById("com.todoist:id/toolbar");
		List<MobileElement> buttons = toolBar.findElementsByClassName("android.widget.ImageButton");
		if(buttons.size() > 0) {
			//Wait until the today activity loaded.
			Thread.sleep(5000);
			buttons.get(0).click();
			MobileElement sideBar = tryFindVisibilityOfElementById("android:id/list");
			MobileElement projectExpandBtn = sideBar.findElement(By.id("com.todoist:id/collapse"));
			projectExpandBtn.click();
			//Wait until all projects loaded.
			Thread.sleep(2000);
			webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(MobileBy.id("android:id/list")));
		}
	}
	
	public void verifyCreateProject() throws InterruptedException, IOException {
		signInWithGmail();
		showProjects();	
		List<MobileElement> listProject = findElementsById("android:id/content");
		List<String> projectNames = new ArrayList<String>();
		for(MobileElement mobileEle : listProject) {
			String name = mobileEle.findElementById("com.todoist:id/name").getText();
			projectNames.add(name);
		}
		assertTrue(projectNames.contains(createNewProject.getName()), "Can not create project" + createNewProject.getName());
	}

	public void verifyCreateTask() throws InterruptedException {
		Thread.sleep(5000);
		String GET_ALL_CREATED_TASK = "https://api.todoist.com/rest/v1/tasks";
		Response response = RestAssured
				.given()
				.header("Authorization", "Bearer "+api_token)
				.get(GET_ALL_CREATED_TASK);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Can not get all task");
		List<TaskModel> allTasks = (List<TaskModel>) response.body().jsonPath().getList(".", TaskModel.class);
		assertTrue(containTask(allTasks, newTaskName, projectId), "Can not create new task with task name : " + newTaskName);
		for(TaskModel task : allTasks) {
			if(task.getContent().equals(newTaskName) && task.getProject_id() == projectId) {
				taskId = task.getId();
				break;
			}
		}
	}
	
	public boolean containTask(final List<TaskModel> list, final String taskName, final long projectId){
	    return list.stream().anyMatch(o -> o.getContent().equals(taskName) && o.getProject_id() == projectId);
	}
	
	public void verifyReopenedTask() {
		List<MobileElement> listTaskEle = findElementsById("com.todoist:id/item");
		List<String> taskNames = new ArrayList<String>();
		for(MobileElement taskEle : listTaskEle) {
			String name = taskEle.findElementById("com.todoist:id/text").getText();
			taskNames.add(name);
		}
		assertTrue(taskNames.contains(newTaskName), "Task " + newTaskName + " not appear in the screen");
	}
}
