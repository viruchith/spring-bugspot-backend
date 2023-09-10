package bugspot.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import bugspot.app.exception.UserNotFoundException;
import bugspot.app.model.AppUser;
import bugspot.app.repository.AppUserRepository;

@Component
public class CurrentLoggedInAppUser {

	@Autowired
	private AppUserRepository appUserRepository;

	public AppUser get() {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		AppUser appUser = appUserRepository.findFirstByUsername(userPrincipal.getUsername())
				.orElseThrow(() -> new UserNotFoundException(
						"User with username : " + userPrincipal.getUsername() + ", does not exist !"));

		return appUser;
	}
}
