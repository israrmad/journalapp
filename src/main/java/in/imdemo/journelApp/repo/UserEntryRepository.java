package in.imdemo.journelApp.repo;

import in.imdemo.journelApp.entity.JournalEntry;
import in.imdemo.journelApp.entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserEntryRepository extends MongoRepository<UserEntity, ObjectId> {

    public Optional<UserEntity> findByUserName(String userName);

}
