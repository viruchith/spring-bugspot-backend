package bugspot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bugspot.app.auth.CurrentLoggedInAppUser;
import bugspot.app.dtos.IssueDTO;
import bugspot.app.exception.IssueNotFoundException;
import bugspot.app.exception.ProjectNotFoundException;
import bugspot.app.exception.UnauthorizedResourceActionException;
import bugspot.app.model.AppUser;
import bugspot.app.model.Issue;
import bugspot.app.model.Project;
import bugspot.app.model.Status;
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
	
	private AppUser currentUser;
	
	private Project currentProject;
	
	private Issue currentIssue;
	
	private boolean isCurrentUserAMemberOfProject(Long projectId) {
		currentUser = currentLoggedInAppUser.get();
		
		currentProject = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project by the id : "+projectId+", was not found !"));

		if(currentProject.getMembers().contains(currentUser)) {
			return true;
		}else {
			throw new UnauthorizedResourceActionException("You are not a member of this project !");
		}
	}
	
	
	private boolean isCurrentUserCreatorOfTheIssue(Long projectId,Long issueId) {
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
		
	
	@Override
	public IssueDTO addIssueToProject(Issue issue, Long projectId) {
		if(isCurrentUserAMemberOfProject(projectId)) {
			issue.setProject(currentProject);
			issue.setCreatorAppUser(currentUser);
			issue.setStatus(Status.NEW);
			issue = issueRepository.save(issue);
			IssueDTO issueDTO = issueRepository.findById(issue.getId(),IssueDTO.class).orElseThrow(()->new IssueNotFoundException("Issue was not found !"));
			return issueDTO;
		}
		return null;
		
	}

	@Override
	public List<IssueDTO> getAllIssueForProject(Long projectId) {
		if(isCurrentUserAMemberOfProject(projectId)) {
			List<IssueDTO> issueDTOs = issueRepository.findByProject(currentProject, IssueDTO.class);
			return issueDTOs;
		}
		return null;
	}
	

	@Override
	public IssueDTO updateIssue(Long issueId,Issue issue, Long projectId) {
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
			
			IssueDTO issueDTO =  issueRepository.findById(currentIssue.getId(), IssueDTO.class).orElseThrow(()->new IssueNotFoundException());
			
			return issueDTO;
		}
		return null;
	}


	@Override
	public void deleteIssue(Long issueId, Long projectId) {
		if(isCurrentUserCreatorOfTheIssue(projectId, issueId)) {
			issueRepository.delete(currentIssue);
		}
	}


	@Override
	public IssueDTO updateIssueStatus(Long issueId, Long projectId, Status status) {
		if(isCurrentUserCreatorOfTheIssue(projectId, issueId)) {
			currentIssue.setStatus(status);
			issueRepository.save(currentIssue);
			IssueDTO issueDTO =  issueRepository.findById(currentIssue.getId(), IssueDTO.class).orElseThrow(()->new IssueNotFoundException());
			return issueDTO;
		}
		return null;
	}
	
	
	

}
