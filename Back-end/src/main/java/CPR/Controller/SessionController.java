package CPR.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import CPR.Boundary.SessionBoundary;
import CPR.Service.SessionService;

@RestController
public class SessionController {
	
	private SessionService sessionService;
	
	@Autowired
	public SessionController(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	//POST request, path="/api/sessions"
	//Accept: SessionBoundary
	//Return: SessionBoundary
	@RequestMapping(
			path="/api/sessions",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object createSession(@RequestBody SessionBoundary session) {
		System.out.println("SESSION_CONTROLLER /api/sessions CREATE SESSION TYPE_POST called " + session.toString());
		
		return this.sessionService.createSession(session);
	}
	
	//GET request, path="/api/sessions/{id}"
	//Accept: Nothing
	//Return SessionBoundary with given the given ID
	@RequestMapping(
			path = "/api/sessions/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getSessionById(@PathVariable("id") String session_Id) {
		System.out.println("SESSION_CONTROLLER /api/sessions/" + session_Id + " GET SESSION BY ID TYPE_GET called");
		
		return this.sessionService.getSessionById(session_Id);
	}
	
	//PUT request, path="/api/sessions"
	//Accept: Updated SessionBoundary object of an existing SessionBoundary
	//Return: Nothing
	@RequestMapping(
			path = "/api/sessions",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateSession(@RequestBody SessionBoundary session) {
		System.out.println("SESSION_CONTROLLER /api/sessions UPDATE SESSION TYPE_PUT called " + session.toString());
		
		this.sessionService.updateSession(session);
	}
	
	//GET request, path="/api/sessions/calculate/{id}"
	//Accept: Nothing
	//Return Session Boundary with updated measurements
	@RequestMapping(
			path= "/api/sessions/calculate/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Object calculateSessionSummaryById(@PathVariable("id") String session_Id) {
		System.out.println("SESSION_CONTROLLER /api/sessions/calculate/cpr/" + session_Id + " CALCULATE SESSION SUMMARY BY ID TYPE_GET called");
		
		return this.sessionService.calculateSessionSummaryById(session_Id);
	}
	
}
