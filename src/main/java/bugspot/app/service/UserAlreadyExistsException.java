package bugspot.app.service;

public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException() {
		super();
	}

	public UserAlreadyExistsException(String message) {
		super(message);
	}

}
