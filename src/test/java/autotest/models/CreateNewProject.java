package autotest.models;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class CreateNewProject {
	private String name;
	public CreateNewProject() {
		this.name = "Autotest" + RandomStringUtils.random(5, true, false);
	}
	//Get
	public String getName() {
		return name;
	}
	
	//Set
	public void setName(String name) {
		this.name = name;
	}
}
