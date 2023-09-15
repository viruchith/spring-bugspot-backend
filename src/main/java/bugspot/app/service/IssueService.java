package bugspot.app.service;

import java.util.List;

import bugspot.app.dtos.IssueDTO;
import bugspot.app.exception.ProjectNotFoundException;
import bugspot.app.exception.UnauthorizedResourceActionException;
import bugspot.app.model.Issue;
import bugspot.app.model.Status;

public interface IssueService {
	List<IssueDTO> getAllIssueForProject(Long projectId) throws UnauthorizedResourceActionException,ProjectNotFoundException;
	IssueDTO addIssueToProject(Issue issue,Long projectId) throws UnauthorizedResourceActionException,ProjectNotFoundException;
	IssueDTO updateIssue(Long issueId, Issue issue,Long projectId);
	void deleteIssue(Long issueId,Long projectId);
	IssueDTO updateIssueStatus(Long issueId,Long projectId,Status status);
	//TODO change status of issue
}
