package CPR.Service;


import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CPR.Boundary.SampleBoundary;
import CPR.Boundary.SessionBoundary;
import CPR.DAL.IdGeneratorDao;
import CPR.DAL.SessionDao;
import CPR.Data.IdGeneratorEntity;
import CPR.Data.SampleEntity;
import CPR.Data.SessionConverter;
import CPR.Data.SessionEntity;
import CPR.Data.SessionType;
import CPR.Exception.SessionBadRequestException;
import CPR.Exception.SessionNotFoundException;
import CPR.Finals.FINALS;

@Service
public class SessionServiceImplMongo implements SessionService {

	
	private SessionConverter converter;
	private SessionDao sessionDao;
	private SampleService service;
	private IdGeneratorDao idGeneratorDao;
		
	@Autowired
	public SessionServiceImplMongo(SessionDao sessionDao, SessionConverter converter, IdGeneratorDao idGeneratorDao, SampleService service) {
		this.sessionDao = sessionDao;
		this.converter = converter;
		this.idGeneratorDao = idGeneratorDao;
		
		this.service = service;
	}
	
	@PostConstruct
	public void init() {}
	@Override
	public Object createSession(SessionBoundary session) {
		if (session.getUsername() != null && session.getType() != null) {
			SessionEntity entity = converter.convertToEntity(session);
			entity.setCreationDate(new Date());
			
			//Generate random new ID
			IdGeneratorEntity idContainer = new IdGeneratorEntity();
			idContainer = this.idGeneratorDao.insert(idContainer);
			String newId = this.idGeneratorDao.findAll().get(0).getId();
			this.idGeneratorDao.deleteAll();
			
			entity.setSessionId(newId);
			entity = sessionDao.save(entity);
			System.out.println("Created and stored session in memory successfully.");
			
			return converter.convertToBoundary(entity);
		}
		else {
			// TODO: Invalid username
			throw new SessionBadRequestException("Invalid Username or session type");
		}
		//return null;
	}

	@Override
	public List<Object> getAllSessions() {
		return sessionDao.findAll()
				.stream().parallel()
				.map(converter::convertToBoundary)
				.collect(Collectors.toList());
	}
	

