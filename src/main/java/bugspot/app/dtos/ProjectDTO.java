package bugspot.app.dtos;

import java.util.List;

public interface ProjectDTO {
	
	Long getId();
	
	AppUserDTO getOwnerAppUser();

	String getName();

	String getDescription();
	
	String getRepoURL();
	
	List<String> getProductVersions();
	
	List<AppUserDTO> getMembers();
	
	List<AppUserDTO> getAdminUsers();

}
