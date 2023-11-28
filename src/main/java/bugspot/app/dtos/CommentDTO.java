package bugspot.app.dtos;

import java.time.LocalDateTime;

public interface CommentDTO {
	Long getId();
	AppUserDTO getCreatorAppUser();
//	IssueDTO getIssue();
	String getNote();
	String getCode();
	LocalDateTime getCreatedAt();
	LocalDateTime getUpdatedAt();
}
