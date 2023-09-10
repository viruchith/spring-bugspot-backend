package bugspot.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bugspot.app.dtos.ProjectDTO;
import bugspot.app.model.Project;
import bugspot.app.service.ProjectService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/users/{userId}/projects")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@PostMapping
	public ResponseEntity<?> createProjectForUser(@PathVariable Long userId,@RequestBody @Valid Project project){
		ProjectDTO projectDTO = projectService.addProject(project);
		return new ResponseEntity<>(projectDTO,HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<?> getAllProjectsOfUser(@PathVariable Long userId){
		List<ProjectDTO> projectDTOs = projectService.getAllProjectsForUser(userId);
		return ResponseEntity.ok(projectDTOs);
	}
	
	@DeleteMapping("{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable Long projectId){
		projectService.deleteProject(projectId);
		return new ResponseEntity<>("Project deleted successfully !",HttpStatus.NO_CONTENT);
	}
	
}
