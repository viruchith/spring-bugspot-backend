package bugspot.app.service;

import java.util.HashSet;
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

	@Override
	public ProjectDTO addProject(Project project) {
		AppUser currentAppUser = currentLoggedInAppUser.get();
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
		// TODO get all projects where the user is member or owner
		AppUser ownerAppUser = currentLoggedInAppUser.get();
		List<ProjectDTO> projectDTOs = projectRepository.findAllByMembersContains(ownerAppUser, ProjectDTO.class);
		return projectDTOs;
	}

	@Override
	public void deleteProject(Long projectId) {
		AppUser currentAppUser = currentLoggedInAppUser.get();

		Optional<Project> projectOptional = projectRepository.findById(projectId);

		if (projectOptional.isPresent()) {
			Project project = projectOptional.get();

			if (project.getOwnerAppUser().equals(currentAppUser)) {
				projectRepository.delete(project);
			} else {
				throw new UnauthorizedResourceActionException("Only Project Owner can delete the project !");
			}
		}
	}

	@Override
	public List<AppUserDTO> addMemberToProject(Long projectId, MemberDTO memberDTO) {
		AppUser currentAppUser = currentLoggedInAppUser.get();

		AppUser memberAppUser = appUserRepository.findFirstByUsername(memberDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException(
						"User with username : " + memberDTO.getUsername() + ", does not exist !"));
		;

		Optional<Project> projectOptional = projectRepository.findById(projectId);

		if (projectOptional.isPresent()) {
			Project project = projectOptional.get();
			if (project.getOwnerAppUser().equals(currentAppUser) || project.getAdminUsers().contains(currentAppUser)) {
				project.getMembers().add(memberAppUser);
				project = projectRepository.save(project);
				ProjectDTO projectDTO = projectRepository.findById(projectId, ProjectDTO.class);

				List<AppUserDTO> members = projectDTO.getMembers();

				return members;
			} else {
				throw new UnauthorizedResourceActionException(
						"Only Project Owner and Admins can add members to the project !");
			}
		} else {
			throw new ProjectNotFoundException("Project with id : " + projectId + ", doest not exist !");
		}

	}

	@Override
	public void deleteMemberFromProject(Long memberId, Long projectId) throws ProjectNotFoundException {
		AppUser currentAppUser = currentLoggedInAppUser.get();

		AppUser memberAppUser = appUserRepository.findById(memberId)
				.orElseThrow(() -> new UserNotFoundException("User by the id : " + memberId + ", does not exist !"));
		;

		Optional<Project> projectOptional = projectRepository.findById(projectId);

		if (projectOptional.isPresent()) {
			Project project = projectOptional.get();
			if (project.getOwnerAppUser().equals(currentAppUser) || project.getAdminUsers().contains(currentAppUser)) {
				if (project.getOwnerAppUser().equals(memberAppUser)) {
					throw new UnauthorizedResourceActionException("Owner of the project cannot be deleted !");
				}

				if (!project.getOwnerAppUser().equals(currentLoggedInAppUser)
						&& project.getAdminUsers().contains(memberAppUser)) {
					throw new UnauthorizedResourceActionException(
							"Only Owner of the project can delete other admins !");
				}

				Set<AppUser> adminUsers = project.getAdminUsers();
				adminUsers.remove(memberAppUser);
				project.setAdminUsers(adminUsers);

				Set<AppUser> members = project.getMembers();

				members.remove(memberAppUser);

				project.setMembers(members);

				projectRepository.save(project);

			} else {
				throw new UnauthorizedResourceActionException("Only Admins can delete a project member !");
			}
		} else {
			throw new ProjectNotFoundException("Project with id : " + projectId + ", doest not exist !");
		}
	}

	@Override
	public List<AppUserDTO> promoteMemberToAdminForProject(Long projectId, MemberDTO memberDTO)
			throws UsernameNotFoundException {
		AppUser currentAppUser = currentLoggedInAppUser.get();

		AppUser memberAppUser = appUserRepository.findFirstByUsername(memberDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException(
						"User with username : " + memberDTO.getUsername() + ", does not exist !"));
		;

		Optional<Project> projectOptional = projectRepository.findById(projectId);

		if (projectOptional.isPresent()) {
			Project project = projectOptional.get();

			if (project.getOwnerAppUser().equals(currentAppUser)) {
				Set<AppUser> members = project.getMembers();
				members.add(memberAppUser);
				project.setMembers(members);
				Set<AppUser> adminUsers = project.getAdminUsers();
				adminUsers.add(memberAppUser);
				project.setAdminUsers(adminUsers);
				project = projectRepository.save(project);
				ProjectDTO projectDTO = projectRepository.findById(projectId, ProjectDTO.class);
				List<AppUserDTO> adminDtos = projectDTO.getAdminUsers();
				return adminDtos;
			} else {
				throw new UnauthorizedResourceActionException("Only Proejct owner can promote a member to admin !");
			}
		} else {
			throw new ProjectNotFoundException("Project with id : " + projectId + ", doest not exist !");
		}

	}

	@Override
	public List<AppUserDTO> getProjectMembersForProject(Long projectId) throws ProjectNotFoundException {
		AppUser currentAppUser = currentLoggedInAppUser.get();

		Optional<Project> projectOptional = projectRepository.findById(projectId);

		if (projectOptional.isPresent()) {
			Project project = projectOptional.get();
			if (project.getMembers().contains(currentAppUser)) {
				ProjectDTO projectDTO = projectRepository.findById(projectId, ProjectDTO.class);
				List<AppUserDTO> memberDtos = projectDTO.getMembers();
				return memberDtos;
			} else {
				throw new UnauthorizedResourceActionException(
						"You are not a member of this project ! You have no authority view the members");
			}
		} else {
			throw new ProjectNotFoundException("Project with id : " + projectId + ", doest not exist !");
		}

	}

	@Override
	public void demoteAdminFromProject(Long memberId, Long projectId)
			throws ProjectNotFoundException, UnauthorizedResourceActionException {
		AppUser currentAppUser = currentLoggedInAppUser.get();

		Project project = projectRepository.findById(projectId).orElseThrow(
				() -> new ProjectNotFoundException("Project with id : " + projectId + ", doest not exist !"));

		if (project.getOwnerAppUser().equals(currentAppUser)) {
			AppUser memberAppUser = appUserRepository.findById(memberId).orElseThrow(
					() -> new UserNotFoundException("User by the id : " + memberId + ", does not exist !"));
			;
			if (currentAppUser.equals(memberAppUser)) {
				throw new BadResourceActionException("Owner of the project cannot be removef from thet project !");
			}

			Set<AppUser> adminUsers = project.getAdminUsers();
			adminUsers.remove(memberAppUser);
			project.setAdminUsers(adminUsers);
			projectRepository.save(project);
		} else {
			throw new UnauthorizedResourceActionException("Only the project owner can demote an ADMIN user !");
		}

	}

	@Override
	public List<AppUserDTO> getProjectAdminsForProject(Long projectId)
			throws ProjectNotFoundException, UnauthorizedResourceActionException {
		AppUser currentAppUser = currentLoggedInAppUser.get();

		Project project = projectRepository.findById(projectId).orElseThrow(
				() -> new ProjectNotFoundException("Project with id : " + projectId + ", doest not exist !"));


		if (project.getMembers().contains(currentAppUser)) {
			ProjectDTO projectDTO = projectRepository.findById(projectId, ProjectDTO.class);
			List<AppUserDTO> adminAppUserDTOs = projectDTO.getAdminUsers();
			return adminAppUserDTOs;
		}else {
			throw new UnauthorizedResourceActionException(
					"You are not a member of this project ! You have no authority view the members");
		}
		
	}


}
