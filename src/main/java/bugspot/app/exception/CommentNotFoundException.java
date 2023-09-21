package bugspot.app.exception;

public class CommentNotFoundException extends ResourceNotFoundException {

	/*
	 * 
	 * Thrown when a Comment is not found by the given ID.
	 * 
	 */
	
	private static final long serialVersionUID = -8066273819666978341L;

	
	
	public CommentNotFoundException() {
		super();
	}

	public CommentNotFoundException(String message) {
		super(message);
	}

}
