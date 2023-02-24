package CPR.DAL;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import CPR.Data.UserEntity;


public interface UserDao extends MongoRepository<UserEntity, String>{
	
	public UserEntity findByUsername(@Param("username") String username);
}
