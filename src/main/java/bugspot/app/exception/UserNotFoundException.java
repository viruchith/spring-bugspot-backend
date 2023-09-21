package bugspot.app.exception;

public class UserNotFoundException extends RuntimeException {

	/*
	 *
	 *Thrown when a user is not found by the given username or ID.
	 *
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2719703284678360510L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message) {
		super(message);
	}

}
