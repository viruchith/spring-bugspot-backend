package bugspot.app.dtos;

import bugspot.app.model.Category;
import bugspot.app.model.Priority;
import bugspot.app.model.Reproducibility;
import bugspot.app.model.Severity;
import bugspot.app.model.Status;

public interface IssueDTO {
	
	Long getId();
	
	AppUserDTO getCreatorAppUser();
	
	IssueProjectDTO getProject();
	
	Status getStatus();
	
	Category getCategory();
	
	Reproducibility getReproducibility();
	
	Severity getSeverity();
	
	Priority getPriority();
	
	String getProjectVersion();
	
	String getSummary();
	
	String getDescription();
	
	
	String getStepsToReproduce();
	
	String getAdditionalInformation();
	
	String getSourceCode();
}
