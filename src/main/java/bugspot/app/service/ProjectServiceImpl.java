package bugspot.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bugspot.app.auth.CurrentLoggedInAppUser;
import bugspot.app.dtos.ProjectDTO;
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
		AppUser ownerAppUser =  currentLoggedInAppUser.get();
		project.setOwnerAppUser(ownerAppUser);
		List<AppUser> members = new ArrayList<>();
		List<AppUser>adminUsers = new ArrayList<>();
		members.add(ownerAppUser);
		project.setMembers(members);
		project.setAdminUsers(adminUsers);
		project =  projectRepository.save(project);
		ProjectDTO projectDTO =  projectRepository.findById(project.getId(), ProjectDTO.class);
		log.info("CREATED PROJECT  ID : {} NAME : {}",projectDTO.getId(),project.getName());		
		return projectDTO;
	}

	@Override
	public List<ProjectDTO> getAllProjectsForUser(Long userId) {
		//TODO get all projects where the user is member or owner
		AppUser ownerAppUser =  currentLoggedInAppUser.get();
		List<ProjectDTO> projectDTOs = projectRepository.findAllByMembersContains(ownerAppUser, ProjectDTO.class);
		return projectDTOs;
	}

	@Override
	public void deleteProject(Long projectId) {
		AppUser ownerAppUser =  currentLoggedInAppUser.get();
		
		Optional<Project> projectOptional = projectRepository.findById(projectId);
		
		if(projectOptional.isPresent()) {
			Project project = projectOptional.get();
			
			if(project.getOwnerAppUser().equals(ownerAppUser)) {
				projectRepository.delete(project);
			}else {
				//TODO No Permission todo action exception
			}
		}
	}

}
