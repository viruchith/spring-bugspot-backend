package bugspot.app.service;

import java.util.List;

import bugspot.app.dtos.CommentDTO;
import bugspot.app.model.Comment;

public interface CommentService {
	CommentDTO createComment(Long projectId, Long issueId,Comment comment);
	List<CommentDTO> getAllCommentsForIssue(Long projectId, Long issueId);
	CommentDTO updateComment(Long projectId,Long issueId,Long commentId,Comment comment);
	void deleteComment(Long projectId,Long issueId,Long commentId);
}
