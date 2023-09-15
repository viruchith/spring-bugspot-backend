package bugspot.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bugspot.app.model.Comment;
import java.util.List;
import bugspot.app.model.Issue;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	<T> Optional<T> findFirstById(Long id, Class<T> type);
	<T> List<T> findByIssue(Issue issue, Class<T> type);
}
