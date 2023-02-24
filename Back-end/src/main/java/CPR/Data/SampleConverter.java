package CPR.Data;

import org.springframework.stereotype.Component;

import CPR.Boundary.SampleBoundary;

@Component
public class SampleConverter {

	public SampleBoundary convertToBoundary(SampleEntity entity)
	{
		SampleBoundary sampleBoundary=new SampleBoundary();

		sampleBoundary.setSampleId(entity.getSampleId());
		sampleBoundary.setSessionId(entity.getSessionId());
		sampleBoundary.setMeasurements(entity.getMeasurements());
		
		return sampleBoundary;

	}
	public SampleEntity convertToEntity(SampleBoundary boundary)
	{
		SampleEntity sampleEntity=new SampleEntity();
		
		sampleEntity.setSampleId(boundary.getSampleId());
		sampleEntity.setSessionId(boundary.getSessionId());
		sampleEntity.setMeasurements(boundary.getMeasurements());
		
		return sampleEntity;

	}
}
