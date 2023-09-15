package bugspot.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bugspot.app.model.Issue;
import bugspot.app.model.Project;


@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
	<T> Optional<T> findById(Long id,Class<T> type);
	 <T> List<T> findByProject(Project project,Class<T>type);
}
