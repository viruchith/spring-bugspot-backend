package bugspot.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bugspot.app.model.AppUser;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findFirstByUsername(String username);
	Optional<AppUser> findFirstByEmail(String email);
}
