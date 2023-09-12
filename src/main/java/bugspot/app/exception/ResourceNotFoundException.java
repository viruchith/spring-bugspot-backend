package bugspot.app.exception;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 2177531117587599443L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
