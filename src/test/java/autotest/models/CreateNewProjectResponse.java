package autotest.models;

public class CreateNewProjectResponse {
	private String id;
	private String name;
	private int comment_count;
	private String color;
	private String shared;
	private String sync_id;
	private int order;
	private boolean favorite;
	
	//Get
	public String getName() {
		return name;
	}
	
	//Set
	public void setName(String name) {
		this.name = name;
	}
	
	//Get
	public String getId() {
		return id;
	}
	
	//Set
	public void setId(String id) {
		this.id = id;
	}
}
