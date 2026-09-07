package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.document.CourseMongoDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class CourseMongoCustomRepositoryImpl implements CourseMongoCustomRepository {

    private final MongoTemplate mongoTemplate;

    public CourseMongoCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<CourseMongoDocument> search(String name, Difficulty difficulty, Double minPrice, Double maxPrice, Pageable pageable) {
        Query query = new Query();

        if (name != null && !name.isBlank()) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }

        if (difficulty != null) {
            query.addCriteria(Criteria.where("difficulty").is(difficulty));
        }

        if (minPrice != null) {
            query.addCriteria(Criteria.where("price").gte(minPrice));
        }

        if (maxPrice != null) {
            query.addCriteria(Criteria.where("price").lte(maxPrice));
        }

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), CourseMongoDocument.class);

        /*
         * Apply pagination and sorting from Pageable to the MongoDB query.
         *
         * Pageable contains:
         * - page number / offset
         * - page size
         * - sorting information
         *
         * same as query.with(pageable);
         */

        query.skip(pageable.getOffset());
        query.limit(pageable.getPageSize());
        query.with(pageable.getSort());

        List<CourseMongoDocument> courses = mongoTemplate.find(query, CourseMongoDocument.class);

        return PageableExecutionUtils.getPage(courses, pageable, () -> total);
    }
}
