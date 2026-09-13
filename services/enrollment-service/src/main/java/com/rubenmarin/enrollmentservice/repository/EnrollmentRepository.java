package com.rubenmarin.enrollmentservice.repository;

import com.rubenmarin.enrollmentservice.document.EnrollmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnrollmentRepository
        extends MongoRepository<EnrollmentDocument, String> {
}