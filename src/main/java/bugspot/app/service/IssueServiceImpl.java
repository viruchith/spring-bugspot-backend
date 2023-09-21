package bugspot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bugspot.app.auth.CurrentLoggedInAppUser;
import bugspot.app.dtos.CommentDTO;
import bugspot.app.dtos.IssueDTO;
import bugspot.app.exception.CommentNotFoundException;
import bugspot.app.exception.IssueNotFoundException;
import bugspot.app.exception.ProjectNotFoundException;
import bugspot.app.exception.UnauthorizedResourceActionException;
import bugspot.app.model.AppUser;
import bugspot.app.model.Comment;
import bugspot.app.model.Issue;
import bugspot.app.model.Project;
import bugspot.app.model.Status;
import bugspot.app.repository.CommentRepository;
import bugspot.app.repository.IssueRepository;
import bugspot.app.repository.ProjectRepository;

@Service
public class IssueServiceImpl implements IssueService {

	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private CurrentLoggedInAppUser currentLoggedInAppUser;
	
	@Autowired
	private CommentRepository commentRepository;
	
	private AppUser currentUser;
	
	private Project currentProject;
	
	private Issue currentIssue;
	
	private boolean isCurrentUserAMemberOfProject(Long projectId) {
		/*
		 * Check if current user is a member of the given project ID.
		 */
		currentUser = currentLoggedInAppUser.get();
		
		currentProject = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project by the id : "+projectId+", was not found !"));

		if(currentProject.getMembers().contains(currentUser)) {
			return true;
		}else {
			throw new UnauthorizedResourceActionException("You are not a member of this project !");
		}
	}
	
	
	private boolean isCurrentUserCreatorOfTheIssue(Long projectId,Long issueId) {
		/*
		 * Check if the current user is the creator of the given issue id.
		 */
		if(isCurrentUserAMemberOfProject(projectId)) {
			currentIssue = issueRepository.findById(issueId).orElseThrow(()->new IssueNotFoundException("Issue by the id  :"+issueId+", does not exist ! "));
			if(currentIssue.getCreatorAppUser().equals(currentUser)) {
				return true;
			}else {
				throw new UnauthorizedResourceActionException("Only the creator of an issue can update the ISSUE details !");
			}
		}
		
		return false;
	}
	
	
	private IssueDTO getIssueDTOById(Long issueId) {
		IssueDTO issueDTO = issueRepository.findById(issueId,IssueDTO.class).orElseThrow(()->new IssueNotFoundException("Issue was not found !"));
		return issueDTO;
	}
		
	
	@Override
	public IssueDTO addIssueToProject(Issue issue, Long projectId) {
		/*
		 * add issue to the project only if the current user is a member of the project.
		 */
		if(isCurrentUserAMemberOfProject(projectId)) {
			issue.setProject(currentProject);
			issue.setCreatorAppUser(currentUser);
			issue.setStatus(Status.NEW);
			issue = issueRepository.save(issue);
			return getIssueDTOById(issue.getId());
		}
		return null;
		
	}

	@Override
	public List<IssueDTO> getAllIssueForProject(Long projectId) {
		/*
		 * 
		 * Get all issues for the given project.
		 * 
		 */
		if(isCurrentUserAMemberOfProject(projectId)) {
			List<IssueDTO> issueDTOs = issueRepository.findByProject(currentProject, IssueDTO.class);
			return issueDTOs;
		}
		return null;
	}
	

	@Override
	public IssueDTO updateIssue(Long issueId,Issue issue, Long projectId) {
		/*
		 * 
		 * Update issue for the given project only if the current user is the creator of the issue.
		 * 
		 */
		if(isCurrentUserCreatorOfTheIssue(projectId, issueId)) {
			currentIssue.setStatus(issue.getStatus());
			currentIssue.setCategory(issue.getCategory());
			currentIssue.setReproducibility(issue.getReproducibility());
			currentIssue.setSeverity(issue.getSeverity());
			currentIssue.setPriority(issue.getPriority());
			currentIssue.setProjectVersion(issue.getProjectVersion());
			currentIssue.setSummary(issue.getSummary());
			currentIssue.setDescription(issue.getDescription());
			currentIssue.setStepsToReproduce(issue.getStepsToReproduce());
			currentIssue.setAdditionalInformation(issue.getAdditionalInformation());
			currentIssue.setSourceCode(issue.getSourceCode());
			
			currentIssue = issueRepository.save(currentIssue);
			
			
			return getIssueDTOById(issueId);
		}
		return null;
	}


	@Override
	public void deleteIssue(Long issueId, Long projectId) {
		/*
		 * 
		 * Delete the issue only if the current user is the creator of the issue
		 * 
		 */
		if(isCurrentUserCreatorOfTheIssue(projectId, issueId)) {
			issueRepository.delete(currentIssue);
		}
	}


	@Override
	public IssueDTO updateIssueStatus(Long issueId, Long projectId, Status status) {
		/*
		 * 
		 * Update the issue status only if the current user is the creator of the issue.
		 * 
		 */
		if(isCurrentUserCreatorOfTheIssue(projectId, issueId)) {
			currentIssue.setStatus(status);
			issueRepository.save(currentIssue);
			return getIssueDTOById(issueId);
		}
		return null;
	}


	@Override
	public CommentDTO updateAcceptedComment(Long issueId, Long projectId, Comment comment) {
		/*
		 * 
		 * Update the accepted comment of the issue only if the user is the creator of the issue.
		 * 
		 */
		if(isCurrentUserCreatorOfTheIssue(projectId, issueId)) {
			Comment commentPersisted = commentRepository.findById(comment.getId()).orElseThrow(()->new CommentNotFoundException("Comment by the ID : "+comment.getId()+", was not found !"));
			if(currentIssue.getComments().contains(commentPersisted)) {
				currentIssue.setAcceptedComment(commentPersisted);
				CommentDTO commentDTO = commentRepository.findFirstById(commentPersisted.getId(), CommentDTO.class).orElseThrow(()->new CommentNotFoundException("Comment by the ID : "+comment.getId()+", was not found !"));;
				return commentDTO;
			}else {
				throw new UnauthorizedResourceActionException("The comment is not part of the specified ISSUE !");
			}
		}
		return null;
	}
	
	
	

}
