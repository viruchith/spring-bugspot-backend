package bugspot.app.controller;

import java.util.List;
import java.util.Set;

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

import bugspot.app.dtos.AppUserDTO;
import bugspot.app.dtos.MemberDTO;
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
	
	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable Long projectId){
		projectService.deleteProject(projectId);
		return new ResponseEntity<>("Project deleted successfully !",HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{projectId}/members")
	public ResponseEntity<?> getProjectMembersForProject(@PathVariable Long projectId){
		List<AppUserDTO> members =  projectService.getProjectMembersForProject(projectId);
		return ResponseEntity.ok(members);
	}

	
	@PostMapping("/{projectId}/members")
	public ResponseEntity<?> addMemberToProjects(@PathVariable Long projectId, @Valid @RequestBody MemberDTO memberDTO){
		List<AppUserDTO> members = projectService.addMemberToProject(projectId, memberDTO);
		return new ResponseEntity<>(members,HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/{projectId}/members/{memberId}")
	public ResponseEntity<?> deleteMemberFromProject(@PathVariable Long projectId,@PathVariable Long memberId){
		projectService.deleteMemberFromProject(memberId, projectId);
		return new ResponseEntity<>("Member deleted successfully !",HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/{projectId}/admins")
	public ResponseEntity<?> addAdminToProjects(@PathVariable Long projectId,@Valid @RequestBody MemberDTO memberDTO){
		List<AppUserDTO> adminDtos = projectService.promoteMemberToAdminForProject(projectId, memberDTO);
		return new ResponseEntity<>(adminDtos,HttpStatus.CREATED);
	}
	
	@GetMapping("/{projectId}/admins")
	public ResponseEntity<?> getAllAdminsForProject(@PathVariable Long projectId){
		List<AppUserDTO> adminAppUserDTOs = projectService.getProjectAdminsForProject(projectId);
		return ResponseEntity.ok(adminAppUserDTOs);
	}
	
	@DeleteMapping("/{projectId}/admins/{memberId}")
	public ResponseEntity<?> demoteAdminForProject(@PathVariable Long projectId, @PathVariable Long memberId){
		projectService.demoteAdminFromProject(memberId, projectId);
		return new ResponseEntity<>("Admin removed successfully !",HttpStatus.NO_CONTENT);
	}
	
}
