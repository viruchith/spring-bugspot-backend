package bugspot.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bugspot.app.model.AppUser;
import bugspot.app.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	<T> List<T> findAllByOwnerAppUser(AppUser ownerAppUser,Class<T> type);
	<T> List<T> findAllByMembersContains(AppUser appUser,Class<T> type);
	<T> T findById(Long id,Class<T> type);
}
