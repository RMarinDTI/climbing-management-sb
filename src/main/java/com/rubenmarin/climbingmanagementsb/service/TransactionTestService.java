
package com.rubenmarin.climbingmanagementsb.service;

import com.rubenmarin.climbingmanagementsb.entity.CourseEntity;
import com.rubenmarin.climbingmanagementsb.repository.CourseRepositoryJpa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionTestService {

    private final CourseRepositoryJpa courseRepositoryJpa;

    public TransactionTestService(CourseRepositoryJpa courseRepositoryJpa) {
        this.courseRepositoryJpa = courseRepositoryJpa;
    }


    /*
     * TRANSACTION PROPAGATION: REQUIRES_NEW
     *
     * REQUIRES_NEW always starts a completely new transaction.
     *
     * If operationB() is called from another transaction:
     *
     * 1. The existing transaction is suspended.
     * 2. A new transaction is started for operationB().
     * 3. operationB() executes independently.
     * 4. operationB() commits or rolls back.
     * 5. The original transaction is resumed.
     *
     * Therefore, if the outer transaction later rolls back, this change can still remain committed.
     *
     * This is different from REQUIRED, which would normally participate in the existing transaction.
     *
     * Important:
     * The transaction only works this way because operationB() is called through another Spring-managed service.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void operationB(Long id) {
        CourseEntity course = courseRepositoryJpa.findById(id).orElseThrow();
        course.setPrice(999.0);

        /*
         * No save() is required here.
         *
         * The entity is managed by Hibernate.
         * Dirty Checking detects the change and Hibernate generates the UPDATE when this transaction commits.
         */
    }


    /*
     * CHECKED EXCEPTION + ROLLBACK
     * By default, Spring rolls back transactions for unchecked exceptions such as RuntimeException.
     * Checked exceptions such as Exception do NOT normally trigger a rollback.
     * rollbackFor = Exception.class explicitly tells Spring:
     * "Roll back this transaction when an Exception is thrown, including checked exceptions."
     * Therefore, the price change to 666.0 is rolled back.
     */
    @Transactional(rollbackFor = Exception.class)
    public void testCheckedException(Long id) throws Exception {
        CourseEntity course = courseRepositoryJpa.findById(id).orElseThrow();
        course.setPrice(666.0);
        throw new Exception("Testing checked exception");
    }
}

