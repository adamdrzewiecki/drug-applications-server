package org.example.repository;

import org.example.model.DrugRecordApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRecordApplicationRepository extends MongoRepository<DrugRecordApplication, String> {
}
