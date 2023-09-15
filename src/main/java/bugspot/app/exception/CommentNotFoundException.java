package bugspot.app.exception;

public class CommentNotFoundException extends ResourceNotFoundException {

	public CommentNotFoundException() {
		super();
	}

	public CommentNotFoundException(String message) {
		super(message);
	}

}
