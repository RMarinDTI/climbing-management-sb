package com.rubenmarin.climbingmanagementsb.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


//"Quiero levantar la infraestructura MVC necesaria para probar CourseController, pero no toda la aplicación."
@WebMvcTest(GlobalController.class)
public class GlobalControllerTest {

    @Autowired
    MockMvc mockMvc;


    //En nuestros tests de Service hacíamos: when → assert → verify
    // Con MockMvc estamos haciendo: perform → andExpect → andExpect
    @Test
    void shouldReturnHello() throws Exception {

        // 3. Perform
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Climbing Management API"));

    }


}
