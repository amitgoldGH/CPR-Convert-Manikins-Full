package CPR.DAL;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import CPR.Data.SampleEntity;

public interface SampleDao extends MongoRepository<SampleEntity, String> {
	
	public List<SampleEntity> findAllBySessionId(@Param("session_Id") String sessionId);
	
	public SampleEntity findBySampleId(@Param("sample_Id") String sample_Id);
	
	
}
