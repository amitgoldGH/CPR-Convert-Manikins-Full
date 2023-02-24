package CPR.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import CPR.Boundary.SampleBoundary;

public interface SampleService {
	
	//CREATE
	public Object createSample(SampleBoundary sample);
	
	//RETRIEVE
	public List<Object> retrieveAllSessionSamples(String session_Id);
	
	public List<Object> getAllSamples();
	
	//UPDATE
	public void updateSample(SampleBoundary update_sample);
	
	//DELETE
	public void deleteAllSamples();
	
	public void deleteSample(String sample_Id);


}
