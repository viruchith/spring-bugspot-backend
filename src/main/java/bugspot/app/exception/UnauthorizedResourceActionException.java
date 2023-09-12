package bugspot.app.exception;

public class UnauthorizedResourceActionException extends RuntimeException
{

	private static final long serialVersionUID = 8389588647283608204L;

	public UnauthorizedResourceActionException() {
		super();
	}

	public UnauthorizedResourceActionException(String message) {
		super(message);
	}

}
