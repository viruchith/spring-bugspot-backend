package bugspot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bugspot.app.auth.CurrentLoggedInAppUser;
import bugspot.app.dtos.CommentDTO;
import bugspot.app.dtos.IssueDTO;
import bugspot.app.exception.BadResourceActionException;
import bugspot.app.exception.CommentNotFoundException;
import bugspot.app.exception.IssueNotFoundException;
import bugspot.app.exception.ProjectNotFoundException;
import bugspot.app.exception.UnauthorizedResourceActionException;
import bugspot.app.model.AppUser;
import bugspot.app.model.Comment;
import bugspot.app.model.Issue;
import bugspot.app.model.Project;
import bugspot.app.repository.CommentRepository;
import bugspot.app.repository.IssueRepository;
import bugspot.app.repository.ProjectRepository;


@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private CurrentLoggedInAppUser currentLoggedInAppUser;
	
	private AppUser currentUser;
	
	private Project currentProject;
	
	private Issue currentIssue;
	
	private Comment currentComment;
	
	private boolean isCurrentUserAMemberOfProject(Long projectId) {
		/*
		 * check if the current user is a member of the project.
		 */
		currentUser = currentLoggedInAppUser.get();
		
		currentProject = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project by the id : "+projectId+", was not found !"));

		if(currentProject.getMembers().contains(currentUser)) {
			return true;
		}else {
			throw new UnauthorizedResourceActionException("You are not a member of this project !");
		}
	}
	
	private boolean isIssuePartOfProject(Long issueId) {
		/*
		 * check if the issue is part of the project
		 */
		currentIssue = issueRepository.findById(issueId).orElseThrow(()->new IssueNotFoundException("Issue by the id :"+issueId+", does not exist !"));
		if(currentProject.getIssues().contains(currentIssue)) {
			return true;
		}else {
			throw new BadResourceActionException("Issue by the ID : "+issueId+", is not part of the project you requested !");
		}
	}
	
	
	private boolean isCurrentCommentPartOfCurrentIssue(Long commentId) {
		/*
		 * check if the comment is part of the current issue.
		 */
		currentComment = commentRepository.findById(commentId).orElseThrow(()->new CommentNotFoundException("Comment By the ID : "+commentId+", was not found !"));
		if(currentComment.getIssue().equals(currentIssue)) {
			return true;
		}else {
			throw new BadResourceActionException("The comment by the ID : "+commentId+", is not part of the specified project !");
		}
	}
	
	private boolean isCurrentUserCreatorOfCurrentComment() {
		/*
		 * check if the current user is the creator of the comment.
		 */
		if(currentComment.getCreatorAppUser().equals(currentUser)) {
			return true;
		}else {
			throw new UnauthorizedResourceActionException("You are not a creator of this comment !");
		}
	}
	
	private CommentDTO getCommentDTOById(Long commentId) {
		CommentDTO commentDTO = commentRepository.findFirstById(commentId,CommentDTO.class).orElseThrow(()->new CommentNotFoundException(""));
		return commentDTO;
	}
	
	@Override
	public CommentDTO createComment(Long projectId, Long issueId, Comment comment) {
		/*
		 * Add the comment to a project and save it
		 */
		if(isCurrentUserAMemberOfProject(projectId) && isIssuePartOfProject(issueId)) {
			comment.setCreatorAppUser(currentUser);
			comment.setIssue(currentIssue);
			comment = commentRepository.save(comment);
			return getCommentDTOById(comment.getId());
		}
		return null;
	}

	@Override
	public List<CommentDTO> getAllCommentsForIssue(Long projectId, Long issueId) {
		/*
		 * Get all comments in an Issue only if the current user a member of the project .
		 */
		if(isCurrentUserAMemberOfProject(projectId) && isIssuePartOfProject(issueId)) {
			List<CommentDTO> commentDTOs = commentRepository.findByIssue(currentIssue,CommentDTO.class);
			return commentDTOs;
		}
		return null;
	}

	@Override
	public CommentDTO updateComment(Long projectId, Long issueId, Long commentId, Comment comment) {
		/*
		 * Update the comment only if the current user is the creator of the comment.
		 * 
		 */
		if(isCurrentUserAMemberOfProject(projectId) && isIssuePartOfProject(issueId)) {
			if(isCurrentCommentPartOfCurrentIssue(commentId) && isCurrentUserCreatorOfCurrentComment()) {
				currentComment.setNote(comment.getNote());
				currentComment.setCode(comment.getCode());
				commentRepository.save(currentComment);
				return getCommentDTOById(commentId);
			}
		}
		return null;
	}

	@Override
	public void deleteComment(Long projectId, Long issueId, Long commentId) {
		/*
		 * Delete the comment only if the user is the creator.
		 */
		if(isCurrentUserAMemberOfProject(projectId) && isIssuePartOfProject(issueId)) {
			if(isCurrentCommentPartOfCurrentIssue(commentId) && isCurrentUserCreatorOfCurrentComment()) {
				commentRepository.delete(currentComment);
			}
		}

	}
	
	

}
