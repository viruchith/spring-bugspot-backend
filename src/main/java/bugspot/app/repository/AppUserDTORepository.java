package bugspot.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bugspot.app.model.AppUser;

@Repository
public interface AppUserDTORepository extends JpaRepository<AppUser, Long> {
	<T> Optional<T> findFirstByUsername(String username,Class<T> type);
}
