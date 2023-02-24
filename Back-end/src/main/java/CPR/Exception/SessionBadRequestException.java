package CPR.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SessionBadRequestException extends RuntimeException{

	private static final long serialVersionUID = -8673187677573235999L;

	public SessionBadRequestException() {}

	public SessionBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionBadRequestException(String message) {
		super(message);
	}

	public SessionBadRequestException(Throwable cause) {
		super(cause);
	}
	
	
}
