package CPR.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SessionNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7650754524862661459L;
	
	public SessionNotFoundException() {}
	
	public SessionNotFoundException(String message) {
		super(message);
	}
	
	public SessionNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public SessionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}