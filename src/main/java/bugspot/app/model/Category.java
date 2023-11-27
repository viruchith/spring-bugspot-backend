package bugspot.app.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import bugspot.app.exception.BadResourceActionException;

public enum Category {
	BUILD("BUILD"),
	CORE("CORE"),
	INFRASTRUCTURE("INFRASTRUCTURE"),
	TESTS("TESTS"),
	USER_INTERFACE("USER_INTERFACE");
	
	private String title;

	private Category(String title) {
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
	public static Category fromValue(String title) {
		for(Category category : Category.values()) 
		{
			if(category.title.equals(title)) {
				return category;
			}
		}
		
		throw new BadResourceActionException("Allowed CATEGORY values are : "+Arrays.toString(Category.values())+", only !");
	}
	
	
}
