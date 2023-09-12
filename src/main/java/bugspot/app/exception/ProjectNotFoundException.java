package bugspot.app.exception;

public class ProjectNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 176228822712824890L;

	public ProjectNotFoundException() {
		super();
	}

	public ProjectNotFoundException(String message) {
		super(message);
	}
	
	

}
