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
import bugspot.app.model.Comment;
import bugspot.app.service.CommentService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/users/{userId}/projects/{projectId}/issues/{issueId}/comments")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	
	@PostMapping
	public ResponseEntity<?> addCommentToIssue(@PathVariable Long projectId,@PathVariable Long issueId,@RequestBody @Valid Comment comment){
		CommentDTO commentDTO = commentService.createComment(projectId, issueId, comment);
		return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
	}
	
	
	@GetMapping
	public ResponseEntity<?> getAllCommentsForIssue(@PathVariable Long projectId,@PathVariable Long issueId){
		List<CommentDTO> commentDTOs = commentService.getAllCommentsForIssue(projectId, issueId);
		return ResponseEntity.ok(commentDTOs);
	}
	
	@PutMapping("/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable Long projectId,@PathVariable Long issueId,@PathVariable Long commentId,@RequestBody @Valid Comment comment){
		CommentDTO commentDTO = commentService.updateComment(projectId, issueId, commentId, comment);
		return ResponseEntity.ok(commentDTO);
	}
	
	
	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable Long projectId,@PathVariable Long issueId,@PathVariable Long commentId){
		commentService.deleteComment(projectId, issueId, commentId);
		return new ResponseEntity<>("The comment was deleted successfully !",HttpStatus.NO_CONTENT);
	}
	
}
