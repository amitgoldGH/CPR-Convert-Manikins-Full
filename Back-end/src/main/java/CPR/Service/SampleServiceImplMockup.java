package CPR.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import CPR.Boundary.SampleBoundary;
import CPR.Data.SampleConverter;
import CPR.Data.SampleEntity;
import CPR.Exception.SampleBadRequestException;
import CPR.Exception.SampleNotFoundException;

//@Service
public class SampleServiceImplMockup implements SampleService {

	private Map<String, SampleEntity> storage;
	private SampleConverter converter;
	private AtomicLong counter;
	
	@Autowired
	public SampleServiceImplMockup(SampleConverter converter)
	{
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
	public Object createSample(SampleBoundary sample) {
		SampleEntity sampleEntity = converter.convertToEntity(sample);
		sampleEntity.setSampleId(String.valueOf(counter.getAndIncrement()));
		
		if (sampleEntity.getSampleId() != null && sampleEntity.getSessionId() != null)
		{
			System.out.println("createSample Success, storing in memory!");
			storage.put(sampleEntity.getSampleId(), sampleEntity);
			return converter.convertToBoundary(sampleEntity);
		}
		else {
			System.out.println("\nERROR!!!!!!! createSample in SampleServiceImpl\n" + sampleEntity.toString());
			// TODO: Exception failed to create sample_id or invalid session_id in given sample.
			//return converter.convertToBoundary(new SampleEntity("NULL", "NULL", new String[] {"NULL"}));
			throw new SampleBadRequestException("Bad sample input given.");
		}
	}

	@Override
	public List<Object> retrieveAllSessionSamples(String session_Id) {
		List<Object> filteredList = new ArrayList<Object>();
		
		for (SampleEntity entity : storage.values().stream().parallel().collect(Collectors.toList())) {
			if (entity.getSessionId().equalsIgnoreCase(session_Id))
				filteredList.add(converter.convertToBoundary(entity));
		}
		
		return filteredList;
		
		
	}

	@Override
	public List<Object> getAllSamples() {
		return storage.values().stream().parallel().map(converter::convertToBoundary).collect(Collectors.toList());
	}

	@Override
	public void updateSample(SampleBoundary sample) {
		if (sample.getSampleId() != null && sample.getSessionId() != null)
		{
			if (storage.get(sample.getSampleId()) != null)
			{
				storage.put(sample.getSampleId(), converter.convertToEntity(sample));
				System.out.println("Update sample success, sample_id: " + sample.getSampleId() + " updated.");
				
			}
			else 
			{
				throw new SampleNotFoundException("Existing sample with same id doesn't exist.");
				// TODO: Exception existing sample with same id doesnt exist.
			}
		}
		else
		{
			throw new SampleBadRequestException("Bad sample, sample id or session id are null");
			// TODO: Exception, invalid sample
		}
	}

	@Override
	public void deleteAllSamples() {
		storage.clear();
		System.out.println("Sample Storage Cleared.");
		
	}

	@Override
	public void deleteSample(String sample_Id) {
		if (sample_Id != null) {
			storage.remove(sample_Id);
			System.out.println("Deleted sample with sample_Id: " + sample_Id);	
		}
		else {
			// TODO : EXCEPTION SAMPLE DOESNT EXIST			
		}
		
	}

}