	@Override
	public List<Object> getAllSessionsByUser(String username) {		
		List<SessionEntity> list = sessionDao.findAllByUsername(username);
		
		return list.stream()
				.map(this.converter::convertToBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public Object getSessionById(String session_Id) {
		if (session_Id != null)
		{
			SessionEntity entity = sessionDao.findBySessionId(session_Id);
			if (entity != null)
				return converter.convertToBoundary(entity);
			else
				throw new SessionNotFoundException("No session found with given id");
		}
		else
			throw new SessionBadRequestException("Invalid session id given.");
	}
	
	@Override
	public Object calculateSessionSummaryById(String session_Id) 
	{
		if (session_Id != null) // Valid session id 
		{
			SessionEntity entity = sessionDao.findBySessionId(session_Id);
			
			if (entity != null) // Session actually exists 
			{
				List<Object> relatedSamples = service.retrieveAllSessionSamples(session_Id); 
				int num_Of_Samples = relatedSamples.size();
				if  (num_Of_Samples > 0) 
				{
					switch(entity.getType().name())  
					{
						case "CPR": {
							// CPR CALCULATION HERE //
							int num_Of_Good_Depth_Samples = 0, num_Of_Good_Rate_Samples = 0, num_Of_Good_Side_Touch_Samples = 0, num_Of_Center_Touch_Samples = 0;
							
							for(Object sample : relatedSamples) 
							{
								// SAMPLE MEASUREMENT FORMAT: DEPTH[0], RATE[1], LEFT_TOUCH[2], TOP_TOUCH[3], BOTTOM_TOUCH[4], RIGHT_TOUCH[5], CENTER_SENSOR[6]
								String[] measurements = ((SampleBoundary)sample).getMeasurements();
								int depth = Integer.parseInt(measurements[0]), rate = Integer.parseInt(measurements[1]), center_touch = Integer.parseInt(measurements[6]);
								
								if (depth >= FINALS.GOOD_DEPTH_BOT && depth <= FINALS.GOOD_DEPTH_TOP) 
									num_Of_Good_Depth_Samples += 1;							
								if (rate >= FINALS.GOOD_CPR_RATE_BOT && rate <= FINALS.GOOD_CPR_RATE_TOP)
									num_Of_Good_Rate_Samples += 1;
								if (measurements[2].equals("0") && measurements[3].equals("0") && measurements[4].equals("0") && measurements[5].equals("0")) 
									num_Of_Good_Side_Touch_Samples += 1;
								if (center_touch >= FINALS.GOOD_CENTER_TOUCH_THRESHOLD)
									num_Of_Center_Touch_Samples += 1;
								
							}
							
							float depth_Percentage, rate_Percentage, side_Touch_Percentage, center_Touch_Percentage;
							depth_Percentage = (float)num_Of_Good_Depth_Samples / num_Of_Samples;
							rate_Percentage = (float)num_Of_Good_Rate_Samples / num_Of_Samples;
							side_Touch_Percentage = (float)num_Of_Good_Side_Touch_Samples / num_Of_Samples;
							center_Touch_Percentage = (float)num_Of_Center_Touch_Samples / num_Of_Samples;
							
							String[] results = new String[4];
							results[0] = (depth_Percentage >= FINALS.PASSING_SCORE_PERCENTAGE) ? FINALS.PASSING_MESSAGE : FINALS.FAILING_MESSAGE;
							results[1] = (rate_Percentage >= FINALS.PASSING_SCORE_PERCENTAGE) ? FINALS.PASSING_MESSAGE : FINALS.FAILING_MESSAGE;
							results[2] = (side_Touch_Percentage >= FINALS.PASSING_SCORE_PERCENTAGE) ? FINALS.PASSING_MESSAGE : FINALS.FAILING_MESSAGE;
							results[3] = (center_Touch_Percentage >= FINALS.PASSING_SCORE_PERCENTAGE) ? FINALS.PASSING_MESSAGE : FINALS.FAILING_MESSAGE;
							entity.setMeasurementSummary(results);
							
							sessionDao.save(entity);
							return converter.convertToBoundary(entity); 
						}
						case "BVM": {
							// BVM CALCULATION HERE //
							int num_Of_Good_Rate = 0, num_Of_Good_Airway = 0, num_Of_Good_Seal = 0;
							for(Object sample : relatedSamples) 
							{
								// Sample format is :  PRESSURE_RATE[0] (sample 10 times to get rate),AIRWAY[1], SEAL[2]
								String[] measurements = ((SampleBoundary)sample).getMeasurements();
								if (measurements[0].equals("1"))
									num_Of_Good_Rate += 1;
								if (measurements[1].equals("1"))
									num_Of_Good_Airway += 1;
								if (measurements[2].equals("1"))
									num_Of_Good_Seal += 1;
							}
							float rate_Percentage, airway_Percentage, seal_Percentage;
							rate_Percentage = (float)num_Of_Good_Rate / num_Of_Samples;
							airway_Percentage = (float)num_Of_Good_Airway / num_Of_Samples;
							seal_Percentage = (float)num_Of_Good_Seal / num_Of_Samples;
							
							String[] results = new String[3];
							results[0] = (rate_Percentage >= FINALS.PASSING_SCORE_PERCENTAGE) ? FINALS.PASSING_MESSAGE : FINALS.FAILING_MESSAGE;
							results[1] = (airway_Percentage >= FINALS.PASSING_SCORE_PERCENTAGE) ? FINALS.PASSING_MESSAGE : FINALS.FAILING_MESSAGE;
							results[2] = (seal_Percentage >= FINALS.PASSING_SCORE_PERCENTAGE) ? FINALS.PASSING_MESSAGE : FINALS.FAILING_MESSAGE;
							entity.setMeasurementSummary(results);
							
							sessionDao.save(entity);
							return converter.convertToBoundary(entity);
						}
						default: {
							return converter.convertToBoundary(entity);
						}
					
					}
					
				}
				else
					throw new SessionBadRequestException("No samples for given session");
			} 
			else
				throw new SessionNotFoundException("No session found with given id");
		} 
		else 
			throw new SessionBadRequestException("Invalid session id given.");
	}
	

	@Override
	public void updateSession(SessionBoundary session) {
		if (session.getSessionId() != null && session.getUsername() != null && session.getType() != null)
		{
			SessionEntity entity = sessionDao.findBySessionId(session.getSessionId());
			if (entity != null)
			{
				entity.setUsername(session.getUsername());
				entity.setType(SessionType.valueOf(session.getType()));
				
				if (session.getMeasurementSummary() != null)
					entity.setMeasurementSummary(session.getMeasurementSummary());
				
				sessionDao.save(entity);
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
		service.deleteAllSamples();
		sessionDao.deleteAll();
		System.out.println("Session storage cleared");
		
	}

	@Override
	public void deleteSessionById(String session_Id) {
		if (sessionDao.findBySessionId(session_Id) != null)
		{
			sessionDao.deleteById(session_Id);
			
			List<Object> relatedSamples = service.retrieveAllSessionSamples(session_Id); 
			
			for (Object sample : relatedSamples) {
				service.deleteSample(((SampleBoundary)sample).getSampleId());
			}
		}
		else
			System.out.println("Session doesnt exist");
		// TODO Exception for session not found
		
	}

	@Override
	public void deleteSessionByUsername(String username) {
		
		for (SessionEntity entity : sessionDao.findAllByUsername(username).stream().parallel().collect(Collectors.toList())) {
			deleteSessionById(entity.getSessionId());
		}
		System.out.println("Deleted sessions by username: " + username);
		
	}

}

