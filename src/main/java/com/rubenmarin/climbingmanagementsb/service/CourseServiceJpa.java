package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.entity.CourseEntity;
import com.rubenmarin.climbingmanagementsb.record.CourseRecord;
import com.rubenmarin.climbingmanagementsb.repository.CourseRepositoryJpa;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CourseServiceJpa {

    private final CourseRepositoryJpa courseRepositoryJpa;
    private final TransactionTestService transactionTestService;

    public CourseServiceJpa(
            CourseRepositoryJpa courseRepositoryJpa,
            TransactionTestService transactionTestService) {

        this.courseRepositoryJpa = courseRepositoryJpa;
        this.transactionTestService = transactionTestService;
    }


    /*
     * Entity → Record
     *
     * The entity is the JPA/database representation.
     * The record is the object exposed by the API.
     */
    private CourseRecord toRecord(CourseEntity entity) {
        return new CourseRecord(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getDifficulty()
        );
    }


    /*
     * Record → Entity
     *
     * Used when receiving data from the API and creating
     * a new persistent entity.
     */
    private CourseEntity toEntity(CourseRecord record) {
        return new CourseEntity(
                record.name(),
                record.price(),
                record.difficulty()
        );
    }


    public List<CourseRecord> findAll() {

        return courseRepositoryJpa.findAll()
                .stream()
                .map(this::toRecord)
                .toList();
    }


    public CourseRecord findById(Long id) {

        return courseRepositoryJpa.findById(id)
                .map(this::toRecord)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );
    }


    public CourseRecord create(CourseRecord courseRecord) {

        CourseEntity entity = toEntity(courseRecord);

        CourseEntity saved = courseRepositoryJpa.save(entity);

        return toRecord(saved);
    }


    /*
     * @Transactional starts a database transaction.
     *
     * The entity returned by findById() is managed by Hibernate
     * inside the Persistence Context.
     *
     * When we modify the entity, Hibernate detects the changes
     * automatically through Dirty Checking.
     *
     * At transaction commit, Hibernate generates the UPDATE SQL.
     *
     * Therefore, calling repository.save(existing) is not necessary
     * for an already managed entity.
     */
    @Transactional
    public CourseRecord update(Long id, CourseRecord courseRecord) {

        CourseEntity existing = courseRepositoryJpa.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );

        existing.setName(courseRecord.name());
        existing.setPrice(courseRecord.price());
        existing.setDifficulty(courseRecord.difficulty());

        return toRecord(existing);
    }


    public CourseRecord delete(Long id) {

        CourseEntity existing = courseRepositoryJpa.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );

        courseRepositoryJpa.delete(existing);

        return toRecord(existing);
    }


    /*
     * The repository method uses a Derived Query:
     *
     * findTopByOrderByPriceDesc()
     *
     * Spring Data JPA derives the query from the method name.
     *
     * Conceptually:
     *
     * SELECT *
     * FROM courses
     * ORDER BY price DESC
     * LIMIT 1
     */
    public CourseRecord findMostExpensive() {

        CourseEntity mostExpensive = courseRepositoryJpa
                .findTopByOrderByPriceDesc()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );

        return toRecord(mostExpensive);
    }


    /*
     * Derived Query:
     *
     * Spring Data JPA derives the SQL from the method name.
     */
    public List<CourseRecord> findByDifficulty(Difficulty difficulty) {

        return courseRepositoryJpa.findByDifficulty(difficulty)
                .stream()
                .map(this::toRecord)
                .toList();
    }


    /*
     * JPQL query defined with @Query in the repository.
     *
     * JPQL works with entities and their Java properties,
     * rather than directly with database tables and columns.
     */
    public List<CourseRecord> findByDifficultyAndPriceLessThan(
            Difficulty difficulty,
            Double price) {

        return courseRepositoryJpa
                .searchCourses(difficulty, price)
                .stream()
                .map(this::toRecord)
                .toList();
    }


    /*
     * TRANSACTIONAL ROLLBACK TEST
     *
     * @Transactional means both updates belong to the same transaction.
     *
     * If the RuntimeException is thrown, the transaction is rolled back
     * and neither change is persisted.
     *
     * By default, Spring rolls back transactions for unchecked exceptions
     * such as RuntimeException.
     */
    @Transactional
    public void testRequired(Long id1, Long id2) {

        CourseEntity course1 = courseRepositoryJpa.findById(id1)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course 1 not found"
                        )
                );

        CourseEntity course2 = courseRepositoryJpa.findById(id2)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course 2 not found"
                        )
                );

        course1.setPrice(111.0);
        course2.setPrice(222.0);

        throw new RuntimeException("Testing REQUIRED rollback");
    }


    /*
     * TRANSACTION PROPAGATION TEST
     *
     * operationA() starts a transaction.
     *
     * transactionTestService.operationB() is called from inside
     * that transaction. The behavior depends on the propagation
     * configured in operationB().
     *
     * This is used to study REQUIRED vs REQUIRES_NEW.
     */
    @Transactional
    public void operationA(Long id) {

        CourseEntity course = courseRepositoryJpa.findById(id)
                .orElseThrow();

        course.setName("CAMBIO DE A");

        transactionTestService.operationB(id);

        throw new RuntimeException("Rollback of A");
    }


    /*
     * CHECKED EXCEPTION TEST
     *
     * Spring's default rollback rules apply to unchecked exceptions
     * (RuntimeException and Error), but NOT normally to checked
     * exceptions.
     *
     * This method is used to demonstrate that difference.
     */
    public void testCheckedException(Long id) throws Exception {

        transactionTestService.testCheckedException(id);
    }


    /*
     * PESSIMISTIC LOCKING
     *
     * PESSIMISTIC_WRITE requests a database-level lock on the selected row.
     *
     * Another transaction attempting a conflicting lock on the same row
     * must wait until the current transaction completes.
     *
     * The lock is held for the duration of the transaction.
     *
     * Conceptually, PostgreSQL generates something similar to:
     *
     * SELECT ...
     * FROM courses
     * WHERE id = ?
     * FOR UPDATE;
     */
    @Transactional
    public CourseEntity findByIdWithLock(Long id) {

        return courseRepositoryJpa.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );
    }
}