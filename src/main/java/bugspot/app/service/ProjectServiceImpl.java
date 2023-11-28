package bugspot.app.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bugspot.app.auth.CurrentLoggedInAppUser;
import bugspot.app.dtos.AppUserDTO;
import bugspot.app.dtos.MemberDTO;
import bugspot.app.dtos.ProjectDTO;
import bugspot.app.exception.BadResourceActionException;
import bugspot.app.exception.ProjectNotFoundException;
import bugspot.app.exception.UnauthorizedResourceActionException;
import bugspot.app.exception.UserNotFoundException;
import bugspot.app.model.AppUser;
import bugspot.app.model.Project;
import bugspot.app.repository.AppUserRepository;
import bugspot.app.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private CurrentLoggedInAppUser currentLoggedInAppUser;
	
	private AppUser currentUser;
	
	private Project currentProject;

	
	private boolean isCurrentUserMemberOfProject(Long projectId) {
		/*
		 * Check if user is member of the given project id
		 */
		currentUser = currentLoggedInAppUser.get();
		currentProject = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException());
		
		if(currentProject.getMembers().contains(currentUser)) {
			return true;
		}else {
			throw new UnauthorizedResourceActionException("You are not a member of this project !");

		}
	}
	
	private boolean isCurrentUserAdminOfTheProject(Long projectId) {
		/*
		 * check if user is admin of the given project ID
		 */
		if(isCurrentUserMemberOfProject(projectId)) {
			if (currentProject.getAdminUsers().contains(currentUser)) {
				return true;
			}else {
				throw new UnauthorizedResourceActionException("Only project admin can perform this action !");
			}
		}
		
		return false;
		
	}
	
	private boolean isCurrentUserOwnerOfTheProject(Long projectId) {
		/*
		 * 
		 * check if user is owner of the given project
		 * 
		 */
		if(isCurrentUserAdminOfTheProject(projectId)) {
			if(currentProject.getOwnerAppUser().equals(currentUser)) {
				return true;
			}else {
				throw new UnauthorizedResourceActionException("You are not a owner of this project !");
			}
		}
		return false;
	}

	@Override
	public ProjectDTO addProject(Project project) {
		/*
		 *  Save the project to DB
		 */
		AppUser currentAppUser = currentLoggedInAppUser.get();
		// set current user as project owner
		project.setOwnerAppUser(currentAppUser);
		Set<AppUser> members = new HashSet<>();
		Set<AppUser> adminUsers = new HashSet<>();
		members.add(currentAppUser);
		project.setMembers(members);
		project.setAdminUsers(adminUsers);
		project = projectRepository.save(project);
		ProjectDTO projectDTO = projectRepository.findById(project.getId(), ProjectDTO.class);
		log.info("CREATED PROJECT  ID : {} NAME : {}", projectDTO.getId(), project.getName());
		return projectDTO;
	}

	@Override
	public List<ProjectDTO> getAllProjectsForUser(Long userId) {
		/*
		 * Get all projects in which the user is a member.
		 */
		AppUser ownerAppUser = currentLoggedInAppUser.get();
		List<ProjectDTO> projectDTOs = projectRepository.findAllByMembersContains(ownerAppUser, ProjectDTO.class);
		return projectDTOs;
	}

	@Override
	public void deleteProject(Long projectId) {
		/*
		 * Delete the project by the given ID, only if the current user is owner of the project.
		 */
		if(isCurrentUserOwnerOfTheProject(projectId)) {
			projectRepository.delete(currentProject);
		}
	}

	@Override
	public List<AppUserDTO> addMemberToProject(Long projectId, MemberDTO memberDTO) {
		/*
		 * 
		 * Add the given user to the given project only if the current user is an admin of the project.
		 * 
		 */
		AppUser memberAppUser = appUserRepository.findFirstByUsername(memberDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException(
						"User with username : " + memberDTO.getUsername() + ", does not exist !"));
		;

		if(isCurrentUserAdminOfTheProject(projectId)) {
			currentProject.getMembers().add(memberAppUser);
			currentProject = projectRepository.save(currentProject);
			ProjectDTO projectDTO = projectRepository.findById(projectId, ProjectDTO.class);
			List<AppUserDTO> members = projectDTO.getMembers();
			return members;
		}
		
		return null;

	}

	@Override
	public void deleteMemberFromProject(Long memberId, Long projectId) throws ProjectNotFoundException {

		/*
		 * 
		 * Delete the given member from the project only if the current user is owner of the project.
		 * 
		 */
		
		AppUser memberAppUser = appUserRepository.findById(memberId)
				.orElseThrow(() -> new UserNotFoundException("User by the id : " + memberId + ", does not exist !"));
		;


			if (isCurrentUserAdminOfTheProject(projectId)) {
				if (currentProject.getOwnerAppUser().equals(memberAppUser)) {
					throw new UnauthorizedResourceActionException("Owner of the project cannot be deleted !");
				}

				if (!isCurrentUserOwnerOfTheProject(projectId)
						&& currentProject.getAdminUsers().contains(memberAppUser)) {
					throw new UnauthorizedResourceActionException(
							"Only Owner of the project can delete other admins !");
				}

				Set<AppUser> adminUsers = currentProject.getAdminUsers();
				adminUsers.remove(memberAppUser);
				currentProject.setAdminUsers(adminUsers);

				Set<AppUser> members = currentProject.getMembers();

				members.remove(memberAppUser);

				currentProject.setMembers(members);

				projectRepository.save(currentProject);

			} 
			
	}

	@Override
	public List<AppUserDTO> promoteMemberToAdminForProject(Long projectId, MemberDTO memberDTO)
			throws UsernameNotFoundException {

		/*
		 * promote the given member to Admin of the project only if the current user is the project owner.
		 * 
		 */
		
		AppUser memberAppUser = appUserRepository.findFirstByUsername(memberDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException(
						"User with username : " + memberDTO.getUsername() + ", does not exist !"));
		;


			if (isCurrentUserOwnerOfTheProject(projectId)) {
				Set<AppUser> members = currentProject.getMembers();
				members.add(memberAppUser);
				currentProject.setMembers(members);
				Set<AppUser> adminUsers = currentProject.getAdminUsers();
				adminUsers.add(memberAppUser);
				currentProject.setAdminUsers(adminUsers);
				currentProject = projectRepository.save(currentProject);
				ProjectDTO projectDTO = projectRepository.findById(projectId, ProjectDTO.class);
				List<AppUserDTO> adminDtos = projectDTO.getAdminUsers();
				return adminDtos;
			}
			
			return null;

	}

	@Override
	public List<AppUserDTO> getProjectMembersForProject(Long projectId) throws ProjectNotFoundException {
			/*
			 * Get all members for the given project only if the current user is a member of the project.
			 */
			if (isCurrentUserMemberOfProject(projectId)) {
				ProjectDTO projectDTO = projectRepository.findById(projectId, ProjectDTO.class);
				List<AppUserDTO> memberDtos = projectDTO.getMembers();
				return memberDtos;
			} 
			
			return null;
	}

	@Override
	public void demoteAdminFromProject(Long memberId, Long projectId)
			throws ProjectNotFoundException, UnauthorizedResourceActionException {

		/*
		 * 
		 * Demote an admin of the given project to member only if the current user is the owner of the project.
		 * 
		 */
		

		if (isCurrentUserOwnerOfTheProject(projectId)) {
			AppUser memberAppUser = appUserRepository.findById(memberId).orElseThrow(
					() -> new UserNotFoundException("User by the id : " + memberId + ", does not exist !"));
			;
			if (currentUser.equals(memberAppUser)) {
				throw new BadResourceActionException("Owner of the project cannot be removef from thet project !");
			}

			Set<AppUser> adminUsers = currentProject.getAdminUsers();
			adminUsers.remove(memberAppUser);
			currentProject.setAdminUsers(adminUsers);
			projectRepository.save(currentProject);
		} else {
			throw new UnauthorizedResourceActionException("Only the project owner can demote an ADMIN user !");
		}

	}

	@Override
	public List<AppUserDTO> getProjectAdminsForProject(Long projectId)
			/*
			 * Get all admin users of the project only if the current user is a member of the project.
			 * 
			 */
			throws ProjectNotFoundException, UnauthorizedResourceActionException {
		
			if(isCurrentUserMemberOfProject(projectId)) {
				ProjectDTO projectDTO = projectRepository.findById(projectId, ProjectDTO.class);
				List<AppUserDTO> adminAppUserDTOs = projectDTO.getAdminUsers();
				return adminAppUserDTOs;
			}
			
			return null;
		
		
	}

	@Override
	public Set<String> addProjectVersion(Long projectId, String projectVersion) {
		/*
		 * Allow user to add project version only if he is the admin or owner of the project.
		 * */
		if(isCurrentUserOwnerOfTheProject(projectId) || isCurrentUserAdminOfTheProject(projectId)) {
			Set<String> productVersions =  currentProject.getProductVersions();
			if(productVersions==null) {
				productVersions = new LinkedHashSet<>();
			}
			productVersions.add(projectVersion);
			currentProject.setProductVersions(productVersions);
			projectRepository.save(currentProject);
			return currentProject.getProductVersions();
		}
		return null;
	}

	@Override
	public void deleteProjectVersion(Long projectId, String projectVersion) {
		/*
		 * Allow user to delete project version only if the user is a admin or owner of the project.
		 * */
		if(isCurrentUserOwnerOfTheProject(projectId) || isCurrentUserAdminOfTheProject(projectId)) {
			Set<String> productVersions =  currentProject.getProductVersions();
			if(productVersions==null) {
				productVersions = new LinkedHashSet<>();
			}
			productVersions.remove(projectVersion);
			currentProject.setProductVersions(productVersions);
			projectRepository.save(currentProject);

		}
	}


}
