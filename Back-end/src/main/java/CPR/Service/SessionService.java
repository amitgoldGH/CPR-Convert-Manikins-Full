package CPR.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import CPR.Boundary.SessionBoundary;


public interface SessionService {

	//CREATE
	public Object createSession(SessionBoundary session);
	
	// RETRIVE
	public List<Object> getAllSessions();
	
	public List<Object> getAllSessionsByUser(String username);
	
	public Object getSessionById(String session_Id);
	
	//UPDATE
	public void updateSession(SessionBoundary session);
	
	//DELETE
	public void deleteAllSessions();
	
	public void deleteSessionById(String session_Id);
	
	public void deleteSessionByUsername(String username);

	public Object calculateSessionSummaryById(String session_Id);
}
