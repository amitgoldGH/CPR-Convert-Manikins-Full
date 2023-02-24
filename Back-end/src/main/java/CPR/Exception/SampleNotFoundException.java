package CPR.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SampleNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -93023554846647788L;
		
	public SampleNotFoundException() {}
	
	public SampleNotFoundException(String message) {
		super(message);
	}
	
	public SampleNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public SampleNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}