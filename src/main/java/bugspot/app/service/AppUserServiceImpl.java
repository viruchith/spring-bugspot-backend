package bugspot.app.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bugspot.app.auth.CurrentLoggedInAppUser;
import bugspot.app.auth.JWTProvider;
import bugspot.app.dtos.AppUserDTO;
import bugspot.app.dtos.AppUserLoginDTO;
import bugspot.app.exception.UserNotFoundException;
import bugspot.app.model.AppUser;
import bugspot.app.repository.AppUserDTORepository;
import bugspot.app.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {

	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTProvider jwtProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CurrentLoggedInAppUser currentLoggedInAppUser;

	@Autowired
	private AppUserDTORepository appUserDTORepository;
	
	
	@Override
	public AppUser addAppUser(AppUser appUser) throws UserNotFoundException {
		// check if username already exists
		if(appUserRepository.findFirstByUsername(appUser.getUsername()).isPresent()){
			throw new UserAlreadyExistsException("User with username : "+appUser.getUsername()+", already exists !");
		}
		
		// check if email already exists
		if(appUserRepository.findFirstByUsername(appUser.getEmail()).isPresent()){
			throw new UserAlreadyExistsException("User with email : "+appUser.getEmail()+", already exists !");
		}
		
		// save encoded password to DB
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		
		appUser =  appUserRepository.save(appUser);
		
		return appUser;
	}

	@Override
	public Map<String, Object> loginAppUser(AppUserLoginDTO appUserLoginDTO) {
		// authenticate user
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUserLoginDTO.getUsername(), appUserLoginDTO.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		AppUserDTO appUserDTO = appUserDTORepository.findFirstByUsername(appUserLoginDTO.getUsername(), AppUserDTO.class).orElseThrow(()->new UserNotFoundException("User with username : "+appUserLoginDTO.getUsername()+", does not exist !"));
		// return user details and login token
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		map.put("user", appUserDTO);
		return map;
	}

	@Override
	public String loggedinHello() {
//		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("CURRENT LOGGEDIN USER : {}",currentLoggedInAppUser.get().getUsername()); 
		return "Hello FROM loggedin user : "+currentLoggedInAppUser.get().getUsername();
	}

}
