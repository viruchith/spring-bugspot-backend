package bugspot.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bugspot.app.auth.JWTProvider;
import bugspot.app.dtos.AppUserLoginDTO;
import bugspot.app.model.AppUser;
import bugspot.app.service.AppUserService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private AppUserService appUserService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody @Valid AppUser appUser){
		appUser = appUserService.addAppUser(appUser);
		return new ResponseEntity<>(appUser,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody @Valid AppUserLoginDTO appUserLoginDTO){
		Map<String, Object> map = appUserService.loginAppUser(appUserLoginDTO);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/loginHello")
	public String loginHello() {
		return""+appUserService.loggedinHello();
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	
	
	
}
