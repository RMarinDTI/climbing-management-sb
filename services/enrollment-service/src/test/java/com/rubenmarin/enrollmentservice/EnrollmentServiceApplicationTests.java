package com.rubenmarin.enrollmentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "course-service.base-url=http://localhost:8080"
})
class EnrollmentServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
