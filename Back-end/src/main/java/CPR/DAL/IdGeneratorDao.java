package CPR.DAL;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdGeneratorDao extends MongoRepository<CPR.Data.IdGeneratorEntity, String>{

}

