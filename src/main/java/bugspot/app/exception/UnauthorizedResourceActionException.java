package bugspot.app.exception;

public class UnauthorizedResourceActionException extends RuntimeException
{
	/*
	 * Thrown when a user tries to perform an action
	 * which the user is not authorized to.
	 * 
	 * eg : when a user tries to access a project
	 * details in which the user is not a member of.
	 * 
	 * */

	private static final long serialVersionUID = 8389588647283608204L;

	public UnauthorizedResourceActionException() {
		super();
	}

	public UnauthorizedResourceActionException(String message) {
		super(message);
	}

}
