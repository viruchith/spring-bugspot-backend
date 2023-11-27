package bugspot.app.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import bugspot.app.exception.BadResourceActionException;

public enum Status {

	NEW("NEW"),
	ASSIGNED("ASSIGNED"),
	RESOLVED("RESOLVED");
	
	private String title;

	private Status(String title) {
		this.title = title;
	}
	
	@JsonValue
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	@JsonCreator
	public static Status fromValue(String title) {
		for(Status status : Status.values()) 
		{
			if(status.title.equals(title)) {
				return status;
			}
		}
		
		throw new BadResourceActionException("Allowed STATUS values are : "+Arrays.toString(Status.values())+", only !");
	}

}
