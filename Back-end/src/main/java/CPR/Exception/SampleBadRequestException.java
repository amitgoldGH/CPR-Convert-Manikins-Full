package CPR.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SampleBadRequestException extends RuntimeException {
	private static final long serialVersionUID = 8569566806305082833L;

	public SampleBadRequestException() {
		super();
	}

	public SampleBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public SampleBadRequestException(String message) {
		super(message);
	}

	public SampleBadRequestException(Throwable cause) {
		super(cause);
	}

	
}
