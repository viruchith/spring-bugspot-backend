package bugspot.app.service;

import java.util.List;

import bugspot.app.dtos.ProjectDTO;
import bugspot.app.model.Project;

public interface ProjectService {
	ProjectDTO addProject(Project project);
	
	List<ProjectDTO> getAllProjectsForUser(Long userId);
	
	void deleteProject(Long projectId);
}
