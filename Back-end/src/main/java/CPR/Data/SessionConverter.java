package CPR.Data;

import org.springframework.stereotype.Component;

import CPR.Boundary.SessionBoundary;

@Component
public class SessionConverter {
	
	public SessionBoundary convertToBoundary(SessionEntity entity)
	{
		SessionBoundary sessionBoundary=new SessionBoundary();

		sessionBoundary.setSessionId(entity.getSessionId());
		sessionBoundary.setUsername(entity.getUsername());
		sessionBoundary.setType(entity.getType().name());
		sessionBoundary.setMeasurementSummary(entity.getMeasurementSummary());
		sessionBoundary.setCreationDate(entity.getCreationDate());
		
		return sessionBoundary;

	}
	public SessionEntity convertToEntity(SessionBoundary boundary)
	{
		SessionEntity sessionEntity=new SessionEntity();

		sessionEntity.setSessionId(boundary.getSessionId());
		sessionEntity.setUsername(boundary.getUsername());
		sessionEntity.setType(SessionType.valueOf(boundary.getType()));
		sessionEntity.setMeasurementSummary(boundary.getMeasurementSummary());
		sessionEntity.setCreationDate(boundary.getCreationDate());
		
		return sessionEntity;

	}

}
