package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.document.EnrollmentMongoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnrollmentMongoRepository extends MongoRepository<EnrollmentMongoDocument, String> {


}
