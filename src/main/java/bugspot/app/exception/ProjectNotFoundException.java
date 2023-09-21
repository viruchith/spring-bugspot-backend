package bugspot.app.exception;

public class ProjectNotFoundException extends ResourceNotFoundException {

	/*
	 * 
	 * Thrown when a project is not found by the given ID.
	 * 
	 */
	
	private static final long serialVersionUID = 176228822712824890L;

	public ProjectNotFoundException() {
		super();
	}

	public ProjectNotFoundException(String message) {
		super(message);
	}
	
	

}
