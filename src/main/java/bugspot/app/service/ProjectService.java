package bugspot.app.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import bugspot.app.dtos.AppUserDTO;
import bugspot.app.dtos.MemberDTO;
import bugspot.app.dtos.ProjectDTO;
import bugspot.app.exception.ProjectNotFoundException;
import bugspot.app.exception.UnauthorizedResourceActionException;
import bugspot.app.model.Project;

public interface ProjectService {
	ProjectDTO addProject(Project project);
	
	List<ProjectDTO> getAllProjectsForUser(Long userId);
	
	void deleteProject(Long projectId) throws UnauthorizedResourceActionException;
	
	List<AppUserDTO> getProjectMembersForProject(Long projectId) throws ProjectNotFoundException;
	
	List<AppUserDTO> addMemberToProject(Long projectId,MemberDTO memberDTO) throws ProjectNotFoundException,UnauthorizedResourceActionException;
	
	void deleteMemberFromProject(Long memberId,Long projectId) throws ProjectNotFoundException,UnauthorizedResourceActionException;
	
	List<AppUserDTO> promoteMemberToAdminForProject(Long projectId,MemberDTO memberDTO) throws UsernameNotFoundException, UnauthorizedResourceActionException;

	void demoteAdminFromProject(Long memberId, Long projectId) throws ProjectNotFoundException, UnauthorizedResourceActionException;
	
	List<AppUserDTO> getProjectAdminsForProject(Long projectId) throws ProjectNotFoundException, UnauthorizedResourceActionException;
	
	// TODO updatae project details
	Set<String> addProjectVersion(Long projectId,String projectVersion);
	void deleteProjectVersion(Long projectId, String projectVersion);
	//TODO UPDATE proejct version
}
