package com.rubenmarin.climbingmanagementsb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// "Can Spring Boot start my application successfully?"
// When you create a Spring Boot project using the standard project generator, it commonly creates this test automatically.
// It's essentially a smoke test.

@SpringBootTest
@ActiveProfiles("test")
class ClimbingManagementSbApplicationTests {

    @Test
    void contextLoads() {
    }

}
