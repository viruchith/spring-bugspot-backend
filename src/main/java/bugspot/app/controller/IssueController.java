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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bugspot.app.dtos.CommentDTO;
import bugspot.app.dtos.IssueDTO;
import bugspot.app.model.Comment;
import bugspot.app.model.Issue;
import bugspot.app.model.Status;
import bugspot.app.service.IssueService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/users/{userId}/projects/{projectId}/issues")
public class IssueController {

	@Autowired
	private IssueService issueService;
	private Issue issueId;
	
	@GetMapping
	public ResponseEntity<?> getAllIssuesForProject(@PathVariable Long projectId){
		List<IssueDTO> issueDTOs = issueService.getAllIssueForProject(projectId);
		return ResponseEntity.ok(issueDTOs);
	}
	
	
	@PostMapping
	public ResponseEntity<?> addIssueToProject(@PathVariable Long projectId,@RequestBody @Valid Issue issue){
		IssueDTO issueDTO = issueService.addIssueToProject(issue, projectId);
		return new ResponseEntity<>(issueDTO,HttpStatus.CREATED);
	}
	
	@PutMapping("/{issueId}")
	public ResponseEntity<?> updateIssueDetails(@PathVariable Long projectId,@PathVariable Long issueId , @RequestBody @Valid Issue issue){
		IssueDTO issueDTO = issueService.updateIssue(issueId,issue, projectId);
		return ResponseEntity.ok(issueDTO);
	}
	
	@DeleteMapping("/{issueId}")
	public ResponseEntity<?> deleteIssue(@PathVariable Long projectId,@PathVariable Long issueId){
		issueService.deleteIssue(issueId, projectId);
		return new ResponseEntity<>("Issue deleted successfully !",HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/{issueId}/status")
	public ResponseEntity<?> updateIssueStatus(@PathVariable Long projectId,@PathVariable Long issueId,@RequestBody @Valid Status status){
		IssueDTO issueDTO =  issueService.updateIssueStatus(issueId, projectId,status);
		return ResponseEntity.ok(issueDTO);
	}
	
	@PutMapping("/{issueId}/acceptedStatus")
	public ResponseEntity<?> updateAcceptedComment(@PathVariable Long projectId,@PathVariable Long issueId,@RequestBody @Valid Status status,@Valid @RequestBody Comment comment){
		CommentDTO commentDTO = issueService.updateAcceptedComment(issueId, projectId, comment);
		return ResponseEntity.ok(commentDTO);
	}

	
}
