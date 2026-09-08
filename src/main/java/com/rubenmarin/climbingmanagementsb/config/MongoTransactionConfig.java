package com.rubenmarin.climbingmanagementsb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Configuration
public class MongoTransactionConfig {

    /* Configures MongoDB transaction management for Spring.
    *
    * MongoDB transactions require a transaction manager so that Spring can create and manage MongoDB sessions and transactions.
    *
    * MongoTransactionManager integrates MongoDB transactions with Spring's @Transactional annotation.
    */
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        /*
        * MongoDatabaseFactory provides access to the MongoDB database.
        *
        * MongoTransactionManager uses this factory to create and manage MongoDB transactions.
        */
        return new MongoTransactionManager(mongoDatabaseFactory);
    }
}
