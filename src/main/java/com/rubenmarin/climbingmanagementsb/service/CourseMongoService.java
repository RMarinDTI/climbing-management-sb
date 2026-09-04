package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.document.CourseMongoDocument;
import com.rubenmarin.climbingmanagementsb.repository.CourseMongoRepository;
import org.springframework.boot.mongodb.autoconfigure.MongoConnectionDetails;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseMongoService {

    private final CourseMongoRepository courseMongoRepository;
    private final MongoTemplate mongoTemplate;
    private final Environment environment;
    private final MongoConnectionDetails mongoConnectionDetails;

    public CourseMongoService(CourseMongoRepository courseMongoRepository , MongoTemplate mongoTemplate, Environment environment, MongoConnectionDetails mongoConnectionDetails) {
        this.courseMongoRepository = courseMongoRepository;
        this.mongoTemplate = mongoTemplate;
        this.environment = environment;
        this.mongoConnectionDetails = mongoConnectionDetails;
    }

    public String getDatabaseName() {
        return mongoTemplate.getMongoDatabaseFactory().getClass().getName()
                + " | "
                + mongoTemplate.getDb().getName();
    }
    public String getMongoUri() {
        return environment.getProperty("spring.data.mongodb.uri");
    }

    public String getMongoConnectionDetails() {
        return mongoConnectionDetails.getConnectionString().getConnectionString();
    }

    public String getMongoConnectionDetailsClass() {
       // return mongoConnectionDetails.getClass().getName();
        return mongoConnectionDetails.getClass().getDeclaredFields()[0].getName();
    }


    public String getMongoProperties() {
        return environment.getProperty("spring.data.mongodb.uri")
                + " | "
                + environment.getProperty("spring.data.mongodb.database")
                + " | "
                + environment.getProperty("spring.mongodb.uri")
                + " | "
                + environment.getProperty("spring.mongodb.database");
    }

    public List<CourseMongoDocument> findAll() {
        return courseMongoRepository.findAll();
    }

    public CourseMongoDocument findById(String id) {
        return courseMongoRepository.findById(id)                .orElseThrow();
    }

    public CourseMongoDocument create(CourseMongoDocument course) {
        return courseMongoRepository.save(course);
    }

    public CourseMongoDocument update(String id, CourseMongoDocument course) {
        CourseMongoDocument existing = findById(id);

        existing.setName(course.getName());
        existing.setPrice(course.getPrice());
        existing.setDifficulty(course.getDifficulty());

        return courseMongoRepository.save(existing);
    }

    public void delete(String id) {
        courseMongoRepository.deleteById(id);
    }
}
