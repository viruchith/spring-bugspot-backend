package bugspot.app.service;


import java.util.Map;

import bugspot.app.dtos.AppUserLoginDTO;
import bugspot.app.model.AppUser;

public interface AppUserService {
	AppUser addAppUser(AppUser appUser) throws UserAlreadyExistsException;
	Map<String, Object> loginAppUser(AppUserLoginDTO appUserLoginDTO);
	
	//MISC
	String loggedinHello();
}
