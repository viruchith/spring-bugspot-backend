package bugspot.app.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import bugspot.app.service.UserAlreadyExistsException;

@ControllerAdvice
public class AppCustomExceptionHandler {
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException  e){
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(error->{
			FieldError fieldError = (FieldError) error;
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		});
		return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
		
	
	
	
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundExistsException(ResourceNotFoundException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	@ExceptionHandler(UnauthorizedResourceActionException.class)
	public ResponseEntity<?> handleUnauthorizedResourceActionException(UnauthorizedResourceActionException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
	}
	
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadResourceActionException.class)
	public ResponseEntity<?> handleBadResourceActionException(BadResourceActionException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	
	
	

	


	
	
}
