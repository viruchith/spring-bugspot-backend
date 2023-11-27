package bugspot.app.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import bugspot.app.exception.BadResourceActionException;

public enum Severity {
	FEATURE("FEATURE"),
	TRIVIAL("TRIVIAL"),
	TEXT("TEXT"),
	TWEAK("TWEAKS"),
	MINOR("MINOR"),
	MAJOR("MAJOR"),
	CRASH("CRASH"),
	BLOCK("BLOCK");
	
	private String title;

	private Severity(String title) {
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
	public static Severity fromValue(String title) {
		for(Severity severity : Severity.values()) 
		{
			if(severity.title.equals(title)) {
				return severity;
			}
		}
		
		throw new BadResourceActionException("Allowed SEVERITY values are : "+Arrays.toString(Severity.values())+", only !");
	}
	
}
