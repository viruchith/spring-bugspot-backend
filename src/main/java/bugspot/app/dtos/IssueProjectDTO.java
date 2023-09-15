package bugspot.app.dtos;

import java.util.List;

public interface IssueProjectDTO {
	Long getId();
	
	AppUserDTO getOwnerAppUser();

	String getName();

	String getDescription();
	
	String getRepoURL();
	
	List<String> getProductVersions();
}
