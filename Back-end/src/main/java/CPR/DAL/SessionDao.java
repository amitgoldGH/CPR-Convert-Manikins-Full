package CPR.DAL;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import CPR.Data.SessionEntity;

public interface SessionDao extends MongoRepository<SessionEntity, String>{
	
	public List<SessionEntity> findAllByUsername(@Param("username") String username);

	public SessionEntity findBySessionId(@Param("sessionId") String sessionId);
	

}
