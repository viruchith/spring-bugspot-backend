package bugspot.app.exception;

public class IssueNotFoundException extends ResourceNotFoundException {

		private static final long serialVersionUID = -8773715919634118291L;

		public IssueNotFoundException() {
			super();
		}

		public IssueNotFoundException(String message) {
			super(message);
		}

		
}
