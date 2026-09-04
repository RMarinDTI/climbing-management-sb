package com.rubenmarin.climbingmanagementsb.repository;

import com.rubenmarin.climbingmanagementsb.Difficulty;
import com.rubenmarin.climbingmanagementsb.entity.CourseEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


/*
 * Spring Data JPA Repository
 *
 * By extending JpaRepository<CourseEntity, Long>, Spring Data automatically
 * provides common CRUD operations such as:
 *
 * findAll(), findById(), save(), deleteById(), existsById(), count(), etc.
 *
 * We only need to define methods for queries specific to our application.
 */
public interface CourseRepositoryJpa extends JpaRepository<CourseEntity, Long> {


    /*
     * DERIVED QUERY
     *
     * Spring Data JPA derives the query from the method name.
     *
     * findByDifficulty(...)
     *       ↓
     * looks for the "difficulty" property in CourseEntity
     *       ↓
     * generates the corresponding SQL automatically.
     *
     * Conceptually:
     *
     * SELECT *
     * FROM courses
     * WHERE difficulty = ?
     */
    List<CourseEntity> findByDifficulty(Difficulty difficulty);


    /*
     * DERIVED QUERY
     *
     * findTopByOrderByPriceDesc()
     *
     * Spring interprets:
     *   Top       → return the first result
     *   OrderBy   → sort by a property
     *   Price     → property to sort
     *   Desc      → descending order
     *
     * Conceptually:
     *
     * SELECT *
     * FROM courses
     * ORDER BY price DESC
     * LIMIT 1
     */
    Optional<CourseEntity> findTopByOrderByPriceDesc();


    /*
     * DERIVED QUERY
     *
     * Spring Data can combine multiple conditions directly
     * from the method name.
     *
     * Difficulty + And + Price + LessThan
     *
     * Conceptually:
     *
     * SELECT *
     * FROM courses
     * WHERE difficulty = ?
     * AND price < ?
     *
     * This method is kept as an example of a more complex
     * Derived Query.
     */
    List<CourseEntity> findByDifficultyAndPriceLessThan(
            Difficulty difficulty,
            Double price
    );


    /*
     * JPQL
     *
     * @Query allows us to explicitly define the query.
     *
     * JPQL works with ENTITY classes and their Java properties,
     * NOT directly with database table/column names.
     *
     * CourseEntity → entity
     * c.difficulty → Java entity property
     * c.price      → Java entity property
     *
     * This is different from native SQL.
     */
    @Query("""
            SELECT c
            FROM CourseEntity c
            WHERE c.difficulty = :difficulty
            AND c.price < :price
            """)
    List<CourseEntity> searchCourses(
            @Param("difficulty") Difficulty difficulty,
            @Param("price") Double price
    );


    /*
     * PESSIMISTIC LOCKING
     *
     * PESSIMISTIC_WRITE requests a database-level lock on the
     * selected entity.
     *
     * Another transaction attempting a conflicting operation
     * on the same row must wait until the current transaction
     * completes.
     *
     * The lock is held for the duration of the transaction.
     *
     * Hibernate/PostgreSQL generated SQL during our test:
     *
     * SELECT ...
     * FROM courses
     * WHERE id = ?
     * FOR NO KEY UPDATE;
     *
     * The exact SQL depends on the database and Hibernate dialect.
     *
     * Important:
     * The lock is held by the DATABASE TRANSACTION, not by Java
     * or by the repository method itself.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CourseEntity c WHERE c.id = :id")
    Optional<CourseEntity> findByIdForUpdate(@Param("id") Long id);
}