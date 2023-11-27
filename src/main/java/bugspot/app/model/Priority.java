package bugspot.app.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import bugspot.app.exception.BadResourceActionException;

public enum Priority {
	LOW("LOW"),
	NORMAL("NORMAL"),
	HIGH("HIGH"),
	URGENT("URGENT"),
	IMMEDIATE("IMMEDIATE");
	
	private String title;

	private Priority(String title) {
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
	public static Priority fromValue(String title) {
		for(Priority priority : Priority.values()) 
		{
			if(priority.title.equals(title)) {
				return priority;
			}
		}
		
		throw new BadResourceActionException("Allowed PRIORITY values are : "+Arrays.toString(Priority.values())+", only !");
	}
}
