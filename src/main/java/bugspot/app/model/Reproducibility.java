package bugspot.app.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import bugspot.app.exception.BadResourceActionException;

public enum Reproducibility {
	ALWAYS("ALWAYS"),
	SOMETIMES("SOMETIMES"),
	RANDOM("RANDOM"),
	UNABLE_TO_PRODUCE("UNABLE_TO_PRODUCE"),
	NA("NA");
	
	private String title;

	private Reproducibility(String title) {
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
	public static Reproducibility fromValue(String title) {
		for(Reproducibility reproducibility : Reproducibility.values()) 
		{
			if(reproducibility.title.equals(title)) {
				return reproducibility;
			}
		}
		
		throw new BadResourceActionException("Allowed REPRODUCIBILITY values are : "+Arrays.toString(Reproducibility.values())+", only !");
	}
}
