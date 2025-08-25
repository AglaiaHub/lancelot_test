package test.test.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import test.test.model.ListTasks;

import java.util.Optional;

public interface TaskRepository extends MongoRepository<ListTasks, String> {
    boolean existsByFileName(String fileName);

    Optional<ListTasks> findByFileName(String fileName);

}
