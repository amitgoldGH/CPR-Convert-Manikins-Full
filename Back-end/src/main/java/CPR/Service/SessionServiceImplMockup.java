package CPR.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CPR.Boundary.SessionBoundary;
import CPR.Data.SessionConverter;
import CPR.Data.SessionEntity;
import CPR.Exception.SessionBadRequestException;
import CPR.Exception.SessionNotFoundException;

//@Service
public class SessionServiceImplMockup implements SessionService{

	private Map<String, SessionEntity> storage;
	private SessionConverter converter;
	private AtomicLong counter;
	
	@Autowired
	public SessionServiceImplMockup(SessionConverter converter) {
		this.converter = converter;
	}
	
	@PostConstruct
	public void init()
	{
		// initialize thread safe storage
		this.storage = Collections.synchronizedMap(new HashMap<>());
		
		// initialize counter
		this.counter = new AtomicLong(1L);		
	}
	@Override
	public Object createSession(SessionBoundary session) {
		if (session.getUsername() != null) {
			SessionEntity entity = converter.convertToEntity(session);
			entity.setCreationDate(new Date());
			entity.setSessionId(String.valueOf(counter.getAndIncrement()));
			storage.put(entity.getSessionId(), entity);
			System.out.println("Created and stored session in memory successfully.");
			
			return converter.convertToBoundary(entity);
		}
		else {
			// TODO: Invalid username
			throw new SessionBadRequestException("Invalid Username");
		}
		//return null;
	}

	@Override
	public List<Object> getAllSessions() {
		return storage.values().stream().parallel().map(converter::convertToBoundary).collect(Collectors.toList());
	}
	

	@Override
	public List<Object> getAllSessionsByUser(String username) {
		List<Object> filteredList = new ArrayList<Object>();
		
		for (SessionEntity entity : storage.values().stream().parallel().collect(Collectors.toList())) {
			if (entity.getUsername().equalsIgnoreCase(username))
				filteredList.add(converter.convertToBoundary(entity));
		}
		
		return filteredList;
	}

	@Override
	public Object getSessionById(String session_Id) {
		if (session_Id != null & storage.get(session_Id) != null)
			return converter.convertToBoundary(storage.get(session_Id));
		else // TODO EXCEPTION SESSION NOT FOUND
			throw new SessionNotFoundException("No session with given id found");
	}

	@Override
	public void updateSession(SessionBoundary session) {
		if (session.getSessionId() != null && session.getUsername() != null)
		{
			if (storage.get(session.getSessionId()) != null)
			{
				storage.put(session.getSessionId(), converter.convertToEntity(session));
				System.out.println("Update sample success, sample_id: " + session.getSessionId() + " updated.");
				
			}
			else 
			{
				// TODO: Exception existing session with same id doesnt exist.
				throw new SessionNotFoundException("Existing session with given id doesn't exist");
			}
		}
		else
		{
			// TODO: Exception, invalid session
			throw new SessionBadRequestException("Invalid session input given.");
		}
		
	}

	@Override
	public void deleteAllSessions() {
		storage.clear();
		System.out.println("Session storage cleared");
		
	}

	@Override
	public void deleteSessionById(String session_Id) {
		if (storage.get(session_Id) != null)
			storage.remove(session_Id);
		else
			System.out.println("Session doesnt exist");
		// TODO Exception for session not found
		
	}

	@Override
	public void deleteSessionByUsername(String username) {
		int delCounter = 0;
		for (SessionEntity entity : storage.values().stream().parallel().collect(Collectors.toList())) {
			if (entity.getUsername().equalsIgnoreCase(username))
			{
				storage.remove(entity.getSessionId());
				delCounter++;
			}
		}
		System.out.println("Delete session by username: " + username + ", Deleted " + delCounter + " sessions.");
		
	}

	@Override
	public Object calculateSessionSummaryById(String session_Id) {
		// TODO Auto-generated method stub
		return null;
	}

}
