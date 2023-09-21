package bugspot.app.exception;

public class BadResourceActionException extends RuntimeException {

	/*
	 * 
	 * Thrown when a user performs an invalid action on a resource.
	 * 
	 */
	private static final long serialVersionUID = 8389588647283608204L;

	public BadResourceActionException() {
		super();
	}

	public BadResourceActionException(String message) {
		super(message);
	}
	
}
